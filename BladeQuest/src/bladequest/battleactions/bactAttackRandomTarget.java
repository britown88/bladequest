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

public class bactAttackRandomTarget  extends BattleAction {

	BattleEventBuilder builder;
	float power;
	DamageTypes type;
	int attacks;
	float speedFactor;
	
	public bactAttackRandomTarget(int frame, float power, DamageTypes type, float speedFactor, BattleEventBuilder builder) {
		super(frame);
		this.power = power;
		this.type = type;
		this.builder = builder;
		this.speedFactor = speedFactor;
	}
	public void run(PlayerCharacter attacker, List<PlayerCharacter> targets, List<DamageMarker> markers)
	{
		int frameTime = getFrame();
		BattleAnim anim = attacker.getWeaponAnimation();
		
		targets = Global.battle.getTargetable(attacker, targets);
		if (!targets.isEmpty())
		{
			List<PlayerCharacter> target = new ArrayList<PlayerCharacter>();
			target.add(targets.get(Global.rand.nextInt(targets.size())));
			int midFrame = anim.syncToAnimation(0.5f) + frameTime+(int)(BattleEvent.frameFromActIndex(3)/speedFactor);
			
			
			builder.addEventObject(new BattleEventObject(frameTime+(int)(BattleEvent.frameFromActIndex(0)/speedFactor), faces.Ready, 0, attacker));
			builder.addEventObject(new BattleEventObject(frameTime+(int)(BattleEvent.frameFromActIndex(2)/speedFactor), faces.Attack, 0, attacker));
			builder.addEventObject(new BattleEventObject(frameTime+(int)(BattleEvent.frameFromActIndex(3)/speedFactor), faces.Attack, 1, attacker));
			builder.addEventObject(new BattleEventObject(frameTime+(int)(BattleEvent.frameFromActIndex(4)/speedFactor), faces.Attack, 2, attacker));	
			builder.addEventObject(new BattleEventObject(frameTime+(int)(BattleEvent.frameFromActIndex(5)/speedFactor), faces.Ready, 0, attacker));			
			builder.addEventObject(new BattleEventObject(frameTime+(int)(BattleEvent.frameFromActIndex(3)/speedFactor), anim, attacker, target));
			builder.addEventObject(new BattleEventObject(midFrame, new bactDamage(midFrame, power, type), attacker, target));			
		}
	}
	public void setBuilder(BattleEventBuilder builder) 
	{
		this.builder = builder;
	}
}
