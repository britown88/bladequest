package bladequest.actions;

import bladequest.world.Global;

public class actModifyGold extends Action 
{
	int gold;
	public actModifyGold(int gold)
	{
		super();
		this.gold = gold;
	}
	
	@Override
	public void run()
	{
		Global.party.addGold(gold);		
		done = true;
	}
	
	@Override
	public boolean isDone()
	{
		return done;
	}

}
