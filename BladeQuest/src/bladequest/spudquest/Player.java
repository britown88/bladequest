package bladequest.spudquest;

public interface Player {
	
	Card[] getStartingHand();
	Move takeTurn(Game gameState); //can return NULL to wait.
}
