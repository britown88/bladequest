package bladequest.actions;

import java.util.ArrayList;
import java.util.List;

public class ActionScript 
{
	public enum Status
	{	
		Ready,
		Running,
		Finished
	}
	
	private List<Action> actionList; 
	private Status status;
	
	private int currentActionIndex;
	
	public ActionScript()
	{
		actionList = new ArrayList<Action>(); 
		status = Status.Ready;
		currentActionIndex = 0;
	}
	
	public void addAction(Action action){actionList.add(action);}	
	public boolean hasActions()	{return !actionList.isEmpty();}	
	public Status getStatus(){return status;}	
	
	public Status execute()
	{
		if(status == Status.Finished)
			reset();
		
		if(hasActions() && status == Status.Ready)
		{
			actionList.get(currentActionIndex).run();
			status = Status.Running;
		}
		
		return status;	
		
	}
	
	public void reset()
	{
		status = Status.Ready;
		currentActionIndex = 0;
		
		for(Action a : actionList)
			a.reset();		
	}
	
	
	public Status update()
	{
		if(status == Status.Running)
		{				
			if(actionList.get(currentActionIndex).isDone())
			{					
				currentActionIndex++;
				
				if (currentActionIndex >= actionList.size())					
					status = Status.Finished;
				else
					actionList.get(currentActionIndex).run();
			}
			else if(currentActionIndex+1 < actionList.size() && 
					actionList.get(currentActionIndex).name.equals("actMessage") &&
					actionList.get(currentActionIndex+1).name.equals("actMessage"))
			{				
				actionList.get(currentActionIndex).reset();
				currentActionIndex++;
				actionList.get(currentActionIndex).run();
				
			}
		}
		
		return status;
	}

}
