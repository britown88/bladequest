package bladequest.statuseffects;

import java.util.ArrayList;
import java.util.List;

import bladequest.battleactions.BattleAction;
import bladequest.battleactions.DamageBuilder;
import bladequest.battleactions.DelegatingAction;
import bladequest.battleactions.bactDamage;
import bladequest.battleactions.bactInflictStatus;
import bladequest.battleactions.bactWait;
import bladequest.combat.BasicAttackBuilder;
import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.triggers.Trigger;
import bladequest.system.Recyclable;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class sePoisonWeapon extends StatusEffect
{
	int duration;
	List<Recyclable> disposeTriggers;
	PlayerCharacter affected;
	
	public sePoisonWeapon(int duration)
	{
		super("poison weapon", true);
		this.duration = duration;
		icon = "magisword";
		curable = false;
		removeOnDeath = true;
		battleOnly = true;
		hidden = false;
		disposeTriggers = new ArrayList<Recyclable>();
	}
	public StatusEffect clone() 
	{
		return new sePoisonWeapon(duration);
	}
	@Override
	public String saveLine() 
	{ 
		return "status " + sePoisonWeapon.class.getSimpleName() + " " + duration; 
	}
	@Override
	public void onInflict(PlayerCharacter c) 
	{
		affected = c;
		
		disposeTriggers.add(new Trigger(c.getOnAttackEvent()){
		
		@Override
		public void trigger() {
			BasicAttackBuilder builder = BattleEvent.attackBuilder;
			BattleEventBuilder eventBuilder = builder.getEventBuilder();
			eventBuilder.addEventObject(new DelegatingAction()
			{
				@Override
				protected void buildEvents(BattleEventBuilder builder) {
					//roololololololol
					if (Global.rand.nextInt(100) < 50 && BattleAction.getTarget(builder).isInBattle()) //half-chance of poison, hardcode for now
					{
						builder.addEventObject(new bactInflictStatus(new sePoison(10.0f)));
					}
				}
			}.addDependency(eventBuilder.getLast()));
		}
		
		});
		
		//just slightly increase weapon damage.
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
						builder.setDamage((int)(builder.getDamage() * 1.15));
						
						//TODO:  insert animation here!
						//Global.playAnimation("poison", null, builder.getDefender().getPosition(true));
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
			Global.battle.setInfoBarText(target.getDisplayName() + "'s poison ran out.");
			target.removeStatusEffect(this);
			builder.addEventObject(new bactWait(450));
			return;
		}
		
		--duration;
	}
	

}
