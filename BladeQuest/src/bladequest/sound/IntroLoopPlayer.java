package bladequest.sound;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import bladequest.world.Global;


public class IntroLoopPlayer
{
	private MediaPlayer mPlayer;
	private Song song;
	private long startTime, duration, pausedAt;
	
	private boolean paused, loop, playIntro, loopHasStarted, prepared, started;
	
	public IntroLoopPlayer()
	{			
		started = false;
		prepared = false;
		mPlayer = new MediaPlayer();		
		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mPlayer.setOnPreparedListener(new OnPreparedListener()
		{

			@Override
			public void onPrepared(MediaPlayer mp) {
				duration = mp.getDuration();
				mp.start();
				prepared = true;
			}
			
		});
		mPlayer.setOnErrorListener(new OnErrorListener()
		{

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
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
	
	private void prepareAndPlaySong(String path, boolean loop)
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
			mPlayer.setLooping(loop);
			if(playIntro && !loopHasStarted)
				mPlayer.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						completion();
					}
				});
			prepared = false;
			mPlayer.prepareAsync();		
			
			afd.close();
		} catch (Exception e) {Log.d("MEDIAPLAYER", "FUCKED");}
		
		started = true;
		//mPlayer.start();
		
	}

	private void completion()
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
		//if(!paused && System.currentTimeMillis() - startTime >= duration)
			//onCompletion();
	}
	
	public void play()
	{
		//if (!started) return;
		//while (!prepared) {} //loloo
		if(paused)
		{
			startTime = System.currentTimeMillis();		
			startTime -= pausedAt;
			pausedAt = 0;
			
			if(playIntro && !loopHasStarted)
				prepareAndPlaySong(song.IntroPath(), false);			
			else
			{
				loopHasStarted = true;
				prepareAndPlaySong(song.Path(), loop);			
			}
			
		//	duration = mPlayer.getDuration();
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
