package bladequest.combatactions;

import android.graphics.Point;
import android.graphics.Rect;
import bladequest.battleactions.bactInflictStatus;
import bladequest.battleactions.bactLeaveStance;
import bladequest.battleactions.bactRunAnimation;
import bladequest.battleactions.bactSetFace;
import bladequest.battleactions.bactWait;
import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleAnimObjState;
import bladequest.graphics.BattleAnimObjState.PosTypes;
import bladequest.graphics.BattleAnimObject;
import bladequest.graphics.BattleAnimObject.Types;
import bladequest.graphics.BattleSprite.faces;
import bladequest.graphics.BitmapFrame;
import bladequest.statuseffects.StatusEffect;
import bladequest.world.Ability;
import bladequest.world.BattleAnimations;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;
import bladequest.world.Stats;
import bladequest.world.TargetTypes;

public class combBerserkerStance extends Stance {
	
	boolean broken;
	
	public combBerserkerStance()
	{
		name = "Berserker Stance";
		type = DamageTypes.Magic; //ignored
		targetType = TargetTypes.Self;
		
		actionText = " shifted to berserker stance."; ///?????
		broken = false;
	}
	public String getShortName()
	{
		return "Berserker";
	}
	public boolean equalTo(Stance rhs)
	{
		return (combBerserkerStance)(rhs) != null;
	}
	@Override
	public String getDescription() { return "Attack furiously, increasing your attack damage but decreasing your defense.";}
	
	@Override
	public String getStatusName()
	{
		return "Berserker";	
	}
	
	static BattleAnim makeBeserkerAnim()
	{
		final int particleCount= 40;
		BitmapFrame[] frames = BattleAnimations.getBeserkerSwords().getFrames();
		BattleAnim out = new BattleAnim(60.0f);
		
		int particleDelay = 2;
		int sparkleDelay = 6;
		int flyRate = 16;
		for (int j = 0; j < particleCount; ++j) 
		{
			BattleAnimObject sword = new BattleAnimObject(Types.Bitmap, false, frames[0].bitmap);
			
			int xOffset = Global.rand.nextInt(96) - 72;
			int yOffset =  36;
			
			for(int i = 0; i < frames.length; ++i)
			{
				BattleAnimObjState state = new BattleAnimObjState(j*particleDelay + i * sparkleDelay, PosTypes.Target);
				state.size = new Point((int)(frames[i].srcRect.width()*2), (int)(frames[i].srcRect.height()*2));
				state.pos1 = new Point(xOffset+(flyRate * i),yOffset-(flyRate * i));
				state.argb(255 - ((i * 255) / (frames.length)), 255, 255, 255);
				Rect r = frames[i].srcRect;
				state.setBmpSrcRect(r.left, r.top, r.right, r.bottom);
				sword.addState(state);
			}
			out.addObject(sword);
		}
		
		
		frames = BattleAnimations.getBeserkerBase().getFrames();
		
		int baseStart = particleDelay * particleCount + sparkleDelay * frames.length;
		int baseDelay = 8;
		
		BattleAnimObject base = new BattleAnimObject(Types.Bitmap, false, frames[0].bitmap);
		
		int xOffset = -8;  //slightly off
		int yOffset =  0;
		
		for(int i = 0; i < frames.length; ++i)
		{
			BattleAnimObjState state = new BattleAnimObjState(baseStart + i * baseDelay, PosTypes.Target);
			state.size = new Point((int)(frames[i].srcRect.width()*2), (int)(frames[i].srcRect.height()*2));
			state.pos1 = new Point(xOffset,yOffset);
			state.argb(255 - ((i * 196) / (frames.length)), 255, 255, 255);
			Rect r = frames[i].srcRect;
			state.setBmpSrcRect(r.left, r.top, r.right, r.bottom);
			base.addState(state);
		}
		out.addObject(base);		
		
		return out;
	}
	StatusEffect getBerserkStatus()
	{
		return new StatusEffect(getStatusName(), false)
		{
			Stance stance;
			int statShift;
			
			{
				icon = "2swords";
				negative = false;
				removeOnDeath = true;
				curable = false;
				battleOnly = true;
				hidden = false; //show name for status switch!
				
				statShift = 0;
			}
			public StatusEffect clone() {return this;}
			StatusEffect initialize(Stance stance)
			{
				this.stance = stance;
				return this;
			}
			private void trySetEnabledState(Ability ability, boolean on)
			{
				if (ability != null)
				{
					ability.setEnabled(on);
				}
			}
			public void getStatShift(PlayerCharacter c)
			{
				statShift = (int)(c.getUnModdedStat(Stats.Power) * 0.25f);
			}
			public void onInflict(PlayerCharacter c) 
			{			
				trySetEnabledState(c.getAbility("assault"), true);
				trySetEnabledState(c.getAbility("zornhau"), true);
				
				getStatShift(c);
				c.modStat(Stats.Power.ordinal(), statShift);
				c.modStat(Stats.Defense.ordinal(), -statShift);
			}
			public void onRemove(PlayerCharacter c) 
			{
				trySetEnabledState(c.getAbility("assault"), false);
				trySetEnabledState(c.getAbility("zornhau"), false);
				c.modStat(Stats.Power.ordinal(), -statShift);
				c.modStat(Stats.Defense.ordinal(), statShift);				
			}
			public Stance getStance() 
			{
				return stance;
			}			
		}.initialize(this);
	}
	@Override
	public void buildEvents(BattleEventBuilder builder)
	{
		int castTime = BattleEvent.frameFromActIndex(3);
		
		builder.addEventObject(new bactSetFace(faces.Cast, 0));
		builder.addEventObject(new bactWait(castTime));
		
		BattleAnim anim = makeBeserkerAnim();
		
		builder.addEventObject(new bactRunAnimation(anim).addDependency(builder.getLast()));
		
		//actual implementation of the berserk
		builder.addEventObject(new bactLeaveStance().addDependency(builder.getLast()));
		builder.addEventObject(new bactInflictStatus(getBerserkStatus()).addDependency(builder.getLast()));
		
		builder.addEventObject(new bactSetFace(faces.Cast, 0).addDependency(builder.getLast()));
		
		//wait a bit
		builder.addEventObject(new bactWait(BattleEvent.frameFromActIndex(2)).addDependency(builder.getLast()));
	}

	@Override
	public boolean isBroken() {
		return broken;
	}

	@Override
	public void setBroken(boolean broken) {
		this.broken = broken;
	}
}
