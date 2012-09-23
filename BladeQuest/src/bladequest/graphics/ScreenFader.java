package bladequest.graphics;

import bladequest.world.Global;
import android.graphics.*;

public class ScreenFader 
{
	private int red, green, blue;
	private int alpha, finalAlpha, fadeSpeed;
	private boolean fading, fadeIn, done, flashing;
	
	public ScreenFader()
	{
		fading = false;
		fadeIn = true;
		finalAlpha = 0;
		done = true;
	}
	
	public void setFadeColor(int a, int r, int g, int b)
	{
		red = r;
		green = g;
		blue = b;
		finalAlpha = a;		
	}
	
	public void fadeIn(int fadeSpeed)
	{
		alpha = finalAlpha;
		fading = true;
		this.fadeSpeed = fadeSpeed;
		fadeIn = true;
		done = false;
	}
	
	public void fadeOut(int fadeSpeed)
	{
		alpha = 0;
		fading = true;
		this.fadeSpeed = fadeSpeed;
		fadeIn = false;
		done = false;
	}
	
	public void flash(int speed)
	{
		flashing = true;
		fadeOut(speed);
	}
	
	public boolean isDone() { return done; }
	public boolean fadingOut() { return fading && !fadeIn;}
	
	public void setFaded()
	{
		alpha = finalAlpha;
		fading = false;
	}
	
	public void clear()
	{
		flashing = false;
		alpha = 0;
		fading = false;
		done = true;
	}
	
	public void update()
	{
		if(fading)
		{
			int fadeAmount = (int)(255 * (fadeSpeed/100.0f));
			if(fadeAmount == 0)
				fadeAmount = 1;

			if(fadeIn)
				alpha = Math.max(alpha - fadeAmount, 0);				
			else
				alpha = Math.min(alpha + fadeAmount, Math.min(finalAlpha, 255));
			
			if(alpha == 0 || alpha == Math.min(finalAlpha, 255))
			{
				if(flashing)
				{
					if(fadeIn)
					{
						flashing = false;
						fading = false;
						done = true;						
					}
					else
					{
						fadeIn(fadeSpeed);
					}
					
				}
				else
				{
					fading = false;
					done = true;					
				}
				
				
			}
										

		}
	}
	
	public void render()
	{
		Global.renderer.drawColor(alpha, red, green, blue);
		
	}

}
