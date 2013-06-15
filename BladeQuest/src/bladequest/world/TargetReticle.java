package bladequest.world;

import android.graphics.Point;
import bladequest.graphics.Sprite;

public class TargetReticle 
{
	private boolean show;
	private Sprite spr;
	
	private static final float animFrameDuration = 0.15f;
	private long startTime, animStartTime;
	private int imageIndex;
	
	
	public TargetReticle()
	{
		show = false;		
		
		spr = new Sprite(Global.sprites.get("target"));
	}
	
	public void show()
	{
		startTime = animStartTime = System.currentTimeMillis();
		imageIndex = 0;
		show = true;
	}
	
	public void update()
	{
		if(show)
		{
			long currTime = System.currentTimeMillis();
			
			//check for animUpdatefirst
			if(currTime - animStartTime > animFrameDuration*1000.0f)
			{
				++imageIndex;
				
				if(imageIndex >= spr.getNumFrames())
					imageIndex = 0;
				
				animStartTime = currTime;
			}
			
			
			
			if(currTime - startTime > 250)
				show = Global.party.isTraveling();
			
		}
		
	}
	
	public void render()
	{
		Point pos = new Point((32 - spr.getWidth())/2, (32 - spr.getHeight())/2);
		
		if(show)
			spr.render(
					Global.mouseGridPos.x*32 + pos.x, 
					Global.mouseGridPos.y*32 + pos.y, imageIndex);
	}
	
	
	
	

}
