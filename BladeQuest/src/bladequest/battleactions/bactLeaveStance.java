package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;
import bladequest.combatactions.Stance;

public class bactLeaveStance  extends BattleAction {
	@Override
	public State run(BattleEventBuilder builder)
	{
		Stance.leaveStance(builder.getSource());
		return State.Finished;
	}
}
