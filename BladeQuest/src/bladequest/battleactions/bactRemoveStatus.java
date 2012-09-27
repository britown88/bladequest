package bladequest.battleactions;

import java.util.*;

import bladequest.combat.DamageMarker;
import bladequest.statuseffects.*;
import bladequest.world.Character;
import bladequest.world.Global;
import bladequest.world.States;

public class bactRemoveStatus extends BattleAction
{
	private String se;
	
	public bactRemoveStatus(int animFrame, String se)
	{	
		super(animFrame);
		this.se = se;
	}
	
	@Override
	public void run(Character attacker, List<Character> targets, List<DamageMarker> markers)
	{
		for(Character t : targets)
		{
			t.removeStatusEffect(se);
			
			if(Global.GameState == States.GS_MAINMENU)
				Global.menu.dmgText(t, "CURE", 0);	
		}
			
	}
	
	@Override
	public boolean willAffectTarget(Character target) 
	{		
		return target.hasStatus(se); 
	}

}
