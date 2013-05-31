package bladequest.actions;

import bladequest.sound.BladeSong;


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
		BladeSong.instance().fadeOut(fadeTime);
	}
	
	@Override
	public boolean isDone()
	{
		return true;
	}

}
