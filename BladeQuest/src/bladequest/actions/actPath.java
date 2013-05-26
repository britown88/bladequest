package bladequest.actions;

import bladequest.actions.Action;
import bladequest.world.*;


public class actPath extends Action 
{
	private ObjectPath path;
	private boolean wait;
	
	public actPath(ObjectPath path, boolean wait)
	{
		super();
		this.path = path;
		this.wait = wait;
	}
	
	public ObjectPath getPath()
	{
		return path;
	}
	
	@Override
	public void run()
	{
		path.attachPath();
	}
	
	@Override
	public boolean isDone()
	{
		if(wait)
			return path.isDone();
		else
			return true;
	}

}
