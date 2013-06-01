package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;
import bladequest.world.Global;

public class bactFlash extends BattleAction {
	float flashDuration;
	int a,r,g,b;
	public bactFlash(float flashDuration, int a, int r, int g, int b)
	{
		this.flashDuration = flashDuration;
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	public State run(BattleEventBuilder builder)
	{
		Global.screenFader.setFadeColor(a,r,g,b);
		Global.screenFader.flash(flashDuration);
		return State.Finished;
	}
}
