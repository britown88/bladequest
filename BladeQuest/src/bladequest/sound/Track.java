package bladequest.sound;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import bladequest.world.Global;

public class Track {
	
	private abstract class MusicState
	{
		MusicState(Music player)
		{
			this.player = player;
		}
		protected Music player;
		abstract boolean isPlaying();
		boolean isDead() {return false;}
		void play(){}
		void play(int ms){}
		void pause(){}
		void update(){}
		void onLoad(){}
		int overflow(){return 0;}
	}
	
	
	private class UnsetState extends MusicState
	{
		UnsetState(Music player)
		{
			super(player);
		}
		
		boolean isPlaying()
		{
			return false;
		}
		void play()
		{
			if (!player.isLoaded())
			{
				player.setState(new PlayWaitState(player));
			}
			else
			{
				player.setState(new PlayState(player));
			}
		}
		void play(int ms)
		{
			if (!player.isLoaded())
			{
				player.setState(new PlayFromWaitState(player, ms));
			}
			else
			{
				player.setState(new PlayState(player, ms));
			}
		}		
	}
	
	private class PlayWaitState extends MusicState
	{
		PlayWaitState(Music player)
		{
			super(player);
		}		
		boolean isPlaying() {return true;}
		void pause()
		{
			player.setState(new UnsetState(player));
		}
		void onLoad()
		{
			//actually play.
			player.setState(new PlayState(player));
		}		
	}
	
	private class PlayFromWaitState extends MusicState
	{
		int ms;
		PlayFromWaitState(Music player, int ms)
		{
			super(player);
			this.ms = ms;
		}		
		boolean isPlaying() {return true;}
		void pause()
		{
			player.setState(new UnsetState(player));
		}
		void onLoad()
		{
			//actually play.
			player.mp.seekTo(ms);
			player.setState(new PlayState(player, ms));
		}		
	}

	private class PlayState extends MusicState
	{
		long startTime;
		PlayState(Music player)
		{
			super(player);
			startTime = System.currentTimeMillis();
			player.mp.start();
			
			if (player.loops)
			{
				nextPlay = new Music(player.file, 1.0f, true);
			}
		}		
		PlayState(Music player, long delay)
		{
			super(player);
			startTime = System.currentTimeMillis() - delay;
			player.mp.start(); //restart from a paused state
			player.mp.seekTo((int)delay); //I SURE HOOP THIS WORKS>>FAFa:s,f
			
			if (player.loops)
			{
				nextPlay = new Music(player.file, 1.0f, true);
			}			
		}				
		boolean isPlaying() {return true;}
		void pause()
		{
			if (nextPlay != null)
			{
				nextPlay.kill();
				nextPlay = null;
			}
			player.setState(new PausedState(player, startTime));
		}	
		void update()
		{
			if (System.currentTimeMillis() - startTime > player.duration)
			{
				//oh no, we're out of song
				//unload resources.			
				player.setState(new DeadState(player, (int)(player.duration - (System.currentTimeMillis() - startTime))));			
				
				if (player.loops)
				{
					loop = nextPlay;
					nextPlay = null;
					loop.play();
				}
			}
		}
	}

	
	private class PausedState extends MusicState
	{
		long delayTime;
		PausedState(Music player, long startTime)
		{
			super(player);
			delayTime = System.currentTimeMillis()-startTime;
			player.mp.pause();
		}			
		boolean isPlaying() {return false;}
		void play()
		{
			player.setState(new PlayState(player, delayTime));
		}
	}
	
	
	private class DeadState extends MusicState
	{
		int overflowMS;
		DeadState(Music player, int overflowMS)
		{
			super(player);			
			this.overflowMS = overflowMS;
			player.mp.release();
			player.mp = null;
		}
		boolean isPlaying() 
		{
			return false;
		}
		boolean isDead() {return true;}
		int overflow(){return overflowMS;}
	}
	
	
	private class Music
	{
		boolean loaded = false;
		MediaPlayer mp;
		float volume;
		int duration; 
		MusicState currentState;
		boolean loops;
		String file;
		
