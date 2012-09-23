package bladequest.actions;

import bladequest.UI.SaveLoadMenu;
import bladequest.world.Global;

public class actSaveMenu extends Action 
{
	
	public actSaveMenu()
	{
		super();
	}
	
	@Override
	public void run()
	{
		Global.openSaveLoadMenu(SaveLoadMenu.SAVING);
		done = true;
	}
	
	@Override
	public boolean isDone()
	{
		return done;
	}

}
