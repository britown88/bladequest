package bladequest.graphics;

import android.graphics.*;
import bladequest.world.Global;

import java.util.*;

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
		updateBuffer.add(new DrawObject(left+Global.screenShaker.drawDelta.x, top, right+Global.screenShaker.drawDelta.x, bottom, paint, DrawObject.Types.Rect1));
	}
	
	public void drawElipse(float left, float top, float right, float bottom, Paint paint)
	{
		updateBuffer.add(new DrawObject(left+Global.screenShaker.drawDelta.x, top, right+Global.screenShaker.drawDelta.x, bottom, paint, DrawObject.Types.Elipse1));
	}
	
	public void drawLine(float startX, float startY, float stopX, float stopY, Paint paint)
	{
		updateBuffer.add(new DrawObject(startX+Global.screenShaker.drawDelta.x, startY, stopX+Global.screenShaker.drawDelta.x, stopY, paint, DrawObject.Types.Line));
	}

	public DrawObject drawBitmap(Bitmap bitmap, float left, float top, Paint paint)
	{
		DrawObject dro = new DrawObject(bitmap, left+Global.screenShaker.drawDelta.x, top, paint);
		updateBuffer.add(dro);	
		return dro;
	}
	public DrawObject drawBitmap(Bitmap bitmap, Rect src, Rect dest, Paint paint)
	{
		dest.offset(Global.screenShaker.drawDelta.x, 0);
		DrawObject dro = new DrawObject(bitmap, src, dest, paint);		
		updateBuffer.add(dro);		
		return dro;
	}
	public void drawText(String text, float x, float y, Paint paint)
	{
		updateBuffer.add(new DrawObject(text, x, y, paint));	
	}
	public void drawColor(int color)
	{
		updateBuffer.add(new DrawObject(color));
	}
	public void drawColor(int a, int r, int g, int b)
	{
		updateBuffer.add(new DrawObject(a, r, g, b));
	}
	
	public void drawRect(float left, float top, float right, float bottom, Paint paint, boolean ignoreShake)
	{
		if(ignoreShake)
			updateBuffer.add(new DrawObject(left, top, right, bottom, paint, DrawObject.Types.Rect1));
		else
			updateBuffer.add(new DrawObject(left+Global.screenShaker.drawDelta.x, top, right+Global.screenShaker.drawDelta.x, bottom, paint, DrawObject.Types.Rect1));
	}
	public void drawRect(Rect r, Paint paint, boolean ignoreShake)
	{
		if(!ignoreShake)
			r.offset(Global.screenShaker.drawDelta.x, 0);
		
		updateBuffer.add(new DrawObject(r, paint, DrawObject.Types.Rect2));		
	}
	
	public void drawElipse(Rect r, Paint paint)
	{		
		updateBuffer.add(new DrawObject(r, paint, DrawObject.Types.Elipse2));		
	}
	
	public void drawBitmap(Bitmap bitmap, float left, float top, Paint paint, boolean ignoreShake)
	{
		if(ignoreShake)
			updateBuffer.add(new DrawObject(bitmap, left, top, paint));		
		else
			updateBuffer.add(new DrawObject(bitmap, left+Global.screenShaker.drawDelta.x, top, paint));		
	}
	public void drawBitmap(Bitmap bitmap, Rect src, Rect dest, Paint paint, boolean ignoreShake)
	{
		if(!ignoreShake)
			dest.offset(Global.screenShaker.drawDelta.x, 0);
		
		updateBuffer.add(new DrawObject(bitmap, src, dest, paint));		
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
