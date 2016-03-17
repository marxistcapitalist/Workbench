package online.workbench;

import online.workbench.data.WorkbenchDB;
import online.workbench.websocket.WorkbenchWS;
import spark.Spark;

import java.util.Scanner;

import static spark.Spark.*;

public class Workbench
{
	public static final String DATABASE = "workbench";
	public static final String STATIC = "/public";

	public static void main(String[] args)
	{
		int port = 80;
		int pool = 20;

		if (args.length == 2)
		{
			port = Integer.valueOf(args[0]);
			pool = Integer.valueOf(args[1]);
		}

		System.console().writer().println("===================================================");
		System.console().writer().println("==              Launching Workbench              ==");
		System.console().writer().println("===================================================");
		port(port);
		System.console().writer().println("==                                               ==");
		System.console().writer().println("== • Port Set: "+port+"                                ==");
		threadPool(pool);
		System.console().writer().println("==                                               ==");
		System.console().writer().println("== • Thread Pool Created: "+pool+"                     ==");
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