package bladequest.actions;

import bladequest.actions.Action;
import bladequest.world.*;


public class actShake extends Action 
{
	private float duration;
	private int intensity;
	private boolean wait;
	
	public actShake(float duration, int intensity, boolean wait)
	{
		super();
		this.duration = duration;
		this.intensity = intensity;
		this.wait = wait;
	}
	
	@Override
	public void run()
	{
		Global.screenShaker.shake(intensity, duration, false);
	}
	
	@Override
	public boolean isDone()
	{
		return wait ? Global.screenShaker.isDone() : true;
	}

}
