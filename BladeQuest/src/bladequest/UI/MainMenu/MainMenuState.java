package bladequest.UI.MainMenu;

import bladequest.world.Global;


public class MainMenuState
{
	MainMenuState prevState;
	public final void setPreviousState(MainMenuState prevState)
	{
		this.prevState = prevState;
	}
	public void cancelToPrevState()
	{
		Global.menu.cancelToState(prevState);
	}
	
	public void changeStateTo(MainMenuState state) {}
	public void onSwitchedTo(MainMenuState prevState) {}

	public void update() {}
	public void render() {}
	
	public void handleClosing(){}
	
	public void changeToRoot()	{}
	public void changeToItemSelect() {}
	public void changeToOptions() {}
	
	public void onFling(float velocityX, float velocityY) {}
	public void backButtonPressed() {}
	public void touchActionUp(int x, int y) {}
	public void touchActionMove(int x, int y) {}
	public void touchActionDown(int x, int y) {}
	


}
