package bladequest.pathfinding;


import java.util.*;

import bladequest.world.Global;

import android.graphics.Point;

public class AStarPath {
	private Point end;
	private AStarNode currentNode;
	//private Map<Point, AStarNode> openList;
	//private Map<Point, AStarNode> closedList;
	private Map<Point, AStarNode> nodes;
	private Map<Point, AStarObstacle> obstacles;

	AStarHeap openList;
	AStarNode closestNode;
	private List<Point> finalList;
	//private List<AStarObstacle> obstacles;
	
	private AStarNode blockedPath;
	private boolean blockDest, unableToReach;
	
	
	
	public AStarPath(Point start, Point end, List<AStarObstacle> obstacles)
	{
		this.end = end;
		
		openList = new AStarHeap();
		this.nodes = new HashMap<Point, AStarNode>();
		this.obstacles = new HashMap<Point, AStarObstacle>();
		
		for (AStarObstacle obs : obstacles)
		{
			if (this.obstacles.get(obs.getPoint()) == null)
			{
				this.obstacles.put(obs.getPoint(), obs);
			}
		}
		
		
		int h = calculateHeuristic(start.x, start.y);
		//set first node
		closestNode = currentNode = new AStarNode(start, 0, h, h, null);
		nodes.put(start, currentNode);
		openList.insert(currentNode);
		//openList.put(currentNode.point, currentNode);
	}
	
	public List<Point> getPath() { return finalList; }
	
	public List<Point> AStar()
	{	
		//blockedPath = null;
		blockDest = false;
		unableToReach = false;
		
		for (;;)
		{
			currentNode = openList.pop();
			if (currentNode == null) 
			{
				if (blockDest)
					currentNode = blockedPath;
				else
					currentNode = closestNode;
				
				unableToReach = true;
				break;
			}
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

			currentNode.closed = true;
		}
		finalList = new ArrayList<Point>();
		
		//if final node is blocked, pop it off
		if(blockDest && currentNode.parent != null)
			currentNode = currentNode.parent;
		
		while(currentNode.parent != null)
		{
			finalList.add(currentNode.point);
			currentNode = currentNode.parent;
		}		
		
		java.util.Collections.reverse(finalList);
		
		return finalList;		
	}
	
	private void addNode(int x, int y)
	{
		Point p = new Point(x, y);
		int heuristic = 0;
		int cost = 0;
		int score = 0;
		
		if (!((p.x >= Global.vpGridPos.x && p.x < Global.vpGridPos.x + Global.vpGridSize.x && 
				p.y >= Global.vpGridPos.y && p.y < Global.vpGridPos.y + Global.vpGridSize.y)
				|| (Global.pathIsValid && Global.validPathArea.contains(x, y)))) 
		{
			return;  //bad point, not visible, can't consider it for pathing.
		}
		
		AStarNode existingNode = nodes.get(p);
		
		if (existingNode != null && existingNode.closed) return; //can't edit closed node.
		
				
		cost = currentNode.cost + 1;
		if (existingNode == null)
		{
			//if in viewing area
			heuristic = calculateHeuristic(x, y);
			score = cost + heuristic;
			AStarNode newNode = null;
			if(isClear(p))
			{
				newNode = new AStarNode(p, cost, heuristic, score, currentNode);
			}
			else if(heuristic == 0 && !blockDest)
			{
				//we reached the path but it was blocked
				blockDest = true;
				
				//if the destination blocks on all sides we can go ahead and return it
				if(blocksOnAllSides(p))
					newNode = new AStarNode(p, cost, heuristic, score, currentNode);
				else					
					//otherwise, save this node in case we can't get to it
					blockedPath = new AStarNode(p, cost, heuristic, score, currentNode);
			}
			
			if (newNode != null)
			{
				openList.insert(newNode);
				if (heuristic < closestNode.heuristic) closestNode = newNode;
				nodes.put(p, newNode);
			}
		}
		else  //update existing open list key.
		{
			if (cost < existingNode.cost)
			{
				existingNode.cost = cost;
				existingNode.score = existingNode.cost + existingNode.heuristic;
				openList.decreaseKey(existingNode);
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
		AStarObstacle ob = obstacles.get(p);
		//check for collision objects at destination
		if(ob != null && ob.equals(p))
			return ob.blocksOnAllSides();	

		return false;
	}
	
	private boolean isClear(Point origin, Point destination)
	{
		AStarObstacle orig =  obstacles.get(origin);
		AStarObstacle dest = obstacles.get(destination);
		
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
