package bladequest.battleactions;

import java.util.List;

import android.graphics.Point;
import bladequest.combat.DamageMarker;
import bladequest.world.PlayerCharacter;

public class bactChangePosition extends BattleAction {

	Point newPosition;
	public bactChangePosition(int frame, Point newPosition) {
		super(frame);
		this.newPosition = newPosition;
	}
	@Override
	public void run(PlayerCharacter victim, List<PlayerCharacter> unused, List<DamageMarker> markers)
	{
		victim.setPosition(newPosition);
	}
}
