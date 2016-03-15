package online.workbench.data;

import online.workbench.managers.TokenManager;
import online.workbench.base.DatabaseMethods;
import online.workbench.model.struct.*;

public class WorkbenchDB implements DatabaseMethods
{
	public static String name;

	public WorkbenchDB(String name)
	{
		WorkbenchDB.name = "jdbc:sqlite:" + name + ".db";
		initialize();
	}

	private void initialize()
	{

	}

	@Override
	public void updateUserEmailAsync(int id, String email)
	{

	}

	@Override
	public void updateUserNameAsync(int id, String name)
	{

	}

	@Override
	public void updateUserAvatarAsync(int id, String avatar)
	{

	}

	@Override
	public void updateUserPasswordAsync(int id, String passwordHash)
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
