package online.workbench.model.struct;


public enum PermissionLevel
{
	NONE("", 0),
	VIEWER("Viewer", 1),
	EDITOR("Editor", 2),
	MANAGER("Manager", 3),
	OWNER("Owner", 4),
	ADMIN("Admin", 5);

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

	public static PermissionLevel get(int level)
	{
		switch (level)
		{
			case 1:
				return PermissionLevel.VIEWER;
			case 2:
				return PermissionLevel.EDITOR;
			case 4:
				return PermissionLevel.OWNER;
			case 5:
				return PermissionLevel.ADMIN;
			default:
				return PermissionLevel.NONE;
		}
	}
}
