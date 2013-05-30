package bladequest.statuseffects;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import bladequest.battleactions.DamageBuilder;
import bladequest.battleactions.bactDamage;
import bladequest.battleactions.bactWait;
import bladequest.combat.Battle;
import bladequest.combat.BattleCalc;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.DamageComponent;
import bladequest.combat.triggers.Trigger;
import bladequest.graphics.BattleSprite.faces;
import bladequest.system.Recyclable;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;
import bladequest.world.Stats;

public class seFrozen extends StatusEffect
{
	int duration;
	List<Recyclable> disposeTriggers;
	PlayerCharacter affected;
	
	public seFrozen(int duration)
	{
		super("frozen", true);
		this.duration = duration;
		icon = "frozen";
		curable = true;
		removeOnDeath = true;
		battleOnly = true;
		hidden = false;
		disposeTriggers = new ArrayList<Recyclable>();
	}
	public faces weakendFace() {return faces.Damaged;}
	public boolean weakens() {return true;}
	public StatusEffect clone() 
	{
		return new seFrozen(duration);
	}
	@Override
	public String saveLine() 
	{ 
		return "status " + seConfuse.class.getSimpleName() + " " + duration; 
	}
	
	void setToFrozenState(Battle.PlayerBattleActionSelect actionSelector)
	{
		actionSelector.skipPlayerInput();
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
				setToFrozenState(Global.battle.resetPlayerAction(c));
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
				setToFrozenState(actionSelector);
			}
		}
		
		}.initialize(c));
		
		disposeTriggers.add(new Trigger(affected.getOnDamagedEvent()){
			PlayerCharacter aff;
			Trigger initialize(PlayerCharacter aff)
			{
				this.aff = aff;
				return this;
			}			
			@Override
			public void trigger() {
				DamageBuilder builder = bactDamage.triggerDamageBuilder;
				
				//no evasion when frozen, all attacks always hit your giant lovable iceblock self.
				if (builder.attackType() == BattleCalc.DamageReturnType.Miss) builder.setAttackType(BattleCalc.DamageReturnType.Hit);
				
				//halve all physical damage taken.  IgnoreDef still should go through??
				if (builder.getDamageType() == DamageTypes.Physical)
				{
					builder.setDamage(builder.getDamage()/2);
				}
				
				
				boolean dispels = false;
				for (DamageComponent component : builder.getDamageComponents())
				{
					if (component.getAffinity() == Stats.Fire) //FIRE SPELL YAY YAY
					{
						dispels = true;
						break;
					}
				}
				if (dispels)
				{
					aff.removeStatusEffect("frozen");	
					aff.clearDamaged();
				}
			}
		}.initialize(c));
	}
	
	@Override
	public void onTurn(BattleEventBuilder builder) 
	{
		PlayerCharacter poorFrozenSap =  builder.getSource();
		if (duration == 0)
		{
			Global.battle.setInfoBarText(poorFrozenSap.getDisplayName() + "thawed out!");
			poorFrozenSap.removeStatusEffect(this);
			poorFrozenSap.clearDamaged();
			builder.addEventObject(new bactWait(450));
			return;
		}
		
		
		Global.battle.setInfoBarText(poorFrozenSap.getDisplayName() + " is frozen solid.");
		
		builder.addEventObject(new bactWait(450));
		
		--duration;
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
	public void onRender(PlayerCharacter c)
	{
		
		// full size is a bit big... scale it a bit.
		int width = (int)(c.getWidth()/2.1);
		int height =(int)(c.getHeight()/2.1);
		
		Point drawPoint = c.getPosition(true);
		Rect r = new Rect(drawPoint.x -width, drawPoint.y-height,
						  drawPoint.x +width, drawPoint.y+height);
		
		//bitch, you frozen
		Bitmap iceCube = Global.bitmaps.get("icecube");		
		Rect iceCubeRect = new Rect(0,0,26,29);
		
		Global.setvpToScreen(r);
		
		Global.renderer.drawBitmap(iceCube, iceCubeRect, r, null, false);
	}

}