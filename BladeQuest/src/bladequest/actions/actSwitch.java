package bladequest.actions;

import bladequest.actions.Action;
import bladequest.world.*;


public class actSwitch extends Action 
{
	String switchName;
	boolean state;
	
	public actSwitch(String switchName, Boolean state)
	{
		super();
		this.switchName = switchName;
		this.state = state;
	}
	
	@Override
	public void run()
	{
		Global.switches.put(switchName, state);
		done = true;
	}
	
	@Override
	public boolean isDone()
	{
		return done;
	}

}
