package bladequest.graphics.drawobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import bladequest.graphics.DrawObject;

public class DrawScaledBmp implements DrawObject {


	Bitmap bitmap;
	Rect src, dest;
	Paint paint;
	public boolean drawn, unloadBitmap;
	
	public DrawScaledBmp(Bitmap bitmap, Rect src, Rect dest, Paint paint)
	{
		this.bitmap =bitmap;
		this.src = src;
		this.dest = dest;
		this.paint = paint;
		drawn = unloadBitmap = false;
	}

	@Override
	public void render(Canvas canvas) {
		
		drawn = true;
		if(!bitmap.isRecycled())
			canvas.drawBitmap(bitmap, src, dest, paint);
		
		if (unloadBitmap) bitmap.recycle();
	}

}
