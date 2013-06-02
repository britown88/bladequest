package bladequest.combat;

import java.util.ArrayList;
import java.util.List;

import bladequest.battleactions.BattleAction;
import bladequest.battleactions.BattleAction.State;
import bladequest.battleactions.BattleActionPatterns;
import bladequest.battleactions.BattleActionRunner;
import bladequest.battleactions.bactTryEscape;
import bladequest.statuseffects.StatusEffect;
import bladequest.world.Ability;
import bladequest.world.DamageTypes;
import bladequest.world.PlayerCharacter;

public class BattleEvent 
{
	private static final long actTimerLength = 150;//milliseconds
	public static int frameFromActIndex(int index){return (int)(index*actTimerLength);}	
	
	private PlayerCharacter source;
	private List<PlayerCharacter> targets;
	
	private BattleActionRunner actionRunner;
	
	private boolean running, done;
	private long startTime;
	
	PlayerCharacter.Action action;
	Ability ability;
	
	private List<StatusEffect> endTurnStatuses;
	private List<DamageMarker> markers;
	private boolean dontRunStatus;
	
	enum ActionType
	{
		Chosen,
		Special
	}
	
	ActionType type;
	

	
	public BattleEvent(PlayerCharacter.Action action, Ability ability, PlayerCharacter source, List<PlayerCharacter> targets, List<DamageMarker> markers)
	{
		this.action = action;
		this.ability = ability;
		this.source = source;
		this.targets = new ArrayList<PlayerCharacter>(targets);
		this.markers = markers;
		type = ActionType.Chosen;
		dontRunStatus = false;
	}
	
	public BattleEvent(PlayerCharacter.Action action, Ability ability, PlayerCharacter source, List<PlayerCharacter> targets, List<DamageMarker> markers, ActionType type)
	{
		this.action = action;
		this.ability = ability;
		this.source = source;
		this.targets = new ArrayList<PlayerCharacter>(targets);
		this.markers = markers;
		this.type = type;
		dontRunStatus = false;
	}	
	public Ability getAbility() {return ability;}
	PlayerCharacter.Action getAction() {return action;}
	public ActionType getType(){return type;}
	public PlayerCharacter getSource() { return source; }
	public List<PlayerCharacter> getTargets() { return targets;}	
	public boolean isDone(){ return done;}
	public boolean runningStatus() { return endTurnStatuses != null;}
	
	boolean special = false;
	public void setSpecial()
	{
		special = true;
	}
	public boolean isSpecial()
	{
		return special;
	}
	
	public void setActionType(PlayerCharacter.Action action)
	{
		//mainly to reset to skipped, possibly for beserk??/
		this.action = action;
	}
	
	public void dontRunStatusEffects()
	{
		dontRunStatus = true;
	}
	
	private BattleEventBuilder makeBattleEventBuilder()
	{
		return new BattleEventBuilder()
		{	 
			BattleEvent ev;
			BattleEventBuilder initializer(BattleEvent ev)
			{
				this.ev = ev;
				return this;
			}
			@Override				 
			public List<PlayerCharacter> getTargets()
			{
				return targets;
			}
			@Override				 
			public PlayerCharacter getSource()
		    {
				return source;
			}
			@Override
			public int getCurrentBattleFrame()
			{
				return ev.getCurrentFrame();	
			}
			@Override
			public BattleAction getLast() {
				return ev.actionRunner.getLast();
			}
			@Override
			public void addEventObject(BattleAction eventObj) {
				ev.actionRunner.addAction(eventObj);
			}
			@Override
			public void addMarker(DamageMarker marker) {
				markers.add(marker);
			}
		}.initializer(this);	
	}
	
	public void setTargets(List<PlayerCharacter> targets)
	{
		this.targets = targets;
		init();
	}
	public static BattleEventBuilderObject abilityToBattleEventBuilder(Ability ability)
	{
		return new BattleEventBuilderObject()
		{
			Ability ability;
			public BattleEventBuilderObject initialize(Ability ability)
			{
				this.ability = ability;
				return this;
			}
			@Override
			public void buildEvents(BattleEventBuilder builder) {
				for (BattleAction action : ability.getActions())
				{
					builder.addEventObject(action);
				}
			}
			
		}.initialize(ability);
	}
	
	public static BasicAttackBuilder attackBuilder;
	private class AttackBuilder implements BasicAttackBuilder 
	{
		BattleEventBuilder builder;
		AttackBuilder(BattleEventBuilder builder)
		{
			this.builder = builder;
		}
		@Override
		public BattleEventBuilder getEventBuilder() {
			return builder;
		}
		
	}
	public void init()
	{
		if (actionRunner != null)
		{
			actionRunner.reset();
		}
		actionRunner = new BattleActionRunner();
		BattleEventBuilder builder = makeBattleEventBuilder();
		done = running = false;
		
		switch(action)
		{
		case Attack:
			builder.addEventObject(new BattleAction()
			{
				public State run(BattleEventBuilder builder)
				{
					for (PlayerCharacter target : builder.getTargets()) {target.getOnPhysicalHitEvent().trigger();}
					return State.Finished;
				}
			});
			BattleActionPatterns.BuildSwordSlash(builder, 1.0f, DamageTypes.Physical, 1.0f, builder.getLast());
			attackBuilder = new AttackBuilder(builder);
			//TRIGGER WARNING
			builder.getSource().getOnAttackEvent().trigger();
			//TRIGGER WARNING
			
			break;
		case Ability:
			abilityToBattleEventBuilder(ability).buildEvents(builder);
			break;
		case CombatAction:
			source.getEventBuilder().buildEvents(builder);
			break;
		case Item:
			BattleActionPatterns.BuildItemUse(builder);
			break;
		case Run:
			builder.addEventObject(new bactTryEscape());
			break;
		case Guard: case Skipped:
//insta-fail
		default:
			break;
		}
		actionRunner.initialize();
	}
	private int getCurrentFrame()
	{
		if (!running || done) return 0;
		return (int)(System.currentTimeMillis() - startTime);
	}
	public void interrupt()
	{
		actionRunner.interrupt();
	}
	public void update(Battle battle)
	{
		if (done) return;
		if (!source.isInBattle() || battle.isBattleOver())
		{
			running = false;
			done = true;
			actionRunner.reset();
			return;
		}
		if(!running)
		{
			running = true;
			startTime = System.currentTimeMillis();
		}
		else
		{
			BattleEventBuilder builder = makeBattleEventBuilder();
			
			State state = actionRunner.run(builder);
			if (state  == State.Finished)
			{
				if (endTurnStatuses == null) //haven't resolved end turn statuses yet.
				{
					//player turn over, apply status.
					//clone the list, apply all, but don't apply newly added statuses.
					//e.g. double buffer.						
					endTurnStatuses = new ArrayList<StatusEffect>(source.getStatusEffects());
				}
				while (actionRunner.getActions().isEmpty())
				{
					if (endTurnStatuses.isEmpty() || dontRunStatus)
					{
						endTurnStatuses = null; //nothing more to do, we can end the turn.
						done  = true;
						return;
					}
					else  //try and apply a status effect.
					{
						StatusEffect firstStatus = endTurnStatuses.get(0);
						endTurnStatuses.remove(0);
						firstStatus.onTurn(builder);
					}						
				}				
			}
		}
	}
}
