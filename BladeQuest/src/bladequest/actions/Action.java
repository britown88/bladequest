package bladequest.actions;

import java.util.ArrayList;
import java.util.List;


public class Action 
{
	protected enum YesNoResult{Yes, No, Unknown}
	
	protected boolean done, runningBranch;
	
	public String name;
	public int skip = 0;	
	
	protected List<Action> runningList;
	protected List<List<Action>> branches;
	
	public void addToBranch(int index, Action action)
	{
		if(index > branches.size() - 1)
			for(int i = 0; i < index - branches.size() + 1; ++i)
				branches.add(new ArrayList<Action>());
		
		branches.get(index).add(action);		
	}
	
	protected void startBranch(int resultIndex)
	{				
		runningList = new ArrayList<Action>(branches.get(resultIndex));
		
		if(!runningList.isEmpty())
		{
			runningBranch = true;
			runningList.get(0).run();			
		}		
	}
	
	//returns whether done
	protected boolean branchIsDone()
	{
		if(runningList.get(0).isDone())
		{
			runningList.remove(0);
			if(runningList.isEmpty())
			{
				runningBranch = false;
				return true;
			}				
			else
				runningList.get(0).run();			
		}
		
		return false;	

	}
	
	public Action()
	{	
		done = false;
		name = getClass().getSimpleName();
		skip = 0;
		
		branches = new ArrayList<List<Action>>();
	}
	
	public boolean isDone()
	{
		return done;
	}
	
	public void run()
	{
	}
	
	public void reset()
	{
		done = false;
	}

}
