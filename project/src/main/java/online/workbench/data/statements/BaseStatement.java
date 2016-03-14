package online.workbench.data.statements;

import online.workbench.data.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;

public abstract class BaseStatement<T> implements Callable<T>
{

	protected DataSource _database;
	protected Connection _connection;
	protected PreparedStatement _statement;
	protected ResultSet _result;

	public BaseStatement(DataSource database)
	{
		_database = database;
	}

	@Override
	public T call() throws SQLException
	{
		try
		{
			_connection = _database.getConnection();

			if (_connection != null)
			{
				return doWork();
			}
			else
			{
				throw new SQLException("A connection to the database could not be established");
			}

		}
		catch (SQLException e)
		{
			throw e;

		}
		finally
		{
			if (_connection != null)
			{
				try
				{
					_connection.close();
				} catch (SQLException ex)
				{
				}
			}
			if (_statement != null)
			{
				try
				{
					_statement.close();
				} catch (SQLException ex)
				{
				}
			}
			if (_result != null)
			{
				try
				{
					_result.close();
				} catch (SQLException ex)
				{
				}
			}
		}
	}

	/**
	 * Called once the connection is initialized.
	 *
	 * @return The result of the work, if it is able to complete successfully.
	 * @throws SQLException On sql failure.
	 */
	protected abstract T doWork() throws SQLException;

	/**
	 * Prepares a new statement using the connection, while also closing any previous prepared
	 * statement.
	 *
	 * @param sql The query to prepare.
	 * @throws SQLException On sql failure.
	 */
	protected void prepareStatement(String sql) throws SQLException
	{
		if (_statement != null)
		{
			_statement.close();
		}

		_statement = _connection.prepareStatement(sql);
	}

	/**
	 * Gets results and stores them in the _result field, while also closing any
	 * previous result set.
	 *
	 * @throws SQLException On sql failure.
	 */
	protected void executeQuery() throws SQLException
	{
		if (_result != null)
		{
			_result.close();
		}
		_result = _statement.executeQuery();
	}

}