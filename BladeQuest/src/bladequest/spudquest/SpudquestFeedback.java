package bladequest.spudquest;

public interface SpudquestFeedback {
	void enemyPlayCard(Card c, int x, int y);
	void kill(int x, int y);
	void modifyAttack(int x, int y, int amount);
	void modifyHealth(int x, int y, int amount);
	void reveal(int x, int y);
}
