package bladequest.battleactions;

import java.util.ArrayList;
import java.util.List;

import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.BattleEventObject;
import bladequest.combat.DamageMarker;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleSprite.faces;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class bactDelayedDamageAll extends BattleAction {
	
	String animation; 
	int frameDelay;
	BattleEventBuilder builder;
	float power;
	DamageTypes type;
	
	public bactDelayedDamageAll(int frame, float power, DamageTypes type, String animation, int frameDelay) {
		super(frame);
		this.animation = animation;
		this.frameDelay = frameDelay;
		this.power = power;
		this.type = type;
	}
	public void run(PlayerCharacter attacker, List<PlayerCharacter> targets, List<DamageMarker> markers)
	{
		int frameNum = getFrame();
		BattleAnim anim = Global.battleAnims.get(animation);
		int endFrameTime = BattleEvent.frameFromActIndex(frameNum);
		builder.addEventObject(new BattleEventObject(endFrameTime, faces.Cast, 0, attacker));
		for(PlayerCharacter t : targets)
		{
			List<PlayerCharacter> target = new ArrayList<PlayerCharacter>();
			target.add(t);
			
			int frameTime = BattleEvent.frameFromActIndex(frameNum);
			endFrameTime = anim.syncToAnimation(1.0f) + frameTime;
			//add animation and damage calls.
			builder.addEventObject(new BattleEventObject(frameTime, anim, attacker, target));
			builder.addEventObject(new BattleEventObject(endFrameTime, new bactDamage(endFrameTime, power, type), attacker, target));
			
			frameNum += frameDelay;
		}
		builder.addEventObject(new BattleEventObject(endFrameTime, faces.Ready, 0, attacker));
		//multiple done actions is legit.  Only the last one counts.
		builder.addEventObject(new BattleEventObject(endFrameTime));
	}
	public void setBuilder(BattleEventBuilder builder) 
	{
		this.builder = builder;
	}
}
