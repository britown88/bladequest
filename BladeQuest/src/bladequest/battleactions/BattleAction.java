package bladequest.battleactions;

import java.util.List;

import bladequest.world.Character;

public class BattleAction 
{
	public BattleAction(){}	
	public void run(Character attacker, List<Character> target, int delay){}
	public boolean isDone() {return true; }
	public boolean willAffectTarget(Character target){ return true;}

}
