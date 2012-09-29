package bladequest.combat;

import java.util.ArrayList;
import java.util.List;

import bladequest.battleactions.BattleAction;
import bladequest.battleactions.bactDamage;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleSprite.faces;
import bladequest.world.Ability;
import bladequest.world.Character;
import bladequest.world.DamageTypes;

public class BattleEvent 
{
	private static final long actTimerLength = 150;//milliseconds
	
	private Character source;
	private List<Character> targets;
	private List<BattleEventObject> objects;
	
	private boolean running, done;
	private long startTime;
	
	private BattleAnim anim;	
	private int animStartIndex;
	
	public BattleEvent(Character source, List<Character> targets)
	{
		this.source = source;
		this.targets = new ArrayList<Character>(targets);	
		
		objects = new ArrayList<BattleEventObject>();
	}
	
	public Character getSource() { return source; }
	public List<Character> getTargets() { return targets;}	
	public boolean isDone(){ return done;}	
	private int frameFromActIndex(int index){return (int)(index*actTimerLength);}	
	
	//take the frame number from an animation and convert it to the frame time of the event
	private int syncToAnimation(int frame)
	{
		int startFrame = frameFromActIndex(animStartIndex);
		int actualFrame = (int)(anim.getFrameLength() * (frame == -1 ? anim.getFinalFrame() : frame));
		
		return startFrame + actualFrame;
	}
	private int syncToAnimation(float animPercent)
	{
		int startFrame = frameFromActIndex(animStartIndex);
		int actualFrame = (int)(anim.getFrameLength() * (anim.getFinalFrame()*animPercent));
		
		return startFrame + actualFrame;
	}
	
	public void setTargets(List<Character> targets)
	{
		this.targets = targets;
		for(BattleEventObject obj : objects)
			obj.setTargets(targets);
			
	}
	
	public void init()
	{
		done = running = false;
		
		switch(source.getAction())
		{
		case Attack:
			anim = source.getWeaponAnimation();
			animStartIndex = 3;
			objects.add(new BattleEventObject(frameFromActIndex(0), faces.Ready, 0, source));
			objects.add(new BattleEventObject(frameFromActIndex(2), faces.Attack, 0, source));
			objects.add(new BattleEventObject(frameFromActIndex(3), faces.Attack, 1, source));
			objects.add(new BattleEventObject(frameFromActIndex(4), faces.Attack, 2, source));	
			objects.add(new BattleEventObject(frameFromActIndex(5), faces.Ready, 0, source));
			objects.add(new BattleEventObject(frameFromActIndex(animStartIndex), source.getWeaponAnimation(), source, targets));
			objects.add(new BattleEventObject(syncToAnimation(0.5f), new bactDamage(0, 1.0f, DamageTypes.Physical), source, targets));
			objects.add(new BattleEventObject(frameFromActIndex(7)));
			break;
		case Ability:
			Ability ab = source.getAbilityToUse();
			anim = ab.getAnimation();
			animStartIndex = 3;
			
			objects.add(new BattleEventObject(frameFromActIndex(animStartIndex), anim, source, targets));
			for(BattleAction action : ab.getActions())
				objects.add(new BattleEventObject(syncToAnimation(action.getFrame()), action, source, targets));
			objects.add(new BattleEventObject(syncToAnimation(-1)));
			
			break;
		case CombatAction:
			break;
		case Item:
			break;
		}
		
	}
	
	public void update(BattleNew battle, List<DamageMarker> markers)
	{
		if(!running)
		{
			running = true;
			startTime = System.currentTimeMillis();
		}
		else
		{
			long frame = System.currentTimeMillis() - startTime;
			BattleEventObject rmObj = null;
			//int actIndex = (int)(frame / actTimerLength);
			for(BattleEventObject obj : objects)
			{
				if(obj.Frame() <= frame)
				{
					obj.execute(battle, markers);
					rmObj = obj;
					break;					
				}
			}
			
			if(rmObj != null)
			{
				objects.remove(rmObj);			
				if(objects.size() == 0)
				{
					running = false;
					done = true;
				}
			}			
		}
	}
}
