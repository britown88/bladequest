package bladequest.actions;

import bladequest.graphics.ScreenFilter;

public class actRemoveFilter extends Action
{
	public actRemoveFilter()
	{
	}
	
	@Override
	public void run()
	{
		ScreenFilter.instance().popFilter();
	}
	
	@Override
	public boolean isDone()
	{
		return true;
	}

}
