package bladequest.graphics.drawobjects;

import android.graphics.Canvas;
import android.graphics.Paint;
import bladequest.graphics.DrawObject;

public class DrawLine implements DrawObject {

	Paint p;
	float x1, y1, x2, y2;
	public DrawLine(float x1, float x2, float y1, float y2, Paint p) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.p = p;
	}

	@Override
	public void render(Canvas canvas) {
		canvas.drawLine(x1, y1, x2, y2, p);
	}

}
