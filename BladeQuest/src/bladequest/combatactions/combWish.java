package bladequest.combatactions;

import java.util.Vector;
import bladequest.world.Character;
import bladequest.world.TargetTypes;
import bladequest.world.DamageTypes;


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
	public void execute(Vector<Character> targets)
	{
		
	}

}
