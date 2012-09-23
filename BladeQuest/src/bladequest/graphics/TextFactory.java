package bladequest.graphics;

import android.graphics.*;
import android.graphics.Paint.Align;

import java.util.*;

public class TextFactory 
{
	private Map<String, Paint> paints;
	private Typeface font;
	
	public TextFactory(Typeface font)
	{
		paints = new HashMap<String, Paint>();		
		this.font = font;
	}
	
	public Paint getTextPaint(int size, int color, Align align)
	{
		String key = size+"|"+color+"|"+align.toString();
		
		Paint p = paints.get(key);
		if(p == null)
		{
			p = buildTextPaint(size, color, align);
			paints.put(key, p);
		}		
		return p;		
	}
	
	public Paint getTextOutline(Paint paint, int color, int strokeWidth)
	{
		String key = (int)paint.getTextSize()+"|"+color+"|"+paint.getTextAlign().toString()+"|OUTLINE";
		
		Paint p = paints.get(key);
		if(p == null)
		{
			p = buildTextOutline(paint, color, strokeWidth);
			paints.put(key, p);
		}		
		return p;		
	}
	
	public Paint buildTextOutline(Paint p, int color, int strokeWidth)
	{
		Paint paint = new Paint(p);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(strokeWidth);
		paint.setColor(color);	
		return paint;
		
	}
	
	
	
	private Paint buildTextPaint(int size, int color, Align align)
	{

		Paint paint = new Paint();	
		paint.setColor(color);
		paint.setTextSize(size);
		//paint.setFakeBoldText(true);
		paint.setTypeface(font);
		paint.setTextAlign(align);
		paint.setStyle(Paint.Style.FILL);
		paint.setTextScaleX(0.9f);
		//paint.setAntiAlias(true);
		
		return paint;		
	}

}
