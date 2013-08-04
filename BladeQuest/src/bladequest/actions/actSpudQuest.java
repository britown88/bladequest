package bladequest.actions;

import bladequest.world.Global;

public class actSpudQuest extends Action 
{
	float fadeTime;
	public actSpudQuest()
	{
		super();
	}
	
	@Override
	public void run()
	{
		Global.startSpudQuest();
	}
	
	@Override
	public boolean isDone()
	{
		return true;
	}

}