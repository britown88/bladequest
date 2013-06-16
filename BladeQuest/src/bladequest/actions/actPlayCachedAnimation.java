package bladequest.actions;

import android.graphics.Point;
import bladequest.graphics.AnimationBuilder;
import bladequest.graphics.AnimationPosition;
import bladequest.graphics.BattleAnim;
import bladequest.world.Global;

public class actPlayCachedAnimation extends Action
{
	BattleAnim anim;
	String source, target;
	Point gridSrc, gridTar;
	BattleAnim playingAnim;
	boolean wait, stoppingShort;
	float stopShortTime;
	long duration, startTime;
	
	public actPlayCachedAnimation(AnimationBuilder animBuilder, String source, String target, boolean wait)
	{
		this.anim = animBuilder.buildAnimation(null);
		this.target = target;
		this.source = source;
		this.wait = wait;
		this.stoppingShort = false;
	}
	
	public actPlayCachedAnimation(AnimationBuilder animBuilder, Point source, Point target, boolean wait)
	{
		this.anim = animBuilder.buildAnimation(null);
		this.gridTar = new Point(target.x*32, target.y*32);
		this.gridSrc = new Point(source.x*32, source.y*32);
		this.wait = wait;
		this.stoppingShort = false;
	}
	
	public actPlayCachedAnimation(AnimationBuilder animBuilder, String source, String target, float secondsShort)
	{
		this.anim = animBuilder.buildAnimation(null);
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
			//grid position
			if(gridTar != null)
			{
				playingAnim = Global.playAnimation(
						anim, 
						new AnimationPosition() {
							private Point src, tar;
							
							public AnimationPosition init(Point source, Point target)
							{
								this.src = source;
								this.tar = target;					
								return this;
							}			
							
							@Override
							public Point getTarget() {
								return Global.gridPosToVP(tar);
							}
							
							@Override
							public Point getSource() {
								return Global.gridPosToVP(src);
							}
						}.init(gridSrc, gridTar));
				
			}
			//object position
			else
			{
				playingAnim = Global.playAnimation(
						anim, 
						new AnimationPosition() {
							private String src, tar;
							
							public AnimationPosition init(String source, String target)
							{
								this.src = source;
								this.tar = target;					
								return this;
							}			
							
							@Override
							public Point getTarget() {
								return Global.getVPCoordsFromObject(tar);
							}
							
							@Override
							public Point getSource() {
								return Global.getVPCoordsFromObject(src);
							}
						}.init(source, target));
				
			}
			
			
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
