package bladequest.actions;

import bladequest.world.Global;

public class actGoldTransaction extends Action
{
	int gold;
	public actGoldTransaction(int gold)
	{
		super();
		this.gold = gold;
	}

	
	@Override 
	public void run()
	{
		if(Global.party.getGold() >= gold)
		{
			Global.party.addGold(-gold);
			startBranch(0);
		}
		else
			startBranch(1);
			
	}
	
	@Override
	public boolean isDone()
	{
		return branchIsDone();
	}
}
