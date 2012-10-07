package bladequest.battleactions;

import java.util.*;

import bladequest.combat.Battle;
import bladequest.combat.DamageMarker;
import bladequest.statuseffects.*;
import bladequest.world.Character;
import bladequest.world.Global;
import bladequest.world.States;

public class bactRemoveStatus extends BattleAction
{
	private String se;
	
	public bactRemoveStatus(int frame, String se)
	{	
		super(frame);
		this.se = se;
	}
	
	@Override
	public void run(Character attacker, List<Character> targets, List<DamageMarker> markers)
	{
		for(Character t : targets)
		{
			if(t.hasStatus(se))
			{
				t.removeStatusEffect(se);
				markers.add(new DamageMarker("CURE", t));
			}
			else				
				markers.add(new DamageMarker("MISS", t));
		}
			
	}
	
	@Override
	public boolean willAffectTarget(Character target) 
	{		
		return target.hasStatus(se); 
	}

}
