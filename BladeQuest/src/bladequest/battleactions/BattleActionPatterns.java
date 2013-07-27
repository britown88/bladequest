package bladequest.battleactions;

import java.util.List;

import bladequest.battleactions.BattleAction.State;
import bladequest.combat.BattleCalc;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.DamageComponent;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.Item;
import bladequest.world.PlayerCharacter;
import bladequest.world.TargetTypes;

public class BattleActionPatterns {
	
	public static BattleAction BuildItemUse(BattleEventBuilder builder)
	{
		Item itm = builder.getSource().getItemToUse();
		
		for (BattleAction action : itm.getActions())
		{
			builder.addEventObject(action);
		}
		builder.addEventObject(new bactUseItem().addDependency(builder.getLast()));
		return builder.getLast();
	}
	

	public static BattleAction BuildSwordSlashWithAccuracy(BattleEventBuilder builder, float power, DamageTypes type, float speedFactor, BattleAction prev, BattleCalc.AccuracyType accuracyType, float missChance)
	{
		return BuildSwordSlashImpl(builder, power, type, speedFactor, prev, accuracyType, missChance);
	}
	public static BattleAction BuildSwordSlashWithAccuracy(BattleEventBuilder builder, float power, DamageTypes type, float speedFactor, BattleCalc.AccuracyType accuracyType, float missChance)
	{
		return BuildSwordSlashImpl(builder, power, type, speedFactor, null, accuracyType, missChance);
	}
	public static BattleAction BuildSwordSlash(BattleEventBuilder builder, float power, DamageTypes type, float speedFactor)
	{
		return BuildSwordSlashImpl(builder, power, type, speedFactor, null, BattleCalc.AccuracyType.Regular, 0.0f);

	}
	public static BattleAction BuildSwordSlashImpl(BattleEventBuilder builder, float power, DamageTypes type, float speedFactor, BattleAction prev, BattleCalc.AccuracyType accuracyType, float missChance)
	{
		PlayerCharacter attacker = builder.getSource();
		
		bactDamage damageAction = new bactDamage(power, type, accuracyType, missChance);
		
		if(attacker.hand1Equipped())
			for(DamageComponent dc : attacker.hand1().getDamageComponents())
				damageAction.addDamageComponent(dc.getAffinity(), dc.getPower());
		
		
		bactSlash slash = new bactSlash(damageAction, PlayerCharacter.Hand.MainHand, speedFactor);
		
		builder.addEventObject(slash.addDependency(prev));
		
		return slash;
	}
	//this DOES NOT TRIGGER basic attack stuff!
	public static BattleAction  BuildHandedAttack(PlayerCharacter.Hand hand, float power, float speed)
	{
		return new DelegatingAction()
		{
			PlayerCharacter.Hand hand;
			float power, speed;
			DelegatingAction initialize(PlayerCharacter.Hand hand, float power, float speed)
			{
				this.power = power;
				this.speed = speed;
				this.hand = hand;
				return this;
			}
			@Override
			protected void buildEvents(BattleEventBuilder builder) 
			{
				PlayerCharacter attacker = builder.getSource();
				List<PlayerCharacter> targets = Global.battle.getTargetable(attacker, builder.getTargets(), TargetTypes.Single);
				if (!targets.isEmpty())
				{
					PlayerCharacter target = targets.get(0);
					

					builder.addEventObject(new TargetedAction(target)
					{
						PlayerCharacter.Hand hand;
						float speed, power;
						TargetedAction initialize(PlayerCharacter.Hand hand, float power, float speed)
						{
							this.power = power;
							this.speed = speed;
							this.hand = hand;
							return this;
						}

						@Override
						protected void buildEvents(BattleEventBuilder builder) {

													
							//figure out off-hand penalty
							if (hand == PlayerCharacter.Hand.OffHand) power *= 0.5f;
							
							PlayerCharacter attacker = builder.getSource();
							bactDamage damageAction = new bactDamage(power, DamageTypes.Physical);
							damageAction.setHand(hand);
							damageAction.onHitRunner().addEventObject(new BattleAction()
							{
								public State run(BattleEventBuilder builder)
								{
									builder.getSource().getOnPhysicalHitSuccessEvent().trigger();
									for (PlayerCharacter target : builder.getTargets()) {target.getOnPhysicalHitLandsEvent().trigger();}
									return State.Finished;
								}
							});
							
							boolean equipped = hand == PlayerCharacter.Hand.MainHand && attacker.hand1Equipped() ||
											   hand == PlayerCharacter.Hand.OffHand && attacker.hand2Equipped();
					 		
							if(equipped)
								for(DamageComponent dc : (hand == PlayerCharacter.Hand.MainHand ? attacker.hand1() : attacker.hand2()).getDamageComponents())
									damageAction.addDamageComponent(dc.getAffinity(), dc.getPower());
						    
							bactSlash slash = new bactSlash(damageAction, hand, speed);
							
							builder.addEventObject(slash);
							
							
						}
					}.initialize(hand, power, speed));
					
				}
			}
				
		}.initialize(hand, power, speed);		
	}
	
	public static BattleAction BuildSwordSlash(BattleEventBuilder builder, float power, DamageTypes type, float speedFactor, BattleAction prev)
	{
		return BuildSwordSlashImpl(builder, power, type, speedFactor, null, BattleCalc.AccuracyType.Regular, 0.0f);
	}
}
