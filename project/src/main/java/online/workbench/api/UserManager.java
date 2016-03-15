package online.workbench.api;

import online.workbench.WorkbenchDB;
import online.workbench.model.struct.User;
import online.workbench.security.Encrypt;

import java.util.ArrayList;

public class UserManager
{
	private ArrayList<User> users;
	private WorkbenchDB database;

	public UserManager(WorkbenchDB database)
	{
		users = new ArrayList<>();
		this.database = database;
	}

	private User get(int id)
	{
		return users.stream().filter(u -> u.Id == id).findFirst().get();
	}

	private User get(String loginKey)
	{
		if (loginKey.contains("@"))
		{
			return users.stream().filter(u -> u.Email.equalsIgnoreCase(loginKey)).findFirst().get();
		}
		else
		{
			return users.stream().filter(u -> u.Username.equalsIgnoreCase(loginKey)).findFirst().get();
		}
	}

	public User load(int id)
	{
		if (!isLoaded(id))
		{
			User user = this.database.loadUser(id);

			if (user != null)
			{
				this.users.add(user);
				return user;
			}
			return null;
		}
		return this.get(id);
	}

	public User refresh(User user)
	{
		User u = this.database.loadUser(user.Id);

		if (u != null && u.Id != 0)
		{
			this.users.add(u);
			return u;
		}
		return null;
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

	public boolean updatePassword(User user, String password)
	{
		return true;
	}

	public boolean updateUsername(User user, String username)
	{
		return true;
	}

	public boolean updateEmail(User user, String email)
	{
		return true;
	}

	public boolean updateAvatar(User user, String avatar)
	{
		return true;
	}

	public int validateUser(String loginKey, String password)
	{
		String hash = null;
		int id = this.database.grabId(loginKey.toLowerCase());
		if (loginKey.contains("@"))
		{
			String username = this.database.grabUser(loginKey.toLowerCase());
			if (username != null && id != 0)
			{
				hash = Encrypt.hash(username, password, loginKey, id);
			}
		}
		else
		{
			String email = this.database.grabEmail(loginKey.toLowerCase());
			if (email != null && id != 0)
			{
				hash = Encrypt.hash(loginKey, password, email, id);
			}
		}

		if (hash != null)
		{
			boolean result = this.database.validateUserLogin(id, hash);
			if (result)
			{
				load(id);
				return id;
			}
			return 0;
		}
		return 0;
	}

	/**
	 * Does not do any duplicate checking
	 * (this is currently done in the endpoint def)
	 */
	public User createUser(String username, String password, String email)
	{
		int id = this.database.createNewUser(username.toLowerCase(), email.toLowerCase());
		String hash = Encrypt.hash(username.toLowerCase(), password, email.toLowerCase(), id);
		this.database.setPassword(id, hash);
		return load(id);
	}
}
