package bladequest.sound;

import bladequest.combat.BattleLibrary;
import bladequest.scripting.LibraryWriter;
import bladequest.scripting.Script.BadSpecialization;
import bladequest.scripting.ScriptVar.BadTypeException;
import bladequest.world.Global;

public class SoundLibrary 
{
	
	public static void publishLibrary(LibraryWriter library) 
	{
		try {
			library.add("addsong", SoundLibrary.class.getMethod("addSong", String.class, String.class, int.class));
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (BadTypeException e) {
		} catch (BadSpecialization e) {
		}
	}
	
	public static Song addSong(String name, String path, int startMS)
	{
		Song song = new Song(path, startMS);
		Global.music.put(name, song);
		
		return song;
	}

}
