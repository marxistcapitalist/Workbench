package online.workbench.websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@ServerEndpoint(value = "/echo")
public class EchoWebSocketEndPoint
{
	private static List<Session> connectedSessions = new LinkedList<>();

	@OnOpen
	public void newConnection(final Session session)
	{
		if (!connectedSessions.contains(session))
		{
			connectedSessions.add(session);
			broadcastToAll("user" + session.getId() + " joined");
		}
		else {

		}

	}

	@OnMessage
	public String onMessage(final String message, final Session session)
	{
		System.out.println(message);
		return "Hello";

	}

	private void broadcastToAll(final String s) {
		for (Session session : connectedSessions) {
			try {
				session.getBasicRemote().sendText(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@OnClose
	public void closeConnectionHandler(Session session, CloseReason closeReason) {
		connectedSessions.remove(session);
		broadcastToAll("user" + session.getId() + " quit");
	}

}
