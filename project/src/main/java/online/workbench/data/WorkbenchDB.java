package online.workbench.data;

import online.workbench.data.initialization.Statement;
import online.workbench.managers.TokenManager;
import online.workbench.base.DatabaseMethods;
import online.workbench.model.struct.*;
import online.workbench.security.Encrypt;
import online.workbench.security.Token;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

			if (!result.next())
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
		Connection connection = null;
		PreparedStatement statement = null;
		try
		{
			connection = this.getConnection();
			statement = connection.prepareStatement(Statement.CHECK_EMAIL_AVAILABILITY);
			statement.setString(1, email.toLowerCase());
			ResultSet result = statement.executeQuery();

			if (!result.next())
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
	public TokenManager.Token loadToken(int id)
	{
		Connection connection = null;
		PreparedStatement statement = null;
		try
		{
			connection = this.getConnection();
			statement = connection.prepareStatement(Statement.LOAD_TOKEN);
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();

			if (result.next())
			{
				String token = result.getString(1);
				long time = result.getLong(2);

				return new TokenManager.Token(token, time);
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
		return null;
	}

	@Override
	public String generateToken(int id)
	{
		long time = System.currentTimeMillis();
		String token = Token.gen();

		if (loadToken(id) != null)
		{
			try
			{
				Connection connection = this.getConnection();
				PreparedStatement statement = connection.prepareStatement(Statement.UPDATE_TOKEN);
				statement.setString(1, token);
				statement.setLong(2, time);
				statement.setInt(3, id);
				statement.executeUpdate();
				statement.close();
				connection.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				Connection connection = this.getConnection();
				PreparedStatement statement = connection.prepareStatement(Statement.INSERT_TOKEN);
				statement.setInt(1, id);
				statement.setString(2, token);
				statement.setLong(3, time);
				statement.executeUpdate();
				statement.close();
				connection.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return token;
	}

	@Override
	public boolean validateUserLogin(int id, String passwordHash)
	{
		Connection connection = null;
		PreparedStatement statement = null;
		try
		{
			connection = this.getConnection();
			statement = connection.prepareStatement(Statement.VALIDATE_USER_LOGIN);
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();

			if (result.next())
			{
				String hash = result.getString(1);

				if (hash.equalsIgnoreCase(passwordHash))
				{
					return true;
				}
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
	public User loadUser(int userId)
	{
		int id = userId;
		String username = null;
		String email = null;
		boolean confirmed = true;
		Map<Integer, UserNode> nodes = new HashMap<>();
		List<BenchData> benches = new ArrayList<>();
		String avatar = null;

		List<Integer> tempBenches = new ArrayList<>();

		Connection connection = null;
		PreparedStatement statement = null;

		try
		{
			connection = this.getConnection();
			statement = connection.prepareStatement(Statement.LOAD_USER__INFO);
			statement.setInt(1, userId);
			ResultSet result = statement.executeQuery();

			if (result.next())
			{
				username = result.getString(1);
				email = result.getString(2);
				avatar = result.getString(3);
				confirmed = result.getBoolean(4);
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
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}


		try
		{
			statement = connection.prepareStatement(Statement.LOAD_USER__BENCH_COUNT);
			statement.setInt(1, userId);
			ResultSet result = statement.executeQuery();

			while (result.next())
			{
				tempBenches.add(result.getInt(1));
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
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		try
		{
			statement = connection.prepareStatement(Statement.LOAD_USER__BENCH_DATA);

			for (Integer bench_id_item : tempBenches)
			{
				statement.clearParameters();
				statement.setInt(1, bench_id_item);
				ResultSet result = statement.executeQuery();

				int b_owner = result.getInt(1);
				long b_created = result.getLong(2);
				String b_title = result.getString(3);
				String b_background = result.getString(4);
				boolean b_archived = result.getBoolean(5);

				benches.add(new BenchData(bench_id_item, "", b_owner, b_title, b_created, b_archived, b_background));
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
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		try
		{
			statement = connection.prepareStatement(Statement.LOAD_USER__BENCH_DATA_OWNER_USERNAME);

			for (BenchData bench_mod : benches)
			{
				statement.clearParameters();
				statement.setInt(1, bench_mod.Id);
				ResultSet result = statement.executeQuery();
				String bench_username = result.getString(1);
				bench_mod.Owner = bench_username;
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
				connection.close()
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		if (username != null && id != 0)
		{
			return new User(id, username, email, confirmed, nodes, benches, avatar);
		}
		return null;
	}

	/**
	 * Only used internally by the loadBench() method.
	 */
	private BenchNode loadNode(Connection connection, int bNodeId)
	{
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(Statement.VALIDATE_USER_LOGIN);
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();

			if (result.next())
			{
				String hash = result.getString(1);

				if (hash.equalsIgnoreCase(passwordHash))
				{
					return true;
				}
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
	public Bench loadBench(int benchId)
	{
		int id = benchId; //
		User owner = null; //
		String title = null; //
		long created = 0; //
		Map<Integer, PermissionLevel> users = new HashMap<>(); //
		Map<Integer, BenchNode> nodes = new HashMap<>();
		int width = 3840; //temp
		int height = 2160; //temp
		boolean archived = false; //
		String background = null; //
		String preview = null; //


		Connection connection = null;
		PreparedStatement statement = null;

		try
		{
			connection = this.getConnection();
			statement = connection.prepareStatement(Statement.LOAD_BENCH__DATA);
			statement.setInt(1, benchId);
			ResultSet result = statement.executeQuery();

			if (result.next())
			{
				owner = loadUser(result.getInt(1));
				created = result.getLong(2);
				title = result.getString(3);
				background = preview = result.getString(4);
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
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}


		try
		{
			statement = connection.prepareStatement(Statement.LOAD_BENCH__GET_USER_IDS);
			statement.setInt(1, benchId);
			ResultSet result = statement.executeQuery();

			while (result.next())
			{
				int member_userId = result.getInt(1);
				int member_level = result.getInt(2);

				users.put(member_userId, PermissionLevel.get(member_level));
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
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		try
		{
			statement = connection.prepareStatement(Statement.LOAD_BENCH__GET_BENCH_NODE_IDS);
			statement.setInt(1, benchId);
			ResultSet result = statement.executeQuery();

			while (result.next())
			{
				int bNodeId = result.getInt(1);


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
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		new Bench(id, owner, title, created, users, nodes, width, height, archived, background, preview);
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
