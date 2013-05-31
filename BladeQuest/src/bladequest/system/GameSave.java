package bladequest.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Point;
import bladequest.world.PlayerCharacter;
import bladequest.world.Item;

public class GameSave 
{	
	public String mapName, mapDisplayName, playingSong;
	public Point mapPos;
	public int playTime, gold;
	public Map<String, Boolean> switches;
	public Map<String, String> defaultNames;
	public List<PlayerCharacter> characters;
	public List<Item> items;
	public boolean stretchScreen;
	public int fc1r, fc1g, fc1b, fc2r, fc2g, fc2b;
	public int textSpeed;
	public String controlScheme;
	public Map<String, Map<String, Integer>> merchantLimitedQtyItems;
	StringBuilder serializedString;
	
	public GameSave()
	{
		serializedString = new StringBuilder(); 
		switches = new HashMap<String, Boolean>();
		defaultNames = new HashMap<String, String>();
		characters = new ArrayList<PlayerCharacter>();
		items = new ArrayList<Item>();
		//HashMap < MerchantName , Map < ItemName , Count > >
		merchantLimitedQtyItems = new HashMap<String, Map<String,Integer>>();
		
	}
	
	

}
