package bladequest.pathfinding;

import android.graphics.*;

public class AStarObstacle 
{
	private Point pos;
	private boolean blockLeft, blockTop, blockRight, blockBottom;
	
	public AStarObstacle(Point pos,boolean left,boolean top,boolean right,boolean bottom)
	{
		this.pos = pos;
		this.blockLeft = left;
		this.blockTop = top;
		this.blockRight = right;
		this.blockBottom = bottom;		
	}
	
	public boolean blocksOnAllSides()
	{
		return blockLeft && blockTop && blockRight && blockBottom;
	}
	
	public boolean equals(Point other)
	{
		return this.pos.equals(other);
		
	}
	
	public boolean isBlocked(AStarObstacle ob)
	{
		if(ob.pos.x < pos.x)
		{
			return blockLeft || ob.blockRight;
		}		
		else if(ob.pos.x > pos.x)
		{
			return blockRight || ob.blockLeft;
		}		
		else if(ob.pos.y < pos.y)
		{
			return blockTop || ob.blockBottom;
		}		
		else if(ob.pos.y > pos.y)
		{
			return blockBottom || ob.blockTop;
		}
		else
			return false;

	}
	
	public boolean allowEntryFrom(Point origin)
	{
		if(origin.x < pos.x && blockLeft)
			return false;
		
		if(origin.x > pos.x && blockRight)
			return false;
		
		if(origin.y < pos.y && blockTop)
			return false;
		
		if(origin.y > pos.y && blockBottom)
			return false;
		
		
		return true;
	}
}
