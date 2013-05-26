package bladequest.system;

import android.graphics.Point;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import bladequest.UI.ListBox.LBStates;
import bladequest.world.Global;
import bladequest.world.States;

public class BqThread extends Thread
{
	private BqPanel gamePanel;
	private boolean running;
	public void setRunning(boolean running) {
		this.running = running;
	}
	public BqThread(BqPanel gamePanel)
	{
		super();
		this.gamePanel = gamePanel;
		
	}
	
	@Override
	public void run() 
	{
		long startTime, frameTime;
		int sleepTime;
		
		
		while(running)
		{
	    	startTime = System.currentTimeMillis();
	    	
	    	handleInput();  
	    	Global.update();	
	    	gamePanel.draw();
	    	
	    	Global.lock.lock();
	    	Global.renderer.swap();	    	
	    	Global.lock.unlock();

			frameTime = System.currentTimeMillis() - startTime;
			sleepTime = (int)(Global.FRAME_PERIOD - frameTime);					
			if(sleepTime > 0)
				try{sleep(sleepTime);}
					catch (InterruptedException e) {}
		}
		
	}
	
	private void handleInput()
	{
		
		if(Global.activity.touchEvents != null)
    	{
			int size = Global.activity.touchEvents.size();
			
			if(size > 0)
			{
				MotionEvent[] events = new MotionEvent[size];
				
				for(int i = 0; i < size; ++i)
					events[i] =  Global.activity.touchEvents.get(i);					
				
				for(MotionEvent me : events)
				{
					onTouchEvent(me);
					Global.activity.touchEvents.remove(me);
					me.recycle();
				}	
			}			
    	}
		
		if(Global.activity.keyEvents != null)
    	{
			int size = Global.activity.keyEvents.size();
			
			if(size > 0)
			{
				int[] events = new int[size];
				
				for(int i = 0; i < size; ++i)
					events[i] =  Global.activity.keyEvents.get(i);					
				
				for(int ke : events)
				{
					onKeyEvent(ke);
					Global.activity.keyEvents.remove((Integer)ke);
				}	
			}			
    	}
	}
	
