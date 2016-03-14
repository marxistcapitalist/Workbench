package online.workbench;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import online.workbench.api.BenchManager;
import online.workbench.api.Protocol;

import online.workbench.api.UserManager;
import online.workbench.model.struct.BenchData;
import online.workbench.model.struct.User;
import online.workbench.api.Protocol.*;

import java.util.ArrayList;

import static spark.Spark.*;

public class WorkbenchAPI
{
	private WorkbenchDB database;
	private @Setter WorkbenchWS websocket;
	private @Getter UserManager userManager;
	private @Getter BenchManager benchManager;
	private Gson gson;

	private final String API = "api/";
	private static final char[] disallowed = {'\\', '\'', '\"', ';', '&', '@', '#', '/', '$', ':', '%', '^', '*', '<', '>', '{', '}', '[', ']', '(', ')'};

	public WorkbenchAPI(WorkbenchDB database)
	{
		this.userManager = new UserManager(database);
		this.benchManager = new BenchManager(database);
		this.database = database;
		this.gson = new Gson();
		initialize();
	}

	private void initialize()
	{
		//GENERAL
		ping();

		//ACCOUNTS
		login();
		register();
		authenticate();
		logout();

		//BENCH - CORE


		//BENCH - MISC


		//BENCHNODE - CORE


		//BENCHNODE - MISC


		//USERNODE

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
				User user = this.userManager.load(id);

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
						response.setError("Username cannot contain \"" + disallowed[c] + "\"");
						return gson.toJson(response);
					}
				}
			}

			if(!this.database.checkUsernameAvailability(username))
			{
				response.setSuccess(false);
				response.setError("Username is not available :(");
				return gson.toJson(response);
			}

			int id = this.database.createUser(username, email, password);
			String token = this.database.issueToken(id);

			this.userManager.load(id);

			response.setSuccess(true);
			response.getAgent().setUser(username);
			response.getAgent().setId(id);
			response.setToken(token);
			return gson.toJson(response);
		});
	}

	public void authenticate()
	{
		post(API + "authenticate", (req, res) ->
		{
			Authenticate body = gson.fromJson(req.body(), Authenticate.class);

			if (!this.database.checkToken(body.getId(), body.getToken()))
			{
				return "{}";
			}

			Authenticate response = new Authenticate();
			response.setToken(body.getToken());
			response.setId(body.getId());

			return gson.toJson(response);
		});
	}

	public void logout()
	{
		post(API + "logout", (req, res) ->
		{
			ClientToken body = gson.fromJson(req.body(), ClientToken.class);
			this.database.invalidateToken(body.getToken());
			return "{}";
		});
	}

	public void user()
	{
		post(API + "user", (req, res) ->
		{
			ClientUserData body = gson.fromJson(req.body(), ClientUserData.class);
			int id = body.getAgent().getId();
			User user = userManager.load(id);

			if (user != null)
			{
				String avatar = "static/avatar/" + id + ".png";
				String token = body.getAgent().getToken();

				if (token.isEmpty())
				{
					ServerUserDataUnauthenticated response = new ServerUserDataUnauthenticated();
					response.setAvatar(avatar);
					response.setUser(user.Username);
					response.setId(user.Id);
					return gson.toJson(response);
				}
				else
				{
					if (this.database.checkToken(id, token))
					{
						ServerUserDataAuthenticated response = new ServerUserDataAuthenticated();
						response.setId(user.Id);
						response.setUser(user.Username);
						response.setAvatar(avatar);
						response.setEmail(user.Email);

						ArrayList<ServerUserDataAuthenticated.MemberNode> member = new ArrayList<>();
						ArrayList<ServerUserDataAuthenticated.OwnerNode> owner = new ArrayList<>();

						for (BenchData bench : user.Benches)
						{
							if (bench.OwnerId == user.Id)
							{
								ServerUserDataAuthenticated.OwnerNode o = new ServerUserDataAuthenticated.OwnerNode();
								o.setId(bench.Id);
								o.setPreview(bench.PreviewImagePath);
								o.setTitle(bench.Title);
								o.setUsers(benchManager.countBenchUsers(bench.Id));
								owner.add(o);
							}
							else
							{
								ServerUserDataAuthenticated.MemberNode m = new ServerUserDataAuthenticated.MemberNode();
								m.setId(bench.Id);
								m.setTitle(bench.Title);
								m.setPreview(bench.PreviewImagePath);
								m.setOwner(bench.Owner);
								member.add(m);
							}
						}

						response.setOwner(owner);
						response.setMember(member);
						return gson.toJson(response);
					}
				}
			}
			return "{}";
		});
	}

	public void copy()
	{

	}

	public void create()
	{

	}

	public void delete()
	{

	}

	public void edit()
	{

	}

	public void move()
	{
		
	}

}
