package bladequest.battleactions;

import java.util.List;

import android.graphics.Point;
import bladequest.UI.MsgBox.MsgBox.Position;
import bladequest.combat.DamageMarker;
import bladequest.statuseffects.StatusEffect;
import bladequest.world.EncounterZone;
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
		boolean foundZone = false;
		if (Global.map != null)
		{
			Point pos = Global.party.getGridPos();
			for (EncounterZone ez : Global.map.encounterZones)
			{
				if (ez.getZone().contains(pos.x, pos.y))
				{
					foundZone = true;
				}
			}
		}
		if (!foundZone)
		{
			Global.party.addItem("lurebell", 1);
			Global.menu.showBasicMessage("There were no nearby enemies to lure.");
			return;
		}
		
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
					Global.menu.showBasicMessage(c.getDisplayName() + " started ringing the lure bell!");
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
						Global.showBasicMessage("The lure bell stopped ringing...", Position.Bottom);
						if (Global.map != null && Global.map.isLoaded())
						{
							for(EncounterZone zone : Global.map.encounterZones)
							{
								zone.reset();
							}	
						}	
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
