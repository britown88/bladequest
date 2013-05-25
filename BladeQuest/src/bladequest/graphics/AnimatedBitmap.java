package bladequest.graphics;

import android.graphics.Point;
import android.graphics.Rect;
import bladequest.graphics.BattleAnimObjState.PosTypes;
import bladequest.graphics.BattleAnimObject.Types;

public interface AnimatedBitmap {
	BitmapFrame[] getFrames();
	
	class Extensions
	{
		public static BattleAnim genAnim(AnimatedBitmap animBitmap, float fps, int timeDelay) 
		{
			BattleAnim anim = new BattleAnim(fps);
			BitmapFrame[] frames = animBitmap.getFrames();
			BattleAnimObject baobj = new BattleAnimObject(Types.Bitmap, false, frames[0].bitmap);
			
			for(int i = 0; i < frames.length; ++i)
			{
				BattleAnimObjState state = new BattleAnimObjState(i*timeDelay, PosTypes.Target);
				state.size = new Point(frames[i].srcRect.width()*2, frames[i].srcRect.height()*2);
				state.pos1 = new Point(0,0);
				state.argb(255, 255, 255, 255);
				if (i == frames.length-1) state.a = 0; //fade out.
				Rect r = frames[i].srcRect;
				state.setBmpSrcRect(r.left, r.top, r.right, r.bottom);
				baobj.addState(state);
			}
			
			anim.addObject(baobj);
			
			return anim;
		}
	}
	
}