package bladequest.combatactions;

import bladequest.combat.BattleEventBuilder;
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
	public void buildEvents(BattleEventBuilder builder)
	{ 
	}
}
