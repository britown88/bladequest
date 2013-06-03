package bladequest.world;

import java.util.*;

public class ObjectPath 
{
	private List<Actions> actionList;
	private String target;
	int index;
	private boolean done;
	private boolean loop;
	
	static HashMap<Character, ObjectPath.Actions> pathActions;

	
	public ObjectPath(String target)
	{
		actionList = new ArrayList<ObjectPath.Actions>();
		this.target = target;
		index = 0;
		done = false;
		
		pathActions = new HashMap<Character, ObjectPath.Actions>();
		pathActions.put('U', Actions.MoveUp);
		pathActions.put('D', Actions.MoveDown);
		pathActions.put('L', Actions.MoveLeft);
		pathActions.put('R', Actions.MoveRight);
		pathActions.put('u', Actions.FaceUp);
		pathActions.put('d', Actions.FaceDown);
		pathActions.put('l', Actions.FaceLeft);
		pathActions.put('r', Actions.FaceRight);
		pathActions.put('S', Actions.IncreaseMoveSpeed);
		pathActions.put('s', Actions.DecreaseMoveSpeed);
		pathActions.put('V', Actions.Show);
		pathActions.put('v', Actions.Hide);
		pathActions.put('K', Actions.LockFacing);
		pathActions.put('k', Actions.UnlockFacing);
		pathActions.put('W', Actions.Wait);	
		
	}
	
	public void addAction(Actions action)
	{
		actionList.add(action);
	}
	
	public Actions getCurrentAction()
	{
		return actionList.size() > index ? actionList.get(index) : null; 
			
	}
	
	public void advanceActions()
	{
		index += 1;
		if(index >= actionList.size())
			done = true;
	}
	
	public void deserialize(String cmds)
	{
		int i = 0;
		while(i < cmds.length())
		{
			char c = cmds.charAt(i++);
			if(pathActions.containsKey(c))
			{	
				
				String scount = "";				
				while(i < cmds.length() && 
						cmds.charAt(i) >= '0' &&
						cmds.charAt(i) <= '9')
					scount += cmds.charAt(i++); 
				
				int count = scount.length() == 0 ? 1 : Math.max(0, Integer.parseInt(scount));
				for(int j = 0; j < count; ++j)
					addAction(pathActions.get(c));			

			}
		}
	}
	
//	public Actions nextAction()
//	{
//		Actions returnval;
//		if(actionList.size() > 0)
//		{
//			returnval = actionList.get(index);			
//			
//			index += 1;
//			if(index >= actionList.size())
//				done = true;			
//		}
//		else
//			returnval = null;
//		
//		return returnval;		
//	}
	
	public void reset()
	{
		index = 0;
		done = false;
	}
	
	public boolean isDone() { return done; }
	
	public boolean loops() { return loop; }
	public void setLooping(boolean l) { loop = l; }
	
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
