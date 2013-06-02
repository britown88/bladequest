package bladequest.battleactions;

import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.SyncronizableAction;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleSprite.faces;
import bladequest.world.PlayerCharacter;

public class bactSlash extends DelegatingAction {

	BattleAction onSlash;
	float speedFactor;
	public bactSlash(BattleAction onSlash, float speedFactor) {
		this.onSlash = onSlash;
		this.speedFactor = speedFactor;
	}
	@Override
	protected void buildEvents(BattleEventBuilder builder) {
		// TODO Auto-generated method stub
		PlayerCharacter attacker = builder.getSource();

		BattleAnim anim = attacker.getWeaponAnimation();
		builder.addEventObject(new bactSetFace(faces.Ready, 0));
		//wait two frames...
		int frameLen = (int)(BattleEvent.frameFromActIndex(1) * speedFactor);
		builder.addEventObject(new bactWait(frameLen*2));
		builder.addEventObject(new bactSetFace(faces.Attack, 0).addDependency(builder.getLast()));
		builder.addEventObject(new bactWait(frameLen*3));
		BattleAction animStartAction = builder.getLast();
		
		if (anim != null)
		{
			SyncronizableAction animationAction = new bactRunAnimation(new BattleAnim(anim, speedFactor));
			builder.addEventObject(animationAction.addDependency(animStartAction));	
			builder.addEventObject(new bactWait(animationAction.syncronizeTime(0.5f)).addDependency(animStartAction));
			builder.addEventObject(onSlash.addDependency(builder.getLast()));
		}
		
		builder.addEventObject(new bactSetFace(faces.Attack, 1).addDependency(animStartAction));
		builder.addEventObject(new bactWait(frameLen*4));
		builder.addEventObject(new bactSetFace(faces.Attack, 2).addDependency(builder.getLast()));
		builder.addEventObject(new bactWait(frameLen*5));
		builder.addEventObject(new bactSetFace(faces.Ready, 0).addDependency(builder.getLast()));
		
		if(anim == null)
		{
			builder.addEventObject(onSlash.addDependency(builder.getLast()));
		}
	}

}
