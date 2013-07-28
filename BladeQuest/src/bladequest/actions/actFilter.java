package bladequest.actions;

import bladequest.world.Global;

public class actFilter extends Action
{
	float[] filter;
	
	public actFilter(float[] filter)
	{
		this.filter = filter;
	}
	
	@Override
	public void run()
	{
		Global.screenFilter.pushFilter(filter);
		
		if (Global.map != null && Global.map.isLoaded())
		{
			Global.map.invalidateTiles();
		}		
	}
	
	@Override
	public boolean isDone()
	{
		return true;
	}

}
