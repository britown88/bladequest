package bladequest.battleactions;

import java.util.List;

import bladequest.combat.DamageMarker;
import bladequest.statuseffects.StatusEffect;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class bactLureEnemies extends BattleAction {

	int lureSteps;
	public bactLureEnemies(int lureSteps) 
	{
		this.lureSteps = lureSteps;
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
					remainingSteps = lureSteps;
				    hidden = false;
					negative = false;
					removeOnDeath = false;
					curable = false;
					battleOnly = false;
				}
				
				@Override
				public StatusEffect clone() {
					return this;
				}
				@Override
				public void onInflict(PlayerCharacter c) 
				{
					Global.menu.showMessage(c.getDisplayName() + " started ringing the lure bell!", false);
				}
				
				@Override
				public void worldEffect() 
				{
					if (!Global.party.getAllowMovement()) return;
					if (remainingSteps-- == 0)
					{
						for (PlayerCharacter t : Global.party.getPartyList(true))
						{
							t.removeStatusEffect("lure");
						}
						Global.party.clearMovementPath();
						Global.showMessage("The lure bell stopped ringing...", false);
					}
				}
			});
			return;
		}
	}
	@Override
	public boolean willAffectTarget(PlayerCharacter target)
	{
		for (PlayerCharacter t : Global.party.getPartyList(true))
		{
			if (t.hasStatus("lure")) return false;
		}		
		return true;
	}
}
