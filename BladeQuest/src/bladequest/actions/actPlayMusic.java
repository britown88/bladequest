package bladequest.actions;

import bladequest.actions.Action;
import bladequest.world.*;


public class actPlayMusic extends Action 
{
	private String song;
	private boolean playIntro;
	private int repeatCount;
	
	public actPlayMusic(String song, boolean playIntro, int repeatCount)
	{
		super();
		this.song = song;
		this.playIntro = playIntro;
		this.repeatCount = repeatCount;
	}
	
	@Override
	public void run()
	{
		Global.musicBox.play(song, playIntro, repeatCount, 0);
	}
	
	@Override
	public boolean isDone()
	{
		return repeatCount == -1 || Global.musicBox.isDone();
	}

}
