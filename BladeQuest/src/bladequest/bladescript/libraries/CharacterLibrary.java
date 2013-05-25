package bladequest.bladescript.libraries;

import bladequest.bladescript.LibraryWriter;
import bladequest.combatactions.*;

import bladequest.combatactions.CombatAction;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class CharacterLibrary 
{	
	public static void publishLibrary(LibraryWriter library) 
	{
		try {
			library.addAllIn(CharacterLibrary.class);	
		} catch (Exception e) {
		}
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
	
	public static PlayerCharacter equip(PlayerCharacter pc, String item)
	{
		pc.firstEquip(item);
		return pc;
	}
	
	public static PlayerCharacter addAbility(PlayerCharacter pc, String name)
	{
		pc.addAbility(name);
		return pc;
	}
	
	public static PlayerCharacter addLearnableAbility(PlayerCharacter pc, String name, int level)
	{
		pc.addLearnableAbility(name, level);
		return pc;
	}


}
