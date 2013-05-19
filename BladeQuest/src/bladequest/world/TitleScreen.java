package bladequest.world;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import bladequest.UI.ListBox;
import bladequest.UI.MenuPanel.Anchors;
import bladequest.UI.SaveLoadMenu;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.Scene;
import bladequest.system.DataLoadThread;


public class TitleScreen 
{
	private Bitmap dapperlogo;
	private Rect logodest;
	private Scene whitescene;
	private Paint paint, greyPaint;
	private TitleStates state;
	private ListBox menu;
	
	private DataLoadThread dataThread;
	private long startTime;
	private final float logoViewTime = 7.0f;
	private final int openspeed = 15;
	private final String openingSong = "aramis";
	
	private boolean skippedIntro;
	
	private BattleAnim playingAnim;
	
	public TitleScreen()
	{
		state = TitleStates.NotStarted;			

		dapperlogo = Global.bitmaps.get("dapperhatlogo");		
		whitescene = Global.scenes.get("white");
		
		paint = Global.textFactory.getTextPaint(13, Color.WHITE, Align.CENTER);		
		greyPaint = Global.textFactory.getTextPaint(13, Color.GRAY, Align.CENTER);		
		
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
		skippedIntro = false;
		
		Global.fc1r = 0;
		Global.fc1g = 0;
		Global.fc1b = 200;
		Global.fc2r = 0;
		Global.fc2g = 0;
		Global.fc2b = 100;	
		
		Global.screenFader.setFadeColor(255, 255, 255, 255);
		
		
		startTime = System.currentTimeMillis();
		
		dataThread = new DataLoadThread();
		dataThread.run();
		
		if(!whitescene.isLoaded())
			whitescene.load();
		
	}
	
	private void changeState(TitleStates newState)
	{
		switch(newState)
		{
		case Company:
			break;
		case GameLogo:	
			Global.musicBox.play(openingSong, true, -1);
			playingAnim = Global.playAnimation("title", null, null);
			break;
		case CompanyTransition:
			Global.screenFader.fadeOut(2);
			break;
		case Menu:
			
			Global.clearAnimations();
			playingAnim = Global.playAnimation("titleloop", null, null);
			Global.screenFader.clear();
			
			skippedIntro = true;
			
			buildMenu();
			menu.open();			
			break;
		case MenuTransition:
			Global.saveLoadMenu.open(SaveLoadMenu.LOADING);
			Global.screenFader.setFadeColor(255, 0, 0, 0);
			Global.screenFader.fadeOut(4);
			menu.close();
			break;
		case LoadMenu:
			Global.screenFader.fadeIn(4);
			break;
		case NewgameTransition:
			menu.close();
			Global.screenFader.setFadeColor(255, 0, 0, 0);
			Global.screenFader.fadeOut(1);
			break;
		default:
			break;
		}
		
		state = newState;
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
		Global.screenFader.setFadeColor(255, 0, 0, 0);
		Global.musicBox.play("", false, -1);
		whitescene.unload();
		Global.clearAnimations();
	}
		
	private void handleOption(String opt)
	{
		if(opt.equals("new"))
		{
			changeState(TitleStates.NewgameTransition);
		}
		else if(opt.equals("con"))
		{
			if(Global.saveLoader.getSaves().size() > 0)
				changeState(TitleStates.MenuTransition);
		}
		else if(opt.equals("quit"))
		{
			menu.close();
			Global.closeGame();
		}
	}
	
	public void update()
	{
		logodest = new Rect(
				Global.vpToScreenX((int)((Global.vpWidth - dapperlogo.getWidth())/2)),
				Global.vpToScreenY((int)((Global.vpHeight - dapperlogo.getHeight())/2)),
				Global.vpToScreenX((int)((Global.vpWidth - dapperlogo.getWidth())/2) + dapperlogo.getWidth()),
				Global.vpToScreenY((int)((Global.vpHeight - dapperlogo.getHeight())/2) + dapperlogo.getHeight()));

		
		switch(state)
		{
		case Company:			
			float elapsed = (System.currentTimeMillis() - startTime)/1000.0f;		
			
			if(dataThread.isDone() && elapsed >= logoViewTime)
				changeState(TitleStates.CompanyTransition);				
			
			break;
		case CompanyTransition:
			if(Global.screenFader.isDone())
			{
				Global.screenFader.fadeIn(2);
				changeState(TitleStates.GameLogo);
			}
			break;
		case GameLogo:
			if(playingAnim.Done())
				changeState(TitleStates.Menu);
			
			if(menu != null && !menu.Closed())
				menu.update();
			break;
		case Menu:
			menu.update();
			
			break;
		case MenuTransition:
			menu.update();
			if(Global.screenFader.isFadedOut())
				changeState(TitleStates.LoadMenu);
			

			break;
		case LoadMenu:
			Global.saveLoadMenu.update();
			if(Global.saveLoadMenu.isClosed())
			{
				state = TitleStates.Menu;
				Global.screenFader.setFadeColor(255, 0, 0, 0);
				Global.screenFader.fadeIn(4);
				buildMenu();
				menu.open();
			}			
			break;
		case NewgameTransition:
			menu.update();
			if(Global.screenFader.isDone())	
			{
				close();
				Global.NewGame();
			}
				

			break;
		default:
			break;
		}
		
	}
	
	public void render()
	{
		switch(state)
		{
		case Company:
		case CompanyTransition:
			whitescene.render();
			Global.renderer.drawBitmap(dapperlogo, null, logodest, null);			
			break;
		case GameLogo:
		case NewgameTransition:
		case MenuTransition:
		case Menu:
			Global.renderAnimations();
			
			if(menu != null && !menu.Closed())
				menu.render();
			
			break;
		case LoadMenu:
			Global.saveLoadMenu.render();
			break;
		default:
			break;
		}
		
		Global.screenFader.render();
	}
	
	public void onLongPress()
	{
		switch(state)
		{
		case LoadMenu:
			Global.saveLoadMenu.onLongPress();
			break;
		default:
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
		default:
			break;
		}
		
	}
	public void touchActionUp(int x, int y)
	{
		switch(state)
		{
		case Company:
			if(dataThread.isDone())
				changeState(TitleStates.CompanyTransition);
			break;
		case GameLogo:	
			if(!skippedIntro)
			{
				Global.musicBox.play("", false, -1);
				Global.musicBox.play(openingSong, false, -1);
				
			}
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
			default:
				break;
			}
			
			break;
		case LoadMenu:
			Global.saveLoadMenu.touchActionUp(x, y);
			break;
		default:
			break;
		}
	}
	public void touchActionDown(int x, int y)
	{
		switch(state)
		{
		case Menu:
			menu.touchActionDown(x, y);
			break;
		case LoadMenu:
			Global.saveLoadMenu.touchActionDown(x, y);
			break;
		default:
			break;
		}
		
	}	
	private enum TitleStates
	{
		NotStarted,
		Company,
		CompanyTransition,
		GameLogo,
		Menu,
		MenuTransition,
		NewgameTransition,
		LoadMenu
	}

}
