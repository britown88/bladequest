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
		final float arcFactorMisdirect = 1.0f/3.0f;
		//jump to a random point to throw off the enemy!
		
		final int perspectiveSize = 80;
		final int hitBuffer = 235;
		
		Point p = null;

		do 
		{
			//a bit of buffer on the end so as to not mess with the UI
			float targetX = (float)(hitBuffer + Global.rand.nextInt(Global.vpWidth-hitBuffer-112));
			float targetY = perspectiveSize + Global.rand.nextInt(Global.vpHeight - perspectiveSize-64);
		
			p = new Point((int)targetX, (int)targetY);

			
		} while (PointMath.length(attacker.getPosition(true), p) < 75);
		
		Point stabPoint = PointMath.getStabPoint(attacker, target);
		
		BattleAnim misdirection = PointMath.buildJumpAnimation(attacker, attacker.getPosition(true), p, arcFactorMisdirect,3);
		BattleAnim endJump = PointMath.buildJumpAnimation(attacker, p, stabPoint, arcFactorFast,1);
		
		PointMath.toTopLeft(p, attacker);
		PointMath.toTopLeft(stabPoint, attacker);
		
		builder.addEventObject(new bactChangeVisibility(false));
		builder.addEventObject(new bactRunAnimation(misdirection));
		builder.addEventObject(new bactChangePosition(p).addDependency(builder.getLast()));
		builder.addEventObject(new bactChangeVisibility(true).addDependency(builder.getLast()));
		
		BattleAction waitAction = new bactWait(150).addDependency(builder.getLast()); 
		
		builder.addEventObject(waitAction);
		builder.addEventObject(new bactChangeVisibility(false).addDependency(waitAction));
		builder.addEventObject(new bactRunAnimation(endJump).addDependency(waitAction));
		builder.addEventObject(new bactChangePosition(stabPoint).addDependency(builder.getLast()));
		builder.addEventObject(new bactChangeVisibility(true).addDependency(builder.getLast()));
	}
}
