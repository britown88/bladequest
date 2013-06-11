package bladequest.world;

import java.util.HashMap;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import bladequest.battleactions.BattleAction;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.AnimatedBitmap;
import bladequest.graphics.AnimationBuilder;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleAnimObjState;
import bladequest.graphics.BattleAnimObject;
import bladequest.graphics.BitmapFrame;
import bladequest.graphics.BattleAnimObjState.PosTypes;
import bladequest.graphics.BattleAnimObject.Types;
import bladequest.math.PointMath;

public class BattleAnimations 
{
	public static void createAnimationBuilders()
	{
		Global.animationBuilders = new HashMap<String, AnimationBuilder>();
		Global.animationBuilders.put("antidote", getAntidoteAnim());
		Global.animationBuilders.put("chilllasers", getChillLasers());
		Global.animationBuilders.put("chilliceblock", getChillIceBlock());		
		Global.animationBuilders.put("drain", getDrainAnim());
		Global.animationBuilders.put("entomb", getEntombAnim());
		Global.animationBuilders.put("gale", getGaleAnim());		
		Global.animationBuilders.put("heal", getHealAnim());
		Global.animationBuilders.put("icebarrage", getIceBarrage());
		Global.animationBuilders.put("ignite", getIgniteAnim());
		Global.animationBuilders.put("igniteSmoke", getIgniteSmokeAnim());		
		Global.animationBuilders.put("potion", getPotionAnim());
		Global.animationBuilders.put("provoke", getProvokeAnim());
		Global.animationBuilders.put("redcard", getRedCardAnim());
		Global.animationBuilders.put("tranquilizer", getTranquilizerAnim());
		Global.animationBuilders.put("trickery", getTrickeryAnim());
	}
	
	

