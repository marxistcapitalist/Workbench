package online.workbench;

import com.google.gson.Gson;
import lombok.Getter;
import online.workbench.managers.BenchManager;

import online.workbench.managers.TokenManager;
import online.workbench.managers.UserManager;
import online.workbench.data.WorkbenchDB;
import online.workbench.model.struct.*;
import online.workbench.utils.TimeConverter;
import static online.workbench.base.Protocol.*;

import java.io.Console;
import java.util.ArrayList;
import java.util.Map;

import static spark.Spark.*;

public class WorkbenchAPI
{
	private WorkbenchDB database;
	private @Getter UserManager userManager;
	private @Getter BenchManager benchManager;
	private @Getter TokenManager tokenManager;
	private Gson gson;

	public final String API = "/api/";
	private static final char[] disallowed = {'\\', '\'', '\"', ';', '&', '@', '#', '/', '$', ':', '%', '^', '*', '<', '>', '{', '}', '[', ']', '(', ')'};

	public WorkbenchAPI(WorkbenchDB database)
	{
		this.userManager = new UserManager(database);
		this.benchManager = new BenchManager(database, userManager);
		this.tokenManager = new TokenManager(database);
		this.database = database;
		this.gson = new Gson();
	}

	public void initialize()
	{
		////////// GENERAL ////////////
		ping();
		ping2();

		////////// ACCOUNTS ////////////
		login();
		register();
		authenticate();
		logout();
		useredit();
		user();

		///////// BENCH - CORE ////////////
		bench();
		create();
		deleteBench();
		edit();

		///////// BENCH - USERS ///////////////
		adduser();
		moduser();
		removeuser();

		////////// BENCHNODE - CORE ////////////
		//copyBenchNode();
		createBenchNode();
		deleteBenchNode();
		editBenchNode();
		moveBenchNode();
		resizeBenchNode();
		renameBenchNode();
		//spawnBenchNode();

		/////////// BENCHNODE - MISC ///////////
		//ignoreBenchNode();
		//watchBenchNode();

		////////// USERNODE /////////////
		//createUserNode();
		//deleteUserNode();
		//editUserNode();
		//renameUserNode();
	}



	///////////// GENERAL ////////////////

	private void ping()
	{
		post(API + "ping", (req, res) -> "pong!");
	}

	private void ping2()
	{
		get("/ping", (req, res) -> "pong!");
	}

	//////////// ACCOUNTS ///////////////

	private void login()
	{
		post(API + "login", (req, res) ->
		{
			try
			{
				String body = req.body();
				ClientLogin obj = gson.fromJson(body, ClientLogin.class);
				String login = obj.getLoginkey().toLowerCase();
				String password = obj.getPassword();

				int id = this.userManager.validateUser(login, password);

				if (id != 0)
				{
					User user = this.userManager.load(id);

					if (user == null)
					{
						res.status(401);
						return "";
					}

					ServerLogin response = new ServerLogin();
					response.getAgent().setId(user.Id);
					response.getAgent().setUser(user.Username);
					String token = tokenManager.issue(id);
					response.setToken(token);

					return gson.toJson(response);
				}
				res.status(403);
				return "";
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}
		});
	}

	private void register()
	{
		post(API + "register", (req, res) ->
		{

			try
			{
				Register body = gson.fromJson(req.body(), Register.class);
				String email = body.getEmail().toLowerCase();
				String username = body.getUsername().toLowerCase();
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

				if (!this.database.checkUsernameAvailability(username))
				{
					response.setSuccess(false);
					response.setError("Username is not available :(");
					return gson.toJson(response);
				}

				if (!this.database.checkEmailAvailability(email))
				{
					response.setSuccess(false);
					response.setError("Email is not available :(");
					return gson.toJson(response);
				}

				User user = userManager.createUser(username, password, email);
				String token = tokenManager.issue(user.Id);

				response.setSuccess(true);
				response.getAgent().setUser(user.Username);
				response.getAgent().setId(user.Id);
				response.setToken(token);
				return gson.toJson(response);
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}
		});
	}

	public void authenticate()
	{
		post(API + "auth", (req, res) ->
		{

			try
			{
				Authenticate body = gson.fromJson(req.body(), Authenticate.class);

				if (!tokenManager.check(body.getAgent().getId(), body.getAgent().getToken()))
				{
					res.status(401);
					return "";
				}

				ServerLogin response = new ServerLogin();
				response.getAgent().setId(body.getAgent().getId());
				response.getAgent().setUser(userManager.load(body.getAgent().getId()).Username);
				response.setToken(body.getAgent().getToken());

				res.status(200);
				return gson.toJson(response);
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}
		});
	}

