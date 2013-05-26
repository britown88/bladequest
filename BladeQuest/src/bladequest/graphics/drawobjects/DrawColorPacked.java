package bladequest.graphics.drawobjects;

import android.graphics.Canvas;
import bladequest.graphics.DrawObject;

public class DrawColorPacked  implements DrawObject {

	int color;
	
	public DrawColorPacked(int color)
	{
		this.color = color;	
	}

	@Override
	public void render(Canvas canvas) {
		canvas.drawColor(color);
		
	}

}
