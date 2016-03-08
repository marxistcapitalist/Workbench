package online.workbench.data.struct;

public enum ContentType
{
	TEXT("Text"),
	IMAGE("Image"),
	VIDEO("Video"),
	G_DOC("Google Docs"),
	TWEET("Tweet"),
	IFRAME("iFrame");

	private String _label;

	ContentType(String label)
	{
		_label = label;
	}
	
	@Override
	public String toString()
	{
		return _label;
	}
}
