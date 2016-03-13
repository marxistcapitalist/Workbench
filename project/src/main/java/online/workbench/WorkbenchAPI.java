package online.workbench;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import online.workbench.api.Protocol;

import online.workbench.api.UserManager;
import online.workbench.model.struct.BenchData;
import online.workbench.model.struct.User;
import online.workbench.api.Protocol.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static spark.Spark.*;

public class WorkbenchAPI
{
	private WorkbenchDB database;
	private @Setter WorkbenchWS websocket;
	private @Getter UserManager userManager;
	private Gson gson;

	private final String API = "api/";
	private static final char[] disallowed = {'\\', '\'', '\"', ';', '&', '@', '#', '/', '$', ':', '%', '^', '*', '<', '>', '{', '}', '[', ']', '(', ')'};

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
		login();
	}

	public void createUserEndpoint(User user)
	{

	}

	public void deleteUserEndpoint(int userId)
	{

	}

	public void createBenchEndpoint(BenchData bench)
	{

	}

	public void deleteBenchEndpoint(int benchId)
	{

	}

	////////////// ENDPOINTS //////////////

	private void ping()
	{
		post(API + "ping", (req, res) -> "{}");
	}

	private void login()
	{
		post(API + "login", (req, res) ->
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

				ServerLogin response = new ServerLogin();
				response.getAgent().setId(user.Id);
				response.getAgent().setUser(user.Username);
				String token = database.issueToken(user.Id);
				response.setToken(token);

				return gson.toJson(response);
			}
			return "{}";
		});
	}

	private void register()
	{
		post(API + "register", (req, res) ->
		{
			Register body = gson.fromJson(req.body(), Register.class);
			String email = body.getEmail();
			String username = body.getUsername();
			String password = body.getPassword();

			ServerRegister response = new ServerRegister();

			if (email.length() < 6 && email.length() > 254)
			{
				response.setSuccess(false);
				response.setError("Email must be between 6 and 254 characters long");
				return gson.toJson(response);
			}

			int count = 0;
			char[] emailArray = email.toCharArray();
			for (int i = 0; i < email.length(); i++) if (emailArray[i] == '@') count++;
			if (!email.contains(".") || count != 1)
			{
				response.setSuccess(false);
				response.setError("Email is not valid");
				return gson.toJson(response);
			}

			if (username.length() > 32 || username.length() < 1)
			{
				response.setSuccess(false);
				response.setError("Username must be between 1 and 32 characters long");
				return gson.toJson(response);
			}

			if (password.length() > 256 || password.length() < 7)
			{
				response.setSuccess(false);
				response.setError("Password must be between 7 and 256 characters long");
				return gson.toJson(response);
			}

			char[] usernameArray = username.toCharArray();

			for (int i = 0; i < usernameArray.length; i++)
			{
				for (int c = 0; c < disallowed.length; c++)
				{
					if (usernameArray[i] == disallowed[c])
					{
						response.setSuccess(false);
						response.setError("Username cannot contain \"" + disallowed[c]);
						return gson.toJson(response);
					}
				}
			}

			this.database,.)

			response.setSuccess(true);
			response.getAgent().set


		});
	}


}
