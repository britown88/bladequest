package bladequest.system;

import bladequest.graphics.TilePlate;
import bladequest.world.*;
import java.util.*;

import java.util.*;

public class PlateLoadThread extends Thread
{
	private List<TilePlate> plates;
	
	public PlateLoadThread(List<TilePlate> plates)
	{
		this.plates = plates;
	}
	
	@Override	
	public void run()
	{
		for(TilePlate plate : plates)
			plate.Load();
	}
	
	

}
