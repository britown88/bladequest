package bladequest.world;

import android.graphics.Point;

public class ScreenShaker 
{
	private boolean shaking, done, shakeLeft;
	private int intensity;
	private float duration;
	private long startTime;
	
	public Point drawDelta;
	
	public ScreenShaker()
	{
		drawDelta = new Point(0,0);
		shaking = false;
		done = false;
		
		
	}
	
	public void shake(int intensity, float duration, boolean vibrate)
	{
		this.intensity = intensity;
		this.duration = duration;
		done = false;
		shaking = true;
		startTime = System.currentTimeMillis();
		
			
		
	}
	
	public void stopShaking()
	{
		
	}
	
	public boolean isDone()
	{
		return done;
	}
	
	public void update()
	{
		if(drawDelta.x == 0 && System.currentTimeMillis() - startTime >= duration * 1000.0f)
		{
			shaking = false;
			done = true;
			
		}
		
		if(shaking)
		{
			if(shakeLeft)
			{
				if(drawDelta.x-- <= -intensity)
					shakeLeft = false;
			}
			else
				if(drawDelta.x++ >= intensity)
					shakeLeft = true;
		}
		
	}


}
