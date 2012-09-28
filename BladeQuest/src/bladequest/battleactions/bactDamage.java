package bladequest.battleactions;

import java.util.*;

import bladequest.combat.BattleCalc;
import bladequest.combat.DamageMarker;
import bladequest.graphics.BattleSprite.faces;
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
			int dmg = BattleCalc.calculatedDamage(attacker, t, power, type);
			switch(BattleCalc.getDmgReturnType())
			{
			case Blocked:
				markers.add(new DamageMarker("BLOCK", t));	
				break;
			case Critical:
				Global.screenFader.setFadeColor(255, 255, 255, 255);
				Global.screenFader.flash(15);
			case Hit:
				if(dmg >= 0 && !t.isEnemy())
					t.showDamaged();
				t.modifyHP(-dmg, false);
				markers.add(new DamageMarker(-dmg, t));	
				break;
			case Miss:
				markers.add(new DamageMarker("MISS", t));	
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
