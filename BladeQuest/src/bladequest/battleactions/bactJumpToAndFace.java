package bladequest.battleactions;

import android.graphics.Point;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.BattleAnim;
import bladequest.math.PointMath;
import bladequest.world.PlayerCharacter;

public class bactJumpToAndFace extends DelegatingAction{

	float invArcFactor;
	int timeToAnimate;
	
	public bactJumpToAndFace(float invArcFactor, int time) {
		this.invArcFactor = invArcFactor;
		this.timeToAnimate = time;
	}
	@Override
	protected void buildEvents(BattleEventBuilder builder) {
		PlayerCharacter attacker = builder.getSource();
		PlayerCharacter target = BattleAction.getTarget(builder);
		
		if (attacker == target) return;
		
		Point p = PointMath.getStabPoint(attacker, target);//target.getPosition(true);
		Point pTopLeft = new Point(p);
		PointMath.toTopLeft(pTopLeft, target);
		
		BattleAnim jump = PointMath.buildJumpAnimation(attacker, attacker.getPosition(true), p, 1.0f/invArcFactor,timeToAnimate);
		
		builder.addEventObject(new bactChangeVisibility(false));
		builder.addEventObject(new bactRunAnimation(jump).addDependency(builder.getLast()));
		builder.addEventObject(new bactChangePosition(pTopLeft).addDependency(builder.getLast()));
		builder.addEventObject(new bactChangeVisibility(true).addDependency(builder.getLast()));
		if (target.isEnemy() != attacker.isEnemy())
		{
			builder.addEventObject(new bactMirror(false).addDependency(builder.getLast()));	
		}
		else
		{
			builder.addEventObject(new bactMirror(true).addDependency(builder.getLast()));
		}
	}

}
