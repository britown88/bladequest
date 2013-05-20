package bladequest.battleactions;

import java.util.List;

import bladequest.combat.DamageMarker;
import bladequest.world.PlayerCharacter;

public class bactUseItem extends BattleAction
{
	public bactUseItem(int frame) {
		super(frame);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void run(PlayerCharacter attacker, List<PlayerCharacter> target, List<DamageMarker> markers)
	{
		//resets item count and removes from inventory
		attacker.useItem();
	}

	
	

}
