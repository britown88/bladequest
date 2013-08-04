package bladequest.spudquest;

public class Card {

	Type type;
	Player owner;
	
	int attackPower;
	int HP;
	
	int bonusHP;
	int bonusPower;
	
	boolean revealed;
	
	public enum Type
	{
		Spud,
		DeadSpud,
		Empty
	}
	public Card(CardParameters params)
	{
		this.type = params.type;
		this.owner = params.owner;
		this.HP = params.hp;
		this.attackPower = params.damage;
		this.revealed = false;
	}
	
	public int getHP()
	{
		return HP;
	}
	public int getDamage()
	{
		return attackPower;
	}
	
	public Type getType()
	{
		return type;
	}
	public Player getOwner()
	{
		return owner;
	}
	public void setOwner(Player owner)
	{
		this.owner = owner;
	}
	//overload meee
	public int getAttackPower()
	{
		return attackPower + bonusPower;
	}
	public void reveal(Card source)
	{
		revealed = true;
	}
	public boolean isRevealed()
	{
		return revealed;
	}
	public void takeDamage(int damage)
	{
		if (bonusHP > 0)
		{
			if (bonusHP > damage)
			{
				bonusHP -= damage;
				return;
			}
			damage -= bonusHP;
			bonusHP = 0;
		}
		HP -= damage;
	}
	public boolean isDead()
	{
		return HP <= 0;
	}
	public void applyArmyBuff(int buff)
	{
		bonusHP = bonusPower = buff;
	}
	public void endBattle()
	{
		bonusHP = bonusPower = 0;
	}
	
}
