package online.workbench.model.struct;

import java.util.Map;

public class Bench
{
	public int Id;
	public User Owner;
	public String Title;
	public long Created;
	public Map<Integer, PermissionLevel> Users;
	public Map<Integer, BenchNode> Nodes;
	public Dimensions Dimensions;
	public boolean Archived;
	public String Background;
	public String Preview;

	public Bench(int id, User owner, String title, long created, Map<Integer, PermissionLevel> users, Map<Integer, BenchNode> nodes, int width, int height, boolean archived, String background, String preview)
	{
		Id = id;
		Owner = owner;
		Title = title;
		Created = created;
		Users = users;
		Nodes = nodes;
		Dimensions = new Dimensions(width, height);
		Archived = archived;
		Background = background;
		Preview = preview;
	}
}
