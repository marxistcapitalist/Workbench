package online.workbench.data;

import online.workbench.data.initialization.Statement;
import online.workbench.managers.TokenManager;
import online.workbench.base.DatabaseMethods;
import online.workbench.model.struct.*;

import java.sql.*;
import java.util.ArrayList;

/**
 * Methods labeled as asynchronous in this class are actually blocking methods.
 * The framework was designed for a production environment, but due to the nature
 * of how it is being presented, asynchronisity is not required
 */
public class WorkbenchDB implements DatabaseMethods
{
	public static String name;

	public WorkbenchDB(String name)
	{
		WorkbenchDB.name = "jdbc:sqlite:" + name + ".db";
		initialize();
	}

	private Connection getConnection()
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection("jdbc:sqlite:" + name + ".db");
		}
		catch (ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private void initialize()
	{

	}

	@Override
	public void updateUserEmailAsync(int id, String email)
	{
		try
		{
			Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(Statement.UPDATE_EMAIL);
			statement.setString(1, email.toLowerCase());
			statement.setInt(2, id);
			statement.executeUpdate();
			statement.close();
			connection.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void updateUserNameAsync(int id, String name)
	{
		try
		{
			Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(Statement.UPDATE_USERNAME);
			statement.setString(1, name.toLowerCase());
			statement.setInt(2, id);
			statement.executeUpdate();
			statement.close();
			connection.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void updateUserAvatarAsync(int id, String avatar)
	{
		try
		{
			Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(Statement.UPDATE_AVATAR);
			statement.setString(1, avatar);
			statement.setInt(2, id);
			statement.executeUpdate();
			statement.close();
			connection.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void updateUserPasswordAsync(int id, String passwordHash)
	{
		try
		{
			Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(Statement.UPDATE_PASSWORD);
			statement.setString(1, passwordHash);
			statement.setInt(2, id);
			statement.executeUpdate();
			statement.close();
			connection.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void invalidateToken(String token)
	{
		try
		{
			Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(Statement.INVALIDATE_TOKEN);
			statement.setLong(1, System.currentTimeMillis());
			statement.setString(2, token.toUpperCase());
			statement.executeUpdate();
			statement.close();
			connection.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean checkUsernameAvailability(String username)
	{
		Connection connection = null;
		PreparedStatement statement = null;
		try
		{
			connection = this.getConnection();
			statement = connection.prepareStatement(Statement.CHECK_USERNAME_AVAILABILITY);
			statement.setString(1, username.toLowerCase());
			ResultSet result = statement.executeQuery();

			if (result.next())
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				statement.close();
				connection.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean checkEmailAvailability(String email)
	{
		return false;
	}

	@Override
	public TokenManager.Token loadToken(int id)
	{
		return null;
	}

	@Override
	public String generateToken(int id)
	{
		return null;
	}

	@Override
	public boolean validateUserLogin(int id, String passwordHash)
	{
		return false;
	}

	@Override
	public User loadUser(int userId)
	{
		return null;
	}

	@Override
	public Bench loadBench(int benchId)
	{
		return null;
	}

	@Override
	public int countBenchMembers(int benchId)
	{
		return 0;
	}

	@Override
	public int createNewUser(String username, String email)
	{
		return 0;
	}

	@Override
	public void setPassword(int id, String passwordHash)
	{

	}

	@Override
	public Bench createBench(User user, String title, int w, int h)
	{
		return null;
	}

	@Override
	public void archiveBench(int id)
	{

	}

	@Override
	public void submitNodeContentEditAsync(NodeType type, int id, String content)
	{

	}

	@Override
	public void submitNodeContentTypeEditAsync(NodeType type, int id, ContentType cType)
	{

	}

	@Override
	public void submitNodeMoveAsync(NodeType type, int id, int x, int y)
	{

	}

	@Override
	public void submitNodeResizeAsync(NodeType type, int id, int w, int h)
	{

	}

	@Override
	public void submitNodeRenameAsync(NodeType type, int id, String title)
	{

	}

	@Override
	public void submitNodeArchiveAsync(NodeType type, int id)
	{

	}

	@Override
	public BenchNode submitNodeCreate(BenchNode node)
	{
		return null;
	}

	@Override
	public String grabEmail(String username)
	{
		return null;
	}

	@Override
	public String grabUser(String email)
	{
		return null;
	}

	@Override
	public int grabId(String loginKey)
	{
		return 0;
	}

	@Override
	public void submitBenchBackgroundEdit(Bench bench, String background)
	{

	}

	@Override
	public void submitBenchTitleEdit(Bench bench, String title)
	{

	}

	@Override
	public void submitBenchResize(Bench bench, int w, int h)
	{

	}

	@Override
	public void addUserToBench(Bench bench, int user, PermissionLevel role)
	{

	}

	@Override
	public void removeUserFromBench(Bench bench, int user)
	{

	}

	@Override
	public void modifyUserInBench(Bench bench, int user, PermissionLevel role)
	{

	}
}
