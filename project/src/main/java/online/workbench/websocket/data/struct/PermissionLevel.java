package online.workbench.websocket.data.struct;


public enum PermissionLevel
{
	VIEWER("Viewer"),
	CONTRIBUTOR("Contributor"),
	EDITOR("Editor"),
	DIRECTOR("Director"),
	OWNER("Owner");

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
}
