package online.workbench.websocket.data;


import online.workbench.websocket.util.Logger;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection
{
	public SQLiteDatabase(String databaseName)
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			_connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName + ".db");
			Logger.success("SQLite database opened successfully");
		}
		catch (Exception e)
		{
			Logger.error("SQLite database failed to open");
			e.printStackTrace();
		}
	}

	public void submit(String sql) throws SQLException
	{
		Statement statement = _connection.createStatement();
		int result = statement.executeUpdate(sql);
		_connection.commit();
		statement.close();
	}

	public Object query(String sql) throws SQLException
	{
		Statement statement = _connection.createStatement();
		ResultSet result = statement.executeQuery(sql);
		_connection.commit();
		result.
				statement.close();
	}

	public void close()
	{
		try
		{
			_connection.close();
		}
		catch (SQLException e)
		{
			Logger.warn("SQLite database did not close correctly");
			e.printStackTrace();
		}
	}
}
