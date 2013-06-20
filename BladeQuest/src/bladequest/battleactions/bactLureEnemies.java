package bladequest.battleactions;

import java.util.List;

import bladequest.combat.DamageMarker;
import bladequest.statuseffects.StatusEffect;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class bactLureEnemies extends BattleAction {

	public bactLureEnemies() 
	{
		
	}
	public void runOutsideOfBattle(PlayerCharacter attacker, List<PlayerCharacter> targets, List<DamageMarker> markers) 
	{
		for (PlayerCharacter t : Global.party.getPartyList(true))
		{
			t.removeStatusEffect("lure");
		}
		
		for (PlayerCharacter t : targets)
		{
			t.applyStatusEffect(new StatusEffect("lure", false)
			{
				
				int remainingSteps;

				{
				    icon = "";	
					remainingSteps = 50;
				    hidden = false;
					negative = false;
					removeOnDeath = false;
					curable = false;
					battleOnly = false;
				}
				
				@Override
				public StatusEffect clone() {
					// TODO Auto-generated method stub
					return null;
				}
				@Override
				public void worldEffect() 
				{
					if (--remainingSteps == 0)
					{
						for (PlayerCharacter t : Global.party.getPartyList(true))
						{
							t.removeStatusEffect("lure");
						}
						Global.worldMsgBox.addMessage("The lure bell stopped ringing...");
					}
				}
				
			});
			return;
		}
	}
}
