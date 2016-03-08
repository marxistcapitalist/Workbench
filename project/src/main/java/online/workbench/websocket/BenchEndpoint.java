package online.workbench.websocket;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;

public class BenchEndpoint extends Endpoint
{
	private ArrayList<Session> _sessions;
	private int _roomId;

	public BenchEndpoint(int roomId)
	{
		_sessions = new ArrayList<Session>();
		_roomId = roomId;
	}

	@Override
	public void onOpen(final Session session, final EndpointConfig config)
	{
		if (true) //room allow validation
		{
			_sessions.add(session);
			session.addMessageHandler(new MessageHandler.Whole<String>()
			{
				@Override
				public void onMessage(String message)
				{
					handleMessage(session, message);
				}
			});
		}
		else
		{
			try
			{
				session.close();
			}
			catch (IOException e)
			{

			}
		}
	}

	private void handleMessage(final Session session, String message)
	{

	}
}
