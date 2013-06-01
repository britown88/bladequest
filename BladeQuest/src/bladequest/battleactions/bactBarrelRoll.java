package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.BattleAnim;

public class bactBarrelRoll extends BattleAction{

	long time;
	long startTime;
	
	public bactBarrelRoll(long ms) {

		time = ms;
	}

	
	public void initialize() 
	{
		startTime = System.currentTimeMillis();
	}
	public void clearState() {}
	
	
	public State run(BattleEventBuilder builder)
	{
		long currentTime = System.currentTimeMillis() - startTime;
		if (currentTime > time)
		{
			builder.getSource().getBattleSprite().setRotation(0.0f);
			return State.Finished;
		}
		else
		{
			float t = currentTime / (float)time;
			builder.getSource().getBattleSprite().setRotation(BattleAnim.linearInterpolation(0.0f, 360.0f, t));
		}
		
		return State.Continue;
	}	
}
