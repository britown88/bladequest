package bladequest.actions;

import bladequest.actions.Action;
import bladequest.world.*;


public class actFadeControl extends Action 
{
	private int fadeSpeed, a, r, g, b;
	private boolean fadeOut, wait;
	
	public actFadeControl(int fadeSpeed, int a, int r, int g, int b, boolean fadeOut, boolean wait)
	{
		super();
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
		this.fadeOut = fadeOut;
		this.fadeSpeed = fadeSpeed;
		this.wait = wait;
		
	}
	
	@Override
	public void run()
	{
		Global.screenFader.setFadeColor(a, r, g, b);
		if(fadeSpeed >= 100)
			Global.screenFader.setFaded();
		else
			if(fadeOut)
				Global.screenFader.fadeOut(fadeSpeed);
			else
				Global.screenFader.fadeIn(fadeSpeed);				

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
