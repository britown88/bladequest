package bladequest.battleactions;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleAnimObjState;
import bladequest.graphics.BattleAnimObjState.PosTypes;
import bladequest.graphics.BattleAnimObject;
import bladequest.graphics.BattleAnimObject.Types;
import bladequest.graphics.BitmapFrame;
import bladequest.math.PointMath;
import bladequest.math.PointMath.ForkingPath;
import bladequest.world.BattleAnimations;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;


//Very specialized action just for shatter.  it was easiest to do it this way. :(
public class bactShatter extends DelegatingAction {

	public bactShatter() {
	}

	BattleAnim getShatterAnimation(PlayerCharacter target)
	{
		Rect r = BattleAnimations.getCharacterIceCube(target);
		
		
		BattleAnim anim = new BattleAnim(1000.0f);
		
		final int iterations = 4;
		final int shatterPoints = 24;
		final int frameLength = 4;
		int finalFrames = 2 << iterations;
		
		
		BitmapFrame iceblock = BattleAnimations.getIceBlock();
		
		//draw the ice-cube manually during shatter FIRST so that the cracks are drawn over it!
		
		BattleAnimObject iceCubeHack = new BattleAnimObject(Types.Bitmap, false, iceblock.bitmap);
		
		BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Screen); 
		state.size = new Point(r.width(), r.height());
		state.pos1 = new Point(r.centerX(), r.centerY());
		state.argb(255, 255, 255, 255);
		state.setBmpSrcRect(iceblock.srcRect.left, iceblock.srcRect.top, iceblock.srcRect.right, iceblock.srcRect.bottom);
		iceCubeHack.addState(state);
				
		state = new BattleAnimObjState(finalFrames * frameLength + 400, PosTypes.Screen); 
		state.size = new Point(r.width(), r.height());
		state.pos1 = new Point(r.centerX(), r.centerY());
		state.argb(255, 255, 255, 255);
		state.setBmpSrcRect(iceblock.srcRect.left, iceblock.srcRect.top, iceblock.srcRect.right, iceblock.srcRect.bottom);
		iceCubeHack.addState(state);
		
		anim.addObject(iceCubeHack);
		
		float radius = r.width() / 16.0f;
		
		//ok, now we can draw the cracks.
		
		Point centerPoint = target.getPosition(true);
		List<Point> pts = new ArrayList<Point>();
		for (int i = 0; i < shatterPoints; ++i)
		{
			Point p = new Point();
			p.x = r.left + (int)radius + Global.rand.nextInt((int)(r.width()-radius));
			p.y = r.bottom - (int)radius - Global.rand.nextInt((int)(r.height()/1.1-radius));
			
			pts.add(p);
		}
		
		
		int index = 0;
		
		List<ForkingPath> openPaths = new ArrayList<ForkingPath>();
		List<ForkingPath> lastPaths = new ArrayList<ForkingPath>();
		
		List<ForkingPath> swapBuf; 

		openPaths.add(PointMath.getForkingPath(centerPoint, pts, iterations, radius * 2.0f));

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
							
						state = new BattleAnimObjState((int)(index*frameLength), PosTypes.Screen);
						state.pos1 = new Point(p);
						state.pos2 = new Point(p);
						state.strokeWidth = 1.0f;								
						state.argb(255, 255, 255, 255);
						obj.addState(state);
						
						state = new BattleAnimObjState((int)(index*frameLength + frameLength), PosTypes.Screen);
						state.pos1 = new Point(p);
						state.pos2 = new Point(nextPt);
						state.strokeWidth = 1.0f;								
						state.argb(255, 255, 255, 255);
						obj.addState(state);
						
						state = new BattleAnimObjState((int)(finalFrames*frameLength), PosTypes.Screen);
						state.pos1 = new Point(p);
						state.pos2 = new Point(nextPt);
						state.strokeWidth = 1.0f;								
						state.argb(255, 255, 255, 255);
						obj.addState(state);
						