	private void onKeyEvent(int event)
	{
		switch(event)
		{
		case KeyEvent.KEYCODE_BACK:
			switch(Global.GameState)
			{
			case GS_WORLDMOVEMENT:
				break;
			case GS_TITLE:
				Global.title.backButtonPressed();
				break;
			case GS_BATTLE:
				Global.battle.backButtonPressed();
				break;
			case GS_MAINMENU:			
				
				Global.menu.backButtonPressed();
				break;	
			case GS_MERCHANT:
				Global.merchantScreen.backButtonPressed();
				break;
			case GS_DEBUG:
				Global.debugScreen.backButtonPressed();
				break;
			case GS_SAVELOADMENU:				
				Global.saveLoadMenu.backButtonPressed();
				break;
			default:
				break;
			}
			break;
			
		case KeyEvent.KEYCODE_MENU:
			switch(Global.GameState)
			{
			case GS_WORLDMOVEMENT:
				if(Global.menuButton.Opened())
					Global.openMainMenu();
				break;
			case GS_MAINMENU:
				Global.closeMainMenu();
				break;
				
			default:
                break;					
			}
			break;
			
	    case KeyEvent.KEYCODE_VOLUME_UP:
	    	Global.audioMgr.adjustStreamVolume(AudioManager.STREAM_MUSIC,
	                AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
	    	break;
	        
	    case KeyEvent.KEYCODE_VOLUME_DOWN:
	    	Global.audioMgr.adjustStreamVolume(AudioManager.STREAM_MUSIC,
	                AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
	        break;

		}
		
	}
	
	private void onTouchEvent(MotionEvent event) 
    {
		if(event != null)
		{			
			Point p = Global.inputToScreen(event.getX(), event.getY());
			
	    	switch(Global.GameState)
	    	{
	    	case GS_TITLE:
	    		
	    		switch (event.getAction())
	        	{
	        	case MotionEvent.ACTION_DOWN:
	        		Global.title.touchActionDown(p.x, p.y);
	        		break;
	        	case MotionEvent.ACTION_UP:
	        		Global.title.touchActionUp(p.x, p.y);    		
	        		break;
	        	case MotionEvent.ACTION_MOVE:
	        		Global.title.touchActionMove(p.x, p.y);
	        		break;
	        	}
	    		break;
	    	case GS_WORLDMOVEMENT:
	    		switch (event.getAction())
	        	{
	        	case MotionEvent.ACTION_DOWN:        		
	        		if(Global.worldMsgBox != null) 
	        			Global.worldMsgBox.touchActionDown(p.x, p.y);

	    			if(Global.debugButton != null)
	    			{
	    				Global.debugButton.touchActionDown(p.x, p.y);
	    				Global.debugButton.clearSelectedEntry();
	    			}
	    			
	    			if(Global.party.allowMovement() && Global.menuButton.Opened())
	    				Global.menuButton.touchActionDown(p.x, p.y);	    				

	            	Global.updateMousePosition(p.x, p.y, true);

	    			
	        		//Global.playTestSound();
	        		
	        		break;
	        	case MotionEvent.ACTION_MOVE:
	        		if(Global.worldMsgBox != null) 
	        			Global.worldMsgBox.touchActionMove(p.x, p.y);

	    			if(Global.debugButton != null)
	    				Global.debugButton.touchActionMove(p.x, p.y);

	    			if(Global.party.allowMovement() && Global.menuButton.Opened())
	    				Global.menuButton.touchActionDown(p.x, p.y);	
	    			
	    			Global.updateMousePosition(p.x, p.y, false);
	    			
	        		//Global.playTestSound();
	        		
	        		break;
	        	case MotionEvent.ACTION_UP:
	        		if(Global.worldMsgBox != null) 
	        		{
	        			Global.worldMsgBox.touchActionUp(p.x, p.y);
	        			if(Global.worldMsgBox.Closing() && Global.party.allowMovement())
	        				Global.menuButton.open();
	        		}
	        			
	        		
	        		if(Global.debugButton != null && Global.debugButton.contains(p.x, p.y) && Global.debugButton.touchActionUp(p.x, p.y) == LBStates.Selected)
	        			Global.openDebugMenu();
	        		
	        		if(Global.party.allowMovement() && Global.menuButton.contains(p.x, p.y) 
	        				&& Global.menuButton.touchActionUp(p.x, p.y) == LBStates.Selected
	        				&& Global.menuButton.Opened())
	    				Global.openMainMenu();
	        		break;
	        	}
	    		break;
	    		
	    	case GS_BATTLE:
	    		switch (event.getAction())
	        	{
	        	case MotionEvent.ACTION_DOWN:
	        		Global.battle.touchActionDown(p.x, p.y);
	        		break;
	        		
	        	case MotionEvent.ACTION_UP:
	        		Global.battle.touchActionUp(p.x, p.y);
	        		break;
	        		
	        	case MotionEvent.ACTION_MOVE:
	        		Global.battle.touchActionMove(p.x, p.y);
	        		break;
	        	}
	    		break;
	    	case GS_MAINMENU:
	    		switch (event.getAction())
	        	{
	        	case MotionEvent.ACTION_DOWN:
	        		Global.menu.touchActionDown(p.x, p.y);
	        		break;
	        		
	        	case MotionEvent.ACTION_UP:
	        		Global.menu.touchActionUp(p.x, p.y);
	        		break;
	        		
	        	case MotionEvent.ACTION_MOVE:
	        		Global.menu.touchActionMove(p.x, p.y);
	        		break;
	        	}
	    		break;
	    		
	    	case GS_SAVELOADMENU:
	    		switch (event.getAction())
	        	{
	        	case MotionEvent.ACTION_DOWN:
	        		Global.saveLoadMenu.touchActionDown(p.x, p.y);
	        		break;
	        		
	        	case MotionEvent.ACTION_UP:
	        		Global.saveLoadMenu.touchActionUp(p.x, p.y);
	        		break;
	        		
	        	case MotionEvent.ACTION_MOVE:
	        		Global.saveLoadMenu.touchActionMove(p.x, p.y);
	        		break;
	        	}
	    		break;
	    	case GS_MERCHANT:
	    		switch (event.getAction())
	        	{
	        	case MotionEvent.ACTION_DOWN:
	        		Global.merchantScreen.touchActionDown(p.x, p.y);
	        		break;
	        		
	        	case MotionEvent.ACTION_UP:
	        		Global.merchantScreen.touchActionUp(p.x, p.y);
	        		break;
	        		
	        	case MotionEvent.ACTION_MOVE:
	        		Global.merchantScreen.touchActionMove(p.x, p.y);
	        		break;
	        	}
	    		break;
	    	case GS_DEBUG:
	    		switch (event.getAction())
	        	{
	        	case MotionEvent.ACTION_DOWN:
	        		Global.debugScreen.touchActionDown(p.x, p.y);
	        		break;
	        		
	        	case MotionEvent.ACTION_UP:
	        		Global.debugScreen.touchActionUp(p.x, p.y);
	        		break;
	        		
	        	case MotionEvent.ACTION_MOVE:
	        		Global.debugScreen.touchActionMove(p.x, p.y);
	        		break;
	        	}
	    		break;
	    	case GS_NAMESELECT:
	    		switch (event.getAction())
	        	{
	        	case MotionEvent.ACTION_DOWN:
	        		Global.nameSelect.touchActionDown(p.x, p.y);
	        		break;
	        		
	        	case MotionEvent.ACTION_UP:
	        		Global.nameSelect.touchActionUp(p.x, p.y);
	        		break;
	        		
	        	case MotionEvent.ACTION_MOVE:
	        		Global.nameSelect.touchActionMove(p.x, p.y);
	        		break;
	        	}
	    		break;
			default:
				break;
	    		
	    	}
		}
		
    }   

}
