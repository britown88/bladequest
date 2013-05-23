package bladequest.battleactions;

import java.util.List;

import android.graphics.Point;
import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleAnimObject;
import bladequest.graphics.BattleAnimObject.Types;
import bladequest.graphics.BattleSprite;
import bladequest.graphics.BattleSprite.faces;
import bladequest.math.PointMath;
import bladequest.world.DamageTypes;
import bladequest.world.PlayerCharacter;

public class bactAttackClose  extends DelegatingAction {

	float power;
	DamageTypes type;
	
	public bactAttackClose(float power, DamageTypes type) {
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
	@Override
	protected void buildEvents(BattleEventBuilder builder) 
	{
		PlayerCharacter attacker = builder.getSource();
		PlayerCharacter target = getTarget(builder);
		
		if (target != null)
		{
			int attackerHeight = attacker.getBattleSprite().getHeight();
			int defenderHeight = target.getBattleSprite().getHeight(); 
			
			Point startPos = attacker.getPosition(false);
			Point offset = PointMath.subtract(attacker.getPosition(true), startPos); 
			Point pointOff = target.getPosition(false);
			pointOff.x += 8 + target.getBattleSprite().getWidth();
			pointOff.y +=  defenderHeight - attackerHeight;
			
			final float attackArcFactor = 1.0f/3.0f;  //hardcoded for now.
			final float returnArcFactor = 1.0f/6.0f;
			final int   attackSpeed = 2;
			final int   returnSpeed = 3;
			BattleAnim currentAnim;
			//Jump anim.  get arc
			
			
			builder.addEventObject(new bactChangeVisibility(false));
			currentAnim = buildJumpAnimation(attacker, PointMath.add(offset, attacker.getPosition()), PointMath.add(offset, pointOff), attackArcFactor, attackSpeed);
			builder.addEventObject(new bactRunAnimation(currentAnim).addDependency(builder.getLast()));
			builder.addEventObject(new bactSpecialPosition(true).addDependency(builder.getLast()));
			builder.addEventObject(new bactChangePosition(pointOff).addDependency(builder.getLast()));
			builder.addEventObject(new bactChangeVisibility(true).addDependency(builder.getLast()));
			//now visible in this new place, start attacking!
			//attack at double speed.
			BattleAction slashEnd = BattleActionPatterns.BuildSwordSlash(builder, power, type, 0.5f, builder.getLast());
			
			//jump back to starting location.
			builder.addEventObject(new bactChangeVisibility(false).addDependency(slashEnd));
			currentAnim = buildJumpAnimation(attacker, PointMath.add(offset, pointOff), PointMath.add(offset, startPos), returnArcFactor, returnSpeed);
			builder.addEventObject(new bactRunAnimation(currentAnim).addDependency(builder.getLast()));
			builder.addEventObject(new bactChangePosition(startPos).addDependency(builder.getLast()));
			builder.addEventObject(new bactSpecialPosition(false).addDependency(builder.getLast()));
			builder.addEventObject(new bactChangeVisibility(true).addDependency(builder.getLast()));
			
			//add in some chillaxing time.
			builder.addEventObject(new bactWait(BattleEvent.frameFromActIndex(3)).addDependency(builder.getLast()));	
		}
	}	
}
