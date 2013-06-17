package bladequest.UI.MerchantScreen;

import bladequest.world.Global;


public class MerchantScreenState
{
	MerchantScreenState prevState;
	public final void setPreviousState(MerchantScreenState prevState)
	{
		this.prevState = prevState;
	}
	public void cancelToPrevState()
	{
		Global.merchantScreen.cancelToState(prevState);
	}
	
	public void changeStateTo(MerchantScreenState state) {}
	public void onSwitchedTo(MerchantScreenState prevState) {}

	public void update() {}
	public void render() {}
	
	public void handleClosing(){}	

	public void backButtonPressed() {}
	public void touchActionUp(int x, int y) {}
	public void touchActionMove(int x, int y) {}
	public void touchActionDown(int x, int y) {}
	public void longPress(int x, int y) {}
	


}
