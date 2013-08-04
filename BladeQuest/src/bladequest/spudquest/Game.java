package bladequest.spudquest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bladequest.spudquest.Card.Type;

public class Game {
	
	SpudquestFeedback feedback;

	List<Player> players;
	GameState currentState;
	Board board;
	
	int turn;
	int turnsTaken;
	
	public Game(GameParameters parameters) 
	{
		board  = new Board();
		players = parameters.players;
		turn = 0;
		turnsTaken = 0;
		setupTurn();
	}
	
	public void setFeedback(SpudquestFeedback feedback)
	{
		this.feedback = feedback;
	}
	
	private Player currentPlayer()
	{
		return players.get(turn);
	}

	public void endTurn()  //semi-protect
	{
		turnsTaken++;
		if (!gameIsOver()) //still more to do!
		{
			turn = (turn+1)%players.size();
			setupTurn();	
		}
		else
		{
			//game over man, game over!
		}
	}

	public boolean gameIsOver()
	{
		return turnsTaken == 16;
	}
	public void update()
	{
		currentState.onUpdate();
	}
	
	
	
	

	//private
	
	private void setupTurn()
	{
		currentState = new TakeTurnState(this, currentPlayer());
	}
	
	
	class TakeTurnState extends GameState
	{
		Player player;
		public TakeTurnState(Game game, Player player)
		{
			super(game);
			this.player = player;
		}
		@Override
		public void onUpdate() 
		{
			Move m = player.takeTurn(game);
			if (m != null)
			{
				
				Player defender;
				if (player == game.players.get(0))
				{
					defender = game.players.get(1);
				}
				else
				{
					defender = game.players.get(0);
				}
				//player wants to do thing
				board.playCard(m.card, m.x, m.y);
				
				//vision phase.
				game.revealAt(player, m.card, m.x, m.y);
				
				//battle phase.
				List<Army> playerArmies = new ArrayList<Army>();
				List<Army> enemyArmies = new ArrayList<Army>(); 
				List<List<Army>> enemyArmyEngagements = new ArrayList<List<Army>>();
				game.getBattleArmies(m, playerArmies, enemyArmies, enemyArmyEngagements);
				
				if (!enemyArmies.isEmpty()) //ARE YOU READY TO D-D-D-D-D-D-D-D-DUEL?
				{
				
					//apply positive buffs for both teams.
					for (Army army : playerArmies)
					{
						game.applyBuffs(army);
					}
					for (Army army : enemyArmies)
					{
						game.applyBuffs(army);
					}					
					
					//apply debuffs.
					int enemyArmyCount = enemyArmyEngagements.size();
					for (int i = 0; i < enemyArmyCount; ++i)
					{
						Army zeRussians = enemyArmies.get(i);
						for (Army zeGoodGuys : enemyArmyEngagements.get(i))
						{
							game.applyDebuffs(zeGoodGuys, zeRussians);
							game.applyDebuffs(zeRussians, zeGoodGuys);
						} 
					}
				
					int validDefenders = 0;
					for (Army army : enemyArmies)
					{
						for (Position p : army.army)
						{
							validDefenders |= (1 << (p.x + p.y * 4));
						}
					}					
					
					//dueling time!
					runArmyBattles(playerArmies, validDefenders, player, defender);
					
					//undo battle state.  It's okay to call endBattle on a dead card.
					for (Army army : playerArmies)
					{
						endBattle(army);
					}
					for (Army army : enemyArmies)
					{
						endBattle(army);
					}
				}
				
				//next dude go go go
				game.endTurn();
			}
		}
	}
	
	void endBattle(Army army)
	{
		for (Position card : army.army)
		{
			Card c = board.cardAt(card.x, card.y);
			if (c.bonusHP > 0)
			{
				feedback.modifyHealth(card.x, card.y, -c.bonusHP);
			}
			if (c.bonusPower > 0)
			{
				feedback.modifyAttack(card.x, card.y, -c.bonusPower);
			}			
			c.endBattle();
			
		}
	}
	
