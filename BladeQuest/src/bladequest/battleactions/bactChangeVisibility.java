package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;

public class bactChangeVisibility extends BattleAction {

	boolean visiblity;
	public bactChangeVisibility(boolean isVisible) {
		visiblity = isVisible;
	}
	@Override
	public State run(BattleEventBuilder builder)
	{
		builder.getSource().setVisible(visiblity);
		return State.Finished;
	}
}
