package bladequest.UI;

import bladequest.UI.ListBox.LBStates;
import bladequest.UI.MenuPanel.Anchors;
import bladequest.UI.MsgBox.YesNo;
import bladequest.world.Global;
import bladequest.world.PlayTimer;
import bladequest.world.States;
import bladequest.world.Character;
import bladequest.system.GameSave;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Point;
import android.graphics.Paint.Align;

public class SaveLoadMenu 
{
	public static final int LOADING = 0, SAVING = 1;
	private Paint textPaint, textPaintRight, textPaintCenter;
	private ListBox menu;	
	private int saveLoad;
	
	private boolean close, closed, closeAfterMsg;
	
	private MsgBox messageBox;
	private final int darkenAlphaMax = 200, darkenInc = 5;
	private int darkenAlpha;
	private boolean darkening, deleting;
	
	
	
	public SaveLoadMenu()
	{
		textPaint = Global.textFactory.getTextPaint(13, Color.WHITE, Align.LEFT);
		textPaintRight = Global.textFactory.getTextPaint(13, Color.WHITE, Align.RIGHT);
		textPaintCenter = Global.textFactory.getTextPaint(13, Color.WHITE, Align.CENTER);
		
		messageBox = new MsgBox();
		darkenAlpha = 0;
		darkening = false;
	}
	
	public void open(int saveOrLoad)
	{
		close = false;
		closed = false;
		this.saveLoad = saveOrLoad;
		buildPanels();
		closeAfterMsg = false;
		
		Global.saveLoader.readSaves(Global.activity);
	}
	
	private void buildPanels()
	{		
		menu = new ListBox(0, 0,Global.vpWidth, Global.vpHeight, 3, 1, textPaintCenter);
		
		menu.drawAllFrames = true;
		
		if(saveLoad == SAVING)
			menu.addItem("New Game", null, false);
		
		int i = 0;		
		for(GameSave save : Global.saveLoader.getSaves())
		{
			ListBoxEntry entry = menu.addItem("", i++, false);
			entry.clear();
			
			int highestLevel = 0;			
			for(Character c : save.characters)
				if(c != null && c.getLevel() > highestLevel)
					highestLevel = c.getLevel();
			
			entry.addTextBox(save.mapDisplayName, 8, 15, textPaint);
			entry.addTextBox("Level:"+highestLevel, menu.width - 8, 15, textPaintRight);
			entry.addTextBox("Gold:"+save.gold+"G", menu.width - 8, 30, textPaintRight);
			entry.addTextBox("Time:"+PlayTimer.genPlayTimeString(save.playTime, true), menu.width - 8, 45, textPaintRight);
		
			entry.setCustomColor(
					Color.rgb(save.fc1r, save.fc1g, save.fc1b), 
					Color.rgb(save.fc2r, save.fc2g, save.fc2b));
			
			int cindex = 0;
			int portraitSize = (int)(menu.getRowHeight()*0.75f);
			for(Character c : save.characters)
			{
				if(c != null) 
				{
					Rect src = c.getPortraitSrcRect();
					Rect dest = new Rect(
							portraitSize*cindex, 
							menu.getRowHeight() - portraitSize, 
							portraitSize*cindex+portraitSize,
							menu.getRowHeight());
					dest.inset(5, 5);
					entry.addPicBox(Global.bitmaps.get("portraits"), src, dest);
					++cindex;
				}
					
			}
			
		}
		menu.drawOutlines();
		menu.update();
	}
	
	public void close()
	{
		if(!close)
		{
			close = true;
			Global.screenFader.setFadeColor(255, 0, 0, 0);
			Global.screenFader.fadeOut(4);
		}
		
	}	
	private void handleClosing()
	{
		if(messageBox.Opened())
		{
			messageBox.close();
			undarken();
		}
		else
		{
			if(Global.screenFader.isDone())
			{
				if(saveLoad == LOADING && Global.saveLoader.save != null)
				{
					Global.screenFader.clear();
					Global.musicBox.play("", false, -1);
					Global.LoadMap(Global.saveLoader.save.mapName);
					if(Global.saveLoader.save.playingSong != null)
						Global.musicBox.play(Global.saveLoader.save.playingSong, false, -1);
				}
				
				closed = true;
			}
		}
		
		
	}
	
