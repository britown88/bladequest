package bladequest.world;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Rect;

public class EncounterZone 
{
	private int encounterRate;
	private Rect zone;
	
	private List<String> encounters;
	
	public EncounterZone(int x, int y, int width, int height, int rate)
	{
		this.encounterRate = rate;
		this.zone = new Rect(x, y, x+width, y+height);
		encounters = new ArrayList<String>();
	}
	
	public String getEncounter()
	{
		if(encounters.size() > 0 && Global.rand.nextInt(100) < encounterRate)
			return encounters.get(Global.rand.nextInt(encounters.size()));
		else
			return null;		
	}
	
	public Rect getZone() { return zone; }
	public void addEncounter(String str) { encounters.add(str); }
	

}
