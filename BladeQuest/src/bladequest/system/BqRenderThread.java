package bladequest.system;


import bladequest.world.Global;

import android.graphics.*;
import android.view.SurfaceHolder;

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

	public BqRenderThread(SurfaceHolder surfaceHolder) {
		super();
		this.surfaceHolder = surfaceHolder;
	}
	

	@Override
	public void run() 
	{
		Canvas canvas;
		
		long startTime;
		long frameTime;
		int sleepTime;

				
		while (running) 
		{
			canvas = null;

			try 
			{				
				canvas = this.surfaceHolder.lockCanvas();

				//canvas.setBitmap(bmp);
				synchronized (surfaceHolder) 
				{					
			    	startTime = System.currentTimeMillis();
			    	Global.renderer.render(canvas);  //locked internally now.

					frameTime = System.currentTimeMillis() - startTime;
					sleepTime = (int)(Global.FRAME_PERIOD - frameTime);					
					if(sleepTime > 0)
						try{sleep(sleepTime);}
							catch (InterruptedException e) {}							
							
				}
			} 
			finally 
			{
				if (canvas != null)
					surfaceHolder.unlockCanvasAndPost(canvas);
			}
				

		}
	}
	
}
