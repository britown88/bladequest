package bladequest.spudquest;

import java.util.ArrayList;
import java.util.List;

public class GameParameters {

	public List<Player> players;
	
	
	public GameParameters() {
		players = new ArrayList<Player>();
	}

	GameParameters addPlayer(Player p)
	{
		players.add(p);
		return this;
	}
}
