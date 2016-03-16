package online.workbench.websocket;

import com.google.gson.Gson;
import lombok.Getter;
import online.workbench.WorkbenchAPI;
import online.workbench.base.Protocol;
import online.workbench.base.WebsocketMethodsOutgoing;
import online.workbench.model.struct.*;
import online.workbench.websocket.WebsocketProtocolHandler;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class WorkbenchWS implements WebsocketMethodsOutgoing
{
    private @Getter WorkbenchAPI api;
	private @Getter Map<Bench, HashMap<Session, User>> sessions;
	private Gson gson;

	public WorkbenchWS(WorkbenchAPI api)
	{
		api.getBenchManager().setWebsocket(this);
		this.sessions = new HashMap<>();
		this.gson = new Gson();
		this.api = api;

		initialize();
		chat();
	}

	/**
	 * Initializes main protocol websocket
	 */
	private void initialize()
	{
		WebsocketProtocolHandler.WebsocketProtocolHandlerInitialize(this);
		webSocket(api.API + "ws", WebsocketProtocolHandler.class);
	}

	/**
	 * Initializes chat protocol websocket
	 */
	private void chat()
	{

	}

	@Override
	public void sendNotifyBench(Bench bench, String header, String text, String link, int time)
	{
		Protocol.Notify message = new Protocol.Notify();
		message.setType(Protocol.Out.NOTIFY);
		message.getData().setHeader(header);
		message.getData().setLink(link);
		message.getData().setText(text);
		message.getData().setTime(time);

		send(bench, message);
	}

	@Override
	public void sendUserAdd(Bench bench, User userAffected, PermissionLevel level)
	{
		Protocol.UserModifyOutgoing message = new Protocol.UserModifyOutgoing();
		message.setId(userAffected.Id);
		message.setUser(userAffected.Username);
		message.setAction("add");
		message.setBench(bench.Id);
		message.setLevel(level.toString());
		message.setType(Protocol.Out.MOD);

		send(bench, message);
	}

	@Override
	public void sendUserModify(Bench bench, User userAffected, PermissionLevel level)
	{
		Protocol.UserModifyOutgoing message = new Protocol.UserModifyOutgoing();
		message.setId(userAffected.Id);
		message.setUser(userAffected.Username);
		message.setAction("modify");
		message.setBench(bench.Id);
		message.setLevel(level.toString());
		message.setType(Protocol.Out.MOD);

		send(bench, message);
	}

	@Override
	public void sendUserRemove(Bench bench, User userAffected)
	{
		Protocol.UserModifyOutgoing message = new Protocol.UserModifyOutgoing();
		message.setId(userAffected.Id);
		message.setUser(userAffected.Username);
		message.setAction("remove");
		message.setBench(bench.Id);
		message.setLevel("");
		message.setType(Protocol.Out.MOD);

		send(bench, message);
	}

	@Override
	public void sendBenchNodeCreate(Bench bench, User userTrigger, BenchNode node)
	{
		Protocol.ServerCreate message = new Protocol.ServerCreate();
		message.setType(Protocol.Out.CREATE);
		message.setBench(bench.Id);
		message.getContent().setType(node.ContentType.toString());
		message.getContent().setData(node.Content);
		message.getContent().setTitle(node.Title);
		message.getDimensions().setH(node.Position.Height);
		message.getDimensions().setW(node.Position.Width);
		message.getDimensions().setX(node.Position.X);
		message.getDimensions().setY(node.Position.Y);
		message.setNode(node.Id);
		message.getAgent().setId(userTrigger.Id);
		message.getAgent().setName(userTrigger.Username);

		send(bench, message);
	}

	@Override
	public void sendBenchNodeDelete(Bench bench, User userTrigger, int node)
	{
		Protocol.WS message = new Protocol.WS();
		message.getAgent().setName(userTrigger.Username);
		message.getAgent().setId(userTrigger.Id);
		message.setNode(node);
		message.setBench(bench.Id);
		message.setType(Protocol.Out.DELETE);

		send(bench, message);
	}

	@Override
	public void sendBenchNodeEdit(Bench bench, User userTrigger, int node, String content, ContentType type)
	{
		Protocol.ServerEdit message = new Protocol.ServerEdit();
		message.setNode(node);
		message.setType(Protocol.Out.EDIT);
		message.setBench(bench.Id);
		message.getAgent().setId(userTrigger.Id);
		message.getAgent().setName(userTrigger.Username);
		message.getContent().setType(type.toString());
		message.getContent().setData(content);

		send(bench, message);
	}

	@Override
	public void sendBenchNodeMove(Bench bench, User userTrigger, int node, int x, int y)
	{
		Protocol.ServerMove message = new Protocol.ServerMove();
		message.getAgent().setName(userTrigger.Username);
		message.getAgent().setId(userTrigger.Id);
		message.setBench(bench.Id);
		message.setNode(node);
		message.setType(Protocol.Out.MOVE);
		message.getDimensions().setX(x);
		message.getDimensions().setY(y);

		send(bench, message);
	}

	@Override
	public void sendBenchNodeRename(Bench bench, User userTrigger, int node, String name)
	{
		Protocol.ServerRename message = new Protocol.ServerRename();
		message.setBench(bench.Id);
		message.setNode(node);
		message.setType(Protocol.Out.RENAME);
		message.getContent().setTitle(name);
		message.getAgent().setId(userTrigger.Id);
		message.getAgent().setName(userTrigger.Username);

		send(bench, message);
	}

	@Override
	public void sendBenchNodeResize(Bench bench, User userTrigger, int node, int w, int h)
	{
		Protocol.ServerResize message = new Protocol.ServerResize();
		message.setBench(bench.Id);
		message.setNode(node);
		message.setType(Protocol.Out.RESIZE);
		message.getAgent().setId(userTrigger.Id);
		message.getAgent().setName(userTrigger.Username);
		message.getDimensions().setH(h);
		message.getDimensions().setW(w);

		send(bench, message);
	}

	private void send(Bench bench, Object message)
	{
		String out = gson.toJson(message);

		if (sessions.containsKey(bench))
		{
			sessions.get(bench).keySet().forEach(s ->
			{
				if (s.isOpen())
				{
					try
					{
						s.getRemote().sendString(out);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * This is needed since only one websocket is being spawned to coordinate all benches.
	 * The one websocket implementation is a product of running this software example
	 * as once instance on one low power machine. In deployment, a new websocket would
	 * be spawned for each active bench and then afterwards removed.
	 */
	public void addBenchToSessions(Bench bench)
	{
		if (!sessions.containsKey(bench))
		{
			sessions.put(bench, new HashMap<Session, User>());
		}
	}

	public void removeBenchFromSessions(Bench bench)
	{
		if (sessions.containsKey(bench))
		{
			for (Map.Entry<Session, User> item : sessions.get(bench).entrySet())
			{
				if (item.getKey().isOpen())
				{
					item.getKey().close();
				}
			}
			sessions.remove(bench);
		}
	}
}
