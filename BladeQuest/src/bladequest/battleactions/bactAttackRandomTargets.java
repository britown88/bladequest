package bladequest.battleactions;

import java.util.List;

import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.BattleEventObject;
import bladequest.combat.DamageMarker;
import bladequest.graphics.BattleSprite.faces;
import bladequest.world.DamageTypes;
import bladequest.world.PlayerCharacter;

public class bactAttackRandomTargets extends BattleAction {

	BattleEventBuilder builder;
	float power;
	DamageTypes type;
	int attacks;
	float speedFactor;
	
	public bactAttackRandomTargets(int frame, float power, DamageTypes type, int attacks, float speedFactor) {
		super(frame);
		this.power = power;
		this.type = type;
		this.attacks = attacks;
		this.speedFactor = speedFactor;
	}
	public void run(PlayerCharacter attacker, List<PlayerCharacter> targets, List<DamageMarker> markers)
	{
		int frameNum = getFrame();
		int frameTime = BattleEvent.frameFromActIndex(frameNum);
		builder.addEventObject(new BattleEventObject(frameTime, faces.Cast, 0, attacker));
		for (int i = 0; i < attacks; ++i)
		{
			builder.addEventObject(new BattleEventObject(frameTime, new bactAttackRandomTarget(frameTime, power, type, speedFactor, builder), attacker, targets));
			
			frameTime += BattleEvent.frameFromActIndex(6)/speedFactor;
		}
		builder.addEventObject(new BattleEventObject(frameTime, faces.Ready, 0, attacker));
	}
	public void setBuilder(BattleEventBuilder builder) 
	{
		this.builder = builder;
	}
}
