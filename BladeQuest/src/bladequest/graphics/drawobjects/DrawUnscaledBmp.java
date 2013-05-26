package bladequest.graphics.drawobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import bladequest.graphics.DrawObject;

public class DrawUnscaledBmp implements DrawObject {

	Bitmap bitmap; 
	float left; 
	float top; 
	Paint paint;
	
	public DrawUnscaledBmp(Bitmap bitmap, float left, float top, Paint paint)
	{
		this.bitmap= bitmap;
		this.left = left;
		this.top = top;
		this.paint = paint;
		
	}
	@Override
	public void render(Canvas canvas) {
		if(!bitmap.isRecycled())
			canvas.drawBitmap(bitmap, left, top, paint);
	}

}
