package online.workbench;

public class Workbench
{
	public static void main(String[] args)
	{
		WorkbenchDB database = new WorkbenchDB();
		WorkbenchAPI api = new WorkbenchAPI(database);
		WorkbenchWS websocket = new WorkbenchWS(api);


	}
}