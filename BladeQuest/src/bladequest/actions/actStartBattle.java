package bladequest.actions;

import bladequest.actions.Action;
import bladequest.world.*;


public class actStartBattle extends Action 
{
	String encounter;
	boolean allowGameOver;
	
	public actStartBattle(String encounter, boolean allowGameOver)
	{
		super();
		this.encounter = encounter;
		this.allowGameOver = allowGameOver;
	}
	
	@Override
	public void run()
	{
		Global.beginBattle(encounter, allowGameOver);
		done = true;
	}
	
	@Override
	public boolean isDone()
	{
		if(!runningBranch)
		{
			startBranch(Global.battle.getOutcome().ordinal());
			return !runningBranch;
		}
		else
			return branchIsDone();
	}

}