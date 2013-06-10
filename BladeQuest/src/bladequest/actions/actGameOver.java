package bladequest.actions;

import bladequest.world.Global;

public class actGameOver extends Action
{
	public actGameOver(){}
	
	@Override
	public void run()
	{
		Global.party.setAllowMovement(true);
		Global.clearPan();				
		Global.executeGameOver();
	}
	
	@Override
	public boolean isDone()
	{
		return true;
	}

}
