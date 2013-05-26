package bladequest.actions;

import bladequest.actions.Action;
import bladequest.world.*;


public class actStartBattle extends Action 
{
	String encounter;
	
	public actStartBattle(String encounter)
	{
		super();
		this.encounter = encounter;
	}
	
	@Override
	public void run()
	{
		Global.beginBattle(encounter);
		done = true;
	}
	
	@Override
	public boolean isDone()
	{
		return done;
	}

}