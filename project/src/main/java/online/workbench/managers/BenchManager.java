package online.workbench.managers;

import lombok.Setter;
import online.workbench.data.WorkbenchDB;
import online.workbench.websocket.WorkbenchWS;
import online.workbench.model.struct.*;

import java.util.ArrayList;

public class BenchManager
{
	private ArrayList<Bench> benches;
	private WorkbenchDB database;
	private @Setter WorkbenchWS websocket;
	private UserManager userManager;

	public BenchManager(WorkbenchDB database, UserManager userManager)
	{
		this.benches = new ArrayList<>();
		this.userManager = userManager;
		this.database = database;
	}

	public BenchNode getNode(int id)
	{
		return this.benches.stream().filter(b -> b.Nodes.containsKey(id)).findFirst().get().Nodes.get(id);
	}

	public void addUser(Bench bench, int userId, PermissionLevel level)
	{
		User user = userManager.load(userId);

		if (user != null && !level.equals(PermissionLevel.ADMIN))
		{
			user.Benches.add(BenchData.metafy(bench));
			bench.Users.put(user.Id, level);
			database.addUserToBench(bench, userId, level);
			websocket.sendUserAdd(bench, user, level);
			websocket.sendNotifyBench(bench, user.Username + " has been added", "added as: " + level.toString(), "", 5000);
		}
	}

	public void modUser(Bench bench, int userId, PermissionLevel level)
	{
		User user = userManager.load(userId);

		if (user != null)
		{
			if (bench.Owner.Id != user.Id && bench.Users.containsKey(user.Id))
			{
				if (!bench.Users.get(user.Id).equals(level) && !level.equals(PermissionLevel.ADMIN))
				{
					bench.Users.put(user.Id, level);
					database.modifyUserInBench(bench, user.Id, level);
					websocket.sendUserModify(bench, user, level);

					String happened = level.val() < bench.Users.get(user.Id).val() ? "demoted" : "promoted";
					websocket.sendNotifyBench(bench, user.Username + " has been " + happened, "user is now: " + level.toString(), "", 3000);
				}
			}
		}
	}

	public void removeUser(Bench bench, int userId)
	{
		User user = userManager.load(userId);

		if (user != null)
		{
			if (bench.Owner.Id != user.Id && bench.Users.containsKey(user.Id))
			{
				bench.Users.remove(user.Id);
				database.removeUserFromBench(bench, user.Id);
				websocket.sendUserRemove(bench, user);

				websocket.sendNotifyBench(bench, user.Username + " has been removed", "", "", 3000);
			}
		}
	}

	public Bench createBench(User user, String title, int w, int h)
	{
		Bench bench = database.createBench(user, title, w, h);
		user.Benches.add(new BenchData(bench.Id, bench.Owner.Username, bench.Owner.Id, bench.Title, bench.Created, bench.Archived, bench.Background));
		return bench.Id != 0 ? bench : null;
	}

	public void deleteBench(Bench bench)
	{
		benches.remove(bench);
		database.archiveBench(bench.Id);
		websocket.removeBenchFromSessions(bench);
	}

	public BenchNode getNode(Bench bench, int id)
	{
		return bench.Nodes.containsKey(id) ? bench.Nodes.get(id) : null;
	}

	public int countBenchUsers(int id)
	{
		return database.countBenchMembers(id);
	}

	private Bench get(int id)
	{
		return this.benches.stream().filter(b -> b.Id == id).findFirst().get();
	}

	public Bench load(int id)
	{
		if (id != 0)
		{
			Bench bench = this.database.loadBench(id);

			if (bench != null)
			{
				this.benches.add(bench);
				this.websocket.addBenchToSessions(bench);
			}
			return bench;
		}
		return null;
	}

	public void editNode(User user, Bench bench, BenchNode node, String content)
	{
		if (this.contentEditAsync(bench, node.Id, content))
		{
			bench.Nodes.get(node.Id).Content = content;
			websocket.sendBenchNodeEdit(bench, user, node.Id, content, node.ContentType);
		}
	}

	public void moveNode(User user, Bench bench, BenchNode node, int x, int y)
	{
		if (this.moveAsync(bench, node.Id, x, y))
		{
			bench.Nodes.get(node.Id).Position.X = x;
			bench.Nodes.get(node.Id).Position.Y = y;
			websocket.sendBenchNodeMove(bench, user, node.Id, x, y);
		}
	}

