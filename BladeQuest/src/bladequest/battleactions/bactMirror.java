package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;
import bladequest.world.PlayerCharacter;

public class bactMirror extends BattleAction {
	
	boolean mirror;
	
	public bactMirror(boolean mirror) {
		this.mirror = mirror;
	}
	
	@Override
	public State run(BattleEventBuilder builder)
	{
		PlayerCharacter toUpdate = builder.getSource();
		toUpdate.setMirrored(mirror);
		return State.Finished;
	}
	
}
