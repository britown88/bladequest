package bladequest.actions;

import bladequest.world.Global;

public class actFloat extends Action
{
	boolean startFloating;
	int period, intensity;
	String target;
	
	public actFloat(String target, boolean startFloating, int period, int intensity)
	{
		this.startFloating = startFloating;
		this.target = target;
		this.period = period;
		this.intensity = intensity;
	}
	
	@Override
	public void run()
	{
		Global.party.setFloating(startFloating, period, intensity);
	}
	
	@Override
	public boolean isDone()
	{
		return true;
	}
	
	

}
