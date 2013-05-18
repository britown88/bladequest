package bladequest.combat;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import bladequest.world.PlayerCharacter;
import bladequest.world.Global;

public class DamageMarker 
{
	private final int life = 50;
	private final float startingXSpeed = -3.0f;
	
	private int value;
	private Paint paint;
	private Paint paintOutline;
	private Point position;
	private int bottom;
	
	private int timer;
	private boolean show;
	
	private float xSpeed;
	private boolean done;
	private PlayerCharacter target;
	private String dmgText;
	private boolean manualPos;
	
	public DamageMarker(int value, PlayerCharacter c)
	{
		this.target = c;
		this.value = value;
		this.dmgText = "";
		init();
	}
	public DamageMarker(String str, PlayerCharacter c)
	{
		this.target = c;
		this.value = 0;		
		this.dmgText = str;
		init();
	}	
	public DamageMarker(int value, PlayerCharacter c, int x, int y)
	{
		this.target = c;
		this.value = value;
		this.dmgText = "";
		this.position = new Point(x, y);
		manualPos = true;
		init();
	}	
	public DamageMarker(String str, PlayerCharacter c, int x, int y)
	{
		this.target = c;
		this.value = 0;		
		this.dmgText = str;
		this.position = new Point(x, y);
		manualPos = true;
		init();
	}
	
	private void init()
	{
		if(!manualPos)
		{
			Point drawPos = new Point(target.getPosition(true));
			drawPos.y += 8;
			this.position = Global.vpToScreen(drawPos);
		}		
		
		bottom = position.y + 24;
		
		done = false;
		show = false;
		
		timer = 0;
		xSpeed = startingXSpeed;
		
		paint = Global.textFactory.getTextPaint(dmgText == "" ? 13 : 7, value > 0 ? Color.argb(255, 34, 177, 76) : Color.WHITE, Align.CENTER);
		paintOutline= Global.textFactory.getTextOutline(paint, Color.BLACK, 4);
	}
	
	public void update()
	{	
		if(!done)
		{
			timer++;
			if(!show)
			{
				show = true;
				timer = 0;
			}
			else
			{
				position.y += xSpeed;
				xSpeed += 1;
				
				if (position.y > bottom)
					position.y = bottom;
				
				if(timer > life)
				{
					done = true;
					if(target != null)
						target.clearDamaged();
				}					
			}
		}	
	}
	
	public boolean isShown(){return show;}	
	public boolean isDone(){return done;}
	
	public void render()
	{
		if(show)
		{
			int i = Math.abs(value);
			
			if(dmgText == "")
			{
				Global.renderer.drawText(""+i, position.x, position.y, paintOutline);
				Global.renderer.drawText(""+i, position.x, position.y, paint);
			}
			else
			{
				Global.renderer.drawText(dmgText, position.x, position.y, paintOutline);
				Global.renderer.drawText(dmgText, position.x, position.y, paint);
			}
		}
				
	}

}
