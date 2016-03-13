package online.workbench.api;

import online.workbench.model.struct.User;

import java.util.ArrayList;

public class UserManager
{
	private ArrayList<User> users;

	public UserManager()
	{
		users = new ArrayList<>();
	}

	public boolean isLoaded(int id)
	{
		return true;
	}

	public boolean isLoaded(User user)
	{
		return true;
	}

	public void put(User user)
	{

	}

	public void clean()
	{

	}



}
