package bladequest.battleactions;

import java.util.List;

import android.graphics.Point;
import android.graphics.Rect;
import bladequest.combat.Battle;
import bladequest.combat.BattleCalc.AccuracyType;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleAnimObjState;
import bladequest.graphics.BattleAnimObjState.PosTypes;
import bladequest.graphics.BattleAnimObject;
import bladequest.graphics.BattleAnimObject.Types;
import bladequest.graphics.BattleSprite;
import bladequest.graphics.BattleSprite.faces;
import bladequest.graphics.BitmapFrame;
import bladequest.graphics.Shadow;
import bladequest.math.PointMath;
import bladequest.world.BattleAnimations;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;
import bladequest.world.TargetTypes;


//special for roland
public class bactFrozenGrip extends DelegatingAction {

	public bactFrozenGrip() {
		// TODO Auto-generated constructor stub
	}

	BattleAnim getFrozenGripAnimation(PlayerCharacter target, PlayerCharacter throwLocation)
	{
		Point drawPoint = target.getPosition(true);
		Point targetPoint = throwLocation.getPosition(true);
		Rect r = BattleAnimations.getCharacterIceCube(target);
		
		BattleAnim anim = new BattleAnim(1000.0f);
		
		final int elevation = 20;
		final int pullDist = 200;
		
		final int pullInTime = 2500;
		final int waitTime = pullInTime + 350;
		final int hitTime = waitTime + 150;
		
		//first the player.
		
		BattleSprite playerSprite = target.getBattleSprite();
		
		BattleAnimObject baObj = new BattleAnimObject(Types.Bitmap, false, playerSprite.getBmpName());
		Rect srcRect = playerSprite.getFrameRect(faces.Damaged, 0);
		
        BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Screen);
		
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = drawPoint;
		state.size = new Point(playerSprite.getWidth(), playerSprite.getHeight());
		baObj.addState(state);		
		
		state = new BattleAnimObjState(pullInTime, PosTypes.Screen);
		
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = PointMath.add(drawPoint, new Point(-pullDist, -elevation));
		state.size = new Point(playerSprite.getWidth(), playerSprite.getHeight());
		baObj.addState(state);		
		
		state = new BattleAnimObjState(waitTime, PosTypes.Screen);
		
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = PointMath.add(drawPoint, new Point(-pullDist, -elevation));
		state.size = new Point(playerSprite.getWidth(), playerSprite.getHeight());
		baObj.addState(state);				
				
		
		state = new BattleAnimObjState(hitTime, PosTypes.Screen);
		
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = targetPoint;
		state.size = new Point(playerSprite.getWidth(), playerSprite.getHeight());
		baObj.addState(state);				
						
		
		anim.addObject(baObj);
		
		//next the icecube!
		
		BitmapFrame iceblock = BattleAnimations.getIceBlock();
		
		
		BattleAnimObject iceCubeHack = new BattleAnimObject(Types.Bitmap, false, iceblock.bitmap);
		
		state = new BattleAnimObjState(0, PosTypes.Screen); 
		state.size = new Point(r.width(), r.height());
		state.pos1 = drawPoint;
		state.argb(255, 255, 255, 255);
		state.setBmpSrcRect(iceblock.srcRect.left, iceblock.srcRect.top, iceblock.srcRect.right, iceblock.srcRect.bottom);
		iceCubeHack.addState(state);
				
		state = new BattleAnimObjState(pullInTime, PosTypes.Screen); 
		state.size = new Point(r.width(), r.height());
		state.pos1 = PointMath.add(drawPoint, new Point(-pullDist, -elevation));
		state.argb(255, 255, 255, 255);
		state.setBmpSrcRect(iceblock.srcRect.left, iceblock.srcRect.top, iceblock.srcRect.right, iceblock.srcRect.bottom);
		iceCubeHack.addState(state);
		
		state = new BattleAnimObjState(waitTime, PosTypes.Screen); 
		state.size = new Point(r.width(), r.height());
		state.pos1 = PointMath.add(drawPoint, new Point(-pullDist, -elevation));
		state.argb(255, 255, 255, 255);
		state.setBmpSrcRect(iceblock.srcRect.left, iceblock.srcRect.top, iceblock.srcRect.right, iceblock.srcRect.bottom);
		iceCubeHack.addState(state);
		
