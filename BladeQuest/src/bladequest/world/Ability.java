package bladequest.world;

import java.util.ArrayList;
import java.util.List;

import bladequest.battleactions.BattleAction;

public class Ability 
{
	private String displayName;
	private DamageTypes dmgType;	
	private TargetTypes targetType;
	private int mpcost, accuracy, delay;	
	private boolean usableOutOfBattle;
	
	public String name;
	
	private List<BattleAction> actions;
	
	public Ability(String name, String displayName, DamageTypes dmgType, TargetTypes targetType, int mpcost, int accuracy, boolean useableOutOfBattle)
	{
		this.name = name;
		this.displayName = displayName;
		this.dmgType = dmgType;
		this.targetType = targetType;
		this.mpcost = mpcost;
		this.usableOutOfBattle = usableOutOfBattle;

		this.accuracy = accuracy;
		delay = 5;
		
		actions = new ArrayList<BattleAction>();
	}
	
	public void setActionDelay(int delay)
	{
		this.delay = delay;
	}
	public boolean isUsableOutOfBattle() { return usableOutOfBattle;}
	public void addAction(BattleAction action){actions.add(action);}
	public String getDisplayName() { return displayName;}
	public DamageTypes DmgType() { return dmgType;}
	public TargetTypes TargetType() { return targetType;}
	public int MPCost() { return mpcost;}
	public int Accuracy() { return accuracy;}
	
	private int bactIndex;
	public boolean running = false;
	
	private Character attacker;
	private List<Character> targets;
	
	public void update()
	{
		if(actions.get(bactIndex).isDone())
		{
			bactIndex++;
			if(bactIndex < actions.size())
				actions.get(bactIndex).run(attacker, targets, bactIndex*delay);
			else
				running = false;
		}
	}
	
	
	
	public void execute(Character attacker, List<Character> targets)
	{
		this.attacker = attacker;
		this.targets = targets;
		
		bactIndex = 0;
		running = true;

		actions.get(bactIndex).run(attacker, targets, 0);
	}

}
