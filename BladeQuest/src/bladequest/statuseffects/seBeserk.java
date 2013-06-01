package bladequest.statuseffects;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.graphics.Rect;
import bladequest.battleactions.DamageBuilder;
import bladequest.battleactions.bactDamage;
import bladequest.battleactions.bactRunAnimation;
import bladequest.battleactions.bactWait;
import bladequest.combat.Battle;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.triggers.Trigger;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleAnimObjState;
import bladequest.graphics.BattleAnimObjState.PosTypes;
import bladequest.graphics.BattleAnimObject;
import bladequest.graphics.BattleAnimObject.Types;
import bladequest.graphics.BitmapFrame;
import bladequest.system.Recyclable;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;
import bladequest.world.TargetTypes;

public class seBeserk extends StatusEffect
{
	int duration;
	List<Recyclable> disposeTriggers;
	PlayerCharacter affected;
	
	public seBeserk(int duration)
	{
		super("beserk", true);
		this.duration = duration;
		icon = "axe";
		curable = true;
		removeOnDeath = true;
		battleOnly = true;
		hidden = false;
		disposeTriggers = new ArrayList<Recyclable>();
	}
	public StatusEffect clone() 
	{
		return new seBeserk(duration);
	}
	@Override
	public String saveLine() 
	{ 
		return "status " + seBeserk.class.getSimpleName() + " " + duration; 
	}
	public boolean mutes() {return true;}
	void setToBeserkState(Battle.PlayerBattleActionSelect actionSelector)
	{
		if (actionSelector.isSkipped() ||
			actionSelector.isAbilitySet() ||
				actionSelector.isAttackSet()) return; //don't overwrite other actions.

		Battle.Team team;
		if (affected.isEnemy()) team = Battle.Team.Enemy;
		else team = Battle.Team.Player;
		actionSelector.setAttack(Battle.getRandomTargets(TargetTypes.SingleEnemy, affected, team).get(0));
	}
	BattleAnimObjState getAngerBubbleState(BitmapFrame frame, int frameCount)
	{
		int yOffset = affected.getHeight()/2; 
		BattleAnimObjState out = new BattleAnimObjState(frameCount, PosTypes.Source);
		out.pos1 = new Point(-8, -yOffset);
		out.argb(255, 255, 255, 255);
		out.size = new Point(32, 64);
		Rect r = frame.srcRect;
		out.setBmpSrcRect(r.left, r.top, r.right, r.bottom);
		return out;
	}
	BattleAnim getAngerAnim()
	{
		BitmapFrame[] frames = Global.getReactionAnger().getFrames();
		BattleAnim out = new BattleAnim(1000.0f);
		BattleAnimObject angerBubble = new BattleAnimObject(Types.Bitmap, false, frames[0].bitmap);
		int frameCount = 0;
		final int playCount = 3;
		for (int i = 0; i < playCount; ++i)
		{
			for (BitmapFrame frame : frames)
			{
				angerBubble.addState(getAngerBubbleState(frame, frameCount++*100));
			}	
		}
		angerBubble.addState(getAngerBubbleState(frames[frames.length-1], frameCount++*100));  //cap off the final animation!

		out.addObject(angerBubble);
		return out;
	}
	@Override
	public void onInflict(PlayerCharacter c) 
	{
		affected = c;
		
		if (Global.battle.getCurrentActor() != c)
		{
			//gotta check and see if they hvan't gone yet...
			if (!Global.battle.getPlayerHasGone(c))
			{
				setToBeserkState(Global.battle.resetPlayerAction(c));
			}
		}	
		
		disposeTriggers.add(new Trigger(Global.battle.getOnActionSelectEvent()){
			 
		PlayerCharacter aff;
		Trigger initialize(PlayerCharacter aff)
		{
			this.aff = aff;
			return this;
		}
		
		@Override
		public void trigger() {
			
			Battle.PlayerBattleActionSelect actionSelector = Global.battle.getActionSetter();
			if (actionSelector.getPlayer() == aff)
			{
				setToBeserkState(actionSelector);
			}
		}
		
		}.initialize(c));
		
		disposeTriggers.add(new Trigger(Global.battle.getOnDamageDealt()){
			PlayerCharacter aff;
			Trigger initialize(PlayerCharacter aff)
			{
				this.aff = aff;
				return this;
			}			
			@Override
			public void trigger() {
				DamageBuilder builder = bactDamage.triggerDamageBuilder;
				if (builder.getAttacker() == aff)
				{
					if (builder.getDamageType() == DamageTypes.Physical ||
						builder.getDamageType() == DamageTypes.PhysicalIgnoreDef)
					{
						builder.setDamage((int)(builder.getDamage() * 1.5));
					}
				}
			}
		}.initialize(c));
	}
	@Override
	public void onRemove(PlayerCharacter c)
	{
		for (Recyclable disposeTrigger : disposeTriggers)
		{
			if (disposeTrigger != null)
			{
				disposeTrigger.recycle();
				disposeTrigger = null;
			}
		}
	}
	public void onTurn(BattleEventBuilder builder) 
	{
		PlayerCharacter target =  builder.getSource();
		if (duration == 0)
		{
			Global.battle.setInfoBarText(target.getDisplayName() + " is calming down.");
			target.removeStatusEffect(this);
			builder.addEventObject(new bactWait(450));
			return;
		}
		
		Global.battle.setInfoBarText(target.getDisplayName() + " is going beserk!");
		
		builder.addEventObject(new bactRunAnimation(getAngerAnim()));
		
		--duration;
	}
	

}
