package bladequest.combat;

import java.util.ArrayList;
import java.util.List;

import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.Stats;
import bladequest.world.PlayerCharacter;
import bladequest.world.PlayerCharacter.Action;

public class BattleCalc 
{

	public enum AccuracyType
	{
		Regular,
		NoMiss,
		ReplaceEvade,
		WithEvade
	}
	
	public static final float maxEvade = 90.0f;
	public static final float minEvade = 5.0f;
	public static final float maxCrit = 90.0f;
	public static final float minCrit = 5.0f;
	
	public static final float maxLevel = 99.0f;
	public static final float maxHP = 9999.0f;
	public static final float maxMP = 999.0f;
	public static final float maxStat = 255.0f;
	
	public static int calculateCurvedHPMPValue(float finalStat, float level, float maximum)
	{		
		float invLevel = level / maxLevel;
		float curve = 0.3f + (invLevel * 0.7f);
		float invFinalStat = finalStat / maxStat;
		
		float ret = curve * invLevel * (
					(maximum * invFinalStat * 0.2f) +
					(maximum * 0.8f) +
					invLevel * 0.4f * maximum * invFinalStat -
					invLevel * 0.4f * maximum
				);
		
		return (int)(Math.max(1, Math.min(maximum, ret)));		
	}
	
	
	private static DamageReturnType damageReturnType;	
	public static DamageReturnType getDmgReturnType(){return damageReturnType;}
	
	private static int getStandardEvasion(PlayerCharacter character, DamageTypes type)
	{
		switch (type)
		{
		case PhysicalIgnoreDef:	
		case Physical:
			return (int)((float)character.getStat(Stats.Evade)*(maxEvade/255.0f));
		default:
			return 0;
		} 
	}
	
	public static int calculatedDamage(PlayerCharacter attacker, PlayerCharacter defender, float power, DamageTypes type, List<DamageComponent> arrayList, float customMiss, AccuracyType accuracy)
	{
		boolean hitStatusApplied = false;
		
		attacker.updateSecondaryStats();
		defender.updateSecondaryStats();
		
		boolean physical = type == DamageTypes.Physical || type == DamageTypes.PhysicalIgnoreDef; 
		boolean ignoreDef = type == DamageTypes.MagicalIgnoreDef || type == DamageTypes.PhysicalIgnoreDef;
				
		int AP = physical ? attacker.getStat(Stats.BattlePower) : attacker.getStat(Stats.MagicPower);
		float DP = physical ? defender.getStat(Stats.Defense) : defender.getStat(Stats.MagicDefense);
		
		//guarding
		//if(defender.getAction() == Action.Guard) DP *= 1.5f;		
		if (ignoreDef) DP = 0.0f;
		
		float attackerTypeMod = attacker.isEnemy() ? 7.0f : 10.0f;
		
		float rawDamage = AP * attackerTypeMod;
		float reduceFactor = (float) Math.pow(DP/255.0, 0.7);
		float variance = ((float)Global.rand.nextInt(20) - 10.0f)/100.0f;	
		
		float moddedDmg = rawDamage * (1.0f - reduceFactor) * (1.0f + variance);
	
		int evade = 0;
		int standardEvade = getStandardEvasion(defender, type);
		int roll;
		switch (accuracy)
		{
		case Regular:
			evade = standardEvade;
			break;
		case NoMiss: evade = 0; break;
		case ReplaceEvade:
			evade = (int)(customMiss*100);
		case WithEvade:
			roll = Global.rand.nextInt(100);
			evade = (int)(customMiss*100);
			if(roll < standardEvade)
			{
				damageReturnType = DamageReturnType.Miss;
				hitStatusApplied = true;
			}
			break;
		}
		
		roll = Global.rand.nextInt(100);
		
		if(roll < evade)
		{
			damageReturnType = DamageReturnType.Miss;
			hitStatusApplied = true;
		}
		
		
		float finalDmg = 0.0f;
		
		switch(type)
		{
		case Fixed:
			damageReturnType = DamageReturnType.Hit;
			finalDmg = power;
			break;
		case PhysicalIgnoreDef:	
		case Physical:
				roll = Global.rand.nextInt(100);
				int blockChance = (int)((float)defender.getStat(Stats.Block)*(90.0f/255.0f));				
				if(roll < blockChance && !hitStatusApplied)
				{
					hitStatusApplied = true;
					damageReturnType = DamageReturnType.Blocked;
				}

				finalDmg = applyAffinities(moddedDmg, attacker, defender, arrayList);
				
				roll = Global.rand.nextInt(100);
				int critChance = (int)((float)attacker.getStat(Stats.Crit)*(maxCrit/255.0f));
				
				if(roll < critChance && !hitStatusApplied)
				{
					damageReturnType = DamageReturnType.Critical;
					hitStatusApplied = true;
					finalDmg *= 2.0f;
				}
				else
				{
					if (!hitStatusApplied)
					{
						damageReturnType = DamageReturnType.Hit;
					}
				}
									
					
			break;
		case Magic:
		case MagicalIgnoreDef:
			finalDmg = applyAffinities(moddedDmg, attacker, defender, arrayList);
			if (!hitStatusApplied)
			{
				damageReturnType = DamageReturnType.Hit;
			}			
			break;
		}
		
		if(power > 0)
		{
			if(defender.getAction() == Action.Guard || (!attacker.isEnemy() && !defender.isEnemy()))
				finalDmg *= 0.5f;	
			
			finalDmg = Math.max(0, finalDmg - defender.getStat(Stats.DamageIgnore) * 10.0f);
		}		
			
		finalDmg = Math.max(-9999, Math.min(9999, finalDmg));
		
		return (int)finalDmg;
	}
	
