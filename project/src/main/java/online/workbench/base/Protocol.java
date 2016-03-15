package online.workbench.base;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import online.workbench.model.struct.User;

import java.util.ArrayList;

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
	@SerializedName("http|register ->")HTTP_SERVER_REGISTER(ServerRegister.class),
	@SerializedName("http|user <-")HTTP_CLIENT_USER(ClientUserData.class),
	@SerializedName("http|user ->")HTTP_SERVER_USER_UNAUTHENTICATED(ServerUserDataUnauthenticated.class),
	@SerializedName("http|user ->")HTTP_CLIENT_USER_AUTHENTICATED(ServerUserDataAuthenticated.class),
	@SerializedName("http|user <-")HTTP_CLIENT_USEREDIT(UserEditRequest.class),
	@SerializedName("http|user ->")HTTP_SERVER_USEREDIT(UserEditResponse.class),
	@SerializedName("http|benchId/adduser <-")HTTP_CLIENT_ADDUSER(UserModObject.class),
	@SerializedName("http|benchId/adduser ->")HTTP_SERVER_ADDUSER(BooleanResponse.class),
	@SerializedName("http|benchId/moduser <-")HTTP_CLIENT_MODUSER(UserModObject.class),
	@SerializedName("http|benchId/moduser ->")HTTP_SERVER_MODUSER(BooleanResponse.class),
	@SerializedName("http|benchId/removeuser <-")HTTP_CLIENT_REMOVEUSER(UserModNoPermObject.class),
	@SerializedName("http|benchId/removeuser ->")HTTP_SERVER_REMOVEUSER(BooleanResponse.class),
	@SerializedName("http|create <-")HTTP_CLIENT_CREATE(BenchCreate.class),
	@SerializedName("http|create ->")HTTP_SERVER_CREATE(IdResponse.class),
	@SerializedName("http|delete <-")HTTP_CLIENT_DELETE(BenchDelete.class),
	@SerializedName("http|delete ->")HTTP_SERVER_DELETE(BooleanResponse.class),
	@SerializedName("http|edit <-")HTTP_CLIENT_EDIT(BenchEdit.class),
	@SerializedName("http|edit ->")HTTP_SERVER_EDIT(BenchEditResponse.class),
	@SerializedName("http|bench <-")HTTP_CLIENT_BENCH(BenchInfoRequest.class),
	@SerializedName("http|bench ->")HTTP_SERVER_BENCH_LOW(BenchInfoResponseLow.class),
	@SerializedName("http|bench ->")HTTP_SERVER_BENCH_MEDIUM(BenchInfoResponseMedium.class),
	@SerializedName("http|bench ->")HTTP_SERVER_BENCH_HIGH(BenchInfoResponseHigh.class),
	@SerializedName("http|benchId/copy <-")HTTP_CLIENT_BENCHNODE_COPY(BenchNodeCopy.class),
	@SerializedName("http|benchId/copy ->")HTTP_SERVER_BENCHNODE_COPY(IdResponse.class),
	@SerializedName("http|benchId/create <-")HTTP_CLIENT_BENCHNODE_CREATE(BenchNodeCreate.class),
	@SerializedName("http|benchId/create ->")HTTP_SERVER_BENCHNODE_CREATE(BooleanResponse.class),
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
	@SerializedName("ws|textselect ->")WEBSOCKET_SERVER_TEXTSELECT(ServerTextSelect.class),
	@SerializedName("ws|mod ->")WEBSOCKET_SERVER_MOD(UserModifyOutgoing.class),
	@SerializedName("ws|verify <-")WEBSOCKET_CLIENT_VERIFY(VerifyIncoming.class),
	@SerializedName("ws|verify ->")WEBSOCKET_SERVER_VERIFY(VerifyOutgoing.class);

	@Data
	public static class VerifyIncoming
	{
		String type;
		int bench;
		ClientAgent agent = new ClientAgent();
	}

	@Data
	public static class VerifyOutgoing
	{
		String type;
		int bench;
		String role;
	}

	@Data
	public static class UserModifyOutgoing
	{
		String type;
		int bench;
		String action;
		String user;
		int id;
		String level;
	}

	@Data
	public static class UserEditRequest
	{
		String username;
		String email;
		String avatar;
		String password;
		ClientAgent agent = new ClientAgent();
	}

	@Data
	public static class UserEditResponse
	{
		boolean username;
		boolean email;
		boolean avatar;
		boolean password;
	}

	@Data
	public static class BenchInfoRequest
	{
		int id;
		String verbosity;
		ClientAgent agent = new ClientAgent();
	}

	@Data
	public static class BenchInfoResponseBase
	{
		int id;
		String title;
		Owner owner = new Owner();
		@Data
		public class Owner
		{
			int id;
			String user;
		}
		String preview;
		String background;
		ArrayList<Member> members = new ArrayList<>();
		@Data
		public static class Member
		{
			int id;
			String user;
			String role;
			String avatar;
		}
		String created;
	}


	@Data
	public static class BenchInfoResponseLow extends BenchInfoResponseBase
	{
		int nodes;
	}

	@Data
	public static class BenchInfoResponseMedium extends BenchInfoResponseBase
	{
		ArrayList<BenchInfoResponseNodeObject> nodes = new ArrayList<>();
	}

	@Data
	public static class BenchInfoResponseNodeObject
	{
		int id;
		int bench;
		String title;
		String created;
		String lastUpdate;
		Creator creator = new Creator();
		@Data
		class Creator
		{
			int id;
			String user;
		}
		FullDimensions position = new FullDimensions();
		String contentType;
	}

	@Data
	public static class BenchInfoResponseHigh extends BenchInfoResponseBase
	{
		ArrayList<BenchInfoResponseNodeObjectVerbose> nodes = new ArrayList<>();
		@Data
		public static class BenchInfoResponseNodeObjectVerbose extends BenchInfoResponseNodeObject
		{
			String content;
		}
	}

	@Data
	public static class ServerUserDataUnauthenticated
	{
		int id;
		String user;
		String avatar;
	}

	@Data
	public static class ServerUserDataAuthenticated extends ServerUserDataUnauthenticated
	{
		String email;
		ArrayList<OwnerNode> owner = new ArrayList<>();
		@Data
		public static class OwnerNode
		{
			int id;
			String title;
			String preview;
			int users;
		}
		ArrayList<MemberNode> member = new ArrayList<>();
		@Data
		public static class MemberNode
		{
			int id;
			String title;
			String owner;
			String preview;
		}
	}

	@Data
	public static class ClientUserData
	{
		ClientAgent agent = new ClientAgent();
	}

	@Data
	public static class ServerRegister
	{
		boolean success;
		String error;
		String token;
		ServerAgent agent = new ServerAgent();
	}

	@Data
	public static class ClientChat
	{
		String type;
		int bench;
		String message;
		String level;
		ClientAgent agent = new ClientAgent();
	}

	@Data
	public static class ServerTextSelect extends ServerTextBase
	{
		Content content = new Content();
		@Data
		class Content
		{
			int being;
			int end;
		}
	}

	@Data
	public static class ServerTextModify extends ServerTextBase
	{
		Content content = new Content();
		@Data
		class Content
		{
			int begin;
			int end;
			String data;
		}
	}

	@Data
	public static class ServerTextCursor extends ServerTextBase
	{
		Content content = new Content();
		@Data
		class Content
		{
			int index;
			boolean show;
		}
	}

	@Data
	public static class ServerTextBase
	{
		String type;
		int node;
		int bench;
		WSServerAgent agent = new WSServerAgent();
	}

	@Data
	public static class Notify extends EmptyAgent
	{
		String type;
		Data data = new Data();
		@lombok.Data
		public static class Data
		{
			String header;
			String text;
			String link;
			int time;
		}
	}

	@Data
	public static class ServerWatch extends ServerBase
	{
		String interval;
	}

	@Data
	public static class ServerChat extends ServerBase
	{
		String message;
		String level;
	}

	@Data
	public static class ServerBase
	{
		String type;
		int bench;
		WSServerAgent agent = new WSServerAgent();
	}

	@Data
	public static class ServerResize extends WS
	{
		WH dimensions = new WH();
	}

	@Data
	public static class ServerRename extends WS
	{
		Content content = new Content();
		@Data
		public static class Content
		{
			String title;
		}
	}

	@Data
	public static class ServerMove extends WS
	{
		XY dimensions = new XY();
	}

	@Data
	public static class ServerEdit extends WS
	{
		Content content = new Content();
		@Data
		public static class Content
		{
			String type;
			String data;
		}
	}

	@Data
	public static class ServerCreate extends WS
	{
		FullDimensions dimensions = new FullDimensions();
		Content content = new Content();
		@Data
		public static class Content
		{
			String title;
			String type;
			String data;
		}
	}

	@Data
	public static class WS
	{
		String type;
		int node;
		int bench;
		WSServerAgent agent = new WSServerAgent();

	}

	@Data
	public static class FullDimensions
	{
		int x;
		int y;
		int w;
		int h;
	}

	@Data
	public static class WSServerAgent
	{
		public WSServerAgent() { }

		public WSServerAgent(User user)
		{
			name = user.Username;
			id = user.Id;
		}

		String name;
		int id;
	}

	@Data
	public static class ClientTextSelect extends WC
	{
		Content content = new Content();
		@Data
		class Content
		{
			int begin;
			int end;
		}
	}

	@Data
	public static class ClientTextModify extends WC
	{
		Content content = new Content();
		@Data
		class Content
		{
			int begin;
			int end;
			String data;
		}
	}

	@Data
	public static class ClientTextCursor extends WC
	{
		Content content = new Content();
		@Data
		class Content
		{
			int index;
			boolean show;
		}
	}

	@Data
	public static class ClientResize extends WC
	{
		WH dimensions = new WH();
	}

	@Data
	public static class ClientMove extends WC
	{
		XY dimensions = new XY();
	}

	@Data
	public static class XY
	{
		int x;
		int y;
	}

	@Data
	public static class WC
	{
		String type;
		int node;
		int bench;
		ClientAgent agent = new ClientAgent();
	}

	@Data
	public static class RenameUserNode
	{
		int node;
		Content content = new Content();
		ClientAgent agent = new ClientAgent();
		@Data
		class Content
		{
			String title;
		}
	}

	@Data
	public static class EditUserNode
	{
		int node;
		Content content = new Content();
		ClientAgent agent = new ClientAgent();
		@Data
		class Content
		{
			String type;
			String data;
		}
	}

	@Data
	public static class DeleteUserNode
	{
		int node;
		ClientAgent agent = new ClientAgent();
	}

	@Data
	public static class CreateUserNode
	{
		WH dimensions = new WH();
		Content content = new Content();
		ClientAgent agent = new ClientAgent();
		@Data
		class Content
		{
			String title;
			String type;
			String data;
		}
	}

	@Data
	public static class IgnoreWatchNode
	{
		int node;
		ClientAgent agent = new ClientAgent();
	}

	@Data
	public static class BenchNodeSpawn
	{
		XY dimensions = new XY();
		Content content = new Content();
		ClientAgent agent = new ClientAgent();
		@Data
		class Content
		{
			String retitle;
			int node;
		}
	}

	@Data
	public static class BenchNodeResize
	{
		int node;
		WH dimensions = new WH();
		ClientAgent agent = new ClientAgent();
	}

	@Data
	public static class BenchNodeRename
	{
		int node;
		Content content = new Content();
		ClientAgent agent = new ClientAgent();
		@Data
		public static class Content
		{
			String title;
		}
	}

	@Data
	public static class BenchNodeMove
	{
		int node;
		XY dimensions = new XY();
		ClientAgent agent = new ClientAgent();
	}

	@Data
	public static class BenchNodeEdit
	{
		int node;
		Content content = new Content();
		ClientAgent agent = new ClientAgent();
		@Data
		public static class Content
		{
			String type;
			String data;
		}
	}

	@Data
	public static class BenchNodeDelete
	{
		int node;
		ClientAgent agent = new ClientAgent();
	}

	@Data
	public static class BenchNodeCreate
	{
		FullDimensions dimensions = new FullDimensions();
		Content content = new Content();
		ClientAgent agent = new ClientAgent();
		@Data
		public static class Content
		{
			String title;
			String type;
			String data;
		}
	}

	@Data
	public static class BenchNodeCopy
	{
		Content content = new Content();
		ClientAgent agent = new ClientAgent();
		@Data
		public static class Content
		{
			String retitle;
			int node;
		}
	}

	@Data
	public static class BenchEdit
	{
		int id;
		WH dimensions = new WH();
		Content content = new Content();
		ClientAgent agent = new ClientAgent();

		@Data
		public static class Content
		{
			String title;
			String background;
		}
	}

	@Data
	public static class BenchEditResponse
	{
		boolean dimensions;
		boolean background;
		boolean title;
	}

	@Data
	public static class BenchDelete
	{
		int id;
		ClientAgent agent = new ClientAgent();
	}

	@Data
	public static class IdResponse
	{
		int id;
	}

	@Data
	public static class BenchCreate
	{
		String title;
		WH dimensions = new WH();
		ClientAgent agent = new ClientAgent();
	}

	@Data
	public static class WH
	{
		int w;
		int h;
	}

	@Data
	public static class GetUsersResponse
	{
		String creator;
		ArrayList<MetaUser> users = new ArrayList<>();

	}

	@Data
	public static class MetaUser
	{
		int id;
		String user;
		String permission;
		boolean online;
	}

	@Data
	public static class ContainedClientAgent
	{
		ClientAgent agent = new ClientAgent();
	}

	@Data
	public static class UserModObject
	{
		int userId;
		String permission;
		ClientAgent agent = new ClientAgent();
	}

	@Data
	public static class UserModNoPermObject
	{
		int userId;
		ClientAgent agent = new ClientAgent();
	}

	@Data
	public static class BooleanResponse
	{
		boolean result;
	}

	@Data
	public static class Register
	{
		String username;
		String password;
		String email;
	}

	@Data
	public static class Empty { }

	@Data
	public static class EmptyAgent
	{
		Agent agent = new Agent();
		@Data
		class Agent
		{

		}
	}

	@Data
	public static class ServerAgent
	{
		int id;
		String user;
	}

	@Data
	public static class ClientAgent
	{
		int id;
		String token;
	}

	@Data
	public static class Authenticate
	{
		String token;
		int id;
	}

	@Data
	public static class ClientLogin
	{
		String loginkey;
		String password;
	}

	@Data
	public static class ServerLogin
	{
		String token;
		ServerAgent agent = new ServerAgent();
	}

	@Data
	public static class ClientToken
	{
		String token;
	}

	public Object get() { return this.cClass; }

	private Object cClass;

	Protocol (Object object)
	{
		cClass = object;
	}

	public static class Out
	{
		public static final String VERIFY = "verify";
		public static final String CREATE = "create";
		public static final String DELETE = "delete";
		public static final String EDIT = "edit";
		public static final String MOVE = "move";
		public static final String RENAME = "rename";
		public static final String RESIZE = "resize";
		public static final String CHAT = "chat";
		public static final String IGNORE = "ignore";
		public static final String MOD = "mod";
		public static final String NOTIFY = "notify";
		public static final String WATCH = "watch";
		public static final String TEXTCURSOR = "textcursor";
		public static final String TEXTMODIFY = "textmodify";
		public static final String TEXTSELECT = "textselect";
	}
}

