package bladequest.graphics;

import java.util.*;

import bladequest.world.Global;

import android.graphics.Bitmap;
import android.graphics.Paint.Style;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Rect;
import android.graphics.Point;
import android.graphics.Paint;

public class BattleAnimObject 
{
	public List<BattleAnimObjState> states;
	private int startFrame, endFrame, stateIndex;
	private String bmpName;
	private Types type;
	private boolean outlineOnly;
	public Point source, target, lineDrawPos1, lineDrawPos2;
	
	
	private BattleAnimObjState workingState, currentState, nextState, y0, y3;

	private Paint objPaint;
	private Rect objRect, drawRect;
	
	private boolean alwaysDraw, linearInterp;
	
	public BattleAnimObject(Types type, boolean outlineOnly, String bmpName)
	{
		states = new ArrayList<BattleAnimObjState>();
		this.outlineOnly = outlineOnly;
		
		this.type = type;
		this.bmpName = bmpName;
		objPaint = new Paint();
		objPaint.setStyle(outlineOnly || type == Types.Line ? Style.STROKE : Style.FILL);
		objPaint.setAntiAlias(true);
		//objPaint.setStrokeCap(Cap.)
		
		objRect = new Rect();
		
	}
	
	public BattleAnimObject(BattleAnimObject other)
	{
		states = new ArrayList<BattleAnimObjState>();
		this.outlineOnly = other.outlineOnly;
		this.type = other.type;
		this.bmpName = other.bmpName;
		this.linearInterp = other.linearInterp;
		
		
		
		for(BattleAnimObjState state : other.states)
			addState(new BattleAnimObjState(state));	
		
		objPaint = new Paint();
		objPaint.setStyle(outlineOnly || type == Types.Line ? Style.STROKE : Style.FILL);
		objPaint.setAntiAlias(true);

		
		objRect = new Rect();
	}

	
	
	public void alwaysDraw() { alwaysDraw = true; }
	public void interpolateLinearly() { linearInterp = true; }
	public int getCurrentStateIndex() { return stateIndex; }
	public BattleAnimObjState getCurrentState() { return currentState; }
	public BattleAnimObjState getNextState() { return nextState; }
	public BattleAnimObjState getworkingState() { return workingState; }
	
	private void genStartAndEndFrame()
	{ 
		startFrame = states.get(0).frame;
		endFrame = states.get(states.size()-1).frame;
	}
	
	
	public void start(Point source, Point target)
	{
		this.source = source;
		this.target = target;
		
		genStartAndEndFrame();
		
		for(BattleAnimObjState state : states)
			state.randomized = false;
		
		states.get(0).randomize(this);
		setState(0);
		if(nextState != null && !nextState.randomized) nextState.randomize(this);
		
		
	}
	
	private void setState(int index)
	{
		if(index >= 0 && index < states.size())
		{
			stateIndex = index;	
			
			
			
			currentState = states.get(index);
			workingState = new BattleAnimObjState(currentState);
			
			y0 = index == 0 ? currentState : states.get(index - 1);
			
			if(index < states.size() - 1)
				nextState = states.get(index+1);
			else
				nextState = null;
			
			y3 = index < states.size() - 2 ? states.get(index + 2) : nextState;
				
			//nextState = index < states.size() - 1 ? states.get(index+1) : null;
			if(y3 != null && !y3.randomized) y3.randomize(this);

			updatePaint();
			updateRect();
		}
		
	}
	
	private void updatePaint()
	{
		if(type != Types.Bitmap)
		{
			objPaint.setARGB(workingState.a, workingState.r, workingState.g, workingState.b);
			if(type == Types.Line || outlineOnly)
				objPaint.setStrokeWidth(workingState.strokeWidth);
		}
		else
			objPaint.setAlpha(workingState.a);		
	}
	
	private void updateRect()
	{
		objRect = new Rect(
				workingState.pos1.x - (int)(workingState.size.x/2),
				workingState.pos1.y - (int)(workingState.size.y/2),
				workingState.pos1.x + (int)(workingState.size.x/2),
				workingState.pos1.y + (int)(workingState.size.y/2));	
	
	}
	
	private void updateDrawPos()
	{
		drawRect = Global.vpToScreen(objRect);
		lineDrawPos1 = Global.vpToScreen(workingState.pos1);
		lineDrawPos2 = Global.vpToScreen(workingState.pos2);
		
		
	}
	
	public void addState(BattleAnimObjState state){states.add(state);}
	public int getEndFrame() { genStartAndEndFrame();return endFrame; }
	public int getStartFrame() { genStartAndEndFrame();return startFrame; }
	
