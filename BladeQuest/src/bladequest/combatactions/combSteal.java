package bladequest.combatactions;

import java.util.List;

import bladequest.world.Character;
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
	public void execute(List<Character> targets)
	{
		Enemy target = (Enemy)targets.get(0);
		
		if(target.hasItems())
		{
			String item = target.getItem(true);
			if(item == null)
			{
				//Global.battle.dmgText(target, "MISS", 0);
				//Global.battle.setFrameText("Failed to steal!");
			}
			else
			{
				Global.party.addItem(item);
				
				//Global.battle.dmgText(target, "STEAL!", 0);
				//Global.battle.setFrameText("Stole a "+ Global.items.get(item).getName() +"!");
			}
		}
		else
		{
			//Global.battle.dmgText(target, "FAIL", 0);
			//Global.battle.setFrameText("Doesn't have anything!");
		}
		
		
	}

}
