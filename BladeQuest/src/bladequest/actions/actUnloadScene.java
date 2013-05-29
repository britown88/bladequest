package bladequest.actions;

import bladequest.world.Global;

public class actUnloadScene extends Action
{
	public actUnloadScene(){}
	
	@Override
	public void run()
	{
		if(Global.showScene != null && Global.showScene.isLoaded())
			Global.showScene.unload();
	}
	
	@Override
	public boolean isDone()
	{
		return true;
	}

}
