package bladequest.system;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import bladequest.world.Global;
import bladequest.world.Character;
import bladequest.world.Item;
import bladequest.world.Party;
import bladequest.world.PlayTimer;
import bladequest.world.Stats;
import bladequest.world.Ability;
import bladequest.world.Merchant;
import bladequest.statuseffects.*;

public class GameSaveLoader 
{
	private static final String TAG = GameSaveLoader.class.getSimpleName();
	
	private List<GameSave> saves;
	private final String SaveFile = "saves";
	
	private GameSave tempSave;	
	private Character c;
	
	public GameSave save;
	
	public GameSaveLoader()
	{
		saves = new ArrayList<GameSave>();
	}
	
	public boolean hasSaves()
	{
		return saves.size()> 0;
	}
	
	public List<GameSave> getSaves() { return saves; }
	
	public void deleteSave(int index) { saves.remove(index); }
	
	public void saveGame(int index)
	{
		GameSave save = new GameSave();		
		
		save.mapName = Global.map.Name();
		save.mapDisplayName = Global.map.displayName();
		save.mapPos = Global.party.getGridPos();
		save.playTime = Global.playTimer.getSeconds();
		save.gold = Global.party.getGold();
		
		//options
		save.textSpeed = Global.textSpeed;
		save.controlScheme = "touchpad";//TODO
		save.stretchScreen = Global.stretchScreen;
		save.fc1r = Global.fc1r;
		save.fc1g = Global.fc1g;
		save.fc1b = Global.fc1b;
		save.fc2r = Global.fc2r;
		save.fc2g = Global.fc2g;
		save.fc2b = Global.fc2b;
		
		//default charNames
		for(Character c : Global.characters.values())
			save.defaultNames.put(c.getName(), c.getDisplayName());
		
		//merchant limited qty items
		for(Merchant m : Global.merchants.values())
			if(m.limQtyChanged())
				save.merchantLimitedQtyItems.put(m.getName(), m.getLimQtyItems());			
		
		if(Global.map.BGM().equals("inherit"))
			save.playingSong = Global.musicBox.getPlayingSong();
		
		for(Map.Entry<String, Boolean> entry : Global.switches.entrySet())
			save.switches.put(entry.getKey(), entry.getValue());
		for(Item i : Global.party.getInventory(false))
		{
			Item it = new Item(i);
			it.setCount(i.getCount());
			save.items.add(it);
		}
			
		for(Character c : Global.party.getPartyMembers(true))
			if(c != null)
				save.characters.add(new Character(c));	
			else
				save.characters.add(null);
		
		if(index >= saves.size())
			saves.add(0, save);
		else
			saves.set(index, save);
		
	}
	
	public void loadGame(int index)
	{
		save = saves.get(index);
		
		//options
		Global.stretchScreen = save.stretchScreen;
		Global.panel.scaleImage();
		Global.textSpeed = save.textSpeed;
		Global.fc1r = save.fc1r;
		Global.fc1g = save.fc1g;
		Global.fc1b = save.fc1b;
		Global.fc2r = save.fc2r;
		Global.fc2g = save.fc2g;
		Global.fc2b = save.fc2b;
		
		Global.switches.clear();
		for(Map.Entry<String, Boolean> entry : save.switches.entrySet())
			Global.switches.put(entry.getKey(), entry.getValue());			
		
		Global.party = new Party(save.mapPos.x, save.mapPos.y);
		Global.playTimer = new PlayTimer(save.playTime);
		Global.party.addGold(save.gold);
		for(Item i : save.items)
			Global.party.addItem(i);
		
		//defaultnames
		for(Map.Entry<String, String> name : save.defaultNames.entrySet())
			Global.characters.get(name.getKey()).setDisplayName(name.getValue());
		
		//merchant limited qty items
		for(Map.Entry<String, Map<String, Integer>> lql : save.merchantLimitedQtyItems.entrySet())
			for(Map.Entry<String, Integer> entry : lql.getValue().entrySet())
				Global.merchants.get(lql.getKey()).setLimitedQtyItem(entry.getKey(), entry.getValue(), false);
	
		int i = 0;
		for(Character c : save.characters)
			Global.party.insertCharacter(c, i++);	
		
		Global.loading = true;
		
	}
	
