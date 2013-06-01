package bladequest.combatactions;

import java.util.List;

import bladequest.combat.BattleEventBuilder;
import bladequest.combat.DamageMarker;
import bladequest.world.DamageTypes;
import bladequest.world.PlayerCharacter;
import bladequest.world.TargetTypes;


public class combDragonForm extends CombatAction 
{
	public combDragonForm()
	{
		name = "Drgn Form";
		type = DamageTypes.Magic;
		targetType = TargetTypes.Self;
		actionText = " assumes the form of a dragon!";
	}
	
	@Override
	public String getDescription() { return "Assume the form of a ferocious ice dragon.";}
	
	@Override
	public void execute(List<PlayerCharacter> targets, List<DamageMarker> markers)
	{
		
	}
	
	@Override
	public void buildEvents(BattleEventBuilder builder)
	{
		
	}

}
