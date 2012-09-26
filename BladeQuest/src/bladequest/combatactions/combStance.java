package bladequest.combatactions;

import java.util.List;

import bladequest.world.Character;
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
	public void execute(List<Character> targets)
	{
		
	}

}
