package bladequest.actions;

import bladequest.world.Global;

public class actGameOver extends Action
{
	public actGameOver(){}
	
	@Override
	public void run()
	{
		Global.party.setAllowMovement(true);
		Global.setPanned(0, 0);				
		Global.executeGameOver();
	}
	
	@Override
	public boolean isDone()
	{
		return true;
	}

}
