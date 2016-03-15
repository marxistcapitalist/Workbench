package online.workbench.model.struct;

import java.util.List;
import java.util.Date;

public class UserNode extends Node
{
	public List<Bench> BenchList;

	public UserNode(int id, String title, ContentType contentType, String content, long created, long lastEdit, List<Bench> benchList, boolean archived)
	{
		super(id, title, contentType, content, created, lastEdit, archived);
		BenchList = benchList;
	}

}
