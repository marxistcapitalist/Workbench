package online.workbench.api;

import com.google.gson.annotations.SerializedName;

public enum Protocol
{
	@SerializedName("http|ping <-")HTTP_CLIENT_PING(Empty.class),
	@SerializedName("http|ping ->")HTTP_SERVER_PING(Empty.class),
	@SerializedName("http|authenticate <-")HTTP_CLIENT_AUTHENTICATE(Authenticate.class),
	@SerializedName("http|authenticate ->")HTTP_SERVER_AUTHENTICATE(Authenticate.class),
	@SerializedName("http|login <-")HTTP_CLIENT_LOGIN(ClientLogin.class),
	@SerializedName("http|login ->")HTTP_SERVER_LOGIN(ServerLogin.class),
	@SerializedName("http|logout <-")HTTP_CLIENT_LOGOUT(ClientToken.class),
	@SerializedName("http|logout ->")HTTP_SERVER_LOGOUT(EmptyAgent.class),
	@SerializedName("http|register <-")HTTP_CLIENT_REGISTER(Register.class),
	@SerializedName("http|register ->")HTTP_SERVER_REGISTER(ServerLogin.class),
	@SerializedName("http|benchId/adduser <-")HTTP_CLIENT_ADDUSER(UserModObject.class),
	@SerializedName("http|benchId/adduser ->")HTTP_SERVER_ADDUSER(BooleanResponse.class),
	@SerializedName("http|benchId/getusers <-")HTTP_CLIENT_GETUSERS(ContainedClientAgent.class),
	@SerializedName("http|benchId/getusers ->")HTTP_SERVER_GETUSERS(GetUsersResponse.class),
	@SerializedName("http|benchId/moduser <-")HTTP_CLIENT_MODUSER(UserModObject.class),
	@SerializedName("http|benchId/moduser ->")HTTP_SERVER_MODUSER(BooleanResponse.class),
	@SerializedName("http|benchId/removeuser <-")HTTP_CLIENT_REMOVEUSER(UserModNoPermObject.class),
	@SerializedName("http|benchId/removeuser ->")HTTP_SERVER_REMOVEUSER(BooleanResponse.class),
	@SerializedName("http|create <-")HTTP_CLIENT_CREATE(BenchCreate.class),
	@SerializedName("http|create ->")HTTP_SERVER_CREATE(IdResponse.class),
	@SerializedName("http|delete <-")HTTP_CLIENT_DELETE(BenchDelete.class),
	@SerializedName("http|delete ->")HTTP_SERVER_DELETE(BooleanResponse.class),
	@SerializedName("http|edit <-")HTTP_CLIENT_EDIT(BenchEdit.class),
	@SerializedName("http|edit ->")HTTP_SERVER_EDIT(BooleanResponse.class),
	@SerializedName("http|benchId/copy <-")HTTP_CLIENT_BENCHNODE_COPY(BenchNodeCopy.class),
	@SerializedName("http|benchId/copy ->")HTTP_SERVER_BENCHNODE_COPY(IdResponse.class),
	@SerializedName("http|benchId/create <-")HTTP_CLIENT_BENCHNODE_CREATE(BenchNodeCreate.class),
	@SerializedName("http|benchId/create ->")HTTP_SERVER_BENCHNODE_CREATE(IdResponse.class),
	@SerializedName("http|benchId/delete <-")HTTP_CLIENT_BENCHNODE_DELETE(BenchNodeDelete.class),
	@SerializedName("http|benchId/delete ->")HTTP_SERVER_BENCHNODE_DELETE(BooleanResponse.class),
	@SerializedName("http|benchId/edit <-")HTTP_CLIENT_BENCHNODE_EDIT(BenchNodeEdit.class),
	@SerializedName("http|benchId/edit ->")HTTP_SERVER_BENCHNODE_EDIT(BooleanResponse.class),
	@SerializedName("http|benchId/move <-")HTTP_CLIENT_BENCHNODE_MOVE(BenchNodeMove.class),
	@SerializedName("http|benchId/move ->")HTTP_SERVER_BENCHNODE_MOVE(BooleanResponse.class),
	@SerializedName("http|benchId/rename <-")HTTP_CLIENT_BENCHNODE_RENAME(BenchNodeRename.class),
	@SerializedName("http|benchId/rename ->")HTTP_SERVER_BENCHNODE_RENAME(BooleanResponse.class),
	@SerializedName("http|benchId/resize <-")HTTP_CLIENT_BENCHNODE_RESIZE(BenchNodeResize.class),
	@SerializedName("http|benchId/resize ->")HTTP_SERVER_BENCHNODE_RESIZE(BooleanResponse.class),
	@SerializedName("http|benchId/spawn <-")HTTP_CLIENT_BENCHNODE_SPAWN(BenchNodeSpawn.class),
	@SerializedName("http|benchId/spawn ->")HTTP_SERVER_BENCHNODE_SPAWN(IdResponse.class),
	@SerializedName("http|benchId/ignore <-")HTTP_CLIENT_BENCHNODE_IGNORE(IgnoreWatchNode.class),
	@SerializedName("http|benchId/ignore ->")HTTP_SERVER_BENCHNODE_INGORE(BooleanResponse.class),
	@SerializedName("http|benchId/watch <-")HTTP_CLIENT_BENCHNODE_WATCH(IgnoreWatchNode.class),
	@SerializedName("http|benchId/watch ->")HTTP_SERVER_BENCHNODE_WATCH(BooleanResponse.class),
	@SerializedName("http|userId/create <-")HTTP_CLIENT_USERNODE_CREATE(CreateUserNode.class),
	@SerializedName("http|userId/create ->")HTTP_SERVER_USERNODE_CREATE(IdResponse.class),
	@SerializedName("http|userId/delete <-")HTTP_CLIENT_USERNODE_DELETE(DeleteUserNode.class),
	@SerializedName("http|userId/delete ->")HTTP_SERVER_USERNODE_DELETE(BooleanResponse.class),
	@SerializedName("http|userId/edit <-")HTTP_CLIENT_USERNODE_EDIT(EditUserNode.class),
	@SerializedName("http|userId/edit ->")HTTP_SERVER_USERNODE_EDIT(BooleanResponse.class),
	@SerializedName("http|userId/rename <-")HTTP_CLIENT_USERNODE_RENAME(RenameUserNode.class),
	@SerializedName("http|userId/rename ->")HTTP_SERVER_USERNODE_RENAME(BooleanResponse.class),

