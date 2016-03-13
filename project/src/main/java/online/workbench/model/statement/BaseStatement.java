package online.workbench.websocket.data.statement;

import online.workbench.websocket.util.Logger;

import java.sql.*;
import java.util.concurrent.Callable;

public abstract class BaseStatement<T> implements Callable<T>
{
	private String _dataSource;

	protected BaseStatement(String dataSource)
	{
		_dataSource = dataSource;
	}

	@Override
	public T call() throws Exception
	{
		Connection connection = null;
		Statement statement = null;

		try
		{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + _dataSource + ".db");
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			return doWork(statement);
		}
		catch (Exception e)
		{
			Logger.warn("Connection to dataSource: '" + _dataSource + "' failed");
			throw new Exception(e);
		}
		finally
		{
			if (connection != null) connection.commit();
			if (statement != null) statement.close();
			if (connection != null) connection.close();
		}
	}

	/**
	 * Override this to actually do work
	 * @return mixed
	 */
	protected abstract T doWork(Statement statement);
}
