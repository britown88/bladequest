package bladequest.sound;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import bladequest.world.Global;

public class MusicBox
{
	private String playingSong, lastSong;
	private boolean paused, done, nonInfinite;
	private MediaPlayer mPlayer;
	//private JetPlayer.OnJetEventListener listener;
	
	public MusicBox()
	{
		mPlayer = new MediaPlayer();
		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);	
		mPlayer.setOnCompletionListener(new OnCompletionListener() {public void onCompletion(MediaPlayer mp) {OnCompletion(mp);}});

		playingSong = "";
		paused = true;
		nonInfinite = false;
		
	}
	
	public void OnCompletion(MediaPlayer mp)
	{
		Song song = Global.music.get(playingSong);
		
		mPlayer.seekTo(song.LoopToMS());
		
		mPlayer.start();
		
	}
	
	public boolean isDone() { return done; }
	public String getPlayingSong() { return playingSong; }
	
	public void play(String songName, boolean playIntro, int repeatCount)
	{		
		if(playingSong.equals(songName) || songName.equals("inherit"))
			return;
		
		//non-infinite-loop
		if(repeatCount != -1)
		{
			//saveSong();
			nonInfinite = true;
			done = false;
		}			
		
		playingSong = songName;
		Song song = Global.music.get(songName);
		if(song != null)
		{
			playSong(song, playIntro, repeatCount);
			paused = false;
		}
		else
		{
			pause(true);
		}
	}
	
	private void playSong(Song song, boolean playIntro, int repeatCount)
	{
		AssetFileDescriptor afd;
		mPlayer.reset();	
		
		try {
			afd = Global.activity.getAssets().openFd(song.Path());
			mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			mPlayer.prepare();
			afd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!playIntro)
			mPlayer.seekTo(song.LoopToMS());
		
		mPlayer.start();		
		
	}
	
	public void resumeLastSong()
	{
		play(lastSong, false, -1);
	}
	
	public void saveSong()
	{
		lastSong = playingSong;
	}
	
	public void pause(boolean setPaused)
	{
		if(!paused)
		{
			mPlayer.pause();
			
			if(setPaused)
				paused = true;
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
