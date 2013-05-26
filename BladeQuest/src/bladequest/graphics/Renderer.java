package bladequest.graphics;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import bladequest.graphics.drawobjects.DrawColorARGB;
import bladequest.graphics.drawobjects.DrawColorPacked;
import bladequest.graphics.drawobjects.DrawEllipse;
import bladequest.graphics.drawobjects.DrawLine;
import bladequest.graphics.drawobjects.DrawRect;
import bladequest.graphics.drawobjects.DrawRectF;
import bladequest.graphics.drawobjects.DrawScaledBmp;
import bladequest.graphics.drawobjects.DrawText;
import bladequest.graphics.drawobjects.DrawUnscaledBmp;
import bladequest.world.Global;

public class Renderer 
{
	private List<DrawObject> frontBuffer, backBuffer;
	private List<DrawObject> updateBuffer, renderBuffer;
	
	public Renderer()
	{

		frontBuffer = new ArrayList<DrawObject>();
		backBuffer = new ArrayList<DrawObject>();
		
		updateBuffer = frontBuffer;
		renderBuffer = backBuffer;
	}	

	public void swap()
	{
		if(updateBuffer.equals(frontBuffer)) updateBuffer = backBuffer;
		else
		if(updateBuffer.equals(backBuffer)) updateBuffer = frontBuffer;

		if(renderBuffer.equals(frontBuffer)) renderBuffer = backBuffer;
		else
		if(renderBuffer.equals(backBuffer)) renderBuffer = frontBuffer;
		
		updateBuffer.clear();
	}
	
	public void drawRect(float left, float top, float right, float bottom, Paint paint)
	{
		updateBuffer.add(new DrawRectF(left+Global.screenShaker.drawDelta.x, top, right+Global.screenShaker.drawDelta.x, bottom, paint));
	}
	
	public void drawElipse(float left, float top, float right, float bottom, Paint paint)
	{
		updateBuffer.add(new DrawEllipse(left+Global.screenShaker.drawDelta.x, top, right+Global.screenShaker.drawDelta.x, bottom, paint));
	}
	
	public void drawLine(float startX, float startY, float stopX, float stopY, Paint paint)
	{
		updateBuffer.add(new DrawLine(startX+Global.screenShaker.drawDelta.x, startY, stopX+Global.screenShaker.drawDelta.x, stopY, paint));
	}

	public DrawObject drawBitmap(Bitmap bitmap, float left, float top, Paint paint)
	{
		DrawObject dro = new DrawUnscaledBmp(bitmap, left+Global.screenShaker.drawDelta.x, top, paint);
		updateBuffer.add(dro);	
		return dro;
	}
	public DrawObject drawBitmap(Bitmap bitmap, Rect src, Rect dest, Paint paint)
	{
		dest.offset(Global.screenShaker.drawDelta.x, 0);
		DrawObject dro = new DrawScaledBmp(bitmap, src, dest, paint);		
		updateBuffer.add(dro);		
		return dro;
	}
	public void drawText(String text, float x, float y, Paint paint)
	{
		updateBuffer.add(new DrawText(text, x, y, paint));	
	}
	public void drawColor(int color)
	{
		updateBuffer.add(new DrawColorPacked(color));
	}
	public void drawColor(int a, int r, int g, int b)
	{
		updateBuffer.add(new DrawColorARGB(a, r, g, b));
	}
	
	public void drawRect(float left, float top, float right, float bottom, Paint paint, boolean ignoreShake)
	{
		if(ignoreShake)
			updateBuffer.add(new DrawRectF(left, top, right, bottom, paint));
		else
			updateBuffer.add(new DrawRectF(left+Global.screenShaker.drawDelta.x, top, right+Global.screenShaker.drawDelta.x, bottom, paint));
	}
	public void drawRect(Rect r, Paint paint, boolean ignoreShake)
	{
		if(!ignoreShake)
			r.offset(Global.screenShaker.drawDelta.x, 0);
		
		updateBuffer.add(new DrawRect(r, paint));		
	}
	
	public void drawElipse(Rect r, Paint paint)
	{		
		updateBuffer.add(new DrawEllipse(r, paint));		
	}
	
	public void drawBitmap(Bitmap bitmap, float left, float top, Paint paint, boolean ignoreShake)
	{
		if(ignoreShake)
			updateBuffer.add(new DrawUnscaledBmp(bitmap, left, top, paint));		
		else
			updateBuffer.add(new DrawUnscaledBmp(bitmap, left+Global.screenShaker.drawDelta.x, top, paint));		
	}
	public void drawBitmap(Bitmap bitmap, Rect src, Rect dest, Paint paint, boolean ignoreShake)
	{
		if(!ignoreShake)
			dest.offset(Global.screenShaker.drawDelta.x, 0);
		
		updateBuffer.add(new DrawScaledBmp(bitmap, src, dest, paint));		
	}


	
	public void render(Canvas canvas)
	{
		canvas.scale(Global.baseScale + Global.imageScale, Global.baseScale + Global.imageScale, canvas.getWidth() / 2, canvas.getHeight() / 2);

		
		for(DrawObject dob : renderBuffer)
		{
			dob.render(canvas);

		}
			
		
		
	}

}
