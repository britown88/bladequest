package bladequest.actions;

import bladequest.graphics.ScreenFilter;
import bladequest.world.Global;

public class actRemoveFilter extends Action
{
	public actRemoveFilter()
	{
	}
	
	@Override
	public void run()
	{
		ScreenFilter.instance().popFilter();
		
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