	public void resizeNode(User user, Bench bench, BenchNode node, int w, int h)
	{
		if (this.resizeAsync(bench, node.Id, w, h))
		{
			bench.Nodes.get(node.Id).Position.Width = w;
			bench.Nodes.get(node.Id).Position.Height = h;
			websocket.sendBenchNodeResize(bench, user, node.Id, w, h);
		}
	}

	public void typeNode(User user, Bench bench, BenchNode node, ContentType type)
	{
		if (this.contentTypeEditAsync(bench, node.Id, type))
		{
			bench.Nodes.get(node.Id).ContentType = type;
			websocket.sendBenchNodeEdit(bench, user, node.Id, node.Content, type);
		}
	}

	public void deleteNode(User user, Bench bench, BenchNode node)
	{
		if (this.archiveAsync(bench, node.Id))
		{
			bench.Nodes.remove(node.Id);
			websocket.sendBenchNodeDelete(bench, user, node.Id);
		}
	}


	public void renameNode(User user, Bench bench, BenchNode node, String title)
	{
		if (this.renameAsync(bench, node, title))
		{
			bench.Nodes.get(node.Id).Title = title;
			websocket.sendBenchNodeRename(bench, user, node.Id, title);
		}
	}


	public void createNode(User user, Bench bench, BenchNode node)
	{
		BenchNode newNode = database.submitNodeCreate(node);

		if (newNode != null && newNode.Id != 0)
		{
			bench.Nodes.put(node.Id, node);
			websocket.sendBenchNodeCreate(bench, user, node);
		}
	}

	public void watchNode(User user, Bench bench, BenchNode node)
	{

	}

	public void ignoreNode(User user, Bench bench, BenchNode node)
	{

	}

	private boolean renameAsync(Bench bench, BenchNode node, String title)
	{
		if (bench.Nodes.containsKey(node.Id))
		{
			if (title.length() <= 256)
			{
				this.database.submitNodeRenameAsync(NodeType.BENCH, node.Id, title);
				return true;
			}
		}
		return false;
	}

	private boolean archiveAsync(Bench bench, int id)
	{
		if (bench.Nodes.containsKey(id))
		{
			if (!bench.Nodes.get(id).Archived)
			{
				this.database.submitNodeArchiveAsync(NodeType.BENCH, id);
				return true;
			}
		}
		return false;
	}

	private boolean contentEditAsync(Bench bench, int id, String content)
	{
		if (bench.Nodes.containsKey(id))
		{
			if (content.length() < Integer.MAX_VALUE) //ARBITRARY
			{
				this.database.submitNodeContentEditAsync(NodeType.BENCH, id, content);
				return true;
			}
		}
		return false;
	}

	private boolean moveAsync(Bench bench, int id, int x, int y)
	{
		if (bench.Nodes.containsKey(id))
		{
			if (x <= bench.Dimensions.Width && y <= bench.Dimensions.Height)
			{
				this.database.submitNodeMoveAsync(NodeType.BENCH, id, x, y);
				return true;
			}
		}
		return false;
	}

	private boolean resizeAsync(Bench bench, int id, int w, int h)
	{
		if (bench.Nodes.containsKey(id))
		{
			if (w < bench.Dimensions.Width && h < bench.Dimensions.Height)
			{
				this.database.submitNodeResizeAsync(NodeType.BENCH, id, w, h);
				return true;
			}
		}
		return false;
	}

	private boolean contentTypeEditAsync(Bench bench, int id, ContentType type)
	{
		if (bench.Nodes.containsKey(id))
		{
			if (!bench.Nodes.get(id).ContentType.equals(type))
			{
				this.database.submitNodeContentTypeEditAsync(NodeType.BENCH, id, type);
				return true;
			}
		}
		return false;
	}

	public void updateDimensions(Bench bench, int w, int h)
	{
		bench.Dimensions.Height = h;
		bench.Dimensions.Width = w;
		database.submitBenchResize(bench, w, h);

		websocket.sendNotifyBench(bench, "Dimensions Updated", "(" + w + "x" + h + ")", "/bench/" + bench.Id, 10000);
	}

	public void updateTitle(Bench bench, String title)
	{
		bench.Title = title;
		database.submitBenchTitleEdit(bench, title);

		websocket.sendNotifyBench(bench, "Title Updated", "(" + title + ")", "/bench/" + bench.Id, 10000);
	}

	public void updateBackground(Bench bench, String background)
	{
		bench.Background = background;
		database.submitBenchBackgroundEdit(bench, background);

		websocket.sendNotifyBench(bench, "Background Updated", "(" + background + ")", "/bench/" + bench.Id, 10000);
	}
}
