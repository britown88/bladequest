package bladequest.system;

import java.util.ArrayList;
import java.util.List;

import bladequest.graphics.TilePlate;

public class PlateLoadThread extends Thread
{
	private List<TilePlate> plates;
	
	public PlateLoadThread()
	{
		plates = new ArrayList<TilePlate>();
	}
	
	public synchronized void addPlates(List<TilePlate> plates)
	{
		for (TilePlate plate : plates)
		{
			if (!this.plates.contains(plate))
			{
				this.plates.add(plate);
			}
		}
	}
	public synchronized TilePlate getNextPlate()
	{
		if (plates.isEmpty()) return null;
		TilePlate out = plates.get(plates.size()-1);
		plates.remove(plates.size()-1);
		return out;
	}
	
	@Override	
	public void run()
	{
		for (;;)
		{

			TilePlate next = getNextPlate();
			if (next != null)
			{
				next.Load();
			}
			else
			{
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  //wait for input.
			}			
		}
	}
	
	

}
