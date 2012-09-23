package bladequest.actions;

import bladequest.actions.Action;
import bladequest.world.*;


public class actShowScene extends Action 
{
	private String sceneName;
	
	public actShowScene(String sceneName)
	{
		super();
		this.sceneName = sceneName;	
	}
	
	@Override
	public void run()
	{
		Global.ShowScene(sceneName);
	}
	
	@Override
	public boolean isDone()
	{
		return true;
	}

}
