package bladequest.bladescript.libraries;

import android.graphics.Color;
import bladequest.battleactions.BattleAction;
import bladequest.bladescript.LibraryWriter;
import bladequest.world.Global;
import bladequest.world.Item;
import bladequest.world.Stats;
import bladequest.world.TargetTypes;

public class ItemLibrary {

	public static Item addItem(String gameName, String displayName, String itemType, String itemIcon, String description)
	{
		Item itm = new Item(gameName, displayName, Item.Type.valueOf(itemType), itemIcon, description);
		Global.items.put(gameName, itm);
		return itm;
	}
	public static Item setPower(Item itm, int power)
	{
		itm.setPower(power);
		return itm;
	}
	public static Item setUsableBy(Item itm, String character)
	{
		itm.addUsableBy(character);
		return itm;
	}
	public static Item setTargetType(Item itm, String targetType)
	{
		itm.setTargetType(TargetTypes.valueOf(targetType));
		return itm;
	}
	public static Item setSellable(Item itm, boolean canSell)
	{
		itm.setSellable(canSell);
		return itm;
	}
	public static Item setValue(Item itm, int value)
	{
		itm.setValue(value);
		return itm;
	}
	public static Item modStat(Item itm, String stat, int value)
	{
		itm.addStatMod(Stats.valueOf(stat), value);
		return itm;
	}
	public static Item addDamageComp(Item itm, String affinity, float factor)
	{
		itm.addDamageComponent(Stats.valueOf(affinity), factor);
		return itm;
	}
	public static Item swing(Item itm, String swingModel, String swingAnim)
	{
		itm.initSwingData(swingModel, swingAnim);
		return itm;
	}
	public static Item swingBase(Item itm, int frame, int r, int g, int b)
	{
		itm.swingColor(
				false, 
				frame, 
				Color.argb(
						255, 
						r, 
						g, 
						b));
		return itm;		
	}
	public static Item swingSlash(Item itm, int frame, int r, int g, int b)
	{
		itm.swingColor(
				true, 
				frame, 
				Color.argb(
						255, 
						r, 
						g, 
						b));
		return itm;		
	}		
	public static Item add(Item itm, BattleAction action)
	{
		itm.addAction(action);
		return itm;
	}
	public static Item addDep(Item itm, BattleAction action)
	{
		itm.addAction(action.addDependency(itm.getActions().get(itm.getActions().size()-1)));
		return itm;
	}	
	
	public static void publishLibrary(LibraryWriter library) 
	{
		try {
			library.addAllIn(ItemLibrary.class);	
		} catch (Exception e) {
		}
	}

}

