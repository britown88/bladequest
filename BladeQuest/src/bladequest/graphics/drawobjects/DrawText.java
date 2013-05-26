package bladequest.graphics.drawobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import bladequest.graphics.DrawObject;
import bladequest.world.Global;

public class DrawText implements DrawObject {

	String text;
	float x, y;
	Paint paint;
	Rect bounds;
	
	public DrawText(String text, float x, float y, Paint paint)
	{
		this.text = text;
		this.x = x;
		this.y = y;
		this.paint = paint;
		
		bounds = new Rect();
	}

	@Override
	public void render(Canvas canvas) {
		paint.getTextBounds("H", 0, 1, bounds);	
		
	    int height = Math.abs(bounds.bottom - bounds.top);
	    
	    Paint p2 = Global.textFactory.getTextPaint((int)paint.getTextSize(), Color.BLACK, paint.getTextAlign());
	    int offset = 2;
	    
	    for(int i = 1; i <= offset; ++i)		    
	    	canvas.drawText(text, x - bounds.left + i, y + height/2.0f + 2 + i, p2);			
		
	    canvas.drawText(text, x - bounds.left, y + height/2.0f + 2, paint);
	}

}
