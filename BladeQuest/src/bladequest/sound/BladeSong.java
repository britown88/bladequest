package bladequest.sound;


import bladequest.serialize.DeserializeFactory;
import bladequest.serialize.Deserializer;
import bladequest.serialize.Serializable;
import bladequest.serialize.Serializer;
import bladequest.world.Global;

public class BladeSong extends Serializable {
	
	private BladeSong()
	{
		super(deserializeTag);
		currentState = new StoppedState();
	}
	private static BladeSong bladeSongInstance;
	
	public synchronized static BladeSong instance()
	{
		if (bladeSongInstance == null)
		{
			bladeSongInstance = new BladeSong();
			Global.saveLoader.registerFactory(deserializeTag, new BladeSongFactory());
		}
		
		return bladeSongInstance;
	}
	public static final String deserializeTag = "BladeSong";
	
	private static class BladeSongFactory extends DeserializeFactory
	{

		BladeSongFactory()
		{
			super(deserializeTag);
		}
		@Override
		public Object deserialize(Deserializer deserializer) {
			
			BladeSong instance = instance();
			instance.stop();
			
			if (deserializer.readInt() != 0) //something should be playing, and it's....
			{
				//TODO: Make this not just guess arguments (e.g. not suck donkey balls)
				instance.play(deserializer.readString(), false, true, 2.0f);
			}
			return instance;
		}
	}
	
	
	private abstract class BladeSongState
	{
		void onUpdate(){}	
		void onStop(){}
		void onPause(){}
		void onResume(){}
		void onSerialize(Serializer serializer){serializer.write(0);} //by default, write 0 for "Stopped",
		String playingSong(){return "";}
		abstract BladeSongState strippedState();
	}
	
	BladeSongState currentState;
	
	private class PlaySongState extends BladeSongState
	{
		Track track;
		String trackName;
		PlaySongState(Song song, String songName, float startVolume, boolean loops, boolean playIntro)
		{
			trackName = songName;
			track = new Track(song, startVolume, loops, playIntro);
			track.play();
		}
		String playingSong()
		{
			return trackName;
		}
		void onUpdate()
		{
			track.update();
			if (track.isDone())
			{
				stop();
			}
		}
		void onPause()
		{
			track.pause();
		}
		void onResume()
		{
			track.play();
		}		
		void onStop()
		{
			track.kill();
			currentState = new StoppedState();
		}
		BladeSongState strippedState()
		{
			return this;
		}
		void onSerialize(Serializer serializer)
		{
			serializer.write(1);
			serializer.write(trackName);
			//more info here....
		}
	}
	
	private class StoppedState extends BladeSongState
	{
		BladeSongState strippedState()
		{
			return this;
		}		
	}	
	private class FadeOutState extends BladeSongState
	{
		PlaySongState parent;
		long startTime;
		float fadeTime;
		FadeOutState(PlaySongState parent, float fadeTime)// in seconds
		{
			this.parent = parent;
			this.fadeTime = fadeTime * 1000.0f;
			this.startTime = System.currentTimeMillis();
		}
		void onUpdate()
		{
			parent.onUpdate();
			long time = System.currentTimeMillis()- startTime;
			if (time > fadeTime)
			{
				stop();
			}
			else
			{
				float volume = 1.0f-(time/((float)fadeTime));
				parent.track.setVolume(volume);
			}
		}		
		void onStop()
		{
			parent.onStop();
		}		
		void onPause()
		{
			stop(); //already dying anyway.
		}
		void onResume()
		{
			//!?!?
			stop();
		}				
		BladeSongState strippedState()
		{
			return parent.strippedState();
		}		
	}	
	
	private class FadeInState extends BladeSongState
	{
		PlaySongState parent;
		long startTime;
		float fadeTime;
		FadeInState(PlaySongState parent, float fadeTime)// in seconds
		{
			this.parent = parent;
			this.fadeTime = fadeTime * 1000.0f;
			this.startTime = System.currentTimeMillis();
		}
		void onUpdate()
		{
			float volume = Math.min(1.0f, (System.currentTimeMillis()- startTime)/((float)fadeTime));
			parent.onUpdate();
			parent.track.setVolume(volume);
		}
		BladeSongState strippedState()
		{
			return parent.strippedState();
		}
		void onStop()
		{
			parent.onStop();
		}				
		void onPause()
		{
			parent.onPause();
		}
		void onResume()
		{
			parent.onResume();
		}		
		void onSerialize(Serializer serializer)
		{
			parent.onSerialize(serializer);
		}		
	}	
	

	public synchronized void stop()
	{
		currentState.onStop();
	}
	
	public synchronized void update()
	{
		currentState.onUpdate();
	}
	
	public synchronized void play(String songName, boolean playIntro, boolean loop, float fadeTime)
	{		
		if(!songName.equals("") && (currentState.playingSong().equals(songName) || songName.equals("inherit")))
			return;
		
		Song song = Global.music.get(songName);
		stop();
		if (song == null) return;
		
		boolean fading = fadeTime > 0.0f;
		float startingVolume = fading ? 0.0f : 1.0f;
		currentState = new PlaySongState(song, songName, startingVolume, loop, playIntro);
		if (fading)
		{
			currentState = new FadeInState((PlaySongState)currentState, fadeTime);
		}
	}
	public synchronized void resume()
	{
		currentState.onResume();
	}
	public synchronized void pause()
	{
		currentState.onPause();
	}
	
	private PlaySongState getPlayingSongState() 
	{
		PlaySongState state = null;
		try
		{
				state = (PlaySongState)(currentState.strippedState());
		}
		catch (Exception e)
		{
			//called from a bad state, derp derp
		}
		return state;
	}
	public String getCurrentSong()
	{
		PlaySongState state = getPlayingSongState();
		if (state == null) return "";
		return state.trackName;
	}
	public synchronized void fadeOut(float fadeTime)
	{
		if (fadeTime <= 0.0f)
		{
			stop();
			return;
		}
		PlaySongState state = getPlayingSongState();
		if (state == null) return; //can't fade out while not playing anything!
		currentState = new FadeOutState(state, fadeTime);
	}
	
	//system music calls for screen on/off
	//nothing special currently...?  since we use "synchronized" here another thread can't nuke your state unexpectedly.
	public synchronized void systemPause()
	{
		pause();
	}	
	public synchronized void systemResume()
	{
		resume();
	}

	@Override
	public synchronized void onSerialize(Serializer serializer) {
		currentState.onSerialize(serializer);
	}	
}
