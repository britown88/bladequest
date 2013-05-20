package bladequest.UI.MainMenu;


public class MainMenuStateMachine {
	MainMenuState currentState;
	public void setState(MainMenuState newState)
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
	public void resetToState(MainMenuState prevState)
	{
		//no "changeStateTo", no parent set
		prevState.onSwitchedTo(currentState);
		currentState = prevState;
	}
	public MainMenuState getState()
	{
		return currentState;
	}
}
