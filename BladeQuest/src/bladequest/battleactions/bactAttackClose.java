package bladequest.battleactions;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.BattleEventObject;
import bladequest.combat.DamageMarker;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleAnimObject;
import bladequest.graphics.BattleAnimObject.Types;
import bladequest.graphics.BattleSprite;
import bladequest.graphics.BattleSprite.faces;
import bladequest.math.PointMath;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class bactAttackClose  extends BattleAction {

	BattleEventBuilder builder;
	float power;
	DamageTypes type;
	int attacks;
	float speedFactor;
	
	public bactAttackClose(int frame, float power, DamageTypes type) {
		super(frame);
		this.power = power;
		this.type = type;
	}
	public BattleAnim buildJumpAnimation(PlayerCharacter attacker, Point from, Point to, float arcFactor, int animationTime)
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
	public void run(PlayerCharacter attacker, List<PlayerCharacter> targets, List<DamageMarker> markers)
	{
		int frameTime = getFrame();
		
		targets = Global.battle.getTargetable(attacker, targets);
		if (!targets.isEmpty())
		{
			List<PlayerCharacter> target = new ArrayList<PlayerCharacter>();
			target.add(targets.get(0));
			
			int attackerHeight = attacker.getBattleSprite().getHeight();
			int defenderHeight = target.get(0).getBattleSprite().getHeight(); 
			
			Point startPos = attacker.getPosition(false);
			Point offset = PointMath.subtract(attacker.getPosition(true), startPos); 
			Point pointOff = target.get(0).getPosition(false);
			pointOff.x += 8 + target.get(0).getBattleSprite().getWidth();
			pointOff.y +=  defenderHeight - attackerHeight;
			
			final float attackArcFactor = 1.0f/3.0f;  //hardcoded for now.
			final float returnArcFactor = 1.0f/6.0f;
			final int   attackSpeed = 2;
			final int   returnSpeed = 3;
			BattleAnim currentAnim;
			//Jump anim.  get arc
			
			
			builder.addEventObject(new BattleEventObject(frameTime, new bactChangeVisibility(frameTime, false), attacker, target));
			currentAnim = buildJumpAnimation(attacker, PointMath.add(offset, attacker.getPosition()), PointMath.add(offset, pointOff), attackArcFactor, attackSpeed);
			builder.addEventObject(new BattleEventObject(frameTime, currentAnim, attacker, target));
			frameTime += currentAnim.syncToAnimation(1.0f); //at end of this animation...
			builder.addEventObject(new BattleEventObject(frameTime, new bactSpecialPosition(frameTime, true), attacker, target));
			builder.addEventObject(new BattleEventObject(frameTime, new bactChangePosition(frameTime, pointOff), attacker, target));
			builder.addEventObject(new BattleEventObject(frameTime, new bactChangeVisibility(frameTime, true), attacker, target));
			//now visible in this new place, start attacking!
			//attack at double speed.
			frameTime += BattleActionPatterns.BuildSwordSlash(builder, attacker, target, power, type, frameTime, 0.5f);
			
			//jump back to starting location.
			builder.addEventObject(new BattleEventObject(frameTime, new bactChangeVisibility(frameTime, false), attacker, target));
			currentAnim = buildJumpAnimation(attacker, PointMath.add(offset, pointOff), PointMath.add(offset, startPos), returnArcFactor, returnSpeed);
			builder.addEventObject(new BattleEventObject(frameTime, currentAnim, attacker, target));
			frameTime += currentAnim.syncToAnimation(1.0f);
			builder.addEventObject(new BattleEventObject(frameTime, new bactChangePosition(frameTime, startPos), attacker, target));
			builder.addEventObject(new BattleEventObject(frameTime, new bactSpecialPosition(frameTime, false), attacker, target));
			builder.addEventObject(new BattleEventObject(frameTime, new bactChangeVisibility(frameTime, true), attacker, target));
			
			//add in some chillaxing time.
			builder.addEventObject(new BattleEventObject(frameTime + BattleEvent.frameFromActIndex(3)));	
		}
	}
	public void setBuilder(BattleEventBuilder builder) 
	{
		this.builder = builder;
	}
}
