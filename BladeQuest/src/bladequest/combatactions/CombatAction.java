package bladequest.combatactions;
import java.util.*;
import bladequest.world.Character;
import bladequest.world.TargetTypes;
import bladequest.world.DamageTypes;

public class CombatAction 
{
	protected String name, actionText;
	protected DamageTypes type;
	protected TargetTypes targetType;
	
	public CombatAction(){}	
	public void execute(List<Character> targets){}
	
	public String getName() { return name; }
	public String getActionText() { return actionText; }
	public DamageTypes getType() { return type; }
	public TargetTypes getTargetType() { return targetType; }

}
