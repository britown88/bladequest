package bladequest.system;

public class Lock 
{
	private boolean locked;
	
	public Lock()
	{
		locked = false;
	}
	
	public synchronized void lock()
	{
		if(locked)
		{
			do
			{
				try {wait();} catch (Exception e) {}
			}while(locked);			
		}
		locked = true;			
	}
	public synchronized void unlock()
	{
		if(locked)
		{
			locked = false;
			notify();
		}
		
	}

}
