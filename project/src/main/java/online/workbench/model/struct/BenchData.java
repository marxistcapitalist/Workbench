package online.workbench.model.struct;

import java.util.Date;

public class BenchData
{
	public int Id;
	public int OwnerId;
	public String Owner;
	public String Title;
	public long Created;
	public boolean Archived;
	public String PreviewImagePath;

	public BenchData(int id, String owner, int ownerId, String title, long created, boolean archived, String previewImagePath)
	{
		Id = id;
		Title = title;
		Created = created;
		Archived = archived;
		PreviewImagePath = previewImagePath;
		Owner = owner;
		OwnerId = ownerId;
	}
}
