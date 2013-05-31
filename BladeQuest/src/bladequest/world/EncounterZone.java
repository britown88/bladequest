package bladequest.world;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Rect;

public class EncounterZone 
{
	private float encounterRate;
	private float currentChance;
	private Rect zone;
	
	private List<String> encounters;
	
	public EncounterZone(int x, int y, int width, int height, float rate)
	{
		this.encounterRate = rate;
		this.zone = new Rect(x, y, x+width, y+height);
		encounters = new ArrayList<String>();
	}
	
	public String getEncounter()
	{
		if(encounters.size() > 0)
			return encounters.get(Global.rand.nextInt(encounters.size()));
		else
			return null;		
	}
	
	public boolean checkForEncounters(float growthRate)
	{
		float roll = Global.rand.nextFloat();
		boolean encounter = roll < currentChance;
		
		if(encounter)
			currentChance = 0;
		else
			if(currentChance == 0)
				currentChance = encounterRate / 100.0f;
			else
				currentChance = (float)Math.pow(currentChance, growthRate);
		
		return encounter;
	}
	
	public void reset()
	{
		currentChance = 0;
	}
	
	public Rect getZone() { return zone; }
	public void addEncounter(String str) { encounters.add(str); }
	

}
