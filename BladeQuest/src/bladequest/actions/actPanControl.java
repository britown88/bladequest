package bladequest.actions;

import android.graphics.Point;
import bladequest.actions.Action;
import bladequest.world.*;


public class actPanControl extends Action 
{
	private Point dest;
	private float time;
	private boolean wait;
	private long startTime;
	
	public actPanControl(int x, int y, float time, boolean wait)
	{
		super();
		dest = new Point(x, y);
		this.time = time;
		this.wait = wait;	
	}
	
	@Override
	public void run()
	{
		Global.pan(dest.x, dest.y, time);
		startTime = System.currentTimeMillis();

	}
	
	@Override
	public boolean isDone()
	{
		return !wait || System.currentTimeMillis() - startTime >= time*1000.0f;
	}

}
