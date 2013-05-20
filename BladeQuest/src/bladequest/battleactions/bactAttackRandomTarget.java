package bladequest.battleactions;

import java.util.ArrayList;
import java.util.List;

import bladequest.combat.BattleEventBuilder;
import bladequest.combat.DamageMarker;
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
		
		targets = Global.battle.getTargetable(attacker, targets);
		if (!targets.isEmpty())
		{
			List<PlayerCharacter> target = new ArrayList<PlayerCharacter>();
			target.add(targets.get(Global.rand.nextInt(targets.size())));
			
			BattleActionPatterns.BuildSwordSlash(builder, attacker, target, power, type, frameTime, 1.0f/speedFactor);			
		}
	}
	public void setBuilder(BattleEventBuilder builder) 
	{
		this.builder = builder;
	}
}
