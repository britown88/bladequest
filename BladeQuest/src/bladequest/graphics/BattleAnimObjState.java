package bladequest.graphics;

import android.graphics.Rect;
import android.graphics.Point;

import bladequest.world.Global;

public class BattleAnimObjState 
{

	public float rotation, strokeWidth, colorize;
	public int r, g, b, a;
	
	public Point size, pos1, pos2;
	
	public int frame;
	
	public boolean show, randomized;
	
	public PosTypes posType;
	
	private boolean random;
	private Rect randomRange;
	
	public void setRandomRange(Rect range){randomRange = range; random = true;}
	public void randomize(BattleAnimObject parent)
	{
		if(random)
		{
			int rx = randomRange.left, ry = randomRange.top;
			if(randomRange.width() > 0)
				rx += Global.rand.nextInt(randomRange.width());
			if(randomRange.height() > 0)
				ry += Global.rand.nextInt(randomRange.height());
				
			pos1 = new Point(rx, ry);
		}
		
		switch(posType)
		{
		case Source:
			pos1.offset(parent.source.x, parent.source.y);
			pos2.offset(parent.source.x, parent.source.y);
			break;
		case Target:
			pos1.offset(parent.target.x, parent.target.y);
			pos2.offset(parent.target.x, parent.target.y);
			break;
		}
		
		randomized = true;
	}
	
	public BattleAnimObjState(int frame, PosTypes posType)
	{
		show = true;
		random = false;
		
		this.frame = frame;
		this.posType = posType;
		
		pos1 = new Point(0,0);
		pos2 = new Point(0,0);
		size = new Point(0,0);
	}
	
	public void argb(int a, int r, int g, int b)
	{
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
		
	}
	
	public BattleAnimObjState(BattleAnimObjState other)
	{
		this.rotation = other.rotation;
		this.size = new Point(other.size);
		this.strokeWidth = other.strokeWidth;
		this.r = other.r;
		this.g = other.g;
		this.b = other.b;
		this.a = other.a;
		this.pos1 = new Point(other.pos1);
		this.pos2 = new Point(other.pos2);
		this.frame = other.frame;
		this.show = other.show;
		this.random = other.random;
		this.posType = other.posType;
		if(other.randomRange != null)
			this.randomRange = new Rect(other.randomRange);
		this.randomized = other.randomized;
		this.colorize = other.colorize;
	}
	
	public enum PosTypes
	{
		Source,
		Target,
		Screen
	}

}
