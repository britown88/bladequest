package bladequest.graphics;

import bladequest.world.Global;
import android.graphics.*;

public class Icon 
{	
	private Bitmap bmp;	
	
	public int srcX, srcY, x, y;
	public float scale;
	
	public Rect src, destRect;
	
	public Icon(int x, int y)
	{
		srcX= x;
		srcY = y;
		
		src = new Rect(
				srcX*Global.iconSize, 
				srcY*Global.iconSize, 
				srcX*Global.iconSize + Global.iconSize, 
				srcY*Global.iconSize +  Global.iconSize);
		
		bmp = Global.bitmaps.get("icons");		
	}
	
	public Icon(Icon other)
	{
		this.srcX = other.srcX;
		this.srcY = other.srcY;
		
		this.src = new Rect(other.src);
		this.destRect = new Rect(0,0,0,0);
		bmp = Global.bitmaps.get("icons");		
	}
	
	public void move(int x, int y)
	{
		this.x = x;
		this.y = y;
		
		int d = (int)(((float)Global.iconSize*scale)/2.0f);
		destRect = new Rect(x - d, y - d, x + d, y + d);		
		
	}
	
	
	public void render(int x, int y)
	{
		Rect dest = new Rect(destRect);		
		dest.offset(x, y);
		Global.renderer.drawBitmap(bmp, src, dest, null);
	}

}
