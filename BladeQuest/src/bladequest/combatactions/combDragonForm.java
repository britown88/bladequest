package bladequest.combatactions;

import java.util.Vector;

import bladequest.world.Character;
import bladequest.world.TargetTypes;
import bladequest.world.DamageTypes;


public class combDragonForm extends CombatAction 
{
	public combDragonForm()
	{
		name = "Dragon Form";
		type = DamageTypes.Magic;
		targetType = TargetTypes.Self;
		actionText = " assumes the form of a dragon!";
	}
	
	@Override
	public void execute(Vector<Character> targets)
	{
		
	}

}
