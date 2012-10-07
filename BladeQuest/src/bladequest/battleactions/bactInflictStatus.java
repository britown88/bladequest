package bladequest.battleactions;

import java.util.*;

import bladequest.combat.Battle;
import bladequest.combat.DamageMarker;
import bladequest.statuseffects.*;
import bladequest.world.Character;
import bladequest.world.Global;



public class bactInflictStatus extends BattleAction
{
	private StatusEffect se;
	private boolean show;
	
	public bactInflictStatus(int frame, boolean show, StatusEffect se)
	{	
		super(frame);
		this.se = se;
		this.show = show;
	}
	
	@Override
	public void run(Character attacker, List<Character> targets, List<DamageMarker> markers)
	{
		for(Character t : targets)
		{
			if(!t.isDead() && !t.hasStatus(se.Name()))
			{
				markers.add(new DamageMarker(se.Name().toUpperCase(), t));			
				t.applyStatusEffect(se);
			}	
			else
				markers.add(new DamageMarker("MISS", t));
		}		
	}
	
	@Override
	public boolean willAffectTarget(Character target) 
	{		
		return !target.isDead() && !target.hasStatus(se.Name()); 
	}

}
