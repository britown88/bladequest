package bladequest.actions;

import bladequest.world.Global;

public class actExpectInput extends Action
{
	private float delay;
	private long startTime;
	boolean startedListening, receivedInput;
	
	public actExpectInput(float delay)
	{
		this.delay = delay;
	}
	
	public void provideInput()
	{
		receivedInput = true;
		Global.inputExpecter = null;//stop listening
	}
	
	@Override
	public void run()
	{
		if(delay <= 0)
		{
			Global.inputExpecter = this;
			startedListening = true;
		}			
		else
		{
			startTime = System.currentTimeMillis();
			startedListening = false;
		}
		receivedInput = false;
		startBranch(0);
			
	}
	
	@Override
	public boolean isDone()
	{
		if(!startedListening && System.currentTimeMillis() - startTime >= delay*1000.0f)
		{
			Global.inputExpecter = this;
			startedListening = true;
		}
		
		//no actions in the branch, we are done
		if(!runningBranch)
		{
			Global.inputExpecter = null;
			return true;
		}
		else
		{
			//branch is running, if we received input, switch to branch 1
			//otherwise return if branch is done
			if(receivedInput)
			{
				receivedInput = false;
				startNewbranch(1);
				return false;
			}
			else
			{
				if(branchIsDone())
				{
					Global.inputExpecter = null;
					return true;
				}
				else
					return false;
			}
		}
			
	}

}
