package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;
import bladequest.world.PlayerCharacter;

public abstract class SourcedAction extends DelegatingAction {
	
	PlayerCharacter source;
	
	public SourcedAction (PlayerCharacter source)
	{
		this.source = source;
	}
	protected BattleEventBuilder getAdaptedBuilder(BattleEventBuilder builder)
	{
		return changeSource(changeRunner(builder, runner), source);
	}
}
