package bladequest.battleactions;

import java.util.ArrayList;
import java.util.List;

import bladequest.combat.BattleEventBuilder;
import bladequest.combat.DamageMarker;
import bladequest.world.PlayerCharacter;

public class BattleAction 
{
	List<BattleAction> children;
	int refCount;
	boolean finished;
	public BattleAction()	
	{
		children = new ArrayList<BattleAction>();
		refCount = 0;
		finished = false;
	}
	public BattleAction addDependency(BattleAction parent)
	{
		if (parent != null)
		{
			parent.children.add(this);
		}
		return this;
	}
	
	public void incref()
	{
		++refCount;
	}
	public void decref()
	{
		--refCount;
	}
	public final void reset()
	{
		refCount = 0;
		clearState();
	}
	
	public void setReferences()
	{
		for (BattleAction action : children)
		{
			action.incref();
		}
		finished = false;
		initialize();
	}
	public void removeReferences()
	{
		if (!finished)
		{
			finished = true;		
			for (BattleAction action : children)
			{
				action.decref();
			}
		}
	}
	public boolean isFinished()
	{
		return finished;
	}
	

	
	public static PlayerCharacter getTarget(BattleEventBuilder builder)
	{
		List<PlayerCharacter> targets = builder.getTargets();
		if (targets == null || targets.size() < 1) return null;
		return builder.getTargets().get(0);
	}
	public static BattleEventBuilder changeTargets(BattleEventBuilder builder, PlayerCharacter target)
	{
		List<PlayerCharacter> charList = new ArrayList<PlayerCharacter>();
		charList.add(target);
		return changeTargets(builder, charList);
	}
	public static BattleEventBuilder changeTargets(BattleEventBuilder builder, List<PlayerCharacter> targets)
	{
		return new BattleEventBuilder()
		{
			BattleEventBuilder builder;
			List<PlayerCharacter> targets;
			BattleEventBuilder initialize(BattleEventBuilder builder, List<PlayerCharacter> targets)
			{
				this.builder = builder;
				this.targets = targets;
				return this;
			}
			@Override
			public List<PlayerCharacter> getTargets() {
				return targets;
			}

			@Override
			public BattleAction getLast()
			{
				return builder.getLast();
			}
			@Override
			public PlayerCharacter getSource() {
				return builder.getSource();
			}

			@Override
			public void addEventObject(BattleAction eventObj) {
				builder.addEventObject(eventObj);
			}

			@Override
			public void addMarker(DamageMarker marker) {
				builder.addMarker(marker);
			}

			@Override
			public int getCurrentBattleFrame() {
				return builder.getCurrentBattleFrame();
			}
		}.initialize(builder, targets);
	}
	
	public static BattleEventBuilder changeSource(BattleEventBuilder builder, PlayerCharacter source)
	{
		return new BattleEventBuilder()
		{
			BattleEventBuilder builder;
			PlayerCharacter source;
			BattleEventBuilder initialize(BattleEventBuilder builder, PlayerCharacter source)
			{
				this.builder = builder;
				this.source = source;
				return this;
			}
			@Override
			public List<PlayerCharacter> getTargets() {
				return builder.getTargets();
			}

			@Override
			public BattleAction getLast()
			{
				return builder.getLast();
			}
			@Override
			public PlayerCharacter getSource() {
				return source;
			}

			@Override
			public void addEventObject(BattleAction eventObj) {
				builder.addEventObject(eventObj);
			}

			@Override
			public void addMarker(DamageMarker marker) {
				builder.addMarker(marker);
			}

			@Override
			public int getCurrentBattleFrame() {
				return builder.getCurrentBattleFrame();
			}
		}.initialize(builder, source);
	}
	
	public static BattleEventBuilder changeRunner(BattleEventBuilder builder, BattleActionRunner runner)
	{
		return new BattleEventBuilder()
		{
			BattleEventBuilder builder;
			BattleActionRunner runner;
			BattleEventBuilder initialize(BattleEventBuilder builder, BattleActionRunner runner)
			{
				this.builder = builder;
				this.runner = runner;
				return this;
			}
			@Override
			public List<PlayerCharacter> getTargets() {
				return builder.getTargets();
			}

			@Override
			public BattleAction getLast()
			{
				return runner.getLast();
			}
			@Override
			public PlayerCharacter getSource() {
				return builder.getSource();
			}

			@Override
			public void addEventObject(BattleAction eventObj) {
				runner.addAction(eventObj);
			}

			@Override
			public void addMarker(DamageMarker marker) {
				builder.addMarker(marker);
			}

			@Override
			public int getCurrentBattleFrame() {
				return builder.getCurrentBattleFrame();
			}
		}.initialize(builder, runner);
	}
	
	//OVERRIDE MEEEE  AAHHHH
	public void initialize() {}
	public void clearState() {}
	
	public enum State 
	{
		Continue,  //need to run more
		Finished, //can delete safely
	}
	
	public State run(BattleEventBuilder builder){return State.Finished;}
	public void runOutsideOfBattle(PlayerCharacter attacker, List<PlayerCharacter> targets, List<DamageMarker> markers) {}
	public boolean isRoot() {return refCount == 0; }
	public boolean willAffectTarget(PlayerCharacter target){ return true;}
}