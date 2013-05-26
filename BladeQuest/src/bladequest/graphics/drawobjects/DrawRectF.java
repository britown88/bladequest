package bladequest.graphics.drawobjects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import bladequest.graphics.DrawObject;

public class DrawRectF implements DrawObject {

	RectF r;
	Paint paint;

	
	public DrawRectF(float left, float top, float right, float bottom, Paint paint)
	{
		this.r = new RectF(left, top, right, bottom);
		this.paint = paint;
	}
	
	public DrawRectF(RectF r, Paint paint)
	{
		this.r = r;
		this.paint = paint;
	}
	
	@Override
	public void render(Canvas canvas) {
		canvas.drawRect(r,paint);
	}

}
