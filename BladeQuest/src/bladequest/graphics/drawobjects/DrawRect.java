package bladequest.graphics.drawobjects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import bladequest.graphics.DrawObject;

public class DrawRect implements DrawObject {

	Rect r;
	Paint paint;

	
	public DrawRect(Rect r, Paint paint)
	{
		this.r = r;
		this.paint = paint;
	}
	
	@Override
	public void render(Canvas canvas) {
		canvas.drawRect(r, paint);
	}

}
