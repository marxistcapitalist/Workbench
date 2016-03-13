package online.workbench;

import lombok.Data;
import lombok.Setter;
import online.workbench.base.API;
import online.workbench.websocket.data.struct.BenchData;
import online.workbench.websocket.data.struct.User;
import spark.Spark.*;

public class WorkbenchAPI
{
	private WorkbenchDB database;
	private @Setter WorkbenchWS websocket;


	public WorkbenchAPI(WorkbenchDB database)
	{
		this.database = database;
	}

	public void createUserEndpoint(User user, API api)
	{

	}

	public void deleteUserEndpoint(int userId)
	{

	}

	public void createBenchEndpoint(BenchData bench, API api)
	{

	}

	public void deleteBenchEndpoint(int benchId)
	{

	}
}