		state = new BattleAnimObjState(waitTime, PosTypes.Screen); 
		state.size = new Point(r.width(), r.height());
		state.pos1 = targetPoint;
		state.argb(255, 255, 255, 255);
		state.setBmpSrcRect(iceblock.srcRect.left, iceblock.srcRect.top, iceblock.srcRect.right, iceblock.srcRect.bottom);
		iceCubeHack.addState(state);		
		
		anim.addObject(iceCubeHack);
		
		//lastly, the shadow.
		
		Shadow shadow = target.getShadow();
		
		
		//animate shadow flying up...
		BattleAnimObject shadowObj = new BattleAnimObject(Types.Interpolatable, false, ""); 
		shadowObj.addState(new BattleAnimObjState(0, PosTypes.Screen, new Shadow(
				shadow.getWidth(), 
				shadow.getDepth(), 
				shadow.getElevAtCenter(), 
				0, 
				shadow.getXNudge(), 
				drawPoint
				)));
		
		shadowObj.addState(new BattleAnimObjState(pullInTime, PosTypes.Screen, new Shadow(
				shadow.getWidth(), 
				shadow.getDepth(), 
				shadow.getElevAtCenter(), 
				elevation, 
				shadow.getXNudge(), 
				PointMath.add(drawPoint, new Point(-pullDist, -elevation))
				)));		
		
		shadowObj.addState(new BattleAnimObjState(waitTime, PosTypes.Screen, new Shadow(
				shadow.getWidth(), 
				shadow.getDepth(), 
				shadow.getElevAtCenter(), 
				elevation, 
				shadow.getXNudge(), 
				PointMath.add(drawPoint, new Point(-pullDist, -elevation))
				)));			
		
		shadowObj.addState(new BattleAnimObjState(waitTime, PosTypes.Screen, new Shadow(
				shadow.getWidth(), 
				shadow.getDepth(), 
				shadow.getElevAtCenter(), 
				0, 
				shadow.getXNudge(), 
				targetPoint
				)));					
		
		anim.addObject(shadowObj);
		
