package bladequest.battleactions;

import java.util.List;

import bladequest.combat.BattleEventBuilder;
import bladequest.combat.DamageMarker;
import bladequest.world.PlayerCharacter;

public class bactRemoveStatus extends BattleAction
{
	private String se;
	
	public bactRemoveStatus(String se)
	{	
		this.se = se;
	}
	
	
	@Override
	public State run(BattleEventBuilder builder)
	{
		for(PlayerCharacter t : builder.getTargets())
		{
			if(t.hasStatus(se))
			{
				t.removeStatusEffect(se);
				builder.addMarker(new DamageMarker("CURE", t));
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
	public boolean willAffectTarget(PlayerCharacter target) 
	{		
		return target.hasStatus(se); 
	}

}
