package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;

public abstract class DelegatingAction extends BattleAction {

	protected BattleActionRunner runner;
	
	@Override
	public void initialize()
	{
		runner = null;
	}
	public void clearState()
	{
		if (runner != null) runner.reset();
	}
	
	protected abstract void buildEvents(BattleEventBuilder builder);
	protected BattleEventBuilder getAdaptedBuilder(BattleEventBuilder builder)
	{
		return changeRunner(builder, runner);
	}
	public State run(BattleEventBuilder builder)
	{
		if (runner == null)
		{
			runner = new BattleActionRunner();
			buildEvents(changeRunner(builder, runner));
			runner.initialize();
		}
		return runner.run(getAdaptedBuilder(builder));
	}
}
