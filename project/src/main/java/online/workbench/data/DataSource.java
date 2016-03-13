package online.workbench.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource
{
	private String _name;

	public DataSource(String name)
	{
		_name = name;
	}

	public Connection getConnection()
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection("jdbc:sqlite:" + _name + ".db");
		}
		catch (ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
