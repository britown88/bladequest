package bladequest.graphics;

import android.graphics.Point;

public class ReactionBubble 
{
	private Sprite sprite;
	
	private int index, frameLength, frameCount;
	private boolean loop, show, done;
	private float duration;
	private Point drawPos;
	
	private long startTime;
	
	public ReactionBubble(Sprite spr, int frameLength)
	{
		this.sprite = new Sprite(spr);
		this.frameLength = frameLength;
	}
	
	public ReactionBubble(ReactionBubble other)
	{
		this.sprite = new Sprite(other.sprite);		
		this.frameLength = other.frameLength;
	}
	
	public boolean isDone() { return done; }
	
	public void turnOff()
	{
		done = true;
		show = false;
	}
	
	public void open(Point drawPos, float duration, boolean loop)
	{
		this.drawPos = drawPos;
		this.duration = duration;
		this.show = true;
		this.done = false;
		this.frameCount = 0;
		this.index = 0;
		this.loop = loop;
		
		this.startTime = System.currentTimeMillis();
	}
	
	private void updateAnimation()
	{
		if(++frameCount > frameLength)
		{
			frameCount = 0;			
			if(index + 1 >= sprite.getNumFrames())
			{
				if(loop)
					index = 0;
			}
			else
				index++;
			
			if(duration != -1.0 && System.currentTimeMillis() - startTime >= duration*1000)
			{
				done = true;
				show = false;
			}
		}
	}
	
	public void render()
	{		
		if(show)
		{
			updateAnimation();
			sprite.render(drawPos.x, drawPos.y, index);
		}
			
	}

}
