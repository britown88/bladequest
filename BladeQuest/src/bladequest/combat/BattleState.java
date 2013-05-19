package bladequest.combat;


//empty, fill in what control flow your state needs.  
//write a constructor that captures the needed data for your state.
public class BattleState {
	public void menuUpdate() {}
	public void changeStateTo(BattleState state) {}
	public void onSwitchedTo(BattleState prevState) {}
	public void drawPanels() {}
	public void update() {}
	public void backButtonPressed() {}
	public void touchActionUp(int x, int y) {}
	public void touchActionMove(int x, int y) {}
	public void touchActionDown(int x, int y) {}
 }
