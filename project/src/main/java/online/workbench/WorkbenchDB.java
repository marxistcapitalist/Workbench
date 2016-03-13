package online.workbench;

import online.workbench.base.DatabaseMethods;
import online.workbench.model.struct.*;

import java.util.List;

public class WorkbenchDB implements DatabaseMethods
{

	@Override
	public boolean checkToken(String token)
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
	public BenchNode submitNodeCopy(UserNode node, Bench bench)
	{
		return null;
	}

	@Override
	public UserNode submitNodeCopy(BenchNode node, User user)
	{
		return null;
	}

	@Override
	public BenchNode submitNodeCreate(BenchNode node)
	{
		return null;
	}

	@Override
	public UserNode submitNodeCreate(UserNode node)
	{
		return null;
	}

	@Override
	public void submitNodeArchive(List<Node> nodes)
	{

	}

	@Override
	public void submitNodeResize(List<BenchNode> nodes)
	{

	}

	@Override
	public void submitNodeMove(List<BenchNode> nodes)
	{

	}

	@Override
	public void submitNodeContent(List<Node> nodes)
	{

	}

	@Override
	public void submitNodeEdit(List<Node> nodes)
	{

	}

	@Override
	public Bench createBench(Bench bench)
	{
		return null;
	}

	@Override
	public User createUser(User user)
	{
		return null;
	}
}
