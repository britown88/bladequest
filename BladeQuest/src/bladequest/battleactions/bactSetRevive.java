package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;

public class bactSetRevive extends BattleAction {

	public bactSetRevive() {
	}
	public State run(BattleEventBuilder builder)
	{
		BattleAction.getTarget(builder).revive();
		return State.Finished;
	}
}