	public void logout()
	{
		post(API + "logout", (req, res) ->
		{

			try
			{
				ClientToken body = gson.fromJson(req.body(), ClientToken.class);
				this.database.invalidateToken(body.getToken());
				tokenManager.invalidate(body.getToken());
				res.status(200);
				return "";
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}
		});
	}

	public void user()
	{
		post(API + "user/:userId", (req, res) ->
		{
			int requestedUser;

			try
			{
				requestedUser = Integer.valueOf(req.params(":userId"));
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}

			try
			{
				ClientUserData body = gson.fromJson(req.body(), ClientUserData.class);
				int id = body.getAgent().getId();

				if (requestedUser != id || !tokenManager.check(id, body.getAgent().getToken()))
				{
					throw new Exception();
				}

				User user = userManager.load(requestedUser);

				if (user != null)
				{
					String avatar = user.Avatar;

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
					response.setAvatar(avatar);
					res.status(200);
					return gson.toJson(response);
				}

				res.status(400);
				return "";
			}
			catch (Exception e)
			{
				User user = userManager.load(requestedUser);

				if (user != null)
				{
					ServerUserDataUnauthenticated response = new ServerUserDataUnauthenticated();
					response.setAvatar(user.Avatar);
					response.setUser(user.Username);
					response.setId(user.Id);
					res.status(200);
					return gson.toJson(response);
				}

				res.status(404);
				return "";
			}
		});
	}

	public void useredit()
	{
		put(API + "user/:userId", (req, res) ->
		{
			try
			{
				UserEditRequest body = gson.fromJson(req.body(), UserEditRequest.class);

				if (tokenManager.check(body.getAgent().getId(), body.getAgent().getToken()))
				{
					User user = userManager.load(body.getAgent().getId());

					String username = body.getUsername();
					String email = body.getEmail();
					String avatar = body.getAvatar();
					String password = body.getPassword();

					UserEditResponse response = new UserEditResponse();

					int count = 0;
					char[] emailArray = email.toCharArray();
					for (int i = 0; i < email.length(); i++) if (emailArray[i] == '@') count++;
					if (!email.contains(".") || count != 1)
					{
						response.setEmail(false);
					} else
					{
						response.setEmail(userManager.updateEmail(user, email.toLowerCase()));
					}

					if (username.length() > 32 || username.length() < 1)
					{
						response.setUsername(false);
					} else
					{
						response.setUsername(userManager.updateUsername(user, username.toLowerCase()));
					}

					if (password.length() > 256 || password.length() < 7)
					{
						response.setPassword(false);
					} else
					{
						response.setPassword(userManager.updatePassword(userManager.load(user.Id), password));
					}

					if (!avatar.isEmpty()) response.setAvatar(userManager.updateAvatar(user, avatar));

					return gson.toJson(response);
				}
				res.status(403);
				return "";
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}
		});
	}

	/////////////// BENCH - CORE //////////////////

