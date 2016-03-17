import online.workbench.WorkbenchAPI;
import online.workbench.data.WorkbenchDB;
import online.workbench.utils.HexSelector;
import online.workbench.websocket.WorkbenchWS;
import spark.Spark;

import java.util.ArrayList;
import java.util.Scanner;

import static spark.Spark.port;
import static spark.Spark.staticFileLocation;
import static spark.Spark.threadPool;

public class Main
{
	public static void main (String[] args)
	{
		ArrayList<String> used = new ArrayList<>();

		for (int i = 0; i < 10000; i++)
		{
			String val = HexSelector.sel();

			if (!used.contains(val))
			{
				used.add(val);
				System.out.println(val);
			}
		}
	}

}
