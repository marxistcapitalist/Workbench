package online.workbench.api;

import lombok.Setter;
import online.workbench.WorkbenchDB;
import online.workbench.WorkbenchWS;
import online.workbench.model.struct.*;

import java.util.ArrayList;

public class BenchManager
{
	private ArrayList<Bench> benches;
	private WorkbenchDB database;
	private @Setter WorkbenchWS websocket;

	public BenchManager(WorkbenchDB database)
	{
		this.benches = new ArrayList<>();
		this.database = database;
	}

	public BenchNode getNode(int id)
	{
		return this.benches.stream().filter(b -> b.Nodes.containsKey(id)).findFirst().get().Nodes.get(id);
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

	public Bench loadBench(int id)
	{
		Bench bench = this.database.loadBench(id);

		if (bench != null)
		{
			this.benches.add(bench);
		}
		return bench;
	}

	public void editNode(User user, Bench bench, BenchNode node, String content)
	{
		if (this.contentEditAsync(bench, node.Id, content))
		{
			websocket.sendEdit(bench, user, node, content);
		}
	}

	public void moveNode(User user, Bench bench, BenchNode node, int x, int y)
	{
		if (this.moveAsync(bench, node.Id, x, y))
		{
			websocket.sendMove(bench, user, node, x, y);
		}
	}

	public void resizeNode(User user, Bench bench, BenchNode node, int w, int h)
	{
		if (this.resizeAsync(bench, node.Id, w, h))
		{
			websocket.sendResize(bench, user, node, w, h);
		}
	}

	public void typeNode(User user, Bench bench, BenchNode node, ContentType type)
	{
		if (this.contentTypeEditAsync(bench, node.Id, type))
		{
			websocket.sendTypeEdit(bench, user, node, type);
		}
	}

	public void deleteNode(User user, Bench bench, BenchNode node)
	{
		if (this.archiveAsync(bench, node.Id))
		{
			websocket.sendDelete(bench, user, node);
		}
	}

	public void createNode(User user, Bench bench, BenchNode node)
	{
		BenchNode newNode = this.createBlocking(bench, node);

		if (newNode != null)
		{
			websocket.sendCreate(bench, user, node);
		}
	}


	private BenchNode createBlocking(Bench bench, BenchNode node)
	{
		if (node.Id == 0)
		{
			BenchNode newNode = this.database.submitNodeCreate(node);
			return newNode;
		}
		return null;
	}

	private boolean createAsync(Bench bench, BenchNode node)
	{
		if (node.Id == 0)
		{
			this.database.submitNodeCreateAsync(node);
			return true;
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
				bench.Nodes.get(id).Content = content;
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
				bench.Nodes.get(id).Position.X = x;
				bench.Nodes.get(id).Position.Y = y;
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
				bench.Nodes.get(id).Position.Height = h;
				bench.Nodes.get(id).Position.Width = w;
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


}