	void tryReveal(Player p, Card source,int x, int y)
	{
		if (x < 0 || x > 3 ||
			y < 0 || y > 3) return; //trivial rejection
		
		Card c = board.cardAt(x, y);
		if (c.getType() == Type.Spud && 
			c.getOwner() != p)
		{
			if (!c.isRevealed())
			{
				c.reveal(source);
				feedback.reveal(x, y);
			}
			if (!source.isRevealed())
			{
				source.reveal(null);
			}
		}
	}
	void revealAt(Player p, Card source,int x, int y)
	{
		boolean revealSelf = source.isRevealed();
		
		tryReveal(p, source, x-1, y);
		tryReveal(p, source, x+1, y);
		tryReveal(p, source, x, y-1);
		tryReveal(p, source, x, y+1);
		
		if (revealSelf != source.isRevealed())  //we got revealed!
		{
			feedback.reveal(x, y);
		}
	}
	
	void addAttackSquare(int x, int y, int validDefenders, Player attacker, List<Position> attackable)
	{
		if (x < 0 || x > 3 ||
			y < 0 || y > 3) return; //trivial rejection
		
		if (0 == (validDefenders & (1 << (x + y * 4))) )
		{
			return; //can't attack this square!
		}
		
		Card c = board.cardAt(x, y);
		if (c.getType() == Type.Spud && c.getOwner() != attacker)
		{
			attackable.add(new Position(x,y));
		}
	}
	
	void attack(Position attackLoc, Card attacker, Position defendLoc, Card defender)
	{
		//attacker goes first.
		int damage = attacker.getAttackPower();
		defender.takeDamage(damage);
		
		if (defender.isDead())
		{
			board.playCard(new Card(new CardParameters().setType(Type.DeadSpud)), defendLoc.x, defendLoc.y);
			feedback.kill(defendLoc.x, defendLoc.y);
		}
		else		// check if a counter attack is possible. 
		{
			feedback.modifyHealth(defendLoc.x, defendLoc.y, -damage);
			damage = defender.getAttackPower();
			attacker.takeDamage(damage);
			if (attacker.isDead())
			{
				board.playCard(new Card(new CardParameters().setType(Type.DeadSpud)), attackLoc.x, attackLoc.y);
				feedback.kill(attackLoc.x, attackLoc.y);
			}
			else
			{
				feedback.modifyHealth(attackLoc.x, attackLoc.y, -damage);
			}
		}
	}
	
	void runArmyBattles(List<Army> armies, int validDefenders, Player attacker, Player defender)
	{
		List<Position> attackingCards = new ArrayList<Position>();
		//good guys attack first!
		for (Army army : armies)
		{
			for (Position card : army.army)
			{
				attackingCards.add(card);
			}
		}
		Collections.sort(attackingCards, getPlayerSortDirection(attacker));
		//now attacking cards should be in order of which they should launch their attacks
		
		//get each neighbor, in order.
		for (Position check : attackingCards)
		{
			Card c = board.cardAt(check.x, check.y);
			if (c.getType() == Type.Spud) //not dead yet...
			{
				List<Position> canAttack = new ArrayList<Position>(); //range goes here?
				addAttackSquare(check.x - 1, check.y, validDefenders, attacker, canAttack);
				addAttackSquare(check.x + 1, check.y, validDefenders, attacker, canAttack);
				addAttackSquare(check.x, check.y-1, validDefenders, attacker, canAttack);
				addAttackSquare(check.x, check.y+1, validDefenders, attacker, canAttack);
				
				if (!canAttack.isEmpty())
				{
					//sort in reverse order.  We use this like a stack so that we can pop.
					Collections.sort(canAttack, getPlayerSortDirection(defender));
			
					do
					{
						Position attackSquare = canAttack.get(canAttack.size()-1);
						canAttack.remove(canAttack.size()-1);
						Card defenderCard = board.cardAt(attackSquare.x, attackSquare.y);
						if (defenderCard.getType() == Type.Spud && defenderCard.getOwner() != attacker)
						{
							//LETS GET READY TO RUUUUUUUMBLE
							attack(check, c, attackSquare, defenderCard);
						}
						c = board.cardAt(check.x, check.y);
					} while (c.getType() == Type.Spud && !canAttack.isEmpty());
				}
			}
		}
	}
	
	Comparator<Position> getPlayerSortDirection(Player p)
	{
		if (p == players.get(0))
		{
			return new Comparator<Position>()
					{
						@Override
						public int compare(Position lhs, Position rhs) {
							int dY = lhs.y - rhs.y;
							if (dY == 0) return lhs.x - rhs.x;
							return dY;
						}
					};
		}
		else
		{
			return new Comparator<Position>()
					{
						@Override
						public int compare(Position lhs, Position rhs) {
							int dY = rhs.y - lhs.y;
							if (dY == 0) return rhs.x - lhs.x;
							return dY;
						}
					};			
		}
	}
	
