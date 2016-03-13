package online.workbench.model.struct;

import java.util.List;
import java.util.Map;

public class User
{
	public int Id;
	public String Username;
	public String Email;
	public boolean Confirmed;
	public Map<Integer, UserNode> Nodes;
	public List<BenchData> Benches;

	public User(int id, String username, String email, boolean confirmed, Map<Integer, UserNode> nodes, List<BenchData> benches)
	{
		Id = id;
		Username = username;
		Email = email;
		Confirmed = confirmed;
		Nodes = nodes;
		Benches = benches;
	}
}