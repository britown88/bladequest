package bladequest.world;

import java.util.ArrayList;
import java.util.List;

import bladequest.battleactions.BattleAction;
import bladequest.graphics.BattleAnim;

public class Ability 
{
	private String displayName;
	private DamageTypes dmgType;	
	private TargetTypes targetType;
	private int mpcost, accuracy;	
	private boolean usableOutOfBattle;
	private String animation;
	
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
		
		actions = new ArrayList<BattleAction>();
	}
	public boolean isUsableOutOfBattle() { return usableOutOfBattle;}
	public void addAction(BattleAction action){actions.add(action);}
	public List<BattleAction> getActions() { return actions;}
	public String getDisplayName() { return displayName;}
	public DamageTypes DmgType() { return dmgType;}
	public TargetTypes TargetType() { return targetType;}
	public int MPCost() { return mpcost;}
	public int Accuracy() { return accuracy;}
	
	public BattleAnim getAnimation()
	{
		return Global.battleAnims.get(animation);
		//Global.playAnimation(animation, src.getPosition(true), target.getPosition(true));
	}
}
