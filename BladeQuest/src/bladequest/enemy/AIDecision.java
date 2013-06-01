package bladequest.enemy;

import bladequest.world.Ability;
import bladequest.world.PlayerCharacter;

public interface AIDecision {
	void pickAbility(Ability ability);
	void pickTarget(PlayerCharacter target);
}