		return anim;
	}
	
	
	BattleAnim getFrozenGripInstantKill(PlayerCharacter target)
	{

		Point drawPoint = target.getPosition(true);
		Rect r = BattleAnimations.getCharacterIceCube(target);


		BattleAnim anim = new BattleAnim(1000.0f);
		
		final int elevation = 96;
		
		final int pullInTime = 2500;
		final int waitTime = pullInTime + 350;
		final int hitTime = waitTime + 75;
		
		
		
		//first the player.
		
		BattleSprite playerSprite = target.getBattleSprite();
		
		BattleAnimObject baObj = new BattleAnimObject(Types.Bitmap, false, playerSprite.getBmpName());
		Rect srcRect = playerSprite.getFrameRect(faces.Damaged, 0);
		
        BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Screen);
		
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = drawPoint;
		state.size = new Point(playerSprite.getWidth(), playerSprite.getHeight());
		baObj.addState(state);		
		
		state = new BattleAnimObjState(pullInTime, PosTypes.Screen);
		
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = PointMath.add(drawPoint, new Point(0, -elevation));
		state.size = new Point(playerSprite.getWidth(), playerSprite.getHeight());
		baObj.addState(state);		
		
		state = new BattleAnimObjState(waitTime, PosTypes.Screen);
		
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = PointMath.add(drawPoint, new Point(0, -elevation));
		state.size = new Point(playerSprite.getWidth(), playerSprite.getHeight());
		baObj.addState(state);				
				
		
		state = new BattleAnimObjState(hitTime, PosTypes.Screen);
		
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = drawPoint;
		state.size = new Point(playerSprite.getWidth(), playerSprite.getHeight());
		baObj.addState(state);				
						
		
		anim.addObject(baObj);
		
		//next the icecube!
		
		BitmapFrame iceblock = BattleAnimations.getIceBlock();
		
		BattleAnimObject iceCubeHack = new BattleAnimObject(Types.Bitmap, false, iceblock.bitmap);
		
		state = new BattleAnimObjState(0, PosTypes.Screen); 
		state.size = new Point(r.width(), r.height());
		state.pos1 = drawPoint;
		state.argb(255, 255, 255, 255);
		state.setBmpSrcRect(iceblock.srcRect.left, iceblock.srcRect.top, iceblock.srcRect.right, iceblock.srcRect.bottom);
		iceCubeHack.addState(state);
				
		state = new BattleAnimObjState(pullInTime, PosTypes.Screen); 
		state.size = new Point(r.width(), r.height());
		state.pos1 = PointMath.add(drawPoint, new Point(0, -elevation));
		state.argb(255, 255, 255, 255);
		state.setBmpSrcRect(iceblock.srcRect.left, iceblock.srcRect.top, iceblock.srcRect.right, iceblock.srcRect.bottom);
		iceCubeHack.addState(state);
		
		state = new BattleAnimObjState(waitTime, PosTypes.Screen); 
		state.size = new Point(r.width(), r.height());
		state.pos1 = PointMath.add(drawPoint, new Point(0, -elevation));
		state.argb(255, 255, 255, 255);
		state.setBmpSrcRect(iceblock.srcRect.left, iceblock.srcRect.top, iceblock.srcRect.right, iceblock.srcRect.bottom);
		iceCubeHack.addState(state);
		
		state = new BattleAnimObjState(waitTime, PosTypes.Screen); 
		state.size = new Point(r.width(), r.height());
		state.pos1 = drawPoint;
		state.argb(255, 255, 255, 255);
		state.setBmpSrcRect(iceblock.srcRect.left, iceblock.srcRect.top, iceblock.srcRect.right, iceblock.srcRect.bottom);
		iceCubeHack.addState(state);		
		
		anim.addObject(iceCubeHack);
		
		//lastly, the shadow.
		
		Shadow shadow = target.getShadow();
		
		
		//animate shadow flying up...
		BattleAnimObject shadowObj = new BattleAnimObject(Types.Interpolatable, false, ""); 
		shadowObj.addState(new BattleAnimObjState(0, PosTypes.Screen, new Shadow(
				shadow.getWidth(), 
				shadow.getDepth(), 
				shadow.getElevAtCenter(), 
				0, 
				shadow.getXNudge(), 
				drawPoint
				)));
		
		shadowObj.addState(new BattleAnimObjState(pullInTime, PosTypes.Screen, new Shadow(
				shadow.getWidth(), 
				shadow.getDepth(), 
				shadow.getElevAtCenter(), 
				elevation, 
				shadow.getXNudge(), 
				PointMath.add(drawPoint, new Point(0, -elevation))
				)));		
		
		shadowObj.addState(new BattleAnimObjState(waitTime, PosTypes.Screen, new Shadow(
				shadow.getWidth(), 
				shadow.getDepth(), 
				shadow.getElevAtCenter(), 
				elevation, 
				shadow.getXNudge(), 
				PointMath.add(drawPoint, new Point(0, -elevation))
				)));			
		
		shadowObj.addState(new BattleAnimObjState(waitTime, PosTypes.Screen, new Shadow(
				shadow.getWidth(), 
				shadow.getDepth(), 
				shadow.getElevAtCenter(), 
				0, 
				shadow.getXNudge(), 
				drawPoint
				)));					
		
		anim.addObject(shadowObj);
		
		return anim;
	}
	
	BattleAnim getFrozenGripRecovery(PlayerCharacter target, PlayerCharacter hitTarget)
	{
		
		Point drawPoint = hitTarget.getPosition(true);
		


		BattleAnim anim = new BattleAnim(1000.0f);
		
		
		final int getUpTime = 150;
		final int standTime = getUpTime + 200;
		
		//first the player.
		
		BattleSprite playerSprite = target.getBattleSprite();
		
		BattleAnimObject baObj = new BattleAnimObject(Types.Bitmap, false, playerSprite.getBmpName());
		Rect srcRect = playerSprite.getFrameRect(faces.Dead, 0);
		
        BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Screen);
		
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = drawPoint;
		state.size = new Point(playerSprite.getWidth(), playerSprite.getHeight());
		baObj.addState(state);		
		
		state = new BattleAnimObjState(getUpTime, PosTypes.Screen);
		
		
		srcRect = playerSprite.getFrameRect(faces.Weak, 0);
		
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = PointMath.add(drawPoint, new Point(16, 0));
		state.size = new Point(playerSprite.getWidth(), playerSprite.getHeight());
		baObj.addState(state);		
		
		state = new BattleAnimObjState(standTime, PosTypes.Screen);
		
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = PointMath.add(drawPoint, new Point(16, 0));
		state.size = new Point(playerSprite.getWidth(), playerSprite.getHeight());
		baObj.addState(state);	
		
		anim.addObject(baObj);
		
		return anim;
	}
	
	
	
	@Override
	protected void buildEvents(BattleEventBuilder builder) {
		//player should be already frozen.
		//make 'em invisible
		//pick 'em up
		//move towards roland, draw shadow, increasing.
		//wait a moment
		//TINY TOSS
		//SPLOOM
	
		//pick a random target.
		List<PlayerCharacter> targets = Battle.getRandomTargets(TargetTypes.AllEnemies, builder.getSource());
		PlayerCharacter target = BattleAction.getTarget(builder);		
		
		
		if (!target.hasStatus("frozen"))
		{
			//aw, miss.
			builder.addEventObject(new bactAddMarker(target, "MISS"));
			return;
		}
		target.removeStatusEffect("frozen");
				
		builder.addEventObject(new SourcedAction(target){
			@Override
			protected void buildEvents(BattleEventBuilder builder) {
				builder.addEventObject(new bactChangeVisibility(false));
			}
			
		});
		
		if (targets.size() == 1) //insta-kill
		{
			builder.addEventObject(new bactRunAnimation(getFrozenGripInstantKill(target)));
			
			builder.addEventObject(new SourcedAction(target){
				@Override
				protected void buildEvents(BattleEventBuilder builder) {
					builder.addEventObject(new bactChangeVisibility(false));
				}
				
			}.addDependency(builder.getLast()));
			
			
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
			
			builder.addEventObject(new SourcedAction(target){
				@Override
				protected void buildEvents(BattleEventBuilder builder) {
					builder.addEventObject(new bactChangeVisibility(true));
				}
				
			}.addDependency(builder.getLast()));
			
			
			builder.addEventObject(new bactRunAnimation(BattleAnimations.getSnowImplosion(target, 0.0f, 180.0f)).addDependency(builder.getLast()));
		}
		else
		{
			PlayerCharacter hitPlayer = targets.get(Global.rand.nextInt(targets.size()));
			while (hitPlayer == target)
			{
				hitPlayer = targets.get(Global.rand.nextInt(targets.size()));
			}
			builder.addEventObject(new bactRunAnimation(getFrozenGripAnimation(target, hitPlayer)));
			
			builder.addEventObject(new SourcedAction(target){
				@Override
				protected void buildEvents(BattleEventBuilder builder) {
					builder.addEventObject(new bactChangeVisibility(false));
				}
				
			}.addDependency(builder.getLast()));
			
			builder.addEventObject(new TargetedAction(hitPlayer)
			{
				protected void buildEvents(BattleEventBuilder builder) {
					builder.addEventObject(new bactDamage(1.25f, DamageTypes.Physical, AccuracyType.NoMiss, 0.0f));
				}
			}.addDependency(builder.getLast()));
					
			
			
			BattleAction damageAction = builder.getLast();
			
			builder.addEventObject(new bactRunAnimation(BattleAnimations.getSnowImplosion(hitPlayer, 0.0f, 60.0f)).addDependency(damageAction));
			
			builder.addEventObject(new SourcedAction(target){
				
				PlayerCharacter hitPlayer;
				PlayerCharacter thrownPlayer;
				SourcedAction initialialize(PlayerCharacter thrownPlayer, PlayerCharacter hitPlayer)
				{
					this.thrownPlayer = thrownPlayer;
					this.hitPlayer = hitPlayer;
					return this;
				}
				@Override
				protected void buildEvents(BattleEventBuilder builder) {
					builder.addEventObject(new bactSpecialPosition(true));
					builder.addEventObject(new bactChangePosition(PointMath.add(hitPlayer.getPosition(), new Point(16, 0))));

					builder.addEventObject(new bactRunAnimation(getFrozenGripRecovery(thrownPlayer, hitPlayer)).addDependency(builder.getLast()));
					builder.addEventObject(new bactSetFace(faces.Ready, 0).addDependency(builder.getLast()));
					builder.addEventObject(new bactChangeVisibility(true).addDependency(builder.getLast()));
					
					builder.addEventObject(new bactWait(650));
					builder.addEventObject(new bactJumpHome(6.0f, 4).addDependency(builder.getLast()));
					builder.addEventObject(new bactSpecialPosition(false).addDependency(builder.getLast()));
				}
				
			}.initialialize(target, hitPlayer).addDependency(damageAction));
			
		}
	}

}
