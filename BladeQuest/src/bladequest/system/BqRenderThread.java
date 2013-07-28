package bladequest.system;


import android.graphics.Canvas;
import android.view.SurfaceHolder;
import bladequest.world.Global;

public class BqRenderThread extends Thread 
{	
	

	// Surface holder that can access the physical surface
	private SurfaceHolder surfaceHolder;
	// The actual view that handles inputs
	// and draws to the surface

	// flag to hold game state 
	private boolean running;
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	public void setHolder(SurfaceHolder surfaceHolder)
	{
		if (this.surfaceHolder != null)
		{
			synchronized (this.surfaceHolder) 
			{			
				this.surfaceHolder = surfaceHolder;
			}	
		}
		else
		{
			this.surfaceHolder = surfaceHolder;
		}
	}

	public BqRenderThread(SurfaceHolder surfaceHolder) {
		super();
		this.surfaceHolder = surfaceHolder;
		running = false;
	}
	

	@Override
	public void run() 
	{
		Canvas canvas;
		
		long startTime;
		long frameTime;
		int sleepTime;

				
		for(;;) 
		{
			startTime = System.currentTimeMillis();
			if (running && surfaceHolder != null)
			{
				canvas = null;
				synchronized (surfaceHolder) 
				{									
					try 
					{		
						canvas = this.surfaceHolder.lockCanvas();
		
						if (canvas != null)
						{
							Global.renderer.render(canvas);  //locked internally now.								
						}
	
					} 
					finally 
					{
						if (canvas != null)
						{
							surfaceHolder.unlockCanvasAndPost(canvas);
						}
					}
				}
			}


			frameTime = System.currentTimeMillis() - startTime;
			sleepTime = (int)(Global.FRAME_PERIOD - frameTime);					
			if(sleepTime > 0)
				try{sleep(sleepTime);}
					catch (InterruptedException e) {}

		}
	}
	
}
