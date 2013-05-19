package bladequest.battleactions;

import java.util.List;

import bladequest.combat.DamageMarker;
import bladequest.world.PlayerCharacter;

public class bactChangeVisibility extends BattleAction {

	boolean visiblity;
	public bactChangeVisibility(int frame, boolean isVisible) {
		super(frame);
		visiblity = isVisible;
	}
	@Override
	public void run(PlayerCharacter attacker, List<PlayerCharacter> target, List<DamageMarker> markers)
	{
		attacker.setVisible(visiblity);
	}
}
