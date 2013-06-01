package bladequest.UI;

import java.util.Map;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import bladequest.UI.ListBox.LBStates;
import bladequest.UI.MenuPanel.Anchors;
import bladequest.world.GameObject;
import bladequest.world.Global;
import bladequest.world.Item;
import bladequest.world.PlayerCharacter;
import bladequest.world.States;

public class DebugScreen 
{
	private Paint menuText, menuTextRight;
	
	private final float menuWidthVpPercent = 28.0f; 
	private final float menuHeightVpPercent = 16.0f;
	
	private final int menuWidth, barHeight;
	
	private states currentState;
	
	private boolean closing, closed;
	
	private ListBox rootMenu, switchList, itemList, goButton;
	private NumberPicker X, Y;
	private String map = "";
	
	public DebugScreen()
	{		
		menuWidth = (int)((float)Global.vpWidth * (menuWidthVpPercent/100.0f));
		barHeight = (int)((float)Global.vpHeight * (menuHeightVpPercent/100.0f));
		
		buildPaints();
		buildPanels();	
		closed = true;
		
	}
	
	private void buildPaints()
	{
		
		menuText = Global.textFactory.getTextPaint(13, Color.WHITE, Align.LEFT);	
		menuTextRight = Global.textFactory.getTextPaint(13, Color.WHITE, Align.RIGHT);
		
		}
	private void buildPanels()
	{
		rootMenu = new ListBox(0, 0, menuWidth, barHeight*4, 5, 1, menuText);
		rootMenu.addItem("Switches", states.Switches, false);
		rootMenu.addItem("Full Heal", states.Characters, false);
		rootMenu.addItem("Items", states.Items, false);
		rootMenu.addItem("Location", states.Location, false);
		rootMenu.addItem("Close", null, false);
		
		switchList = new ListBox(menuWidth, 0, menuWidth*2, Global.vpHeight, 10, 1, menuText);
		itemList = new ListBox(menuWidth, 0, menuWidth*2, Global.vpHeight, 10, 1, menuText);
		
		X = new NumberPicker(Global.vpWidth, 0, menuWidth/2, barHeight);
		Y = new NumberPicker(Global.vpWidth, barHeight, menuWidth/2, barHeight);
		goButton = new ListBox(Global.vpWidth, barHeight*2, menuWidth/2, barHeight, 1, 1, menuText);
		goButton.addItem("Go!", null, false);
		
		X.anchor = Y.anchor = goButton.anchor = Anchors.TopRight;
	}
	
	private void updateSwitchList()
	{
		switchList.clear();
		
		for(Map.Entry<String, Boolean> entry : Global.switches.entrySet())
		{
			ListBoxEntry panel = switchList.addItem(entry.getKey(), entry.getValue(), false);
			panel.addTextBox(""+(Boolean)panel.obj, switchList.getColumnWidth() - 8, switchList.getRowHeight()/2, menuTextRight);
		}
		
		switchList.update();
	}
	
	private void updateItemList()
	{
		itemList.clear();
		
		//add item for gold
		
		ListBoxEntry panel = itemList.addItem("Gold", null, false);
		panel.addTextBox(""+Global.party.getGold(), itemList.getColumnWidth() - 8, itemList.getRowHeight()/2, menuTextRight);
	
		
		for(Item item : Global.items.values())
		{
			panel = itemList.addItem(item.getDisplayName(), item, false);
			panel.addTextBox(""+Global.party.getItemCount(item.idName), itemList.getColumnWidth() - 8, itemList.getRowHeight()/2, menuTextRight);
		}
		
		itemList.update();
	}
	
	private void updateMapList()
	{
		itemList.clear();	
		
		for(String item : Global.maps.keySet())
			itemList.addItem(item, item, false);

		itemList.update();
		
		X.setValue(Global.party.getGridPos().x);
		Y.setValue(Global.party.getGridPos().y);
		
	}
	
	public boolean closed() { return closed; }
	
	public void open()
	{
		closed = false;
		closing = false;
		
		currentState = states.Root;
		Global.map.clearAutoStarters();
		Global.screenFader.clear();
		Global.map.clearObjectAction();
		Global.setPanned(0, 0);
		Global.party.setAllowMovement(true);
	}
	
	private void close()
	{
		closing = true;
	}
	
