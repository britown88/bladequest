package bladequest.actions;

import bladequest.world.Global;

public class actModifyParty extends Action
{
	String charName;
	boolean remove;

	public actModifyParty(String charName, boolean remove)
	{
		super();
		this.charName = charName;
		this.remove = remove;
	}
	
	@Override
	public void run()
	{
		if(remove)
		{
			//TODO: remove character
		}
		else
		{
			Global.party.addCharacter(charName);
		}
		
		done = true;
	}
	
	@Override
	public boolean isDone()
	{
		return done;
	}
}
