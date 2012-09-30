package bladequest.combat;

import java.util.ArrayList;
import java.util.List;

import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.Stats;
import bladequest.world.Character;
import bladequest.world.Character.Action;

public class BattleCalc 
{
	
	public static final float maxEvade = 90.0f;
	public static final float minEvade = 5.0f;
	
	
	private static DamageReturnType damageReturnType;	
	public static DamageReturnType getDmgReturnType(){return damageReturnType;}
	
	public static int calculatedDamage(Character attacker, Character defender, float power, DamageTypes type)
	{
		attacker.updateSecondaryStats();
		defender.updateSecondaryStats();
		
		int AP = attacker.getStat(Stats.BattlePower);
		float DP = defender.getStat(Stats.Defense);
		
		//guarding
		if(defender.getAction() == Action.Guard)
			DP *= 1.5f;
				
		int BP = (int)(AP*power);
		float coefficient = attacker == null ? 1.0f : attacker.getCoefficient();
		float defenderTypeMod = defender.isEnemy() ? 10.0f : 4.0f;
		int baseDmg = (int)((Math.max(1.0f, (BP * 2.0f) - DP) * defenderTypeMod * coefficient));		
		
		int variance = Global.rand.nextInt(20);		
		int dmgMod = (int)((float)baseDmg*(float)((variance-10)/100.0F));
		
		int finalDmg = 0;
		
		switch(type)
		{
		case Fixed:
			finalDmg = (int)power;
			break;
		case Physical:
			int roll = Global.rand.nextInt(100);
			int evadeChance = (int)((float)defender.getStat(Stats.Evade)*(maxEvade/255.0f));
			
			if(roll < evadeChance)
				damageReturnType = DamageReturnType.Miss;
			else
			{
				roll = Global.rand.nextInt(100);
				int blockChance = (int)((float)defender.getStat(Stats.Block)*(255.0f/90.0f));				
				if(roll < blockChance)
					damageReturnType = DamageReturnType.Blocked;
				else
				{
					roll = Global.rand.nextInt(100);
					if(roll < 5)
					{
						damageReturnType = DamageReturnType.Critical;
						finalDmg = (int)((float)(baseDmg + dmgMod) * 2.0f);
					}
					else
					{
						damageReturnType = DamageReturnType.Hit;
						finalDmg = baseDmg + dmgMod;
					}					
				}				
			}
			break;
		case Magic:
		case MagicalIgnoreDef:
		case PhysicalIgnoreDef:
			break;
			//TODO: configure other damage types
		}
		
		return finalDmg;
	}
	
	//returns whether rhs has priority over lhs
	private static boolean checkMovePriority(BattleEvent rhs, BattleEvent lhs)
	{
		//TODO: add more advanced move priority code here
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
	

}
