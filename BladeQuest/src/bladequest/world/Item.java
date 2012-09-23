package bladequest.world;

import java.util.*;

import bladequest.battleactions.*;

public class Item 
{
	private String displayName, icon, description;
	private int count, useCount;
	private Type type;
	private int power, value;
	private boolean equipped, sellable;
	private int[] statMods;
	
	private static int id_ = 0;
	private int id;
	
	public String idName;
	
	private List<battleAction> actions;
	private List<String> usableBy;
	private TargetTypes targetType;
	
	public Item(String idName, String name, Type type, String icon, String description)
	{
		this.idName = idName;
		this.displayName = name;
		this.icon = icon;
		this.description = description;
		count = 1;
		id = id_++;
		actions = new ArrayList<battleAction>();
		usableBy = new ArrayList<String>();
		statMods = new int[Stats.NUM_STATS.ordinal()];
		this.type = type;
		power = 0;
		equipped = false;
		useCount = 0;
		
	}
	
	public Item(Item i)
	{
		this.idName = i.idName;
		this.id = i.id;
		this.displayName = i.displayName;
		this.description = i.description;
		this.icon = i.icon;
		this.count = 1;
		
		this.value = i.value;
		this.sellable = i.sellable;
		
		this.actions = i.actions;
		this.usableBy = i.usableBy;
		this.targetType = i.targetType;
		this.type = i.type;
		this.power = i.power;
		this.equipped = i.equipped;
		
		//copy over stat mods
		statMods = new int[Stats.NUM_STATS.ordinal()];	
		for(int j = 0; j < Stats.NUM_STATS.ordinal(); ++j)
			statMods[j] = i.statMods[j];
		
		this.useCount = 0;
	}
	
	//merchant
	public int getValue() { return value; }
	public void setValue(int v) { value = v; }
	
	public boolean isSellable() { return sellable; }
	public void setSellable(boolean b) { sellable = b; }
	
	public void addAction(battleAction action){actions.add(action);}	
	public String getName(){return displayName;}	
	public String getDescription(){return description;}
	public int getCount(){return count - useCount;}	
	public void setCount(int i){count = i; useCount = 0;}
	public int getId(){return id;}
	public Type getType(){return type;}
	public void addUsableBy(String name){usableBy.add(name);}
	public List<String> getUsableChars() { return usableBy; }
	
	public String getIcon() { return icon; }
	
	public void setTargetType(TargetTypes t){targetType = t;}
	public TargetTypes getTargetType() { return targetType; }
	
	public void setPower(int i){ power = i;}
	public int Power(){ return power; }
	
	public boolean isEquipped() { return equipped; }
	
	public void use(){useCount += 1;}
	public void unuse(){useCount = Math.max(0, useCount - 1);}
	public boolean isUsed() { return useCount >= count; }
	
	public void addStatMod(Stats stat, int amt){ statMods[stat.ordinal()] = amt;}
	public void addStatMod(int stat, int amt){ statMods[stat] = amt;}
	public int getStatMod(int stat){return statMods[stat]; }
	
	public void equip(Character c) 
	{ 
		equipped = true; 
		for(int j = 0; j < Stats.NUM_STATS.ordinal(); ++j)
			c.modStat(j, statMods[j]);

	}
	public void unequip(Character c) 
	{ 
		equipped = false; 
		for(int j = 0; j < Stats.NUM_STATS.ordinal(); ++j)
			c.modStat(j, -statMods[j]);
	}
	
	public void execute(Character attacker, List<Character> targets)
	{
		for(int i = 0; i < actions.size(); ++i)
			actions.get(i).run(attacker, targets, i*5);
	}
	
	public boolean willAffect(Character c)
	{
		for(battleAction ba : actions)
			if(!ba.willAffectTarget(c))
				return false;
		
		return true;
	}
	
	public void modifyCount(int i)
	{
		count += i;
		if(count <= 0)
			count = 0;
		if(count > 99)
			count = 99;			
	}
	
	public enum Type
	{
		Usable,
		Weapon,
		Shield,
		Helmet,
		Torso,
		Accessory,
		Key
	}

}
