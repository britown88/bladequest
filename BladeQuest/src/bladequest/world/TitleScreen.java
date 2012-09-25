package bladequest.world;

import bladequest.UI.ListBox;
import bladequest.UI.MenuPanel.Anchors;
import bladequest.UI.SaveLoadMenu;
import bladequest.graphics.Scene;
import bladequest.system.DataLoadThread;

import android.graphics.*;
import android.graphics.Paint.Align;


public class TitleScreen 
{
	private Bitmap titleMain, titleSub, dapperlogo;
	private float titleScale, scaleAccel;
	private Rect dest, logodest;
	private int titleWidth, titleHeight;
	private Scene titlescene, whitescene;
	private Paint paint, greyPaint;
	private TitleStates state, oldState;
	private ListBox menu;
	
	private DataLoadThread dataThread;
	private long startTime;
	private final float logoViewTime = 7.0f;
	private final int openspeed = 15;
	private final String openingSong = "aramis";
	
	public TitleScreen()
	{
		state = TitleStates.NotStarted;		
		
		titleMain = Global.bitmaps.get("titlemain");
		titleSub = Global.bitmaps.get("titlesub");
		dapperlogo = Global.bitmaps.get("dapperhatlogo");
		
		titleWidth = titleMain.getWidth();
		titleHeight = titleMain.getHeight();		
		
		//titlescene = Global.scenes.get("title");
		titlescene = Global.scenes.get("white");
		
		whitescene = Global.scenes.get("white");
		
		paint = Global.textFactory.getTextPaint(13, Color.WHITE, Align.CENTER);		
		greyPaint = Global.textFactory.getTextPaint(13, Color.GRAY, Align.CENTER);		
		
		dest = new Rect();
		logodest = new Rect();
		
	}
	
	public void titleStart()
	{
		changeState(TitleStates.Company);
		
		//reset options to default to default
		Global.textSpeed = 1;
		Global.stretchScreen = true;
		if(Global.panel != null)
			Global.panel.scaleImage();
		
		Global.fc1r = 0;
		Global.fc1g = 0;
		Global.fc1b = 200;
		Global.fc2r = 0;
		Global.fc2g = 0;
		Global.fc2b = 100;		
		
		Global.screenFader.setFadeColor(255, 0, 0, 0);
		Global.screenFader.setFaded();	
		Global.screenFader.fadeIn(4);
		
		titleScale = 30.0f;	
		scaleAccel = 0.05f;
		
		startTime = System.currentTimeMillis();
		
		dataThread = new DataLoadThread();
		dataThread.run();
		
		if(!titlescene.isLoaded())
			titlescene.load();
		if(!whitescene.isLoaded())
			whitescene.load();
		
	}
	
	private void changeState(TitleStates newState)
	{
		oldState = state;
		switch(newState)
		{
		case Company:
			break;
		case GameLogo:	
			Global.screenFader.fadeOut(2);
			Global.musicBox.play(openingSong, true, -1);
			Global.playAnimation("title", null, null);
			break;
		case Menu:
			titleScale = 2.0f;
			updateTitleSize();
			buildMenu();
			menu.open();			
			break;
		case MenuTransition:
			Global.saveLoadMenu.open(SaveLoadMenu.LOADING);
			menu.close();
			Global.screenFader.fadeOut(4);
			break;
		case LoadMenu:
			Global.screenFader.fadeIn(4);
			
			break;
		}
		
		state = newState;
	}
	
	private void handleOption(String opt)
	{
		if(opt.equals("new"))
		{
			close();
			Global.NewGame();
		}
		else if(opt.equals("con"))
		{
			if(Global.saveLoader.getSaves().size() > 0)
				changeState(TitleStates.MenuTransition);
		}
		else if(opt.equals("quit"))
		{
			close();
			Global.closeGame();
		}
	}
	
	public void update()
	{
		switch(state)
		{
		case Company:			
			float elapsed = (System.currentTimeMillis() - startTime)/1000.0f;		
			logodest = new Rect(
					Global.vpToScreenX((int)((Global.vpWidth - dapperlogo.getWidth())/2)),
					Global.vpToScreenY((int)((Global.vpHeight - dapperlogo.getHeight())/2)),
					Global.vpToScreenX((int)((Global.vpWidth - dapperlogo.getWidth())/2) + dapperlogo.getWidth()),
					Global.vpToScreenY((int)((Global.vpHeight - dapperlogo.getHeight())/2) + dapperlogo.getHeight()));

			if(dataThread.isDone() && elapsed >= logoViewTime)
				changeState(TitleStates.GameLogo);
				
			
			break;
		case GameLogo:
			if(Global.screenFader.isDone())
			{
				Global.screenFader.clear();
				if(titleScale > 2.0f)
				{
					titleScale -= scaleAccel;
					scaleAccel += .0001;
				}
				else
					titleScale = 2.0f;
				
				updateTitleSize();					
			}
			if(menu != null && !menu.Closed())
				menu.update();
			break;
		case Menu:
			Global.screenFader.clear();
			menu.update();
			
			break;
		case MenuTransition:
			menu.update();
			if(Global.screenFader.isDone())				
				changeState(TitleStates.LoadMenu);

			break;
		case LoadMenu:
			Global.saveLoadMenu.update();
			if(Global.saveLoadMenu.isClosed())
			{
				state = TitleStates.Menu;
				buildMenu();
				menu.open();
			}			
			break;
		}
		
		
		
	}
	
