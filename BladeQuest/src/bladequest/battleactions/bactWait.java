package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;
import bladequest.combat.SyncronizableAction;

public class bactWait extends SyncronizableAction {

	int waitTime;
	int endTime;
	boolean running;
	
	public bactWait(int waitTime) {
		this.waitTime = waitTime; 
	}

	@Override
	public void initialize()
	{
		running = false;
	}
	
	@Override
	public State run(BattleEventBuilder builder)
	{
		if (!running)
		{
			running = true;
			endTime = waitTime + builder.getCurrentBattleFrame();
		}
		if (builder.getCurrentBattleFrame() >= endTime) return State.Finished;
		return State.Continue;
	}
	
	@Override
	public int syncronizeTime(float factor) 
	{
		return (int)(waitTime *factor);
	}

}
