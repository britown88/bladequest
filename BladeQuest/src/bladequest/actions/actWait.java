package bladequest.actions;



public class actWait extends Action 
{
	private float seconds;
	private long startTime;
	
	public actWait(float seconds)
	{
		super();
		this.seconds = seconds;
	}
	
	@Override
	public void run()
	{
		startTime = System.currentTimeMillis();
	}
	
	@Override
	public boolean isDone()
	{
		long elapsed = System.currentTimeMillis() - startTime;
		return (float)elapsed / 1000.0f >= seconds;
	}

}