	public void bench()
	{
		post(API + "bench/:benchId", (req, res) ->
		{
			int requestedBench;

			try
			{
				requestedBench = Integer.valueOf(req.params(":benchId"));
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}

			try
			{
				BenchInfoRequest request = gson.fromJson(req.body(), BenchInfoRequest.class);

				if (tokenManager.check(request.getAgent().getId(), request.getAgent().getToken()))
				{
					Bench bench = benchManager.load(requestedBench);

					if (bench == null)
					{
						res.status(404);
						return "";
					}

					if (bench.Users.containsKey(request.getAgent().getId()))
					{
						switch (request.getVerbosity().toLowerCase())
						{
							case "high":
							{
								BenchInfoResponseHigh response = new BenchInfoResponseHigh();
								response.getDimensions().setWidth(bench.Dimensions.Width);
								response.getDimensions().setHeight(bench.Dimensions.Height);
								response.setId(bench.Id);
								response.setBackground(bench.Background);
								response.setCreated(TimeConverter.get(bench.Created));
								response.getOwner().setId(bench.Owner.Id);
								response.getOwner().setUser(bench.Owner.Username);
								response.setPreview(bench.Preview);
								response.setTitle(bench.Title);

								ArrayList<BenchInfoResponseHigh.BenchInfoResponseNodeObjectVerbose> nodes = new ArrayList<>();
								for (BenchNode node : bench.Nodes.values())
								{
									BenchInfoResponseHigh.BenchInfoResponseNodeObjectVerbose object = new BenchInfoResponseHigh.BenchInfoResponseNodeObjectVerbose();
									object.setId(node.Id);
									object.setTitle(node.Title);
									object.setLastUpdate(TimeConverter.get(node.LastEdit));
									object.setContent(node.Content);
									object.setBench(bench.Id);
									object.setContentType(node.ContentType.toString());
									object.setCreated(TimeConverter.get(node.Created));
									object.getPosition().setHeight(node.Position.Height);
									object.getPosition().setWidth(node.Position.Width);
									object.getPosition().setX(node.Position.X);
									object.getPosition().setY(node.Position.Y);
									object.getCreator().setId(node.Creator.Id);
									object.getCreator().setUser(node.Creator.Username);

									nodes.add(object);
								}
								response.setNodes(nodes);

								ArrayList<BenchInfoResponseBase.Member> members = new ArrayList<>();
								for (Map.Entry<Integer, PermissionLevel> member : bench.Users.entrySet())
								{
									User user = userManager.load(member.getKey());
									BenchInfoResponseBase.Member object = new BenchInfoResponseBase.Member();
									object.setId(user.Id);
									object.setUser(user.Username);
									object.setAvatar(user.Avatar);
									object.setRole(member.getValue().toString());
									members.add(object);
								}
								response.setMembers(members);

								return gson.toJson(response);
							}
							case "medium":
							{
								BenchInfoResponseMedium response = new BenchInfoResponseMedium();
								response.getDimensions().setWidth(bench.Dimensions.Width);
								response.getDimensions().setHeight(bench.Dimensions.Height);
								response.setId(bench.Id);
								response.setBackground(bench.Background);
								response.setCreated(TimeConverter.get(bench.Created));
								response.getOwner().setId(bench.Owner.Id);
								response.getOwner().setUser(bench.Owner.Username);
								response.setPreview(bench.Preview);
								response.setTitle(bench.Title);

								ArrayList<BenchInfoResponseNodeObject> nodes = new ArrayList<>();
								for (BenchNode node : bench.Nodes.values())
								{
									BenchInfoResponseNodeObject object = new BenchInfoResponseNodeObject();
									object.setId(node.Id);
									object.setTitle(node.Title);
									object.setLastUpdate(TimeConverter.get(node.LastEdit));
									object.setBench(bench.Id);
									object.setContentType(node.ContentType.toString());
									object.setCreated(TimeConverter.get(node.Created));
									object.getPosition().setHeight(node.Position.Height);
									object.getPosition().setWidth(node.Position.Width);
									object.getPosition().setX(node.Position.X);
									object.getPosition().setY(node.Position.Y);
									object.getCreator().setId(node.Creator.Id);
									object.getCreator().setUser(node.Creator.Username);

									nodes.add(object);
								}
								response.setNodes(nodes);

								ArrayList<BenchInfoResponseBase.Member> members = new ArrayList<>();
								for (Map.Entry<Integer, PermissionLevel> member : bench.Users.entrySet())
								{
									User user = userManager.load(member.getKey());
									BenchInfoResponseBase.Member object = new BenchInfoResponseBase.Member();
									object.setId(user.Id);
									object.setUser(user.Username);
									object.setAvatar(user.Avatar);
									object.setRole(member.getValue().toString());
									members.add(object);
								}
								response.setMembers(members);

								return gson.toJson(response);

							}
							case "low":
							default:
							{
								BenchInfoResponseLow response = new BenchInfoResponseLow();
								response.getDimensions().setWidth(bench.Dimensions.Width);
								response.getDimensions().setHeight(bench.Dimensions.Height);
								response.setId(bench.Id);
								response.setBackground(bench.Background);
								response.setCreated(TimeConverter.get(bench.Created));
								response.getOwner().setId(bench.Owner.Id);
								response.getOwner().setUser(bench.Owner.Username);
								response.setPreview(bench.Preview);
								response.setTitle(bench.Title);
								response.setNodes(bench.Nodes.size());

								ArrayList<BenchInfoResponseBase.Member> members = new ArrayList<>();
								for (Map.Entry<Integer, PermissionLevel> member : bench.Users.entrySet())
								{
									User user = userManager.load(member.getKey());
									BenchInfoResponseBase.Member object = new BenchInfoResponseBase.Member();
									object.setId(user.Id);
									object.setUser(user.Username);
									object.setAvatar(user.Avatar);
									object.setRole(member.getValue().toString());
									members.add(object);
								}
								response.setMembers(members);

								return gson.toJson(response);
							}
						}
					}

				}
				res.status(403);
				return "";
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}
		});
	}

