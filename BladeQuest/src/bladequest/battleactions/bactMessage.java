package bladequest.battleactions;

import java.util.List;

import bladequest.combat.DamageMarker;
import bladequest.world.PlayerCharacter;

public class bactMessage extends BattleAction
{
	String message;
	
	public bactMessage(int frame, String message)
	{
		super(frame);
		this.message = message;
	}
	
	@Override
	public void run(PlayerCharacter attacker, List<PlayerCharacter> targets, List<DamageMarker> markers)
	{
		//Global.battle.addMessage(message);
			
		
	}
	
	@Override
	public boolean isDone() {return true;/*Global.battle.messageQueue.size() == 0;*/ }

}