		Music(String file, float volume, boolean loops)
		{
			build(file, volume);
			this.loops = loops;
			this.file = file;
		}
		private void build(String file,float volume)
		{
			mp = new MediaPlayer();
			this.volume = volume;
			this.currentState = new UnsetState(this);
			mp.setOnPreparedListener(new OnPreparedListener()
	    	{ 
	    		@Override
	    		public void onPrepared(MediaPlayer mp) {
	    			setLoaded();
	    		}                       
	    	});			
			
	        try {
	            AssetFileDescriptor afd = Global.activity.getAssets().openFd(file);            
	            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());
	            mp.prepareAsync();        
	           
	            afd.close();
	        } catch (Exception e) {Log.d("MEDIAPLAYER", "FUCKED");}			
			
		}
		synchronized void kill()
		{
			if (isDead()) return;
			if (loaded)
			{
				currentState.pause();
				currentState = new DeadState(this, 0);
			}
		}
		synchronized void setState(MusicState state)
		{
			currentState = state;
		}
		synchronized boolean isLoaded()
		{
			return loaded;
		}
		synchronized boolean isDead()
		{
			return currentState.isDead();
		}

		synchronized void update()
		{
			currentState.update();
		}
		
		synchronized float getVolume()
		{
			return this.volume;
		}
		synchronized boolean getLoops()
		{
			return this.loops;
		}
		synchronized void setVolume(float volume)
		{
			if (isDead()) return;
			if (loaded)
			{
				this.volume = volume;
				mp.setVolume(volume, volume);
			}
			else
			{
				this.volume = volume;
			}
		}
		synchronized int getOverflow() 
		{
			return currentState.overflow();
		}
		synchronized void setLoaded()
		{
			loaded = true;
			setVolume(this.volume); //set initial volume state.
			this.duration = mp.getDuration();
			currentState.onLoad();
		}
		synchronized void play()
		{
			currentState.play();
		}

		synchronized void play(int ms)
		{
			currentState.play(ms);
		}		
		synchronized void pause()
		{
			currentState.pause();
		}		
	}
	
	
	Music intro, loop, nextPlay;
	Song song;
	public Track(Song song, float startingVolume, boolean loops, boolean playIntro) 
	{		
		this.song = song;
		if (playIntro && song.HasIntro())
		{
			intro =  new Music(song.IntroPath(), startingVolume, false);
		}
		loop =  new Music(song.Path(), startingVolume, loops);
	}
	synchronized boolean getLoops()
	{
		return loop.getLoops();
	}	
	synchronized float getVolume()
	{
		return loop.getVolume();
	}
	synchronized void setVolume(float newVolume)
	{
		if (intro != null && !intro.isDead()) intro.setVolume(newVolume);
		loop.setVolume(newVolume);
		if (nextPlay != null) loop.setVolume(newVolume);
	}
	private Music activeMusic()
	{
		if (intro == null || intro.isDead())
		{
			return loop;
		}
		else
		{
			return intro;
		}
	}
	synchronized void update()
	{
		if (intro == null || intro.isDead())
		{
			loop.update();
		}
		else
		{
			intro.update();
			if (intro.isDead())
			{
				int overflow = 0;//intro.getOverflow();
				if (overflow > 40)
				{
					loop.play(overflow);
				}
				else
				{
					loop.play();
				}
			}
		}
	}
	synchronized boolean isDone()
	{
		return activeMusic().isDead() && activeMusic() != intro;
	}
	synchronized void play()
	{
		activeMusic().play();
	}
	synchronized void pause()
	{
		activeMusic().pause();
	}
	synchronized void kill()
	{
		if (intro != null && !intro.isDead())
		{
			intro.kill();
		}
		loop.kill();
		if (nextPlay != null)
		{
			nextPlay.kill();
		}
		intro = null;
		loop = null;
		nextPlay = null;
	}
	synchronized void seekTo(int ms)
	{
		boolean loops = getLoops();
		float volume = getVolume();
		kill();
		
		if (song.HasIntro())
		{
			intro =  new Music(song.IntroPath(), volume, false);
		}
		loop =  new Music(song.Path(), volume, loops);
		if (intro != null)
		{
			intro.play(ms);
		}
		else 
		{
			loop.play(ms);
		}
	}
	synchronized void skipIntro()
	{
		if (intro != null && !intro.isDead())
		{
			intro.kill();
			loop.play();
		}
		
	}
	
}
