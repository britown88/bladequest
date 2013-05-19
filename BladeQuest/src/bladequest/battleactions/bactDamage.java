package bladequest.battleactions;

import java.util.List;

import bladequest.combat.BattleCalc;
import bladequest.combat.DamageMarker;
import bladequest.world.PlayerCharacter;
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
	public void run(PlayerCharacter attacker, List<PlayerCharacter> targets, List<DamageMarker> markers)
	{
		for(PlayerCharacter t : targets)
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
	public void runOutsideOfBattle(PlayerCharacter attacker, List<PlayerCharacter> targets, List<DamageMarker> markers)
	{
		for(PlayerCharacter t : targets)
		{
			int dmg = BattleCalc.calculatedDamage(attacker, t, power, type);
			switch(BattleCalc.getDmgReturnType())
			{
			case Blocked:
				markers.add(new DamageMarker("BLOCK", t));	
				break;
			case Critical:
				markers.add(new DamageMarker("CRIT", t));	
			case Hit:
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
	public boolean willAffectTarget(PlayerCharacter target) 
	{		
		return (target.isInBattle() && 
				power > 0 && 
				target.getHP() < target.getStat(Stats.MaxHP)) 
				|| 
				power < 0; 
	}

}
