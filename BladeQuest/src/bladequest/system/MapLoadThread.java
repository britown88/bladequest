package bladequest.system;

import java.io.InputStream;

import bladequest.world.BqMap;
import bladequest.world.Global;

public class MapLoadThread extends Thread
{
	private String mapname;
	private InputStream file;
	private boolean done;
	
	public boolean isDone() { return done; }
	
	public MapLoadThread(String mapname, InputStream file)
	{
		this.mapname = mapname;
		this.file = file;		
	}
	
	public void run()
	{
		done = false;
		
		Global.map = new BqMap(mapname, file);	
		Global.playTimer.start();
		
		done = true;
		
	}
	
	

}
