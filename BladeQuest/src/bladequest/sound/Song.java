package bladequest.sound;

public class Song 
{
	private int id;
	private boolean hasIntro;
	
	public Song(int id, boolean hasIntro)
	{
		this.id = id;
		this.hasIntro = hasIntro;
	}
	
	public boolean HasIntro(){return hasIntro;}	
	public int ID() { return id; }


}
