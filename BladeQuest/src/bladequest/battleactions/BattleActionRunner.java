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
	public boolean hasRemaining()
	{
		return !remainingActions.isEmpty();
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
	
	public void interrupt()
	{
		clearState();
		this.remainingActions = new ArrayList<BattleAction>();
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
		List<BattleAction> rootActions = new ArrayList<BattleAction>();

		for (BattleAction action : remainingActions)
		{
			if (action.isRoot())
			{
				rootActions.add(action);
			}
		}
		
		List<BattleAction> ranActions  = new ArrayList<BattleAction>();
		
		boolean runAgain = true;
		
		while (runAgain)
		{
			runAgain = false;
			for (BattleAction action : rootActions)
			{
				ranActions.add(action);
				State outState = action.run(builder);
				if (outState == State.Finished)
				{
					remainingActions.remove(action);
					action.removeReferences();
					
					rootActions = new ArrayList<BattleAction>();

					for (BattleAction subActions : remainingActions)
					{
						if (subActions.isRoot() && !ranActions.contains(subActions))
						{
							rootActions.add(subActions);
						}
						runAgain = true;
					}				
					break;					
				}
			}			
		}

		
		if (remainingActions.isEmpty()) return State.Finished;
		return State.Continue;
	}
}
