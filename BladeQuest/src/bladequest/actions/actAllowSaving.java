package bladequest.actions;

import bladequest.world.Global;

public class actAllowSaving extends Action
{
	public actAllowSaving(){}
	
	@Override
	public void run()
	{
		Global.party.allowSaving();
	}
	
	@Override
	public boolean isDone()
	{
		return true;
	}

}
