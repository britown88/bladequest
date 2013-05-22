package bladequest.combat;

import java.util.List;

import bladequest.scripting.LibraryWriter;
import bladequest.scripting.Script.BadSpecialization;
import bladequest.scripting.ScriptVar.BadTypeException;
import bladequest.world.Enemy;
import bladequest.world.PlayerCharacter;

public class BattleLibrary {

	public static List<Enemy> getEnemies(Battle battle)
	{
		return battle.getEncounter().Enemies();
	}
	public static List<PlayerCharacter> getParty(Battle battle)
	{
		return battle.getParty();
	}
	public static boolean getIsInBattle(PlayerCharacter p)
	{
		return p.isInBattle();
	}	
	public static void publishLibrary(LibraryWriter library) 
	{
		try {
			library.add("getEnemies", BattleLibrary.class.getMethod("getEnemies", Battle.class));
			library.add("getPlayer", BattleLibrary.class.getMethod("getParty", Battle.class));
			library.add("getIsInBattle", BattleLibrary.class.getMethod("getIsInBattle", PlayerCharacter.class));
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (BadTypeException e) {
		} catch (BadSpecialization e) {
		} 
	}
}
