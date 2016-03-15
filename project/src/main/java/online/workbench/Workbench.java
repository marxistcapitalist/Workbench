package online.workbench;

import online.workbench.data.WorkbenchDB;
import online.workbench.websocket.WorkbenchWS;

public class Workbench
{
	public static void main(String[] args)
	{
		WorkbenchDB database = new WorkbenchDB("workbench");
		WorkbenchAPI api = new WorkbenchAPI(database);
		WorkbenchWS websocket = new WorkbenchWS(api);

	}
}