	private static float applyAffinities(float damage, PlayerCharacter attacker, PlayerCharacter defender, List<DamageComponent> damageComponents)
	{
		float neutralComponent = 1.0f;
		for(DamageComponent dc : damageComponents)
		{
			if(dc.getAffinity() == null)
			{
				neutralComponent = dc.getPower();
				damageComponents.remove(dc);
				break;
			}
			else
				neutralComponent -= dc.getPower();
		}
		
		List<Float> damageParts = new ArrayList<Float>();
		if(neutralComponent > 0.0f)
			damageParts.add(damage * neutralComponent);
		
		for(DamageComponent dc : damageComponents)
		{
			if(dc != null)
				damageParts.add(
						applyAffinityMods(
								dc.getPower() * damage, 
								attacker.getStat(dc.getAffinity()), 
								defender.getStat(dc.getAffinity())));
			
		}
		
		float finalDamage = 0.0f;
		for(Float f : damageParts)
			finalDamage += f;
		
		return finalDamage;
			
	}
	
	private static float applyAffinityMods(float base, float attackerAffinity, float defenderAffinity)
	{
		base *= (attackerAffinity/100.0f);
		
		//only apply defense on attacks, not heals
		if(base > 0)
			base *= (200.0f-defenderAffinity)/100.0f;	
		else
			base *= (defenderAffinity/100.0f);
		
		return base;
	}
	
	//returns whether rhs has priority over lhs
	private static boolean checkMovePriority(BattleEvent rhs, BattleEvent lhs)
	{
		if (rhs.getPriority().ordinal() < lhs.getPriority().ordinal()) return true;
		if (rhs.getPriority().ordinal() > lhs.getPriority().ordinal()) return false;
		return rhs.getSource().getStat(Stats.Speed) > lhs.getSource().getStat(Stats.Speed);
		
	}
	
	//generate move order using quicksort
	public static List<BattleEvent> genMoveOrder(List<BattleEvent> events)
	{
		if(events.size() <= 1)
			return events;
		
		BattleEvent pivot = events.get(events.size()/2);		
		events.remove(pivot);
		
		List<BattleEvent> greater = new ArrayList<BattleEvent>();
		List<BattleEvent> less = new ArrayList<BattleEvent>();
		
		for(BattleEvent be : events)
			if(!checkMovePriority(be, pivot))
				less.add(be);
			else
				greater.add(be);
		
		
		List<BattleEvent> returnList = new ArrayList<BattleEvent>(genMoveOrder(greater));
		returnList.add(pivot);
		returnList.addAll(genMoveOrder(less));		
		
		return returnList;
	}
	
	public enum DamageReturnType
	{
		Hit,
		Miss,
		Blocked,
		Critical
	}

	
	//top of the list == faster
	public enum MovePriority
	{
		Item,
		High,
		Regular,
		Low
	}

}
