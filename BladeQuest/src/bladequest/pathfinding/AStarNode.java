package bladequest.pathfinding;

import android.graphics.Point;


public class AStarNode {
	public Point point;
	public int score;
	public int heuristic;
	public int cost;
	public AStarNode parent;
	public boolean closed;
	
	
	//if the first node, prev->child == this.
	public AStarNode next, prev, child;
	
	public AStarNode(Point point, int cost, int heuristic, int score, AStarNode parent)
	{
		this.point = point;
		this.cost = cost;
		this.heuristic = heuristic;
		this.score = score;
		this.parent = parent;
		this.closed = false;
	}
	boolean opened(AStarHeap heap)
	{
		return next != null || prev != null || child != null || heap.peek() == this;
	}
}
