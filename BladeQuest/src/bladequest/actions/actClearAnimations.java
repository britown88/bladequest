package bladequest.actions;

import bladequest.world.Global;

public class actClearAnimations extends Action
{
	public actClearAnimations(){}
	
	@Override
	public void run()
	{
		Global.clearAnimations();
	}
	
	@Override
	public boolean isDone()
	{
		return true;
	}
}
