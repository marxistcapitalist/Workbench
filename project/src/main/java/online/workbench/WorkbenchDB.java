package online.workbench;

import online.workbench.base.DatabaseMethods;
import online.workbench.model.struct.*;

public class WorkbenchDB implements DatabaseMethods
{

	@Override
	public void updateUserEmailAsync(User user, String email)
	{

	}

	@Override
	public void updateUserNameAsync(User user, String name)
	{

	}

	@Override
	public void updateUserAvatarAsync(User user, String avatar)
	{

	}

	@Override
	public void updateUserPasswordAsync(User user, String password)
	{

	}

	@Override
	public void invalidateToken(String token)
	{

	}

	@Override
	public boolean checkUsernameAvailability(String username)
	{
		return false;
	}

	@Override
	public boolean checkToken(int id, String token)
	{
		return false;
	}

	@Override
	public String issueToken(int id)
	{
		return null;
	}

	@Override
	public int validateUserName(String username, String passwordHash)
	{
		return 0;
	}

	@Override
	public int validateUserEmail(String email, String passwordHash)
	{
		return 0;
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
	public int createUser(String username, String email, String password)
	{
		return 0;
	}

	@Override
	public Bench createBench(Bench bench)
	{
		return null;
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
	public void submitNodeArchiveAsync(NodeType type, int id)
	{

	}

	@Override
	public UserNode submitNodeCreate(UserNode node)
	{
		return null;
	}

	@Override
	public void submitNodeCreateAsync(UserNode node)
	{

	}

	@Override
	public void submitNodeCreateAsync(BenchNode node)
	{

	}

	@Override
	public BenchNode submitNodeCreate(BenchNode node)
	{
		return null;
	}

	@Override
	public UserNode submitNodeCopyAsync(BenchNode node, User user)
	{
		return null;
	}

	@Override
	public BenchNode submitNodeCopy(UserNode node, Bench bench)
	{
		return null;
	}
}
