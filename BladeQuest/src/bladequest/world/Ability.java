package bladequest.world;

import java.util.ArrayList;
import java.util.List;

import bladequest.battleactions.BattleAction;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.BattleAnim;

public class Ability 
{
	private String displayName;
	private DamageTypes dmgType;	
	private TargetTypes targetType;
	private int mpcost, accuracy;	
	private boolean usableOutOfBattle;
	private String animation;
	private boolean enabled;
	
	public String name;
	
	private List<BattleAction> actions;
	
	public Ability(String name, String displayName, String animation, DamageTypes dmgType, TargetTypes targetType, int mpcost, int accuracy, boolean usableOutOfBattle)
	{
		this.name = name;
		this.displayName = displayName;
		this.dmgType = dmgType;
		this.targetType = targetType;
		this.mpcost = mpcost;
		this.usableOutOfBattle = usableOutOfBattle;
		this.animation = animation;
		this.accuracy = accuracy;
		this.enabled = true;
		
		actions = new ArrayList<BattleAction>();
	}
	public boolean isUsableOutOfBattle() { return usableOutOfBattle;}
	public void addAction(BattleAction action){actions.add(action);}
	public List<BattleAction> getActions(BattleEventBuilder builder) 
	{
		for (BattleAction action : actions)
		{
			action.setBuilder(builder);
		}
		return actions;
	}
	public String getDisplayName() { return displayName;}
	public DamageTypes DmgType() { return dmgType;}
	public TargetTypes TargetType() { return targetType;}
	public int MPCost() { return mpcost;}
	public int Accuracy() { return accuracy;}
	public boolean isEnabled() { return enabled;}
	public void setEnabled(boolean enabled) {this.enabled = enabled;}
	
	public BattleAnim getAnimation()
	{
		if (animation.equals("noanim")) return null;
		return Global.battleAnims.get(animation);
		//Global.playAnimation(animation, src.getPosition(true), target.getPosition(true));
	}
}