	private void darken(){darkening = true;}	
	private void undarken(){darkening = false;}	
	private void renderDark(){Global.renderer.drawColor(Color.argb(darkenAlpha, 0, 0, 0));}
	private void showMessage(String msg, boolean yesNoOpt)
	{
		darken();
		messageBox.addMessage(msg, yesNoOpt);
		messageBox.open();
	}
	
	public boolean isClosed() { return closed; }
	
	public void update()
	{
		if(darkening)
			darkenAlpha = Math.min(darkenAlpha + darkenInc, darkenAlphaMax);
		else
			darkenAlpha = Math.max(darkenAlpha - darkenInc, 0);
		
		if(close)
			handleClosing();
		
		messageBox.update();
		
		if(messageBox.Closed())
			menu.update();
		else if(menu.getSelectedEntry() != null)
			menu.getSelectedEntry().update();
	}
	
	public void render()
	{
		menu.render();
		
		if(messageBox.Opened())
		{
			renderDark();
			if(menu.getSelectedEntry() != null)
				menu.getSelectedEntry().render();
		}
					
		messageBox.render();
	}
	
	private void handleMsgBoxClose()
	{
		if(closeAfterMsg)
			close();
		else
			if(saveLoad == SAVING)
			{
				if(messageBox.getSelectedOpt() == YesNo.Yes || !messageBox.isYesNo())
				{
					if(menu.getSelectedEntry().obj == null)
						Global.saveLoader.saveGame(Global.saveLoader.getSaves().size());
					else
						Global.saveLoader.saveGame((Integer)menu.getSelectedEntry().obj);
					
					Global.saveLoader.writeSaves(Global.activity);
					Global.saveLoader.readSaves(Global.activity);
					buildPanels();
					
					showMessage("Game saved!", false);
					closeAfterMsg = true;
				}			
			}
			else if(saveLoad == LOADING)
			{
				if(messageBox.getSelectedOpt() == YesNo.Yes)
				{
					if(deleting)
					{
						Global.saveLoader.deleteSave((Integer)menu.getSelectedEntry().obj);
						Global.saveLoader.writeSaves(Global.activity);
						Global.saveLoader.readSaves(Global.activity);
						buildPanels();
						
						
						if(!Global.saveLoader.hasSaves())
							close();
					}
					else
					{
						Global.saveLoader.loadGame((Integer)menu.getSelectedEntry().obj);
						showMessage("Game loaded!", false);
						closeAfterMsg = true;
					}
					
				}
				deleting = false;
			}
	}
	
	public void backButtonPressed()
	{
		close();
	}
	
	public void onLongPress()
	{
		if(!close && menu.getSelectedEntry() != null)
		{
			showMessage("Delete this save?", true);
			deleting = true;
			menu.showOptSelect = false;
			//menu.getSelectedEntry().move(0, 0, 10);	
		}
		
	}
	
	public void touchActionMove(int x, int y)
	{
		if(!close)
		{
			if(!messageBox.Closed())
			{
				messageBox.touchActionMove(x, y);
			}				
			else
			{
				menu.touchActionMove(x, y);
			}
			
		}
		
	}
	
	
	
	public void touchActionUp(int x, int y)
	{
		if(!close)
		{
			if(!messageBox.Closed())
			{
				messageBox.touchActionUp(x, y);
				if(!messageBox.Opened())
				{
					undarken();
					handleMsgBoxClose();
				}
					
			}				
			else
			{
				ListBox.LBStates state = menu.touchActionUp(x, y);
				if(state == LBStates.Selected)
				{
					if(saveLoad == SAVING)
					{
						if(menu.getSelectedEntry().obj == null)
							showMessage("Saving game...", false);													
						else
							showMessage("Overwrite this save?", true);
					}
					else if(saveLoad == LOADING && !deleting)
					{
						showMessage("Load this save?", true);
						
					}
					//menu.getSelectedEntry().move(0, 0, 10);	
					//menu.getSelectedEntry().pos = new Point(0,0);		
					
					//close();
				}
			}
			
		}
		
	}
	
	public void touchActionDown(int x, int y)
	{
		if(!close)
		{
			if(!messageBox.Closed())
			{
				messageBox.touchActionDown(x, y);
			}				
			else
			{
				menu.touchActionDown(x, y);
			}
			
		}
		
	}
	
	

}
