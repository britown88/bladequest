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

	
	public static int playSong(String songName, boolean playIntro, boolean loop, float fadeTime)
	{
		Global.bladeSong.play(songName, playIntro, loop, fadeTime);
		return 0;
	}
	
	public static int fadeMusic(float fadeTime)
	{
		Global.bladeSong.fadeOut(fadeTime);
		return 0;
	}
	
	public static String pushSong() 
	{
		Global.pushSong();
		return Global.bladeSong.getCurrentSong();
	}
	public static String popSong() 
	{
		Global.popSong();
		return Global.bladeSong.getCurrentSong();
	}	
	
}