	private int linearInterpolation(int x0, int x1, float mu){return (int)(x0 * (1.0f-mu) + x1*mu);}
	private float linearInterpolation(float x0, float x1, float mu){return x0 * (1.0f-mu) + x1*mu;}
	private int cubicInterpolation(int y0, int y1, int y2, int y3, float mu)
	{
		int a0,a1,a2,a3;
		float mu2;

		mu2 = mu*mu;
		a0 = (int)(-0.5f*y0 + 1.5f*y1 - 1.5f*y2 + 0.5f*y3);
		a1 = (int)(y0 - 2.5f*y1 + 2.0f*y2 - 0.5f*y3);
		a2 = (int)(-0.5f*y0 + 0.5f*y2);
		a3 = y1;
	
		return (int)(a0*mu*mu2 + a1*mu2 + a2*mu + a3);
	}
	private float cubicInterpolation(float y0, float y1, float y2, float y3, float mu)
	{
		float a0,a1,a2,a3,mu2;

		mu2 = mu*mu;
		a0 = -0.5f*y0 + 1.5f*y1 - 1.5f*y2 + 0.5f*y3;
		a1 = y0 - 2.5f*y1 + 2.0f*y2 - 0.5f*y3;
		a2 = -0.5f*y0 + 0.5f*y2;
		a3 = y1;
	
		return a0*mu*mu2 + a1*mu2 + a2*mu + a3 ;
	}
	private int cosineInterpolation(int y1, int y2, float t)
	{
		float t2 = (1.0f - (float)(Math.cos(t*Math.PI)))/2.0f;
		return y1 == y2 ? y1 : (int)(y1*(1-t2)+y2*t2);
	}
	private float cosineInterpolation(float y1, float y2, float t)
	{
		float t2 = (1.0f - (float)(Math.cos(t*Math.PI)))/2.0f;
		return y1 == y2 ? y1 : y1*(1-t2)+y2*t2;
	}
	
	public float progress;
	public void update(int frame)
	{
		//only interpolate if there's a next state to interpolate to!
		if(nextState != null && frame >= startFrame && frame <= endFrame)
		{
			//update to most current state
			if(frame >= nextState.frame)
				setState(stateIndex+1);

			if(nextState != null)
			{
				//calculated percantage between the two frames current frame is at
				int stateLength = nextState.frame - currentState.frame;
				progress = (float)(frame - currentState.frame) / (float)stateLength;
				
				//interpolation			
				//---size
				workingState.size.x = cosineInterpolation(currentState.size.x, nextState.size.x, progress);
				workingState.size.y = cosineInterpolation(currentState.size.y, nextState.size.y, progress);
				
				//---color
				
				workingState.a = cosineInterpolation(currentState.a, nextState.a, progress);
				workingState.r = cosineInterpolation(currentState.r, nextState.r, progress);
				workingState.g = cosineInterpolation(currentState.g, nextState.g, progress);
				workingState.b = cosineInterpolation(currentState.b, nextState.b, progress);
				
				if(type == Types.Line || outlineOnly)
					workingState.strokeWidth = linearInterpolation(currentState.strokeWidth, nextState.strokeWidth, progress);
				
				//bitmap colorization
				if(type == Types.Bitmap)
				{
					float t= workingState.colorize=cosineInterpolation(currentState.colorize, nextState.colorize, progress);
					float[] colorTransform = {
				            1-t, 0, 0, 0, workingState.r*t, 
				            0, 1-t, 0, 0, workingState.g*t,
				            0, 0, 1-t, 0, workingState.b*t, 
				            0, 0, 0, 1, 0};					
					objPaint.setColorFilter(new ColorMatrixColorFilter(colorTransform));
				}
					
				updatePaint();
				
				//---rotation
				
				//---position (including lines)
				if(linearInterp)
				{
					workingState.pos1.x = linearInterpolation(currentState.pos1.x, nextState.pos1.x, progress);
					workingState.pos1.y = linearInterpolation(currentState.pos1.y, nextState.pos1.y, progress);
					workingState.pos2.x = linearInterpolation(currentState.pos2.x, nextState.pos2.x, progress);
					workingState.pos2.y = linearInterpolation(currentState.pos2.y, nextState.pos2.y, progress);
				
				}
				else
				{
					workingState.pos1.x = cubicInterpolation(y0.pos1.x, currentState.pos1.x, nextState.pos1.x, y3.pos1.x, progress);
					workingState.pos1.y = cubicInterpolation(y0.pos1.y, currentState.pos1.y, nextState.pos1.y, y3.pos1.y, progress);
					workingState.pos2.x = cubicInterpolation(y0.pos2.x, currentState.pos2.x, nextState.pos2.x, y3.pos2.x, progress);
					workingState.pos2.y = cubicInterpolation(y0.pos2.y, currentState.pos2.y, nextState.pos2.y, y3.pos2.y, progress);
					
				}
						
				
				//build final rect
				updateRect();
			}				
			
		}
		
	}
	
	//takes target location
	public void render(int frame)
	{
		//if state is shown and frame is within object's screentime
		if((workingState.show && frame >= startFrame && frame <= endFrame) || alwaysDraw)
		{
			updateDrawPos();
			
			//render based on current state
			switch(type)
			{
			case Rect:
				Global.renderer.drawRect(drawRect, new Paint(objPaint), false);
				break;
			case Elipse:				
				Global.renderer.drawElipse(drawRect, new Paint(objPaint));
				break;
			case Bitmap:
				Global.renderer.drawBitmap(Global.bitmaps.get(bmpName), workingState.bmpSrcRect, drawRect, new Paint(objPaint));
				break;
			case Line:
				Global.renderer.drawLine(
						lineDrawPos1.x, lineDrawPos1.y,
						lineDrawPos2.x, lineDrawPos2.y, 
						new Paint(objPaint));
				break;
				
			}
		}
		
	}
	
	public enum Types
	{
		Rect,
		Elipse,
		Bitmap,
		Line
	}

}
