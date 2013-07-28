package bladequest.actions;

import bladequest.world.Global;


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
		Global.bladeSong.play(song, playIntro, loop, fadeTime);
	}
	
	@Override
	public boolean isDone()
	{
		return loop || Global.bladeSong.getCurrentSong().equals("");  //only true when no longer playing anything.
	}

}
