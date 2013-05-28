package bladequest.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.Point;
import android.graphics.Rect;
import bladequest.graphics.BattleAnimObjState;
import bladequest.graphics.BattleAnimObjState.PosTypes;
import bladequest.graphics.BattleAnimObject.Types;
import bladequest.graphics.BattleSprite.faces;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleAnimObject;
import bladequest.graphics.BattleSprite;
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
}
