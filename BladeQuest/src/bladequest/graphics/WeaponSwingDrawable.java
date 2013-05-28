package bladequest.graphics;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import bladequest.graphics.BattleAnimObjState.PosTypes;
import bladequest.graphics.BattleAnimObject.Types;
import bladequest.world.Global;

public class WeaponSwingDrawable 
{
	private Rect[] srcFrames, animSrcFrames;
	private Point frameSize/*, animFrameSize*/;
	private Bitmap bmp, animBmp;
	
	public WeaponSwingDrawable(Bitmap bmp, Point frameSize, Bitmap animBmp, Point animFrameSize)
	{
		srcFrames = new Rect[3];
		for(int i = 0; i < 3; ++i)
			srcFrames[i] = new Rect(i * frameSize.x,0,i * frameSize.x + frameSize.x,frameSize.y);
		this.bmp = bmp;		
		this.frameSize = frameSize;
		
		animSrcFrames = new Rect[3];		
		for(int i = 0; i < 3; ++i)
			animSrcFrames[i] = new Rect(i * animFrameSize.x,0,i * animFrameSize.x + animFrameSize.x,animFrameSize.y);
		this.animBmp = animBmp;		
		//this.animFrameSize = animFrameSize;
	}
	
	public void release()
	{
		bmp.recycle();
		animBmp.recycle();
	}
	
	public void render(int frame, int x, int y, boolean mirrored)
	{
		Rect dest = new Rect(
				Global.vpToScreenX(x),
				Global.vpToScreenY(y),
				Global.vpToScreenX(x + frameSize.x * 2),
				Global.vpToScreenY(y + frameSize.y * 2));
		
		if (mirrored)
		{
			Global.renderer.drawMirroredBitmap(bmp, 0.0f, srcFrames[frame], dest, null);
		}
		else
		{
			Global.renderer.drawBitmap(bmp, 0.0f, srcFrames[frame], dest, null);
		}
				
	}
	
	public BattleAnim genAnim() 
	{
		BattleAnim anim = new BattleAnim(180.0f);
		BattleAnimObject baobj = new BattleAnimObject(Types.Bitmap, false, animBmp);
		
		for(int i = 0; i < 4; ++i)
		{
			BattleAnimObjState state = new BattleAnimObjState(i*15, PosTypes.Target);
			state.size = new Point(64, 64);
			state.pos1 = new Point(0,0);
			state.argb(255, 255, 255, 255);
			Rect r = animSrcFrames[Math.min(i, 2)];
			state.setBmpSrcRect(r.left, r.top, r.right, r.bottom);
			baobj.addState(state);
		}

		//fade out
		baobj.states.get(3).a = 0;

		
		anim.addObject(baobj);
		
		return anim;
	}

}
