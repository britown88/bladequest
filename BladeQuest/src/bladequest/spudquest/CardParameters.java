package bladequest.spudquest;

import bladequest.spudquest.Card.Type;

public class CardParameters {

	Player owner;
	int hp;
	int damage;
	Card.Type type;
	
	public CardParameters() {
		owner = null;
		hp = 0;
		damage = 0;
		type = Type.Empty;
	}

	public CardParameters setOwner(Player owner)
	{
		this.owner = owner;
		return this;
	}
	public CardParameters setHP(int HP)
	{
		this.hp = HP;
		return this;
	}
	public CardParameters setDamage(int damage)
	{
		this.damage = damage;
		return this;
	}	
	public CardParameters setType(Card.Type type)
	{
		this.type = type;
		return this;
	}		
}
