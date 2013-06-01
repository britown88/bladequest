package bladequest.actions;

import bladequest.world.Global;

public class actFlash extends Action
{
	private int a, r, g, b;
	private float flashLength;
	private boolean wait;
	
	public actFlash(int a, int r, int g, int b, float flashLength, boolean wait)
	{
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
		this.flashLength = flashLength;
		this.wait = wait;
	}
	
	@Override
	public void run()
	{
		Global.screenFader.setFadeColor(a, r, g, b);
		Global.screenFader.flash(flashLength);
	}
	
	@Override
	public boolean isDone()
	{
		return !wait || Global.screenFader.isDone();
	}
	

}
