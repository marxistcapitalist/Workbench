package online.workbench.api;

import online.workbench.WorkbenchDB;
import online.workbench.model.struct.User;

import java.util.ArrayList;

public class UserManager
{
	private ArrayList<User> users;
	private WorkbenchDB db;

	public UserManager(WorkbenchDB db)
	{
		users = new ArrayList<User>();
		this.db = db;
	}

	private User get(int id)
	{
		return users.stream().filter(u -> u.Id == id).findFirst().get();
	}

	public User load(int id)
	{
		if (!isLoaded(id))
		{
			User user = this.db.loadUser(id);

			if (user != null)
			{
				this.users.add(user);
				return user;
			}
			return null;
		}
		return this.get(id);
	}

	public void loadAsync(int id)
	{

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

	public void refreshAsync(int id)
	{

	}

	public void saveAsync(int id)
	{

	}

	public boolean isLoaded(int id)
	{
		return true;
	}

	public boolean isLoaded(User user)
	{
		return true;
	}

	public void clean()
	{

	}

	public boolean updatePassword(String password)
	{
		return true;
	}

	public boolean updateUsername(String username)
	{
		return true;
	}

	public boolean updateEmail(String email)
	{
		return true;
	}

	public boolean updateAvatar(String avatar)
	{
		return true;
	}



}
