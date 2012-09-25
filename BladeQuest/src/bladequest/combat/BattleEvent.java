package bladequest.combat;

import java.util.ArrayList;
import java.util.List;

import bladequest.world.Character;
import bladequest.world.TargetTypes;

public class BattleEvent 
{
	private Character source;
	private List<Character> targets;
	private TargetTypes targetType;
	
	public BattleEvent(Character source, List<Character> targets, TargetTypes targetType)
	{
		this.source = source;
		this.targets = new ArrayList<Character>(targets);
		this.targetType = targetType;		
	}
	
	
	

}
