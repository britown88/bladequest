package bladequest.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Merchant 
{
	//list of items, name, hashmap of quantities for limited-use items
	//discount is applied when opened
	//limited-use quantities should be saved in the save file with merchant name, item name, and qty
	//also need strings for what the merchant says
	
	//default strings
	private static final String defGreeting = "Hello! Welcome! Have a look around!";
	private static final String defBuying = "See something you like?";
	private static final String defSelling = "Have something to sell?";
	private static final String defInsufficentFunds = "You don't have enough for that!";
	private static final String defEquipConfirm = "Do you want to equip it now?";
	private static final String defEquipSellOld = "Do you want to sell the old equipment?";
	private static final String defSell = "This looks interesting.";
	private static final String defFarewell = "Come again!";
	
	private List<Item> items;
	private Map<String, Integer> limitedQtyItems;
	
	private String name;
	public String greeting, buying, selling, sell, insufficientFunds, equipConfirm, equipSellOld, farewell;
	private boolean limQtyChanged;
	
	public Merchant(String name)
	{
		items = new ArrayList<Item>();
		limitedQtyItems = new HashMap<String, Integer>();
		
		greeting = defGreeting;
		buying = defBuying;
		selling = defSelling;
		sell = defSell;
		equipConfirm = defEquipConfirm;
		equipSellOld = defEquipSellOld;
		farewell = defFarewell;
		insufficientFunds = defInsufficentFunds;
		
		limQtyChanged = false;
		
		this.name = name;
	}
	
	public void addItem(Item i)
	{
		if(i != null)
		{
			//ensure item isn't already present
			for(Item it : items)
				if(it.getId() == i.getId())
					return;
			
			items.add(i);
		}
		
	}
	public String getName() { return name;}
	
	//boolean def determines if this is a default value
	public void setLimitedQtyItem(String name, int count, boolean def)
	{
		limitedQtyItems.put(name, count);
		limQtyChanged = !def;
	}
	public void subtractLimQtyItem(String name){setLimitedQtyItem(name, limitedQtyItems.get(name) - 1, false);}
	public int getCount(String name) { return limitedQtyItems.get(name); }
	public boolean itemIsLimited(String name) { return limitedQtyItems.containsKey(name); }
	public boolean limQtyChanged() { return limQtyChanged; }
	public Map<String, Integer> getLimQtyItems() { return limitedQtyItems; }
	
	//returns list of items ignoring limited quantity items where count is 0
	public List<Item> getItems() 
	{ 
		List<Item> newList = new ArrayList<Item>();
		for(Item i : items)
			if(!limitedQtyItems.containsKey(i.idName) || limitedQtyItems.get(i.idName) > 0)
				newList.add(i);
		return newList; 
	}	
	
	

}
