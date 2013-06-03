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
	protected List<Boolean> loopBranches;
	private boolean branchIsLooping;
	private int branchActionIndex;

	
	public void addToBranch(int index, Action action)
	{
		addNewLists(index);		
		branches.get(index).add(action);		
	}
	
	public void setBranchLoop(int index, boolean loop)
	{
		addNewLists(index);
		loopBranches.set(index, loop);	
	}
	
	private void addNewLists(int index)
	{
		int size = branches.size();
		if(index > size - 1)
			for(int i = 0; i < index - size + 1; ++i)
			{
				branches.add(new ArrayList<Action>());
				loopBranches.add(false);
			}
	}
	
	protected void startBranch(int resultIndex)
	{		
		newIndex = -1;
		addNewLists(resultIndex);
		
		runningList = new ArrayList<Action>(branches.get(resultIndex));
		branchIsLooping = loopBranches.get(resultIndex);
		branchActionIndex = 0;
		
		if(!runningList.isEmpty())
		{
			runningBranch = true;
			runningList.get(0).run();	
			if(runningList.get(branchActionIndex).name.equals("actMessage"))
				while(branchActionIndex + 1 < runningList.size() && 
						runningList.get(branchActionIndex + 1).name.equals("actMessage"))
				{
					branchActionIndex += 1;
					runningList.get(branchActionIndex).run();
				}
		}		
	}
	private int newIndex;
	protected void startNewbranch(int index)
	{
		newIndex = index;
	}
	
	//returns whether done
	protected boolean branchIsDone()
	{
		if(runningList.get(branchActionIndex).isDone())
		{
			if(newIndex != -1)
				startBranch(newIndex);
			{
				++branchActionIndex;
				if(branchActionIndex >= runningList.size())
				{
					if(branchIsLooping)
					{
						branchActionIndex = 0;
						runningList.get(branchActionIndex).run();
						if(runningList.get(branchActionIndex).name.equals("actMessage"))
							while(branchActionIndex + 1 < runningList.size() && 
									runningList.get(branchActionIndex + 1).name.equals("actMessage"))
							{
								branchActionIndex += 1;
								runningList.get(branchActionIndex).run();
							}
							
					}
					else
					{
						runningBranch = false;
						return true;
					}				
				}				
				else
				{
					runningList.get(branchActionIndex).run();
					if(runningList.get(branchActionIndex).name.equals("actMessage"))
						while(branchActionIndex + 1 < runningList.size() && 
								runningList.get(branchActionIndex + 1).name.equals("actMessage"))
						{
							branchActionIndex += 1;
							runningList.get(branchActionIndex).run();
						}
				}
						
			}					
		}		
		return false;	
	}
	
	public Action()
	{	
		done = false;
		name = getClass().getSimpleName();
		skip = 0;
		
		branches = new ArrayList<List<Action>>();
		loopBranches = new ArrayList<Boolean>();
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
		branchIsLooping = false;
		done = false;
	}

}
