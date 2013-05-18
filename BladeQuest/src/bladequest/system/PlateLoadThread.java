package bladequest.system;

import java.util.List;

import bladequest.graphics.TilePlate;

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
