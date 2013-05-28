package bladequest.sound;

import java.util.ArrayList;
import java.util.List;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import bladequest.world.Global;
 
 
public class IntroLoopPlayer
{
	
    private Song song;
    private long startTime, duration, pausedAt;
   
    private boolean paused, loop, playIntro, loopHasStarted, prepared, started;
    
    private MediaPlayer mPlayer;
    private int playIndex;
   
    public IntroLoopPlayer()
    {           

    	playIndex = 0;
    	started = false;
    	prepared = false;
    	
    	mPlayer = new MediaPlayer();           
    	mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    	mPlayer.setOnPreparedListener(new OnPreparedListener()
    	{ 
    		@Override
    		public void onPrepared(MediaPlayer mp) {
    			startTime = System.currentTimeMillis();		
    			startTime -= pausedAt;
    			pausedAt = 0;
    			duration = mp.getDuration();
    			mp.start();
    			prepared = true;
    		}                       
    	});
    }

    
    public void playSong(Song song, boolean playIntro, boolean loop, boolean fadeIn)
    {
        this.song = song;                      
        this.loop = loop;
        this.playIntro = playIntro && song.HasIntro();
        loopHasStarted = false;
        
        mPlayer.setVolume(fadeIn?0:1, fadeIn?0:1);
       
        paused = true;//play only runs from paused        
        pausedAt = 0;
        play();
    }
   
    private void prepareAndPlaySong(String path)
    {
        if (started)
        {
            while (!prepared) {}
            started = false;
        }
        mPlayer.reset();               
       
        try {
            AssetFileDescriptor afd = Global.activity.getAssets().openFd(path);            
            mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());
            prepared = false;
            mPlayer.prepareAsync();        
           
            afd.close();
        } catch (Exception e) {Log.d("MEDIAPLAYER", "FUCKED");}
   
        started = true;
        //mPlayer.start();
               
	}
 
	private void onCompletion()
	{
		while (!prepared) {}
		if(playIntro && !loopHasStarted)
		{
			loopHasStarted = true;
			paused = true;
			play();
		}
		else if(loop && loopHasStarted)
		{
			paused = true;
			play();
		}
	}
       
	public void update()
	{
		if(started && prepared && !paused && System.currentTimeMillis() - startTime >= duration)
			onCompletion();
	}
       
	public void play()
	{
		if(paused)
		{ 
			if(playIntro && !loopHasStarted)
				prepareAndPlaySong(song.IntroPath());                   
			else
			{
				loopHasStarted = true;
				prepareAndPlaySong(song.Path());                 
			}
               
			paused = false;                
		}              
	}
       
	public void pause()
	{      
		if (!started) return;
		while (!prepared) {}
		long st = System.currentTimeMillis();
		if(mPlayer.isPlaying())
		{
                mPlayer.pause();
                long d = System.currentTimeMillis() - st;
               
                pausedAt = System.currentTimeMillis() - startTime + d;
                paused = true;
            }              
        }
       
        public void setVolume(float left, float right)
        {
            while (!prepared) {}
            mPlayer.setVolume(left, right);                
        }
 
        public void unload()
        {
            while (!prepared) {}
            if(mPlayer.isPlaying())
                mPlayer.stop();
           
            mPlayer.release();
               
        }

}
