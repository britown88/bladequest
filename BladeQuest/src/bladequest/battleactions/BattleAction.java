package bladequest.battleactions;

import java.util.List;

import bladequest.world.Character;
import bladequest.combat.DamageMarker;

public class BattleAction 
{
	private int animFrame;
	
	public int getAnimFrame() { return animFrame;}	
	public BattleAction(int animFrame){this.animFrame = animFrame;}	
	public void run(Character attacker, List<Character> target, List<DamageMarker> markers){}
	public boolean isDone() {return true; }
	public boolean willAffectTarget(Character target){ return true;}

}
