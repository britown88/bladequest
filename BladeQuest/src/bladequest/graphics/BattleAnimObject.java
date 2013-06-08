package bladequest.graphics;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import bladequest.world.Global;

public class BattleAnimObject 
{
	public List<BattleAnimObjState> states;
	private int startFrame, endFrame, stateIndex;
	private String bmpName;
	private Types type;
	private boolean outlineOnly;
	public Point lineDrawPos1, lineDrawPos2;
	public AnimationPosition animPos;
	
	private BattleAnimObjState workingState, currentState, nextState, y0, y3;

	//TODO: consider using syncronization here instead of mass allocations on render
	private Paint objPaint;
	private Rect objRect, drawRect;
	private Bitmap customBmp;
	
	float[] colorTransform;
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
		workingState = new BattleAnimObjState();
		drawRect = new Rect();
		lineDrawPos1 = new Point();
		lineDrawPos2= new Point();
				
	}
	
	public BattleAnimObject(Types type, boolean outlineOnly, Bitmap customBmp)
	{
		states = new ArrayList<BattleAnimObjState>();
		this.outlineOnly = outlineOnly;
		
		this.type = type;
		this.customBmp = customBmp;
		objPaint = new Paint();
		objPaint.setStyle(outlineOnly || type == Types.Line ? Style.STROKE : Style.FILL);
		objPaint.setAntiAlias(true);
		//objPaint.setStrokeCap(Cap.)
		
		objRect = new Rect();
		workingState = new BattleAnimObjState();
		drawRect = new Rect();
		lineDrawPos1 = new Point();
		lineDrawPos2= new Point();		
	}
	
	public BattleAnimObject(BattleAnimObject other)
	{
		states = new ArrayList<BattleAnimObjState>();
		this.outlineOnly = other.outlineOnly;
		this.type = other.type;
		this.bmpName = other.bmpName;
		this.linearInterp = other.linearInterp;
		this.customBmp = other.customBmp;
		
		
		for(BattleAnimObjState state : other.states)
			addState(new BattleAnimObjState(state));	
		
		objPaint = new Paint();
		objPaint.setStyle(outlineOnly || type == Types.Line ? Style.STROKE : Style.FILL);
		objPaint.setAntiAlias(true);

		workingState = new BattleAnimObjState();
		
		objRect = new Rect();
		drawRect = new Rect();
		lineDrawPos1 = new Point();
		lineDrawPos2= new Point();		
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
	
	
	public void start(AnimationPosition pos)
	{
		this.animPos = pos;
		
		genStartAndEndFrame();
		setState(0);		
		
	}
	
	private void setState(int index)
	{
		if(index >= 0 && index < states.size())
		{
			stateIndex = index;
			
			currentState = states.get(index);
			workingState.copyFrom(currentState);
			workingState.offset(this);
			
			y0 = index == 0 ? currentState : states.get(index - 1);
			
			if(index < states.size() - 1)
				nextState = states.get(index+1);
			else
				nextState = null;
			
			if(index < states.size() - 2)
			{
				y3 = states.get(index + 2);
			}
			else
				y3 = nextState;


			updatePaint();
			updateRect();
		}
		
	}
	
	private void updatePaint()
	{
		if (type == Types.Interpolatable) return;
		
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
		if (type == Types.Interpolatable) return;
		
		objRect.set(
				workingState.pos1.x - (int)(workingState.size.x/2),
				workingState.pos1.y - (int)(workingState.size.y/2),
				workingState.pos1.x + (int)(workingState.size.x/2),
				workingState.pos1.y + (int)(workingState.size.y/2));	
	
	}
	
	private void updateDrawPos()
	{
		if (type == Types.Interpolatable) return;
		
		drawRect.set(objRect.left, objRect.top, objRect.right, objRect.bottom);
		lineDrawPos1.set(workingState.pos1.x, workingState.pos1.y);
		lineDrawPos2.set(workingState.pos2.x, workingState.pos2.y);
		
		Global.setvpToScreen(drawRect);
		Global.setvpToScreen(lineDrawPos1);
		Global.setvpToScreen(lineDrawPos2);
	}
	
	public void addState(BattleAnimObjState state){states.add(state);}
	public int getEndFrame() { genStartAndEndFrame();return endFrame; }
	public int getStartFrame() { genStartAndEndFrame();return startFrame; }
	

	//~= 0.2126 R + 0.7152 G + 0.0722 B
	
	public float progress;
	public void update(int frame)
	{
		//only interpolate if there's a next state to interpolate to!
		if(nextState != null && frame >= startFrame && frame <= endFrame)
		{
			//update to most current state
			if(frame >= nextState.frame)
				setState(stateIndex+1);
			
			workingState.updateSpriteAnimation();

			if(nextState != null)
			{
				//calculated percantage between the two frames current frame is at
				int stateLength = nextState.frame - currentState.frame;
				if (stateLength <=0) stateLength = 1;
				progress = (float)(frame - currentState.frame) / (float)stateLength;
				
				
				
				currentState.offset(this);
				nextState.offset(this);
				
				if(y0 != null && !y0.equals(currentState))
					y0.offset(this);
				
				if(y3 != null && !y3.equals(nextState))
					y3.offset(this);
				
				
				//injected interface.
				if (type == Types.Interpolatable)
				{
					workingState.interpObj = currentState.interpObj.interpolateAgainst(nextState.interpObj, progress);
					return;
				}
				//interpolation			
				//---size
				if(currentState.size.x != nextState.size.x || currentState.size.y != nextState.size.y)
				{
					workingState.size.x = BattleAnim.cosineInterpolation(currentState.size.x, nextState.size.x, progress);
					workingState.size.y = BattleAnim.cosineInterpolation(currentState.size.y, nextState.size.y, progress);
					
				}
				
				//---color
				
				workingState.a = BattleAnim.cosineInterpolation(currentState.a, nextState.a, progress);
				workingState.r = BattleAnim.cosineInterpolation(currentState.r, nextState.r, progress);
				workingState.g = BattleAnim.cosineInterpolation(currentState.g, nextState.g, progress);
				workingState.b = BattleAnim.cosineInterpolation(currentState.b, nextState.b, progress);

				if((type == Types.Line || outlineOnly) && currentState.strokeWidth != nextState.strokeWidth)
					workingState.strokeWidth = BattleAnim.linearInterpolation(currentState.strokeWidth, nextState.strokeWidth, progress);
				
				//bitmap colorization
				if(type == Types.Bitmap)
				{
					float t= workingState.colorize= BattleAnim.cosineInterpolation(currentState.colorize, nextState.colorize, progress);
					float[] newColorTransform = {
				            1-t, 0, 0, 0, workingState.r*t, 
				            0, 1-t, 0, 0, workingState.g*t,
				            0, 0, 1-t, 0, workingState.b*t, 
				            0, 0, 0, 1, 0};
					colorTransform = newColorTransform.clone();
					
					//objPaint.setColorFilter(new ColorMatrixColorFilter(colorTransform));
				}
					
				updatePaint();
				
				//---rotation (lerp rotation)
				
				workingState.rotation = BattleAnim.linearInterpolation(currentState.rotation, nextState.rotation, progress);
				
				//---position (including lines)

				if(linearInterp)
				{

					workingState.pos1.x = BattleAnim.linearInterpolation(currentState.pos1.x, nextState.pos1.x, progress);
					workingState.pos2.x = BattleAnim.linearInterpolation(currentState.pos2.x, nextState.pos2.x, progress);

					workingState.pos1.y = BattleAnim.linearInterpolation(currentState.pos1.y, nextState.pos1.y, progress);
					workingState.pos2.y = BattleAnim.linearInterpolation(currentState.pos2.y, nextState.pos2.y, progress);
				
				}
				else
				{
					workingState.pos1.x = BattleAnim.cubicInterpolation(y0.pos1.x, currentState.pos1.x, nextState.pos1.x, y3.pos1.x, progress);
					workingState.pos1.y = BattleAnim.cubicInterpolation(y0.pos1.y, currentState.pos1.y, nextState.pos1.y, y3.pos1.y, progress);
					workingState.pos2.x = BattleAnim.cubicInterpolation(y0.pos2.x, currentState.pos2.x, nextState.pos2.x, y3.pos2.x, progress);
					workingState.pos2.y = BattleAnim.cubicInterpolation(y0.pos2.y, currentState.pos2.y, nextState.pos2.y, y3.pos2.y, progress);
				
				}
				
				currentState.unOffset(this);
				nextState.unOffset(this);
				
				if(y0 != null && !y0.equals(currentState))
					y0.unOffset(this);
				
				if(y3 != null && !y3.equals(nextState))
					y3.unOffset(this);
				
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
			if (type == Types.Interpolatable)
			{
				workingState.interpObj.render();
				return;
			}
			
			updateDrawPos();
			
			//render based on current state
			switch(type)
			{
			case Rect:
				Global.renderer.drawRect(new Rect(drawRect), new Paint(objPaint), false);
				break;
			case Elipse:				
				Global.renderer.drawElipse(new Rect(drawRect), new Paint(objPaint));
				break;
			case Bitmap:
				Rect srcRect = workingState.bmpSrcRect;
				Rect draw = drawRect;
				if (srcRect != null) srcRect = new Rect(srcRect);
				if (draw != null) draw = new Rect(drawRect);
				
				if (colorTransform != null)
				{
					ScreenFilter.instance().pushFilter(colorTransform);	
				}
				if (currentState.mirrored)
				{
					Global.renderer.drawMirroredBitmap(customBmp == null ? Global.bitmaps.get(bmpName) : customBmp, workingState.rotation, srcRect, draw, new Paint(objPaint));					
				}
				else
				{
					Global.renderer.drawBitmap(customBmp == null ? Global.bitmaps.get(bmpName) : customBmp, workingState.rotation, srcRect, draw, new Paint(objPaint));
				}
				if (colorTransform != null)
				{				
					ScreenFilter.instance().popFilter();
				}
				break;
			case Line:
				Global.renderer.drawLine(
						lineDrawPos1.x, lineDrawPos1.y,
						lineDrawPos2.x, lineDrawPos2.y, 
						new Paint(objPaint));
				break;
			default:
				
			}
		}
		
	}
	
	public boolean isEmpty()
	{
		return states.isEmpty();
	}
	
	public enum Types
	{
		Rect,
		Elipse,
		Bitmap,
		Line,
		Interpolatable
	}

}
