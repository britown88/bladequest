package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;

public class bactSpecialMirrored extends BattleAction {

	boolean specialMirrored;
	public bactSpecialMirrored (boolean specialMirrored) {
		this.specialMirrored = specialMirrored;
	}
	@Override
	public State run(BattleEventBuilder builder)
	{
		builder.getSource().setMirroredSpecial(specialMirrored);
		return State.Finished;
	}
}