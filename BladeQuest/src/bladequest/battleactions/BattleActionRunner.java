package bladequest.battleactions;

import java.util.ArrayList;
import java.util.List;

import bladequest.combat.BattleEventBuilder;

public class BattleActionRunner extends BattleAction {
	
	List<BattleAction> actions;
	List<BattleAction> remainingActions;
	
	public BattleActionRunner()
	{
		this.actions = new ArrayList<BattleAction>();
		this.remainingActions = new ArrayList<BattleAction>();
	}
	
	public void addAction(BattleAction action)
	{
		actions.add(action);
		remainingActions.add(action);
	}
	
	public BattleAction getLast()
	{
		return actions.get(actions.size()-1);
	}
	
	@Override
	public void initialize()
	{
		remainingActions = new ArrayList<BattleAction>();
		for (BattleAction action : actions)
		{
			action.setReferences();
			remainingActions.add(action);
		}
	}
	
	public List<BattleAction> getActions()
	{
		return remainingActions;
	}
	
	@Override
	public void clearState()
	{
		for (BattleAction action : actions)
		{
		  action.reset();
		}
	}
	
	@Override
	public State run(BattleEventBuilder builder)
	{
		if (remainingActions.isEmpty()) return State.Finished;
		
		for (BattleAction action : remainingActions)
		{
			if (action.isRoot())
			{
				State outState = action.run(builder);
				if (outState == State.Finished)
				{
					remainingActions.remove(action);
					if (remainingActions.isEmpty()) return State.Finished;
				}
				return State.Continue;
			}
		}
		//nothing is root?  deadlock?
		return State.Continue;
	}
}
