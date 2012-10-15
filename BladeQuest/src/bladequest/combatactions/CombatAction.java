package bladequest.combatactions;
import java.util.List;

import bladequest.combat.DamageMarker;
import bladequest.world.Character;
import bladequest.world.DamageTypes;
import bladequest.world.TargetTypes;

public class CombatAction 
{
	protected String name, actionText;
	protected DamageTypes type;
	protected TargetTypes targetType;
	
	public CombatAction(){}	
	public void execute(List<Character> targets, List<DamageMarker> markers){}
	
	public String getName() { return name; }
	public String getActionText() { return actionText; }
	public DamageTypes getType() { return type; }
	public TargetTypes getTargetType() { return targetType; }

}
