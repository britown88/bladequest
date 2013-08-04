package bladequest.spudquest;

public class Move {

	public Player player;
	public Card card;
	public int x,y;
	
	public Move(Player player, Card card, int x, int y) {
		this.player = player;
		this.card = card;
		this.x = x;
		this.y = y;
	}
}
