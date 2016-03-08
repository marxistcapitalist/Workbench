package online.workbench.data.struct;


import online.workbench.websocket.data.struct.PermissionLevel;
import online.workbench.websocket.data.struct.User;

import java.util.Date;
import java.util.Map;

public class Bench
{
	public int Id;
	public User Owner;
	public String Title;
	public Date Created;
	public Map<User, PermissionLevel> Users;
	public Map<Integer, BenchNode> Nodes;
	public Dimensions Dimensions;
	public boolean Archived;

	public Bench(int id, User owner, String title, Date created, Map<User, PermissionLevel> users, Map<Integer, BenchNode> nodes, int width, int height, boolean archived)
	{
		Id = id;
		Owner = owner;
		Title = title;
		Created = created;
		Users = users;
		Nodes = nodes;
		Dimensions = new Dimensions(width, height);
		Archived = archived;
	}
}
