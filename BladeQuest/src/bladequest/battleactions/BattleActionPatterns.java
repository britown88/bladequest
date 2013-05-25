package bladequest.battleactions;

import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.DamageComponent;
import bladequest.combat.SyncronizableAction;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleSprite.faces;
import bladequest.world.DamageTypes;
import bladequest.world.Item;
import bladequest.world.PlayerCharacter;

public class BattleActionPatterns {
	
	public static BattleAction BuildItemUse(BattleEventBuilder builder)
	{
		Item itm = builder.getSource().getItemToUse();
		
		for (BattleAction action : itm.getActions())
		{
			builder.addEventObject(action);
		}
		
		return builder.getLast();
	}
	
	public static BattleAction BuildSwordSlash(BattleEventBuilder builder, float power, DamageTypes type, float speedFactor)
	{
		return BuildSwordSlash(builder, power, type, speedFactor, null);
	}
	public static BattleAction BuildSwordSlash(BattleEventBuilder builder, float power, DamageTypes type, float speedFactor, BattleAction prev)
	{
		PlayerCharacter attacker = builder.getSource();

		BattleAnim anim = attacker.getWeaponAnimation();
		builder.addEventObject(new bactSetFace(faces.Ready, 0).addDependency(prev));
		//wait two frames...
		int frameLen = (int)(BattleEvent.frameFromActIndex(1) * speedFactor);
		builder.addEventObject(new bactWait(frameLen*2));
		builder.addEventObject(new bactSetFace(faces.Attack, 0).addDependency(builder.getLast()));
		builder.addEventObject(new bactWait(frameLen*3));
		BattleAction animStartAction = builder.getLast();
		
		bactDamage damageAction = new bactDamage(power, type);
		if (anim != null)
		{
			SyncronizableAction animationAction = new bactRunAnimation(new BattleAnim(anim, speedFactor));
			builder.addEventObject(animationAction.addDependency(animStartAction));	
			builder.addEventObject(new bactWait(animationAction.syncronizeTime(0.5f)).addDependency(animStartAction));
			builder.addEventObject(damageAction.addDependency(builder.getLast()));
		}
		
		
		builder.addEventObject(new bactSetFace(faces.Attack, 1).addDependency(animStartAction));
		builder.addEventObject(new bactWait(frameLen*4));
		builder.addEventObject(new bactSetFace(faces.Attack, 2).addDependency(builder.getLast()));
		builder.addEventObject(new bactWait(frameLen*5));
		builder.addEventObject(new bactSetFace(faces.Ready, 0).addDependency(builder.getLast()));			
		
		if(anim == null)
		{
			builder.addEventObject(damageAction.addDependency(builder.getLast()));
		}
		
		if(attacker.weapEquipped())
			for(DamageComponent dc : attacker.weapon().getDamageComponents())
				damageAction.addDamageComponent(dc.getAffinity(), dc.getPower());
		
		return builder.getLast();
	}
}
