package bladequest.battleactions;

import android.graphics.Point;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.SyncronizableAction;
import bladequest.graphics.BattleAnim;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class bactRunAnimation extends SyncronizableAction {

	BattleAnim animation, playingAnim;
	public bactRunAnimation(BattleAnim animation) {
		this.animation = animation;
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
			
			playingAnim = Global.playAnimation(animation, builder.getSource().getPosition(true), targetP);
			return State.Continue;
		}
		else
		{
			if (playingAnim.Done()) return State.Finished;
			return State.Continue;
		}
	}
	
	@Override
	public int syncronizeTime(float factor) {
		return animation.syncToAnimation(factor);
	}
}
