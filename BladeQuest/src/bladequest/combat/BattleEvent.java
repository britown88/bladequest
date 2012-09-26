package bladequest.combat;

import java.util.ArrayList;
import java.util.List;

import bladequest.world.Character;
import bladequest.world.TargetTypes;

public class BattleEvent 
{
	private Character source;
	private List<Character> targets;
	
	public BattleEvent(Character source, List<Character> targets)
	{
		this.source = source;
		this.targets = new ArrayList<Character>(targets);		
	}
	
	public Character getSource() { return source; }
	public List<Character> getTargets() { return targets;}
	

	
	
	

}
