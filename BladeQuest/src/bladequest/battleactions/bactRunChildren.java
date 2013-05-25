package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;

public class bactRunChildren extends BattleAction {
	BattleAction parentAction;
	public bactRunChildren(BattleAction parent) {
		parentAction = parent;
	}
	
	@Override
	public State run(BattleEventBuilder builder)
	{
		parentAction.removeReferences();
		return State.Finished;
	}
}
