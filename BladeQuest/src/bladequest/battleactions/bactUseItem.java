package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;

public class bactUseItem extends BattleAction
{
	@Override
	public State run(BattleEventBuilder builder)
	{
		//resets item count and removes from inventory
		builder.getSource().useItem();
		return State.Finished;
	}
}
