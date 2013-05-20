package bladequest.battleactions;

import java.util.List;

import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.BattleEventObject;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleSprite.faces;
import bladequest.world.DamageTypes;
import bladequest.world.PlayerCharacter;

public class BattleActionPatterns {
	//returns length of animation.
	static int BuildSwordSlash(BattleEventBuilder builder, PlayerCharacter attacker, List<PlayerCharacter> target, float power, DamageTypes type, int frameTime, float speedFactor)
	{
		BattleAnim anim = attacker.getWeaponAnimation();
		int midFrame = anim.syncToAnimation(0.5f) + frameTime+(int)(BattleEvent.frameFromActIndex(3)*speedFactor);
		
		builder.addEventObject(new BattleEventObject(frameTime+(int)(BattleEvent.frameFromActIndex(0)*speedFactor), faces.Ready, 0, attacker));
		builder.addEventObject(new BattleEventObject(frameTime+(int)(BattleEvent.frameFromActIndex(2)*speedFactor), faces.Attack, 0, attacker));
		builder.addEventObject(new BattleEventObject(frameTime+(int)(BattleEvent.frameFromActIndex(3)*speedFactor), faces.Attack, 1, attacker));
		builder.addEventObject(new BattleEventObject(frameTime+(int)(BattleEvent.frameFromActIndex(4)*speedFactor), faces.Attack, 2, attacker));	
		builder.addEventObject(new BattleEventObject(frameTime+(int)(BattleEvent.frameFromActIndex(5)*speedFactor), faces.Ready, 0, attacker));			
		builder.addEventObject(new BattleEventObject(frameTime+(int)(BattleEvent.frameFromActIndex(3)*speedFactor), anim, attacker, target));
		builder.addEventObject(new BattleEventObject(midFrame, new bactDamage(midFrame, power, type), attacker, target));
		return (int)(BattleEvent.frameFromActIndex(5)*speedFactor);
	}
}
