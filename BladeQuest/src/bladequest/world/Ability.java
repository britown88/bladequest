package bladequest.world;

import java.util.ArrayList;
import java.util.List;

import bladequest.battleactions.BattleAction;
import bladequest.combat.BattleCalc.MovePriority;
import bladequest.combat.DamageMarker;

public class Ability 
{
	private String displayName;
	private TargetTypes targetType;
	private int mpcost;	
	private boolean usableOutOfBattle;
	private boolean enabled;
	private boolean advance;
	
	MovePriority priority;
	public String name, shortName, description, shortDescription;
	
	private List<BattleAction> actions;
	
	public Ability(String name, String displayName, TargetTypes targetType, int mpcost, boolean usableOutOfBattle)
	{
		this.name = name;
		this.displayName = displayName;
		this.targetType = targetType;
		this.mpcost = mpcost;
		this.usableOutOfBattle = usableOutOfBattle;
		this.enabled = true;
		this.description = "";
		this.priority = MovePriority.Regular;
		this.advance = true;
		
		actions = new ArrayList<BattleAction>();
	}
	public void setAdvances(boolean advances)
	{
		this.advance = advances;
	}
	public boolean advances()
	{
		return this.advance;
	}
	public String getShortDescription() { return shortDescription == null ? description : shortDescription;}
	public void setShortDescription(String desc) { shortDescription = desc;}
	public void setDescription(String desc)
	{
		this.description = desc;
	}
	public String getShortName() { return shortName == null ? displayName : shortName;}
	public void setShortName(String desc) { shortName = desc;}
	public String getDescription() { return description; }
	public boolean isUsableOutOfBattle() { return usableOutOfBattle;}
	public void makeUsableOutOfBattle() { usableOutOfBattle = true;}
	public void addAction(BattleAction action){actions.add(action);}
	public BattleAction lastAction(){return actions.get(actions.size()-1);}
	public List<BattleAction> getActions() 
	{
		return actions;
	}
	public String getDisplayName() { return displayName;}
	public TargetTypes TargetType() { return targetType;}
	public int MPCost() { return mpcost;}
	public boolean isEnabled() { return enabled;}
	public void setEnabled(boolean enabled) {this.enabled = enabled;}
	public MovePriority getPriority() 
	{
		return priority;
	}
	public void setPriority(MovePriority priority)
	{
		this.priority = priority;
	}
	
	
	public boolean willAffect(PlayerCharacter c)
	{
		for(BattleAction ba : actions)
			if(!ba.willAffectTarget(c))
				return false;
		
		return true;
	}
	
	public void executeOutOfBattle(PlayerCharacter attacker, List<PlayerCharacter> targets, List<DamageMarker> markers)
	{
		for(int i = 0; i < actions.size(); ++i)
			actions.get(i).runOutsideOfBattle(attacker, targets, markers);
	}
	
}
