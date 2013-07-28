package bladequest.bladescript.libraries;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import bladequest.bladescript.LibraryWriter;
import bladequest.bladescript.ParserException;
import bladequest.bladescript.ScriptVar;
import bladequest.combatactions.CombatAction;
import bladequest.graphics.Shadow;
import bladequest.world.Global;
import bladequest.world.Item;
import bladequest.world.PlayerCharacter;
import bladequest.world.Stats;

public class CharacterLibrary 
{	
	public static void publishLibrary(LibraryWriter library) 
	{
		try {
			library.addAllIn(CharacterLibrary.class);	
		} catch (Exception e) {
		}
	}
	public static boolean hasStatus(PlayerCharacter character, String statusName)
	{
		return character.hasStatus(statusName);
	}
	public static ScriptVar getPlayersWithStatus(String statusName)
	{
		ScriptVar out = new ScriptVar.EmptyList();
		for (PlayerCharacter p : Global.battle.getParty())
		{
			if (p.hasStatus(statusName))
			{
				try {
					out = new ScriptVar.ListNode(ScriptVar.toScriptVar(p), out);
				} catch (ParserException e) {
					e.printStackTrace();
					Log.d("Parser", e.what());
				}
			}
		}
		return out;
	}
	public static PlayerCharacter createCharacter(String name, String displayName, String battleSprite, String worldSprite)
	{
		PlayerCharacter pc = new PlayerCharacter(name, displayName, battleSprite, worldSprite);
		Global.characters.put(name, pc);
		return pc;
	}
	
	public static PlayerCharacter setPortrait(PlayerCharacter pc, int bmpX, int bmpY)
	{
		pc.setPortrait(bmpX, bmpY);
		return pc;
	}
	
	public static PlayerCharacter setStartingName(PlayerCharacter pc, String name)
	{
		pc.setStartingName(name);
		return pc;
	}
	
	public static PlayerCharacter setAbilityName(PlayerCharacter pc, String name)
	{
		pc.setAbilitiesName(name);
		return pc;
	}
	
	public static PlayerCharacter setCombatAction(PlayerCharacter pc, String name)
	{
		CombatAction comb = null;
		
		try {
			comb = (CombatAction) Class.forName("bladequest.combatactions."+name).getConstructor().newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		pc.setCombatAction(comb);
		return pc;
	}
	
	public static PlayerCharacter setBaseStats(PlayerCharacter pc, int strength, int agility, int vitality, int intelligence)
	{
		pc.setBaseStats(strength, agility, vitality, intelligence);
		pc.fullRestore();
		return pc;
	}
	
	public static PlayerCharacter setLevel(PlayerCharacter pc, int level)
	{
		pc.modifyLevel(level, false);
		pc.fullRestore();
		return pc;
	}
	public static PlayerCharacter setHandEquippable(PlayerCharacter pc, String hand, ScriptVar itemTypeList)
	{
		List<String> stringList = new ArrayList<String>();
		ScriptVar.stringListFromSingleOrList(stringList, itemTypeList);
		
		List<Item.Type> itemTypes = new ArrayList<Item.Type>();
		
		for (String s : stringList)
		{
			itemTypes.add(Item.Type.valueOf(s));
		}
		pc.setHandEquippable(PlayerCharacter.Hand.valueOf(hand), itemTypes);
		return pc;
	}
	public static PlayerCharacter equip(PlayerCharacter pc, String slot, String item)
	{
		pc.firstEquip(PlayerCharacter.Slot.valueOf(slot), item);
		return pc;
	}
	
	public static PlayerCharacter setDisplayName(PlayerCharacter pc, String name)
	{
		pc.setDisplayName(name);
		return pc;
	}
	
	public static PlayerCharacter addAbility(PlayerCharacter pc, String name)
	{
		pc.addAbility(name);
		return pc;
	}
	public static PlayerCharacter setShadow(PlayerCharacter pc, Shadow shadow)
	{
		pc.setShadow(shadow);
		return pc;
	}
	public static PlayerCharacter addLearnableAbility(PlayerCharacter pc, String name, int level)
	{
		pc.addLearnableAbility(name, level);
		return pc;
	}

	public static int getHP(PlayerCharacter pc)
	{
		return pc.getHP();
	}

	public static int getStat(PlayerCharacter pc, String stat)
	{
		return pc.getStat(Stats.valueOf(stat));
	}	

}
