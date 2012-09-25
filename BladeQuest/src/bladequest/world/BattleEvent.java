package bladequest.world;

import java.util.List;

public class BattleEvent 
{
	private Character source;
	private List<Character> targets;
	private TargetTypes targetType;
	
	
	
	public enum ActionTypes
	{
		Attack,
		Item,
		Guard,
		CombatAction,
		Ability
	}

}
