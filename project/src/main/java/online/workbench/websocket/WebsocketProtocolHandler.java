package online.workbench.websocket;


import com.google.gson.Gson;
import online.workbench.WorkbenchWS;
import online.workbench.api.BenchManager;
import online.workbench.api.TokenManager;
import online.workbench.api.UserManager;
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
	private Gson gson;
	private WorkbenchWS websocket;
	private ArrayList<Session> pendingSessions;
	private List<String> incomingValids = new ArrayList<>(Arrays.asList(
			"move", "resize", "textcursor", "textmodify", "textselect", "verify"

			//Chat is not handled by this handler class
	));

	private BenchManager benchManager;
	private UserManager userManager;
	private TokenManager tokenManager;

	public WebsocketProtocolHandler(WorkbenchWS websocket)
	{
		this.gson = new Gson();
		this.websocket = websocket;
		this.pendingSessions = new ArrayList<>();
		this.benchManager = websocket.getApi().getBenchManager();
		this.userManager = websocket.getApi().getUserManager();
		this.tokenManager = websocket.getApi().getTokenManager();
	}

	@OnWebSocketConnect
	public void onConnect(Session session)
	{
		if (session.isOpen()) pendingSessions.add(session);
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
		JSONObject json = new JSONObject(message);
		if (json.has("type") && incomingValids.contains(json.getString("type").toLowerCase())
				&& json.has("bench") && json.has("agent") && json.getJSONObject("agent").has("id")
				&& json.getJSONObject("agent").has("token") && json.getJSONObject("agent").getInt("id") != 0)
		{
			String type = json.getString("type").toLowerCase();
			Bench bench = benchManager.load(json.getInt("bench"));
			Protocol.ClientAgent agent = gson.fromJson(json.getJSONObject("agent").toString(), Protocol.ClientAgent.class);

			if (tokenManager.check(agent.getId(), agent.getToken()) && bench.Users.containsKey(agent.getId()))
			{
				if (pendingSessions.contains(session))
				{
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

			if (sessionMap.containsKey(bench))
			{
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

