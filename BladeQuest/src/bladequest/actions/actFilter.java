package bladequest.actions;

import bladequest.graphics.ScreenFilter;

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
		ScreenFilter.instance().pushFilter(filter);
	}
	
	@Override
	public boolean isDone()
	{
		return true;
	}

}
