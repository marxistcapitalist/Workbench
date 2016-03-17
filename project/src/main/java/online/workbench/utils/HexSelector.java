package online.workbench.utils;

public class HexSelector
{
	private static String[] colors = {"#40e0d0", "#003366", "#468499", "#666666", "#088da5", "#8b0000", "#81d8d0", "#4169e1", "#3399ff"};

	public static String sel()
	{
		int index = ((int) (Math.random() * colors.length));
		return colors[index];
	}
}
