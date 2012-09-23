package bladequest.pathfinding;

import android.graphics.Point;


public class AStarNode {
	public Point point;
	public int score;
	public int heuristic;
	public int cost;
	public AStarNode parent;
	
	public AStarNode(Point point, int cost, int heuristic, int score, AStarNode parent)
	{
		this.point = point;
		this.cost = cost;
		this.heuristic = heuristic;
		this.score = score;
		this.parent = parent;
	}
}
