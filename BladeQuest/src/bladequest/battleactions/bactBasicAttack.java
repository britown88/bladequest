package bladequest.battleactions;

import java.util.List;

import bladequest.combat.BattleEventBuilder;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class bactBasicAttack extends DelegatingAction {

	float power;
	DamageTypes type;
	float speedFactor;
	
	public bactBasicAttack(float power, DamageTypes type, float speedFactor) {
		this.power = power;
		this.type = type;
		this.speedFactor = speedFactor;
	}
	
	public void buildEvents(BattleEventBuilder builder)
	{
		PlayerCharacter attacker = builder.getSource();
		List<PlayerCharacter> targets = Global.battle.getTargetable(attacker, builder.getTargets());
		if (!targets.isEmpty())
		{
			BattleActionPatterns.BuildSwordSlash(builder, power, type, speedFactor);
			builder.addEventObject(new bactRunChildren(this).addDependency(builder.getLast()));
		}
	}
}
