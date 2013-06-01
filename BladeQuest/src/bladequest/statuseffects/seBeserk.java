package bladequest.statuseffects;

import java.util.ArrayList;
import java.util.List;

import bladequest.battleactions.bactWait;
import bladequest.combat.Battle;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.triggers.Trigger;
import bladequest.system.Recyclable;
import bladequest.world.Ability;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;
import bladequest.world.TargetTypes;
import bladequest.world.PlayerCharacter.Action;

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
		
		
		c.setDefaultMirroredState(true);
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
		
		disposeTriggers.add(new Trigger(affected.getOnPhysicalHitEvent()){
			PlayerCharacter aff;
			Trigger initialize(PlayerCharacter aff)
			{
				this.aff = aff;
				return this;
			}			
			@Override
			public void trigger() {
				aff.removeStatusEffect("beserk");
			}
		}.initialize(c));
	}
	@Override
	public void onRemove(PlayerCharacter c)
	{
		c.setDefaultMirroredState(false);
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
		
		--duration;
	}
	

}
