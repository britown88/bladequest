package bladequest.combatactions;

import java.util.List;

import bladequest.combat.DamageMarker;
import bladequest.world.PlayerCharacter;
import bladequest.world.DamageTypes;
import bladequest.world.TargetTypes;


public class combWish extends CombatAction 
{
	public combWish()
	{
		name = "Wish";
		type = DamageTypes.Magic;
		targetType = TargetTypes.AllAllies;
		
		actionText = " makes a wish!";
	}
	
	@Override
	public void execute(List<PlayerCharacter> targets, List<DamageMarker> markers)
	{
		
	}

}
