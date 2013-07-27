package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;
import bladequest.world.PlayerCharacter.Hand;

public class bactTwoHandedAttack extends DelegatingAction {

	float power, speed;
	public bactTwoHandedAttack(float power, float speed) {
		this.power = power;
		this.speed = speed;
	}
	@Override
	protected void buildEvents(BattleEventBuilder builder) {
		builder.addEventObject(BattleActionPatterns.BuildHandedAttack(Hand.MainHand, power, speed));
		
		if (builder.getSource().hand2WeaponEquipped())
		{
			builder.addEventObject(BattleActionPatterns.BuildHandedAttack(Hand.OffHand, power, speed).addDependency(builder.getLast()));
		}
	}

}
