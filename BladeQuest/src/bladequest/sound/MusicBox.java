package bladequest.sound;

import android.content.res.AssetFileDescriptor;
import android.media.JetPlayer;
import bladequest.world.Global;


public class MusicBox implements JetPlayer.OnJetEventListener
{
	private JetPlayer jet;
	private String playingSong, lastSong;
	private boolean paused, done, nonInfinite;
	//private JetPlayer.OnJetEventListener listener;
	
	public MusicBox(AssetFileDescriptor afd)
	{
		
		try {
			jet = JetPlayer.getJetPlayer();
			jet.setEventListener(this);
			} catch (Exception e) {}
		
		jet.loadJetFile(afd);
		playingSong = "";
		paused = true;
		nonInfinite = false;
		
	}
	
	public boolean isDone() { return done; }
	public String getPlayingSong() { return playingSong; }
	
	public void play(String songName, boolean playIntro, int repeatCount)
	{		
		if(playingSong.equals(songName) || songName.equals("inherit"))
			return;
		
		//non-infinite-loop
		if(repeatCount != -1)
		{
			//saveSong();
			nonInfinite = true;
			done = false;
		}
			
		
		playingSong = songName;
		Song song = Global.music.get(songName);
		if(song != null)
		{

			playSong(song, playIntro, repeatCount);
			paused = false;
		}
		else
		{
			pause(true);
		}
	}
	
	private void playSong(Song song, boolean playIntro, int repeatCount)
	{
		jet.clearQueue();
		byte ssegmentid = 0;
		if(!song.HasIntro())
			jet.queueJetSegment(song.ID(), -1, repeatCount, 0, 0, ssegmentid);
		else
		{
			if(playIntro)
				jet.queueJetSegment(song.ID(), -1, 0, 0, 0, ssegmentid);			
			jet.queueJetSegment(song.ID()+1, -1, repeatCount, 0, 0, ssegmentid);	
		}		
		
		try {jet.play();} catch (Exception e) {}
	}
	
	public void resumeLastSong()
	{
		play(lastSong, false, -1);

	}
	
	public void saveSong()
	{
		lastSong = playingSong;
	}
	
	public void pause(boolean setPaused)
	{
		if(!paused)
		{
			try {jet.pause();} catch (Exception e) {}	
			if(setPaused)
				paused = true;
		}

		
	}
	
	public void stop()
	{
		jet.release();		
		jet = null;
	}
	
	public void resume()
	{
		if(!paused)
		{
			try {jet.play();} catch (Exception e) {}
			paused = false;
		}
		
	}

	@Override
	public void onJetEvent(JetPlayer player, short segment, byte track,
			byte channel, byte controller, byte value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onJetNumQueuedSegmentUpdate(JetPlayer player, int nbSegments) {
		if(nonInfinite && nbSegments == 0)
		{
			done = true;
			nonInfinite = false;
			//resumeLastSong();
		}
		
	}

	@Override
	public void onJetPauseUpdate(JetPlayer player, int paused) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onJetUserIdUpdate(JetPlayer player, int userId, int repeatCount) {
		// TODO Auto-generated method stub
		
	}
		

}
