package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;
import bladequest.combat.DamageMarker;
import bladequest.world.PlayerCharacter;

public class bactAddMarker extends BattleAction {

	PlayerCharacter target;
	String message;
	public bactAddMarker(PlayerCharacter target, String message) {
		this.target = target;
		this.message = message;
	}

	public State run(BattleEventBuilder builder)
	{
		builder.addMarker(new DamageMarker(message, target));	
		return State.Finished;
	}
}
