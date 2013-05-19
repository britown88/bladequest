package bladequest.battleactions;

import java.util.List;
import java.util.Locale;

import bladequest.combat.DamageMarker;
import bladequest.statuseffects.StatusEffect;
import bladequest.world.PlayerCharacter;



public class bactInflictStatus extends BattleAction
{
	private StatusEffect se;
	//private boolean show;
	
	public bactInflictStatus(int frame, boolean show, StatusEffect se)
	{	
		super(frame);
		this.se = se;
		//this.show = show;
	}
	
	@Override
	public void run(PlayerCharacter attacker, List<PlayerCharacter> targets, List<DamageMarker> markers)
	{
		for(PlayerCharacter t : targets)
		{
			if(t.isInBattle() && !t.hasStatus(se.Name()))
			{
				if (!se.isHidden())
				{
					markers.add(new DamageMarker(se.Name().toUpperCase(Locale.US), t));	
				}	
				
				t.applyStatusEffect(se);
			}	
			else if (!se.isHidden())
			{
				
				markers.add(new DamageMarker("MISS", t));
			}
		}		
	}
	
	@Override
	public boolean willAffectTarget(PlayerCharacter target) 
	{		
		return target.isInBattle() && !target.hasStatus(se.Name()); 
	}

}
