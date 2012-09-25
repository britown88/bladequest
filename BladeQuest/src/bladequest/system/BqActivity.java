package bladequest.system;

import bladequest.world.Global;
import bladequest.world.States;

import android.app.*;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Window;
import android.view.WindowManager;
import android.content.Context;

import java.util.*;

public class BqActivity extends Activity 
implements OnGestureListener, AudioManager.OnAudioFocusChangeListener
{
    /** Called when the activity is first created. */
	
	private static final String TAG = BqActivity.class.getSimpleName();
	public List<MotionEvent> touchEvents;
	public BqPanel panel;
	
	private GestureDetector gestureScanner;

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        touchEvents = new ArrayList<MotionEvent>();
        
        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // set our MainGamePanel as the View
        
        gestureScanner = new GestureDetector(this);
        
        Global.audioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        Global.audioMgr.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
           
        Global.activity = this;
        Global.startGame();
        
        panel = new BqPanel(this);
        setContentView(panel);
        Global.panel = panel;
        
    }
	
	public void onAudioFocusChange(int focusChange) 
	{
        // Do something based on focus change...
		switch (focusChange) {
        case AudioManager.AUDIOFOCUS_GAIN:
        	setVolumeControlStream(AudioManager.STREAM_MUSIC);
        	break;
		}

    }

	
	@Override
    public boolean onTouchEvent(MotionEvent event) 
    {
		if(touchEvents.isEmpty())
			touchEvents.add(event);
    	return gestureScanner.onTouchEvent(event);
    }   
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
	{ 
		switch(keyCode)
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
			case GS_BATTLETEST:
				Global.battleTest.backButtonPressed();
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
			}
			break;
			
		case KeyEvent.KEYCODE_MENU:
			switch(Global.GameState)
			{
			case GS_WORLDMOVEMENT:
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
	        return true;
	        
	    case KeyEvent.KEYCODE_VOLUME_DOWN:
	    	Global.audioMgr.adjustStreamVolume(AudioManager.STREAM_MUSIC,
	                AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
	        return true;

		}
    	
        return true;
	}
	
	@Override
	protected void onResume() {
		panel.resume();
		super.onResume();
		if(Global.playTimer != null)
			Global.playTimer.start();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		panel.pause();
		if(Global.playTimer != null)
			Global.playTimer.stop();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "Destroying...");
		panel.destroyContext();
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "Stopping...");
		panel.destroyContext();
		super.onStop();
	}
	
	
	//gesture events
	@Override
    public boolean onDown(MotionEvent e)
    {
    	//viewA.setText("-" + "DOWN" + "-");
    	return true;
    }
    
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
    	switch(Global.GameState)
    	{
    	case GS_MAINMENU:
    		Global.menu.onFling(velocityX, velocityY);
    		break;
    	}
    	return true;
    }
    
    @Override
    public void onLongPress(MotionEvent e)
    {
    	switch(Global.GameState)
    	{
    	case GS_TITLE:
    		Global.title.onLongPress();
    		break;
    	}
    }
    
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
    {
    	//viewA.setText("-" + "SCROLL" + "-");
    	return true;
    }
    
    @Override
    public void onShowPress(MotionEvent e)
    {
    	//viewA.setText("-" + "SHOW PRESS" + "-");
    }    
    
    @Override
    public boolean onSingleTapUp(MotionEvent e)    
    {
    	//viewA.setText("-" + "SINGLE TAP UP" + "-");
    	return true;
    }
    
    
}