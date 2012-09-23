package bladequest.graphics;

import bladequest.world.Global;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;

public class WeaponSwingDrawable 
{
	private Rect[] srcFrames;
	private Point frameSize;
	private Bitmap bmp;
	
	public WeaponSwingDrawable(Bitmap bmp, Point frameSize)
	{
		srcFrames = new Rect[3];
		
		for(int i = 0; i < 3; ++i)
			srcFrames[i] = new Rect(i * frameSize.x,0,i * frameSize.x + frameSize.x,frameSize.y);
		
		this.bmp = bmp;		
		this.frameSize = frameSize;
	}
	
	public void release()
	{
		bmp.recycle();
	}
	
	public void render(int frame, int x, int y)
	{
		Rect dest = new Rect(
				Global.vpToScreenX(x),
				Global.vpToScreenY(y),
				Global.vpToScreenX(x + frameSize.x * 2),
				Global.vpToScreenY(y + frameSize.y * 2));
		
		Global.renderer.drawBitmap(bmp, srcFrames[frame], dest, null);		
	}

}