	@SerializedName("ws|move <-")WEBSOCKET_CLIENT_MOVE(ClientMove.class),
	@SerializedName("ws|resize <-")WEBSOCKET_CLIENT_RESIZE(ClientResize.class),
	@SerializedName("ws|textcursor <-")WEBSOCKET_CLIENT_TEXTCURSOR(ClientTextCursor.class),
	@SerializedName("ws|textmodify <-")WEBSOCKET_CLIENT_TEXTMODIFY(ClientTextModify.class),
	@SerializedName("ws|textselect <-")WEBSOCKET_CLIENT_TEXTSELECT(ClientTextSelect.class),
	@SerializedName("ws|chat <-")WEBSOCKET_CLIENT_CHAT(ClientChat.class),

	@SerializedName("ws|create ->")WEBSOCKET_SERVER_CREATE(ServerCreate.class),
	@SerializedName("ws|delete ->")WEBSOCKET_SERVER_DELETE(WS.class),
	@SerializedName("ws|edit ->")WEBSOCKET_SERVER_EDIT(ServerEdit.class),
	@SerializedName("ws|move ->")WEBSOCKET_SERVER_MOVE(ServerMove.class),
	@SerializedName("ws|rename ->")WEBSOCKET_SERVER_RENAME(ServerRename.class),
	@SerializedName("ws|resize ->")WEBSOCKET_SERVER_RESIZE(ServerResize.class),
	@SerializedName("ws|chat ->")WEBSOCKET_SERVER_CHAT(ServerChat.class),
	@SerializedName("ws|ignore ->")WEBSOCKET_SERVER_IGNORE(ServerBase.class),
	@SerializedName("ws|watch ->")WEBSOCKET_SERVER_WATCH(ServerWatch.class),
	@SerializedName("ws|notify ->")WEBSOCKET_SERVER_NOTIFY(Notify.class),
	@SerializedName("ws|textcursor ->")WEBSOCKET_SERVER_TEXTCURSOR(ServerTextCursor.class),
	@SerializedName("ws|textmodify ->")WEBSOCKET_SERVER_TEXTMODIFY(ServerTextModify.class),
	@SerializedName("ws|textselect ->")WEBSOCKET_SERVER_TEXTSELECT(ServerTextSelect.class);

