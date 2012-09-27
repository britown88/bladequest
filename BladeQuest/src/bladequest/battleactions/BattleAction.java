package bladequest.battleactions;

import java.util.List;

import bladequest.combat.DamageMarker;
import bladequest.world.Character;

public class BattleAction 
{
	private int frame;
	
	public int getFrame(){return frame;}
	public BattleAction(int frame){this.frame = frame;}	
	public void run(Character attacker, List<Character> target, List<DamageMarker> markers){}
	public boolean isDone() {return true; }
	public boolean willAffectTarget(Character target){ return true;}

}
