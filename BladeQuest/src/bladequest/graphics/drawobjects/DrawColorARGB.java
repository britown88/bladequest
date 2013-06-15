package bladequest.graphics.drawobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import bladequest.graphics.DrawObject;

public class DrawColorARGB implements DrawObject {

	int a,r,g,b;
	PorterDuff.Mode mode;
	public DrawColorARGB(int a, int r, int g, int b)
	{
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
		mode = null;
	}
	
	public DrawColorARGB(int a, int r, int g, int b, PorterDuff.Mode mode)
	{
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
		this.mode = mode;
	}	
	

	@Override
	public void render(Canvas canvas) {
		if (mode == null)
		{
			//default srcOver mode.
			canvas.drawARGB(a,r,g,b);	
		}
		else
		{
			//custom coloration
			canvas.drawColor(Color.argb(a,r,g,b), mode);
		}
	}

}