	public void create()
	{
		post(API + "bench", (req, res) ->
		{
			try
			{
				BenchCreate request = gson.fromJson(req.body(), BenchCreate.class);

				if (tokenManager.check(request.getAgent().getId(), request.getAgent().getToken()))
				{
					IdResponse response = new IdResponse();

					int id = benchManager.createBench(userManager.load(request.getAgent().getId()), request.getTitle(), request.getDimensions().getWidth(), request.getDimensions().getHeight()).Id;

					response.setId(id);
					return gson.toJson(response);
				}
				res.status(403);
				return "";
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}
		});
	}

	public void deleteBench()
	{
		delete(API + "bench/:benchId", (req, res) ->
		{
			int requestedBench;

			try
			{
				requestedBench = Integer.valueOf(req.params(":benchId"));
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}

			try
			{
				BenchDelete request = gson.fromJson(req.body(), BenchDelete.class);

				if (tokenManager.check(request.getAgent().getId(), request.getAgent().getToken()))
				{
					Bench bench = benchManager.load(requestedBench);

					if (bench == null)
					{
						res.status(404);
						return "";
					}

					if (bench.Owner.Id == request.getAgent().getId())
					{
						benchManager.deleteBench(bench);
						res.status(200);
					}
					else
					{
						res.status(403);
					}
					return "";
				}
				res.status(403);
				return "";
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}
		});
	}

	public void edit()
	{
		put(API + "bench/:benchId", (req, res) ->
		{
			int requestedBench;

			try
			{
				requestedBench = Integer.valueOf(req.params(":benchId"));
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}

			try
			{
				BenchEdit request = gson.fromJson(req.body(), BenchEdit.class);
				if (tokenManager.check(request.getAgent().getId(), request.getAgent().getToken()))
				{
					Bench bench = benchManager.load(requestedBench);

					if (bench != null && bench.Id != 0 && bench.Owner.Id == request.getAgent().getId())
					{

						BenchEditResponse response = new BenchEditResponse();

						String background = request.getContent().getBackground();
						String title = request.getContent().getTitle();
						int height = request.getDimensions().getHeight();
						int width = request.getDimensions().getWidth();

						if (width >= 512 && height >= 400)
						{
							benchManager.updateDimensions(bench, width, height);
							response.setDimensions(true);
						}
						else if (width + height >= 0)
						{
							response.setDimensions(false);
						}

						if (!background.isEmpty())
						{
							benchManager.updateBackground(bench, background);
							response.setBackground(true);
						}

						if (!title.isEmpty() && title.length() <= 256)
						{
							benchManager.updateTitle(bench, title);
							response.setTitle(true);
						} else if (!title.isEmpty())
						{
							response.setTitle(false);
						}
						return gson.toJson(response);
					}
				}
				res.status(403);
				return "";
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}
		});
	}

	public void adduser()
	{
		post(API + "bench/:benchId/user/:userId", (req, res) ->
		{
			try
			{
				Bench bench = benchManager.load(Integer.valueOf(req.params(":benchId")));
				UserModObject request = gson.fromJson(req.body(), UserModObject.class);

				if (tokenManager.check(request.getAgent().getId(), request.getAgent().getToken()))
				{
					if (bench.Owner.Id == request.getAgent().getId())
					{
						PermissionLevel level = PermissionLevel.get(request.getPermission());
						if (level.val() >= PermissionLevel.EDITOR.val())
						{
							benchManager.addUser(bench, Integer.valueOf(req.params(":userId")), level);

							res.status(200);
							return "";
						}
					}
					res.status(403);
					return "";
				}
				res.status(403);
				return "";
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}
		});
	}

	public void moduser()
	{
		put(API + "bench/:benchId/user/:userId", (req, res) ->
		{

			try
			{
				Bench bench = benchManager.load(Integer.valueOf(req.params(":benchId")));
				UserModObject request = gson.fromJson(req.body(), UserModObject.class);

				if (tokenManager.check(request.getAgent().getId(), request.getAgent().getToken()))
				{
					if (bench.Owner.Id == request.getAgent().getId())
					{
						PermissionLevel level = PermissionLevel.get(request.getPermission());

						if (level.val() <= PermissionLevel.EDITOR.val())
						{
							benchManager.modUser(bench, Integer.valueOf(req.params(":userId")), level);

							res.status(200);
							return "";
						}
					}
					res.status(403);
					return "";
				}
				res.status(403);
				return "";
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}
		});
	}

