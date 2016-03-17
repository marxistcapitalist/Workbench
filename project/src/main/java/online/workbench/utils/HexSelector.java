package online.workbench.utils;

public class HexSelector
{
	private static String[] colors = {"#7697b0", "#7dffce", "#451010", "#0038b8", "#53d769", "#147efb", "#fc3d39", "#0f9d58", "#db4437", "#df598c", "#8cdf59", "#598cdf", "#59cfdf", "#0a6d3d", "#412406", "#fc3936", "#feff89", "#0a3a6d", "#6d0a3a", "#ffeb8d", "#78d8e7", "#e5b8d3", "#f9f6fb", "#e3d5ee", "#f2f6f8", "#f9f0f6", "#7b224d", "#fcfafd", "#634051", "#3d1126", "#f6fff5", "#751010", "#33cccc", "#470d6f", "#751010"};

	public static String sel()
	{
		int index = ((int) (Math.random() * colors.length));
		return colors[index];
	}
}
