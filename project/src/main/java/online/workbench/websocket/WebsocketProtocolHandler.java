package online.workbench.websocket;


import com.google.gson.Gson;
import online.workbench.managers.BenchManager;
import online.workbench.managers.TokenManager;
import online.workbench.managers.UserManager;
import online.workbench.base.Protocol;
import online.workbench.model.struct.Bench;
import online.workbench.model.struct.PermissionLevel;
import online.workbench.model.struct.User;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

@WebSocket
public class WebsocketProtocolHandler
{
	private static Gson gson = new Gson();
	private static WorkbenchWS websocket;
	private static ArrayList<Session> pendingSessions = new ArrayList<>();
	private List<String> incomingValids = new ArrayList<>(Arrays.asList(
			"move", "resize", "textcursor", "textmodify", "textselect", "verify"

			//Chat is not handled by this handler class
	));

	private static BenchManager benchManager;
	private static UserManager userManager;
	private static TokenManager tokenManager;

	public static void WebsocketProtocolHandlerInitialize(WorkbenchWS websocket)
	{
		WebsocketProtocolHandler.websocket = websocket;
		benchManager = websocket.getApi().getBenchManager();
		userManager = websocket.getApi().getUserManager();
		tokenManager = websocket.getApi().getTokenManager();
	}

	@OnWebSocketConnect
	public void onConnect(Session session)
	{
		if (session.isOpen()) pendingSessions.add(session);
		session.setIdleTimeout(1000000000);
	}

	@OnWebSocketClose
	public void onClose(Session session, int statusCode, String reason)
	{
		if (pendingSessions.contains(session))
		{
			pendingSessions.remove(session);
		}
		else
		{
			for (HashMap<Session, User> benchKey : websocket.getSessions().values())
			{
				if (benchKey.containsKey(session))
				{
					benchKey.remove(session);
				}
			}
		}
	}

	@OnWebSocketMessage
	public void onMessage(Session session, String message)
	{

		try
		{
			session.getRemote().sendString("RESPONSE");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		JSONObject json = new JSONObject(message.trim());
		if (json.has("type") && incomingValids.contains(json.getString("type").toLowerCase())
				&& json.has("bench") && json.has("agent") && json.getJSONObject("agent").has("id")
				&& json.getJSONObject("agent").has("token") && json.getJSONObject("agent").getInt("id") != 0)
		{
			String type = json.getString("type").toLowerCase();
			Bench bench = benchManager.load(json.getInt("bench"));
			Protocol.ClientAgent agent = gson.fromJson(json.getJSONObject("agent").toString(), Protocol.ClientAgent.class);

			try
			{
				session.getRemote().sendString(websocket.getSessions().containsKey(bench) + "" + websocket.getSessions().get(bench).containsKey(session));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			if (tokenManager.check(agent.getId(), agent.getToken()) && bench.Users.containsKey(agent.getId()))
			{
				if (pendingSessions.contains(session))
				{
					try
					{
						session.getRemote().sendString("PENDING SESSION CONTAINS");
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					if (!type.equals("verify"))
					{
						session.close();
					}
					else
					{
						verify(session, bench, agent.getId());
					}
				}
				else if (websocket.getSessions().containsKey(bench) && websocket.getSessions().get(bench).containsKey(session))
				{
					if (bench.Users.get(agent.getId()).val() >= PermissionLevel.EDITOR.val())
					{
						User user = userManager.load(agent.getId());

						if (user != null)
						{
							evaluate(bench, user, message, type);
						}
					}
				}
				else
				{
					pendingSessions.add(session);
				}
			}
		}
	}

	private void verify(Session session, Bench bench, int userId)
	{
		User user = userManager.load(userId);

		if (user != null && user.Id != 0)
		{
			Map<Bench, HashMap<Session, User>> sessionMap = this.websocket.getSessions();

			if (!sessionMap.containsKey(bench))
			{
				benchManager.load(bench.Id);
			}

			HashMap<Session, User> sessions = sessionMap.get(bench);
			sessions.put(session, user);

			Protocol.VerifyOutgoing message = new Protocol.VerifyOutgoing();
			message.setBench(bench.Id);
			message.setRole(bench.Users.get(user.Id).toString());
			message.setType("verify");

			try
			{
				session.getRemote().sendString(gson.toJson(message, Protocol.VerifyOutgoing.class));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			if (pendingSessions.contains(session))
			{
				pendingSessions.remove(session);
			}

		}
	}

	private void move(Bench bench, User user, String json)
	{
		Protocol.ClientMove message = gson.fromJson(json, Protocol.ClientMove.class);
		int node = message.getNode();
		int x = message.getDimensions().getX();
		int y = message.getDimensions().getY();

		websocket.sendBenchNodeMove(bench, user, node, x, y);
	}

	private void resize(Bench bench, User user, String json)
	{
		Protocol.ClientResize message = gson.fromJson(json, Protocol.ClientResize.class);
		int node = message.getNode();
		int w = message.getDimensions().getW();
		int h = message.getDimensions().getH();

		websocket.sendBenchNodeResize(bench, user, node, w, h);
	}

	private void textcursor(Bench bench, User user, String json)
	{

	}

	private void textmodify(Bench bench, User user, String json)
	{

	}

	private void textselect(Bench bench, User user, String json)
	{

	}

	private void evaluate(Bench bench, User user, String message, String type)
	{
		switch(type)
		{
			case "move":
				move(bench, user, message);
				break;
			case "resize":
				resize(bench, user, message);
				break;
			case "textcursor":
				textcursor(bench, user, message);
				break;
			case "textmodify":
				textmodify(bench, user, message);
				break;
			case "textselect":
				textselect(bench, user, message);
				break;
			default:
		}
	}

}