	void applyBuffs(Army army)
	{
		int armyBuff = army.army.size()-1;
		for (Position position : army.army)
		{
			Card c = board.cardAt(position.x, position.y);
			if (armyBuff != 0  && c.getType() == Type.Spud)
			{
				c.applyArmyBuff(armyBuff);
				feedback.modifyAttack(position.x, position.y, armyBuff);
				feedback.modifyHealth(position.x, position.y, armyBuff);
			}
		}
	}
	
	void applyDebuffs(Army source, Army targets)
	{
		
	}
	
	void getBattleArmies(Move m, List<Army> playerArmies, List<Army> enemyArmies, List<List<Army>> enemyArmyEngagements)
	{
		List<List<Position> > enemyArmyPositions =  enumerateSurroundedArmies(m.x, m.y);
		int armySquareTaken = 0;
		for (List<Position> army : enemyArmyPositions)
		{
			enemyArmies.add(new Army(army));
			List<List<Position> > playerArmyPositions = enumerateSurroundingArmies(army);
			enemyArmyEngagements.add(new ArrayList<Army>());
			for (List<Position> playerArmy : playerArmyPositions)
			{
				Army pArmy = new Army(playerArmy);
				enemyArmyEngagements.get(enemyArmyEngagements.size()-1).add(pArmy);
				Position first = playerArmy.get(0); 
				if (0 == (armySquareTaken & (1 << (first.x + first.y*4)))) //army is unique.
				{
					playerArmies.add(pArmy);
					for (Position unit : playerArmy)
					{
						armySquareTaken |= (1 << (unit.x + unit.y*4));
					}
				}
			}
		}
	}
	
	public static class Army
	{
		List<Position> army;
		Army(List<Position> army)
		{
			this.army = army;
		}
	}
	
	public static class Position 
	{
		public int x, y;
		public Position(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
	}
	
	
	//returns false if there is an empty square here, and thus the army isn't surrounded!
	boolean checkSurroundSquare(int x, int y, Player checkPlayer, List<Position> toCheck, List<Position> openList, List<Position> closedList)
	{
		if (x < 0 || y < 0 || x > 3 || y > 3) return true;  //trivial out-of-bounds.
		
		//we're masking another check, delete it so we don't double-evaluate the armies.
		for (Position c : toCheck)
		{
			if (c.x == x && c.y == y)
			{
				toCheck.remove(c);
				break;
			}
		}
		
		Card c = board.cardAt(x, y);
		switch (c.getType())
		{
		case Empty:
			return false;// army not surrounded.
		case Spud:
			if (c.getOwner() != checkPlayer) //an enemy!  don't add to openlist, but we're done here.
			{
				return true;
			}
			else
			{
				for (Position bc : closedList)
				{
					if (x == bc.x && y == bc.y)
					{
						//already evaluated.
						return true;
					}
				}
				Position newCheck = new Position(x,y);
				openList.add(newCheck);
				return true;
			}
		default:
			return true; //eh? 
		}
	}
	//prereq: check square is a player.
	List<Position> getSurroundedArmy(Position check, List<Position> toCheck)
	{
		//make a list of possible moves, and keep a closed list.
		List<Position> openList = new ArrayList<Position>();
		List<Position> closedList = new ArrayList<Position>();
			
		openList.add(check);
		
		Player checkPlayer = board.cardAt(check.x, check.y).getOwner();
		
		while (!openList.isEmpty())
		{
			Position currentCheck = openList.get(openList.size()-1);
			openList.remove(openList.size()-1);
		
			int x = currentCheck.x;
			int y = currentCheck.y;
			
			if (!checkSurroundSquare(x-1, y, checkPlayer, toCheck, openList, closedList)) return null;
			if (!checkSurroundSquare(x+1, y, checkPlayer, toCheck, openList, closedList)) return null;
			if (!checkSurroundSquare(x, y-1, checkPlayer, toCheck, openList, closedList)) return null;
			if (!checkSurroundSquare(x, y+1, checkPlayer, toCheck, openList, closedList)) return null;
			
			closedList.add(currentCheck);
		}
		
		//if we got here, the closed list contains the surrounded army.
		return closedList;
	}
	//prereq: check square is a player.
	List<Position> getSurroundingArmy(Position check, List<Position> toCheck)
	{
		//make a list of possible moves, and keep a closed list.
		List<Position> openList = new ArrayList<Position>();
		List<Position> closedList = new ArrayList<Position>();
			
		openList.add(check);
		
		Player checkPlayer = board.cardAt(check.x, check.y).getOwner();
		
		while (!openList.isEmpty())
		{
			Position currentCheck = openList.get(openList.size()-1);
			openList.remove(openList.size()-1);
		
			int x = currentCheck.x;
			int y = currentCheck.y;
			
			checkSurroundSquare(x-1, y, checkPlayer, toCheck, openList, closedList);
			checkSurroundSquare(x+1, y, checkPlayer, toCheck, openList, closedList);
			checkSurroundSquare(x, y-1, checkPlayer, toCheck, openList, closedList);
			checkSurroundSquare(x, y+1, checkPlayer, toCheck, openList, closedList);
			
			closedList.add(currentCheck);
		}
		
		//return final army.
		return closedList;
	}		
	boolean enemyAtSquare(int x, int y, Player checkPlayer)
	{
		Card c = board.cardAt(x,y);
		return c.getType() == Card.Type.Spud && c.getOwner() != checkPlayer;
	}
	
