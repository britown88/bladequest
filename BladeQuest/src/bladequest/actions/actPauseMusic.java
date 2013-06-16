package bladequest.actions;

import bladequest.sound.BladeSong;
import bladequest.world.Global;


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
		Global.bladeSong.fadeOut(fadeTime);
	}
	
	@Override
	public boolean isDone()
	{
		return true;
	}

}
