package bladequest.combat;

import bladequest.battleactions.BattleAction;

public abstract class SyncronizableAction extends BattleAction {

	public abstract int syncronizeTime(float factor);
}
