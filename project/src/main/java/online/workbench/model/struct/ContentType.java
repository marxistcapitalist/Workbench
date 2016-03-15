package online.workbench.model.struct;

import online.workbench.api.Protocol;

public enum ContentType
{
	NONE(""),
	TEXT("text"),
	IMAGE("image"),
	VIDEO("video"),
	G_DOC("googledoc"),
	TWEET("tweet"),
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

	public static ContentType get(String type)
	{
		switch (type.toLowerCase())
		{
			case "text":
				return ContentType.TEXT;
			case "image":
				return ContentType.IMAGE;
			case "video":
				return ContentType.VIDEO;
			case "googledoc":
				return ContentType.G_DOC;
			case "tweet":
				return ContentType.TWEET;
			case "iframe":
				return ContentType.IFRAME;
			default:
				return ContentType.NONE;
		}
	}
}
