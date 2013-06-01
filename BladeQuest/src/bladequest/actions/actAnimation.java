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
	boolean wait, stoppingShort;
	float stopShortTime;
	long duration, startTime;
	
	public actAnimation(AnimationBuilder animBuilder, String source, String target, boolean wait)
	{
		this.animBuilder = animBuilder;
		this.target = target;
		this.source = source;
		this.wait = wait;
		this.stoppingShort = false;
	}
	
	public actAnimation(AnimationBuilder animBuilder, String source, String target, float secondsShort)
	{
		this.animBuilder = animBuilder;
		this.target = target;
		this.source = source;
		this.wait = true;
		this.stoppingShort = true;
		this.stopShortTime = secondsShort;
		
	}
	
	@Override
	public void run()
	{
		if (playingAnim == null)
		{
			Point sourceP = Global.getVPCoordsFromObject(source);
			Point targetP = Global.getVPCoordsFromObject(target);
			
			playingAnim = Global.playAnimation(animBuilder.buildAnimation(null), sourceP, targetP);
			duration = playingAnim.getDuration();
			startTime = System.currentTimeMillis();
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
		boolean stopShort = (stoppingShort && 
				System.currentTimeMillis() - startTime >= 
				duration - stopShortTime*1000.0f);
		
		return stopShort || (!stoppingShort && (!wait || playingAnim.Done()));
	}
}
