package bladequest.sound;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.util.Log;
import bladequest.world.Global;


public class IntroLoopPlayer
{
	private MediaPlayer mPlayer;
	private Song song;
	private long startTime, duration, pausedAt;
	
	private boolean paused, loop, playIntro, loopHasStarted;
	
	public IntroLoopPlayer()
	{			
		mPlayer = new MediaPlayer();		
		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);		
	}
	
	public boolean loopHasStarted(){return loopHasStarted;}
	
	public void playSong(Song song, boolean playIntro, boolean loop)
	{
		this.song = song;			
		this.loop = loop;
		this.playIntro = playIntro && song.HasIntro();
		loopHasStarted = false;
		
		paused = true;//play only runs from paused		
		play();
	}
	
	private void prepareAndPlaySong(String path)
	{
		mPlayer.reset();		
		
		try {
			AssetFileDescriptor afd = Global.activity.getAssets().openFd(path);		
			mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());
			
			mPlayer.setOnErrorListener(new OnErrorListener() {
				
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					// TODO Auto-generated method stub
					return false;
				}
			});
			mPlayer.prepare();		
			
			afd.close();
		} catch (Exception e) {Log.d("MEDIAPLAYER", "FUCKED");}
		
		mPlayer.start();
		
	}

	private void onCompletion()
	{
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
		if(!paused && System.currentTimeMillis() - startTime >= duration)
			onCompletion();
	}
	
	public void play()
	{
		if(paused)
		{
			startTime = System.currentTimeMillis();		
			startTime -= pausedAt;
			pausedAt = 0;
			
			if(playIntro && !loopHasStarted)
				prepareAndPlaySong(song.IntroPath());			
			else
			{
				loopHasStarted = true;
				prepareAndPlaySong(song.Path());			
			}
			
			duration = mPlayer.getDuration();
			paused = false;			
		}		
	}
	
	public void pause()
	{	
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
		mPlayer.setVolume(left, right);			
	}

	public void unload() 
	{
		if(mPlayer.isPlaying())
			mPlayer.stop();
		
		mPlayer.release();
		
	}

}
