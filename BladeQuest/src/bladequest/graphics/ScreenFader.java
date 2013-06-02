package bladequest.graphics;

import bladequest.world.Global;

public class ScreenFader 
{
	private int red, green, blue;
	private int alpha, finalAlpha;
	private float fadeTime;
	private long startTime;
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
		finalAlpha = Math.min(255, a);		
	}
	
	public void fadeIn(float fadeTime)
	{
		alpha = finalAlpha;
		fading = true;
		this.fadeTime = fadeTime;
		fadeIn = true;
		done = false;
		startTime = System.currentTimeMillis();
	}
	
	public void fadeOut(float fadeTime)
	{
		alpha = 0;
		fading = true;
		this.fadeTime = fadeTime;
		fadeIn = false;
		done = false;
		startTime = System.currentTimeMillis();
	}
	
	public void flash(float flashLength)
	{
		flashing = true;
		fadeOut(flashLength/2);
	}
	
	public boolean isDone() { return done; }
	public boolean isFadedOut() { return done && !fadeIn; }
	public boolean isFadedIn() { return done && fadeIn; }
	public boolean fadingOut() { return fading && !fadeIn;}
	
	public void setFaded()
	{
		alpha = finalAlpha;
		fading = false;
		done = true;
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
			float delta = (System.currentTimeMillis() - startTime)/(fadeTime*1000.0f);
						
			if(delta >= 1.0f)
			{
				if(flashing)
				{
					if(fadeIn)
					{
						flashing = false;
						fading = false;
						done = true;		
						alpha = 0;
					}
					else
					{
						fadeIn(fadeTime);
						delta = 0.0f;
					}
				}
				else
				{
					fading = false;
					done = true;	
					alpha = fadeIn ? 0 : finalAlpha;
				}
			}
			
			if(fading)
			{
				if(fadeIn)
					alpha = (int)(finalAlpha*(1.0f-delta));
				else
					alpha = (int)(finalAlpha*delta);
			}
			
			
		}
	}
	
	public void render()
	{
		Global.renderer.drawColor(alpha, red, green, blue);
		
	}

}
