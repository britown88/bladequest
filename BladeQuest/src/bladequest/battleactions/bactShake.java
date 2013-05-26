package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;
import bladequest.world.Global;

public class bactShake extends BattleAction {

	int duration;
	int intensity;
	boolean vibrate;
	public bactShake(int intensity, int duration, boolean vibrate)
	{
		this.intensity = intensity;
		this.duration = duration;
		this.vibrate = vibrate;
	}
	public State run(BattleEventBuilder builder)
	{
		Global.screenShaker.shake(intensity, duration, vibrate);
		return State.Finished;
	}
}
