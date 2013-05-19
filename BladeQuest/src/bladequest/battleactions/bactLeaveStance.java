package bladequest.battleactions;

import java.util.List;

import bladequest.combat.DamageMarker;
import bladequest.combatactions.Stance;
import bladequest.world.PlayerCharacter;

public class bactLeaveStance  extends BattleAction {
	public bactLeaveStance(int frame) {
		super(frame);
	}
	@Override
	public void run(PlayerCharacter attacker, List<PlayerCharacter> targets, List<DamageMarker> markers)
	{
		Stance.leaveStance(attacker);
	}
}
