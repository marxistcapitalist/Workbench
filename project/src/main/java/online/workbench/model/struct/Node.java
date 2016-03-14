package online.workbench.model.struct;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

public abstract class Node
{
	public int Id;
	public String Title;
	public ContentType ContentType;
	public String Content;
	public Date Created;
	public Date LastEdit;
	public boolean Archived;

	protected Node(int id, String title, ContentType contentType, String content, Date created, Date lastEdit)
	{
		Id = id;
		Title = title;
		ContentType = contentType;
		Content = content;
		Created = created;
		LastEdit = lastEdit;
		Archived = false;
	}

	protected Node(int id, String title, ContentType contentType, String content, Date created, Date lastEdit, boolean archived)
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
