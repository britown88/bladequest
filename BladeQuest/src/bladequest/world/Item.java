package bladequest.world;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import bladequest.battleactions.BattleAction;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.WeaponSwing;
import bladequest.graphics.WeaponSwingDrawable;

public class Item 
{
	private String displayName, icon, description;
	private int count, useCount;
	private Type type;
	private int power, value;
	private boolean equipped, sellable;
	private int[] statMods;
	
	private String swingModel;
	private String swingAnim;
	private BattleAnim battleAnim;
	private int[] swingColorsBase, swingColorsSlash;
	private WeaponSwingDrawable weaponSwing;
	
	private static int id_ = 0;
	private int id;
	
	public String idName;
	
	private List<BattleAction> actions;
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
		actions = new ArrayList<BattleAction>();
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
		
		this.swingAnim = i.swingAnim;
		
		//copy swing model data
		if(i.swingModel != null)
		{
			this.initSwingData(i.swingModel, i.swingAnim);
			for(int j = 0; j < 8; ++j)
			{
				this.swingColorsBase[j] = i.swingColorsBase[j];
				this.swingColorsSlash[j] = i.swingColorsSlash[j];
			}
			
		}
		
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
	
	public List<BattleAction> getActions() { return actions;}
	public void addAction(BattleAction action){actions.add(action);}	
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
	
	public void setAnim(String anim){this.swingAnim = anim;}
	public String getAnimName(){return swingAnim;}
	
	public void initSwingData(String swingModel, String swingAnim)
	{
		this.swingModel = swingModel;
		this.swingAnim = swingAnim;
		swingColorsBase = new int[8];
		swingColorsSlash = new int[8];
	}
	
	public void swingColor(boolean slash, int index, int c)
	{
		(slash ? swingColorsSlash : swingColorsBase)[index] = c;
	}
	
	public void generateSwing()
	{
		if(weaponSwing != null)weaponSwing.release();	
		
		WeaponSwing model = Global.weaponSwingModels.get(swingModel);
		
		weaponSwing = model.genSwingDrawable(swingColorsBase, swingColorsSlash);
		battleAnim = model.genAnim(new BattleAnim(Global.battleAnims.get(swingAnim)));
	}

	public void playAnimation(Point src, Point tar)
	{
		Global.playAnimation(battleAnim, src, tar);
	}
	
	public BattleAnim getAnim() { return battleAnim;}
	
	
	public WeaponSwingDrawable getSwing() { return weaponSwing; }
	
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
		/*for(int i = 0; i < actions.size(); ++i)
			actions.get(i).run(attacker, targets, i*5);*/
	}
	
	public boolean willAffect(Character c)
	{
		for(BattleAction ba : actions)
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
