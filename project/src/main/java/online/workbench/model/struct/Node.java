package online.workbench.model.struct;

import java.util.Date;

public abstract class Node
{
	public int Id;
	public String Title;
	public ContentType ContentType;
	public String Content;
	public Date Created;
	public Date LastEdit;

	protected Node(int id, String title, ContentType contentType, String content, Date created, Date lastEdit)
	{
		Id = id;
		Title = title;
		ContentType = contentType;
		Content = content;
		Created = created;
		LastEdit = lastEdit;
	}
}
