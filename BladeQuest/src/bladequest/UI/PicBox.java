package bladequest.UI;

import android.graphics.*;
import bladequest.world.Global;
import bladequest.graphics.Icon;

public class PicBox 
{
	public Bitmap bmp;
	public Rect srcRect, destRect;
	
	private Icon ico;
	
	public PicBox(Bitmap bmp, Rect srcRect, Rect destRect)
	{
		this.bmp = bmp;
		this.srcRect = srcRect;
		this.destRect = destRect;
	}
	
	public PicBox(Icon ico)
	{
		this.ico = ico;
	}
	

	public void render(int x, int y)
	{
		if(ico == null)
		{
			Rect dest = new Rect(destRect);		
			dest.offset(x, y);							
			Global.renderer.drawBitmap(bmp, srcRect, dest, null);
		}
		else
			ico.render(x, y);
		
	}
}
