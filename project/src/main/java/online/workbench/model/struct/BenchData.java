package online.workbench.model.struct;

import java.util.Date;

public class BenchData
{
	public int Id;
	public String Title;
	public Date Created;
	public boolean Archived;
	public String PreviewImagePath;

	public BenchData(int id, String title, Date created, boolean archived, String previewImagePath)
	{
		Id = id;
		Title = title;
		Created = created;
		Archived = archived;
		PreviewImagePath = previewImagePath;
	}
}
