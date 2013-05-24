package bladequest.sound;


public class Song 
{
	private int loopToMS;
	private String path;
	
	public Song(String path, int loopToMS)
	{

		this.loopToMS = loopToMS;
		this.path = path;
	}
	
	public String Path(){ return path; }
	public int LoopToMS(){return loopToMS;}	



}
