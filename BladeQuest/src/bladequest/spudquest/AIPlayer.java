package bladequest.spudquest;

import bladequest.spudquest.Card.Type;
import bladequest.world.Global;

public class AIPlayer implements Player{

	Card[] hand;
	public AIPlayer(Card[] hand) {
		this.hand = hand;
		for (Card c : hand)
		{
			c.setOwner(this);
		}
	}

	@Override
	public Card[] getStartingHand() {
		return hand;
	}

	@Override
	public Move takeTurn(Game gameState) {
		
		int i = 0;
		Card selectedCard = null;
		do 
		{
			i = Global.rand.nextInt(8);
			selectedCard = hand[i];
		} while (selectedCard == null);
		hand[i] = null;
		
		int x = 0;
		int y = 0;
		
		
		Card boardCard = null;
		do
		{
			x = Global.rand.nextInt(4);
			y = Global.rand.nextInt(4);
			boardCard = gameState.board.cardAt(x, y);
		} while (boardCard.getType() != Type.Empty);
		
		gameState.feedback.enemyPlayCard(selectedCard, x, y);
		return new Move(this, selectedCard, x, y);
	}

}
