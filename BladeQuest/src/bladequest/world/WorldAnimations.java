package bladequest.world;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.AnimationBuilder;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleAnimObjState;
import bladequest.graphics.BattleAnimObjState.PosTypes;
import bladequest.graphics.BattleAnimObject;
import bladequest.graphics.BattleAnimObject.Types;
import bladequest.graphics.BitmapFrame;
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
	public static AnimationBuilder buildGrowingCircle(int c, float time, int radius)
	{
		return new AnimationBuilder() {	
			int radius;
			int c;
			float time;
			AnimationBuilder initialize(int c, float time, int radius)
			{
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
				BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Source);
				state.pos1 = new Point();
				state.size = new Point();
				state.argb(Color.alpha(c), Color.red(c), Color.green(c), Color.blue(c));
				state.colorize = 1.0f;
				obj.addState(state);				
				
				state = new BattleAnimObjState((int)(time*1000), PosTypes.Source);
				state.pos1 = new Point();
				state.size = new Point(radius*2, radius*2);
				state.argb(Color.alpha(c), Color.red(c), Color.green(c), Color.blue(c));
				state.colorize = 1.0f;
				obj.addState(state);
				anim.addObject(obj);
				
				return anim;
			}
		}.initialize(c, time, radius);
	}
	public static AnimationBuilder buildCircle(int c, int radius)
	{
		return new AnimationBuilder() {	
			int radius;
			int c;
			AnimationBuilder initialize(int c, int radius)
			{
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
				BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Source);
				state.pos1 = new Point();
				state.size = new Point(radius*2, radius*2);
				state.argb(Color.alpha(c), Color.red(c), Color.green(c), Color.blue(c));
				obj.addState(state);				
				
				state = new BattleAnimObjState(1000, PosTypes.Source);
				state.pos1 = new Point();
				state.size = new Point(radius*2, radius*2);
				state.argb(Color.alpha(c), Color.red(c), Color.green(c), Color.blue(c));
				obj.addState(state);	
				anim.addObject(obj);
				
				return anim;
			}
		}.initialize(c, radius);
	}
	public static AnimationBuilder buildSugoiMoon()
	{
		return new AnimationBuilder() {	
			AnimationBuilder initialize()
			{
				
				return this;
			}
			
			@Override
			public BattleAnim buildAnimation(BattleEventBuilder builder) 
			{
				BattleAnim anim = new BattleAnim(1000.0f);
				//anim.loop();
				BattleAnimObject obj = new BattleAnimObject(Types.Bitmap, false, "misc/sugoimoonkundesu");
				BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Screen);
				state.pos1 = new Point(100,100);
				state.size = new Point(60, 58);
				state.argb(255, 0, 0, 0);
				state.colorize = 0.0f;
				
				state.makeAnimated(0.1f);
				state.addFrame(new Rect(0, 0, 30, 29));
				state.addFrame(new Rect(30, 0, 60, 29));
				state.addFrame(new Rect(60, 0, 90, 29));
				state.addFrame(new Rect(90, 0, 120, 29));
				state.addFrame(new Rect(120, 0, 150, 29));
				state.addFrame(new Rect(90, 0, 120, 29));
				state.addFrame(new Rect(60, 0, 90, 29));
				state.addFrame(new Rect(30, 0, 60, 29));
				
				//state.setBmpSrcRect(0, 0, 30, 29);
				
				obj.addState(state);				
				
				state = new BattleAnimObjState(5000, PosTypes.Screen);
				state.pos1 = new Point(100,100);
				state.size = new Point(60, 58);
				state.argb(255, 0, 0, 0);
				state.colorize = 0.0f;
				//state.setBmpSrcRect(0, 0, 30, 29);
				obj.addState(state);	
				anim.addObject(obj);
				
				return anim;
			}
		}.initialize();
	}
	
	public static AnimationBuilder buildTwinkleShrink()
	{
		return new AnimationBuilder() {	
			AnimationBuilder initialize()
			{				
				return this;
			}
			
			@Override
			public BattleAnim buildAnimation(BattleEventBuilder builder) 
			{
				BattleAnim anim = new BattleAnim(1000.0f);
				//anim.loop();
				BattleAnimObject obj = new BattleAnimObject(Types.Bitmap, false, "misc/twinkle");
				
				BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Source);
				state.pos1 = new Point(0,0);
				state.size = new Point(27, 27);
				state.argb(255, 0, 0, 0);
				state.colorize = 0.0f;
				
				
				state.makeAnimated(0.1f);
				state.addFrame(new Rect(0, 0, 9, 9));
				state.addFrame(new Rect(9, 0, 18, 9));
				state.addFrame(new Rect(18, 0, 27, 9));
				state.addFrame(new Rect(9, 0, 18, 9));
				
				obj.addState(state);				
				
				state = new BattleAnimObjState(12000, PosTypes.Source);
				state.pos1 = new Point(0,0);
				state.size = new Point(0, 0);
				state.argb(255, 0, 0, 0);
				state.colorize = 0.0f;
				obj.addState(state);	
				anim.addObject(obj);
				
				return anim;
			}
		}.initialize();
	}
	
	public static AnimationBuilder buildTitleSequence()
	{
		return new AnimationBuilder() {	
			AnimationBuilder initialize()
			{				
				return this;
			}
			
			@Override
			public BattleAnim buildAnimation(BattleEventBuilder builder) 
			{
				BattleAnim anim = new BattleAnim(1000.0f);

				//twinkle
				BattleAnimObject obj = new BattleAnimObject(Types.Bitmap, false, "misc/twinkle");
				BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Screen);
				state.pos1 = new Point(120,60);
				state.size = new Point(18, 18);
				state.argb(255, 0, 0, 0);
				state.colorize = 0.0f;				
				state.makeAnimated(0.1f);
				state.addFrame(new Rect(0, 0, 9, 9));
				state.addFrame(new Rect(9, 0, 18, 9));
				state.addFrame(new Rect(18, 0, 27, 9));
				state.addFrame(new Rect(9, 0, 18, 9));				
				obj.addState(state);				
				state = new BattleAnimObjState(30000, PosTypes.Screen);
				state.pos1 = new Point(450,120);
				state.size = new Point(18, 18);
				state.argb(255, 0, 0, 0);
				state.colorize = 0.0f;
				//state.setBmpSrcRect(0, 0, 30, 29);
				obj.addState(state);	
				anim.addObject(obj);
				
				obj = new BattleAnimObject(Types.Bitmap, false, "dapperhatlogotrans");
				state = new BattleAnimObjState(2000, PosTypes.Screen);
				state.pos1 = new Point(224, 144);
				state.size = new Point(144, 57);
				state.argb(0, 0, 0, 0);
				state.colorize = 0.0f;
				obj.addState(state);
				state = new BattleAnimObjState(4000, PosTypes.Screen);
				state.pos1 = new Point(224, 144);
				state.size = new Point(144, 57);
				state.argb(255, 255, 255, 255);
				state.colorize = 1.0f;
				obj.addState(state);
				state = new BattleAnimObjState(7000, PosTypes.Screen);
				state.pos1 = new Point(224, 144);
				state.size = new Point(144, 57);
				state.argb(255, 0, 0, 0);
				state.colorize = 0.0f;
				obj.addState(state);
				state = new BattleAnimObjState(8000, PosTypes.Screen);
				state.pos1 = new Point(224, 144);
				state.size = new Point(144, 57);
				state.argb(255, 0, 0, 0);
				state.colorize = 0.0f;
				obj.addState(state);
				state = new BattleAnimObjState(10000, PosTypes.Screen);
				state.pos1 = new Point(224, 144);
				state.size = new Point(144, 57);
				state.argb(0, 0, 0, 0);
				state.colorize = 0.0f;
				obj.addState(state);				

				anim.addObject(obj);
				
				
				obj = new BattleAnimObject(Types.Bitmap, false, "titlemainsilver");
				state = new BattleAnimObjState(10000, PosTypes.Screen);
				state.pos1 = new Point(224, 144);
				state.size = new Point(224, 144);
				state.argb(0, 0, 0, 0);
				state.colorize = 0.0f;
				obj.addState(state);
				state = new BattleAnimObjState(12000, PosTypes.Screen);
				state.pos1 = new Point(224, 144);
				state.size = new Point(224, 144);
				state.argb(255, 255, 255, 255);
				state.colorize = 1.0f;
				obj.addState(state);
				state = new BattleAnimObjState(15000, PosTypes.Screen);
				state.pos1 = new Point(224, 144);
				state.size = new Point(224, 144);
				state.argb(255, 0, 0, 0);
				state.colorize = 0.0f;
				obj.addState(state);
				state = new BattleAnimObjState(30000, PosTypes.Screen);
				state.pos1 = new Point(224, 144);
				state.size = new Point(224, 144);
				state.argb(255, 0, 0, 0);
				state.colorize = 0.0f;
				obj.addState(state);				

				anim.addObject(obj);
				
				obj = new BattleAnimObject(Types.Bitmap, false, "titlesubsilver");
				state = new BattleAnimObjState(12000, PosTypes.Screen);
				state.pos1 = new Point(224, 144);
				state.size = new Point(224, 144);
				state.argb(0, 0, 0, 0);
				state.colorize = 0.0f;
				obj.addState(state);
				state = new BattleAnimObjState(14000, PosTypes.Screen);
				state.pos1 = new Point(224, 144);
				state.size = new Point(224, 144);
				state.argb(255, 255, 255, 255);
				state.colorize = 1.0f;
				obj.addState(state);
				state = new BattleAnimObjState(17000, PosTypes.Screen);
				state.pos1 = new Point(224, 144);
				state.size = new Point(224, 144);
				state.argb(255, 0, 0, 0);
				state.colorize = 0.0f;
				obj.addState(state);
				state = new BattleAnimObjState(30000, PosTypes.Screen);
				state.pos1 = new Point(224, 144);
				state.size = new Point(224, 144);
				state.argb(255, 0, 0, 0);
				state.colorize = 0.0f;
				obj.addState(state);				

				anim.addObject(obj);
				

				
				return anim;
			}
		}.initialize();
	}
	
	
	public static void buildLightningStrike(BattleAnim anim, int time, 
			int x1, int y1, 
			int x2, int y2,
			int a1, int r1, int g1, int b1,
			int a2, int r2, int g2, int b2,
			float outerWidth, float innerWidth,
			int length)
	{

		List<Point> lightningBolt = PointMath.jaggedPath(new Point(x1, y1),
				                                         new Point(x2, y2), 2, 16);
		
		Point prev = null;
		for (Point p : lightningBolt)
		{
			if (prev == null)
			{
				prev = p;
				continue;
			}
			
			BattleAnimObject outer = new BattleAnimObject(Types.Line, false, "");
			BattleAnimObjState state = new BattleAnimObjState(time, PosTypes.Target);
			
			
			state.pos1 = new Point(prev);
			state.pos2 = new Point(p);
			state.strokeWidth = outerWidth;
			state.argb(a2, r2, g2, b2);
			outer.addState(state);
			
			
			state = new BattleAnimObjState(time+length/2, PosTypes.Target);
				
				
//			state.pos1 = new Point(prev);
//			state.pos2 = new Point(p);
//			state.strokeWidth = outerWidth;
//			state.argb(a2, r2, g2, b2);
//			outer.addState(state);			
//			
//			state = new BattleAnimObjState(time + length, PosTypes.Target);
			
			
			state.pos1 = new Point(prev);
			state.pos2 = new Point(p);
			state.strokeWidth = outerWidth * 0.5f;
			state.argb(0, r2, g2, b2);
			outer.addState(state);
			
			
			
			BattleAnimObject inner = new BattleAnimObject(Types.Line, false, "");
			state = new BattleAnimObjState(time, PosTypes.Target);
			
			
//			state.pos1 = new Point(prev);
//			state.pos2 = new Point(p);
//			state.strokeWidth = innerWidth;
//			state.argb(a1, r1, g1, b1);
//			inner.addState(state);
//			
//			state = new BattleAnimObjState(time + length/2, PosTypes.Target);
			
			
			state.pos1 = new Point(prev);
			state.pos2 = new Point(p);
			state.strokeWidth = innerWidth;
			state.argb(a1, r1, g1, b1);
			inner.addState(state);
						
			
			state = new BattleAnimObjState(time + length, PosTypes.Target);
			
			
			state.pos1 = new Point(prev);
			state.pos2 = new Point(p);
			state.strokeWidth = innerWidth * 0.5f;
			state.argb(0, r1, g1, b1);
			inner.addState(state);
			
			
			
			anim.addObject(outer);			
			anim.addObject(inner);			
			
			prev = p;
		}
	}
	
	
	public static void buildTransformationOval(BattleAnim anim, int time, int startDiameter, int finalDiameter, int animLength, int stayLength, int shrinkLength)
	{
		BattleAnimObject shrinkOval = new BattleAnimObject(Types.Elipse, false, "");
		
		int offset = 0;
		
		BattleAnimObjState state = new BattleAnimObjState(time, PosTypes.Target);
		
		state.pos1 = new Point(0,offset);
		state.size = new Point(startDiameter, startDiameter);
		state.argb(0,0,0,0);
		shrinkOval.addState(state);
		
		state = new BattleAnimObjState(time + animLength, PosTypes.Target);

		state.pos1 = new Point(0,offset);
		state.size = new Point(finalDiameter, finalDiameter);
		state.argb(255,0,0,0);
		shrinkOval.addState(state);
		
		state = new BattleAnimObjState(time + animLength + stayLength, PosTypes.Target);

		state.pos1 = new Point(0,offset);
		state.size = new Point(finalDiameter, finalDiameter);
		state.argb(255,0,0,0);
		shrinkOval.addState(state);
		
		anim.addObject(shrinkOval);
		
		state = new BattleAnimObjState(time + animLength + stayLength + shrinkLength, PosTypes.Target);

		state.pos1 = new Point(0,offset);
		state.size = new Point(0,0);
		state.argb(255,0,0,0);
		shrinkOval.addState(state);
		
		anim.addObject(shrinkOval);		
	}
	
	public static AnimationBuilder buildRolandTransformation()
	{
		return new AnimationBuilder() {
			@Override
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f);
				
				//oval in
				buildTransformationOval(out, 0, 500, 112, 2000, 1000, 1000);
				
				int offset = 0;
				
				//lightning
				for (int i = 0 ; i  < 50; ++i)
				{
					Point start = PointMath.randomPointOnRadius(56);
					
					buildLightningStrike(out,  2000 + i * 20, 
							start.x, start.y + offset, //start offset 
							0, offset, //target
							255, 255, 255, 255,  //inner
							128, 10, 220, 225, //outer
							4.0f, 2.0f,
							300);
				}
				
				//shrink lightning
				
				for (int i = 0 ; i  < 30; ++i)
				{
					int r = BattleAnim.cosineInterpolation(56, 0, i/39.0f);
					
					Point start = PointMath.randomPointOnRadius(r);
					
					int a = 255;//BattleAnim.cosineInterpolation(255, 0, i/39.0f);
					
					buildLightningStrike(out,  2000 + 1000 + i * 20, 
							start.x, start.y + offset, //start offset 
							0,offset, //target
							a, 255, 255, 255,  //inner
							a/2, 10, 220, 225, //outer
							4.0f, 2.0f,
							200);
				}				
				
				return out;
			}	
		};
	}
	
	
	public static AnimationBuilder buildIceCube(int length)
	{
		return new AnimationBuilder() {	
			int length;
			
			AnimationBuilder initialize(int length)
			{			
				this.length = length;
				return this;
			}
			
			@Override
			public BattleAnim buildAnimation(BattleEventBuilder builder) 
			{
				BattleAnim anim = new BattleAnim(1000.0f);
				//anim.loop();
				BattleAnimObject obj = new BattleAnimObject(Types.Bitmap, false, "misc/icecube");
				
				BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Target);
				state.pos1 = new Point(0,0);
				state.size = new Point(48, 48);
				state.argb(255, 0, 0, 0);
				state.colorize = 0.0f;
				obj.addState(state);				
				
				state = new BattleAnimObjState(length, PosTypes.Target);
				state.pos1 = new Point(0,0);
				state.size = new Point(48, 48);
				state.argb(255, 0, 0, 0);
				state.colorize = 0.0f;
				obj.addState(state);	
				anim.addObject(obj);
				
				return anim;
			}
		}.initialize(length);
	}
	
	public static AnimationBuilder buildIcePillar()
	{
		return new AnimationBuilder() {	
			
			AnimationBuilder initialize()
			{			
				return this;
			}
			
			@Override
			public BattleAnim buildAnimation(BattleEventBuilder builder) 
			{
				BattleAnim anim = new BattleAnim(1000.0f);
				//anim.loop();
				BattleAnimObject obj = new BattleAnimObject(Types.Bitmap, false, "misc/iceblockworld");
				
				BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Target);
				state.pos1 = new Point(0,0);
				state.size = new Point(50, 76);
				state.argb(255, 150, 225, 255);
				state.colorize = 1.0f;
				obj.addState(state);	
				
				state = new BattleAnimObjState(2000, PosTypes.Target);
				state.pos1 = new Point(0,0);
				state.size = new Point(50, 76);
				state.argb(255, 0, 0, 0);
				state.colorize = 0.0f;
				obj.addState(state);
				
				state = new BattleAnimObjState(20000, PosTypes.Target);
				state.pos1 = new Point(0,0);
				state.size = new Point(50, 76);
				state.argb(255, 0, 0, 0);
				state.colorize = 0.0f;
				obj.addState(state);	
				anim.addObject(obj);
				
				return anim;
			}
		}.initialize();
	}
	
	public static AnimationBuilder buildIceShot()
	{
		return new AnimationBuilder()
		{
		
			@Override
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f);
				
				final int icicleLife = 500;  
				
				float angle = Global.rand.nextFloat() * 10.0f + 65.0f;
				double angRad = (angle) * (Math.PI/180.0f);
				
				float xDir = (float)Math.cos(angRad);
				float yDir = (float)Math.sin(angRad);
				
				addIcicle(out, 0, 0.0f, 0.0f, angle, xDir, yDir);
				addPoof(out, icicleLife, 0.0f, 0.0f, xDir, yDir);
				
				return out;
			}	
		};
	}
	public static void addIcicle(BattleAnim anim, int time, float x, float y, float angle, float xDir, float yDir)
	{
		
		final int icicleLife = 500;  
		
		BitmapFrame icicleFrame = BattleAnimations.getIcicle();
		
		
		BattleAnimObject icicle = new BattleAnimObject(Types.Bitmap, false, icicleFrame.bitmap);
		
		float startX = x - (xDir * 400.0f);
		float startY = y - (yDir * 400.0f);				
		
		//start state
		BattleAnimObjState state = new BattleAnimObjState(time, PosTypes.Target);
		state.size = new Point(icicleFrame.srcRect.width()*2, icicleFrame.srcRect.height()*2);
		state.pos1 = new Point((int)startX, (int)startY);
		state.argb(255, 255, 255, 255);
		state.rotation = angle;  //angle += 270
		state.setBmpSrcRect(icicleFrame.srcRect.left, icicleFrame.srcRect.top, icicleFrame.srcRect.right, icicleFrame.srcRect.bottom);
		icicle.addState(state);
		//end state
		state = new BattleAnimObjState(time+ icicleLife, PosTypes.Target);
		state.size = new Point(icicleFrame.srcRect.width()*2, icicleFrame.srcRect.height()*2);
		state.pos1 = new Point((int)x,(int)y);
		state.argb(255, 255, 255, 255);
		state.rotation = angle;
		state.setBmpSrcRect(icicleFrame.srcRect.left, icicleFrame.srcRect.top, icicleFrame.srcRect.right, icicleFrame.srcRect.bottom);
		icicle.addState(state);					

		icicle.interpolateLinearly();
						
		anim.addObject(icicle);
	}
	
	public static void addPoof(BattleAnim anim, int time, float x, float y, float xDir, float yDir)
	{
		Bitmap icePoof = Global.bitmaps.get("misc/particles");
		Rect poofRect = new Rect(1,13,13,24);
		
		int poofCount = Global.rand.nextInt(2) + 1;
		
		float reflY = yDir * -1; 
		
		for (int j = 0; j < poofCount; ++j)
		{
			BattleAnimObject poofObj = new BattleAnimObject(Types.Bitmap, false, icePoof);
			
			float endX = x + xDir * 14.0f + Global.rand.nextFloat() * 8.0f - 4.0f;
			float endY = y + reflY * 14.0f + Global.rand.nextFloat() * 8.0f - 4.0f;

			int rot = Global.rand.nextInt(360);
			BattleAnimObjState state = new BattleAnimObjState(time, PosTypes.Target);
			state.size = new Point(poofRect.width() * 2, poofRect.height() * 2);
			state.pos1 = new Point((int)x, (int)y);
			state.argb(196, 255, 255, 255);
			state.rotation = rot;
			state.setBmpSrcRect(poofRect.left, poofRect.top, poofRect.right, poofRect.bottom);
			
			poofObj.addState(state);
			
			int left = 200 + Global.rand.nextInt(100);
			
			state = new BattleAnimObjState(time + left, PosTypes.Target);
			state.size = new Point(poofRect.width() * 2, poofRect.height() * 2);
			state.pos1 = new Point((int)endX, (int)endY);
			state.argb(0, 255, 255, 255);
			state.rotation = rot + Global.rand.nextInt(16) - 8;
			state.setBmpSrcRect(poofRect.left, poofRect.top, poofRect.right, poofRect.bottom);						
			
			poofObj.addState(state);
			
			anim.addObject(poofObj);
		}
	}
	

	public static AnimationBuilder buildIceShatterPoof(float xDir, float yDir)
	{
		return new AnimationBuilder()
		{
			float xDir;
			float yDir;
			AnimationBuilder initialize(float xDir, float yDir)
			{
				this.xDir = xDir;
				this.yDir = yDir;
				return this;
			}
			@Override
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f);
				addShatterPoof(out, 0, xDir, yDir);
				return out;
			}
		}.initialize(xDir, yDir);
	}
	
	public static void addShatterPoof(BattleAnim anim, int time, float xDir, float yDir)
	{
		Bitmap icePoof = Global.bitmaps.get("misc/particles");
		Rect poofRect = new Rect(1,13,13,24);
		
		int poofCount = Global.rand.nextInt(3) + 3;
		
		float reflY = yDir * -1; 
		
		for (int j = 0; j < poofCount; ++j)
		{
			BattleAnimObject poofObj = new BattleAnimObject(Types.Bitmap, false, icePoof);
			
			float endX = xDir * 28.0f + Global.rand.nextFloat() * 16.0f - 8.0f;
			float endY = reflY * 28.0f + Global.rand.nextFloat() * 16.0f  - 8.0f;

			int rot = Global.rand.nextInt(360);
			BattleAnimObjState state = new BattleAnimObjState(time, PosTypes.Target);
			state.size = new Point(poofRect.width() * 2, poofRect.height() * 2);
			state.pos1 = new Point(Global.rand.nextInt(8) - 4, Global.rand.nextInt(8) - 4);
			state.argb(196, 255, 255, 255);
			state.rotation = rot;
			state.setBmpSrcRect(poofRect.left, poofRect.top, poofRect.right, poofRect.bottom);
			
			poofObj.addState(state);
			
			int left = 200 + Global.rand.nextInt(100);
			
			state = new BattleAnimObjState(time + left, PosTypes.Target);
			state.size = new Point(poofRect.width() * 2, poofRect.height() * 2);
			state.pos1 = new Point((int)endX, (int)endY);
			state.argb(0, 255, 255, 255);
			state.rotation = rot + Global.rand.nextInt(16) - 8;
			state.setBmpSrcRect(poofRect.left, poofRect.top, poofRect.right, poofRect.bottom);						
			
			poofObj.addState(state);
			
			anim.addObject(poofObj);
		}
	}

	//generates a storm centered at a target with a specified width and height.
	//drops the specified number of icicles in that time, to modify the intensity of the storm.
	//the very last icicle hit with at the time passed in as "iceStormLength" for timing, blocking, etc.
	public static AnimationBuilder buildIceBarrage(int width, int height, int icicleCount, int iceStormLength)
	{
		return new AnimationBuilder()
		{
			private int width, height, icicleCount, iceStormLength;
			public AnimationBuilder init(int width, int height, int icicleCount, int iceStormLength)
			{
				this.width = width;
				this.height = height;
				this.icicleCount = icicleCount;
				this.iceStormLength = iceStormLength;
				
				return this;
			}
			
			public BattleAnim buildAnimation(BattleEventBuilder builder) {

				//generate more and more and more icicles, and make them poof into small snow clouds on impact that fade gracefully up at an angle.
				
				BattleAnim out = new BattleAnim(1000.0f); //working in milliseconds
				
				final int icicleLife = 500;				
				for (int i = 0; i < icicleCount; ++i) 
				{
					float angle = Global.rand.nextFloat() * 10.0f + 65.0f;
					double angRad = (angle) * (Math.PI/180.0f);
					
					int t = 0;
					if (icicleCount > 1)
					{
						t = (i*(iceStormLength-icicleLife)) / (icicleCount-1);	
					}
					
					float xDir = (float)Math.cos(angRad);
					float yDir = (float)Math.sin(angRad);
					
					float x = Global.rand.nextFloat() * width - width/2;
					float y = Global.rand.nextFloat() * height - height/2;
					

					addIcicle(out, t, x, y, angle, xDir, yDir);
					addPoof(out, t+icicleLife, x, y, xDir, yDir);
				}				
				
				return out;
			}
		}.init(width, height, icicleCount, iceStormLength);
	}
	
}
