package online.workbench.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeConverter
{
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String get(long milliseconds)
	{
		return format.format(new Date(milliseconds));
	}
}
