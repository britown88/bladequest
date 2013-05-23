package bladequest.world;

import java.util.ArrayList;
import java.util.List;

import bladequest.battleactions.BattleAction;

public class Ability 
{
	private String displayName;
	private TargetTypes targetType;
	private int mpcost;	
	private boolean usableOutOfBattle;
	private boolean enabled;
	
	public String name, description;
	
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
		
		actions = new ArrayList<BattleAction>();
	}
	public void setDescription(String desc)
	{
		this.description = desc;
	}
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
}
