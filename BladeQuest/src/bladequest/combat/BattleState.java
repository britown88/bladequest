package bladequest.combat;

import bladequest.world.Global;


//empty, fill in what control flow your state needs.  
//write a constructor that captures the needed data for your state.
public class BattleState {
	
	BattleState prevState;
	public final void setPreviousState(BattleState prevState)
	{
		this.prevState = prevState;
	}
	public void cancelToPrevState()
	{
		Global.battle.cancelToState(prevState);
	}
	
	public void menuUpdate() {}
	public void changeStateTo(BattleState state) {}
	public void onSwitchedTo(BattleState prevState) {}
	public void drawPanels() {}
	public void update() {}
	public void backButtonPressed() {}
	public void onLongPress(int x, int y) {}
	public void touchActionUp(int x, int y) {}
	public void touchActionMove(int x, int y) {}
	public void touchActionDown(int x, int y) {}
 }
