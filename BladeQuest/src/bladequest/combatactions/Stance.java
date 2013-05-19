package bladequest.combatactions;

//a specialized BattleAction.
public abstract class Stance extends CombatAction {

	public abstract boolean isBroken();
	public abstract void setBroken(boolean broken);
	public abstract String getStatusName();
}
