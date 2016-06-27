package online.workbench.model.struct;


public enum PermissionLevel
{
	NONE("", 0),
	VIEWER("Viewer", 1),
	EDITOR("Editor", 2),
	OWNER("Owner", 4);

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
		if (level == null) return PermissionLevel.NONE;

		switch (level.toLowerCase())
		{
			case "viewer":
				return PermissionLevel.VIEWER;
			case "editor":
				return PermissionLevel.EDITOR;
			case "owner":
				return PermissionLevel.OWNER;
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
			case 3:
				return PermissionLevel.OWNER;
			default:
				return PermissionLevel.NONE;
		}
	}
}
