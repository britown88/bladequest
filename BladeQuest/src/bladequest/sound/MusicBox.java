package bladequest.sound;

import bladequest.world.Global;

public class MusicBox
{
	
	public enum FadeTypes
	{
		FadingIn,
		FadingOut,
		NotFading
	}
	private String playingSong, lastSong;
	private float fadeTime;
	private long startTime;
	private boolean done;
	private IntroLoopPlayer player;
	private FadeTypes fadeType;
	
	public MusicBox()
	{
		playingSong = "";
		fadeType = FadeTypes.NotFading;
		player = new IntroLoopPlayer();
	}
	
	public boolean isDone() { return done; }
	public String getPlayingSong() { return playingSong; }
	
	
	public void update()
	{
		if(player != null)			
		{
			player.update();
			
			if(fadeType != FadeTypes.NotFading)
			{
				long time = System.currentTimeMillis() - startTime;
				float seconds = time / 1000.0f;					
				
				if(seconds < fadeTime)
				{
					float percent = seconds/fadeTime;
					if(fadeType == FadeTypes.FadingOut)
						percent = 1.0f - percent;
					
					player.setVolume(percent, percent);			
				}	
				else
				{
					if(fadeType == FadeTypes.FadingOut)
					{
						playingSong = "";
						pause(0);
					}
					
					fadeType = FadeTypes.NotFading;
				}
			}
		}
	}
	
	private void clearFading()
	{
		if(fadeType != FadeTypes.NotFading)
		{
			if(fadeType == FadeTypes.FadingOut)
			{
				playingSong = "";
				pause(0);
			}
			
			player.setVolume(100, 100);
			fadeType = FadeTypes.NotFading;
		}
	}
	
	public void play(String songName, boolean playIntro, boolean loop, float fadeTime)
	{		
		if(!songName.equals("") && (playingSong.equals(songName) || songName.equals("inherit")))
			return;
		
		playingSong = songName;
		Song song = Global.music.get(songName);
		
		clearFading();
		this.fadeTime = fadeTime;
		if(fadeTime > 0)
		{
			fadeType = FadeTypes.FadingIn;
			startTime = System.currentTimeMillis();
		}
			
		
		//non-infinite-loop
		if(!loop) done = false;			
		
		if(song != null)
			playSong(song, playIntro, loop);
		else
			pause(0);
	}
	
	private void playSong(Song song, boolean playIntro, boolean loop)
	{
		player.playSong(song, playIntro, loop, fadeTime > 0);
	}
	
	public void resumeLastSong()
	{
		play(lastSong, false, true, 0);
	}
	
	public void saveSong()
	{
		lastSong = fadeType == FadeTypes.FadingOut ? "" : playingSong;
	}
	
	public void pause(float fadeTime)
	{
		//setFadeData
		if(fadeTime > 0)
		{
			this.fadeTime = fadeTime;
			startTime = System.currentTimeMillis();
			fadeType = FadeTypes.FadingOut;
		}
		else 
			player.pause();
		
	}
	
	//system music calls for screen on/off
	public void systemPause()
	{
		if(player != null)
			player.pause();
	}	
	public void systemResume()
	{
		player.play();
	}	
	
	public void release()
	{
		if(player != null)
		{
			player.unload();
			player = null;
		}
			
	}

}
