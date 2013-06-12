package bladequest.battleactions;

import bladequest.combat.BattleCalc;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.DamageComponent;
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
		
		if(attacker.weapEquipped())
			for(DamageComponent dc : attacker.weapon().getDamageComponents())
				damageAction.addDamageComponent(dc.getAffinity(), dc.getPower());
		
		
		bactSlash slash = new bactSlash(damageAction, speedFactor);
		
		builder.addEventObject(slash.addDependency(prev));
		
		return slash;
	}
	public static BattleAction BuildSwordSlash(BattleEventBuilder builder, float power, DamageTypes type, float speedFactor, BattleAction prev)
	{
		return BuildSwordSlashImpl(builder, power, type, speedFactor, null, BattleCalc.AccuracyType.Regular, 0.0f);
	}
}
