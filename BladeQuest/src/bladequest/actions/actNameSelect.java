package bladequest.actions;

import bladequest.world.GameObject;
import bladequest.world.Global;
import bladequest.world.Character;

public class actNameSelect extends Action
{
	String chName;
	GameObject parent;
	
	public actNameSelect(String chName)
	{
		super();
		this.chName = chName;
	}
	
	@Override
	public void run()
	{
		Character c = null;
		//look for character in current party
		for(Character ch : Global.party.getPartyList(true))
			if(ch.getName().equals(chName))
			{
				c = ch;
				break;
			}
		
		//change name in base character list
		if(c == null)
			c = Global.characters.get(chName);
		
		Global.openNameSelect(c);
	}
	
	@Override
	public boolean isDone()
	{
		return Global.nameSelect.isClosed();
	}

}
