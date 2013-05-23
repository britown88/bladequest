package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;
import bladequest.combatactions.Stance;
import bladequest.statuseffects.StatusEffect;
import bladequest.world.PlayerCharacter;

public class bactBreakStance extends BattleAction {
	@Override
	public State run(BattleEventBuilder builder)
	{
		PlayerCharacter attacker = builder.getSource();
		Stance stance = Stance.getStance(attacker);
		if (stance == null)
		{
			//nothing to do...
			return State.Finished;
		}
		
		stance.setBroken(true);
		//remove status.
		attacker.removeStatusEffect(stance.getStatusName());
		//add a recovery status.
		
		//TODO: rework to be a trigger!		
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
		
		return State.Finished;
	}
}
