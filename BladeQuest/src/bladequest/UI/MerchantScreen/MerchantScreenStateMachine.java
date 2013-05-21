package bladequest.UI.MerchantScreen;


public class MerchantScreenStateMachine {
	MerchantScreenState currentState;
	public void setState(MerchantScreenState newState)
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
	public void resetToState(MerchantScreenState prevState)
	{
		//no "changeStateTo", no parent set
		prevState.onSwitchedTo(currentState);
		currentState = prevState;
	}
	public MerchantScreenState getState()
	{
		return currentState;
	}
}
