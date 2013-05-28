package bladequest.battleactions;

import android.graphics.Point;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.BattleAnim;
import bladequest.math.PointMath;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class bactJumpHome extends DelegatingAction{

	float invArcFactor;
	int timeToAnimate;
	public bactJumpHome(float invArcFactor, int time) {
		this.invArcFactor = invArcFactor;
		this.timeToAnimate = time;
	}
	@Override
	protected void buildEvents(BattleEventBuilder builder) {
		PlayerCharacter attacker = builder.getSource();
		
		Point p = Global.battle.getPlayerDefaultPosition(attacker);
		Point pTopLeft = new Point(p);
		PointMath.toCenter(p, attacker);
		
		BattleAnim jump = PointMath.buildJumpAnimation(attacker, attacker.getPosition(true), p, 1.0f/invArcFactor,timeToAnimate);
		
		builder.addEventObject(new bactChangeVisibility(false));
		builder.addEventObject(new bactRunAnimation(jump).addDependency(builder.getLast()));
		builder.addEventObject(new bactChangePosition(pTopLeft).addDependency(builder.getLast()));
		builder.addEventObject(new bactChangeVisibility(true).addDependency(builder.getLast()));
	}

}
