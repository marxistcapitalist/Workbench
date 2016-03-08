package online.workbench.websocket;


import online.workbench.websocket.data.DatabaseConnection;
import online.workbench.websocket.util.Logger;
import org.glassfish.tyrus.server.Server;

import javax.websocket.DeploymentException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BenchWebSocket
{
	private Server _server;
	private DatabaseConnection _database;

	public BenchWebSocket(DatabaseConnection database, int port)
	{
		_server = new Server("localhost", port, "/ws", null, EchoWebSocketEndPoint.class);
		_database = database;
	}

	public boolean start()
	{
		try
		{
			_server.start();
			Logger.success("WebSocket server successfully started");
			return true;
		}
		catch (DeploymentException e)
		{
			Logger.error("WebSocket server failed to start");
			e.printStackTrace();
			return false;
		}
	}

	public void startRoom()
	{

	}

	public void stopRoom()
	{

	}
}
