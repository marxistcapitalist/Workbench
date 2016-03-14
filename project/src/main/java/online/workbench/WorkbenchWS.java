package online.workbench;

import online.workbench.base.WebsocketMethodsOutgoing;
import online.workbench.model.struct.Bench;
import online.workbench.model.struct.ContentType;
import online.workbench.model.struct.Node;
import online.workbench.model.struct.User;

public class WorkbenchWS implements WebsocketMethodsOutgoing
{
	private WorkbenchAPI api;

	public WorkbenchWS(WorkbenchAPI api)
	{
		api.setWebsocket(this);
		api.getBenchManager().setWebsocket(this);
		this.api = api;
	}

	@Override
	public void sendCreate(Bench bench, User user, Node node)
	{

	}

	@Override
	public void sendDelete(Bench bench, User user, Node node)
	{

	}

	@Override
	public void sendEdit(Bench bench, User user, Node node, String content)
	{

	}

	@Override
	public void sendMove(Bench bench, User user, Node node, int x, int y)
	{

	}

	@Override
	public void sendRename(Bench bench, User user, Node node, String name)
	{

	}

	@Override
	public void sendResize(Bench bench, User user, Node node, int w, int h)
	{

	}

	@Override
	public void sendTypeEdit(Bench bench, User user, Node node, ContentType type)
	{

	}

	@Override
	public void sendChat(Bench bench, User user, String message)
	{

	}

	@Override
	public void sendNotifyUser(User user, String header, String text, String link, int time)
	{

	}

	@Override
	public void sendNotifyBench(Bench bench, String header, String text, String link, int time)
	{

	}

	@Override
	public void sendNotifyGlobal(String header, String text, String link, int time)
	{

	}

	@Override
	public void sendTextCursor(Bench bench, User user, Node node, int index, boolean show)
	{

	}

	@Override
	public void sendTextModify(Bench bench, User user, Node node, int begin, int end, String data)
	{

	}

	@Override
	public void sendTextSelect(Bench bench, User user, Node node, int begin, int end)
	{

	}
}
