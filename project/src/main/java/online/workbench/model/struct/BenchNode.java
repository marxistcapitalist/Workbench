package online.workbench.model.struct;

import java.util.Date;

public class BenchNode extends Node
{
	public Bench Bench;
	public User Creator;
	public Position Position;

	public BenchNode(int id, Bench bench, User creator, int x, int y, int width, int height, String title, ContentType contentType, String content, Date created, Date lastEdit)
	{
		super(id, title, contentType, content, created, lastEdit);
		Bench = bench;
		Creator = creator;
		Position = new Position(x, y, width, height);
	}
}
