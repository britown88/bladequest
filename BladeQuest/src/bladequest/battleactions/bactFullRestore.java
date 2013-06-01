package bladequest.battleactions;

import java.util.List;
import java.util.Locale;

import bladequest.battleactions.BattleAction.State;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.DamageMarker;
import bladequest.world.PlayerCharacter;
import bladequest.world.Stats;

public class bactFullRestore extends BattleAction
{
	public bactFullRestore(){}
	
	@Override
	public State run(BattleEventBuilder builder)
	{
		for(PlayerCharacter t : builder.getTargets())
		{
			if(willAffectTarget(t))
			{
				t.fullRestore();
				builder.addMarker(new DamageMarker("FULL RESTORE", t));
			}				
			else
				builder.addMarker(new DamageMarker("MISS", t));
			
		}	
		return State.Finished;
	}
	
	@Override
	public void runOutsideOfBattle(PlayerCharacter attacker, List<PlayerCharacter> targets, List<DamageMarker> markers)
	{
		for(PlayerCharacter t : targets)
		{
			if(willAffectTarget(t))
			{
				t.fullRestore();
				markers.add(new DamageMarker("FULL RESTORE", t));
			}				
			else
				markers.add(new DamageMarker("MISS", t));
			
		}	
	}
	
	@Override
	public boolean willAffectTarget(PlayerCharacter target) 
	{		
		return target.isDead() || 
				target.hasAdverseStatus() || 
				target.getMP() < target.getStat(Stats.MaxMP.ordinal()) || 
				target.getHP() < target.getStat(Stats.MaxHP.ordinal());
	}

}
