package bladequest.battleactions;

import java.util.List;

import bladequest.combat.DamageMarker;
import bladequest.world.PlayerCharacter;

public class bactRemoveStatus extends BattleAction
{
	private String se;
	
	public bactRemoveStatus(int frame, String se)
	{	
		super(frame);
		this.se = se;
	}
	
	@Override
	public void run(PlayerCharacter attacker, List<PlayerCharacter> targets, List<DamageMarker> markers)
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
