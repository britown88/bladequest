package bladequest.combat;

import java.util.ArrayList;
import java.util.List;

import bladequest.world.Stats;

public class BattleCalc 
{
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
		
		
		List<BattleEvent> returnList = new ArrayList<BattleEvent>(genMoveOrder(less));
		returnList.add(pivot);
		returnList.addAll(genMoveOrder(greater));		
		
		return returnList;
	}
	
	/*function quicksort('array')
    if length('array') <_ 1
        return 'array'  // an array of zero or one elements is already sorted
    select and remove a pivot value 'pivot' from 'array'
    create empty lists 'less' and 'greater'
    for each 'x' in 'array'
        if 'x' <_ 'pivot' then append 'x' to 'less'
        else append 'x' to 'greater'
    return concatenate(quicksort('less'), 'pivot', quicksort('greater'))*/

}
