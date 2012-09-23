package bladequest.world;

import bladequest.graphics.BattleSprite;
import bladequest.graphics.BattleSprite.faces;
import android.graphics.*;
import android.graphics.Paint.Align;

public class DamageMarker 
{
	private int value;
	private Paint paint;
	private Paint paintOutline;
	private Point position;
	private int bottom;
	
	private int timer;
	private int life;
	private int delay;
	private boolean show;
	
	private float xSpeed;
	private boolean done;
	private Character target;
	private String dmgText;
	private boolean manualPos;
	
	public DamageMarker(int value, Character c, int delay)
	{
		this.target = c;
		this.value = value;
		this.delay = delay;
		this.dmgText = "";
		init();
	}
	public DamageMarker(String str, Character c, int delay)
	{
		this.target = c;
		this.value = 0;		
		this.delay = delay;
		this.dmgText = str;
		init();
	}
	
	public DamageMarker(int value, Character c, int delay, int x, int y)
	{
		this.target = c;
		this.value = value;
		this.delay = delay;
		this.dmgText = "";
		this.position = new Point(x, y);
		manualPos = true;
		init();
	}
	
	public DamageMarker(String str, Character c, int delay, int x, int y)
	{
		this.target = c;
		this.value = 0;		
		this.delay = delay;
		this.dmgText = str;
		this.position = new Point(x, y);
		manualPos = true;
		init();
	}
	
	private void init()
	{
		if(!manualPos)
		{
			Point drawPos = new Point(target.getPosition());
			
			if(target.isEnemy())
				drawPos.y += 8;
			else
			{
				drawPos.x += 14;
				drawPos.y += 16;
			}
			this.position = new Point(Global.vpToScreenX(drawPos.x), Global.vpToScreenY(drawPos.y));
		}		
		
		bottom = position.y + 24;
		
		done = false;
		show = false;
		
		timer = 0;
		life = 50;
		xSpeed = -3.0F;
		
		paint = Global.textFactory.getTextPaint(dmgText == "" ? 13 : 7, value > 0 ? Color.argb(255, 34, 177, 76) : Color.WHITE, Align.CENTER);
		paintOutline= Global.textFactory.getTextOutline(paint, Color.BLACK, 4);
	}
	
	public void update()
	{
		if(show)
		{
			position.y += xSpeed;
			xSpeed += 1;
			
			if (position.y > bottom)
				position.y = bottom;
		}
		
		
		timer++;
		if(!show)
		{
			if(timer > delay)
			{
				show = true;
				timer = 0;
				target.modifyHP(value, false);
				if(Global.GameState == States.GS_MAINMENU)
					Global.menu.updateCharUseScreen();
				
				if(Global.GameState == States.GS_BATTLE)
				{
					Global.battle.postDamage(target);
					if(!target.isEnemy() && dmgText == "" && value < 0 && target.isDone())
						target.setBattleFrame(faces.Damaged);
				}
				

				
			}
		}
		else
		{
			if(timer > life)
			{
				done = true;
				if(Global.GameState == States.GS_BATTLE && !target.isEnemy() && dmgText == "")
					target.setIdle(false);
			}
				
		}
		

	}
	
	public boolean isShown()
	{
		return show;
	}
	
	public boolean isDone()
	{
		return done;
	}
	
	public void render()
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
