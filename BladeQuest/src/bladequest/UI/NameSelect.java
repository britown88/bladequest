package bladequest.UI;

import android.graphics.Color;
import android.graphics.Paint.Align;
import bladequest.UI.ListBox.LBStates;
import bladequest.UI.MenuPanel.Anchors;
import bladequest.UI.MsgBox.YesNo;
import bladequest.world.Character;
import bladequest.world.Global;
import bladequest.world.States;
import bladequest.graphics.Sprite;
import bladequest.graphics.Sprite.faces;
import android.graphics.Paint;

public class NameSelect 
{
	private final float menuWidthVpPercent = 28.0f; 
	private final float menuHeightVpPercent = 16.0f;
	private final int nameLength = 8;
	
	private final int menuWidth, barHeight;
	
	private ListBox keyboard, menu;
	private MenuPanel namePanel;
	private MsgBox messageBox;
	private Character character;
	
	private boolean closing, closed;
	
	private Paint menuText;
	private Sprite spr;
	private int imageIndex;
	
	private String newName;
	
	private final int darkenAlphaMax = 200, darkenInc = 5;
	private int darkenAlpha;
	private boolean darkening;
	
	public NameSelect()
	{
		menuWidth = (int)((float)Global.vpWidth * (menuWidthVpPercent/100.0f));
		barHeight = (int)((float)Global.vpHeight * (menuHeightVpPercent/100.0f));
		menuText = Global.textFactory.getTextPaint(13, Color.WHITE, Align.CENTER);
		
		messageBox = new MsgBox();		
		
		buildMenu();		
	}
	
	private void buildMenu()
	{
		keyboard = new ListBox(0, barHeight, Global.vpWidth, Global.vpHeight-barHeight*2, 4, 13, menuText);
		
		for(int i = 65; i <= 90; ++i)
			keyboard.addItem(""+(char)i, ""+(char)i, false);
		
		for(int i = 97; i <= 122; ++i)
			keyboard.addItem(""+(char)i, ""+(char)i, false);
		
		namePanel = new MenuPanel(Global.vpWidth/2, 0, Global.vpWidth*2/3, barHeight);
		namePanel.anchor = Anchors.TopCenter;
		namePanel.addTextBox("_", namePanel.width/2 + 18, barHeight/2, menuText);
		
		menu = new ListBox(Global.vpWidth/2, Global.vpHeight, Global.vpWidth, barHeight, 1, 3, menuText);
		menu.anchor = Anchors.BottomCenter;
		menu.addItem("Space", "spa", false);
		menu.addItem("Back", "bak", false);
		menu.addItem("Done", "don", false);
	}	
	
	public void open(Character c)
	{
		this.character = c;
		closed = closing = false;
		this.spr = new Sprite(c.getWorldSprite());
		spr.changeFace(faces.Down);
		
		//use default name
		newName = Global.characters.get(c.getName()).getDisplayName();
		
		darkenAlpha = 0;
		undarken();
	}	
	private void close()
	{
		if(!closing)
		{
			closing = true;
			Global.screenFader.setFadeColor(255, 0, 0, 0);
			Global.screenFader.fadeOut(4);
		}
		
	}	
	private void handleClosing()
	{
		if(Global.screenFader.isDone())
		{
			closed = true;
			Global.GameState = States.GS_WORLDMOVEMENT;
			Global.delay();
			Global.screenFader.fadeIn(4);			
		}
	}	
	public boolean isClosed() { return closed; }
	
	private void darken(){darkening = true;}	
	private void undarken(){darkening = false;}	
	private void renderDark(){Global.renderer.drawColor(Color.argb(darkenAlpha, 0, 0, 0));}
	private void showMessage(String msg, boolean yesNoOpt)
	{
		darken();
		messageBox.addMessage(msg, yesNoOpt);
		messageBox.open();
	}

	
	private void handleOption(String str)
	{
		if(str.equals("spa"))
		{
			if(newName.length() < nameLength)
				newName += " ";
		}
		else if(str.equals("bak"))
		{
			if(newName.length() > 1)
				newName = newName.substring(0, newName.length() - 1);
			else if(newName.length() == 1)
				newName = "";
		}
		else if(str.equals("don"))
		{
			showMessage("Character will be named " + newName + ". Are you sure?", true);
			//close();
		}
	}
	
	public void update()
	{
		//update darkness
		if(darkening)
			darkenAlpha = Math.min(darkenAlpha + darkenInc, darkenAlphaMax);
		else
			darkenAlpha = Math.max(darkenAlpha - darkenInc, 0);
				
		if(closing)
			handleClosing();
		
		messageBox.update();		
		keyboard.update();
		namePanel.update();
		menu.update();
		
		namePanel.getTextAt(0).text = newName + (newName.length() < nameLength ? "_" : "");
		
		//update sprite
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
		keyboard.render();
		namePanel.render();
		menu.render();
		
		spr.renderFromVP(Global.vpWidth/2 - Global.vpWidth/3+8, 7, imageIndex);
		
		if(messageBox.Opened())
			renderDark();
		
		messageBox.render();
	}
	
	public void touchActionMove(int x, int y)
	{
		if(!closing)
		{
			if(messageBox.Opened())
			{
				messageBox.touchActionMove(x, y);
			}				
			else if(messageBox.Closed())
			{
				keyboard.touchActionMove(x, y);
				menu.touchActionMove(x, y);
			}
			
		}
		
	}
	public void touchActionUp(int x, int y)
	{	
		if(!closing)
		{
			if(messageBox.Opened())
			{
				messageBox.touchActionUp(x, y);
				
				if(messageBox.isYesNo() && messageBox.getSelectedOpt() == YesNo.Yes)
				{
					//change name and close menu
					character.setDisplayName(newName);
					close();
				}
				
				if(!messageBox.Opened())
					undarken();
			}				
			else if(messageBox.Closed())
			{
				if(menu.touchActionUp(x, y) == LBStates.Selected)
					handleOption((String)menu.getSelectedEntry().obj);
				else if(keyboard.touchActionUp(x, y) == LBStates.Selected)
				{
					if(newName.length() < nameLength)
						newName += (String)keyboard.getSelectedEntry().obj;
				}
			}
			
		}
		
	}
	public void touchActionDown(int x, int y)
	{
		if(!closing)
		{
			if(messageBox.Opened())
			{
				messageBox.touchActionDown(x, y);
			}				
			else if(messageBox.Closed())
			{
				keyboard.touchActionDown(x, y);
				menu.touchActionDown(x, y);
			}
			
		}
		
	}

}
