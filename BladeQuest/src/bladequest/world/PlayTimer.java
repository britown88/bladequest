package bladequest.world;

public class PlayTimer 
{
	private int seconds;
	private long startTime;
	private boolean running;
	
	public PlayTimer(int seconds)
	{
		this.seconds = seconds;

	}
	
	public int getSeconds() { return seconds;}
	
	public void start()
	{
		running = true;
		startTime = System.currentTimeMillis();
	}
	
	public void stop()
	{
		running = false;
	}
	
	public void update()
	{
		if(running)
		{
			long time = System.currentTimeMillis();
			if(time - startTime > 1000)
			{
				int s = (int)((time - startTime)/1000.0f);
				seconds += s;
				startTime += s*1000;
			}
		}
	}
	
	public String playTime()
	{		
		return genPlayTimeString(seconds, false);
	}
	
	public static String genPlayTimeString(int seconds, boolean alwaysShowColons)
	{
		int m = (int)(seconds/60.0f);
		int s = seconds - (m*60);
		int h = (int)(m/60.0f);
		m -= h*60;

		String mins = m<10?"0"+m:""+m;
		String secs = s<10?"0"+s:""+s;
		String d = seconds%2 == 0 || alwaysShowColons ? ":" : " ";
		
		return h+d+mins+d+secs;
	}

}
