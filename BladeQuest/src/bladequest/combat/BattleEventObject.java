package bladequest.combat;

import bladequest.battleactions.BattleAction;
import bladequest.combatactions.CombatAction;
import bladequest.graphics.BattleAnim;

public class BattleEventObject 
{
	private BattleAnim animation;
	private BattleAction battleAction;
	private CombatAction combatAction;
	
	public enum types
	{
		Animation,
		BattleAction,
		CombatAction
	}

}
