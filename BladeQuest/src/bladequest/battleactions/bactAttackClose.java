package bladequest.battleactions;

import android.graphics.Point;
import bladequest.combat.BattleCalc;
import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.BattleAnim;
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
			
			final float attackArcFactor = 1.0f/6.0f;  //hardcoded for now.
			final float returnArcFactor = 1.0f/2.0f;
			final int   attackSpeed = 2;
			final int   returnSpeed = 4;
			BattleAnim currentAnim;
			//Jump anim.  get arc
			
			
			builder.addEventObject(new bactChangeVisibility(false));
			currentAnim = PointMath.buildJumpAnimation(attacker, PointMath.add(offset, attacker.getPosition()), PointMath.add(offset, pointOff), attackArcFactor, attackSpeed);
			builder.addEventObject(new bactRunAnimation(currentAnim));
			builder.addEventObject(new bactSpecialPosition(true).addDependency(builder.getLast()));
			builder.addEventObject(new bactChangePosition(pointOff).addDependency(builder.getLast()));
			builder.addEventObject(new bactChangeVisibility(true).addDependency(builder.getLast()));
			//now visible in this new place, start attacking!
			//attack at double speed.
			BattleAction slashEnd = BattleActionPatterns.BuildSwordSlashWithAccuracy(builder, power, type, 0.5f, builder.getLast(), BattleCalc.AccuracyType.NoMiss, 0.0f);
			
			//jump back to starting location.
			currentAnim = PointMath.buildJumpAnimation(attacker, PointMath.add(offset, pointOff), PointMath.add(offset, startPos), returnArcFactor, returnSpeed);
			
			builder.addEventObject(new bactChangeVisibility(false).addDependency(slashEnd));
			
			BattleAction jumpEnd = new bactRunAnimation(currentAnim).addDependency(slashEnd);
			builder.addEventObject(jumpEnd);
			builder.addEventObject(new bactChangePosition(startPos).addDependency(jumpEnd));
			builder.addEventObject(new bactSpecialPosition(false).addDependency(jumpEnd));
			builder.addEventObject(new bactChangeVisibility(true).addDependency(jumpEnd));
			
			//add in some chillaxing time.
			builder.addEventObject(new bactWait(BattleEvent.frameFromActIndex(3)).addDependency(builder.getLast()));	
		}
	}	
}
