package bladequest.world;

import java.util.ArrayList;
import java.util.List;

import bladequest.graphics.BattleAnim;

import android.graphics.Rect;

public class EncounterZone 
{
	private float encounterRate;
	private float currentChance;
	private Rect zone;
	
	private int graceSteps;
	private List<String> encounters;
	
	
	
	public EncounterZone(int x, int y, int width, int height, float rate)
	{
		this.encounterRate = rate;
		this.zone = new Rect(x, y, x+width, y+height);
		encounters = new ArrayList<String>();
		graceSteps = 0;
	}
	
	public String getEncounter()
	{
		if(encounters.size() > 0)
			return encounters.get(Global.rand.nextInt(encounters.size()));
		else
			return null;		
	}
	public int getLevel()
	{
		int level = 0;
		for (String eString : encounters)
		{
			Encounter e = Global.encounters.get(eString);
			if (e.getLevel() > level) level = e.getLevel();
		}
		return level;
	}
	public float levelDifferenceMult()
	{
		
		for (PlayerCharacter t : Global.party.getPartyList(true))
		{
			if (t.hasStatus("lure"))
			{
				return 2.0f;
			}
		}
		
		int partyLevel = Global.party.averageLevel();
		int zoneLevel = getLevel();
		
		if (partyLevel <= zoneLevel) return 1.0f;
		
		float t = (partyLevel - zoneLevel)/5.0f;
		
		if (t > 1.0f) t = 1.0f;
		
		return BattleAnim.linearInterpolation(1.0f, 0.05f, t);
	}
	public boolean checkForEncounters(float growthRate)
	{
		float roll = Global.rand.nextFloat();
		boolean encounter = roll < currentChance;
		
		if (graceSteps > 0)
		{
			--graceSteps;
			encounter = false;
		}
		
		if(encounter)
		{
			graceSteps = 3;
			currentChance = 0;
		}
		else
			if(currentChance == 0)
				currentChance = levelDifferenceMult() * encounterRate / 100.0f;
			else
				currentChance = (float)Math.pow(currentChance, Math.pow(growthRate, levelDifferenceMult()));
		
		return encounter;
	}
	
	public void reset()
	{
		currentChance = 0;
		graceSteps = 0;
	}
	
	public Rect getZone() { return zone; }
	public void addEncounter(String str) { encounters.add(str); }
	

}
