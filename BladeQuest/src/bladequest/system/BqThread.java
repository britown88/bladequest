package bladequest.system;

import android.view.MotionEvent;
import bladequest.UI.ListBox.LBStates;
import bladequest.world.Global;
import bladequest.world.States;

import java.util.*;

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
	    	Global.update();
	    	
	    	handleInput();  	
	    	
	    	gamePanel.onDraw();
	    	
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
				{
					events[i] =  Global.activity.touchEvents.get(i);
//					switch (events[i].getAction())
//			    	{
//			    	case MotionEvent.ACTION_DOWN:
//			    		Global.logMessage("Read  Down " + Global.activity.touchEvents.size());
//			    		break;
//			    	case MotionEvent.ACTION_UP:
//			    		Global.logMessage("Read  Up   " + Global.activity.touchEvents.size());   		
//			    		break;
//			    	case MotionEvent.ACTION_MOVE:
//			    		Global.logMessage("Read  Move " + Global.activity.touchEvents.size());
//			    		break;
//			    	}
				}
					
				
				for(MotionEvent me : events)
				{
					onTouchEvent(me);
					Global.activity.touchEvents.remove(me);
					me.recycle();
				}	
			}			
    	}
	}
	
	private void onTouchEvent(MotionEvent event) 
    {
		if(event != null)
		{
			switch (event.getAction())
	    	{
	    	case MotionEvent.ACTION_DOWN:
	    		Global.logMessage("Acted Down " + Global.activity.touchEvents.size());
	    		break;
	    	case MotionEvent.ACTION_UP:
	    		Global.logMessage("Acted Up   " + Global.activity.touchEvents.size());   		
	    		break;
	    	case MotionEvent.ACTION_MOVE:
	    		Global.logMessage("Acted Move " + Global.activity.touchEvents.size());
	    		break;
	    	}
			
			
			int x = (int)event.getX();
			int y =	(int)event.getY();
			
			//scale x and y coords to screen scale:		
			if(x > Global.screenWidth / 2)
				x = (int)((float)(x-Global.screenWidth/2.0F)/(Global.imageScale+Global.baseScale))+(int)(Global.screenWidth/2.0F);
			if(x < Global.screenWidth / 2)
				x = (int)(Global.screenWidth / 2.0F) - (int)(((Global.screenWidth/2.0F) - (float)x)/(Global.imageScale+Global.baseScale));
			if(y > Global.screenHeight / 2)
				y = (int)((float)(y-Global.screenHeight/2.0F)/(Global.imageScale+Global.baseScale))+(int)(Global.screenHeight/2.0F);
			if(y < Global.screenHeight / 2)
				y = (int)(Global.screenHeight / 2.0F) - (int)(((Global.screenHeight/2.0F) - (float)y)/(Global.imageScale+Global.baseScale));
			
	    	switch(Global.GameState)
	    	{
	    	case GS_TITLE:
	    		
	    		switch (event.getAction())
	        	{
	        	case MotionEvent.ACTION_DOWN:
	        		Global.title.touchActionDown(x, y);
	        		break;
	        	case MotionEvent.ACTION_UP:
	        		Global.title.touchActionUp(x, y);    		
	        		break;
	        	case MotionEvent.ACTION_MOVE:
	        		Global.title.touchActionMove(x, y);
	        		break;
	        	}
	    		break;
	    	case GS_WORLDMOVEMENT:
	    		switch (event.getAction())
	        	{
	        	case MotionEvent.ACTION_DOWN:        		
	        		if(Global.worldMsgBox != null) 
	        			Global.worldMsgBox.touchActionDown(x, y);

	    			if(Global.debugButton != null)
	    			{
	    				Global.debugButton.touchActionDown(x, y);
	    				Global.debugButton.clearSelectedEntry();
	    				
	    				//if(Global.debugButton.getSelectedEntry() == null)
	            		Global.updateMousePosition(x, y, true);
	    			}
	    			else
	    				Global.updateMousePosition(x, y, true);
	    			
	        		//Global.playTestSound();
	        		
	        		break;
	        	case MotionEvent.ACTION_MOVE:
	        		if(Global.worldMsgBox != null) 
	        			Global.worldMsgBox.touchActionMove(x, y);

	    			if(Global.debugButton != null)
	    			{
	    				Global.debugButton.touchActionMove(x, y);
	    				if(Global.debugButton.getSelectedEntry() == null)
	            			Global.updateMousePosition(x, y, false);
	    			}
	    			else
	    				Global.updateMousePosition(x, y, false);
	    			
	        		//Global.playTestSound();
	        		
	        		break;
	        	case MotionEvent.ACTION_UP:
	        		if(Global.worldMsgBox != null) 
	        			Global.worldMsgBox.touchActionUp(x, y);
	        		
	        		if(Global.debugButton != null && Global.debugButton.touchActionUp(x, y) == LBStates.Selected)
	        			Global.openDebugMenu();
	        		break;
	        	}
	    		break;
	    		
	    	case GS_BATTLE:
	    		switch (event.getAction())
	        	{
	        	case MotionEvent.ACTION_DOWN:
	        		Global.battle.touchActionDown(x, y);
	        		break;
	        		
	        	case MotionEvent.ACTION_UP:
	        		Global.battle.touchActionUp(x, y);
	        		break;
	        		
	        	case MotionEvent.ACTION_MOVE:
	        		Global.battle.touchActionMove(x, y);
	        		break;
	        	}
	    		break;
	    	case GS_MAINMENU:
	    		switch (event.getAction())
	        	{
	        	case MotionEvent.ACTION_DOWN:
	        		Global.menu.touchActionDown(x, y);
	        		break;
	        		
	        	case MotionEvent.ACTION_UP:
	        		Global.menu.touchActionUp(x, y);
	        		break;
	        		
	        	case MotionEvent.ACTION_MOVE:
	        		Global.menu.touchActionMove(x, y);
	        		break;
	        	}
	    		break;
	    		
	    	case GS_SAVELOADMENU:
	    		switch (event.getAction())
	        	{
	        	case MotionEvent.ACTION_DOWN:
	        		Global.saveLoadMenu.touchActionDown(x, y);
	        		break;
	        		
	        	case MotionEvent.ACTION_UP:
	        		Global.saveLoadMenu.touchActionUp(x, y);
	        		break;
	        		
	        	case MotionEvent.ACTION_MOVE:
	        		Global.saveLoadMenu.touchActionMove(x, y);
	        		break;
	        	}
	    		break;
	    	case GS_MERCHANT:
	    		switch (event.getAction())
	        	{
	        	case MotionEvent.ACTION_DOWN:
	        		Global.merchantScreen.touchActionDown(x, y);
	        		break;
	        		
	        	case MotionEvent.ACTION_UP:
	        		Global.merchantScreen.touchActionUp(x, y);
	        		break;
	        		
	        	case MotionEvent.ACTION_MOVE:
	        		Global.merchantScreen.touchActionMove(x, y);
	        		break;
	        	}
	    		break;
	    	case GS_DEBUG:
	    		switch (event.getAction())
	        	{
	        	case MotionEvent.ACTION_DOWN:
	        		Global.debugScreen.touchActionDown(x, y);
	        		break;
	        		
	        	case MotionEvent.ACTION_UP:
	        		Global.debugScreen.touchActionUp(x, y);
	        		break;
	        		
	        	case MotionEvent.ACTION_MOVE:
	        		Global.debugScreen.touchActionMove(x, y);
	        		break;
	        	}
	    		break;
	    	case GS_NAMESELECT:
	    		switch (event.getAction())
	        	{
	        	case MotionEvent.ACTION_DOWN:
	        		Global.nameSelect.touchActionDown(x, y);
	        		break;
	        		
	        	case MotionEvent.ACTION_UP:
	        		Global.nameSelect.touchActionUp(x, y);
	        		break;
	        		
	        	case MotionEvent.ACTION_MOVE:
	        		Global.nameSelect.touchActionMove(x, y);
	        		break;
	        	}
	    		break;
	    		
	    	case GS_SHOWSCENE:
	    		switch (event.getAction())
	        	{        		
	        	case MotionEvent.ACTION_UP:
	        		Global.showScene.unload();
	        		Global.GameState = States.GS_WORLDMOVEMENT;
	        		break;
	        	}
	    		break;
	    		
	    	}
		}
		
    }   

}
