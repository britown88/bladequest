package bladequest.actions;

import android.graphics.Point;
import bladequest.actions.Action;
import bladequest.world.*;


public class actPanControl extends Action 
{
	private Point dest;
	private int speed;
	private boolean wait;
	
	public actPanControl(int x, int y, int speed, boolean wait)
	{
		super();
		dest = new Point(x, y);
		this.speed = speed;
		this.wait = wait;	
	}
	
	@Override
	public void run()
	{
		if(speed > 0)
			Global.pan(dest.x, dest.y, speed);
		else
			Global.setPanned(dest.x, dest.y);
	}
	
	@Override
	public boolean isDone()
	{
		return wait ? Global.isPanned() : true;
	}

}
