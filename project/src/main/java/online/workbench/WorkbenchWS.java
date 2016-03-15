package online.workbench;

import online.workbench.base.WebsocketMethodsOutgoing;
import online.workbench.model.struct.*;
import online.workbench.websocket.WebsocketProtocolHandler;

import static spark.Spark.*;

public class WorkbenchWS implements WebsocketMethodsOutgoing
{
	private WorkbenchAPI api;

	public WorkbenchWS(WorkbenchAPI api)
	{
		api.getBenchManager().setWebsocket(this);
		this.api = api;

		initialize();
		chat();
	}

	private void initialize()
	{
		webSocket(api.API + "websocket", WebsocketProtocolHandler.class);
		init();
	}

	private void chat()
	{

	}

	@Override
	public void sendChat(Bench bench, User user, String message)
	{

	}

	@Override
	public void sendNotifyUser(User user, String header, String text, String link, int time)
	{

	}

	@Override
	public void sendNotifyBench(Bench bench, String header, String text, String link, int time)
	{

	}

	@Override
	public void sendNotifyGlobal(String header, String text, String link, int time)
	{

	}

	@Override
	public void sendUserAdd(Bench bench, User user, PermissionLevel level)
	{

	}

	@Override
	public void sendUserModify(Bench bench, User user, PermissionLevel level)
	{

	}

	@Override
	public void sendUserRemove(Bench bench, User user)
	{

	}

	@Override
	public void sendBenchNodeCreate(Bench bench, User user, BenchNode node)
	{

	}

	@Override
	public void sendBenchNodeDelete(Bench bench, User user, int node)
	{

	}

	@Override
	public void sendBenchNodeEdit(Bench bench, User user, int node, String content)
	{

	}

	@Override
	public void sendBenchNodeMove(Bench bench, User user, int node, int x, int y)
	{

	}

	@Override
	public void sendBenchNodeRename(Bench bench, User user, int node, String name)
	{

	}

	@Override
	public void sendBenchNodeResize(Bench bench, User user, int node, int w, int h)
	{

	}

	@Override
	public void sendBenchNodeTypeEdit(Bench bench, User user, int node, ContentType type)
	{

	}
}
