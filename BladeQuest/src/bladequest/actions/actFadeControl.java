package bladequest.actions;

import bladequest.actions.Action;
import bladequest.world.*;


public class actFadeControl extends Action 
{
	private int a, r, g, b;
	float fadeTime;
	private boolean fadeOut, wait;
	
	public actFadeControl(float fadeTime, int a, int r, int g, int b, boolean fadeOut, boolean wait)
	{
		super();
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
		this.fadeOut = fadeOut;
		this.fadeTime = fadeTime;
		this.wait = wait;
		
	}
	
	@Override
	public void run()
	{
		Global.screenFader.setFadeColor(a, r, g, b);
		if(fadeTime == -1)
			Global.screenFader.setFaded();
		else
			if(fadeOut)
				Global.screenFader.fadeOut(fadeTime);
			else
				Global.screenFader.fadeIn(fadeTime);				

	}
	
	@Override
	public boolean isDone()
	{
		if(wait)
			return Global.screenFader.isDone();
		else
			return true;
	}

}
