package bladequest.system;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import bladequest.graphics.TilePlate;
import bladequest.world.Global;


public class BqPanel extends SurfaceView 
implements SurfaceHolder.Callback
{
	private BqRenderThread renderThread;
	public BqThread updateThread;
	private static final String TAG = BqPanel.class.getSimpleName();
	private Paint blackpaint, logText;
	
	public BqPanel(Context c)
	{
		super(c);		
	}
	
	public BqPanel(Activity activity)
	{
		super(activity);
		//register for callbacks
		getHolder().addCallback(this);	
		renderThread = new BqRenderThread(getHolder());	
		updateThread = new BqThread(this);
		setFocusable(true);	
		blackpaint = new Paint();
		blackpaint.setColor(Color.BLACK);
		
		logText = Global.textFactory.getTextPaint(13, Color.WHITE, Align.LEFT);
	}
	
	//@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    } 

    //@Override
    public void surfaceCreated(SurfaceHolder holder) 
    {
    	Log.d(TAG, "Surface Create");
    	
    	Global.screenWidth = getWidth();
    	Global.screenHeight = getHeight();   
    	
    	scaleImage();
    	
    	if (renderThread.getState() == Thread.State.TERMINATED)
    	{
    		Log.d(TAG, "Thread terminated, creating new...");
    		renderThread = new BqRenderThread(getHolder());
    	}
    	renderThread.setRunning(true);
    	renderThread.start(); 
    	
    	if (updateThread.getState() == Thread.State.TERMINATED)
    	{
    		Log.d(TAG, "Thread terminated, creating new...");
    		updateThread = new BqThread(this);
    	}    	
    	updateThread.setRunning(true);
    	updateThread.start();
    	   	
    }
    
    public void scaleImage()
    {
    	if(Global.stretchScreen)
    	{
        	int deltaX = Global.screenWidth - Global.vpWidth;
        	int deltaY = Global.screenHeight - Global.vpHeight;
        	if(deltaX >= deltaY)
        		Global.baseScale = (float)Global.screenHeight / (float)Global.vpHeight;    		
        	else
        		Global.baseScale = (float)Global.screenWidth / (float)Global.vpWidth;
        	
        	Global.baseScale -= 1.0F;
    	}
    	else
    		Global.baseScale = 0.0f;
    }
    

    //@Override
    public void surfaceDestroyed(SurfaceHolder holder) 
    {
    	destroyContext();
    }
    
    public void resume(){
    	renderThread.setRunning(true);
    	updateThread.setRunning(true);
    	Global.musicBox.systemResume();
    }
    
    public void pause()
    {
    	if(Global.musicBox != null)
    		Global.musicBox.systemPause();
    	renderThread.setRunning(false);
    	updateThread.setRunning(false);
    	
    }
    
    public void destroyContext()
    {
    	Log.d(TAG, "Surface Destroy.");
    	boolean retry = true;
    	while(retry)
    	{
    		//try
    		//{
    			renderThread.setRunning(false);
    			//renderThread.join();
    			updateThread.setRunning(false);
    			//updateThread.join();
    			retry = false;
    		//} 
    		//catch (InterruptedException e)
    		//{
    			//Log.d(TAG, "Thread Join FAILED.");
    		//}
    	}
    }       
    
    //@Override
    protected void draw() 
    {   
    	//canvas.clipRect(global.vpRect);
    	drawBackground();
    	    	
    	switch(Global.GameState)
    	{
    	case GS_TITLE:
    		Global.title.render();
    		
    		Global.screenFader.render();
    		break;
    	case GS_MENUTRANSITION:
    	case GS_WORLDMOVEMENT:
    	case GS_BATTLETRANSITION:
    	case GS_PAUSE:
    		if(Global.map != null)
    			drawWorld();
    		
    		if(!Global.menuButton.Closed())
    			Global.menuButton.render();

    		if(Global.debugButton != null)
    			Global.debugButton.render();
    		
    		
    		break;
    	case GS_BATTLE:
    		Global.battle.render();
    		break;
    	case GS_MAINMENU:
    		Global.menu.render();
    		Global.screenFader.render();
    		
    		break;
    	case GS_SAVELOADMENU:
    		Global.saveLoadMenu.render();
    		Global.screenFader.render();
			break;
    	case GS_LOADING:
    		Global.loadingScreen.render();
    		break;
    	case GS_NAMESELECT:
    		Global.nameSelect.render();
    		Global.screenFader.render();
    		break;
    	case GS_MERCHANT:
    		if(Global.map != null)
    			drawWorld();
    		Global.merchantScreen.render();
    		break;
    	case GS_DEBUG:
    		if(Global.map != null)
    			drawWorld();
    		Global.debugScreen.render();
    		break;
    	}	
    	
    	drawForeground();
    	drawLog();
    	
    }
    

    
    private void drawBackground()
    {    	
    	Global.renderer.drawColor(Color.BLACK);
    }
    
    private void drawLog()
	{
		int i = 0;
				
		if(Global.gameLog != null)
		{
			List<String> events = new ArrayList<String>(Global.gameLog);
			events = events.subList(events.size()-Math.min(10, events.size()), events.size());
			
			for(String str : events)
				Global.renderer.drawText(str, Global.vpToScreenX(0), Global.vpToScreenY(10+i++*20), logText);
		
		}
	}
    
    private void drawWorld()
    {
    	//genBG();
    	//canvas.drawBitmap(Global.background, bgSrcRect, bgDstRect, null);  
    	
    	List<TilePlate> loadList = new ArrayList<TilePlate>();
    	
    	Global.map.renderBackground(loadList);    	    	
    	Global.target.render();  	
    	Global.map.renderBackgroundObjs();  	
    	Global.party.render();      	 
    	Global.map.renderForeground(loadList);
    	Global.map.renderForegroundObjs();
    	Global.renderReactionBubbles();
    	Global.renderAnimations();
    	Global.map.renderDisplayName(); 
    	
    	if(Global.showScene != null)
    		Global.showScene.render();
    	
    	Global.screenFader.render();
    	
    	//test elipses
    	//Global.renderer.drawElipse(new Rect(0,0,50,50), blackpaint);
    	
    	
    	//load plates
    	if(loadList.size() >0)
    		(new PlateLoadThread(loadList)).start();
    	
    	//draw msgbox
    	if(Global.worldMsgBox != null)
    		Global.worldMsgBox.render();
    	
    	

    }
    
    private void drawForeground()
    {   
    	Global.renderer.drawRect(0, 0, ((getWidth() - Global.vpWidth)/2), getHeight(), blackpaint, true);
    	Global.renderer.drawRect(0, 0, getWidth(), ((getHeight() - Global.vpHeight)/2), blackpaint, true);
    	Global.renderer.drawRect(getWidth() - ((getWidth() - Global.vpWidth)/2), 0, getWidth(), getHeight(), blackpaint, true);
    	Global.renderer.drawRect(0, getHeight() - ((getHeight() - Global.vpHeight)/2), getWidth(), getHeight(), blackpaint, true);
    }

}


