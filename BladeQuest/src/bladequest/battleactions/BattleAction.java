package bladequest.battleactions;

import java.util.List;

import bladequest.combat.BattleEventBuilder;
import bladequest.combat.DamageMarker;
import bladequest.world.PlayerCharacter;

public class BattleAction 
{
	private int frame;
	
	public int getFrame(){return frame;}
	public BattleAction(int frame){this.frame = frame;}	
	public void run(PlayerCharacter attacker, List<PlayerCharacter> target, List<DamageMarker> markers){}
	public boolean isDone() {return true; }
	public boolean willAffectTarget(PlayerCharacter target){ return true;}
	public void setBuilder(BattleEventBuilder builder) {}
}
