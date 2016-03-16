package online.workbench.data;

import online.workbench.data.initialization.Statement;
import online.workbench.data.initialization.Table;
import online.workbench.managers.TokenManager;
import online.workbench.base.DatabaseMethods;
import online.workbench.model.struct.*;
import online.workbench.security.Token;
import online.workbench.utils.HexSelector;

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
			return DriverManager.getConnection(name);
		}
		catch (ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private void initialize()
	{
		try
		{
			Connection connection = this.getConnection();
			java.sql.Statement statement = connection.createStatement();

			statement.executeUpdate(Table.Accounts);
			statement.executeUpdate(Table.BenchNodeContent);
			statement.executeUpdate(Table.BenchNodes);
			statement.executeUpdate(Table.Benches);
			statement.executeUpdate(Table.Members);
			statement.executeUpdate(Table.Sessions);

			statement.close();
			connection.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
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

			for (Integer bench_id_item : tempBenches)
			{
				statement = connection.prepareStatement(Statement.LOAD_USER__BENCH_DATA);
				statement.setInt(1, bench_id_item);
				ResultSet result = statement.executeQuery();

				if (result.next())
				{
					int b_owner = result.getInt(1);
					long b_created = result.getLong(2);
					String b_title = result.getString(3);
					String b_background = result.getString(4);
					boolean b_archived = result.getBoolean(5);

					benches.add(new BenchData(bench_id_item, "", b_owner, b_title, b_created, b_archived, b_background));
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
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		try
		{
			for (BenchData bench_mod : benches)
			{
				statement = connection.prepareStatement(Statement.LOAD_USER__BENCH_DATA_OWNER_USERNAME);
				statement.setInt(1, bench_mod.Id);
				ResultSet result = statement.executeQuery();

				if (result.next())
				{
					bench_mod.Owner = result.getString(1);
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

		if (username != null && id != 0)
		{
			return new User(id, username, email, confirmed, nodes, benches, avatar);
		}
		return null;
	}

	/**
	 * Only used internally by the loadBench() method.
	 */
	private Map<Integer, BenchNode> loadNodes(Connection connection, int benchId)
	{
		HashMap<Integer, BenchNode> tempNodes = new HashMap<>();
		Map<Integer, BenchNode> finalNodes = new HashMap<>();

		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(Statement.LOAD_BENCH__GET_BENCH_NODE_DATA);
			statement.setInt(1, benchId);
			ResultSet result = statement.executeQuery();

			while (result.next())
			{
				int bNodeId = result.getInt(1);
				int userId = result.getInt(2);
				String title = result.getString(4);
				int x = result.getInt(5);
				int y = result.getInt(6);
				int width = result.getInt(7);
				int height = result.getInt(8);
				boolean archived = result.getBoolean(9);

				tempNodes.put(bNodeId, new BenchNode(bNodeId, null, loadUser(userId), x, y, width, height, title, null, null, 0, 0, archived));
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
				if (statement != null)
				{
					statement.close();
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		try
		{

			for (Map.Entry<Integer, BenchNode> b_t_node : tempNodes.entrySet())
			{
				statement = connection.prepareStatement(Statement.LOAD_BENCH__GET_BENCH_NODE_CONTENT);
				statement.setInt(1, b_t_node.getKey());
				ResultSet result = statement.executeQuery();

				if (result.next())
				{
					long lastEdit = result.getLong(2);
					ContentType type = ContentType.get(result.getString(3));
					String content = result.getString(4);

					b_t_node.getValue().LastEdit = lastEdit;
					b_t_node.getValue().Content = content;
					b_t_node.getValue().ContentType = type;

					finalNodes.put(b_t_node.getKey(), b_t_node.getValue());
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
				if (statement != null)
				{
					statement.close();
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return finalNodes;
	}

	@Override
	public Bench loadBench(int benchId)
	{
		int id = benchId; //
		User owner = null; //
		String title = null; //
		long created = 0; //
		Map<Integer, PermissionLevel> users = new HashMap<>(); //
		Map<Integer, BenchNode> nodes = null; //
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
				if (statement != null)
				{
					statement.close();
				}
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
				if (statement != null)
				{
					statement.close();
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		nodes = this.loadNodes(connection, benchId);

		if (id != 0)
		{
			return new Bench(id, owner, title, created, users, nodes, width, height, archived, background, preview);
		}
		return null;
	}

	@Override
	public int countBenchMembers(int benchId)
	{
		int counter = 0;

		Connection connection = null;
		PreparedStatement statement = null;
		try
		{
			connection = this.getConnection();
			statement = connection.prepareStatement(Statement.COUNT_BENCH_MEMBERS);
			statement.setInt(1, benchId);
			ResultSet result = statement.executeQuery();

			while (result.next())
			{
				counter++;
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
				if (statement != null)
				{
					statement.close();
				}

				if (connection != null)
				{
					connection.close();
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return counter;
	}

	@Override
	public int createNewUser(String username, String email)
	{
		int finalId = 0;

		Connection connection = null;
		PreparedStatement statement = null;
		try
		{
			connection = this.getConnection();
			statement = connection.prepareStatement(Statement.CREATE_USER__INITIAL_DATA);
			statement.setString(1, username.toLowerCase());
			statement.setString(2, email.toLowerCase());
			statement.executeUpdate();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (statement != null)
				{
					statement.close();
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		try
		{
			statement = connection.prepareStatement(Statement.CREATE_USER__GET_ID);
			statement.setString(1, username.toLowerCase());
			ResultSet result = statement.executeQuery();

			if (result.next())
			{
				finalId = result.getInt(1);
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
				if (statement != null)
				{
					statement.close();
				}
				connection.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return finalId;
	}

	@Override
	public void setPassword(int id, String passwordHash)
	{
		Connection connection = null;
		PreparedStatement statement = null;
		try
		{
			connection = this.getConnection();
			statement = connection.prepareStatement(Statement.CREATE_USER__INSERT_PASSWORD);
			statement.setString(1, passwordHash);
			statement.setInt(2, id);
			statement.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (statement != null)
				{
					statement.close();
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public Bench createBench(User user, String title, int w, int h)
	{
		String color = HexSelector.sel();
		int benchId = 0;
		long created = System.currentTimeMillis();

		Connection connection = null;
		PreparedStatement statement = null;
		try
		{
			connection = this.getConnection();
			statement = connection.prepareStatement(Statement.CREATE_BENCH__INSERT_DATA);
			statement.setInt(1, user.Id);
			statement.setLong(2, created);
			statement.setString(3, title);
			statement.setString(4, color);
			statement.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (statement != null)
				{
					statement.close();
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		try
		{
			statement = connection.prepareStatement(Statement.CREATE_BENCH__GRAB_BENCH_ID);
			statement.setInt(1, user.Id);
			statement.setLong(2, created);
			statement.setString(3, title);
			ResultSet result = statement.executeQuery();

			if (result.next())
			{
				benchId = result.getInt(1);
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
				if (statement != null)
				{
					statement.close();
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		if (benchId != 0)
		{
			try
			{
				statement = connection.prepareStatement(Statement.CREATE_BENCH__ADD_OWNER_AS_MEMBER);
				statement.setInt(1, benchId);
				statement.setInt(2, user.Id);
				statement.setInt(3, PermissionLevel.OWNER.val());
				statement.executeUpdate();
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
			return this.loadBench(benchId);
		}
		return null;
	}

	@Override
	public void archiveBench(int id)
	{
		try
		{
			Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(Statement.ARCHIVE_BENCH);
			statement.setBoolean(1, true);
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
	public void submitNodeContentEditAsync(NodeType type, int id, String content)
	{
		try
		{
			Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(Statement.BENCH_NODE_CONTENT_EDIT);
			statement.setString(1, content);
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
	public void submitNodeContentTypeEditAsync(NodeType type, int id, ContentType cType)
	{
		try
		{
			Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(Statement.BENCH_NODE_CONTENT_TYPE_EDIT);
			statement.setString(1, cType.toString());
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
	public void submitNodeMoveAsync(NodeType type, int id, int x, int y)
	{
		try
		{
			Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(Statement.BENCH_NODE_MOVE);
			statement.setInt(1, x);
			statement.setInt(2, y);
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

	@Override
	public void submitNodeResizeAsync(NodeType type, int id, int w, int h)
	{
		try
		{
			Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(Statement.BENCH_NODE_RESIZE);
			statement.setInt(1, w);
			statement.setInt(2, h);
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

	@Override
	public void submitNodeRenameAsync(NodeType type, int id, String title)
	{
		try
		{
			Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(Statement.BENCH_NODE_RENAME);
			statement.setString(1, title);
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
	public void submitNodeArchiveAsync(NodeType type, int id)
	{
		try
		{
			Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(Statement.BENCH_NODE_ARCHIVE);
			statement.setBoolean(1, true);
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
	public BenchNode submitNodeCreate(BenchNode node)
	{
		int finalId = 0;

		Connection connection = null;
		PreparedStatement statement = null;
		try
		{
			connection = this.getConnection();
			statement = connection.prepareStatement(Statement.BENCH_NODE_CREATE__MAIN);
			statement.setInt(1, node.Creator.Id);
			statement.setInt(2, node.Bench.Id);
			statement.setInt(3, node.Position.X);
			statement.setInt(4, node.Position.Y);
			statement.setInt(5, node.Position.Width);
			statement.setInt(6, node.Position.Height);
			statement.setString(7, node.Title);

			statement.executeUpdate();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (statement != null)
				{
					statement.close();
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		try
		{
			statement = connection.prepareStatement(Statement.BENCH_NODE_CREATE__GRAB_BENCH_NODE_ID);
			statement.setInt(1, node.Creator.Id);
			statement.setInt(2, node.Bench.Id);
			statement.setString(3, node.Title);

			ResultSet result = statement.executeQuery();

			if (result.next())
			{
				finalId = result.getInt(1);
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
				if (statement != null)
				{
					statement.close();
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		if (finalId != 0)
		{
			node.Id = finalId;

			try
			{
				statement = connection.prepareStatement(Statement.BENCH_NODE_CREATE__SET_CONTENT_ENTRY);
				statement.setInt(1, node.Id);
				statement.setString(2, node.Content);
				statement.setString(3, node.ContentType.toString());

				statement.executeUpdate();
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

			return node;
		}
		return null;
	}

	@Override
	public String grabEmail(String username)
	{
		try
		{
			Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(Statement.GRAB_EMAIL);
			statement.setString(1, username.toLowerCase());
			ResultSet result = statement.executeQuery();

			if (result.next())
			{
				return result.getString(1);
			}

			statement.close();
			connection.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String grabUser(String email)
	{
		try
		{
			Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(Statement.GRAB_USER);
			statement.setString(1, email.toLowerCase());
			ResultSet result = statement.executeQuery();

			if (result.next())
			{
				return result.getString(1);
			}

			statement.close();
			connection.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int grabId(String loginKey)
	{
		try
		{
			Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(Statement.GRAB_ID);
			statement.setString(1, loginKey.toLowerCase());
			statement.setString(2, loginKey.toLowerCase());
			ResultSet result = statement.executeQuery();

			if (result.next())
			{
				return result.getInt(1);
			}

			statement.close();
			connection.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void submitBenchBackgroundEdit(Bench bench, String background)
	{
		try
		{
			Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(Statement.BENCH_EDIT_BACKGROUND);
			statement.setString(1, background);
			statement.setInt(2, bench.Id);
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
	public void submitBenchTitleEdit(Bench bench, String title)
	{
		try
		{
			Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(Statement.BENCH_EDIT_TITLE);
			statement.setString(1, title);
			statement.setInt(2, bench.Id);
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
	public void submitBenchResize(Bench bench, int w, int h)
	{
		String statement = Statement.BENCH_EDIT_RESIZE___DO_NOT_USE_WILL_BREAK_EVERYTHING;
	}

	@Override
	public void addUserToBench(Bench bench, int user, PermissionLevel role)
	{
		try
		{
			Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(Statement.BENCH_USER_ADD);
			statement.setInt(1, bench.Id);
			statement.setInt(2, user);
			statement.setInt(3, role.val());
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
	public void removeUserFromBench(Bench bench, int user)
	{
		try
		{
			Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(Statement.BENCH_USER_REMOVE);
			statement.setInt(1, user);
			statement.setInt(2, bench.Id);
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
	public void modifyUserInBench(Bench bench, int user, PermissionLevel role)
	{
		try
		{
			Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(Statement.BENCH_USER_MODIFY);
			statement.setInt(1, role.val());
			statement.setInt(2, user);
			statement.setInt(3, bench.Id);
			statement.executeUpdate();
			statement.close();
			connection.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
