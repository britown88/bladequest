package bladequest.actions;

import bladequest.world.Global;

public class actDefaultBattleMusic extends Action {

	public actDefaultBattleMusic() {
	}

	@Override
	public boolean isDone() {
		return true;
	}

	@Override
	public void run() {
		if (Global.battleMusicListener != null)
		{
			Global.battleMusicListener.onRemove();
			Global.battleMusicListener = null;	
		}
	}

}
