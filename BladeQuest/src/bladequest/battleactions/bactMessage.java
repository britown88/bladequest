package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;

public class bactMessage extends BattleAction
{	
	String message;
	public bactMessage(String message)
	{
		this.message = message;
	}
	
	@Override
	public State run(BattleEventBuilder builder)
	{
		return State.Finished;	
	}
}