	public void removeuser()
	{
		delete(API + "bench/:benchId/user/:userId", (req, res) ->
		{

			try
			{
				Bench bench = benchManager.load(Integer.valueOf(req.params(":benchId")));
				UserModNoPermObject request = gson.fromJson(req.body(), UserModNoPermObject.class);

				if (tokenManager.check(request.getAgent().getId(), request.getAgent().getToken()))
				{
					if (bench.Owner.Id == request.getAgent().getId())
					{
						benchManager.removeUser(bench, Integer.valueOf(req.params(":userId")));

						res.status(200);
						return "";

					}
					res.status(403);
					return "";
				}
				res.status(403);
				return "";
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}
		});
	}

	//////////////// BENCHNODE - CORE ////////////////

	public void copyBenchNode()
	{

	}

	public void createBenchNode()
	{
		post(API + "bench/:benchId/node", (req, res) ->
		{

			try
			{
				Bench bench = benchManager.load(Integer.valueOf(req.params(":benchId")));
				BenchNodeCreate request = gson.fromJson(req.body(), BenchNodeCreate.class);
				int id = request.getAgent().getId();

				if (tokenManager.check(id, request.getAgent().getToken()))
				{
					if (bench.Users.containsKey(id) && !bench.Users.get(id).equals(PermissionLevel.VIEWER) && !bench.Users.get(id).equals(PermissionLevel.NONE))
					{
						User user = userManager.load(id);

						int x = request.getDimensions().getX();
						int y = request.getDimensions().getY();
						int w = request.getDimensions().getWidth();
						int h = request.getDimensions().getHeight();
						String title = request.getContent().getTitle();
						ContentType contentType = ContentType.get(request.getContent().getType());
						String content = request.getContent().getData();
						long time = System.currentTimeMillis();

						if (w >= 32 && h >= 32 && x >= 0 && y >= 0 && x <= bench.Dimensions.Width && y <= bench.Dimensions.Height && title.length() <= 256)
						{
							BenchNode node = new BenchNode(0, bench, user, x, y, w, h, title, contentType, content, time, time, false);
							benchManager.createNode(user, bench, node);
							res.status(200);
							return "";
						}
					}
					res.status(403);
					return "";
				}
				res.status(403);
				return "";
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}
		});
	}

	public void deleteBenchNode()
	{
		delete(API + "bench/:benchId/node/:nodeId", (req, res) ->
		{

			try
			{
				Bench bench = benchManager.load(Integer.valueOf(req.params(":benchId")));
				BenchNodeDelete request = gson.fromJson(req.body(), BenchNodeDelete.class);
				int id = request.getAgent().getId();
				User user = userManager.load(id);

				if (tokenManager.check(id, request.getAgent().getToken()))
				{
					if (bench.Users.containsKey(id) && !bench.Users.get(id).equals(PermissionLevel.VIEWER) && !bench.Users.get(id).equals(PermissionLevel.NONE))
					{
						BenchNode node = benchManager.getNode(bench, Integer.valueOf(req.params(":nodeId")));

						if (node != null)
						{
							benchManager.deleteNode(user, bench, node);
							res.status(200);
							return "";
						}
						res.status(404);
						return "";
					}
					res.status(403);
					return "";
				}
				res.status(403);
				return "";
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}
		});
	}

	public void renameBenchNode()
	{
		put(API + "bench/:benchId/node/:nodeId/name", (req, res) ->
		{

			try
			{
				Bench bench = benchManager.load(Integer.valueOf(req.params(":benchId")));
				BenchNodeRename request = gson.fromJson(req.body(), BenchNodeRename.class);
				int id = request.getAgent().getId();
				User user = userManager.load(id);

				if (tokenManager.check(id, request.getAgent().getToken()))
				{
					if (bench.Users.containsKey(id) && !bench.Users.get(id).equals(PermissionLevel.VIEWER) && !bench.Users.get(id).equals(PermissionLevel.NONE))
					{
						String title = request.getContent().getTitle();

						if (title.length() <= 256)
						{
							BenchNode node = benchManager.getNode(bench, Integer.valueOf(req.params(":nodeId")));

							if (node != null && bench.Id != 0)
							{
								benchManager.renameNode(user, bench, node, title);

								res.status(200);
								return "";
							}
							res.status(400);
							return "";
						}
					}
					res.status(403);
					return "";
				}
				res.status(403);
				return "";
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}
		});
	}

