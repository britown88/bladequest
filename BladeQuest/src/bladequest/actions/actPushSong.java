package bladequest.actions;

import bladequest.world.Global;

public class actPushSong extends Action {

	public actPushSong() {
		// TODO Auto-generated constructor stub
	}

	
	
	@Override
	public void run()
	{
		Global.pushSong();
	}
	
	@Override
	public boolean isDone()
	{
		return true;
	}
	
}
