package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;

public class bactSpecialPosition extends BattleAction {

	boolean specialPosition;
	public bactSpecialPosition(boolean specialPosition) {
		this.specialPosition = specialPosition;
	}
	@Override
	public State run(BattleEventBuilder builder)
	{
		builder.getSource().setPositionSpecial(specialPosition);
		return State.Finished;
	}
}
