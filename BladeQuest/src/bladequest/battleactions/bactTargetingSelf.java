package bladequest.battleactions;

import java.util.ArrayList;
import java.util.List;

import bladequest.combat.BattleEventBuilder;

public class bactTargetingSelf extends DelegatingAction {

	
	List<BattleAction> children;
	
	public bactTargetingSelf(List<BattleAction> children) {
		this.children = children;
	}
	
	public bactTargetingSelf(BattleAction child) {
		this.children = new ArrayList<BattleAction>();
		children.add(child);
	}	

	@Override
	protected void buildEvents(BattleEventBuilder builder) {
		builder.addEventObject(new TargetedAction(builder.getSource())
		{
			List<BattleAction> children;
			TargetedAction initialize(List<BattleAction> children)
			{
				this.children = children;
				return this;
			}
			protected void buildEvents(BattleEventBuilder builder) 
			{
				for (BattleAction action : children)
				{
					builder.addEventObject(action);
				}				
			}
		}.initialize(children));
	}

}
