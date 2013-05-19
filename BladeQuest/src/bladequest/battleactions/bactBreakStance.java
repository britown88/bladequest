package bladequest.battleactions;

import java.util.List;

import bladequest.combat.DamageMarker;
import bladequest.combatactions.Stance;
import bladequest.statuseffects.StatusEffect;
import bladequest.world.PlayerCharacter;

public class bactBreakStance extends BattleAction {
	public bactBreakStance(int frame) {
		super(frame);
		// TODO Auto-generated constructor stub
	}
	private Stance getPlayerStance(PlayerCharacter character)
	{
		for (StatusEffect effect : character.getStatusEffects())
		{
			Stance getStance = effect.getStance();
			if (getStance != null) return getStance;
		}
		return null;
	}
	@Override
	public void run(PlayerCharacter attacker, List<PlayerCharacter> targets, List<DamageMarker> markers)
	{
		Stance stance = getPlayerStance(attacker);
		if (stance == null)
		{
			//nothing to do...
			return;
		}
		stance.setBroken(true);
		//remove status.
		attacker.removeStatusEffect(stance.getStatusName());
		//add a recovery status.
		attacker.applyStatusEffect(new StatusEffect(stance.getStatusName() + "Recovery", false)
		{
			Stance stance;
			{
				icon = ""; //shouldn't show.
				negative = false;
				removeOnDeath = false;
				curable = false;
				battleOnly = true;
				hidden = true;
			}
			StatusEffect initialize(Stance stance)
			{
				this.stance = stance;
				return this;
			}
			public void onRemove(PlayerCharacter c) {
				stance.setBroken(false); //removes at end of battle!
			}  
		}.initialize(stance));
	}
}
