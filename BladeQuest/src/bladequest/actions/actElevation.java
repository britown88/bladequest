package bladequest.actions;

import bladequest.world.GameObject;
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
		startTime = System.currentTimeMillis();
		if(target.equals("party"))
			Global.party.moveElevation(elevation, time);
		else
		{
			for(GameObject go : Global.map.Objects())
				if(go.Name().equals(target))
				{
					go.moveElevation(elevation, time);
					return;
				}				
		}		
	}
	
	@Override
	public boolean isDone()
	{
		return !wait || System.currentTimeMillis() - startTime > time * 1000;
	}
	

}
