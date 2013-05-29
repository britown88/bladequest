package bladequest.world;

import java.util.ArrayList;
import java.util.List;

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
				long frameLength = 40;
				int index = 0;
				int finalFrames = 2 << iterations;
				
				List<ForkingPath> openPaths = new ArrayList<ForkingPath>();
				List<ForkingPath> lastPaths;

				openPaths.add(PointMath.getForkingPath(start, targets, iterations, radius));

				while (!openPaths.isEmpty())
				{
				  lastPaths = openPaths;
				  openPaths = new ArrayList<ForkingPath>();
				  for (ForkingPath pat : lastPaths)
				  {
				     Point p = pat.getPoint();
				     for (ForkingPath next : pat.getPaths())
				     {
				    	 Point nextPt = next.getPoint();
				    	 
				    	 BattleAnimObject obj = new BattleAnimObject(Types.Line, false, "");
							
							BattleAnimObjState state = new BattleAnimObjState((int)(index*frameLength), PosTypes.Screen);
							state.pos1 = new Point(p);
							state.pos2 = new Point(p);
							state.strokeWidth = 0.0f;								
							state.argb(255, 255, 255, 255);
							obj.addState(state);
							
							state = new BattleAnimObjState((int)(index*frameLength + frameLength), PosTypes.Screen);
							state.pos1 = new Point(p);
							state.pos2 = new Point(nextPt);
							state.strokeWidth = 5.0f;								
							state.argb(255, 255, 255, 255);
							obj.addState(state);
							
							state = new BattleAnimObjState((int)(finalFrames*frameLength), PosTypes.Screen);
							state.pos1 = new Point(p);
							state.pos2 = new Point(nextPt);
							state.strokeWidth = 5.0f;								
							state.argb(255, 255, 255, 255);
							obj.addState(state);
							
							anim.addObject(obj);
				        openPaths.add(next);
				     }				     
				  }
				  ++index;
				  
				}
				
				
								
		

				return anim;
			}
		}.initialize(start, targets, iterations, radius);
	}

}
