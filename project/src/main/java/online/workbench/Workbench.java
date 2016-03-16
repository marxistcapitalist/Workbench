package online.workbench;

import online.workbench.data.WorkbenchDB;
import online.workbench.websocket.WorkbenchWS;
import spark.Spark;

import static spark.Spark.*;

public class Workbench
{
	public static void main(String[] args)
	{

		System.console().writer().println("===================================================");
		System.console().writer().println("==              Launching Workbench              ==");
		System.console().writer().println("===================================================");
		port(80);
		System.console().writer().println("==                                               ==");
		System.console().writer().println("== • Port Set: 80                                ==");
		threadPool(30);
		System.console().writer().println("==                                               ==");
		System.console().writer().println("== • Thread Pool Created: 30                     ==");
		staticFileLocation("/public");
		System.console().writer().println("==                                               ==");
		System.console().writer().println("== • Static Content Location Set: /public        ==");
		System.console().writer().println("==                                               ==");
		System.console().writer().println("===================================================");
		WorkbenchDB database = new WorkbenchDB("workbench");
		System.console().writer().println("==                                               ==");
		System.console().writer().println("== • Database Initialized (workbench)            ==");
		WorkbenchAPI api = new WorkbenchAPI(database);
		System.console().writer().println("==                                               ==");
		System.console().writer().println("== • API Initialized (/api)                      ==");
		System.console().writer().println("==                                               ==");
		WorkbenchWS websocket = new WorkbenchWS(api);
		System.console().writer().println("== • WebSocket Initialized (/api/ws)             ==");
		System.console().writer().println("==                                               ==");
		System.console().writer().println("===================================================");
		System.console().writer().println("==                                               ==");
		System.console().writer().println("== • Server Startup Complete!                    ==");
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
				System.console().readLine();
				Spark.stop();
			}
		}.start();
	}
}