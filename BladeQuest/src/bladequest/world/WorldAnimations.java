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
				BattleAnimObject obj = new BattleAnimObject(Types.Bitmap, false, "sugoimoonkundesu");
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
				BattleAnimObject obj = new BattleAnimObject(Types.Bitmap, false, "twinkle");
				
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
				BattleAnimObject obj = new BattleAnimObject(Types.Bitmap, false, "twinkle");
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
		
		Bitmap icicleBitmap = Global.bitmaps.get("icicle"); 
		Rect icicleRect = new Rect(0,0,30, 80);
		
		
		BattleAnimObject icicle = new BattleAnimObject(Types.Bitmap, false, icicleBitmap);
		
		float startX = x - (xDir * 400.0f);
		float startY = y - (yDir * 400.0f);				
		
		//start state
		BattleAnimObjState state = new BattleAnimObjState(time, PosTypes.Target);
		state.size = new Point(icicleRect.width()/3, icicleRect.height()/3);
		state.pos1 = new Point((int)startX, (int)startY);
		state.argb(255, 255, 255, 255);
		state.rotation = 270+angle;  //angle += 270
		state.setBmpSrcRect(icicleRect.left, icicleRect.top, icicleRect.right, icicleRect.bottom);
		icicle.addState(state);
		//end state
		state = new BattleAnimObjState(time+ icicleLife, PosTypes.Target);
		state.size = new Point(icicleRect.width()/3, icicleRect.height()/3);
		state.pos1 = new Point((int)x,(int)y);
		state.argb(255, 255, 255, 255);
		state.rotation = 270+angle;
		state.setBmpSrcRect(icicleRect.left, icicleRect.top, icicleRect.right, icicleRect.bottom);
		icicle.addState(state);					

		icicle.interpolateLinearly();
						
		anim.addObject(icicle);
	}
	
	public static void addPoof(BattleAnim anim, int time, float x, float y, float xDir, float yDir)
	{
		Bitmap icePoof = Global.bitmaps.get("particles");
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
