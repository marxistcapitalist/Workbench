package online.workbench.model.struct;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

public abstract class Node
{
	public int Id;
	public String Title;
	public ContentType ContentType;
	public String Content;
	public long Created;
	public long LastEdit;
	public boolean Archived;

	protected Node(int id, String title, ContentType contentType, String content, long created, long lastEdit, boolean archived)
	{
		Id = id;
		Title = title;
		ContentType = contentType;
		Content = content;
		Created = created;
		LastEdit = lastEdit;
		Archived = archived;
	}
}
