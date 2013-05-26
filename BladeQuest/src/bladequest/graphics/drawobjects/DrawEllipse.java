package bladequest.graphics.drawobjects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import bladequest.graphics.DrawObject;

public class DrawEllipse implements DrawObject {

	RectF bounds;
	Paint p;
	
	public DrawEllipse(Rect r, Paint p) {
		bounds = new RectF(r);
		this.p = p;
	}

	public DrawEllipse(float l, float t, float r, float b, Paint p) {
		bounds = new RectF(l, t, r, b);
		this.p = p;
	}

	
	@Override
	public void render(Canvas canvas) {
		canvas.drawOval(new RectF(bounds), p);
	}

}
