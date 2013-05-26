package bladequest.graphics.drawobjects;

import android.graphics.Canvas;
import bladequest.graphics.DrawObject;

public class DrawColorARGB implements DrawObject {

	int a,r,g,b;
	public DrawColorARGB(int a, int r, int g, int b)
	{
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	

	@Override
	public void render(Canvas canvas) {
		canvas.drawARGB(a,r,g,b);
	}

}
