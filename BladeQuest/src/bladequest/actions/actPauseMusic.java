package bladequest.actions;

import bladequest.actions.Action;
import bladequest.world.*;


public class actPauseMusic extends Action 
{
	float fadeTime;
	public actPauseMusic(float fadeTime)
	{
		super();
		this.fadeTime = fadeTime;
	}
	
	@Override
	public void run()
	{
		Global.musicBox.pause(fadeTime);
	}
	
	@Override
	public boolean isDone()
	{
		return true;
	}

}
