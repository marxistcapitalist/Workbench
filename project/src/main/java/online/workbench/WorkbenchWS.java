package online.workbench;

public class WorkbenchWS
{
	private WorkbenchAPI api;

	public WorkbenchWS(WorkbenchAPI api)
	{
		api.setWebsocket(this);
		this.api = api;
	}

}
