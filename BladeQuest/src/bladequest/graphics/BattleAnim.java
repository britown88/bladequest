package bladequest.graphics;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;

public class BattleAnim 
{
	private List<BattleAnimObject> objects;
	
	private float framePeriod;
	private int frame, finalFrame;
	
	private boolean playing, done, loops;
	
	private Point src, tar;
	
	private long startTime, frameTime;
	//private Paint text;
	
	
	public BattleAnim(float fps)
	{
		objects = new ArrayList<BattleAnimObject>();
		this.framePeriod = 1000.0f / fps;
		
		playing = false;
		//text = Global.textFactory.getTextPaint(9, Color.WHITE, Align.LEFT);
	}
	
	public BattleAnim(BattleAnim other)
	{
		this.objects = new ArrayList<BattleAnimObject>();
		this.framePeriod = other.framePeriod;
		this.loops = other.loops;
		for(BattleAnimObject obj : other.objects)
			addObject(new BattleAnimObject(obj));		
		
		playing = false;		
	}
	
	public BattleAnim(BattleAnim other, float speedModifer)
	{
		this.objects = new ArrayList<BattleAnimObject>();
		this.framePeriod = other.framePeriod * speedModifer;
		this.loops = other.loops;
		for(BattleAnimObject obj : other.objects)
			addObject(new BattleAnimObject(obj));		
		
		playing = false;		
	}	
	//take the frame number from an animation and convert it to the frame time of the event
	public int syncToAnimation(int frame)
	{
		int actualFrame = (int)(getFrameLength() * (frame == -1 ? getFinalFrame() : frame));
		
		return actualFrame;
	}
	public int syncToAnimation(float animPercent)
	{
		int actualFrame = (int)(getFrameLength() * (getFinalFrame()*animPercent));
		
		return actualFrame;
	}
	public float getFrameLength(){ return framePeriod;}
	public void loop() { loops = true; }
	public void addObject(BattleAnimObject obj){objects.add(obj);}
	public boolean Done() { return done; }
	public boolean Playing() { return playing; }
	public int getFinalFrame() { findFinalFrame();return finalFrame;}
	
	public void copyLastObject(int count, int frameOffset)
	{
		BattleAnimObject obj = objects.get(objects.size() - 1);
		
		for(int i = 0; i < count; ++i)
		{
			obj = new BattleAnimObject(obj);
			for(BattleAnimObjState state : obj.states)
				state.frame += frameOffset;
			addObject(obj);
		}
	}
	
	public void play(Point source, Point target)
	{
		this.src = source; this.tar = target;
		
		playing = true;
		done = false;
		
		frame = -1;	
		
		//start all bojects
		for(BattleAnimObject obj : objects)
			obj.start(source, target);
		
		//find finalFrame
		findFinalFrame();
		
		//smooth end necessary for looping
		for(BattleAnimObject obj : objects)
			if(obj.getStartFrame() == 0 && obj.getEndFrame() == finalFrame)
				obj.alwaysDraw();

			
	}
	
	private void findFinalFrame()
	{
		finalFrame = 0;
		for(BattleAnimObject obj : objects)
			finalFrame = Math.max(finalFrame, obj.getEndFrame());
	}
	
	//update currentFrame, update all objects, and end playing if past final frame
	public void update()
	{
		if(playing)
		{
			if(frame == -1)
			{
				frame = 0;
				startTime = System.currentTimeMillis();
			}
			else
			{
				frameTime = System.currentTimeMillis() - startTime;
				if(frameTime >= framePeriod)
				{
					//update frame, accounting for times when multiple frames were traversed
					int elapsedFrames = (int)(frameTime / framePeriod);
					frame += elapsedFrames;
					
					//update start time to the end of the last completed frame
					startTime += (int)(elapsedFrames * framePeriod);
					
					if(frame <= finalFrame)
						for(BattleAnimObject obj : objects)
							obj.update(frame);
					else
					{
						//animation is over
						if(loops)
						{
							play(src, tar);
							for(BattleAnimObject obj : objects)								
							{
								obj.update(frame);
								obj.render(frame);
							}
						}							
						else
						{
							playing = false;
							done = true;
						}
						
					}
				}
			}
		}
		
	}
	
	public void render()
	{
		if(playing)
		{
//			//draw debug text
			
//			BattleAnimObjState workingState = objects.get(0).getworkingState();
//			BattleAnimObjState currentState = objects.get(0).getCurrentState();
//			BattleAnimObjState nextState = objects.get(0).getNextState();
//			
//			Global.renderer.drawText("Frame: "+frame, Global.vpToScreenX(0), Global.vpToScreenY(10), text);
//			Global.renderer.drawText("OBJ State Index: "+objects.get(0).getCurrentStateIndex(), Global.vpToScreenX(0), Global.vpToScreenY(20), text);
//			Global.renderer.drawText("Current Pos - x:"+currentState.pos1.x + " y:" + currentState.pos1.y, Global.vpToScreenX(0), Global.vpToScreenY(30), text);
//			Global.renderer.drawText("Working Pos - x:"+workingState.pos1.x + " y:" + workingState.pos1.y, Global.vpToScreenX(0), Global.vpToScreenY(40), text);
//			if(nextState != null)
//			Global.renderer.drawText("Next    Pos - x:"+nextState.pos1.x + " y:" + nextState.pos1.y, Global.vpToScreenX(0), Global.vpToScreenY(50), text);
//			
//			Global.renderer.drawText("State Progress:"+objects.get(0).progress*100.0f + "%", Global.vpToScreenX(0), Global.vpToScreenY(60), text);
//			
			
			for(BattleAnimObject obj : objects)
				obj.render(frame);
		}
			
	}
	

}
