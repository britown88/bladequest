package bladequest.battleactions;

import android.graphics.Point;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.AnimationBuilder;
import bladequest.graphics.BattleAnim;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class bactRunAnimationBuilder extends BattleAction {

	AnimationBuilder animBuilder;
	BattleAnim playingAnim;
	public bactRunAnimationBuilder(AnimationBuilder animBuilder) {
		this.animBuilder = animBuilder;
	}
	
	@Override
	public void initialize()
	{
		playingAnim = null;
	}
	
	@Override
	public State run(BattleEventBuilder builder)
	{
		if (playingAnim == null)
		{
			PlayerCharacter target = getTarget(builder);
			Point targetP = null;
			if(target != null)
				targetP = target.getPosition(true);
			
			playingAnim = Global.playAnimation(animBuilder.buildAnimation(builder), builder.getSource().getPosition(true), targetP);
			return State.Continue;
		}
		else
		{
			if (playingAnim.Done()) return State.Finished;
			return State.Continue;
		}
	}
	

}
