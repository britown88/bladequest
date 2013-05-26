package bladequest.sound;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import bladequest.world.Global;

public class MusicBox
{
	private String playingSong, lastSong;
	private boolean paused, done, looping, playingIntro;
	private MediaPlayer mPlayer;
	private float fadeTime;
	private long startTime;
	private boolean fadingIn, fadingOut;
	
	public MusicBox()
	{
		mPlayer = new MediaPlayer();
		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);	
		mPlayer.setOnCompletionListener(new OnCompletionListener() {public void onCompletion(MediaPlayer mp) {OnCompletion(mp);}});

		playingSong = "";
		paused = true;
		looping = false;
		
	}
	
	public void OnCompletion(MediaPlayer mp)
	{
		Song song = Global.music.get(playingSong);
		
		if(playingIntro)
			playSong(song, false, looping);			
		
	}
	
	public boolean isDone() { return done; }
	public String getPlayingSong() { return playingSong; }
	
	public void update()
	{
		if(fadingIn || fadingOut)
		{
			long time = System.currentTimeMillis() - startTime;
			float seconds = time / 1000.0f;					
			
			if(seconds < fadeTime)
			{
				float percent = seconds/fadeTime;
				if(fadingOut)
					percent = 1.0f - percent;
				
				mPlayer.setVolume(percent, percent);			
			}	
			else
				if(fadingOut && !paused)
					pause(true, 0);
		}

	}
	
	public void play(String songName, boolean playIntro, boolean loop, float fadeTime)
	{		
		if(playingSong.equals(songName) || songName.equals("inherit"))
			return;

		this.fadeTime = fadeTime;
		startTime = System.currentTimeMillis();
		fadingIn = true;
		fadingOut = false;
		if(fadeTime > 0)
			mPlayer.setVolume(0, 0);
		else
			mPlayer.setVolume(1, 1);
		
		//non-infinite-loop
		if(!loop)
			done = false;
		
		
		
		playingSong = songName;
		Song song = Global.music.get(songName);
		
		if(song != null)
		{
			playSong(song, playIntro, loop);
			paused = false;
		}
		else
		{
			pause(true, 0);
		}
	}
	
	private void playSong(Song song, boolean playIntro, boolean loop)
	{
		AssetFileDescriptor afd;
		mPlayer.reset();
		
		looping = loop;
		playingIntro = playIntro && song.HasIntro();
		
		String songPath = playingIntro ? song.IntroPath() : song.Path(); 
		
		try {			
			afd = Global.activity.getAssets().openFd(songPath);
			mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			mPlayer.setLooping(!playIntro && loop);
			mPlayer.prepare();
			afd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mPlayer.start();		
		
	}
	
	public void resumeLastSong()
	{
		play(lastSong, false, true, 0);
	}
	
	public void saveSong()
	{
		lastSong = playingSong;
	}
	
	public void pause(boolean setPaused, float fadeTime)
	{
		if(!paused)
		{
			if(fadeTime == 0)
			{
				mPlayer.pause();
				
				if(setPaused)
					paused = true;
			}			
			else
			{
				this.fadeTime = fadeTime;
				startTime = System.currentTimeMillis();
				fadingIn = false;
				fadingOut = true;
			}
		}		
	}
	
	public void stop()
	{
		mPlayer.release();
	}
	
	public void resume()
	{
		if(!paused)
		{
			mPlayer.start();
			paused = false;
		}		
	}	

}