	public void writeSaves(BqActivity activity)
	{
		Log.d(TAG, "Saving game saves files...");
		FileOutputStream fos = null;
		
		//saves.clear();
		
		try {
			fos = activity.openFileOutput(SaveFile, Context.MODE_PRIVATE);
		} catch (Exception e) {
			Log.d(TAG, "Unable to open file for saving.");
			Global.closeGame();
		}
		
		String str = "";
		
		for(GameSave gs : saves)
		{
			str += "save\n";
			str += "map " + gs.mapName + " \"" + gs.mapDisplayName + "\" " + gs.mapPos.x + " " + gs.mapPos.y + "\n";
			str += "playtime " + gs.playTime + "\n";
			str += "gold " + gs.gold + "\n";
			//options
			str += "stretch " + gs.stretchScreen + "\n";
			str += "txtspeed " + gs.textSpeed + "\n";
			str += "fc1 " + gs.fc1r + " " + gs.fc1g + " " + gs.fc1b + "\n";
			str += "fc2 " + gs.fc2r + " " + gs.fc2g + " " + gs.fc2b + "\n";
			
			//defaultnames
			for(Map.Entry<String, String> name : gs.defaultNames.entrySet())
				str += "defname " + name.getKey() + " \"" + name.getValue() + "\"\n";
			
			//merchant limited quantity items
			for(Map.Entry<String, Map<String, Integer>> lql : gs.merchantLimitedQtyItems.entrySet())
				for(Map.Entry<String, Integer> entry : lql.getValue().entrySet())
					str += "mlqi " + lql.getKey() + " " + entry.getKey() + " " + entry.getValue() + "\"\n";
		
			
			if(gs.playingSong != null)
				str += "playingsong " + gs.playingSong + "\n";

			//switches
			for(Map.Entry<String, Boolean> entry : gs.switches.entrySet())
				str += "switch " + entry.getKey() + " " + entry.getValue() + "\n";
			
			//items
			for(Item i : gs.items)
				str += "item " + i.idName + " " + i.getCount() + "\n";
			
			//characters
			int index = 0;
			for(Character c : gs.characters)
			{
				if(c != null)
				{
					str += "character " + c.getName() + " \"" + c.getDisplayName() + "\" " + index + "\n";
					str += "portrait " + c.portrait.x + " " + c.portrait.y + "\n";
					str += "sprites " + c.getWorldSprite().name + " " + c.getBattleSprite().name + "\n";
					str += "level " + c.getLevel() + "\n";
					str += "exp " + c.getExp() + "\n";
					
					str += "hpmp " + c.getHP() + " "  + c.getMP() + "\n";
					
					for(StatusEffect se : c.getStatusEffects())
						str += se.saveLine() + "\n";
					
					for(Item i : c.getEquippedItems())
						str += "equip " + i.idName + "\n";
					
					for(Ability ab : c.getAbilities())
						str += "ability " + ab.name + "\n";
					
					str += "statmods";
					for(int i = 0; i < Stats.NUM_STATS.ordinal(); ++i)
						str += " " + c.getStatMod(i);
					
					str += "\nendcharacter\n";	
				}
				++index;							
			}			
			str += "endsave\n";			
		}
		
		try {
			fos.write(str.getBytes());
			fos.close();
		} catch (Exception e) {
			Log.d(TAG, "Failed to write file!");
			Global.closeGame();
		}
		
		Log.d(TAG, "Saving complete.");
		
	}
	
