package bladequest.actions;


public class Action 
{
	protected boolean done;
	public String name;
	public int skip = 0;;

	
	public Action()
	{	
		done = false;
		name = getClass().getSimpleName();
		skip = 0;
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
