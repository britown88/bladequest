package bladequest.battleactions;

import java.util.ArrayList;
import java.util.List;

import bladequest.combat.BattleCalc;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.DamageComponent;
import bladequest.world.DamageTypes;
import bladequest.world.PlayerCharacter;
import bladequest.world.Stats;

public class bactDamageGroup extends DelegatingAction {

	float power;
	DamageTypes type;
	List<DamageComponent> damageComponents;
	float customMiss;
	
	public static DamageBuilder triggerDamageBuilder;
	
	BattleCalc.AccuracyType accuracyType;
	
	public bactDamageGroup(float power, DamageTypes type, BattleCalc.AccuracyType accuracy, float missChance)
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
	@Override
	protected void buildEvents(BattleEventBuilder builder) {
		
		for (PlayerCharacter t : builder.getTargets())
		{
			builder.addEventObject(new TargetedAction(t)
			{
				protected void buildEvents(BattleEventBuilder builder) {
					bactDamage damage = new bactDamage(power, type, accuracyType, customMiss);
					for (DamageComponent comp : damageComponents)
					{
						damage.addDamageComponent(comp.getAffinity(), comp.getPower());
					}
					builder.addEventObject(damage);
				}
			});
		}		
	}	

}
