package bladequest.combatactions;

import java.util.List;

import bladequest.combat.DamageMarker;
import bladequest.world.Character;
import bladequest.world.DamageTypes;
import bladequest.world.TargetTypes;


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
	public void execute(List<Character> targets, List<DamageMarker> markers)
	{
		
	}

}
