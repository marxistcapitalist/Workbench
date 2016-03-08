package online.workbench.jetty.handlers.api.objects;

import com.google.gson.annotations.SerializedName;

public enum Protocol
{
	@SerializedName("ping <-")HTTP_CLIENT_PING(Empty.class),
	@SerializedName("ping ->")HTTP_SERVER_PING(Empty.class),
	@SerializedName("authenticate <-")HTTP_CLIENT_AUTHENTICATE(Authenticate.class),
	@SerializedName("authenticate ->")HTTP_SERVER_AUTHENTICATE(Authenticate.class),
	@SerializedName("login <-")HTTP_CLIENT_LOGIN(ClientLogin.class),
	@SerializedName("login ->")HTTP_SERVER_LOGIN(ServerLogin.class),
	@SerializedName("logout <-")HTTP_CLIENT_LOGOUT(ClientToken.class),
	@SerializedName("logout ->")HTTP_SERVER_LOGOUT(EmptyAgent.class),
	@SerializedName("register <-")HTTP_CLIENT_REGISTER(Register.class),
	@SerializedName("register ->")HTTP_SERVER_REGISTER(ServerLogin.class),
	@SerializedName("benchId/adduser <-")HTTP_CLIENT_ADDUSER(UserModObject.class),
	@SerializedName("benchId/adduser ->")HTTP_SERVER_ADDUSER(BooleanResponse.class),
	@SerializedName("benchId/getusers <-")HTTP_CLIENT_GETUSERS(ContainedClientAgent.class),
	@SerializedName("benchId/getusers ->")HTTP_SERVER_GETUSERS(GetUsersResponse.class),
	@SerializedName("benchId/moduser <-")HTTP_CLIENT_MODUSER(UserModObject.class),
	@SerializedName("benchId/moduser ->")HTTP_SERVER_MODUSER(BooleanResponse.class),
	@SerializedName("benchId/removeuser <-")HTTP_CLIENT_REMOVEUSER(UserModNoPermObject.class),
	@SerializedName("benchId/removeuser ->")HTTP_SERVER_REMOVEUSER(BooleanResponse.class),
	@SerializedName("create <-")HTTP_CLIENT_CREATE(BenchCreate.class),
	@SerializedName("create ->")HTTP_SERVER_CREATE(IdResponse.class),
	@SerializedName("delete <-")HTTP_CLIENT_DELETE(BenchDelete.class),
	@SerializedName("delete ->")HTTP_SERVER_DELETE(BooleanResponse.class),
	@SerializedName("edit <-")HTTP_CLIENT_EDIT(BenchEdit.class),
	@SerializedName("edit ->")HTTP_SERVER_EDIT(BooleanResponse.class),
	@SerializedName("benchId/copy <-")HTTP_CLIENT_BENCHNODE_COPY(BenchNodeCopy.class),
	@SerializedName("benchId/copy ->")HTTP_SERVER_BENCHNODE_COPY(IdResponse.class),
	@SerializedName("benchId/create <-")HTTP_CLIENT_BENCHNODE_CREATE(BenchNodeCreate.class),
	@SerializedName("benchId/create ->")HTTP_SERVER_BENCHNODE_CREATE(IdResponse.class),
	@SerializedName("benchId/delete <-")HTTP_CLIENT_BENCHNODE_DELETE(BenchNodeDelete.class),
	@SerializedName("benchId/delete ->")HTTP_SERVER_BENCHNODE_DELETE(BooleanResponse.class),
	@SerializedName("benchId/edit <-")HTTP_CLIENT_BENCHNODE_EDIT(BenchNodeEdit.class),
	@SerializedName("benchId/edit ->")HTTP_SERVER_BENCHNODE_EDIT(BooleanResponse.class),
	@SerializedName("benchId/move <-")HTTP_CLIENT_BENCHNODE_MOVE(BenchNodeMove.class),
	@SerializedName("benchId/move ->")HTTP_SERVER_BENCHNODE_MOVE(BooleanResponse.class),
	@SerializedName("benchId/rename <-")HTTP_CLIENT_BENCHNODE_RENAME(BenchNodeRename.class),
	@SerializedName("benchId/rename ->")HTTP_SERVER_BENCHNODE_RENAME(BooleanResponse.class),
	@SerializedName("benchId/resize <-")HTTP_CLIENT_BENCHNODE_RESIZE(BenchNodeResize.class),
	@SerializedName("benchId/resize ->")HTTP_SERVER_BENCHNODE_RESIZE(BooleanResponse.class),
	@SerializedName("benchId/spawn <-")HTTP_CLIENT_BENCHNODE_SPAWN(BenchNodeSpawn.class),
	@SerializedName("benchId/spawn ->")HTTP_SERVER_BENCHNODE_SPAWN(IdResponse.class),
	@SerializedName("benchId/ignore <-")HTTP_CLIENT_BENCHNODE_IGNORE(IgnoreWatchNode.class),
	@SerializedName("benchId/ignore ->")HTTP_SERVER_BENCHNODE_INGORE(BooleanResponse.class),
	@SerializedName("benchId/watch <-")HTTP_CLIENT_BENCHNODE_WATCH(IgnoreWatchNode.class),
	@SerializedName("benchId/watch ->")HTTP_SERVER_BENCHNODE_WATCH(BooleanResponse.class),
	@SerializedName("userId/create <-")HTTP_CLIENT_USERNODE_CREATE(CreateUserNode.class),
	@SerializedName("userId/create ->")HTTP_SERVER_USERNODE_CREATE(IdResponse.class),
	@SerializedName("userId/delete <-")HTTP_CLIENT_USERNODE_DELETE(DeleteUserNode.class),
	@SerializedName("userId/delete ->")HTTP_SERVER_USERNODE_DELETE(BooleanResponse.class),
	@SerializedName("userId/edit <-")HTTP_CLIENT_USERNODE_EDIT(EditUserNode.class),
	@SerializedName("userId/edit ->")HTTP_SERVER_USERNODE_EDIT(BooleanResponse.class),
	@SerializedName("userId/rename <-")HTTP_CLIENT_USERNODE_RENAME(RenameUserNode.class),
	@SerializedName("userId/rename ->")HTTP_SERVER_USERNODE_RENAME(BooleanResponse.class);

