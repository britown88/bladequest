package bladequest.actions;

import bladequest.world.Global;

public class actPopSong extends Action {

	public actPopSong() {
		// TODO Auto-generated constructor stub
	}

	
	
	@Override
	public void run()
	{
		Global.popSong();
	}
	
	@Override
	public boolean isDone()
	{
		return true;
	}
	
}
