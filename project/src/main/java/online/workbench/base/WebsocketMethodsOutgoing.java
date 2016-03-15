package online.workbench.base;

import online.workbench.model.struct.*;

public interface WebsocketMethodsOutgoing
{
	/**
	 * Tells a single user of the creation of a usernode
	 */
	//void sendUserNodeCreate(User user, UserNode node);

	/**
	 * Tells a single user of the deletion of a usernode
	 */
	//void sendUserNodeDelete(User user, UserNode node);

	/**
	 * Tells a single user of the content edit of a usernode
	 */
	//void sendUserNodeEdit(User user, UserNode node, String content);

	/**
	 * Tells a single user of the renaming of a usernode
	 */
	//void sendUserNodeRename(User user, UserNode node, String name);

	/**
	 * Tells a single user of the resizing of a usernode
	 */
	//void sendUserNodeResize(User user, UserNode node, int w, int h);

	/**
	 * Tells a single user of the type edit of a usernode
	 */
	//void sendUserNodeTypeEdit(User user, UserNode node, ContentType type);

	/**
	 * Sends a chat message to all online users of a workbench
	 */
	//void sendChat(Bench bench, User user, String message);

	/**
	 * Notifies a single user
	 */
	//void sendNotifyUser(User user, String header, String text, String link, int time);

	/**
	 * Notifies all online users of a bench
	 */
	void sendNotifyBench(Bench bench, String header, String text, String link, int time);

	/**
	 * Notifies all online users of workbench
	 */
	//void sendNotifyGlobal(String header, String text, String link, int time);

	/**
	 * Notifies all online users of a bench of cursor movement within a benchnode
	 */
	//void sendTextCursor(Bench bench, User user, Node node, int index, boolean show);

	/**
	 * Notifies all online users of a bench of text content modification within a benchnode
	 */
	//void sendTextModify(Bench bench, User user, Node node, int begin, int end, String data);

	/**
	 * Notifies all online users of a bench of text selection within a benchnode
	 */
	//void sendTextSelect(Bench bench, User user, Node node, int begin, int end);

	/**
	 * Notifies all online users of a bench that a user has been added to that bench
	 */
	void sendUserAdd(Bench bench, User user, PermissionLevel level);

	/**
	 * Notifies all online users of a bench that a user in that bench has been promoted/demoted
	 */
	void sendUserModify(Bench bench, User user, PermissionLevel level);

	/**
	 * Notifies all online users of a bench that a user has been removed from that bench
	 */
	void sendUserRemove(Bench bench, User user);

	/**
	 * Notifies all online users of a bench that a benchnode has been created in that bench
	 */
	void sendBenchNodeCreate(Bench bench, User user, BenchNode node);

	/**
	 * Notifies all online users of a bench that a benchnode has been deleted in that bench
	 */
	void sendBenchNodeDelete(Bench bench, User user, int node);

	/**
	 * Notifies all online users of a bench that the content or type of a benchnode has been edited
	 */
	void sendBenchNodeEdit(Bench bench, User user, int node, String content, ContentType type);

	/**
	 * Notifies all online users of a bench that the position of a benchnode has changed
	 */
	void sendBenchNodeMove(Bench bench, User user, int node, int x, int y);

	/**
	 * Notifies all online users that the title of a benchnode has been changed
	 */
	void sendBenchNodeRename(Bench bench, User user, int node, String name);

	/**
	 * Notifies all online users that the width or height of a benchnode has changed
	 */
	void sendBenchNodeResize(Bench bench, User user, int node, int w, int h);
}
