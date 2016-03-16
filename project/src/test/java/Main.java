import online.workbench.WorkbenchAPI;
import online.workbench.data.WorkbenchDB;
import online.workbench.websocket.WorkbenchWS;
import spark.Spark;

import java.util.Scanner;

import static spark.Spark.port;
import static spark.Spark.staticFileLocation;
import static spark.Spark.threadPool;

public class Main
{
	public static void main(String[] args)
	{
		System.out.println("===================================================");
		System.out.println("==              Launching Workbench              ==");
		System.out.println("===================================================");
		port(4565);
		System.out.println("==                                               ==");
		System.out.println("== • Port Set: 80                                ==");
		threadPool(30);
		System.out.println("==                                               ==");
		System.out.println("== • Thread Pool Created: 30                     ==");
		staticFileLocation("/public");
		System.out.println("==                                               ==");
		System.out.println("== • Static Content Location Set: /public        ==");
		System.out.println("==                                               ==");
		System.out.println("===================================================");
		WorkbenchDB database = new WorkbenchDB("workbench");
		System.out.println("==                                               ==");
		System.out.println("== • Database Initialized (workbench)            ==");
		WorkbenchAPI api = new WorkbenchAPI(database);
		WorkbenchWS websocket = new WorkbenchWS(api);
		System.out.println("==                                               ==");
		System.out.println("== • WebSocket Initialized (/api/ws)             ==");
		api.initialize();
		System.out.println("==                                               ==");
		System.out.println("== • API Initialized (/api)                      ==");
		System.out.println("==                                               ==");
		System.out.println("===================================================");
		System.out.println("==                                               ==");
		System.out.println("== • Server Startup Complete!                    ==");
		System.out.println("==                                               ==");
		System.out.println("== • Type 'STOP' to stop the server              ==");
		System.out.println("==                                               ==");
		System.out.println("===================================================");
		System.out.println("===================================================");

		new Thread()
		{
			@Override
			public void run()
			{
				Scanner scan = new Scanner(System.in);
				scan.nextLine();
				Spark.stop();
			}
		}.start();
	}
}
