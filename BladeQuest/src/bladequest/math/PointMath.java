package bladequest.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleAnimObjState;
import bladequest.graphics.BattleAnimObjState.PosTypes;
import bladequest.graphics.BattleAnimObject;
import bladequest.graphics.BattleAnimObject.Types;
import bladequest.graphics.BattleSprite;
import bladequest.graphics.BattleSprite.faces;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class PointMath {

	public static Point subtract(Point lhs, Point rhs)
	{
	  return new Point(lhs.x - rhs.x, lhs.y - rhs.y);
	}
	public static Point add(Point lhs, Point rhs)
	{
		  return new Point(lhs.x + rhs.x, lhs.y + rhs.y);
	}
	public static Point offset(Point lhs, float x, float y)
	{
		 return new Point(lhs.x + (int)x, lhs.y + (int)y);
	}
	public static float offsetAngle(Point vectorTo, float distance)
	{
	   float xNormalized = -vectorTo.x / distance;
	   if (xNormalized > 1.0f) xNormalized = 1.0f; //floating point stupidity
	   float angle = (float)Math.acos(xNormalized);
	   //check orientation (ghetto det check)
	   if (vectorTo.y < 0)
	   {
		   angle = (float)(Math.PI * 2  - angle);
	   }
	   return angle;
	}
	public static float angleBetween(Point p1, Point p2)
	{
		return (float)Math.atan2(p2.y-p1.y, p2.x-p1.x);
	}
	public static float length(Point p1, Point p2)
	{
		float y = (p2.y-p1.y);
	    float x = (p2.x-p1.x); 
		
	    return (float)Math.sqrt(x*x + y*y);
	}
	public static void toCenter(Point p, PlayerCharacter source)
	{
		BattleSprite battleSpr = source.getBattleSprite();
		p.offset(battleSpr.getWidth()/2, battleSpr.getHeight()/2);
	}
	public static void toTopLeft(Point p, PlayerCharacter source)
	{
		BattleSprite battleSpr = source.getBattleSprite();
		p.offset(-battleSpr.getWidth()/2, -battleSpr.getHeight()/2);
	}	
	public static List<Point> getArc(Point startPoint, Point endPoint, int steps, float arcFactor)
	{
		if (startPoint.x < endPoint.x) 
		{
			List<Point> out = getArc(endPoint, startPoint, steps, arcFactor);
			Collections.reverse(out);
			return out;
		}
		List<Point> out = new ArrayList<Point>();
		Point vectorTo = subtract(endPoint, startPoint);
		Point center = new Point(startPoint.x + (int)(vectorTo.x * 0.5f), 
								 startPoint.y + (int)(vectorTo.y * 0.5f));
		
	    float distance = (float) Math.sqrt((float)(vectorTo.x*vectorTo.x + vectorTo.y*vectorTo.y));
		float height = distance * arcFactor;
	    
	    final float stepSize = (float)(Math.PI / (steps-1));
	    
	    float offsetAngle = offsetAngle(vectorTo, distance);
	    
	    for (int i = 0; i < steps; ++i)
	    {
	    	float angle = stepSize * i;
	    	float x = (float)Math.cos((double)angle) * (distance/2);
	    	float y = (float)Math.sin((double)angle) * (-height/2);
	        Point p = getRotatedPoint(x, y, offsetAngle);
	        out.add(add(p, center));
	    }
	    	    
	    return out;
	}
		
	public static Point getRotatedPoint(float x, float y, float angle)
	{
	   //cos, sin, -sin cos
	   float cosAngle = (float)Math.cos(angle);
	   float sinAngle = (float)Math.sin(angle);
	   
	   float outX = cosAngle  * x + sinAngle * y;
	   float outY = -sinAngle * x + cosAngle * y;
	   
	   return new Point((int)outX, (int)outY);
	}
	
	public static Point getStabPoint(PlayerCharacter attacker, PlayerCharacter target)
	{
		int attackerHeight = attacker.getBattleSprite().getHeight();
		int defenderHeight = target.getBattleSprite().getHeight(); 
		
		Point startPos = attacker.getPosition(false);
		Point offset = PointMath.subtract(attacker.getPosition(true), startPos); 
		Point pointOff = target.getPosition(false);
		pointOff.x += 8 + target.getBattleSprite().getWidth();
		pointOff.y +=  defenderHeight - attackerHeight;
		
		return add(pointOff, offset);
	}
	
	public static BattleAnim buildJumpAnimation(PlayerCharacter attacker, Point from, Point to, float arcFactor, int animationTime)
	{
		//animations are relative to CENTER!
		final int steps = 6;
		//Jump anim.  get arc
		List<Point> path = PointMath.getArc(from, to, steps, arcFactor);
		
		BattleAnim anim = new BattleAnim(60.0f);
		
		BattleSprite playerSprite = attacker.getBattleSprite();
		
		BattleAnimObject baObj = new BattleAnimObject(Types.Bitmap, false, playerSprite.getBmpName());
		
		PointMath.animateAlongPath(playerSprite, playerSprite.getFrameRect(faces.Use, 0), baObj, path, animationTime);		
		
		anim.addObject(baObj);
		
		return anim;
	}
	
	public static void animateAlongPath(BattleSprite spr, Rect sprRect, BattleAnimObject insertObj, List<Point> path, int stepTime)
	{
		int step = 0;
	    for (Point p : path)
	    {
	        BattleAnimObjState state = new BattleAnimObjState(stepTime * step++, PosTypes.Screen);
			
			state.setBmpSrcRect(sprRect.left, sprRect.top, sprRect.right, sprRect.bottom);
			state.argb(255, 255, 255, 255);
			state.pos1 = p;
			state.size = new Point(spr.getWidth(), spr.getHeight());
			insertObj.addState(state);
	    }		
	}
	
	private static List<Point> jaggedPathStep(PointF start, PointF end, int iterations, float radius)
	{
		List<Point> out = new ArrayList<Point>();
		
		
		float x = (start.x + end.x)/2.0f - radius + Global.rand.nextFloat() * radius;
		float y = (start.y + end.y)/2.0f - radius + Global.rand.nextFloat() * radius;
		
		PointF midPoint = new PointF(x,y);
		if (iterations == 0)
		{
			out.add(new Point((int)x, (int)y));
			return out;
		}
		
		for (Point p : jaggedPathStep(start, midPoint, iterations - 1, radius/2))
		{
			out.add(p);
		}
		
		out.add(new Point((int)x, (int)y));

		for (Point p : jaggedPathStep(midPoint, end, iterations - 1, radius/2))
		{
			out.add(p);
		}
		return out;
	}
	public static List<Point> jaggedPath(Point start, Point end, int iterations, float radius)
	{
		List<Point> out = new ArrayList<Point>();
		out.add(start);
		for (Point p : jaggedPathStep(new PointF(start.x, start.y), new PointF(end.x, end.y), iterations, radius))
		{
			out.add(p);
		}		
		out.add(end);
		return out;
	}
	
	public interface ForkingPath
	{
		Point getPoint();
		List<ForkingPath> getPaths();
	}
	
	public static ForkingPath getForkingPath(Point start, List<Point> targets, int iterations, float radius)
	{
	   //hit a random target.  Split into above and below.  generate a forking path to each from a random point (0.25 - 0.5?) and done
	   //get all targets outside a tolerance, fork, call getForkingPath on one of them randomly.  get their lists, ignore first point, add them!
		
		if (targets.isEmpty()) return new ForkingPath() {
			
			private Point startPoint;
			public ForkingPath init(Point startPoint)
			{
				this.startPoint = startPoint;
				return this;
			}
			
			@Override
			public Point getPoint() {
				return startPoint;
			}
			
			@Override
			public List<ForkingPath> getPaths() {
				return new ArrayList<PointMath.ForkingPath>();
			}
		}.init(start);
		
		int randomPick = Global.rand.nextInt(targets.size());
		Point finalPoint = targets.get(randomPick);
		
		//it's possible to do more binning here so that more spawn from the start line... but eh
		List<Point> above = new ArrayList<Point>();
		List<Point> below = new ArrayList<Point>();
		List<Point> beyondTolerance = new ArrayList<Point>();
		
		float x = finalPoint.x - start.x;
		float y = finalPoint.y - start.y;
		float length = (float)Math.sqrt(x*x + y*y);
		
		final float tolerance = 0.6f;
		
		for (Point p : targets)
		{
			if (p != finalPoint)
			{
				float x2 = p.x - start.x;
				float y2 = p.y - start.y;
				float l2 = (float)Math.sqrt(x*x + y*y);
				//dot
		        if (Math.sqrt((x*x2 + y*y2)/(length*l2)) < tolerance)      		
		        {
		        	beyondTolerance.add(p);
		        	continue;
		        }
		        
		        //det
		        if (x * y2 - y * x2 > 0.0f)
		        {
		        	above.add(p);
		        }
		        else
		        {
		        	below.add(p);
		        }
			}
		}
		
		
		List<ForkingPath> out = new ArrayList<ForkingPath>();
		
		ForkingPath outputPath = new ForkingPath()
	    {
			Point p;
			List<ForkingPath> path;
			ForkingPath initialize(Point p, List<ForkingPath> path)
			{
				this.p = p;
				this.path = path;
				return this;
			}
			public Point getPoint()
			{
				return p;
			}
			public List<ForkingPath> getPaths()
			{
				return path;
			}
		}.initialize(start, out);		
		
		
		List<ForkingPath> lastOut = out;
		
		int aboveSplit = (int)(Global.rand.nextFloat() * (above.size()/0.55f) + above.size()/0.1f);
		int belowSplit = (int)(Global.rand.nextFloat() * (below.size()/0.55f) + below.size()/0.1f);
		
		for (ForkingPath path : getForkingPath(start, beyondTolerance, iterations, radius).getPaths())
		{
			//output path.
			out.add(path);
		}
		
		int arg = 0;
		int nextIter = iterations-1;
		for (Point p : jaggedPath(start, finalPoint, iterations, radius))
		{
			List<ForkingPath> sublist = new ArrayList<ForkingPath>();
			ForkingPath currentPath = new ForkingPath()
		    {
				Point p;
				List<ForkingPath> path;
				ForkingPath initialize(Point p, List<ForkingPath> path)
				{
					this.p = p;
					this.path = path;
					return this;
				}
				public Point getPoint()
				{
					return p;
				}
				public List<ForkingPath> getPaths()
				{
					return path;
				}
			}.initialize(p, sublist);
			
			if (arg == aboveSplit)
			{
				for (ForkingPath path : getForkingPath(p, above, nextIter, radius * 0.75f).getPaths())
				{
					sublist.add(path);
				}		
			}
			if (arg == belowSplit)
			{
				for (ForkingPath path : getForkingPath(p, below, nextIter, radius * 0.75f).getPaths())
				{
					sublist.add(path);
				}		
			}			
			++arg;
			
			lastOut.add(currentPath);
			lastOut = sublist;
		}
		
		return outputPath;
	}
}
