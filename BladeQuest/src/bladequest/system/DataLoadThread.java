package bladequest.system;

import bladequest.world.Global;

public class DataLoadThread extends Thread
{
	private boolean done;
	
	public boolean isDone() { return done; }
	
	public DataLoadThread()
	{
	
	}
	
	public void run()
	{
		done = false;
		
		Global.createWorld();
		Global.saveLoader.readSaves(Global.activity);
		
		done = true;
		
	}
	
	

}
