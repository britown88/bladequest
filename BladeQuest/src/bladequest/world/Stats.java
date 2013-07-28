package bladequest.world;

public enum Stats
{
	Strength,
	Agility,
	Vitality,
	Intelligence,
	MaxHP,
	MaxMP,
	Power,
	Defense,
	MagicPower,
	MagicDefense,
	Speed,
	Evade,
	Block,
	Nullify,
	Fury,
	Fire,
	Earth,
	Wind,
	Water;
	private static Stats[] allValues = values();
    public static Stats fromOrdinal(int n) {return allValues[n];}
    public static int count(){ return allValues.length;}
	
}
