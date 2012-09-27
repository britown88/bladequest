package bladequest.battleactions;

import java.util.*;

import bladequest.combat.DamageMarker;
import bladequest.world.Battle;
import bladequest.world.Character;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.Stats;

public class bactDamage extends BattleAction
{
	float power;
	DamageTypes type;
	
	public bactDamage(int frame, float power, DamageTypes type)
	{
		super(frame);
		this.power = power;
		this.type = type;
	}
	
	@Override
	public void run(Character attacker, List<Character> targets, List<DamageMarker> markers)
	{
		for(Character t : targets)
		{
			switch(type)
			{
			case Fixed:
				switch(Global.GameState)
				{
				case GS_BATTLE:
					Global.battle.applyDamage(t, (int)power, 0);
					break;
				case GS_MAINMENU:
					Global.menu.applyDamage(t, (int)power, 0);					
					break;
				}
				
				break;
			case Physical:
				int ap = attacker.getBattlePower();
				int boosted = (int)(ap*power);
				Global.battle.applyDamage(t,-Battle.genDamage(attacker,t,boosted),0);
				break;
			}
			
		}
		
	}
	
	@Override
	public boolean willAffectTarget(Character target) 
	{		
		return (!target.isDead() && 
				power > 0 && 
				target.getHP() < target.getStat(Stats.MaxHP)) 
				|| 
				power < 0; 
	}

}
