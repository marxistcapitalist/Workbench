package online.workbench.model.struct;

public class Position
{
	public int X;
	public int Y;
	public int Width;
	public int Height;

	protected Position(int x, int y, int width, int height)
	{
		X = x;
		Y = y;
		Width = width;
		Height = height;
	}
}
