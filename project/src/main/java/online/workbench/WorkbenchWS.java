package online.workbench;

import online.workbench.base.WebsocketMethodsOutgoing;
import online.workbench.model.struct.Bench;
import online.workbench.model.struct.ContentType;
import online.workbench.model.struct.Node;
import online.workbench.model.struct.User;

public class WorkbenchWS implements WebsocketMethodsOutgoing
{
	private WorkbenchAPI api;

	public WorkbenchWS(WorkbenchAPI api)
	{
		api.getBenchManager().setWebsocket(this);
		this.api = api;
	}

}
