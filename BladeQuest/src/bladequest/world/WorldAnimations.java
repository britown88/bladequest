package bladequest.world;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Point;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.AnimationBuilder;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleAnimObjState;
import bladequest.graphics.BattleAnimObjState.PosTypes;
import bladequest.graphics.BattleAnimObject;
import bladequest.graphics.BattleAnimObject.Types;
import bladequest.math.PointMath;
import bladequest.math.PointMath.ForkingPath;

public class WorldAnimations 
{
	public static AnimationBuilder buildShatter(Point start, List<Point> targets, int iterations, float radius)
	{
		return new AnimationBuilder() {	
			Point start;
			List<Point> targets;
			int iterations;
			float radius;
			AnimationBuilder initialize(Point start, List<Point> targets, int iterations, float radius)
			{
				this.start = start;
				this.targets = targets;
				this.iterations = iterations;
				this.radius = radius;
				
				return this;
			}
			
			@Override
			public BattleAnim buildAnimation(BattleEventBuilder builder) 
			{
				BattleAnim anim = new BattleAnim(1000.0f);
				long frameLength = 175;
				int index = 0;
				int finalFrames = 2 << iterations;
				
				List<ForkingPath> openPaths = new ArrayList<ForkingPath>();
				List<ForkingPath> lastPaths = new ArrayList<ForkingPath>();
				
				List<ForkingPath> swapBuf; 

				openPaths.add(PointMath.getForkingPath(start, targets, iterations, radius));

				while (!openPaths.isEmpty())
				{
					swapBuf = lastPaths;
					lastPaths = openPaths;
					openPaths = swapBuf;
					openPaths.clear();
					for (ForkingPath pat : lastPaths)
					{
						Point p = pat.p;
						if (pat.child != null)
						{
							for (ForkingPath next = pat.child; next != null; next = next.next)
							{
								Point nextPt = next.p;
								 
						    	 BattleAnimObject obj = new BattleAnimObject(Types.Line, false, "");
									
								BattleAnimObjState state = new BattleAnimObjState((int)(index*frameLength), PosTypes.Screen);
								state.pos1 = new Point(p);
								state.pos2 = new Point(p);
								state.strokeWidth = 0.0f;								
								state.argb(255, 255, 0, 0);
								obj.addState(state);
								
								state = new BattleAnimObjState((int)(index*frameLength + frameLength), PosTypes.Screen);
								state.pos1 = new Point(p);
								state.pos2 = new Point(nextPt);
								state.strokeWidth = 2.0f;								
								state.argb(255, 255, 0, 0);
								obj.addState(state);
								
								state = new BattleAnimObjState((int)(finalFrames*frameLength), PosTypes.Screen);
								state.pos1 = new Point(p);
								state.pos2 = new Point(nextPt);
								state.strokeWidth = 2.0f;								
								state.argb(255, 0, 0, 0);
								obj.addState(state);
								
								state = new BattleAnimObjState((int)(finalFrames*frameLength + 1000), PosTypes.Screen);
								state.pos1 = new Point(p);
								state.pos2 = new Point(nextPt);
								state.strokeWidth = 2.0f;								
								state.argb(255, 0, 0, 0);
								obj.addState(state);		
								
								state = new BattleAnimObjState((int)(finalFrames*frameLength + 5000), PosTypes.Screen);
								state.pos1 = new Point(p);
								state.pos2 = new Point(nextPt);
								state.strokeWidth = 2.0f;								
								state.argb(255, 255, 0, 0);
								obj.addState(state);									
								
								anim.addObject(obj);
								
								openPaths.add(next);								
							}
						}
				     }				     
				  ++index;
				}
				return anim;
			}
		}.initialize(start, targets, iterations, radius);
	}
	public static AnimationBuilder buildGrowingCircle(Point start, int c, float time, int radius)
	{
		return new AnimationBuilder() {	
			Point start;
			int radius;
			int c;
			float time;
			AnimationBuilder initialize(Point start, int c, float time, int radius)
			{
				this.start = start;
				this.radius = radius;
				this.time = time;
				this.c = c;
				
				return this;
			}
			
			@Override
			public BattleAnim buildAnimation(BattleEventBuilder builder) 
			{
				BattleAnim anim = new BattleAnim(1000.0f);
				BattleAnimObject obj = new BattleAnimObject(Types.Elipse, false, "");
				BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Screen);
				state.pos1 = start;
				state.size = new Point();
				state.argb(Color.alpha(c), Color.red(c), Color.green(c), Color.blue(c));
				state.colorize = 1.0f;
				obj.addState(state);				
				
				state = new BattleAnimObjState((int)(time*1000), PosTypes.Screen);
				state.pos1 = start;
				state.size = new Point(radius*2, radius*2);
				state.argb(Color.alpha(c), Color.red(c), Color.green(c), Color.blue(c));
				state.colorize = 1.0f;
				obj.addState(state);
				anim.addObject(obj);
				
				return anim;
			}
		}.initialize(start, c, time, radius);
	}
	public static AnimationBuilder buildCircle(Point start, int c, int radius)
	{
		return new AnimationBuilder() {	
			Point start;
			int radius;
			int c;
			AnimationBuilder initialize(Point start, int c, int radius)
			{
				this.start = start;
				this.radius = radius;
				this.c = c;
				
				return this;
			}
			
			@Override
			public BattleAnim buildAnimation(BattleEventBuilder builder) 
			{
				BattleAnim anim = new BattleAnim(1000.0f);
				anim.loop();
				BattleAnimObject obj = new BattleAnimObject(Types.Elipse, false, "");
				BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Screen);
				state.pos1 = start;
				state.size = new Point(radius*2, radius*2);
				state.argb(Color.alpha(c), Color.red(c), Color.green(c), Color.blue(c));
				obj.addState(state);				
				
				state = new BattleAnimObjState(1000, PosTypes.Screen);
				state.pos1 = start;
				state.size = new Point(radius*2, radius*2);
				state.argb(Color.alpha(c), Color.red(c), Color.green(c), Color.blue(c));
				obj.addState(state);	
				anim.addObject(obj);
				
				return anim;
			}
		}.initialize(start, c, radius);
	}

}
