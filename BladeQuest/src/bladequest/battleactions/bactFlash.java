package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;
import bladequest.world.Global;

public class bactFlash extends BattleAction {
	int flashSpeed;
	public bactFlash(int flashSpeed)
	{
		this.flashSpeed = flashSpeed;
	}
	public State run(BattleEventBuilder builder)
	{
		Global.screenFader.setFadeColor(255, 255, 255, 255);
		Global.screenFader.flash(flashSpeed);
		return State.Finished;
	}
}
