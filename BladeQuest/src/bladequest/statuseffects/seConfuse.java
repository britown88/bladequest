package bladequest.statuseffects;

import java.util.ArrayList;
import java.util.List;

import bladequest.combat.Battle;
import bladequest.combat.triggers.Trigger;
import bladequest.system.Recyclable;
import bladequest.world.Ability;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;
import bladequest.world.TargetTypes;
import bladequest.world.PlayerCharacter.Action;

public class seConfuse extends StatusEffect
{
	int duration;
	List<Recyclable> disposeTriggers;
	PlayerCharacter affected;
	
	public seConfuse(int duration)
	{
		super("confuse", true);
		this.duration = duration;
		icon = "confuse";
		curable = true;
		removeOnDeath = true;
		battleOnly = true;
		hidden = false;
		disposeTriggers = new ArrayList<Recyclable>();
	}
	public StatusEffect clone() 
	{
		return new seConfuse(duration);
	}
	@Override
	public String saveLine() 
	{ 
		return "status " + seConfuse.class.getSimpleName() + " " + duration; 
	}
	
	void setToConfusedState(Battle.PlayerBattleActionSelect actionSelector)
	{
		if (actionSelector.getPlayer().getAction() == Action.Skipped) return; //don't write over other skipping states.
		Battle.Team team;
		if (Global.rand.nextInt(100) < 75) //75% of friendly fire
		{
			if (affected.isEnemy()) team = Battle.Team.Player;
			else team = Battle.Team.Enemy;
		}
		else
		{
			if (affected.isEnemy()) team = Battle.Team.Enemy;
			else team = Battle.Team.Player;
		}
		
		//50% chance of using an ability...
		List<Ability> abilities = affected.getAbilities();
		if (Global.rand.nextInt(100) < 50 && !abilities.isEmpty())
		{
			Ability a = abilities.get(Global.rand.nextInt(abilities.size()));
			if (a.isEnabled())
			{
				TargetTypes targetType = a.TargetType();
				if (targetType == TargetTypes.Single) targetType = TargetTypes.SingleEnemy;
				actionSelector.setUseAbility(a, Battle.getRandomTargets(targetType, affected, team));
				return;
			}
		}
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
				setToConfusedState(Global.battle.resetPlayerAction(c));
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
				setToConfusedState(actionSelector);
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
				aff.removeStatusEffect("confuse");
				//if the player hasn't gone yet...  But don't sweep the current game state away!  if unconfusing self with a self-attack, just go ahead and finish.
				if (Global.battle.getCurrentActor() != aff)
				{
					if (!Global.battle.getPlayerHasGone(aff))
					{
						Global.battle.resetPlayerAction(aff).skipPlayerInput();
					}
				}
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

	

}
