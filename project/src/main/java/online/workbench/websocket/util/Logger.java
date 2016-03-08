package online.workbench.websocket.util;

public class Logger
{

	public static void success(String message)
	{
		System.out.println("[SUCCESS] " + message + ".");
	}

	public static void info(String message)
	{
		System.out.println("[INFO] " + message + ".");
	}

	public static void warn(String message)
	{
		System.out.println("[WARNING] " + message + ".");
	}

	public static void error(String message)
	{
		System.out.println("[ERROR] " + message + ".");
	}
}
