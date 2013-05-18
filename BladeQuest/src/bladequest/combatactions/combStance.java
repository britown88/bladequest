package bladequest.combatactions;

import java.util.List;

import bladequest.combat.DamageMarker;
import bladequest.world.PlayerCharacter;
import bladequest.world.DamageTypes;
import bladequest.world.TargetTypes;


public class combStance extends CombatAction 
{
	public combStance()
	{
		name = "Stance";
		type = DamageTypes.Physical;
		targetType = TargetTypes.Self;
		actionText = " assumes a combat stance.";
	}
	
	@Override
	public void execute(List<PlayerCharacter> targets, List<DamageMarker> markers)
	{
		
	}

}
