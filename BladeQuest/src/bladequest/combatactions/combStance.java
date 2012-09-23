package bladequest.combatactions;

import java.util.Vector;

import bladequest.world.Character;
import bladequest.world.TargetTypes;
import bladequest.world.DamageTypes;


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
	public void execute(Vector<Character> targets)
	{
		
	}

}
