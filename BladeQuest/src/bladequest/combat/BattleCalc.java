package bladequest.combat;

import java.util.ArrayList;
import java.util.List;

import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.Stats;
import bladequest.world.Character;

public class BattleCalc 
{
	
	private static DamageReturnType damageReturnType;	
	public static DamageReturnType getDmgReturnType(){return damageReturnType;}
	
	public static int calculatedDamage(Character attacker, Character defender, float power, DamageTypes type)
	{
		int AP = attacker.getBattlePower();
		float DP = defender.getDefense();
				
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
			finalDmg = baseDmg + dmgMod;
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