	void checkForBattle(int x, int y, List<Position> toCheck)
	{
		if (x >= 0 && y >= 0 && x < 4 && y < 4)
		{
			if (enemyAtSquare(x, y, currentPlayer()))
			{
				toCheck.add(new Position(x, y));	
			}
		}
	}
	List<List<Position> > enumerateSurroundedArmies(int x, int y)
	{
		List<List<Position> > out = new ArrayList<List<Position> >();
		List<Position> toCheck = new ArrayList<Position>();
		checkForBattle(x-1, y, toCheck);
		checkForBattle(x+1, y, toCheck);
		checkForBattle(x, y-1, toCheck);
		checkForBattle(x, y+1, toCheck);
		
		while (!toCheck.isEmpty())
		{
			Position check = toCheck.get(toCheck.size()-1);
			toCheck.remove(toCheck.size()-1);
			List<Position> army = getSurroundedArmy(check, toCheck);
			
			if (army != null)
			{
				out.add(army);
			}
		}
		
		return out;
	}
	
	void checkIfSurrounding(int x, int y, Player thisPlayer, List<Position> toCheck)
	{
		if (x < 0 || x > 3 || y < 0 || y > 3) return;
		
		Card checkCard = board.cardAt(x, y); 
		if (checkCard.getType() != Card.Type.Spud) return;
		if (checkCard.getOwner() == thisPlayer) return;
		
		for (Position c : toCheck)
		{
			if (x == c.x && y == c.y) return;
		}
		
		//OKAY, WE'RE GOOD.  THIS IS A LEGIT ENEMY
		toCheck.add(new Position(x, y));
	}
	List<List<Position> > enumerateSurroundingArmies(List<Position> surroundedArmy)
	{
		Position first = surroundedArmy.get(0);
		Player surroundedPlayer = board.cardAt(first.x, first.y).getOwner();
		List<List<Position> > out = new ArrayList<List<Position> >();
		List<Position> toCheck = new ArrayList<Position>();
		
		for (Position armyMember : surroundedArmy)
		{
			int x = armyMember.x;
			int y = armyMember.y;
			checkIfSurrounding(x-1, y, surroundedPlayer, toCheck);
			checkIfSurrounding(x+1, y, surroundedPlayer, toCheck);
			checkIfSurrounding(x, y-1, surroundedPlayer, toCheck);
			checkIfSurrounding(x, y+1, surroundedPlayer, toCheck);
		}
		
		
		//get dem sweet, sweet surrounding armies.
		while (!toCheck.isEmpty())
		{
			Position check = toCheck.get(toCheck.size()-1);
			toCheck.remove(toCheck.size()-1);
			List<Position> army = getSurroundingArmy(check, toCheck);
			
			if (army != null)
			{
				out.add(army);
			}
		}
		
		//surrounding armies ghetto daze
		return out;
	}
	
}
