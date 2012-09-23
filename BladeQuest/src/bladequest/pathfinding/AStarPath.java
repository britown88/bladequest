package bladequest.pathfinding;


import java.util.*;

import bladequest.world.Global;

import android.graphics.Point;

public class AStarPath {
	private Point end;
	private AStarNode currentNode;
	private Map<Point, AStarNode> openList;
	private Map<Point, AStarNode> closedList;

	private List<Point> finalList;
	private List<AStarObstacle> obstacles;
	
	private AStarNode blockedPath;
	private boolean blockDest, unableToReach;
	
	public AStarPath(Point start, Point end, List<AStarObstacle> obstacles)
	{
		this.end = end;
		this.obstacles = obstacles;
		
		openList = new HashMap<Point, AStarNode>();
		closedList = new HashMap<Point, AStarNode>();
		
		//set first node
		currentNode = new AStarNode(start, 0, 0, 0, null);
		openList.put(currentNode.point, currentNode);
	}
	
	public List<Point> getPath() { return finalList; }
	
	public List<Point> AStar()
	{	
		//blockedPath = null;
		blockDest = false;
		unableToReach = false;

		while(true)
		{
			//current node is the destination, breakout out of the loop
			if(calculateHeuristic(currentNode.point.x, currentNode.point.y) == 0)
				break;				
			
			//add north node
			addNode(currentNode.point.x, currentNode.point.y - 1);
			//add east node
			addNode(currentNode.point.x + 1, currentNode.point.y);
			//add west node
			addNode(currentNode.point.x - 1, currentNode.point.y);
			//add south node
			addNode(currentNode.point.x, currentNode.point.y + 1);
			//move current to closedlist

			openList.remove(currentNode.point);
			closedList.put(currentNode.point, currentNode);
			
			//openlist is empty and we never found a valid path			
			if(openList.size()== 0)
			{
				//if we saved a blocked path, set it to current and break the loop
				if(blockDest)					
					currentNode = blockedPath;					
				else	
				{
					//otherwise theres no way to get there so
					//find the item with the lowest heuristic and break
					int score = -1;
					for(AStarNode a : closedList.values())
						if((a.heuristic < score || score == -1) && (a.parent != null || closedList.size() <= 1))
						{
							score = a.heuristic;
							currentNode = a;
						}
					
					unableToReach = true;
				}
				break;
			}
			
			//find open node with current lowscore, set to current node	
			int score = -1;
			for(AStarNode a : openList.values())
				if(a.score < score || score == -1)
				{
					score = a.score;
					currentNode = a;
				}		
			
		}
		finalList = new ArrayList<Point>();
		
		//if final node is blocked, pop it off
		if(blockDest && currentNode.parent != null)
			currentNode = currentNode.parent;
		
		while(currentNode.parent != null)
		{
			finalList.add(0, currentNode.point);
			currentNode = currentNode.parent;
		}		
		
		return finalList;		
	}
	
	private void addNode(int x, int y)
	{
		Point p = new Point(x, y);
		int heuristic = 0;
		int cost = 0;
		int score = 0;
		
		heuristic = calculateHeuristic(x, y);				
		//if not on lists or an obstacle
		if(!openList.containsKey(p) && !closedList.containsKey(p))				
		{				
			//if in viewing area
			if ((p.x >= Global.vpGridPos.x && p.x < Global.vpGridPos.x + Global.vpGridSize.x && 
					p.y >= Global.vpGridPos.y && p.y < Global.vpGridPos.y + Global.vpGridSize.y)
					|| (Global.pathIsValid && Global.validPathArea.contains(x, y)))
			{
				cost = currentNode.cost + 1;
				score = cost + heuristic;
				if(isClear(p))
				{
					openList.put(p, new AStarNode(p, cost, heuristic, score, currentNode));
				}
				else if(heuristic == 0 && !blockDest)
				{
					//we reached the path but it was blocked
					blockDest = true;
					
					//if the destination blocks on all sides we can go ahead and return it
					if(blocksOnAllSides(p))
						openList.put(p, new AStarNode(p, cost, heuristic, score, currentNode));
					else					
						//otherwise, save this node in case we can't get to it
						blockedPath = new AStarNode(p, cost, heuristic, score, currentNode);
				}
			}
		}	
	}
	
	public boolean destinationUnreachable() { return unableToReach; }
	
	private boolean isClear(Point p)
	{
		return isClear(currentNode.point, p);
	}
	
	private boolean blocksOnAllSides(Point p)
	{
		for(AStarObstacle ob : obstacles)
		{
			//check for collision objects at destination
			if(ob.equals(p))
				return ob.blocksOnAllSides();	

		}
		return false;
	}
	
	private boolean isClear(Point origin, Point destination)
	{
		AStarObstacle orig = null;
		AStarObstacle dest = null;
		
		//find the first obstacle whose coordinates match		
		for(AStarObstacle ob : obstacles)
		{
			//check for collision objects at destination
			if(ob.equals(destination))
				dest = ob;	
			//check for collision objects at origin
			if(ob.equals(origin))
				orig = ob;
		}
		
		if(dest != null)
		{
			if(orig != null)
				return !dest.isBlocked(orig);
			else
				return dest.allowEntryFrom(origin);			
		}			
		else
			if(orig != null)
				return orig.allowEntryFrom(destination);	
			else				
				return true;
	}
	
	public int calculateHeuristic(Point p)
	{
		return calculateHeuristic(p.x, p.y);
	}
	
	public int calculateHeuristic(int x, int y)
	{
		return Math.abs(x-end.x) + Math.abs(y - end.y);
	}
	
}
