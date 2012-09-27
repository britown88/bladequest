package bladequest.combat;

import java.util.List;

import android.graphics.Point;
import bladequest.battleactions.BattleAction;
import bladequest.combatactions.CombatAction;
import bladequest.graphics.BattleAnim;
import bladequest.world.Character;
import bladequest.world.Global;

public class BattleEventObject 
{
	private int frame;
	
	private BattleAnim animation;
	private BattleAction battleAction;
	private CombatAction combatAction;
	
	private Character source;
	private List<Character> targets;
	
	private types type;	
	public types Type(){return type;}
	public int Frame(){return frame;}
	public BattleAnim Animation(){ return animation; }
	
	public BattleEventObject(int frame, BattleAnim animation, Character source, List<Character> targets)
	{
		this.frame = frame;
		this.type = types.Animation;
		this.animation = animation;
		this.source = source;
		this.targets = targets;		
	}
	public BattleEventObject(int frame, BattleAction battleAction, Character source, List<Character> targets)
	{
		this.frame = frame;
		this.type = types.BattleAction;
		this.battleAction = battleAction;
		this.source = source;
		this.targets = targets;		
	}
	public BattleEventObject(int frame, CombatAction combatAction, Character source, List<Character> targets)
	{
		this.frame = frame;
		this.type = types.CombatAction;
		this.combatAction = combatAction;
		this.source = source;
		this.targets = targets;		
	}
	
	public void execute(List<DamageMarker> markers)
	{
		Point targetP = null;
		if(targets != null && targets.size() > 0)
			targetP = targets.get(0).getPosition(true);
			
		switch(type)
		{
		case Animation:
			animation = Global.playAnimation(animation, source.getPosition(true), targetP);
			break;
		case BattleAction:
			battleAction.run(source, targets, markers);
			break;
		case CombatAction:
			combatAction.execute(targets);
			break;
		}
	}
	
	public enum types
	{
		Animation,
		BattleAction,
		CombatAction
	}

}
