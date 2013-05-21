package bladequest.battleactions;

import java.util.List;

import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.BattleEventObject;
import bladequest.combat.DamageComponent;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleSprite.faces;
import bladequest.world.DamageTypes;
import bladequest.world.PlayerCharacter;

public class BattleActionPatterns {
	//returns length of animation.
	public static int BuildSwordSlash(BattleEventBuilder builder, PlayerCharacter attacker, List<PlayerCharacter> target, float power, DamageTypes type, int frameTime, float speedFactor)
	{
		BattleAnim anim = attacker.getWeaponAnimation();
		
		builder.addEventObject(new BattleEventObject(frameTime+(int)(BattleEvent.frameFromActIndex(0)*speedFactor), faces.Ready, 0, attacker));
		builder.addEventObject(new BattleEventObject(frameTime+(int)(BattleEvent.frameFromActIndex(2)*speedFactor), faces.Attack, 0, attacker));
		builder.addEventObject(new BattleEventObject(frameTime+(int)(BattleEvent.frameFromActIndex(3)*speedFactor), faces.Attack, 1, attacker));
		builder.addEventObject(new BattleEventObject(frameTime+(int)(BattleEvent.frameFromActIndex(4)*speedFactor), faces.Attack, 2, attacker));	
		builder.addEventObject(new BattleEventObject(frameTime+(int)(BattleEvent.frameFromActIndex(5)*speedFactor), faces.Ready, 0, attacker));			
		
		bactDamage damageAction;
		if(attacker.getWeaponAnimation() != null)
		{
			int midFrame = anim.syncToAnimation(0.5f) + frameTime+(int)(BattleEvent.frameFromActIndex(3)*speedFactor);
			
			damageAction = new bactDamage(midFrame, power, type);
			
			builder.addEventObject(new BattleEventObject(frameTime+(int)(BattleEvent.frameFromActIndex(3)*speedFactor), anim, attacker, target));
			builder.addEventObject(new BattleEventObject(midFrame, damageAction, attacker, target));
		}
		else
		{
			damageAction = new bactDamage(0, power, type);
			builder.addEventObject(new BattleEventObject((int)(BattleEvent.frameFromActIndex(5)*speedFactor), damageAction, attacker, target));
		}
		
		if(attacker.weapEquipped())
			for(DamageComponent dc : attacker.weapon().getDamageComponents())
				damageAction.addDamageComponent(dc.getAffinity(), dc.getPower());
		
		return (int)(BattleEvent.frameFromActIndex(5)*speedFactor);
	}
}
