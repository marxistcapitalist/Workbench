package online.workbench.model.struct;


public enum PermissionLevel
{
	NONE("", 0),
	VIEWER("Viewer", 1),
	//CONTRIBUTOR("Contributor", 2),
	EDITOR("Editor", 3),
	//DIRECTOR("Director", 4),
	OWNER("Owner", 5),
	ADMIN("Admin", 6);

	private String level;
	private int perm;

	PermissionLevel(String level, int perm)
	{
		this.level = level;
		this.perm = perm;
	}

	@Override
	public String toString()
	{
		return level;
	}

	public int val()
	{
		return perm;
	}

	public static PermissionLevel get(String level)
	{
		switch (level.toLowerCase())
		{
			case "viewer":
				return PermissionLevel.VIEWER;
			case "editor":
				return PermissionLevel.EDITOR;
			case "owner":
				return PermissionLevel.OWNER;
			case "admin":
				return PermissionLevel.ADMIN;
			default:
				return PermissionLevel.NONE;
		}
	}
}
