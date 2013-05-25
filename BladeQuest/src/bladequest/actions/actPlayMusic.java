package bladequest.actions;

import bladequest.actions.Action;
import bladequest.world.*;


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
		Global.musicBox.play(song, playIntro, loop, fadeTime);
	}
	
	@Override
	public boolean isDone()
	{
		return loop || Global.musicBox.isDone();
	}

}
