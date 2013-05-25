package bladequest.battleactions;

import java.util.ArrayList;
import java.util.List;

import bladequest.combat.BattleEventBuilder;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class bactAttackRandomTarget extends DelegatingAction {

	float power;
	DamageTypes type;
	float speedFactor;
	List<PlayerCharacter> target;
	
	public bactAttackRandomTarget(float power, DamageTypes type, float speedFactor) {
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
			target = new ArrayList<PlayerCharacter>();
			target.add(targets.get(Global.rand.nextInt(targets.size())));
			
			BattleActionPatterns.BuildSwordSlash(builder, power, type, speedFactor);
			builder.addEventObject(new bactRunChildren(this).addDependency(builder.getLast()));
		}
	}
	public BattleEventBuilder getAdaptedBuilder(BattleEventBuilder builder) 
	{
		return changeTargets(super.getAdaptedBuilder(builder), target);
	}
}
