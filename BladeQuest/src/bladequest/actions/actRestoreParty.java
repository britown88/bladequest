package bladequest.actions;

import bladequest.world.Character;
import bladequest.world.GameObject;
import bladequest.world.Global;

public class actRestoreParty extends Action {
	
	public actRestoreParty()
	{
		super();
	}
	
	@Override
	public void run()
	{
		for(Character c : Global.party.getPartyMembers(false))
		{
			if(c != null)
				c.fullRestore();
		}
		done = true;
	}
	
	@Override
	public boolean isDone()
	{
		return done;
	}

}
