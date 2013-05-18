package bladequest.world;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import bladequest.graphics.Sprite;

public class LoadingScreen 
{
	private Sprite spr;
	private Paint txtPaint;
	private int imageIndex;
	
	public LoadingScreen()
	{
		int character = Global.rand.nextInt(10);
		switch(character)
		{
		case 0:spr = Global.sprites.get("aramis");break;
		case 1:spr = Global.sprites.get("roland");break;
		case 2:spr = Global.sprites.get("sanson");break;
		case 3:spr = Global.sprites.get("reine");break;
		case 4:spr = Global.sprites.get("ursos");break;
		case 5:spr = Global.sprites.get("joy");break;
		case 6:spr = Global.sprites.get("curtana");break;
		case 7:spr = Global.sprites.get("luc");break;
		case 8:spr = Global.sprites.get("marie");break;
		case 9:spr = Global.sprites.get("carl");break;
		}
		
		//build paint
		txtPaint = Global.textFactory.getTextPaint(15, Color.WHITE, Align.LEFT);		
		
		imageIndex = 0;
	}
	
	private void updateAnimation()
	{
		if(spr != null)
		{
			if(Global.updateAnimation)
			{				
				imageIndex++;
				if(imageIndex >= spr.getNumFrames())
					imageIndex = 0;
			}
		}
	}
	
	public void render()
	{
		updateAnimation();
		Global.renderer.drawColor(Color.BLACK);
		
		Global.renderer.drawText("Loading!", 
				Global.vpToScreenX(Global.vpWidth - 120), 
				Global.vpToScreenY(Global.vpHeight - 22), txtPaint);
		
		spr.renderFromVP(Global.vpWidth - 152, Global.vpHeight - 45, imageIndex);
		
	}
	

}
