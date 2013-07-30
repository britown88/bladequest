package bladequest.statuseffects;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import bladequest.battleactions.DamageBuilder;
import bladequest.battleactions.bactDamage;
import bladequest.battleactions.bactRunAnimation;
import bladequest.battleactions.bactWait;
import bladequest.combat.Battle;
import bladequest.combat.BattleCalc;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.DamageComponent;
import bladequest.combat.triggers.Trigger;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleAnimObjState;
import bladequest.graphics.BattleAnimObjState.PosTypes;
import bladequest.graphics.BattleAnimObject;
import bladequest.graphics.BattleAnimObject.Types;
import bladequest.graphics.BattleSprite.faces;
import bladequest.graphics.BitmapFrame;
import bladequest.system.Recyclable;
import bladequest.world.BattleAnimations;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;
import bladequest.world.Stats;

public class seFrozen extends StatusEffect
{
	int duration;
	List<Recyclable> disposeTriggers;
	PlayerCharacter affected;
	
	BattleAnim getSnowImplosion(PlayerCharacter target)
	{
		BattleAnim anim = new BattleAnim(1000.0f);
		
		Bitmap icePoof = Global.bitmaps.get("misc/particles");
		Rect poofRect = new Rect(1,13,13,24);
		
		final int poofs = 50;
		final float minVel = 4.0f;
		final float maxVel = 72.0f;
		final int life = 450;
		
		for (int i = 0; i < poofs; ++i)
		{
			//add a poof at this point that's fairly long-lived.
			BattleAnimObject poofAnim = new BattleAnimObject(Types.Bitmap, false, icePoof);
			
			float initialOffsetX = Global.rand.nextFloat() * 32.0f -  16.0f;
			float initialOffsetY = Global.rand.nextFloat() * 32.0f -  16.0f;
			
			float angle = (float)(Global.rand.nextFloat() * Math.PI);
			float velocity = Global.rand.nextFloat() * (maxVel - minVel) + minVel;
			float x = initialOffsetX + ((float)Math.cos(angle)) * velocity;
			float y = initialOffsetY + ((float)Math.sin(angle)) * velocity;
			
			float rnd = Global.rand.nextFloat() * 360.0f;
			BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Source);
			state.size = new Point((int)(poofRect.width()*4.5f), (int)(poofRect.height()*4.5f));
			state.pos1 = new Point((int)initialOffsetX,(int)initialOffsetY);
			state.argb(196, 255, 255, 255);
			state.rotation = rnd;
			state.setBmpSrcRect(poofRect.left, poofRect.top, poofRect.right, poofRect.bottom);
			poofAnim.addState(state);
			
			state = new BattleAnimObjState(life, PosTypes.Source);
			state.size = new Point(poofRect.width()*3, poofRect.height()*3);
			state.pos1 = new Point((int)x,(int)y);
			state.argb(0, 255, 255, 255);
			state.rotation = rnd;
			state.setBmpSrcRect(poofRect.left, poofRect.top, poofRect.right, poofRect.bottom);
			poofAnim.addState(state);
			
			anim.addObject(poofAnim);
		}
		

		
		return anim;
	}
	
	
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
						Global.playAnimation(getSnowImplosion(builder.getDefender()), builder.getDefender().getPosition(true), builder.getDefender().getPosition(true));
						builder.setDamage(0);
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
		
		if (!c.isEnemy())
		{
			c.setFace(faces.Weak);
		}
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
			builder.addEventObject(new bactRunAnimation(getSnowImplosion(poorFrozenSap)));
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
		Rect r = BattleAnimations.getCharacterIceCube(c);
		//bitch, you frozen
		BitmapFrame iceblock = BattleAnimations.getIceBlock();
		
		Global.setvpToScreen(r);
		
		Global.renderer.drawBitmap(iceblock.bitmap, iceblock.srcRect, r, null, false);
	}

}
