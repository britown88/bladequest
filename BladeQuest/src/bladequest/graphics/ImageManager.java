package bladequest.graphics;

import java.io.InputStream;
import java.util.concurrent.locks.ReentrantLock;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import bladequest.world.Global;

public class ImageManager {

	Image loadedHead, loadedTail;
	Image unloadedHead, unloadedTail;
	
	int loadedGraphicsMemory;
	
	public static int loadedGraphicsMemoryCap = 8 * 1024 * 1024;  //8 meg should be enough for everybody?
	
	public ReentrantLock lock;
	public ImageManager() {
		
		loadedGraphicsMemory = 0;
		
		lock = new ReentrantLock();
		
		loadedHead = new Image();
		loadedTail = new Image();
		
		unloadedHead = new Image();
		unloadedTail = new Image();		
	}
	void unlink(Image image)
	{
		if (image.next != null)
		{
			image.prev.next = image.next;
			image.next.prev = image.prev;
			image.next = null;
			image.prev = null;
		}
	}
    void load(Image image)
    {
    	int newMemory = image.xPixels * image.yPixels * 4;
    	
    	//either never been loaded, or was freed earlier due to running out of space.
    	if (image.bmp == null)  
    	{
        	lock.lock();
        	while (loadedGraphicsMemory + newMemory > loadedGraphicsMemoryCap)
        	{
        		if (unloadedHead.next == unloadedTail) break; //   > Game is Hard!
        		
        		Image toUnload = unloadedTail.prev;
        		toUnload.bmp.recycle();
        		toUnload.bmp = null;
        		loadedGraphicsMemory -= toUnload.xPixels * toUnload.yPixels * 4;
        		unlink(toUnload);        		
        	}
        	loadedGraphicsMemory += newMemory;
        	lock.unlock();
        	
    		InputStream is = null;
    		try {is = Global.activity.getAssets().open(image.path);} catch (Exception e) {
    			Log.d(Global.TAG, "Unable to open file "+image.path);
    			Global.closeGame();}
		
		
    		Bitmap bmp = BitmapFactory.decodeStream(is);
    		image.bmp = bmp;
    	}
    	
    	lock.lock();
    	unlink(image);
    	image.next = loadedHead.next;
    	loadedHead.next = image;
    	image.prev = loadedHead;
    	image.next.prev = image;
    	lock.unlock();
    }
    void unload(Image image)
    {    	
    	lock.lock();
    	unlink(image);
    	image.next = unloadedHead.next;
    	unloadedHead.next = image;
    	image.prev = unloadedHead;
    	image.next.prev = image;    	
    	lock.unlock();    	
    }
}
