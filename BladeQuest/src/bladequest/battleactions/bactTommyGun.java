package bladequest.battleactions;

import java.util.*;

import bladequest.combat.BattleNew;
import bladequest.combat.DamageMarker;
import bladequest.world.*;
import bladequest.world.Character;

public class bactTommyGun extends BattleAction
{
	private int bp, shots;
	public bactTommyGun(int frame, int bp, int shots)
	{
		super(frame);
		this.bp = bp;
		this.shots = shots;
	}
	
	@Override
	public void run(BattleNew battle, Character attacker, List<Character> targets, List<DamageMarker> markers)
	{
		Character t;
		for(int i = 0; i < shots; ++i)
		{
			do{
				t = targets.get(Global.rand.nextInt(targets.size()));
			}while(t.isDead());
			
			//Global.battle.applyDamage(t, -Battle.genDamage(bp, t), i*5);
		}
		
	}
	
	@Override
	public boolean isDone() {return true; }

}
