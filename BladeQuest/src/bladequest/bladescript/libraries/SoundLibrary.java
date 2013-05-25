package bladequest.bladescript.libraries;

import bladequest.bladescript.LibraryWriter;
import bladequest.sound.Song;
import bladequest.world.Global;

public class SoundLibrary 
{
	
	public static void publishLibrary(LibraryWriter library) 
	{
		try {
			library.addAllIn(SoundLibrary.class);
		} catch (Exception e) {
		}
	}
	
	public static Song addSong(String name, String path)
	{
		Song song = new Song(path);
		Global.music.put(name, song);
		
		return song;
	}
	
	public static Song addIntro(Song song, String path)
	{
		song.addIntro(path);		
		return song;
	}

}
