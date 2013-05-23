package bladequest.battleactions;

import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleSprite.faces;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class bactDelayedDamageAll extends DelegatingAction {
	
	String animation; 
	int frameDelay;
	BattleEventBuilder builder;
	float power;
	DamageTypes type;
	
	public bactDelayedDamageAll(float power, DamageTypes type, String animation, int frameDelay) {
		this.animation = animation;
		this.frameDelay = frameDelay;
		this.power = power;
		this.type = type;
	}
	@Override
	protected void buildEvents(BattleEventBuilder builder) {
		BattleAnim anim = Global.battleAnims.get(animation);
		
		BattleAction prevWait = null;
		builder.addEventObject(new bactSetFace(faces.Cast, 0));
		for(PlayerCharacter t : builder.getTargets())
		{
			builder.addEventObject(new TargetedAction(t)
			{
				BattleAnim anim;
				TargetedAction initialize(BattleAnim anim)
				{
					this.anim = anim;
					return this;
				}
				@Override
				protected void buildEvents(BattleEventBuilder builder) {
					builder.addEventObject(new bactRunAnimation(anim));
				}

			}.initialize(anim).addDependency(builder.getLast()));
			
			builder.addEventObject(new bactWait(BattleEvent.frameFromActIndex(frameDelay)).addDependency(prevWait));
			prevWait = builder.getLast();
		}
		builder.addEventObject(new bactSetFace(faces.Ready, 0).addDependency(prevWait));
	}
}
