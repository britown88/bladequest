package bladequest.bladeskits;

import android.graphics.Point;
import bladequest.graphics.Sprite;
import bladequest.world.Global;

public class Actor 
{
	private static final int IMAGE_INDEX_LENGTH = 250;
	
	private Sprite sprite;
	
	private String face;
	private Point startPos, targetPos, pos;
	private Point startSize, targetSize, size;
	
	private long currentFrame;
	
	private long posStartFrame, posTargetFrame;
	private long sizeStartFrame, sizeTargetFrame;
	
	private int imageIndex;
	private long animStartFrame;
	
	public Actor(String spriteName, long frame)
	{
		this.sprite = new Sprite(Global.sprites.get(spriteName));
		pos = new Point();
		size = new Point();
		
		imageIndex = 0;
		
		animStartFrame = frame;
	}
	
	public void moveTo(Point target, long frameCount)
	{
		startPos = new Point(pos);
		targetPos = new Point(target);
		
		posStartFrame = currentFrame;
		posTargetFrame = currentFrame + frameCount;			
	}
	
	public void resize(Point targetSize, long frameCount)
	{
		startSize = new Point(size);
		targetSize = new Point(targetSize);
		
		sizeStartFrame = currentFrame;
		sizeTargetFrame = currentFrame + frameCount;			
	}
	
	
	
	public void update(long frame)
	{
		this.currentFrame = frame;
		
		if(sprite.getNumFrames() > 0 && frame - animStartFrame >= IMAGE_INDEX_LENGTH)
		{
			
		}		
	}
	
	public void render()
	{
		
	}

}
