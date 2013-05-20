package bladequest.battleactions;

import java.util.List;

import bladequest.combat.DamageMarker;
import bladequest.world.PlayerCharacter;

public class bactSpecialPosition extends BattleAction {

	boolean specialPosition;
	public bactSpecialPosition(int frame, boolean specialPosition) {
		super(frame);
		this.specialPosition = specialPosition;
	}
	@Override
	public void run(PlayerCharacter attacker, List<PlayerCharacter> target, List<DamageMarker> markers)
	{
		attacker.setPositionSpecial(specialPosition);
	}
}
