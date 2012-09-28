package bladequest.battleactions;

import java.util.*;

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
			if(show)			
				switch(Global.GameState)
				{
				case GS_BATTLE:
					//Global.battle.dmgText(t, se.Name().toUpperCase(), 0);
					Global.playAnimation("poison", attacker.getPosition(), t.getPosition());
					break;
				case GS_MAINMENU:
					Global.menu.dmgText(t, se.Name().toUpperCase(), 0);					
					break;
				}
			
			t.applyStatusEffect(se);
		}
		
	}
	
	@Override
	public boolean willAffectTarget(Character target) 
	{		
		return !target.isDead(); 
	}

}
