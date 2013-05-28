package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;
import bladequest.world.Global;

public class bactFlash extends BattleAction {
	float flashDuration;
	public bactFlash(float flashDuration)
	{
		this.flashDuration = flashDuration;
	}
	public State run(BattleEventBuilder builder)
	{
		Global.screenFader.setFadeColor(255, 255, 255, 255);
		Global.screenFader.flash(flashDuration);
		return State.Finished;
	}
}