	private void handleClosing()
	{
		switch(currentState)
		{
		case Root:
			Global.GameState = States.GS_WORLDMOVEMENT;
			Global.delay();
			closing = false;
			closed = true;
			break;
		default:
			changeState(states.Root);
			break;
		}
	}
	private void handleOption(states state)
	{
		if(state == null)
			close();
		else
		changeState(state);

	}

	
	private void changeState(states newState)
	{
		switch(newState)
		{
		case Root:

			break;
		case Characters:
			for(PlayerCharacter c : Global.party.getPartyMembers(true))
				if(c != null)
					c.fullRestore();

		case Switches:
			updateSwitchList();
			break;
		case Items:
			updateItemList();
			break;
		case Location:
			map = "";
			updateMapList();
			break;
		default:
			
			break;

		}
		
		currentState = newState;
		
	}
	
	public void update()
	{
		if(closing)
			handleClosing();
		
		rootMenu.update();
		
		switch(currentState)
		{
		case Switches:
			switchList.update();
			break;
		case Items:
			itemList.update();
			break;
		case Location:
			itemList.update();
			goButton.update();
			X.update();
			Y.update();
			break;
		default:
			break;

		}
		
	}
	
	public void render()
	{
		rootMenu.render();
		
		switch(currentState)
		{		

		case Switches:
			switchList.render();
			break;
		case Items:
			itemList.render();
			break;
		case Location:
			itemList.render();
			goButton.render();
			X.render();
			Y.render();
			break;
		default:
			break;

		}

	}
	
	public void backButtonPressed()
	{
		close();
		
	}
	
	public void touchActionMove(int x, int y)
	{
		
		if(!closing)
		{
			rootMenu.touchActionMove(x, y);
			switch(currentState)
			{
			case Switches:
				switchList.touchActionMove(x, y);
				break;
			case Items:
				itemList.touchActionMove(x, y);
				break;
			case Location:
				itemList.touchActionMove(x, y);
				goButton.touchActionMove(x, y);
				X.touchActionMove(x, y);
				Y.touchActionMove(x, y);
				break;
			default:
				break;

			}
		}
		
		
	}
	
	
	public void touchActionUp(int x, int y)
	{
		if(!closing)
		{
			ListBox.LBStates state;
			
			state = rootMenu.touchActionUp(x, y);
			if(state == LBStates.Selected)
				handleOption((states)rootMenu.getSelectedEntry().obj);
			
			else				
			
			switch(currentState)
			{
			case Switches:
				if(switchList.touchActionUp(x, y) == LBStates.Selected)
				{
					ListBoxEntry entry = switchList.getSelectedEntry();
					if(entry != null)
					{
						Global.switches.put(entry.getTextAt(0).text, !(Boolean)entry.obj);
						entry.obj = !(Boolean)entry.obj;
						entry.getTextAt(1).text = ""+(Boolean)entry.obj;
						
						Global.map.clearObjectAction();
					}
					
				}
				break;
				
			case Items:
				if(itemList.touchActionUp(x, y) == LBStates.Selected)
				{
					ListBoxEntry entry = itemList.getSelectedEntry();
					Item item = (Item)entry.obj;
					
					if(entry != null)
					{
						if(item == null)
						{
							Global.party.addGold(1000);
							entry.getTextAt(1).text = ""+Global.party.getGold();
						}
						else
						{
							Global.party.addItem(item.getId());
							entry.getTextAt(1).text = ""+Global.party.getItemCount(item.idName);
						
						}
					}
					
				}
				break;
				
			case Location:
				if(itemList.touchActionUp(x, y) == LBStates.Selected)
					map = itemList.getSelectedEntry().getTextAt(0).text;
				
				X.touchActionUp(x, y);
				Y.touchActionUp(x, y);				
				
				if(goButton.touchActionUp(x, y) == LBStates.Selected)
				{
					if(map.length() > 0 && !Global.map.Name().equals(map))
						Global.LoadMap(map, false);

					Global.party.teleport(X.getValue(), Y.getValue());
					
					Global.pan(0, 0, 100);
					Global.party.setElevation(0);
					Global.party.setFloating(false, 0, 0);
					Global.party.setAllowMovement(true);
				}
				break;
			default:
				break;

			}
		}
		
		
	}
	public void touchActionDown(int x, int y)
	{
		if(!closing)
		{
			
			rootMenu.touchActionDown(x, y);
			
			switch(currentState)
			{
			case Switches:
				switchList.touchActionDown(x, y);
				break;
			case Items:
				itemList.touchActionDown(x, y);
				break;
			case Location:
				itemList.touchActionDown(x, y);
				goButton.touchActionDown(x, y);
				X.touchActionDown(x, y);
				Y.touchActionDown(x, y);
				break;
			default:
				break;

			}
		}	
		
	}
	
	private enum states
	{
		Root,
		Switches,
		Characters,
		Items,
		Location
		
	}

}
