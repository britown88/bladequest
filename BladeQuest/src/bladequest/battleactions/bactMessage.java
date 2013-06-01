package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;
import bladequest.world.Global;

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
	 	Global.battle.showMessage(message);
		return State.Finished;			
		
	}
	

}
