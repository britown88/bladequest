package bladequest.actions;

import bladequest.world.GameObject;
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
		if(target.equals("party"))
			Global.party.setFloating(startFloating, period, intensity);
		else
		{
			for(GameObject go : Global.map.Objects())
				if(go.Name().equals(target))
				{
					go.setFloating(startFloating, period, intensity);
					return;
				}				
		}	
	}
	
	@Override
	public boolean isDone()
	{
		return true;
	}
	
	

}
