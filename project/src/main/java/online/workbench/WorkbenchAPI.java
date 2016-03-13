package online.workbench;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import online.workbench.api.Protocol;

import online.workbench.api.UserManager;
import online.workbench.websocket.data.struct.BenchData;
import online.workbench.websocket.data.struct.User;
import static spark.Spark.*;

public class WorkbenchAPI
{
	private WorkbenchDB database;
	private @Setter WorkbenchWS websocket;
	private @Getter UserManager userManager;
	private Gson gson;

	private final String API = "api/";

	public WorkbenchAPI(WorkbenchDB database)
	{
		this.userManager = new UserManager();
		this.database = database;
		this.gson = new Gson();
		initialize();
	}

	private void initialize()
	{
		ping();
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

	////////////// ENDPOINTS //////////////

	private void ping()
	{
		try
		{
			String ret = gson.toJson(Protocol.HTTP_SERVER_PING.getClass().newInstance());
			get(API + "ping", (req, res) -> ret);
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	private void login()
	{
		get(API + "login", (req, res) ->
		{
			String body = req.body();
			Protocol.ClientLogin obj = gson.fromJson(body, Protocol.ClientLogin.class);
			String login = obj.getLoginkey();
			String password = obj.getPassword();

			int id;
			if (login.contains("@"))
			{
				id = this.database.validateUserEmail(login, password);
			}
			else
			{
				id = this.database.validateUserName(login, password);
			}

			if (id != 0)
			{
				User user = this.database.loadUser(id);
				userManager.put(user);
			}


		});
	}


}
