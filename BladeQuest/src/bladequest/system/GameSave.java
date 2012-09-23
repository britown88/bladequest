package bladequest.system;

import android.graphics.Point;
import android.widget.ArrayAdapter;

import java.util.*;

import bladequest.world.Item;
import bladequest.world.Character;

public class GameSave 
{	
	public String mapName, mapDisplayName, playingSong;
	public Point mapPos;
	public int playTime, gold;
	public Map<String, Boolean> switches;
	public Map<String, String> defaultNames;
	public List<Character> characters;
	public List<Item> items;
	public boolean stretchScreen;
	public int fc1r, fc1g, fc1b, fc2r, fc2g, fc2b;
	public int textSpeed;
	public String controlScheme;
	public Map<String, Map<String, Integer>> merchantLimitedQtyItems;
	
	public GameSave()
	{
		switches = new HashMap<String, Boolean>();
		defaultNames = new HashMap<String, String>();
		characters = new ArrayList<Character>();
		items = new ArrayList<Item>();
		//HashMap < MerchantName , Map < ItemName , Count > >
		merchantLimitedQtyItems = new HashMap<String, Map<String,Integer>>();
		
	}
	
	

}
