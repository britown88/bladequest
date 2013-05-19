package bladequest.combatactions;

import bladequest.statuseffects.StatusEffect;
import bladequest.world.PlayerCharacter;

//a specialized BattleAction.
public abstract class Stance extends CombatAction {

	public abstract boolean isBroken();
	public abstract void setBroken(boolean broken);
	public abstract String getStatusName();
	public abstract boolean equalTo(Stance rhs);
	
	//get a player's current stance.
	public static Stance getStance(PlayerCharacter character)
	{
		for (StatusEffect effect : character.getStatusEffects())
		{
			Stance stance = effect.getStance();
			if (stance != null)
			{
				return stance;
			}
		}
		return null;
	}
	//end current stance, if there is one.  Does not break the stance.
	public static void leaveStance(PlayerCharacter character)
	{
		Stance stance = getStance(character);
		if (stance != null)
		{
			character.removeStatusEffect(stance.getStatusName());
		}
	} 
}
