package bladequest.graphics.drawobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import bladequest.graphics.DrawObject;
import bladequest.world.Global;

public class DrawMatrixScaledBmp implements DrawObject {

	Bitmap bitmap;
	Rect src, dest;
	Paint paint;
	Matrix m;
	
	public DrawMatrixScaledBmp(Bitmap bitmap, Matrix m, Rect src, Rect dest, Paint paint)
	{
		this.bitmap =bitmap;
		this.src = src;
		this.dest = dest;
		this.m = m;
		this.paint = paint;
	}
	
	Matrix getScaleMatrix(Canvas canvas)
	{
		Matrix out = new Matrix();
		out.setScale(Global.baseScale + Global.imageScale, Global.baseScale + Global.imageScale, canvas.getWidth() / 2, canvas.getHeight() / 2);
		return out;
	}
	

	@Override
	public void render(Canvas canvas) {
		Matrix finalMatrix = getScaleMatrix(canvas);
		finalMatrix.preConcat(m);
		canvas.save();
		canvas.setMatrix(finalMatrix);
		if(!bitmap.isRecycled())
			canvas.drawBitmap(bitmap, src, dest, paint);
		
		canvas.restore();
	}
}