	public static BattleAnim getSnowImplosion(PlayerCharacter target, float minAngle, float maxAngle)
	{
		BattleAnim anim = new BattleAnim(1000.0f);
		
		
		Point position = target.getPosition();
		
		Bitmap icePoof = Global.bitmaps.get("particles");
		Rect poofRect = new Rect(1,13,13,24);
		
		//to radian
		minAngle = (float)(Math.PI/180.0f * minAngle);
		maxAngle = (float)(Math.PI/180.0f * maxAngle);
		
		final int poofs = 50;
		final float minVel = 4.0f;
		final float maxVel = 72.0f;
		final int life = 450;
		
		for (int i = 0; i < poofs; ++i)
		{
			//add a poof at this point that's fairly long-lived.
			BattleAnimObject poofAnim = new BattleAnimObject(Types.Bitmap, false, icePoof);
			
			float initialOffsetX = Global.rand.nextFloat() * 32.0f -  16.0f;
			float initialOffsetY = Global.rand.nextFloat() * 32.0f -  16.0f;
			
			float angle = minAngle + (float)(Global.rand.nextFloat()* (minAngle-maxAngle));
			float velocity = Global.rand.nextFloat() * (maxVel - minVel) + minVel;
			float x = initialOffsetX + ((float)Math.cos(angle)) * velocity;
			float y = initialOffsetY + ((float)Math.sin(angle)) * velocity;
			
			
			float rnd = Global.rand.nextFloat() * 360.0f;
			BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Screen);
			state.size = new Point((int)(poofRect.width()*4.5f), (int)(poofRect.height()*4.5f));
			state.pos1 = PointMath.add(position, new Point((int)initialOffsetX,(int)initialOffsetY));
			state.argb(196, 255, 255, 255);
			state.rotation = rnd;
			state.setBmpSrcRect(poofRect.left, poofRect.top, poofRect.right, poofRect.bottom);
			poofAnim.addState(state);
			
			state = new BattleAnimObjState(life, PosTypes.Screen);
			state.size = new Point(poofRect.width()*3, poofRect.height()*3);
			state.pos1 = PointMath.add(position, new Point((int)x,(int)y));
			state.argb(0, 255, 255, 255);
			state.rotation = rnd;
			state.setBmpSrcRect(poofRect.left, poofRect.top, poofRect.right, poofRect.bottom);
			poofAnim.addState(state);
			
			anim.addObject(poofAnim);
		}
		

		
		return anim;
	}
	
	public static BitmapFrame getIceBlock()
	{
		return new BitmapFrame(Global.bitmaps.get("iceblock"), new Rect(0,0,25,38));
	}
	

	public static Rect getCharacterIceCube(PlayerCharacter c)
	{
		int width = (int)(c.getWidth()/2.94);
		int height =(int)(c.getHeight()/2.1);
		
		Point drawPoint = c.getPosition(true);
		Rect r = new Rect(drawPoint.x -width, drawPoint.y-height,
						  drawPoint.x +width, drawPoint.y+height);
		
		return r;
	}
	
	
	public static AnimatedBitmap getRedCard()
	{
		return new AnimatedBitmap()	
		{
			BitmapFrame[] frames;
			{
				final int height = 20;
				final int width = 10;
				
				frames = new BitmapFrame[6];
				for (int i = 0; i < 6; ++i)
				{
					frames[i] = new BitmapFrame(Global.bitmaps.get("redCard"),new Rect(i*width, 0, (i+1)*width, height));
				}
			}
			@Override
			public BitmapFrame[] getFrames() {
				return frames;
			}
			
		};
	}	
	
	public static AnimatedBitmap getBeserkerBase()
	{
		return new AnimatedBitmap()	
		{
			
			BitmapFrame[] frames;
			{
				final int height = 51;
				final int width = 49;
				
				frames = new BitmapFrame[4];
				for (int i = 0; i < 4; ++i)
				{
					frames[i] = new BitmapFrame(Global.bitmaps.get("beserkstance"),new Rect(i*width, 0, (i+1)*width, height));
				}
			}
			@Override
			public BitmapFrame[] getFrames() {
				return frames;
			}
			
		};
	}
	
	public static AnimatedBitmap getGale()
	{
		return new AnimatedBitmap()	
		{
			
			BitmapFrame[] frames;
			{
				final int height = 56;
				final int width = 34;
				
				frames = new BitmapFrame[2];
				for (int i = 0; i < frames.length; ++i)
				{
					frames[i] = new BitmapFrame(Global.bitmaps.get("gale"),new Rect(i*width, 0, (i+1)*width, height));
				}
			}
			@Override
			public BitmapFrame[] getFrames() {
				return frames;
			}
			
		};
	}
	public static AnimatedBitmap getReactionAnger()
	{
		return new AnimatedBitmap()	
		{
			BitmapFrame[] frames;
			{
				final int height = 32;
				final int width = 16;
				
				frames = new BitmapFrame[4];
				for (int i = 0; i < frames.length; ++i)
				{
					frames[i] = new BitmapFrame(Global.bitmaps.get("reactionbubbles"),new Rect((i+7)*width, 0, (i+8)*width, height));
				}
			}
			@Override
			public BitmapFrame[] getFrames() {
				return frames;
			}
			
		};
	}
	public static AnimatedBitmap getReactionQuestion()
	{
		return new AnimatedBitmap()	
		{
			BitmapFrame[] frames;
			{
				final int height = 32;
				final int width = 16;
				
				frames = new BitmapFrame[2];
				for (int i = 0; i < frames.length; ++i)
				{
					frames[i] = new BitmapFrame(Global.bitmaps.get("reactionbubbles"),new Rect((i+5)*width, 0, (i+6)*width, height));
				}
			}
			@Override
			public BitmapFrame[] getFrames() {
				return frames;
			}
			
		};
	}
	
	
	public static AnimatedBitmap getReactionDotDotDot()
	{
		return new AnimatedBitmap()	
		{
			BitmapFrame[] frames;
			{
				final int height = 32;
				final int width = 16;
				
				frames = new BitmapFrame[3];
				for (int i = 0; i < frames.length; ++i)
				{
					frames[i] = new BitmapFrame(Global.bitmaps.get("reactionbubbles"),new Rect((i)*width, 32, (i+1)*width, 32+height));
				}
			}
			@Override
			public BitmapFrame[] getFrames() {
				return frames;
			}
			
		};
	}
	
	public static AnimatedBitmap getReactionExclamation()
	{
		return new AnimatedBitmap()	
		{
			BitmapFrame[] frames;
			{
				final int height = 32;
				final int width = 16;
				
				frames = new BitmapFrame[3];
				for (int i = 0; i < frames.length; ++i)
				{
					frames[i] = new BitmapFrame(Global.bitmaps.get("reactionbubbles"),new Rect((i+3)*width, 32, (i+4)*width, 32+height));
				}
			}
			@Override
			public BitmapFrame[] getFrames() {
				return frames;
			}
			
		};
	}

	
	public static AnimationBuilder getRedCardAnim()
	{
		return new AnimationBuilder()
		{

			@Override
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BitmapFrame [] frames = getRedCard().getFrames();
				
				Bitmap bmp = frames[0].bitmap;
				
				final int spins = 5;
				final int frameTime = 45;
				final int fadeTime = 250;
				final int cardHeight = -55;
				final int cardOffsetX = -24;
				
				BattleAnim out = new BattleAnim(1000.0f);
				
				BattleAnimObject redCard = new BattleAnimObject(Types.Bitmap, false, bmp);
				
				int time = 0;
				
				for (int i = 0; i < spins; ++i)
				{
					for (BitmapFrame frame : frames)
					{
						Rect r = frame.srcRect;
						BattleAnimObjState state = new BattleAnimObjState(time, PosTypes.Source);
						time += frameTime;
						state.size = new Point(r.width(), r.height());
						state.pos1 = new Point(cardOffsetX, cardHeight);
						state.argb(255, 255, 255, 255);
						state.rotation = 0;  //angle += 270
						state.setBmpSrcRect(r.left, r.top, r.right, r.bottom);
						redCard.addState(state);
					}
				}
				
				Rect r = frames[0].srcRect;
				BattleAnimObjState state = new BattleAnimObjState(time, PosTypes.Source);
				state.size = new Point(r.width(), r.height());
				state.pos1 = new Point(cardOffsetX, cardHeight);
				state.argb(255, 255, 255, 255);
				state.rotation = 0;  //angle += 270
				state.setBmpSrcRect(r.left, r.top, r.right, r.bottom);
				redCard.addState(state);
				
				state = new BattleAnimObjState(time + fadeTime, PosTypes.Source);
				state.size = new Point(r.width(), r.height());
				state.pos1 = new Point(cardOffsetX, cardHeight);
				state.argb(255, 255, 255, 255);
				state.rotation = 0;  //angle += 270
				state.setBmpSrcRect(r.left, r.top, r.right, r.bottom);
				redCard.addState(state);				
				
				state = new BattleAnimObjState(time + fadeTime*2, PosTypes.Source);
				state.size = new Point(0, 0);
				state.pos1 = new Point(cardOffsetX, cardHeight);
				state.argb(255, 255, 255, 255);
				state.rotation = 0;  //angle += 270
				state.setBmpSrcRect(r.left, r.top, r.right, r.bottom);
				redCard.addState(state);				
				
				out.addObject(redCard);
				
				return out;
			}
			
		};
	}
	public static AnimationBuilder getIceBarrage()
	{
		return new AnimationBuilder()
		{
			public BattleAnim buildAnimation(BattleEventBuilder builder) {

				//everything is at a -45 degree angle, give or take a couple degrees.
				//generate more and more and more icicles, and make them poof into small snow clouds on impact that fade gracefully up at an angle.
				
				BattleAnim out = new BattleAnim(1000.0f); //working in milliseconds
				
				final int icicleCount = 50;
				final float iceStormLength = 2500;
				final float icicleLife = 500;  
				
				Bitmap icicleBitmap = Global.bitmaps.get("icicle"); 
				Rect icicleRect = new Rect(0,0,30,80);	
				
				Bitmap icePoof = Global.bitmaps.get("particles");
				Rect poofRect = new Rect(1,13,13,24);
				
				for (int i = 0; i < icicleCount; ++i) 
				{
					BattleAnimObject icicle = new BattleAnimObject(Types.Bitmap, false, icicleBitmap);
					BattleAnimObject poofObj = new BattleAnimObject(Types.Bitmap, false, icePoof);
					
					
					float angle = Global.rand.nextFloat() * 10.0f + 65.0f;
					double angRad = (angle) * (Math.PI/180.0f);
					
					float xDir = (float)Math.cos(angRad);
					float yDir = (float)Math.sin(angRad);
					
					float reflY = yDir * -1;
					
					final int perspectiveSize = 80;
					final int hitBuffer = 300;
					//a bit of buffer on the end so as to not mess with the UI
					float targetX = (float)(hitBuffer + Global.rand.nextInt(Global.vpWidth-hitBuffer-64));
					float targetY = perspectiveSize + Global.rand.nextInt(Global.vpHeight - perspectiveSize-32);
					
					float startX = targetX - (xDir * 400.0f);
					float startY = targetY - (yDir * 400.0f);
					
					
					
					
					float t = ((float)i)/(icicleCount-1);

					//start state
					BattleAnimObjState state = new BattleAnimObjState((int)(t * iceStormLength), PosTypes.Screen);
					state.size = new Point(icicleRect.width()/3, icicleRect.height()/3);
					state.pos1 = new Point((int)startX, (int)startY);
					state.argb(255, 255, 255, 255);
					state.rotation = 270+angle;  //angle += 270
					state.setBmpSrcRect(icicleRect.left, icicleRect.top, icicleRect.right, icicleRect.bottom);
					icicle.addState(state);
					//end state
					int hitTime = (int)(t * iceStormLength+ icicleLife);
					state = new BattleAnimObjState((int)(hitTime), PosTypes.Screen);
					state.size = new Point(icicleRect.width()/3, icicleRect.height()/3);
					state.pos1 = new Point((int)targetX, (int)targetY);
					state.argb(255, 255, 255, 255);
					state.rotation = 270+angle;
					state.setBmpSrcRect(icicleRect.left, icicleRect.top, icicleRect.right, icicleRect.bottom);
					icicle.addState(state);					

					icicle.interpolateLinearly();
					
					int poofCount = Global.rand.nextInt(2) + 1;
					
					for (int j = 0; j < poofCount; ++j)
					{
						
						float endX = targetX + xDir * 8.0f + Global.rand.nextFloat() * 3.0f - 1.5f;
						float endY = targetY + reflY * 8.0f + Global.rand.nextFloat() * 3.0f - 1.5f;

						int rot = Global.rand.nextInt(360);
						state = new BattleAnimObjState(hitTime, PosTypes.Screen);
						state.size = new Point(poofRect.width(), poofRect.height());
						state.pos1 = new Point((int)targetX, (int)targetY);
						state.argb(196, 255, 255, 255);
						state.rotation = rot;
						state.setBmpSrcRect(poofRect.left, poofRect.top, poofRect.right, poofRect.bottom);
						
						poofObj.addState(state);
						
						int left = 200 + Global.rand.nextInt(100);
						
						state = new BattleAnimObjState(hitTime + left, PosTypes.Screen);
						state.size = new Point(poofRect.width(), poofRect.height());
						state.pos1 = new Point((int)endX, (int)endY);
						state.argb(0, 255, 255, 255);
						state.rotation = rot + Global.rand.nextInt(10) - 5;
						state.setBmpSrcRect(poofRect.left, poofRect.top, poofRect.right, poofRect.bottom);						
						
						poofObj.addState(state);
					}
					
					out.addObject(icicle);
					out.addObject(poofObj);
				}				
				
				return out;
			}
		};
	}
	public static AnimationBuilder getEntombAnim()
	{
		return new AnimationBuilder()
		{
			
			@Override
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f); //working in milliseconds
				
				final int icicleCount = 16;
				final int icicleStrikeWait = 650;
				final int icicleStrikeTime = 105;
				final int icicleSpawnGap = 8;
				final int icicleStartWait = 1250;
				final int strikeGap = 40;
				final int iciclePoofLife = 750;
			//	final int icicleAdvanceTime = 100;
				final float icicleRadius = 60.0f;
				
				Bitmap icicleBitmap = Global.bitmaps.get("icicle"); 
				Rect icicleRect = new Rect(0,0,30,80);	
				
				Bitmap icePoof = Global.bitmaps.get("particles");
				Rect poofRect = new Rect(1,13,13,24);
				
				//inflict frozen status partially through this!
				
				for (int i = 0; i < icicleCount; ++i)
				{
					BattleAnimObject icicle = new BattleAnimObject(Types.Bitmap, false, icicleBitmap);
					
					int startTime = Global.rand.nextInt(strikeGap * icicleCount) + icicleStartWait;
					
					float radius = icicleRadius + Global.rand.nextFloat() * 16.0f;
					
					float angle =  (float)(Math.PI - (((float)i)/icicleCount-1) * Math.PI);
					float x = (float)(Math.cos(angle) * radius);
					float y = (float)(Math.sin(angle) * radius) + 16.0f;  //offset down slightly to feel a bit more entomb-y
										
					float drawAngle =(float)(90  + (angle * 180/Math.PI));
					float randAngle = Global.rand.nextFloat()*360.0f;
					
					
					int rndX = (int)x - 24 + Global.rand.nextInt(48);
					
					BattleAnimObjState state = new BattleAnimObjState(i * icicleSpawnGap, PosTypes.Target);
					state.pos1 = new Point(rndX, 18);
					state.size = new Point(icicleRect.width()/3, icicleRect.height()/3);
					state.argb(255, 255, 255, 255);
					state.rotation = randAngle;  //angle += 270
					state.setBmpSrcRect(icicleRect.left, icicleRect.top, icicleRect.right, icicleRect.bottom);
					icicle.addState(state);	
					
					state = new BattleAnimObjState(i * icicleSpawnGap+350, PosTypes.Target);
					state.pos1 = new Point(rndX, 18);
					state.size = new Point(icicleRect.width()/3, icicleRect.height()/3);
					state.argb(255, 255, 255, 255);
					state.rotation = randAngle;  //angle += 270
					state.setBmpSrcRect(icicleRect.left, icicleRect.top, icicleRect.right, icicleRect.bottom);
					icicle.addState(state);	
					
					state = new BattleAnimObjState(icicleStartWait, PosTypes.Target);
					state.pos1 = new Point((int)x, (int)y);
					state.size = new Point(icicleRect.width()/3, icicleRect.height()/3);
					state.argb(255, 255, 255, 255);
					state.rotation = drawAngle;  //angle += 270
					state.setBmpSrcRect(icicleRect.left, icicleRect.top, icicleRect.right, icicleRect.bottom);
					icicle.addState(state);		

					//hang in the air a bit.....
					state = new BattleAnimObjState(startTime + icicleStrikeWait, PosTypes.Target);
					state.pos1 = new Point((int)x, (int)y);
					state.size = new Point(icicleRect.width()/3, icicleRect.height()/3);
					state.argb(255, 255, 255, 255);
					state.rotation = drawAngle;  //angle += 270
					state.setBmpSrcRect(icicleRect.left, icicleRect.top, icicleRect.right, icicleRect.bottom);
					icicle.addState(state);		
					
					//Strike!  This should be pretty fast
					state = new BattleAnimObjState(startTime + icicleStrikeWait + icicleStrikeTime, PosTypes.Target);
					state.pos1 = new Point(0,0);
					state.size = new Point(icicleRect.width()/3, icicleRect.height()/3);
					state.argb(255, 255, 255, 255);
					state.rotation = drawAngle;
					state.setBmpSrcRect(icicleRect.left, icicleRect.top, icicleRect.right, icicleRect.bottom);
					icicle.addState(state);		
					
					out.addObject(icicle);
					
					//add a poof at this point that's fairly long-lived.
					BattleAnimObject poofAnim = new BattleAnimObject(Types.Bitmap, false, icePoof);
					
					float rnd = Global.rand.nextFloat() * 360.0f;
					state = new BattleAnimObjState(startTime + icicleStrikeWait +  icicleStrikeTime, PosTypes.Target);
					state.size = new Point((int)(poofRect.width()*4.5f), (int)(poofRect.height()*4.5f));
					state.pos1 = new Point(0,0);
					state.argb(196, 255, 255, 255);
					state.rotation = rnd;
					state.setBmpSrcRect(poofRect.left, poofRect.top, poofRect.right, poofRect.bottom);
					poofAnim.addState(state);
					
					
					int driftX = Global.rand.nextInt(96) - 48;
					int driftY = Global.rand.nextInt(96) - 48;
					
					state = new BattleAnimObjState(startTime + icicleStrikeTime + icicleStrikeWait + iciclePoofLife, PosTypes.Target);
					state.size = new Point(poofRect.width()*3, poofRect.height()*3);
					state.pos1 = new Point(driftX,driftY);
					state.argb(0, 255, 255, 255);
					state.rotation = rnd;
					state.setBmpSrcRect(poofRect.left, poofRect.top, poofRect.right, poofRect.bottom);
					poofAnim.addState(state);
					
					out.addObject(poofAnim);
				}
				
				return out;
			}			
		};
	}
	
	
	public static AnimationBuilder getProvoke()
	{
		return new AnimationBuilder()
		{
			@Override
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f);
				
				return out;
			}
		};
	}
	
	
	public static AnimationBuilder getProvokeAnim()
	{
	    	
		
		return new AnimationBuilder()
		{

			
			BattleAnimObjState buildBubbleForFrame(PlayerCharacter pc, int time, BitmapFrame frame, PosTypes characterOn)
			{
				int offset = 8;
				if (!pc.isEnemy()) offset = -offset;
				BattleAnimObjState out = new BattleAnimObjState(time, characterOn);
				out.pos1 = new Point(offset, -pc.getHeight()/2+4);
				out.argb(255, 255, 255, 255);
				out.size = new Point(32, 64);
				Rect r = frame.srcRect;
				out.setBmpSrcRect(r.left, r.top, r.right, r.bottom);
				return out;
			}
			BattleAnimObject buildBubbleFrames(PlayerCharacter pc, int time, BitmapFrame[] frames, int frameCount, int frameWait, PosTypes characterOn)
			{
				BattleAnimObject obj = new BattleAnimObject(Types.Bitmap, false, frames[0].bitmap);
				for (int i = 0; i < frameCount; ++i)
				{
					obj.addState(buildBubbleForFrame(pc, time+(i*frameWait), frames[i%frames.length], characterOn));
				}
				
				return obj;
			}
			BattleAnimObject buildWaitingBubble(PlayerCharacter pc, int time, BitmapFrame[] frames, int frameCount, int frameWait, PosTypes characterOn)
			{
				BattleAnimObject obj = new BattleAnimObject(Types.Bitmap, false, frames[0].bitmap);
				for (int i = 0; i < frameCount; ++i)
				{
					obj.addState(buildBubbleForFrame(pc, time+(i*frameWait), frames[Math.min(i, frames.length-1)], characterOn));
				}
				
				return obj;
			}			
			
			@Override
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f);
				
				PlayerCharacter source = builder.getSource();
				PlayerCharacter target = BattleAction.getTarget(builder);
				
				out.addObject(buildBubbleFrames(source, 0, getReactionDotDotDot().getFrames(), 10, 100, PosTypes.Source));
				out.addObject(buildWaitingBubble(target, 1100, getReactionQuestion().getFrames(), 5, 100, PosTypes.Target));
				out.addObject(buildWaitingBubble(source, 1700, getReactionExclamation().getFrames(), 5, 100, PosTypes.Source));
				out.addObject(buildBubbleFrames(target, 2500, getReactionAnger().getFrames(), 7, 100, PosTypes.Target));
				
				return out;
			}
		};
	}
	
	public static AnimationBuilder getTrickeryAnim()
	{
	    	
		
		return new AnimationBuilder()
		{

			
			BattleAnimObjState buildQuestionMarkFrame(float angle, int time, BitmapFrame frame, int offset)
			{
				BattleAnimObjState out = new BattleAnimObjState(time, PosTypes.Target);
				out.pos1 = PointMath.getRotatedPointDegrees(offset, 0.0f, -angle);
				out.argb(255, 255, 255, 255);
				out.size = new Point(32, 64);
				out.rotation = 270 + angle;
				Rect r = frame.srcRect;
				out.setBmpSrcRect(r.left, r.top, r.right, r.bottom);
				return out;
			}
			BattleAnimObject buildQuestionMark(float angle, int time, BitmapFrame[] frames, int offset)
			{
				final int questionMarkTime = 4;
				BattleAnimObject obj = new BattleAnimObject(Types.Bitmap, false, frames[0].bitmap);
				int i = 0;
				for (BitmapFrame frame : frames)
				{
					obj.addState(buildQuestionMarkFrame(angle, time+(i++)*100, frame, offset));
				}
				obj.addState(buildQuestionMarkFrame(angle, time+questionMarkTime*100, frames[frames.length-1], offset));
				
				return obj;
			}
			@Override
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f);
				BitmapFrame[] frames = getReactionQuestion().getFrames();
				
				int offset = -builder.getSource().getHeight()/2+4;
				out.addObject(buildQuestionMark(90.0f, 0, frames, offset));
				
				final int questionNumber = 8;
				final int initialWaitTime = 350;
				final int questionWaitTime = 120;
				
				for (int i = 0; i < questionNumber; ++i)
				{
					out.addObject(buildQuestionMark(Global.rand.nextFloat()*120 +30, initialWaitTime + questionWaitTime * i, frames, offset));
				}
				
				final int dotdotdotTime = initialWaitTime + questionWaitTime * questionNumber + 500; 
				
				BitmapFrame[] dotdotdotFrames = getReactionDotDotDot().getFrames();
				out.addObject(buildQuestionMark(90.0f, dotdotdotTime, dotdotdotFrames, offset));
				
				
				BitmapFrame[] exclamationFrames = getReactionExclamation().getFrames();
				
				out.addObject(buildQuestionMark(90.0f, dotdotdotTime+500, exclamationFrames, offset));
				
				return out;
			}
		};
	}

	public static AnimationBuilder getIgniteAnim()
	{
		return new AnimationBuilder()
		{
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f);
				
				Point playerPos = builder.getSource().getPosition(true);
				Point targetPos = BattleAction.getTarget(builder).getPosition(true);
				
				//shoot a fireball at an enemy which explodes rotated to their direction.
				
				//degrees
				float rotation = PointMath.angleBetween(playerPos, targetPos) * (180/(float)Math.PI);

				
				BitmapFrame[] frames = getFireAnim().getFrames();
				
				//this fireball should be small and go fast.
				BattleAnimObject fireball = new BattleAnimObject(Types.Bitmap, false, frames[0].bitmap);
				
				
				BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Screen);
				state.size = new Point((int)(frames[0].srcRect.width() * 0.5f), (int)(frames[0].srcRect.height() * 0.5f));
				state.pos1 = playerPos;
				state.argb(255, 255, 255, 255);
				state.rotation = 0.0f;

				Rect rect = frames[0].srcRect;
				state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
				fireball.addState(state);
				
				state = new BattleAnimObjState(150, PosTypes.Screen);
				state.size = new Point((int)(frames[0].srcRect.width() * 0.5f), (int)(frames[0].srcRect.height() * 0.5f));
				state.pos1 = targetPos;
				state.argb(255, 255, 255, 255);
				state.rotation = 0.0f;

				state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
				fireball.addState(state);		
				
				fireball.interpolateLinearly();
				
				out.addObject(fireball);
				
				final int frameWaitTime = 100;
				
				//now, to add the explosion.
			
				BattleAnimObject explosion = new BattleAnimObject(Types.Bitmap, false, frames[0].bitmap);
				for (int i = 0; i < frames.length+1; ++i)
				{
					BitmapFrame frame = frames[Math.min(i, frames.length-1)];
					state = new BattleAnimObjState(150 + i * frameWaitTime, PosTypes.Target);
					state.size = new Point((int)(frame.srcRect.width() * 1.5f), (int)(frame.srcRect.height() * 1.5f));
					state.pos1 = new Point(0,0);
					state.argb(255, 255, 255, 255);
					state.rotation = rotation + 90.0f;

					rect = frame.srcRect;
					state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
					explosion.addState(state);
				}
				out.addObject(explosion);
				
				return out;
			}
		};
	}
	
	public static AnimationBuilder getIgniteSmokeAnim()
	{
		return new AnimationBuilder()
		{
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f);
				
				Point playerPos = builder.getSource().getPosition(true);
				Point targetPos = BattleAction.getTarget(builder).getPosition(true);
				
		
				final float riseVel = -3.0f;
				final float airResist = 0.96f;
				final int steps = 10;
				int smokeParticles = Global.rand.nextInt(8) + 6;
				final int stepTime = 100;
				
				Bitmap smoke = Global.bitmaps.get("particles");
				Rect poofRect = new Rect(1,13,13,24); 
				

				for (int i = 0; i < smokeParticles; ++i)
				{
					
					float rotation = PointMath.angleBetween(playerPos, targetPos);
					
					rotation += (float)(Math.PI / 180.0 *( Global.rand.nextFloat() * 16 - 8));
				
					float x = (float)Math.cos(rotation);
					float y = (float)Math.sin(rotation);
					
					float velocity = 3.0f + Global.rand.nextFloat()*16;
					Point position = new Point(targetPos.x + Global.rand.nextInt(16)-8,
											   targetPos.y + Global.rand.nextInt(16)-8);
					
					
					float pX = position.x;
					float pY = position.y;
					
					float sprRotation = Global.rand.nextFloat()*360.0f;
					
					int startTime = 150 + Global.rand.nextInt(200) + 25;
					
					BattleAnimObject smokeObj = new BattleAnimObject(Types.Bitmap, false, smoke);
					
					int RGB = Global.rand.nextInt(32);
					
					for (int j = 0; j < steps; ++j)
					{
						
						BattleAnimObjState state = new BattleAnimObjState(startTime + j * stepTime, PosTypes.Screen);
						state.size = new Point(poofRect.width()*2 - ((j*poofRect.width()*2)/(steps-1)), 
								               poofRect.height()*2 - ((j*poofRect.height()*2)/(steps-1)) );
						state.pos1 = position;
						state.argb(196, RGB, RGB, RGB);
						state.colorize = 1.0f;
						state.rotation = sprRotation;
						
						state.setBmpSrcRect(poofRect.left, poofRect.top, poofRect.right, poofRect.bottom);
						
						smokeObj.addState(state);
						
						velocity *= airResist;
						pY += riseVel + y * velocity;
						pX += x*velocity;
						
						position = new Point((int)pX, (int)pY);
					}	
					
					out.addObject(smokeObj);
				}
				
				return out;
			}
		};
	}	

	public static AnimationBuilder getHealAnim()
	{
		return new AnimationBuilder()
		{
			public BattleAnim buildAnimation(BattleEventBuilder builder) 
			{
				BitmapFrame[] frames = getSparkleParticle().getFrames();
				BattleAnim out = new BattleAnim(60.0f);
				
				final int particleCount = 20;
				
				int particleDelay = 2;
				int sparkleDelay = 12;
				int riseRate = 16;
				
				for (int j = 0; j < particleCount; ++j) 
				{
					BattleAnimObject sparkle = new BattleAnimObject(Types.Bitmap, false, frames[0].bitmap);
					
					int xOffset = Global.rand.nextInt(36) - 18;
					int yOffset =  Global.rand.nextInt(8) + 32;
					
					int g = Global.rand.nextInt(64) + 256-64;

					for(int i = 0; i < frames.length; ++i)
					{
						int width = (int)(frames[i].srcRect.width() * 2.5f);
						int height = (int)(frames[i].srcRect.height() * 2.5f);						
						BattleAnimObjState state = new BattleAnimObjState(j*particleDelay + i * sparkleDelay, PosTypes.Target);
						state.size = new Point(width  - (width * i)/(frames.length-1), 
											   height - (height* i)/(frames.length-1));
						
						state.pos1 = new Point(xOffset,yOffset-(riseRate * i));
						state.argb(255, 0, g, 0);
						state.colorize = 1.0f;
						Rect r = frames[i].srcRect;
						state.setBmpSrcRect(r.left, r.top, r.right, r.bottom);
						sparkle.addState(state);
					}
					out.addObject(sparkle);
				}
				return out;				
			}
		};
	}
	public static AnimationBuilder getDrainAnim()
	{
		return new AnimationBuilder()
		{
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f);
				
				
				Point playerPos = builder.getSource().getPosition(true);
				Point targetPos = BattleAction.getTarget(builder).getPosition(true);
				
				final int steps = 8;
				final int stepTime = 80;
				final int stepDelay = 35;
				
				final int particleNumber = 30;
				final float radius = 40.0f;

				BitmapFrame[] frames = getSparkleParticle().getFrames();
				
				for (int i = 0; i < particleNumber; ++i)
				{
				    float t = ((float)i)/(particleNumber-1);
					
					int red = Global.rand.nextInt(120) + 120;
					
					BattleAnimObject blood = new BattleAnimObject(Types.Bitmap, false, frames[0].bitmap);
					
					int frame = Global.rand.nextInt(frames.length);
					float angle = Global.rand.nextFloat() * 360.0f;
							
					double angRad = (angle) * (Math.PI/180.0f);
							
					float rad = radius * (0.5f + Global.rand.nextFloat() * 0.5f);
					
					float x = (float)(Math.cos(angRad) * rad);
					float y = (float)(Math.sin(angRad) * rad);
					
					BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Target);
					state.size = new Point((int)(frames[frame].srcRect.width() * 1.5f), (int)(frames[frame].srcRect.height() * 1.5f));
					state.pos1 = new Point(0,0);
					state.argb(255, red, 0, 0);
					state.colorize = 1.0f;
					state.rotation = angle;

					Rect rect = frames[frame].srcRect;
					state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
					blood.addState(state);
					
					state = new BattleAnimObjState(200, PosTypes.Target);
					state.size = new Point((int)(frames[frame].srcRect.width() * 1.5f), (int)(frames[frame].srcRect.height() * 1.5f));
					state.pos1 = new Point((int)x,(int)y);
					state.argb(255, red, 0, 0);
					state.colorize = 1.0f;
					state.rotation = angle;

					state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
					blood.addState(state);					
					
					
					//fun part
					List<Point> points = PointMath.getArc(PointMath.offset(targetPos,x,y), playerPos, steps, t-.5f);
					
					int arg = 0;
					for (Point p : points)
					{
						state = new BattleAnimObjState(400 + stepDelay * i + (arg++) *stepTime, PosTypes.Screen);
						state.size = new Point((int)(frames[frame].srcRect.width() * 1.0f), (int)(frames[frame].srcRect.height() * 1.0f));
						state.pos1 = new Point(p.x, p.y);
						state.argb(255, (int)(red/1.35f), 0, 0);
						state.colorize = 1.0f;
						state.rotation = angle;					
						
						state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
						blood.addState(state);			
					}
					
					out.addObject(blood);
				}
				
				return out;
			}
		};
	}	
	
	public static AnimationBuilder getTranquilizerAnim()
	{
		return new AnimationBuilder()
		{
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f);
				
				
				//nananananananananananananananananana DART GUN

				final int flyTime = 100;
				final int stickTime = 250;

				Rect r = new Rect(26,0,34,3);
				Bitmap dartBmp = Global.bitmaps.get("particles");
				
				Point start = builder.getSource().getPosition(true);
				Point end =   BattleAction.getTarget(builder).getPosition(true);
				
				BattleAnimObject dart = new BattleAnimObject(Types.Bitmap, false, dartBmp);
				float angle = PointMath.angleBetween(start, end);
				
				BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Screen);
				state.size = new Point(r.width()*2, r.height()*2);
				state.pos1 = start;
				state.argb(255, 255, 255, 255);
				state.rotation = (float)(angle *180.0/Math.PI);
				state.setBmpSrcRect(r.left, r.top, r.right, r.bottom);
				dart.addState(state);
				
				
				state = new BattleAnimObjState(flyTime, PosTypes.Screen);
				state.size = new Point(r.width()*2, r.height()*2);
				state.pos1 = end;
				state.argb(255, 255, 255, 255);
				state.rotation = (float)(angle *180.0/Math.PI);
				state.setBmpSrcRect(r.left, r.top, r.right, r.bottom);
				dart.addState(state);
				
				
				state = new BattleAnimObjState(flyTime+stickTime, PosTypes.Screen);
				state.size = new Point(r.width()*2, r.height()*2);
				state.pos1 = end;
				state.argb(255, 255, 255, 255);
				state.rotation = (float)(angle *180.0/Math.PI);
				state.setBmpSrcRect(r.left, r.top, r.right, r.bottom);
				dart.addState(state);
				
				out.addObject(dart);
				
				return out;
			}
		};
	}
	
	public static AnimationBuilder getPotionAnim()
	{
		return new AnimationBuilder()
		{
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f);
				
				
				//put a whole bunch of sparkles, then suck them in.
				//spiral
				
				final int particleNumber = 30;
				final float radius = 40.0f;

				BitmapFrame[] frames = getSparkleParticle().getFrames();
				
				for (int i = 0; i < particleNumber; ++i)
				{
				
					int r = Global.rand.nextInt(225);
					int gb = Global.rand.nextInt(120) + 135;
					
					if (r > gb) r = gb;
					BattleAnimObject sparkle = new BattleAnimObject(Types.Bitmap, false, frames[0].bitmap);
					
					int frame = Global.rand.nextInt(frames.length);
					float angle = Global.rand.nextFloat() * 360.0f;
							
					double angRad = (angle) * (Math.PI/180.0f);
							
					float rad = radius * (0.5f + Global.rand.nextFloat() * 0.5f);
					
					float x = (float)(Math.cos(angRad) * rad);
					float y = (float)(Math.sin(angRad) * rad);
					
					BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Target);
					state.size = new Point((int)(frames[frame].srcRect.width() * 1.5f), (int)(frames[frame].srcRect.height() * 1.5f));
					state.pos1 = new Point((int)x,(int)y);
					state.argb(255, r, gb, gb);
					state.colorize = 1.0f;
					state.rotation = angle;

					Rect rect = frames[frame].srcRect;
					state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
					sparkle.addState(state);
					
					state = new BattleAnimObjState(100, PosTypes.Target);
					state.size = new Point((int)(frames[frame].srcRect.width() * 1.5f), (int)(frames[frame].srcRect.height() * 1.5f));
					state.pos1 = new Point((int)x,(int)y);
					state.argb(255, r, gb, gb);
					state.colorize = 1.0f;
					state.rotation = angle;

					state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
					sparkle.addState(state);					
					
					state = new BattleAnimObjState(400, PosTypes.Target);
					state.size = new Point((int)(frames[frame].srcRect.width() * 0.5f), (int)(frames[frame].srcRect.height() * 0.5f));
					state.pos1 = new Point(0, 0);
					state.argb(64, r, gb, gb);
					state.colorize = 1.0f;
					state.rotation = angle;					
					
					state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
					sparkle.addState(state);
			
					
					out.addObject(sparkle);
				}
				
				return out;
			}
		};
	}
		
	
	public static AnimationBuilder getAntidoteAnim()
	{
		return new AnimationBuilder()
		{
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f);
				
				
				//spiral
				
				final int antidoteSpiralPoints = 6;
				final int trail = 3;
				final int subStep = 32;
				final int subSteps = 5;
				final float radius = 40.0f;
				final int stepLen = 150;
				
				int step = 360/(antidoteSpiralPoints);
				int baseAngle = step;


				BitmapFrame[] frames = getSparkleParticle().getFrames();
				
				for (int i = 0; i < antidoteSpiralPoints; ++i)
				{
					
					BattleAnimObject[] sparkle = new BattleAnimObject[trail];
					
					for (int j = 0; j < trail; ++j)
					{
						sparkle[j] = new BattleAnimObject(Types.Bitmap, false, frames[0].bitmap);	
					}
					
					
					for (int j = 0; j < subSteps; ++j)
					{
						for (int k = 0; k < trail; ++k)
						{
						
							
							int idx = j + k;
							if (idx >= subSteps) continue;
							
							int frame = idx % (frames.length-1);
							float r = (radius / (subSteps-1)) * ((subSteps-1)-idx);
							int angle = baseAngle + subStep * idx;
							
							double angRad = (angle) * (Math.PI/180.0f);
							
							float x = (float)(Math.cos(angRad) * r);
							float y = (float)(Math.sin(angRad) * r);
							
							BattleAnimObjState state = new BattleAnimObjState(j*stepLen, PosTypes.Target);
							state.size = new Point((int)(frames[frame].srcRect.width() * 1.0f), (int)(frames[frame].srcRect.height() * 1.0f));
							state.pos1 = new Point((int)x,(int)y);
							state.argb(255, 0, 255 - (trail-k-1)*20, 0);
							state.colorize = 1.0f;
							state.rotation = Global.rand.nextFloat()*360.0f;
							
							Rect rect = frames[frame].srcRect;
							state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
							sparkle[k].addState(state);
						}
					}
					
					for (int j = 0; j < trail; ++j)
					{
						out.addObject(sparkle[j]);	
					}
					
					baseAngle += step;
				}
				
				return out;
			}
		};
	}
	public static AnimationBuilder getGaleAnim()
	{
		return new AnimationBuilder()
		{
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f);
				PlayerCharacter source = builder.getSource();
				PlayerCharacter target = BattleAction.getTarget(builder);
			
				Point startPos = source.getPosition(true);
				startPos.x += 16 + source.getWidth()/2;
				
				Point endPos = target.getPosition(true);
				
				BitmapFrame[] frames = getGale().getFrames();
			
				BattleAnimObject gale = new BattleAnimObject(Types.Bitmap, false, frames[0].bitmap);	
				
				//flip frames every 100ms
				final int growTime = 500;
				final int growSteps = 5;

				//part 1 - grow a up big and string
				for (int i = 0; i < growSteps; ++i)
				{
					BattleAnimObjState state = new BattleAnimObjState(i * (growTime/growSteps), PosTypes.Screen);
					
					float t = i/(float)growSteps;
					
					state.size = new Point(BattleAnim.linearInterpolation(0, frames[i%frames.length].srcRect.width()*2, t),
										   BattleAnim.linearInterpolation(0, frames[i%frames.length].srcRect.height()*2, t));
					state.pos1 = new Point(startPos.x, startPos.y);
					state.argb(255, 255, 255, 255);
					state.rotation = 0.0f;				
					
					Rect rect = frames[i%frames.length].srcRect;
					state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
					gale.addState(state);					
				}
				
				int step = growSteps;
					
				final int waitSteps = 3;
				
				for (int i = step; i < growSteps+waitSteps; ++i)
				{
					BattleAnimObjState state = new BattleAnimObjState(i * (growTime/growSteps), PosTypes.Screen);
					
					
					state.size = new Point(frames[i%frames.length].srcRect.width()*2,
										   frames[i%frames.length].srcRect.height()*2);
					state.pos1 = new Point(startPos.x, startPos.y);
					state.argb(255, 255, 255, 255);
					state.rotation = 0.0f;				
					
					Rect rect = frames[i%frames.length].srcRect;
					state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
					gale.addState(state);					
				}		
				
				step = growSteps + waitSteps;
				
				final int shootSteps = 10;
				//part 2 - pew pew pew pew pew
				
				for (; step < growSteps + shootSteps + waitSteps; ++step)
				{
					int i = step-growSteps-waitSteps;
					
					BattleAnimObjState state = new BattleAnimObjState((i + waitSteps) * 100 + growTime, PosTypes.Screen);
					
					float t = (i/(float)(shootSteps-1));
					
					state.size = new Point(frames[i%frames.length].srcRect.width()*2,
							   			   frames[i%frames.length].srcRect.height()*2);
					
					t *= 4.8f;
					state.pos1 = BattleAnim.linearInterpolation(startPos, endPos, t);
					state.argb(255, 255, 255, 255);
					state.rotation = 0.0f;		
					
					Rect rect = frames[step%frames.length].srcRect;
					state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
					gale.addState(state);		
				}
				

				out.addObject(gale);
				return out;
			}
		};
	}
	
	public static AnimatedBitmap getBeserkerSwords()
	{
		return new AnimatedBitmap()	
		{
			
			BitmapFrame[] frames;
			{
				final int top = 50;
				final int height = 14;
				final int width = 15;
				
				frames = new BitmapFrame[5];
				for (int i = 0; i < 5; ++i)
				{
					frames[i] = new BitmapFrame(Global.bitmaps.get("beserkstance"),new Rect(i*width, top, (i+1)*width, top + height));
				}
			}
			@Override
			public BitmapFrame[] getFrames() {
				return frames;
			}
			
		};
	}
	
	public static AnimatedBitmap getSparkleParticle()
	{
		return new AnimatedBitmap()	
		{

			BitmapFrame[] frames;
			{
				frames = new BitmapFrame[5];
				for (int i = 0; i < 5; ++i)
				{
					int idx = i;
					if (i > 2) idx = 4-i;
					frames[i] = new BitmapFrame(Global.bitmaps.get("particles"),new Rect(idx*8, 0, (idx+1)*8, 8));
				}
			}
			@Override
			public BitmapFrame[] getFrames() {
				return frames;
			}
			
		};
	}

	public static AnimatedBitmap getFireAnim()
	{
		return new AnimatedBitmap()	
		{

			BitmapFrame[] frames;
			{
				frames = new BitmapFrame[6];
				for (int i = 0; i < 6; ++i)
				{
					frames[i] = new BitmapFrame(Global.bitmaps.get("fire"),new Rect(i*53, 0, (i+1)*53, 53));
				}
			}
			@Override
			public BitmapFrame[] getFrames() {
				return frames;
			}
			
		};
	}
	
    public static AnimationBuilder getChillLasers()
    {
		return new AnimationBuilder()
		{
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f);    	
    	
				
				final int laserWait = 25;
				final int laserCount = 35;
				final int laserLength = 35;
				final int laserRadius = 120;
				
				for (int i = 0; i < laserCount; ++i)
				{
					BattleAnimObject obj = new BattleAnimObject(Types.Line, false, "");
					
					PointF dir = PointMath.randomDirection();
					Point p = new Point((int)(dir.x*laserRadius), (int)(dir.y*laserRadius));
					Point pOffset = new Point((int)(dir.x*laserLength), (int)(dir.y*laserLength));
					
					int g = Global.rand.nextInt(96) + 96;
					
					//initial state.
					BattleAnimObjState state = new BattleAnimObjState(laserWait*i, PosTypes.Target);
					state.pos1 = p;
					state.pos2 = PointMath.add(p, pOffset);
					state.strokeWidth = 2;
					state.argb(255, g/4, g, 255);
					obj.addState(state);
					
					//hit state
				
					state = new BattleAnimObjState(laserWait*i + laserRadius, PosTypes.Target);
					state.pos1 = new Point();
					state.pos2 = pOffset;
					state.strokeWidth = 2;
					state.argb(255, g/8, g/2, 255);
					obj.addState(state);
					
					//Final state		
					
					state = new BattleAnimObjState(laserWait*i + laserRadius + laserLength, PosTypes.Target);
					state.pos1 = new Point();
					state.pos2 = pOffset;
					state.strokeWidth = 2;
					state.argb(255, 0, g/4, 196);
					obj.addState(state);	
					
					out.addObject(obj);
				}
				return out;
			}
		};
    }
    
    
    public static AnimationBuilder getChillIceBlock()
    {
		return new AnimationBuilder()
		{
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f);    	
    	
				final int iceCubeStayTime = 650;
				final int iceCubeFadeTime = 250;
				
				Rect r = BattleAction.getTarget(builder).getRect();
				
				int size = r.width();
				int height = r.height();
				
				if (height > size) size = height;
				
				size = (int)(size*1.2f);
				
				BitmapFrame iceblock = getIceBlock();
				
				Point p = new Point(0, (height-size)/2);
				
				height = (int)((iceblock.srcRect.height()/((float)iceblock.srcRect.width())) * size);
				
				BattleAnimObject iceCube = new BattleAnimObject(Types.Bitmap, false, iceblock.bitmap);
				
				BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Target); 
				state.size = new Point(size, height);
				state.pos1 = p;
				state.argb(255, 255, 255, 255);
				state.setBmpSrcRect(iceblock.srcRect.left, iceblock.srcRect.top, iceblock.srcRect.right, iceblock.srcRect.bottom);
				iceCube.addState(state);
				
				state = new BattleAnimObjState(iceCubeStayTime, PosTypes.Target); 
				state.size = new Point(size, height);
				state.pos1 = p;
				state.argb(255, 255, 255, 255);
				state.setBmpSrcRect(iceblock.srcRect.left, iceblock.srcRect.top, iceblock.srcRect.right, iceblock.srcRect.bottom);
				iceCube.addState(state);				
								
				state = new BattleAnimObjState(iceCubeFadeTime + iceCubeStayTime, PosTypes.Target); 
				state.size = new Point(size, height);
				state.pos1 = p;
				state.argb(0, 255, 255, 255);
				state.setBmpSrcRect(iceblock.srcRect.left, iceblock.srcRect.top, iceblock.srcRect.right, iceblock.srcRect.bottom);
				iceCube.addState(state);
				
				out.addObject(iceCube);
								
				
				return out;
			}
		};
    }
	
}
