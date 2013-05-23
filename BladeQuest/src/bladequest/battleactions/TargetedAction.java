package bladequest.battleactions;

import java.util.ArrayList;
import java.util.List;

import bladequest.combat.BattleEventBuilder;
import bladequest.world.PlayerCharacter;

public abstract class TargetedAction extends DelegatingAction {
	
	List<PlayerCharacter> targets;
	
	public TargetedAction(PlayerCharacter target)
	{
		targets = new ArrayList<PlayerCharacter>();
		targets.add(target);
	}
	public TargetedAction(List<PlayerCharacter> targets)
	{
		this.targets = targets;
	}	
	protected BattleEventBuilder getAdaptedBuilder(BattleEventBuilder builder)
	{
		return changeTargets(changeRunner(builder, runner), targets);
	}
}
