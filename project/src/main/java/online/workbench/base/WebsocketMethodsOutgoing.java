package online.workbench.base;

import online.workbench.model.struct.Bench;
import online.workbench.model.struct.ContentType;
import online.workbench.model.struct.Node;
import online.workbench.model.struct.User;

public interface WebsocketMethodsOutgoing
{
	void sendCreate(Bench bench, User user, Node node);

	void sendDelete(Bench bench, User user, Node node);

	void sendEdit(Bench bench, User user, Node node, String content);

	void sendMove(Bench bench, User user, Node node, int x, int y);

	void sendRename(Bench bench, User user, Node node, String name);

	void sendResize(Bench bench, User user, Node node, int w, int h);

	void sendTypeEdit(Bench bench, User user, Node node, ContentType type);

	void sendChat(Bench bench, User user, String message);

	void sendNotifyUser(User user, String header, String text, String link, int time);

	void sendNotifyBench(Bench bench, String header, String text, String link, int time);

	void sendNotifyGlobal(String header, String text, String link, int time);

	void sendTextCursor(Bench bench, User user, Node node, int index, boolean show);

	void sendTextModify(Bench bench, User user, Node node, int begin, int end, String data);

	void sendTextSelect(Bench bench, User user, Node node, int begin, int end);

}
