package bladequest.battleactions;

import java.util.*;

import bladequest.combat.DamageMarker;
import bladequest.world.Battle;
import bladequest.world.Character;
import bladequest.world.DamageTypes;
import bladequest.world.Global;

public class bactMessage extends BattleAction
{
	String message;
	
	public bactMessage(int animFrame, String message)
	{
		super(animFrame);
		this.message = message;
	}
	
	@Override
	public void run(Character attacker, List<Character> targets, List<DamageMarker> markers)
	{
		Global.battle.addMessage(message);
			
		
	}
	
	@Override
	public boolean isDone() {return Global.battle.messageQueue.size() == 0; }

}
