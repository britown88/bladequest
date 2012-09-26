package bladequest.graphics;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import bladequest.world.Global;

public class WeaponSwing 
{
	private final Point frameSize = new Point(42, 38);
	private String id;
	
	private Rect srcRect;
	
	public WeaponSwing(String id, int fileX, int fileY)
	{
		this.id = id;
		
		
		
		srcRect = new Rect(
				fileX * (frameSize.x * 3), 
				fileY * frameSize.y,
				fileX * (frameSize.x * 3)+(frameSize.x * 3),
				fileY * frameSize.y + frameSize.y);		
		
	}
	
	private int[] baseColors, swingColors;
	
	public WeaponSwingDrawable genSwingDrawable(int[] base, int[] swing)
	{
		Bitmap newBmp = Bitmap.createBitmap(frameSize.x*3, frameSize.y, Config.ARGB_8888);
		Bitmap swingBmp = Global.bitmaps.get("weaponswing");
		
		baseColors = base;
		swingColors = swing;
		
		
		for(int y = srcRect.top; y < srcRect.bottom; ++y)
			for(int x = srcRect.left; x < srcRect.right; ++x)
			{
				int c = swingBmp.getPixel(x, y);
				
				if(Color.alpha(c) == 255)
				{
					Point relativePos = new Point(x - srcRect.left, y - srcRect.top);
					boolean drawSlash = relativePos.x / frameSize.x == 1;
					int cIndex = Color.red(c) / 32;
					
					newBmp.setPixel(
							relativePos.x, 
							relativePos.y, 
							(drawSlash ? swing[cIndex] : base[cIndex]));					
				}				
			}		
		return new WeaponSwingDrawable(newBmp, frameSize);
	}

	public BattleAnim genAnim(BattleAnim battleAnim) 
	{
		battleAnim.setFirstObjectColors(0, swingColors[0]);
		battleAnim.setFirstObjectColors(1, swingColors[2]);
		battleAnim.setFirstObjectColors(2, swingColors[0]);
		
		return battleAnim;
	}
	

}
