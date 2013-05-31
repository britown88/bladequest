package bladequest.actions;

import bladequest.world.Global;

public class actElevation extends Action
{
	private String target;
	private int elevation;
	private float time;
	private boolean wait;
	private long startTime;
	
	public actElevation(String target, int elevation, float time, boolean wait)
	{
		this.target = target;
		this.elevation = elevation;
		this.time = time;
		this.wait = wait;
	}
	
	@Override
	public void run()
	{
		Global.party.moveElevation(elevation, time);
		startTime = System.currentTimeMillis();
	}
	
	@Override
	public boolean isDone()
	{
		return !wait || System.currentTimeMillis() - startTime > time * 1000;
	}
	

}