	public class RenameUserNode
	{
		int node;
		Content content;
		ClientAgent agent;
		class Content
		{
			String title;
		}
	}

	public class EditUserNode
	{
		int node;
		Content content;
		ClientAgent agent;
		class Content
		{
			String type;
			String data;
		}
	}

	public class DeleteUserNode
	{
		int node;
		ClientAgent agent;
	}

	public class CreateUserNode
	{
		Dimensions dimensions;
		Content content;
		ClientAgent agent;
		class Content
		{
			String title;
			String type;
			String data;
		}
	}

	public class IgnoreWatchNode
	{
		int node;
		ClientAgent agent;
	}

	public class BenchNodeSpawn
	{
		Dimensions dimensions;
		class Dimensions
		{
			int x;
			int y;
		}
		Content content;
		class Content
		{
			String retitle;
			int node;
		}
		ClientAgent agent;
	}

	public class BenchNodeResize
	{
		int node;
		Dimensions dimensions;
		ClientAgent agent;
	}

	public class BenchNodeRename
	{
		int node;
		Content content;
		ClientAgent agent;

		class Content
		{
			String title;
		}
	}

	public class BenchNodeMove
	{
		int node;
		Dimensions dimensions;
		class Dimensions
		{
			int x;
			int y;
		}
		ClientAgent agent;
	}

	public class BenchNodeEdit
	{
		int node;
		Content content;
		ClientAgent agent;

		class Content
		{
			String type;
			String data;
		}
	}

	public class BenchNodeDelete
	{
		String node;
		ClientAgent agent;
	}

	public class BenchNodeCreate
	{
		Dimensions dimensions;
		Content content;
		ClientAgent agent;

		class Content
		{
			String title;
			String type;
			String data;
		}
		class Dimensions
		{
			int x;
			int y;
			int w;
			int h;
		}
	}

	public class BenchNodeCopy
	{
		Content content;
		ClientAgent agent;

		class Content
		{
			String retitle;
			int node;
		}
	}

	public class BenchEdit
	{
		int id;
		Dimensions dimensions;
		Content content;
		ClientAgent agent;

		class Content
		{
			String title;
			String background;
		}
	}

	public class BenchDelete
	{
		int id;
		ClientAgent agent;
	}

	public class IdResponse
	{
		int id;
	}

	public class BenchCreate
	{
		String title;
		Dimensions dimensions;
		ClientAgent agent;
	}

	public class Dimensions
	{
		int w;
		int h;
	}

	public class GetUsersResponse
	{
		String creator;
		MetaUser[] users;

	}

	public class MetaUser
	{
		int id;
		String user;
		String permission;
		boolean online;
	}

	public class ContainedClientAgent
	{
		ClientAgent agent;
	}

	public class UserModObject
	{
		int userId;
		String permission;
		ClientAgent agent;
	}

	public class UserModNoPermObject
	{
		int userId;
		ClientAgent agent;
	}

	public class BooleanResponse
	{
		boolean result;
	}

	public class Register
	{
		String username;
		String password;
		String email;
	}

	public class Empty { }

	public class EmptyAgent
	{
		Agent agent;

		class Agent
		{

		}
	}

	public class ServerAgent
	{
		int id;
		String username;
	}

	public class ClientAgent
	{
		int id;
		String token;
	}

	public class Authenticate
	{
		String token;
		int id;
	}

	public class ClientLogin
	{
		String loginkey;
		String password;
	}

	public class ServerLogin
	{
		String token;
		ServerAgent agent;
	}

	public class ClientToken
	{
		String token;
	}

	private final Class<? extends Object> cClass;

	Protocol (Class<? extends Object> object)
	{
		cClass = object;
	}

}

