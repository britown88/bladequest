package bladequest.battleactions;

import android.graphics.Point;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.BattleAnim;
import bladequest.math.PointMath;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class bactSneakToTarget extends DelegatingAction {

	
	public bactSneakToTarget() {
	}
	@Override
	protected void buildEvents(BattleEventBuilder builder) {
		PlayerCharacter attacker = builder.getSource();
		PlayerCharacter target = getTarget(builder);
		final float arcFactorFast = 1.0f/4.0f;
		final float arcFactorMisdirect = 1.0f/8.0f;
		//jump to a random point to throw off the enemy!
		
		final int perspectiveSize = 80;
		final int hitBuffer = 235;
		//a bit of buffer on the end so as to not mess with the UI
		float targetX = (float)(hitBuffer + Global.rand.nextInt(Global.vpWidth-hitBuffer-94));
		float targetY = perspectiveSize + Global.rand.nextInt(Global.vpHeight - perspectiveSize-64);
		
		Point p = new Point((int)targetX, (int)targetY);
		Point stabPoint = PointMath.getStabPoint(attacker, target);
		BattleAnim misdirection = PointMath.buildJumpAnimation(attacker, attacker.getPosition(true), p, arcFactorMisdirect,4);
		BattleAnim endJump = PointMath.buildJumpAnimation(attacker, p, stabPoint, arcFactorFast,2);
		
		PointMath.toTopLeft(p, attacker);
		PointMath.toTopLeft(stabPoint, attacker);
		
		builder.addEventObject(new bactChangeVisibility(false));
		builder.addEventObject(new bactRunAnimation(misdirection).addDependency(builder.getLast()));
		builder.addEventObject(new bactChangePosition(p).addDependency(builder.getLast()));
		builder.addEventObject(new bactChangeVisibility(true).addDependency(builder.getLast()));
		builder.addEventObject(new bactWait(150).addDependency(builder.getLast()));
		builder.addEventObject(new bactChangeVisibility(false).addDependency(builder.getLast()));
		builder.addEventObject(new bactRunAnimation(endJump).addDependency(builder.getLast()));
		builder.addEventObject(new bactChangePosition(stabPoint).addDependency(builder.getLast()));
		builder.addEventObject(new bactChangeVisibility(true).addDependency(builder.getLast()));
	}
}
