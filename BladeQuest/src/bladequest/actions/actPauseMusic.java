package bladequest.actions;

import bladequest.actions.Action;
import bladequest.world.*;


public class actPauseMusic extends Action 
{
	
	public actPauseMusic()
	{
		super();
	}
	
	@Override
	public void run()
	{
		Global.musicBox.pause(true);
	}
	
	@Override
	public boolean isDone()
	{
		return true;
	}

}
