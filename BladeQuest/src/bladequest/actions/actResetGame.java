package bladequest.actions;

import bladequest.world.Character;
import bladequest.world.GameObject;
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
