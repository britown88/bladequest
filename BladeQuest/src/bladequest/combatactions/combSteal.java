package bladequest.combatactions;

import java.util.List;

import bladequest.combat.DamageMarker;
import bladequest.world.PlayerCharacter;
import bladequest.world.DamageTypes;
import bladequest.world.Enemy;
import bladequest.world.Global;
import bladequest.world.TargetTypes;


public class combSteal extends CombatAction 
{
	public combSteal()
	{
		name = "Steal";
		type = DamageTypes.Physical;
		targetType = TargetTypes.SingleEnemy;
		actionText = " tries to steal!";
	}
	
	@Override
	public void execute(List<PlayerCharacter> targets, List<DamageMarker> markers)
	{
		Enemy target = (Enemy)targets.get(0);
		
		if(target.hasItems())
		{
			String item = target.getItem(true);
			if(item == null)
			{
				markers.add(new DamageMarker("MISS", target));	
				Global.battle.changeStartBarText("Failed to steal!");
			}
			else
			{
				Global.party.addItem(item);
				
				markers.add(new DamageMarker("STEAL", target));
				Global.battle.changeStartBarText("Stole a "+ Global.items.get(item).getName() +"!");
			}
		}
		else
		{
			markers.add(new DamageMarker("FAIL", target));
			Global.battle.changeStartBarText("Doesn't have anything!");
		}
		
		
	}

}
