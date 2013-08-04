package bladequest.spudquest;

import bladequest.spudquest.Card.Type;

public class Board {

	Card cards[];
	
	public Board() 
	{
		cards = new Card[4*4];
		
		for (int i = 0; i < 4*4; ++i)
		{
			cards[i] = new Card(new CardParameters());
		}
	}

	Card cardAt(int x, int y)
	{
		return cards[x + y * 4];
	}
	boolean isPlayable(int x, int y)
	{
		return cards[x + y * 4].getType() == Card.Type.Empty;
	}
	
	void playCard(Card c, int x, int y)
	{
		cards[x + y * 4] = c;
	}
}
