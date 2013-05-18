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
	public List<Integer> keyEvents;
	public BqPanel panel;
	
	private GestureDetector gestureScanner;

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        touchEvents = new ArrayList<MotionEvent>();
        keyEvents = new ArrayList<Integer>();
        
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
		MotionEvent newEvent = MotionEvent.obtain(event);
		touchEvents.add(newEvent);		

    	return gestureScanner.onTouchEvent(event);
    }   
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
	{ 
		keyEvents.add(keyCode);
		
    	
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
		//Global.logMessage("Down");
    	//viewA.setText("-" + "DOWN" + "-");
    	return true;
    }
    
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
    	//Global.logMessage("Fling");
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
    	//Global.logMessage("LongPress");
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
    	//Global.logMessage("Scroll");
    	return true;
    }
    
    @Override
    public void onShowPress(MotionEvent e)
    {
    	//viewA.setText("-" + "SHOW PRESS" + "-");
    	//Global.logMessage("ShowPress");
    }    
    
    @Override
    public boolean onSingleTapUp(MotionEvent e)    
    {
    	//viewA.setText("-" + "SINGLE TAP UP" + "-");
    	//Global.logMessage("SingleTapUp");
    	return true;
    }
    
    
}