package bladequest.graphics;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class TilePlateBitmap 
{
	public boolean used;
	public Bitmap bmp;
	public TilePlateBitmap()
	{
		bmp = Bitmap.createBitmap(320, 320, Config.ARGB_4444);
		used = false;
	}
	
	

}
