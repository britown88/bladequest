package bladequest.battleactions;

import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.BattleSprite.faces;
import bladequest.world.DamageTypes;

public class bactAttackRandomTargets extends DelegatingAction {

	BattleEventBuilder builder;
	float power;
	DamageTypes type;
	int attacks;
	float speedFactor;
	
	public bactAttackRandomTargets(float power, DamageTypes type, int attacks, float speedFactor) {
		this.power = power;
		this.type = type;
		this.attacks = attacks;
		this.speedFactor = speedFactor;
	}

	@Override
	protected void buildEvents(BattleEventBuilder builder) {
		
		builder.addEventObject(new bactSetFace(faces.Cast, 0));
		builder.addEventObject(new bactWait(BattleEvent.frameFromActIndex(1)).addDependency(builder.getLast()));
		for (int i = 0; i < attacks; ++i)
		{
			builder.addEventObject(new bactAttackRandomTarget(power, type, speedFactor).addDependency(builder.getLast()));
		}
		builder.addEventObject(new bactSetFace(faces.Ready, 0).addDependency(builder.getLast()));		
	}

}
