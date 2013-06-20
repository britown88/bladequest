package bladequest.actions;

import bladequest.world.Global;

public class actRetarget extends Action  {

	int x, y;
	boolean retargeted = false;
	public actRetarget(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean isDone() {
		return retargeted;
	}

	@Override
	public void run() {
		if (retargeted) return;
		retargeted = Global.party.target(x,y);
	}

	@Override
	public void reset()
	{
		retargeted = false;
	}
}
