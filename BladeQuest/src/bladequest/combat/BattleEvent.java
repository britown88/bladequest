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
import bladequest.world.Global;
import bladequest.world.Item;

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
	
	//get the frameIndex that falls aftr the end of the animation
	private int getFinalAnimFrameIndex()
	{
		int finalFrame = syncToAnimation(-1);
		int index = (int)(finalFrame / actTimerLength)+1;
		
		return index;
	}
	
	public void setTargets(List<Character> targets)
	{
		this.targets = targets;
		for(BattleEventObject obj : objects)
			obj.setTargets(targets);
			
	}
	
	public void init()
	{
		int finalIndex;
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
			
			finalIndex = getFinalAnimFrameIndex();
			
			objects.add(new BattleEventObject(frameFromActIndex(animStartIndex), faces.Cast, 0, source));
			objects.add(new BattleEventObject(frameFromActIndex(animStartIndex), anim, source, targets));
			for(BattleAction action : ab.getActions())
				objects.add(new BattleEventObject(syncToAnimation(action.getFrame()), action, source, targets));
			objects.add(new BattleEventObject(frameFromActIndex(finalIndex), faces.Ready, 0, source));
			objects.add(new BattleEventObject(frameFromActIndex(finalIndex+2)));
			
			break;
		case CombatAction:
			animStartIndex = 3;
			
			objects.add(new BattleEventObject(frameFromActIndex(animStartIndex), faces.Cast, 0, source));
			objects.add(new BattleEventObject(frameFromActIndex(animStartIndex+1), source.getCombatAction(), source, targets));
			objects.add(new BattleEventObject(frameFromActIndex(animStartIndex+3), faces.Ready, 0, source));
			objects.add(new BattleEventObject(frameFromActIndex(animStartIndex+5)));
			
			break;
		case Item:
			Item itm = source.getItemToUse();
			anim = new BattleAnim(Global.battleAnims.get(itm.getAnimName()));
			animStartIndex = 3;			
			finalIndex = getFinalAnimFrameIndex();
			
			//resets item count and removes from inventory
			source.useItem();
			
			objects.add(new BattleEventObject(frameFromActIndex(animStartIndex), faces.Use, 0, source));
			objects.add(new BattleEventObject(frameFromActIndex(animStartIndex), anim, source, targets));
			for(BattleAction action : itm.getActions())
				objects.add(new BattleEventObject(syncToAnimation(action.getFrame()), action, source, targets));
			objects.add(new BattleEventObject(frameFromActIndex(finalIndex), faces.Ready, 0, source));
			objects.add(new BattleEventObject(frameFromActIndex(finalIndex+2)));
			
			break;
		}
		
	}
	
	public void update(Battle battle, List<DamageMarker> markers)
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
