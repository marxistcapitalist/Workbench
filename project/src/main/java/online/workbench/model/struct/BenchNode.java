package online.workbench.model.struct;

import java.util.Date;

public class BenchNode extends Node
{
	public Bench Bench;
	public User Creator;
	public Position Position;

	public BenchNode(int id, Bench bench, User creator, int x, int y, int width, int height, String title, ContentType contentType, String content, long created, long lastEdit, boolean archived)
	{
		super(id, title, contentType, content, created, lastEdit, archived);
		Bench = bench;
		Creator = creator;
		Position = new Position(x, y, width, height);
	}
}
