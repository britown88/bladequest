package bladequest.actions;

import bladequest.world.Global;

public class actResetGame extends Action {
	
	public actResetGame()
	{
		super();
	}
	
	@Override
	public void run()
	{
		Global.restartGame();
	}
	
	@Override
	public boolean isDone()
	{
		return true;
	}

}
