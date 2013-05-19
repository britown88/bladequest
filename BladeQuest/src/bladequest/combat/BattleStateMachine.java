package bladequest.combat;

public class BattleStateMachine {
	BattleState currentState;
	public void setState(BattleState newState)
	{
		if (currentState != null)
		{
			currentState.changeStateTo(newState);
		}
		newState.setPreviousState(currentState);
		newState.onSwitchedTo(currentState);
		currentState = newState;
	}
	//Cancel back to a previous state
	public void resetToState(BattleState prevState)
	{
		//no "changeStateTo", no parent set
		prevState.onSwitchedTo(currentState);
		currentState = prevState;
	}
	public BattleState getState()
	{
		return currentState;
	}
}
