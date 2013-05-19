package bladequest.combat;

public class BattleStateMachine {
	BattleState currentState;
	public void setState(BattleState newState)
	{
		if (currentState != null)
		{
			currentState.changeStateTo(newState);
		}
		newState.onSwitchedTo(currentState);
		currentState = newState;
	}
	public BattleState getState()
	{
		return currentState;
	}
}
