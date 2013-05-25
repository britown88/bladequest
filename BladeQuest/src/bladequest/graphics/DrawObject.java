package bladequest.graphics;

import bladequest.world.Global;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class DrawObject 
{	
	float f1, f2, f3, f4;
	int i;
	Rect r1, r2, bounds;
	Paint p;
	Bitmap bmp;
	String str;
	int a, r, g, b;
	
	public boolean drawn, unloadBitmap;
	
	private Types type;	
	public Types Type() {return type; }	
	
	public DrawObject(float left, float top, float right, float bottom, Paint paint, Types type)
	{
		this.type = type;
		f1 = left;
		f2 = top;
		f3 = right;
		f4 = bottom;
		p = paint;
	}

	public DrawObject(Rect r, Paint paint, Types type)
	{
		this.type = type;
		r1 = r;
		p = paint;
		
	}
	public DrawObject(Bitmap bitmap, float left, float top, Paint paint)
	{
		type = Types.Bmp1;
		bmp = bitmap;
		f1 = left;
		f2 = top;
		p = paint;
		
	}
	public DrawObject(Bitmap bitmap, Rect src, Rect dest, Paint paint)
	{
		type = Types.Bmp2;
		bmp =bitmap;
		r1 = src;
		r2 = dest;
		p = paint;
		
	}
	public DrawObject(String text, float x, float y, Paint paint)
	{
		type = Types.Text;
		str = text;
		f1 = x;
		f2 = y;
		bounds = new Rect();
		p = paint;	
	}
	public DrawObject(int color)
	{
		type = Types.Color;
		i = color;	
	}
	public DrawObject(int a, int r, int g, int b)
	{
		type = Types.Color2;
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
/*	private void applyARGBtoBitmap(Paint p, int a, int r, int g, int b, float t)
	{
		
		float[] colorTransform = {
	            1-t, 0, 0, 0, r*t, 
	            0, 1-t, 0, 0, g*t,
	            0, 0, 1-t, 0, b*t, 
	            0, 0, 0, 1, 0};
		
		p.setColorFilter(new ColorMatrixColorFilter(colorTransform));
		p.setAlpha(a);
	}*/
	
	public void render(Canvas canvas)
	{
		switch(type)
		{
		case Rect1:
			canvas.drawRect(f1, f2, f3, f4, p);
			break;
		case Rect2:
			canvas.drawRect(r1, p);
			break;
		case Bmp1:
			if(!bmp.isRecycled())
				canvas.drawBitmap(bmp, f1, f2, p);
			break;
		case Bmp2:
			 //p = new Paint();
			 //applyARGBtoBitmap(p, 128, 255, 255, 255, 1.0f);

			if(!bmp.isRecycled())
				canvas.drawBitmap(bmp, r1, r2, p);
			break;
		case Text:
			p.getTextBounds("H", 0, 1, bounds);	
			
			//int width = Math.abs(bounds.right - bounds.left);
		    int height = Math.abs(bounds.bottom - bounds.top);
		    
		    Paint p2 = Global.textFactory.getTextPaint((int)p.getTextSize(), Color.BLACK, p.getTextAlign());
		   
		    canvas.drawText(str, f1 - bounds.left+2, f2 + height/2.0f + 4, p2);			
			canvas.drawText(str, f1 - bounds.left, f2 + height/2.0f + 2, p);
			break;
		case Color:
			canvas.drawColor(i);
			break;
		case Color2:
			canvas.drawARGB(a, r, g, b);
			break;
		case Elipse1:
			canvas.drawOval(new RectF(f1, f2, f3, f4), p);
			break;
		case Elipse2:
			canvas.drawOval(new RectF(r1), p);
			break;		
		case Line:
			canvas.drawLine(f1, f2, f3, f4, p);
			break;
		}
		

		
		drawn = true;
		if(bmp != null && unloadBitmap)
			bmp.recycle();
	}
	
	public enum Types
	{
		Rect1,
		Rect2,
		Elipse1,
		Elipse2,
		Line,
		Bmp1,
		Bmp2,
		Text,
		Color,
		Color2
	}

}