						state = new BattleAnimObjState((int)(finalFrames*frameLength + 400), PosTypes.Screen);
						state.pos1 = new Point(p);
						state.pos2 = new Point(nextPt);
						state.strokeWidth = 1.0f;								
						state.argb(255, 255, 255, 255);
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
//	BattleAnim getSnowImplosion(PlayerCharacter target)
//	{
//		BattleAnim anim = new BattleAnim(1000.0f);
//		
//		Bitmap icePoof = Global.bitmaps.get("particles");
//		Rect poofRect = new Rect(1,13,13,24);
//		
//		final int poofs = 50;
//		final float minVel = 4.0f;
//		final float maxVel = 72.0f;
//		final int life = 450;
//		
//		for (int i = 0; i < poofs; ++i)
//		{
//			//add a poof at this point that's fairly long-lived.
//			BattleAnimObject poofAnim = new BattleAnimObject(Types.Bitmap, false, icePoof);
//			
//			float initialOffsetX = Global.rand.nextFloat() * 32.0f -  16.0f;
//			float initialOffsetY = Global.rand.nextFloat() * 32.0f -  16.0f;
//			
//			float angle = (float)(Global.rand.nextFloat() * Math.PI);
//			float velocity = Global.rand.nextFloat() * (maxVel - minVel) + minVel;
//			float x = initialOffsetX + ((float)Math.cos(angle)) * velocity;
//			float y = initialOffsetY + ((float)Math.sin(angle)) * velocity;
//			
//			float rnd = Global.rand.nextFloat() * 360.0f;
//			BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Target);
//			state.size = new Point((int)(poofRect.width()*4.5f), (int)(poofRect.height()*4.5f));
//			state.pos1 = new Point((int)initialOffsetX,(int)initialOffsetY);
//			state.argb(196, 255, 255, 255);
//			state.rotation = rnd;
//			state.setBmpSrcRect(poofRect.left, poofRect.top, poofRect.right, poofRect.bottom);
//			poofAnim.addState(state);
//			
//			state = new BattleAnimObjState(life, PosTypes.Target);
//			state.size = new Point(poofRect.width()*3, poofRect.height()*3);
//			state.pos1 = new Point((int)x,(int)y);
//			state.argb(0, 255, 255, 255);
//			state.rotation = rnd;
//			state.setBmpSrcRect(poofRect.left, poofRect.top, poofRect.right, poofRect.bottom);
//			poofAnim.addState(state);
//			
//			anim.addObject(poofAnim);
//		}
//		
//
//		
//		return anim;
//	}
//	
	@Override
	protected void buildEvents(BattleEventBuilder builder) {

		PlayerCharacter target = BattleAction.getTarget(builder);
		
		if (target.hasStatus("frozen"))
		{
			//whelp, they're screwed.  
			
			//remove the ice block, the animation has to draw a special version.
			builder.addEventObject(new BattleAction()
			{
				PlayerCharacter target;
				BattleAction initialize(PlayerCharacter target)
				{
					this.target = target;
					return this;
				}
				public State run(BattleEventBuilder builder) {
					target.removeStatusEffect("frozen"); //tiny consolation?
					return State.Finished;
				}
			}.initialize(target));
			
			//play the shatter animation.
			
			builder.addEventObject(new bactRunAnimation(getShatterAnimation(target)));
			
			
			//Kill directly through damage, so it hits whatever events is necessary, but show no markers for effect.
			builder.addEventObject(new BattleAction()
			{
				PlayerCharacter target;
				BattleAction initialize(PlayerCharacter target)
				{
					this.target = target;
					return this;
				}
				public State run(BattleEventBuilder builder) {target.modifyHP(-9999.0f, false); return State.Finished;}
			}.initialize(target).addDependency(builder.getLast()));
			
			//finally, draw a some snow puffs as for an "implosion" sort of feel.
			builder.addEventObject(new bactRunAnimation(BattleAnimations.getSnowImplosion(target,0,180)).addDependency(builder.getLast()));
		}
		else
		{
			//aw, miss.
			builder.addEventObject(new bactAddMarker(target, "MISS"));
		}
	}
	
	

}
