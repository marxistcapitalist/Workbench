package online.workbench.managers;

import online.workbench.data.WorkbenchDB;
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
		if (id != 0)
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
		return null;
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
		for (User user: this.users)
		{
			if (user.Id == id)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Necessary to be implemented in production, but with the single machine setup is not
	 */
	public void clean()
	{

	}

	public boolean updatePassword(User user, String password)
	{
		String hash = Encrypt.hash(user.Username, password, user.Email, user.Id);

		if (!hash.isEmpty())
		{
			this.database.updateUserPasswordAsync(user.Id, hash);
			return true;
		}
		return false;
	}

	public boolean updateUsername(User user, String username)
	{
		if (!username.isEmpty())
		{
			user.Username = username;
			this.database.updateUserNameAsync(user.Id, username);
			return true;
		}
		return false;
	}

	public boolean updateEmail(User user, String email)
	{
		if (!email.isEmpty() && email.length() <= 254)
		{
			user.Email = email;
			this.database.updateUserEmailAsync(user.Id, email);
			return true;
		}
		return false;
	}

	public boolean updateAvatar(User user, String avatar)
	{
		if (!avatar.isEmpty())
		{
			if (avatar.startsWith("#") && avatar.length() != 7)
			{
				return false;
			}
			user.Avatar = avatar;
			this.database.updateUserAvatarAsync(user.Id, avatar);
			return true;
		}
		return false;
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
