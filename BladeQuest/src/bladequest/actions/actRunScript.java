package bladequest.actions;

import bladequest.actions.ActionScript.Status;

public class actRunScript extends Action
{
	ActionScript script;
	
	public actRunScript()
	{
		script = new ActionScript();
	}
	
	public void addAction(Action action)
	{
		script.addAction(action);
	}
	
	@Override
	public void run()
	{
		script.execute();	
	}
	
	@Override
	public boolean isDone()
	{
		ActionScript.Status result = script.update();
		
		if(result == Status.Finished)
			script.reset();
		
		return result == Status.Finished;
	}

}
