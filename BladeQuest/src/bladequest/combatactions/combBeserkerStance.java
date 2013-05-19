package bladequest.combatactions;

import bladequest.battleactions.bactInflictStatus;
import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.BattleEventObject;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleSprite.faces;
import bladequest.statuseffects.StatusEffect;
import bladequest.world.Ability;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;
import bladequest.world.Stats;
import bladequest.world.TargetTypes;

public class combBeserkerStance extends Stance {
	
	boolean broken;
	
	public combBeserkerStance()
	{
		name = "Beserker";
		type = DamageTypes.Magic; //ignored
		targetType = TargetTypes.Self;
		
		actionText = " shifted to beserker stance."; ///?????
		broken = false;
	}
	@Override
	public String getStatusName()
	{
		return "Beserker Stance";	
	}
	StatusEffect getBeserkStatus()
	{
		return new StatusEffect(getStatusName(), false)
		{
			Stance stance;
			int statShift;
			
			{
				icon = ""; //shouldn't show.
				negative = false;
				removeOnDeath = true;
				curable = false;
				battleOnly = true;
				hidden = false; //show name for status switch!
				
				statShift = 0;
			}
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
				statShift = (int)(c.getUnModdedStat(Stats.BattlePower) * 0.25f);
			}
			public void onInflict(PlayerCharacter c) 
			{
				trySetEnabledState(c.getAbility("assault"), true);
				trySetEnabledState(c.getAbility("zornhau"), true);
				
				getStatShift(c);
				c.modStat(Stats.BattlePower.ordinal(), statShift);
				c.modStat(Stats.Defense.ordinal(), -statShift);
			}
			public void onRemove(PlayerCharacter c) 
			{
				trySetEnabledState(c.getAbility("assault"), false);
				trySetEnabledState(c.getAbility("zornhau"), false);
				c.modStat(Stats.BattlePower.ordinal(), -statShift);
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
		int animStartIndex = 3;
		PlayerCharacter source = builder.getSource();
		builder.addEventObject(new BattleEventObject(BattleEvent.frameFromActIndex(0), faces.Ready, 0, source));
		
		//end current stance, if there is one.
		Stance.leaveStance(source);
		
		BattleAnim anim = Global.battleAnims.get("movetest");
		
		int frame = animStartIndex;		
		int startFrameTime = BattleEvent.frameFromActIndex(frame);
		int endFrameTime =  startFrameTime + anim.syncToAnimation(1.0f);
		
		builder.addEventObject(new BattleEventObject(startFrameTime, faces.Cast, 0, source));
		builder.addEventObject(new BattleEventObject(startFrameTime, anim, source, builder.getTargets()));
		
		//actual implementation of the beserk
		builder.addEventObject(new BattleEventObject(endFrameTime, new bactInflictStatus(endFrameTime, false, getBeserkStatus()), source, builder.getTargets()));
		
		
		builder.addEventObject(new BattleEventObject(endFrameTime, faces.Ready, 0, source));
		builder.addEventObject(new BattleEventObject(endFrameTime+BattleEvent.frameFromActIndex(2)));
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
