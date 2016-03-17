package online.workbench;

import online.workbench.data.WorkbenchDB;
import online.workbench.websocket.WorkbenchWS;
import spark.Spark;

import java.util.Scanner;

import static spark.Spark.*;

public class Workbench
{
	public static final int PORT = 80;
	public static final int POOL = 20;
	public static final String DATABASE = "workbench";
	public static final String STATIC = "/public";

	public static void main(String[] args)
	{
		System.console().writer().println("===================================================");
		System.console().writer().println("==              Launching Workbench              ==");
		System.console().writer().println("===================================================");
		port(PORT);
		System.console().writer().println("==                                               ==");
		System.console().writer().println("== • Port Set: "+PORT+"                                ==");
		threadPool(POOL);
		System.console().writer().println("==                                               ==");
		System.console().writer().println("== • Thread Pool Created: "+POOL+"                     ==");
		staticFileLocation(STATIC);
		System.console().writer().println("==                                               ==");
		System.console().writer().println("== • Static Content Location Set: "+STATIC+"        ==");
		System.console().writer().println("==                                               ==");
		System.console().writer().println("===================================================");
		WorkbenchDB database = new WorkbenchDB(DATABASE);
		System.console().writer().println("==                                               ==");
		System.console().writer().println("== • Database Initialized ("+DATABASE+")            ==");
		WorkbenchAPI api = new WorkbenchAPI(database);
		WorkbenchWS websocket = new WorkbenchWS(api);
		System.console().writer().println("==                                               ==");
		System.console().writer().println("== • WebSocket Initialized (/api/ws)             ==");
		api.initialize();
		System.console().writer().println("==                                               ==");
		System.console().writer().println("== • API Initialized (/api)                      ==");
		System.console().writer().println("==                                               ==");
		System.console().writer().println("===================================================");
		System.console().writer().println("==                                               ==");
		System.console().writer().println("== • Server Startup Complete!                    ==");
		System.console().writer().println("==                                               ==");
		System.console().writer().println("== • Server Running @ http://localhost           ==");
		System.console().writer().println("==                                               ==");
		System.console().writer().println("== • Type 'STOP' to stop the server              ==");
		System.console().writer().println("==                                               ==");
		System.console().writer().println("===================================================");
		System.console().writer().println("===================================================");

		new Thread()
		{
			@Override
			public void run()
			{
				String input = "";
				while (!input.equalsIgnoreCase("stop"))
				{
					input = System.console().readLine().trim();
				}
				Spark.stop();
			}
		}.start();
	}
}