	public class ClientChat
	{
		String type;
		int bench;
		String message;
		String level;
		ClientAgent agent;
	}

	public class ServerTextSelect extends ServerTextBase
	{
		Content content;
		class Content
		{
			int being;
			int end;
		}
	}

	public class ServerTextModify extends ServerTextBase
	{
		Content content;
		class Content
		{
			int begin;
			int end;
			String data;
		}
	}

	public class ServerTextCursor extends ServerTextBase
	{
		Content content;
		class Content
		{
			int index;
			boolean show;
		}
	}

	public class ServerTextBase
	{
		String type;
		int node;
		int bench;
		WSServerAgent agent;
	}

	public class Notify extends EmptyAgent
	{
		String type;
		class Data
		{
			String header;
			String text;
			String link;
			int time;
		}
	}

	public class ServerWatch extends ServerBase
	{
		String interval;
	}

	public class ServerChat extends ServerBase
	{
		String message;
		String level;
	}

	public class ServerBase
	{
		String type;
		int bench;
		WSServerAgent agent;
	}

	public class ServerResize extends WS
	{
		WH dimensions;
	}

	public class ServerRename extends WS
	{
		Content content;
		class Content
		{
			String title;
		}
	}

	public class ServerMove extends WS
	{
		XY dimensions;
	}

	public class ServerEdit extends WS
	{
		Content content;
		class Content
		{
			String type;
			String data;
		}
	}

	public class ServerCreate extends WS
	{
		FullDimensions dimensions;
		Content content;
		class Content
		{
			String title;
			String type;
			String data;
		}
	}

	public class WS
	{
		String type;
		int node;
		int bench;
		WSServerAgent agent;

	}

	public class FullDimensions
	{
		int x;
		int y;
		int w;
		int h;
	}

	public class WSServerAgent
	{
		String name;
		int id;
	}

	public class ClientTextSelect extends WC
	{
		class Content
		{
			int begin;
			int end;
		}
	}

	public class ClientTextModify extends WC
	{
		Content content;
		class Content
		{
			int begin;
			int end;
			String data;
		}
	}

	public class ClientTextCursor extends WC
	{
		Content content;
		class Content
		{
			int index;
			boolean show;
		}
	}

	public class ClientResize extends WC
	{
		WH dimensions;
	}

	public class ClientMove extends WC
	{
		XY dimensions;
	}

	public class XY
	{
		int x;
		int y;
	}

	public class WC
	{
		String type;
		int node;
		int bench;
		ClientAgent agent;
	}

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
		WH dimensions;
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
		XY dimensions;
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
		WH dimensions;
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
		XY dimensions;
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
		FullDimensions dimensions;
		Content content;
		ClientAgent agent;

		class Content
		{
			String title;
			String type;
			String data;
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
		WH dimensions;
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
		WH dimensions;
		ClientAgent agent;
	}

	public class WH
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
		String user;
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