	private void buildMenu()
	{
		menu = new ListBox(
				Global.vpWidth / 2, 
				Global.vpHeight - 48, 
				(int)(Global.vpWidth*0.25f), 0, 3, 1, paint);
		menu.setOpenSize(menu.width, 96);
		menu.anchor = Anchors.TrueCenter;
		menu.openSpeed = openspeed;
		menu.setDisabledPaint(greyPaint);
		
		menu.addItem("New Game", "new", false);
		menu.addItem("Continue", "con", !Global.saveLoader.hasSaves());
		menu.addItem("Quit", "quit", false);
	}
	
	private void close()
	{
		Global.musicBox.play("", false, -1);
		whitescene.unload();
		titlescene.unload();
		Global.clearAnimations();
	}
	
	private void updateTitleSize()
	{
		dest = new Rect(
				Global.vpToScreenX((int)((Global.vpWidth - (titleWidth*titleScale))/2)),
				Global.vpToScreenY((int)((Global.vpHeight - (titleHeight*titleScale))/2)),
				Global.vpToScreenX((int)((Global.vpWidth - (titleWidth*titleScale))/2) + (int)(titleWidth*titleScale)),
				Global.vpToScreenY((int)((Global.vpHeight - (titleHeight*titleScale))/2) + (int)(titleHeight*titleScale)));
		
	}
	
	public void render()
	{
		switch(state)
		{
		case Company:
			//Global.renderer.drawColor(Color.WHITE);
			whitescene.render();
			Global.renderer.drawBitmap(dapperlogo, null, logodest, null);			
			break;
		case GameLogo:
			if(!Global.screenFader.isDone() && oldState == TitleStates.Company)
			{
				whitescene.render();
				Global.renderer.drawBitmap(dapperlogo, null, logodest, null);
			}
			else
			{
				titlescene.render();				
				
				Global.renderer.drawBitmap(titleMain, null, dest, null);
				if(titleScale == 2.0f)
				{
					Global.renderer.drawBitmap(titleSub, null, dest, null);
					//if(menu == null || menu.Closed())
					Global.renderer.drawText("Tap to Start!", 
						Global.vpToScreenX(Global.vpWidth / 2), 
						Global.vpToScreenY(Global.vpHeight - 32), paint);					
				}
				
				if(menu != null && !menu.Closed())
					menu.render();
			}
			
			Global.renderAnimations();
			break;
		case MenuTransition:
		case Menu:
			titlescene.render();
			Global.renderer.drawBitmap(titleMain, null, dest, null);
			Global.renderer.drawBitmap(titleSub, null, dest, null);
			if(!menu.Closed())
				menu.render();
			
			break;

		case LoadMenu:
			Global.saveLoadMenu.render();
			break;
		}
		//canvas.drawBitmap(titleMain, Global.vpToScreenX(0), Global.vpToScreenY(0), null);
	}
	
	public void onLongPress()
	{
		switch(state)
		{
		case LoadMenu:
			Global.saveLoadMenu.onLongPress();
			break;
		}
	}
	
	public void backButtonPressed()
	{
		switch(state)
		{
		case LoadMenu:
			Global.saveLoadMenu.backButtonPressed();
			break;
		case Menu:
			this.state = TitleStates.GameLogo;
			menu.close();
			break;
		default:
			Global.closeGame();
			break;
			
		}
	}
	
	public void touchActionMove(int x, int y)
	{
		switch(state)
		{
		case Company:
			break;
		case GameLogo:			
			break;
		case Menu:
			menu.touchActionMove(x, y);
			break;
		case LoadMenu:
			Global.saveLoadMenu.touchActionMove(x, y);
			break;
		}
		
	}
	public void touchActionUp(int x, int y)
	{
		switch(state)
		{
		case Company:
			changeState(TitleStates.GameLogo);
			break;
		case GameLogo:
			if(titleScale > 2.0f)
			{
				titleScale = 2.0f;
				Global.musicBox.play(openingSong, false, -1);
			}
			else	
				changeState(TitleStates.Menu);	

			break;
		case Menu:
			
			ListBox.LBStates state = menu.touchActionUp(x, y);
			
			switch(state)
			{
			case Selected:
				handleOption((String)menu.getSelectedEntry().obj);
				break;
			case Close:
				this.state = TitleStates.GameLogo;
				menu.close();
				break;
			}
			
			break;
		case LoadMenu:
			Global.saveLoadMenu.touchActionUp(x, y);
			break;
		}
	}
	public void touchActionDown(int x, int y)
	{
		switch(state)
		{
		case Company:
			break;
		case GameLogo:
			break;
		case Menu:
			menu.touchActionDown(x, y);
			break;
		case LoadMenu:
			Global.saveLoadMenu.touchActionDown(x, y);
			break;
		}
		
	}	
	private enum TitleStates
	{
		NotStarted,
		Company,
		GameLogo,
		Menu,
		MenuTransition,
		LoadMenu
	}

}
