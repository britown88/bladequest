package bladequest.world;

import java.util.*;

public class ObjectPath 
{
	private List<Actions> actionList;
	private String target;
	private int index;
	private boolean done;

	
	public ObjectPath(String target)
	{
		actionList = new ArrayList<ObjectPath.Actions>();
		this.target = target;
		index = 0;
		done = false;
		
	}
	
	public void addAction(Actions action)
	{
		actionList.add(action);
	}
	
	public Actions nextAction()
	{
		Actions returnval;
		if(actionList.size() > 0)
		{
			returnval = actionList.get(index);			
			
			index += 1;
			if(index >= actionList.size())
				done = true;			
		}
		else
			returnval = null;
		
		return returnval;		
	}
	
	public void reset()
	{
		index = 0;
		done = false;
	}
	
	public boolean isDone() { return done; }
	
	public void attachPath()
	{
		if(target.equals("party"))
		{
			Global.party.applyPath(this);
		}
		else
		{
			for(GameObject go : Global.map.Objects())
			{
				if(go.Name().equals(target))
				{
					go.applyPath(this);
					break;
				}
					
			}
		}
	}
	
	
	public enum Actions
	{
		MoveLeft,
		MoveUp,
		MoveRight,
		MoveDown,
		FaceLeft,
		FaceUp,
		FaceRight,
		FaceDown,
		LockFacing,
		UnlockFacing,
		IncreaseMoveSpeed,
		DecreaseMoveSpeed,
		Hide,
		Show,
		Wait //waits for 1 second
	}

}
