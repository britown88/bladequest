package bladequest.spudquest;

public abstract class Player {
	
	public Player() 
	{
	}
	
	public abstract Card[] getStartingHand();
	public abstract Move takeTurn(); //can return NULL to wait.
}
