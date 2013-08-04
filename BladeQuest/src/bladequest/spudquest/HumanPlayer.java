package bladequest.spudquest;

public class HumanPlayer implements Player {

	Card[] hand;
	
	Move currentMove;
	public HumanPlayer(Card[] hand) {
		this.hand = hand;
		for (Card c : hand)
		{
			c.setOwner(this);
		}
		currentMove = null;
	}

	public void playCard(Card c, int x, int y)
	{
		currentMove = new Move(this, c, x, y);
	}
	
	@Override
	public Card[] getStartingHand() {
		return hand;
	}

	@Override
	public Move takeTurn(Game gameState) {		
		return currentMove;
	}

}
