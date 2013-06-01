package bladequest.bladescript.libraries;

import bladequest.bladescript.LibraryWriter;
import bladequest.bladescript.Script.BadSpecialization;
import bladequest.bladescript.ScriptVar;
import bladequest.bladescript.ScriptVar.BadTypeException;
import bladequest.combat.Battle;
import bladequest.enemy.Enemy;
import bladequest.world.Encounter;
import bladequest.world.Global;

public class EncounterLibrary {

	public static void publishLibrary(LibraryWriter writer)
	{
		try {
			writer.addAllIn(EncounterLibrary.class);
		} catch (BadTypeException e) {
			e.printStackTrace();
		} catch (BadSpecialization e) {
			e.printStackTrace();
		}
	}
	
	public static Encounter createEncounter(String name, String backdrop)
	{
		Encounter encounter =  new Encounter(name, backdrop);
		Global.encounters.put(name, encounter);
		return encounter;
	}
	public static Encounter addEnemy(Encounter encounter, String enemy, int x, int y)
	{
		encounter.addEnemy(enemy, x, y);
		return encounter;
	}
	public static Encounter disableRunning(Encounter encounter)
	{
		encounter.disableRunning = true;
		return encounter;
	}	
	public static Encounter onEncounterBegin(Encounter encounter, ScriptVar script)
	{
		encounter.addScript(script);
		return encounter;
	}
	public static Encounter getEncounter(Battle battle)
	{
		return battle.getEncounter();
	}
	public static Enemy getEncounterEnemy(Encounter encounter, int enemyNumber)
	{
		return encounter.Enemies().get(enemyNumber);
	}
}
