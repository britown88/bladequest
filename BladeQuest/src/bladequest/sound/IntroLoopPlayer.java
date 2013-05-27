package bladequest.sound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import bladequest.world.Global;

public class IntroLoopPlayer
{
	private MediaPlayer introPlayer;
	
	private List<MediaPlayer> loopPlayers;
	private boolean loopPlayerBack;
	private Song song;
	private long startTime, duration, pausedAt;
	
	private boolean paused, loop, playIntro, loopHasStarted;
	
	public IntroLoopPlayer(Song song, boolean playIntro, boolean loop)
	{	 
		this.song = song;		
		
		this.loop = loop;
		this.playIntro = playIntro && song.HasIntro();
		
		loopPlayers = new ArrayList<MediaPlayer>();
		
		for(int i = 0; i < 2; ++i)
		{
			MediaPlayer loopPlayer = new MediaPlayer();		
			loopPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			loopPlayers.add(loopPlayer);
		}		
		
		loopPlayerBack = false;
		
		if(playIntro)
		{
			introPlayer= new MediaPlayer();
			introPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		}
		
		load();
	}
	
	public boolean loopHasStarted(){return loopHasStarted;}
	 
	private void load()
	{
		loopHasStarted = false;
		AssetFileDescriptor afd;
		pausedAt = 0;
		
		try {			
			afd = Global.activity.getAssets().openFd(song.Path());
			
			for(MediaPlayer mp : loopPlayers)
			{
				mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());
				mp.setLooping(false);			
				mp.prepare();
			}			
			
			afd.close();			
			
			if(playIntro)
			{
				afd = Global.activity.getAssets().openFd(song.IntroPath());
				
				introPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());
				introPlayer.setLooping(false);
				introPlayer.prepare();
				
				afd.close();
			}						
		}
		catch (IOException e) {}
	}
	
	private void onCompletion()
	{
		if(playIntro && !loopHasStarted)
		{
			loopHasStarted = true;
			play();
		}
		else if(loop)
		{			
			loopPlayerBack = !loopPlayerBack;
			play();				
			loopPlayers.get(loopPlayerBack ? 0 : 1).seekTo(0);
			loopPlayers.get(loopPlayerBack ? 0 : 1).pause();
			
		}		
	}
	
	public void update()
	{
		if(!paused && System.currentTimeMillis() - startTime >= duration)
			onCompletion();
	}
	 
	public void unload() 
	{
		if(introPlayer != null)
		{
			introPlayer.stop();
			introPlayer.release();
			introPlayer = null;
		}
		for(MediaPlayer mp : loopPlayers)
		{
			if(mp.isPlaying())
				mp.stop();
			mp.release();
		}
		
		loopPlayers.clear();
		
	}
	
	public void play()
	{
		startTime = System.currentTimeMillis();
		
		startTime -= pausedAt;
		pausedAt = 0;
		
		if(playIntro && !loopHasStarted)
		{
			introPlayer.start();
			duration = introPlayer.getDuration();
		}			
		else
		{
			loopHasStarted = true;
			MediaPlayer loopPlayer = loopPlayers.get(loopPlayerBack ? 1 : 0);
			loopPlayer.start();
			duration = loopPlayer.getDuration();
		}
		
		paused = false;
	}
	
	public void pause()
	{	
		long st = System.currentTimeMillis();
		if(!loopHasStarted && introPlayer.isPlaying())
			introPlayer.pause();
		for(MediaPlayer mp : loopPlayers)
		{
			if(mp.isPlaying())
				mp.pause();		
		}
		long d = System.currentTimeMillis() - st;
		
		pausedAt = System.currentTimeMillis() - startTime + d;
		paused = true;
		
	}
	
	public void setVolume(float left, float right)
	{
		if(loopHasStarted)
			for(MediaPlayer mp : loopPlayers)
				mp.setVolume(left, right);
		else if(playIntro)
			introPlayer.setVolume(left, right);			
	}

}
