package bladequest.actions;

import bladequest.sound.BladeSong;


public class actPlayMusic extends Action 
{
	private String song;
	private boolean playIntro, loop;

	private float fadeTime;
	
	public actPlayMusic(String song, boolean playIntro, boolean loop, float fadeTime)
	{
		super();
		this.song = song;
		this.playIntro = playIntro;
		this.loop = loop;
		this.fadeTime = fadeTime;
	}
	
	@Override
	public void run()
	{
		BladeSong.instance().play(song, playIntro, loop, fadeTime);
	}
	
	@Override
	public boolean isDone()
	{
		return loop || BladeSong.instance().getCurrentSong().equals("");  //only true when no longer playing anything.
	}

}
