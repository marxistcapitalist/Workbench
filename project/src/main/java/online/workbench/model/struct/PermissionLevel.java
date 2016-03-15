package online.workbench.model.struct;


public enum PermissionLevel
{
	NONE(""),
	VIEWER("Viewer"),
	//CONTRIBUTOR("Contributor"),
	EDITOR("Editor"),
	//DIRECTOR("Director"),
	OWNER("Owner"),
	ADMIN("Admin");

	private String _level;

	PermissionLevel(String level)
	{
		_level = level;
	}

	@Override
	public String toString()
	{
		return _level;
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