	public void readSaves(BqActivity activity)
	{
		Log.d(TAG, "Loading save files...");
		FileInputStream is = null;
		
		saves.clear();
		
		if(activity.fileList().length > 0)
		{
			try {
				is = activity.openFileInput(SaveFile);
			} catch (Exception e) {
				Log.d(TAG, "Unable to load saves file");
				Global.closeGame();
			}
			
			FileReader fr = new FileReader(is);
			
			String s = "";
			List<DataLine> lines = new ArrayList<DataLine>();
			
			do
			{ 
				s = fr.ReadLine();
				if(s.length() > 0)
					lines.add(new DataLine(s)); 
			} while(s.length() > 0);
			
			for(DataLine dl : lines)
				loadDataLine(dl);	
		}
		
		Log.d(TAG, "Loading complete");
	}
	
	
	private void loadDataLine(DataLine dl)
	{
		if(dl.item.charAt(0) == '#')
			return;
		else if(dl.item.equals("save"))
		{
			tempSave = new GameSave();
		}
		else if(dl.item.equals("map"))
		{
			tempSave.mapName = dl.values.get(0);
			tempSave.mapDisplayName = dl.values.get(1);
			tempSave.mapPos = new Point(
					Integer.parseInt(dl.values.get(2)), 
					Integer.parseInt(dl.values.get(3)));
		}
		else if(dl.item.equals("playtime"))
		{
			tempSave.playTime = Integer.parseInt(dl.values.get(0));
		}
		else if(dl.item.equals("stretch"))
		{
			tempSave.stretchScreen = Boolean.parseBoolean(dl.values.get(0));
		}
		else if(dl.item.equals("txtspeed"))
		{
			tempSave.textSpeed = Integer.parseInt(dl.values.get(0));
		}
		else if(dl.item.equals("fc1"))
		{
			tempSave.fc1r = Integer.parseInt(dl.values.get(0));
			tempSave.fc1g = Integer.parseInt(dl.values.get(1));
			tempSave.fc1b = Integer.parseInt(dl.values.get(2));
		}
		else if(dl.item.equals("fc2"))
		{
			tempSave.fc2r = Integer.parseInt(dl.values.get(0));
			tempSave.fc2g = Integer.parseInt(dl.values.get(1));
			tempSave.fc2b = Integer.parseInt(dl.values.get(2));
		}
		else if(dl.item.equals("defname"))
		{
			tempSave.defaultNames.put(dl.values.get(0), dl.values.get(1));
		}
		else if(dl.item.equals("mlqi"))
		{
			String merch = dl.values.get(0);
			String item = dl.values.get(1);
			int count = Integer.parseInt(dl.values.get(2));
			
			if(!tempSave.merchantLimitedQtyItems.containsKey(merch))
				tempSave.merchantLimitedQtyItems.put(merch, new HashMap<String, Integer>());
			
			tempSave.merchantLimitedQtyItems.get(merch).put(item, count);
		}
		else if(dl.item.equals("playingsong"))
		{
			tempSave.playingSong = dl.values.get(0);
		}
		else if(dl.item.equals("gold"))
		{
			tempSave.gold = Integer.parseInt(dl.values.get(0));
		}
		else if(dl.item.equals("switch"))
		{
			tempSave.switches.put(dl.values.get(0), Boolean.parseBoolean(dl.values.get(1)));
		}
		else if(dl.item.equals("item"))
		{
			Item item = new Item(Global.items.get(dl.values.get(0)));
			item.setCount(Integer.parseInt(dl.values.get(1)));
			tempSave.items.add(item);
			
		}
		else if(dl.item.equals("character"))
		{
			c = new Character(Global.characters.get(dl.values.get(0)));
			c.setDisplayName(dl.values.get(1));
			int index = Integer.parseInt(dl.values.get(2));
			while(index > tempSave.characters.size())
				tempSave.characters.add(null);
			c.clearEquipment();
			c.clearAbilities();
		}
		else if(dl.item.equals("portrait"))
		{
			c.portrait.x = Integer.parseInt(dl.values.get(0));
			c.portrait.y = Integer.parseInt(dl.values.get(1));
		}
		else if(dl.item.equals("sprites"))
		{
			c.setSprites(dl.values.get(0), dl.values.get(1));
		}
		else if(dl.item.equals("level"))
		{
			c.modifyLevel(Integer.parseInt(dl.values.get(0)), false);
		}
		else if(dl.item.equals("exp"))
		{
			c.setExp(Integer.parseInt(dl.values.get(0)));
		}
		else if(dl.item.equals("hpmp"))
		{
			c.setHpMp(Integer.parseInt(dl.values.get(0)), Integer.parseInt(dl.values.get(1)));
		}
		else if(dl.item.equals("status"))
		{
			
			StatusEffect se = loadStatusEffect(dl);
			if(se.Name().equals("KO"))
				c.kill();
			else				
				c.applyStatusEffect(se);
		}
		else if(dl.item.equals("equip"))
		{
			c.firstEquip(dl.values.get(0));
		}
		else if(dl.item.equals("ability"))
		{
			c.addAbility(dl.values.get(0));
		}
		else if(dl.item.equals("statmods"))
		{
			for(int i = 0; i < Stats.NUM_STATS.ordinal(); ++i)
				c.setStatMod(i, Integer.parseInt(dl.values.get(i)));
		}
		else if(dl.item.equals("endcharacter"))
		{
			tempSave.characters.add(c);
		}
		else if(dl.item.equals("endsave"))
		{
			saves.add(tempSave);
		}
	}
	
	private StatusEffect loadStatusEffect(DataLine dl)
	{
		if(dl.values.get(0).equals("sePoison"))
			return new sePoison(Float.parseFloat(dl.values.get(1)));
		else if(dl.values.get(0).equals("seKO"))
			return new seKO();
		else
			return null;
	}
	
	

}
