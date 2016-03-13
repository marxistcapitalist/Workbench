package online.workbench.model.struct;

import java.util.List;
import java.util.Date;

public class UserNode extends Node
{
	public List<Bench> BenchList;

	public UserNode(int id, String title, ContentType contentType, String content, Date created, Date lastEdit, List<Bench> benchList)
	{
		super(id, title, contentType, content, created, lastEdit);
		BenchList = benchList;
	}

}
