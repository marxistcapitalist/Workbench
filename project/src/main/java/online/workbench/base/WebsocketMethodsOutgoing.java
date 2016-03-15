package online.workbench.base;

import online.workbench.model.struct.*;

public interface WebsocketMethodsOutgoing
{
	void sendUserNodeCreate(Bench bench, User user, UserNode node);

	void sendUserNodeDelete(Bench bench, User user, UserNode node);

	void sendUserNodeEdit(Bench bench, User user, UserNode node, String content);

	void sendUserNodeMove(Bench bench, User user, UserNode node, int x, int y);

	void sendUserNodeRename(Bench bench, User user, UserNode node, String name);

	void sendUserNodeResize(Bench bench, User user, UserNode node, int w, int h);

	void sendUserNodeTypeEdit(Bench bench, User user, UserNode node, ContentType type);

	void sendChat(Bench bench, User user, String message);

	void sendNotifyUser(User user, String header, String text, String link, int time);

	void sendNotifyBench(Bench bench, String header, String text, String link, int time);

	void sendNotifyGlobal(String header, String text, String link, int time);

	void sendTextCursor(Bench bench, User user, Node node, int index, boolean show);

	void sendTextModify(Bench bench, User user, Node node, int begin, int end, String data);

	void sendTextSelect(Bench bench, User user, Node node, int begin, int end);

	void sendUserAdd(Bench bench, User user, PermissionLevel level);

	void sendUserModify(Bench bench, User user, PermissionLevel level);

	void sendUserRemove(Bench bench, User user);

	void sendBenchNodeCreate(Bench bench, User user, BenchNode node);

	void sendBenchNodeDelete(Bench bench, User user, int node);

	void sendBenchNodeEdit(Bench bench, User user, int node, String content);

	void sendBenchNodeMove(Bench bench, User user, int node, int x, int y);

	void sendBenchNodeRename(Bench bench, User user, int node, String name);

	void sendBenchNodeResize(Bench bench, User user, int node, int w, int h);

	void sendBenchNodeTypeEdit(Bench bench, User user, int node, ContentType type);
}
