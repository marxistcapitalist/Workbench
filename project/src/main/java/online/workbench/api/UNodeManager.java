package online.workbench.api;

import online.workbench.WorkbenchDB;
import online.workbench.model.struct.BenchNode;
import online.workbench.model.struct.User;
import online.workbench.model.struct.UserNode;

import java.util.ArrayList;

public class UNodeManager
{
	private ArrayList<UserNode> nodes;
	private WorkbenchDB db;

	public UNodeManager(WorkbenchDB db)
	{
		nodes = new ArrayList<>();
		this.db = db;
	}

	private UserNode get(int id)
	{
		return nodes.stream().filter(u -> u.Id == id).findFirst().get();
	}

	public BenchNode load(inz)
	{
		if (!isLoadedB(id))
		{
			BenchNode node = this.db.

			if (user != null)
			{
				this.users.add(user);
				return user;
			}
			return null;
		}
		return this.get(id);
	}

	public User refresh(int id)
	{
		if (this.get(id) != null)
		{
			User user = this.db.loadUser(id);

			if (user != null)
			{
				this.users.add(user);
			}
			return user;
		}
		return null;
	}

	public boolean isLoadedB(int id)
	{
		return true;
	}

	public boolean isLoadedU(int id)
	{
		return true;
	}

	public void clean()
	{

	}




}
