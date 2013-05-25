package bladequest.sound;


public class Song 
{
	private boolean hasIntro;
	private String introPath;
	private String path;
	
	public Song(String path)
	{
		this.path = path;
		hasIntro = false;
	}
	
	public void addIntro(String path)
	{
		hasIntro = true;
		introPath = path;
	}
	
	public String Path(){ return path; }
	public String IntroPath(){ return introPath; }
	public boolean HasIntro(){ return hasIntro; }



}
