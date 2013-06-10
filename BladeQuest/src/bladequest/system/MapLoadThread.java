package bladequest.system;

import bladequest.world.BqMap;
import bladequest.world.Global;

public class MapLoadThread extends Thread
{
	private boolean done;
	
	public boolean isDone() { return done; }
	BqMap map;
	
	public MapLoadThread(BqMap map)
	{
		this.map = map;
	}
	
	public void run()
	{
		done = false;
		
		map.load();
		Global.clearPan();
		Global.playTimer.start();
		done = true;
		
	}
	
	

}
