package bladequest.graphics;

import bladequest.world.Global;
import android.graphics.Bitmap;

public class Image {

	public Image() {
	  prev = next = null;
	}
	public Image(String path, short xPixels, short yPixels) {
		this.path = path;
		this.xPixels = xPixels;
		this.yPixels = yPixels;
		refCount = 0;
		bmp = null;
		prev = next = null;
	}
	
	//for now, only lock and unlock in the graphics thread!
	public Bitmap lock()
	{
		if (refCount == 0)
		{
			//load bitmap...
			Global.imageManager.load(this);  //blocks to load the bitmap currently, if it's unloaded.
			refCount = 1;
		}
		else
		{
			++refCount;
		}
		
		return bmp;
	}
	public void unlock()
    {
    	--refCount;
    	if (refCount == 0)
    	{
    		//add as most recently unloaded
    		Global.imageManager.unload(this);    		
    	}
    }
    
	Bitmap bmp;
	int refCount;
    String path;
    short xPixels, yPixels;
    Image next, prev;
}
