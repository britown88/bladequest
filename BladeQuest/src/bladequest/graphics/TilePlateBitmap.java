package bladequest.graphics;

import bladequest.world.Global;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class TilePlateBitmap 
{
	public boolean used;
	public Bitmap bmp;
	public TilePlateBitmap()
	{
		bmp = Bitmap.createBitmap(16*Global.tilePlateSize.x, 16*Global.tilePlateSize.y, Config.ARGB_8888);
		used = false;
	}
	
	

}
