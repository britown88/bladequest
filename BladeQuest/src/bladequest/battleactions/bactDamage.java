package bladequest.battleactions;

import java.util.ArrayList;
import java.util.List;

import bladequest.combat.BattleCalc;
import bladequest.combat.BattleCalc.DamageReturnType;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.DamageComponent;
import bladequest.combat.DamageMarker;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;
import bladequest.world.Stats;

public class bactDamage extends DelegatingAction
{
	float power;
	DamageTypes type;
	List<DamageComponent> damageComponents;
	float customMiss;
	BattleAction onHit;
	
	public static DamageBuilder triggerDamageBuilder;
	
	BattleCalc.AccuracyType accuracyType;
	
	
	private class TriggerDamageBuilder implements DamageBuilder
	{
		int damage;
		DamageReturnType returnType;
		List<DamageComponent> componentTypes;
		DamageTypes damageType;
		PlayerCharacter attacker, defender;
		
		TriggerDamageBuilder(PlayerCharacter attacker, PlayerCharacter defender, int damage, DamageReturnType returnType, List<DamageComponent> componentTypes, DamageTypes damageType)
		{
			this.damage = damage;
			this.returnType = returnType;
			this.componentTypes = componentTypes;			
			this.damageType = damageType;
			this.attacker = attacker;
			this.defender = defender;
		}
		
		@Override
		public int getDamage() {
			return damage;
		}

		@Override
		public void setDamage(int damage) {
			this.damage = damage; 
		}

		@Override
		public DamageReturnType attackType() {
			return returnType;
		}

		@Override
		public void setAttackType(DamageReturnType type) {
			returnType = type;
		}

		@Override
		public List<DamageComponent> getDamageComponents() {
			return componentTypes;
		}

		@Override
		public DamageTypes getDamageType() {
			return damageType;
		}

		@Override
		public PlayerCharacter getAttacker() {
			return attacker;
		}

		@Override
		public PlayerCharacter getDefender() {
			return defender;
		}
		
	};
	
	public bactDamage(float power, DamageTypes type)
	{
		this.damageComponents = new ArrayList<DamageComponent>();
		this.power = power;
		this.type = type;
		this.customMiss = 0.0f;
		this.accuracyType = BattleCalc.AccuracyType.Regular;
	}
	
	public bactDamage(float power, DamageTypes type, BattleCalc.AccuracyType accuracy, float missChance)
	{
		this.damageComponents = new ArrayList<DamageComponent>();
		this.power = power;
		this.type = type;
		this.customMiss = missChance;
		this.accuracyType = accuracy;
	}	
	
	public void addDamageComponent(Stats affinity, float power)
	{
		damageComponents.add(new DamageComponent(affinity, power));
	}
	
	
	void setOnHit(BattleAction onHit)
	{
		this.onHit = onHit;
	}
	
	@Override
	//public State run(BattleEventBuilder builder)
	public void buildEvents(BattleEventBuilder builder)
	{

		PlayerCharacter attacker = builder.getSource();
		PlayerCharacter target =  BattleAction.getTarget(builder);
	
		int dmg = BattleCalc.calculatedDamage(attacker, target, power, type, damageComponents, customMiss, accuracyType);
		
		TriggerDamageBuilder triggerSettings = new TriggerDamageBuilder(attacker, target, dmg, BattleCalc.getDmgReturnType(), damageComponents, type); 

		triggerDamageBuilder = triggerSettings; 
		//CALLS GENERIC CODE OH SHIT OH FUCK TRIGGER WARNING
		target.getOnDamagedEvent().trigger();
		Global.battle.getOnDamageDealt().trigger();
		//WARNING WARNING DANGER DANGER GAME STATE DESTROYED					
		
		
		switch(triggerSettings.attackType())
		{
		case Blocked:
			builder.addMarker(new DamageMarker("BLOCK", target));	
			break;
		case Critical:
			Global.screenFader.setFadeColor(255, 255, 255, 255);
			Global.screenFader.flash(0.25f);
		case Hit:
			if(dmg >= 0 && !target.isEnemy())
				target.showDamaged();
			target.modifyHP(-triggerSettings.getDamage(), false);
			builder.addMarker(new DamageMarker(-triggerSettings.getDamage(), target));	
			if (onHit != null)
			{
				builder.addEventObject(onHit);
			}
			break;
		case Miss:
			builder.addMarker(new DamageMarker("MISS", target));	
			break;
		}	
	}
	
	@Override
	public void runOutsideOfBattle(PlayerCharacter attacker, List<PlayerCharacter> targets, List<DamageMarker> markers)
	{
		for(PlayerCharacter t : targets)
		{
			int dmg = BattleCalc.calculatedDamage(attacker, t, power, type, damageComponents, customMiss, accuracyType);
			switch(BattleCalc.getDmgReturnType())
			{
			case Blocked:
				markers.add(new DamageMarker("BLOCK", t));	
				break;
			case Critical:
				markers.add(new DamageMarker("CRIT", t));	
			case Hit:
				t.modifyHP(-dmg, false);
				markers.add(new DamageMarker(-dmg, t));	
				break;
			case Miss:
				markers.add(new DamageMarker("MISS", t));	
				break;
			}	
		}
	}
	
	@Override
	public boolean willAffectTarget(PlayerCharacter target) 
	{		
		return target.isInBattle() && 
				((target.getHP() < target.getStat(Stats.MaxHP) 
				&& power < 0) || power >= 0); 
	}

}
