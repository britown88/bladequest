package bladequest.system;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.graphics.Point;
import android.graphics.Rect;
import bladequest.graphics.TilePlate;
import bladequest.world.Global;

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
		Point p = new Point(Global.vpGridPos);
		//sort on distance to player.
		Rect playerVP = new Rect(p.x, p.y, p.x + Global.vpGridSize.x, p.y +  Global.vpGridSize.y); 
		
		java.util.Collections.sort(plates, new Comparator<TilePlate>()
		{
			Rect playerVP;
			Comparator<TilePlate> initialize(Rect playerVP)
			{
				this.playerVP = playerVP;
				return this;
			}
			int distance(TilePlate lhs)
			{
				int distance = 0;
				Rect r = lhs.getRect();
				if (r.left > playerVP.right) distance += r.left - playerVP.right;
				else if (playerVP.left > r.right) distance += playerVP.left - r.right;
				
				
				if (r.top > playerVP.bottom) distance += r.top - playerVP.bottom;
				else if (playerVP.top > r.bottom) distance += playerVP.top- r.bottom;
				
				return distance;
			}
			public int compare(TilePlate lhs, TilePlate rhs) {
				int d1 = distance(lhs);
				int d2 = distance(rhs);
				if (d1 < d2) return 1;
				else if (d1 > d2) return -1; 
				return 0;
			}	
		}.initialize(playerVP));
		
		
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
