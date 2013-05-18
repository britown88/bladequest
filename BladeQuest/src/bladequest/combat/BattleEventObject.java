package bladequest.combat;

import java.util.List;

import android.graphics.Point;
import bladequest.battleactions.BattleAction;
import bladequest.combatactions.CombatAction;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleSprite;
import bladequest.world.PlayerCharacter;
import bladequest.world.Global;

public class BattleEventObject 
{
	private int frame;
	
	private BattleAnim animation;
	private BattleAction battleAction;
	private CombatAction combatAction;
	private BattleSprite.faces face;
	private int imageIndex;
	
	private PlayerCharacter source;
	private List<PlayerCharacter> targets;
	public void setTargets( List<PlayerCharacter> targets){this.targets = targets;}
	
	private types type;	
	public types Type(){return type;}
	public int Frame(){return frame;}
	public BattleAnim Animation(){ return animation; }
	
	public BattleEventObject(int frame, BattleAnim animation, PlayerCharacter source, List<PlayerCharacter> targets)
	{
		this.frame = frame;
		this.type = types.Animation;
		this.animation = animation;
		this.source = source;
		this.targets = targets;		
	}
	public BattleEventObject(int frame, BattleAction battleAction, PlayerCharacter source, List<PlayerCharacter> targets)
	{
		this.frame = frame;
		this.type = types.BattleAction;
		this.battleAction = battleAction;
		this.source = source;
		this.targets = targets;		
	}
	public BattleEventObject(int frame, CombatAction combatAction, PlayerCharacter source, List<PlayerCharacter> targets)
	{
		this.frame = frame;
		this.type = types.CombatAction;
		this.combatAction = combatAction;
		this.source = source;
		this.targets = targets;		
	}
	public BattleEventObject(int frame, BattleSprite.faces face, int imageIndex, PlayerCharacter source)
	{
		this.frame = frame;
		this.type = types.ChangeFace;
		this.imageIndex = imageIndex;
		this.face = face;
		this.source = source;	
	}
	public BattleEventObject(int frame)
	{ 
		this.frame = frame;
		this.type = types.Done;
	}
	
	public void execute(Battle battle, List<DamageMarker> markers)
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
			combatAction.execute(targets, markers);
			break;
		case ChangeFace:
			if(!source.isEnemy())
			{
				source.setBattleFrame(face);
				source.setImageIndex(imageIndex);
			}			
			break;
		default:
			break;
		}
	}
	
	public enum types
	{
		Animation,
		BattleAction,
		CombatAction,
		ChangeFace,
		Done
	}

}
