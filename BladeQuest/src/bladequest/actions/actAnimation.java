package bladequest.actions;

import android.graphics.Point;
import bladequest.graphics.AnimationBuilder;
import bladequest.graphics.BattleAnim;
import bladequest.world.Global;

public class actAnimation extends Action
{
	AnimationBuilder animBuilder;
	String source, target;
	BattleAnim playingAnim;
	
	public actAnimation(AnimationBuilder animBuilder, String source, String target)
	{
		this.animBuilder = animBuilder;
		this.target = target;
		this.source = source;
	}
	
	@Override
	public void run()
	{
		if (playingAnim == null)
		{
			Point sourceP = Global.getVPCoordsFromObject(source);
			Point targetP = Global.getVPCoordsFromObject(target);
			
			playingAnim = Global.playAnimation(animBuilder.buildAnimation(null), sourceP, targetP);
		}
	}
	
	@Override
	public void reset()
	{
		playingAnim = null;
	}
	
	@Override
	public boolean isDone()
	{
		return playingAnim.Done();
	}
}
