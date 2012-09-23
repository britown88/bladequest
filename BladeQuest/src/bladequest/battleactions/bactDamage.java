package bladequest.battleactions;

import java.util.*;

import bladequest.world.Battle;
import bladequest.world.Character;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.Stats;

public class bactDamage extends battleAction
{
	float power;
	DamageTypes type;
	
	public bactDamage(float power, DamageTypes type)
	{
		this.power = power;
		this.type = type;
	}
	
	@Override
	public void run(Character attacker, List<Character> targets, int delay)
	{
		for(Character t : targets)
		{
			switch(type)
			{
			case Fixed:
				switch(Global.GameState)
				{
				case GS_BATTLE:
					Global.battle.applyDamage(t, (int)power, delay);
					break;
				case GS_MAINMENU:
					Global.menu.applyDamage(t, (int)power, delay);					
					break;
				}
				
				break;
			case Physical:
				int ap = attacker.getBattlePower();
				int boosted = (int)(ap*power);
				Global.battle.applyDamage(t,-Battle.genDamage(attacker,t,boosted),delay);
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
