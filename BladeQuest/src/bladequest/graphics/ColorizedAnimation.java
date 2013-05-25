package bladequest.graphics;

import bladequest.system.Recyclable;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;

public class ColorizedAnimation implements AnimatedBitmap, Recyclable {

	BitmapFrame[] frames;
	Bitmap animMap;
	
	private BitmapFrame remap(BitmapFrame frame, int[] colorIndices, Rect outputRect)
	{
		int indices = colorIndices.length;
		
		int yInput = frame.srcRect.top;
		int yOutput = outputRect.top;
		int height = frame.srcRect.height();
		
		int xInput = frame.srcRect.left;
		int xOutput = outputRect.left;
		int width = frame.srcRect.width();
		
		for (int y = 0; y < height; ++y)
		{
			for (int x = 0; x < width; ++x)
			{
				int pixelColor = frame.bitmap.getPixel(x + xInput, y + yInput);
				
				if(Color.alpha(pixelColor) == 255)
				{
					int index = Color.red(pixelColor)/32;
					if (index < indices)
					{
						animMap.setPixel(x + xOutput, y + yOutput, colorIndices[index]);
					}
				}
			}
		}
		
		return new BitmapFrame(animMap, outputRect);
	}
	public ColorizedAnimation(AnimatedBitmap uncoloredFrames, int[] colorIndices)
	{
		//just do this purely along the X for now, since that's easiest
		Point animPt = new Point(0,0);
		BitmapFrame[] framesToRecolor = uncoloredFrames.getFrames();
		frames = new BitmapFrame[framesToRecolor.length];
		
		
		//get anim size
		for (BitmapFrame frame : framesToRecolor)
		{
			animPt.x += frame.srcRect.width();
			animPt.y = Math.max(animPt.y, frame.srcRect.height());
		}
		
		animMap = Bitmap.createBitmap(animPt.x, animPt.y, Config.ARGB_8888);
		
		animPt.x = animPt.y = 0;
		
		int frameIdx = 0;
		//recolor frames.
		for (BitmapFrame frame : framesToRecolor)
		{
			Rect newRect = new Rect(animPt.x, animPt.y, animPt.x + frame.srcRect.width(), animPt.y + frame.srcRect.height());
			frames[frameIdx++] = remap(frame, colorIndices, newRect);
			
			animPt.x += frame.srcRect.width();
		} 
	}
	
	void release() {}
	
	@Override
	public BitmapFrame[] getFrames() {
		return frames;
	}
	@Override
	public void recycle() {
		animMap.recycle();
	}
   
}
