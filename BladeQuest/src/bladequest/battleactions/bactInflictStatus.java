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
	
	boolean tryApplyStatus(PlayerCharacter character)
	{
		if (se.getStacks())
		{
			character.applyStatusEffect(se.clone()); 
			return true;
		}
		List<StatusEffect> effects = character.getStatusEffects();
		for (StatusEffect effect : effects)
		{
			if (effect.Name().equals(se.Name()))
			{
				StatusEffect.ReapplyResult result = effect.onReapply(se);
				switch (result)
				{
				case Missed: return false;
				case Replace: character.removeStatusEffect(effect);  character.applyStatusEffect(se.clone()); return true;
				default:
					return true;
				}
			}
		}
		character.applyStatusEffect(se.clone());
		return true;
	}
	
	@Override
	public State run(BattleEventBuilder builder)
	{
		for(PlayerCharacter t : builder.getTargets())
		{
			if(t.isInBattle())
			{
				if (tryApplyStatus(t))
				{
					if (!se.isHidden())
					{
						builder.addMarker(new DamageMarker(se.Name().toUpperCase(Locale.US), t));	
					}	
					
					
				}
				else if (!se.isHidden())
				{
					builder.addMarker(new DamageMarker("MISS", t));
				}				
			}	
			
		}	
		return State.Finished;
	}
	
	@Override
	public void runOutsideOfBattle(PlayerCharacter attacker, List<PlayerCharacter> targets, List<DamageMarker> markers)
	{
		for(PlayerCharacter t : targets)
		{
			if(tryApplyStatus(t))
			{
				if (!se.isHidden())
					markers.add(new DamageMarker(se.Name().toUpperCase(Locale.US), t));	
			}	
			else if (!se.isHidden())				
				markers.add(new DamageMarker("MISS", t));
		}		
	}
	
	@Override
	public boolean willAffectTarget(PlayerCharacter target) 
	{		
		if (se.isNegative() &&  target.hasStatus(se.Name())) return false;
		return target.isInBattle(); 
	}

}
