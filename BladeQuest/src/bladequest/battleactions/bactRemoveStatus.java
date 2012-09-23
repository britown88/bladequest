package bladequest.battleactions;

import java.util.*;

import bladequest.statuseffects.*;
import bladequest.world.Character;
import bladequest.world.Global;
import bladequest.world.States;

public class bactRemoveStatus extends battleAction
{
	private String se;
	
	public bactRemoveStatus(String se)
	{	
		this.se = se;
	}
	
	@Override
	public void run(Character attacker, List<Character> targets, int delay)
	{
		for(Character t : targets)
		{
			t.removeStatusEffect(se);
			
			if(Global.GameState == States.GS_MAINMENU)
				Global.menu.dmgText(t, "CURE", delay);	
		}
			
	}
	
	@Override
	public boolean willAffectTarget(Character target) 
	{		
		return target.hasStatus(se); 
	}

}