	public void editBenchNode()
	{
		put(API + "bench/:benchId/node/:nodeId", (req, res) ->
		{

			try
			{
				Bench bench = benchManager.load(Integer.valueOf(req.params(":benchId")));
				BenchNodeEdit request = gson.fromJson(req.body(), BenchNodeEdit.class);
				int id = request.getAgent().getId();
				User user = userManager.load(id);

				if (tokenManager.check(id, request.getAgent().getToken()))
				{
					if (bench.Users.containsKey(id) && !bench.Users.get(id).equals(PermissionLevel.VIEWER) && !bench.Users.get(id).equals(PermissionLevel.NONE))
					{
						BenchNode node = benchManager.getNode(bench, Integer.valueOf(req.params(":nodeId")));

						if (node != null && node.Id != 0)
						{
							String content = request.getContent().getData();
							ContentType type = ContentType.get(request.getContent().getType());

							if (!type.equals(ContentType.NONE))
							{
								benchManager.typeNode(user, bench, node, type);
							}

							if (!content.isEmpty())
							{
								benchManager.editNode(user, bench, node, content);
							}

							res.status(200);
							return "";
						}
						res.status(404);
						return "";
					}
					res.status(403);
					return "";
				}
				res.status(403);
				return "";
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}
		});
	}

	public void moveBenchNode()
	{
		put(API + "bench/:benchId/node/:nodeId/move", (req, res) ->
		{

			try
			{
				Bench bench = benchManager.load(Integer.valueOf(req.params(":benchId")));
				BenchNodeMove request = gson.fromJson(req.body(), BenchNodeMove.class);
				int id = request.getAgent().getId();
				User user = userManager.load(id);

				if (tokenManager.check(id, request.getAgent().getToken()))
				{
					if (bench.Users.containsKey(id) && !bench.Users.get(id).equals(PermissionLevel.VIEWER) && !bench.Users.get(id).equals(PermissionLevel.NONE))
					{
						BenchNode node = benchManager.getNode(bench, Integer.valueOf(req.params(":nodeId")));

						if (node != null && node.Id != 0)
						{
							int x = request.getDimensions().getX();
							int y = request.getDimensions().getY();

							if (x >= 0 && y >= 0 && x <= bench.Dimensions.Width && y <= bench.Dimensions.Height)
							{
								node.Position.X = x;
								node.Position.Y = y;
								benchManager.moveNode(user, bench, node, x, y);

								res.status(200);
								return "";
							}
							res.status(400);
							return "";
						}
						res.status(404);
						return "";
					}
					res.status(403);
					return "";
				}
				res.status(403);
				return "";
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}
		});
	}

	public void resizeBenchNode()
	{
		put(API + "bench/:benchId/node/:nodeId/size", (req, res) ->
		{

			try
			{
				Bench bench = benchManager.load(Integer.valueOf(req.params(":benchId")));
				BenchNodeResize request = gson.fromJson(req.body(), BenchNodeResize.class);
				int id = request.getAgent().getId();
				User user = userManager.load(id);

				if (tokenManager.check(id, request.getAgent().getToken()))
				{
					if (bench.Users.containsKey(id) && !bench.Users.get(id).equals(PermissionLevel.VIEWER) && !bench.Users.get(id).equals(PermissionLevel.NONE))
					{
						int w = request.getDimensions().getWidth();
						int h = request.getDimensions().getHeight();

						if (w >= 32 && h >= 32 && w <= bench.Dimensions.Width && h <= bench.Dimensions.Height)
						{
							BenchNode node = benchManager.getNode(bench, Integer.valueOf(req.params(":nodeId")));

							if (node != null && node.Id != 0)
							{
								benchManager.resizeNode(user, bench, node, w, h);

								res.status(200);
								return "";
							}
							res.status(400);
							return "";
						}
					}
					res.status(403);
					return "";
				}
				res.status(403);
				return "";
			}
			catch (Exception e)
			{
				res.status(400);
				return "";
			}
		});
	}

	public void spawnBenchNode()
	{

	}

	//////////////// BENCHNODE - MISC ////////////////

	public void ignoreBenchNode()
	{

	}

	public void watchBenchNode()
	{

	}

	//////////////// USERNODE ////////////////

	public void createUserNode()
	{

	}

	public void deleteUserNode()
	{

	}

	public void editUserNode()
	{

	}

	public void renameUserNode()
	{

	}
}
