package bladequest.bladescript.libraries;

import bladequest.bladescript.LibraryWriter;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class GameStartLibrary 
{
	public static void publishLibrary(LibraryWriter library) 
	{
		try {
			library.addAllIn(GameStartLibrary.class);
		} catch (Exception e) {
		}
	}
	
	public static int setStartPosition(int x, int y)
	{
		Global.party.teleport(x, y);
		return 0;
	}
	public static int setStartMap(String name)
	{
		Global.LoadMap(name);
		return 0;
	}
	
	public static PlayerCharacter addStartCharacter(String name)
	{
		Global.party.addCharacter(name);
		return Global.party.getCharacter(name);
	}
		
	
	public static PlayerCharacter setCharacterStartName(PlayerCharacter pc, String name)
	{
		pc.setDisplayName(name);
		return pc;
		
	}

}
