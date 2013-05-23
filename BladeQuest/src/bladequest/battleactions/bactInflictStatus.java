package bladequest.battleactions;

import java.util.List;
import java.util.Locale;

import bladequest.combat.BattleEventBuilder;
import bladequest.combat.DamageMarker;
import bladequest.statuseffects.StatusEffect;
import bladequest.world.PlayerCharacter;



public class bactInflictStatus extends BattleAction
{
	private StatusEffect se;
	//private boolean show;
	
	public bactInflictStatus(StatusEffect se)
	{	
		this.se = se;
	}
	
	@Override
	public State run(BattleEventBuilder builder)
	{
		for(PlayerCharacter t : builder.getTargets())
		{
			if(t.isInBattle() && !t.hasStatus(se.Name()))
			{
				if (!se.isHidden())
				{
					builder.addMarker(new DamageMarker(se.Name().toUpperCase(Locale.US), t));	
				}	
				
				t.applyStatusEffect(se);
			}	
			else if (!se.isHidden())
			{
				builder.addMarker(new DamageMarker("MISS", t));
			}
		}	
		return State.Finished;
	}
	
	@Override
	public void runOutsideOfBattle(PlayerCharacter attacker, List<PlayerCharacter> targets, List<DamageMarker> markers)
	{
		for(PlayerCharacter t : targets)
		{
			if(t.isInBattle() && !t.hasStatus(se.Name()))
			{
				if (!se.isHidden())
					markers.add(new DamageMarker(se.Name().toUpperCase(Locale.US), t));	
				
				t.applyStatusEffect(se);
			}	
			else if (!se.isHidden())				
				markers.add(new DamageMarker("MISS", t));
		}		
	}
	
	@Override
	public boolean willAffectTarget(PlayerCharacter target) 
	{		
		return target.isInBattle() && !target.hasStatus(se.Name()); 
	}

}
