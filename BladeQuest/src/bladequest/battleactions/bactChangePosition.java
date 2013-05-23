package bladequest.battleactions;

import android.graphics.Point;
import bladequest.combat.BattleEventBuilder;

public class bactChangePosition extends BattleAction {

	Point newPosition;
	public bactChangePosition(Point newPosition) {
		this.newPosition = newPosition;
	}
	@Override
	public State run(BattleEventBuilder builder)
	{
		builder.getSource().setPosition(newPosition);
		return State.Finished;
	}
}
