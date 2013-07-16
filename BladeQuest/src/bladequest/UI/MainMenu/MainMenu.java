package bladequest.UI.MainMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.Rect;
import bladequest.UI.DropBox;
import bladequest.UI.ListBox;
import bladequest.UI.ListBox.LBStates;
import bladequest.UI.ListBoxEntry;
import bladequest.UI.MenuPanel;
import bladequest.UI.MenuPanel.Anchors;
import bladequest.UI.MsgBox;
import bladequest.UI.MsgBoxEndAction;
import bladequest.UI.NumberPicker;
import bladequest.combat.DamageMarker;
import bladequest.statuseffects.StatusEffect;
import bladequest.world.Ability;
import bladequest.world.Global;
import bladequest.world.Item;
import bladequest.world.Item.Type;
import bladequest.world.Item.UseTypes;
import bladequest.world.PlayerCharacter;
import bladequest.world.States;
import bladequest.world.Stats;
import bladequest.world.TargetTypes;

public class MainMenu 
{
	private Paint menuText, menuTextCenter, grayMenuText, 
	blueMenuText, redMenuTextCenter,
	blueMenuTextCenter, smallText, smallTextCenter, smallTextRight;
	private final float menuWidthVpPercent = 28.0f; 
	private final float menuHeightVpPercent = 16.0f;
	
	public static int menuWidth, barHeight;
	
	private Vector<DamageMarker> markers;
	private Item itemToUse;
	private Ability abilityToUse;
	private PlayerCharacter selectedChar, nextChar;
	private Item selectedEqpItem;
	
	//root
	private MenuPanel rootPanel, rootBarPanel, rootInfoBar;
	private ListBox rootMenu;
	private DropBox rootCharPlates;
	
	//inventory
	private ListBox invInfoBar, invList, invSort;	
	private boolean close, invShowKeys;
	
	//equip
	private ListBox eqpSelect, eqpInfoBar, eqpEquipSlots;
	private MenuPanel eqpStats, eqpCharPanel;
	private boolean eqpRemove;
	
	//status
	private MenuPanel charStatusGrow, charStatus;
	private ListBox backButton;
	
	//options
	private ListBox opts, optsBackButton;
	private MenuPanel optsColorWindow;
	private NumberPicker npRed, npGreen, npBlue;
	private int optsColorNumber;
		
	//skills
	private ListBox skillsBackButton, skillsList;
	private MenuPanel skillsCharPanel;
	
	//misc
	private MsgBox messageBox;
	private ListBox charUseScreen;	
	private MenuPanel sideBar,charUseInfo, charUseDesc;
	
	private final int sideBarOpenSpeed = 30;
	
	private final int darkenAlphaMax = 200, darkenInc = 5;
	private int darkenAlpha;
	private boolean darkening;
	
	private Item.Type equipItemType;
	
	private MainMenuStateMachine stateMachine; 
	
	private boolean closeUseAfterClear;	
	
	public MainMenu()
	{
		stateMachine = new MainMenuStateMachine();
		//stateMachine.setState(getRootState());
		
		markers= new Vector<DamageMarker>();
		
		menuWidth = (int)((float)Global.vpWidth * (menuWidthVpPercent/100.0f));
		barHeight = (int)((float)Global.vpHeight * (menuHeightVpPercent/100.0f));
		
		messageBox = new MsgBox();
		darkenAlpha = 0;
		darkening = false;
		
		equipItemType = null;
		
		buildPaints();
		
	}	
	
	private MainMenuState getRootState()
	{
		return new MainMenuState()
		{
			@Override
			public void onSwitchedTo(MainMenuState prevState) {
				
				undarken();
				buildCharPlates();
				
				sideBar.pos = new Point(Global.vpWidth, 0);
				sideBar.anchor = Anchors.TopRight;
				
				if(prevState != null)				
					prevState.changeToRoot();
			}
			@Override
			public void update() {
				updateRoot();
			}
			@Override
			public void render() {
				rootCharPlates.render();
				rootBarPanel.render();
				rootMenu.render();	
				
				if(!sideBar.Closed())
				{
					renderDark();
					sideBar.render();
				}
				
				if(!charStatusGrow.Closed())
					charStatusGrow.render();
			}
			@Override
			public void handleClosing()
			{
				if(Global.screenFader.isDone())
				{
					Global.GameState = States.GS_WORLDMOVEMENT;
					Global.delay();
					Global.screenFader.fadeIn(0.5f);
					
					sideBar.setClosed();
					
				}
			}
			@Override
			public void backButtonPressed() {
				sideBar.setClosed();				
				close();
			}
			@Override
			public void touchActionUp(int x, int y) {
				ListBox.LBStates state;
				MenuPanel selectedPanel;
				state = rootMenu.touchActionUp(x, y);
				if(state == LBStates.Selected)
					handleOption((String)rootMenu.getSelectedEntry().obj);
				
				selectedPanel = rootCharPlates.touchActionUp(x, y);				
				//update party order based on charplates order
				for(int i = 0; i < 4; ++i) 
				{
					Global.party.partyMembers[i] = (PlayerCharacter)rootCharPlates.panels[i].obj;
					if(Global.party.partyMembers[i] != null) Global.party.partyMembers[i].setIndex(i);
					
				}
				if(selectedPanel != null)
				{
					selectedChar = (PlayerCharacter)selectedPanel.obj;
					updateCharStatus();		
					stateMachine.setState(getCharStatusState());

				}
				
			}
			@Override
			public void touchActionMove(int x, int y) {
				rootMenu.touchActionMove(x, y);
				rootCharPlates.touchActionMove(x, y);
			}
			@Override
			public void touchActionDown(int x, int y) {
				rootMenu.touchActionDown(x, y);
				rootCharPlates.touchActionDown(x, y);
			}
		};
	}
	private MainMenuState getItemSelectState()
	{
		return new MainMenuState()
		{
			@Override
			public void changeToRoot()
			{
				sideBar.close();
				invShowKeys = false;
			}
			@Override
			public void onSwitchedTo(MainMenuState prevState) {
				if(sideBar.Closed())
				{
					buildInventory();
					sideBar.open();
				}
				undarken();	
				
				if(prevState != null)	
					prevState.changeToItemSelect();
			}
			@Override
			public void update() {
				invInfoBar.update();
				invList.update();
				if(charUseScreen != null && !charUseScreen.Closed())
					charUseScreen.update();
				if(invSort != null && !invSort.Closed())
					invSort.update();
			}
			@Override
			public void render() {
				if(sideBar.Opened())
				{
					invInfoBar.render();
					invList.render();
					
					if(charUseScreen != null && !charUseScreen.Closed())
					{
						renderDark();				
						charUseScreen.render();
					}				
					
					if(invSort != null && !invSort.Closed())
					{
						renderDark();
						invSort.render();
					}
				}
				else
				{
					rootCharPlates.render();
					rootBarPanel.render();
					rootMenu.render();
					renderDark();
					sideBar.render();
				}				
			}
			@Override
			public void handleClosing() {
				if(invSort.Closed() && charUseScreen.Closed())
					stateMachine.setState(getRootState());				
			}

			@Override
			public void backButtonPressed() {
				stateMachine.setState(getRootState());	
			}
			@Override
			public void touchActionUp(int x, int y) {
				ListBox.LBStates state;
				state = invInfoBar.touchActionUp(x, y);
				if(state == LBStates.Selected)
					handleInvOption((String)invInfoBar.getSelectedEntry().obj);				
				
				state = invList.touchActionUp(x, y);
				if(state == LBStates.Selected)
				{
					Item i = (Item)(invList.getSelectedEntry().obj);
					
					if(invList.getSelectedEntry().Disabled())
					{						
						//if(i.getType() == Type.Usable)
						showMessage(i.getDescription(), false);
						
						//add usable by string
						if(!i.isUsable(UseTypes.World))
						{
							List<String> charNames = new ArrayList<String>();
							PlayerCharacter c;
							for(String str : i.getUsableChars())
							{
								c = Global.party.getCharacter(str);
								if(c != null)
									charNames.add(c.getDisplayName());
							}							
							if(charNames.size() > 0)
							{
								String usableByString = "Usable by: \n";
								for(int j = 0; j < charNames.size(); ++j)
								{
									usableByString += charNames.get(j);
									if(j < charNames.size() - 1)
										usableByString += ", ";
								}
								messageBox.addMessage(usableByString);									
							}	
						}
					}
					else
					{
						if(invShowKeys)
							showMessage(i.getDescription(), false);
						else
						{
							itemToUse = i;
							stateMachine.setState(getItemUseState());
						}							
					}
				}
			}
			@Override
			public void touchActionMove(int x, int y) {
				invInfoBar.touchActionMove(x, y);
				invList.touchActionMove(x, y);
			}
			@Override
			public void touchActionDown(int x, int y) {
				invInfoBar.touchActionDown(x, y);
				invList.touchActionDown(x, y);	
			}
		};
	}
	private MainMenuState getItemSortState()
	{
		return new MainMenuState()
		{
			@Override
			public void changeToItemSelect()
			{
				invSort.close();
			}
			@Override
			public void onSwitchedTo(MainMenuState prevState) {
				darken();
				invSort.open();
			}
			@Override
			public void update() {
				invSort.update();
			}
			@Override
			public void render() {
				invInfoBar.render();
				invList.render();
				renderDark();
				invSort.render();
			}
			@Override
			public void handleClosing() {
				stateMachine.setState(getItemSelectState());	
			}
			@Override
			public void backButtonPressed() {
				stateMachine.setState(getItemSelectState());	
			}
			@Override
			public void touchActionUp(int x, int y) {
				ListBox.LBStates state = invSort.touchActionUp(x, y);
				
				switch(state)
				{
				case Close:
					stateMachine.setState(getItemSelectState());	
					break;
				case Selected:
					handleInvSortOption((String)invSort.getSelectedEntry().obj);
					break;
				default:
					break;
				}
			}
			@Override
			public void touchActionMove(int x, int y) {
				invSort.touchActionMove(x, y);
			}
			@Override
			public void touchActionDown(int x, int y) {
				invSort.touchActionDown(x, y);
			}
		};
	}
	private MainMenuState getItemUseState()
	{
		return new MainMenuState()
		{
			@Override
			public void changeStateTo(MainMenuState state){
				markers.clear();
			}
			@Override
			public void changeToItemSelect() {
				charUseScreen.close();
				fillInventory();
				itemToUse = null;
			}
			@Override
			public void onSwitchedTo(MainMenuState prevState) {
				darken();
				buildCharUseScreen();
				updateCharUseScreen();
				charUseScreen.open();
			}
			@Override
			public void update() {
				charUseScreen.update();
				charUseInfo.update();
				charUseDesc.update();
			}
			@Override
			public void render() {
				invInfoBar.render();
				invList.render();
				renderDark();
				charUseScreen.render();
				if(charUseScreen.Opened())
				{
					charUseInfo.render();
					charUseDesc.render();
				}
					
			}
			@Override
			public void handleClosing() {
				stateMachine.setState(getItemSelectState());	
			}
			@Override
			public void backButtonPressed() {
				stateMachine.setState(getItemSelectState());	
			}
			@Override
			public void touchActionUp(int x, int y) {
				ListBox.LBStates state = charUseScreen.touchActionUp(x, y);
				
				switch(state)
				{
				case Close:
					stateMachine.setState(getItemSelectState());	
					break;
				case Selected:
					if(itemToUse.getCount() > 0)
						useItem((PlayerCharacter)charUseScreen.getSelectedEntry().obj);
					
					if(itemToUse.getCount() <= 0)
						closeUseAfterClear = true;
					
					break;
				default:
					break;
				}
			}
			@Override
			public void touchActionMove(int x, int y) {
				charUseScreen.touchActionMove(x, y);
			}
			@Override
			public void touchActionDown(int x, int y) {
				charUseScreen.touchActionDown(x, y);
			}
		};
	}
	private MainMenuState getEquipState()
	{
		return new MainMenuState()
		{

			@Override
			public void changeToRoot() {
				sideBar.close();
				equipItemType = null;
				selectedEqpItem = null;
				eqpRemove = false;
			}
			@Override
			public void onSwitchedTo(MainMenuState prevState) {
				if(sideBar.Closed())
				{
					updateEquipScreen();
					sideBar.open();
				}
				undarken();
			}
			@Override
			public void update() {
				eqpCharPanel.update();
				eqpEquipSlots.update();
				eqpInfoBar.update();
				eqpSelect.update();
				eqpStats.update();
				if(nextChar != null && sideBar.Opened())
				{
					selectedChar = nextChar;
					nextChar = null;
					updateEquipScreen();
				}
			}
			@Override
			public void render() {
				if(sideBar.Opened() || nextChar != null)
				{
					eqpCharPanel.render();
					eqpEquipSlots.render();
					eqpInfoBar.render();				
					if(equipItemType != null)
						renderDark();
					eqpStats.render();
					eqpSelect.render();
					if(!sideBar.Opened())
						sideBar.render();
				}
				else
				{
					rootCharPlates.render();
					rootBarPanel.render();
					rootMenu.render();
					renderDark();
					sideBar.render();
				}
			}
			@Override
			public void handleClosing() {
				stateMachine.setState(getRootState());	
			}
			@Override
			public void onFling(float velocityX, float velocityY) {
				fling(velocityX, velocityY);
			}
			@Override
			public void backButtonPressed() {
				stateMachine.setState(getRootState());	
			}
			@Override
			public void touchActionUp(int x, int y) {
				ListBox.LBStates state;
				selectedEqpItem = null;
				eqpRemove = false;
				
				if(sideBar.Opened())
				{					
					if(equipItemType != null)
					{				
						selectedEqpItem = null;
						updateEquipStats();
						
						state = eqpSelect.touchActionUp(x, y);
						switch(state)
						{
						case Close:
							equipItemType = null;
							fillEqpSelect();
							undarken();
							break;
						case Selected:
							Item i = (Item)eqpSelect.getSelectedEntry().obj;
							if(i == null)
								selectedChar.unequip(equipItemType);
							else
								selectedChar.equip(i.getId());
							
							undarken();
							equipItemType = null;
							updateEquipScreen();
							break;
						default:
							break;
						}
					}
					else
					{
						state = eqpInfoBar.touchActionUp(x, y);				
						if(state == LBStates.Selected)
						{
							handleEqpOption((String)eqpInfoBar.getSelectedEntry().obj);
						}
						else
						{
							state = eqpEquipSlots.touchActionUp(x, y);
							if(state == LBStates.Selected)
							{
								Item.Type type = (Item.Type)eqpEquipSlots.getSelectedEntry().obj;
								
								if(selectedChar.hasTypeEquipped(type)|| hasItemToEquip(selectedChar, type))
								{
									equipItemType = type;
									darken();
									fillEqpSelect();
								}
							}
							
						}
						
						
					}
					
				}
			}
			@Override
			public void touchActionMove(int x, int y) {
				if(sideBar.Opened())
				{					
					if(equipItemType != null)
					{
						eqpSelect.touchActionMove(x, y);
						if(eqpSelect.getCurrentSelectedEntry() != null)
						{
							if(!eqpSelect.isScrolling() && (Item)(eqpSelect.getCurrentSelectedEntry().obj) != selectedEqpItem)
							{
								selectedEqpItem = (Item)eqpSelect.getCurrentSelectedEntry().obj;
								if(selectedEqpItem == null)
									eqpRemove = true;
								updateEquipStats();
							}
						}
						else
						{
							selectedEqpItem = null;
							updateEquipStats();
						}
					}						
					else
					{
						eqpEquipSlots.touchActionMove(x, y);
						eqpInfoBar.touchActionMove(x, y);
					}
				}
			}
			@Override
			public void touchActionDown(int x, int y) {
				if(sideBar.Opened())
				{						
					if(equipItemType != null)
					{
						eqpSelect.touchActionDown(x, y);
						if(eqpSelect.getCurrentSelectedEntry() != null)
						{
							selectedEqpItem = (Item)eqpSelect.getCurrentSelectedEntry().obj;
							if(selectedEqpItem == null)
								eqpRemove = true;
							updateEquipStats();
						}
						else
						{
							selectedEqpItem = null;
							updateEquipStats();
						}							
					}							
					else
					{
						eqpEquipSlots.touchActionDown(x, y);
						eqpInfoBar.touchActionDown(x, y);
					}
				}					
			}
		};
	}
	private MainMenuState getEquipSelectCharState()
	{
		return new MainMenuState()
		{
			@Override
			public void onSwitchedTo(MainMenuState prevState) {
				darken();
			}
			@Override
			public void update() {
				rootInfoBar.update();
				updateRoot();
			}
			@Override
			public void render() {
				rootBarPanel.render();
				rootMenu.render();			
				renderDark();			
				rootCharPlates.render();
				rootInfoBar.render();
				
				if(!sideBar.Closed())
					sideBar.render();
			}
			@Override
			public void handleClosing() {
				stateMachine.setState(getRootState());	
			}
			@Override
			public void backButtonPressed() {
				stateMachine.setState(getRootState());	
			}
			@Override
			public void touchActionUp(int x, int y) {
				MenuPanel selectedPanel = rootCharPlates.touchActionUp(x, y);	
				//update party order based on charplates order
				for(int i = 0; i < 4; ++i) 
				{
					Global.party.partyMembers[i] = (PlayerCharacter)rootCharPlates.panels[i].obj;
					if(Global.party.partyMembers[i] != null) Global.party.partyMembers[i].setIndex(i);
					
				}
				
				//open equip screen with selected character
				if(selectedPanel != null)
				{
					selectedChar = (PlayerCharacter)selectedPanel.obj;
					stateMachine.setState(getEquipState());	
				}
			}
			@Override
			public void touchActionMove(int x, int y) {
				rootCharPlates.touchActionMove(x, y);
			}
			@Override
			public void touchActionDown(int x, int y) {
				rootCharPlates.touchActionDown(x, y);
			}
		};
	}
	private MainMenuState getSkillSelectState()
	{
		return new MainMenuState()
		{
			@Override
			public void onSwitchedTo(MainMenuState prevState) {
				if(sideBar.Closed())
				{
					updateSkillScreen();
					sideBar.open();
				}
				undarken();
				
				prevState.changeToSkillSelect();
			}
			@Override
			public void changeToRoot() {
				sideBar.close();
			}
			@Override
			public void update() {
				if(nextChar != null && sideBar.Opened())
				{
					selectedChar = nextChar;
					nextChar = null;
					updateSkillScreen();
					
				}
				
				skillsBackButton.update();
				skillsCharPanel.update();
				skillsList.update();
				
				if(charUseScreen != null && !charUseScreen.Closed())
					charUseScreen.update();
			}
			@Override
			public void render() {
				if(sideBar.Opened() || nextChar != null)
				{
					
					skillsCharPanel.render();
					skillsList.render();
					skillsBackButton.render();
					
					if(charUseScreen != null && !charUseScreen.Closed())
					{
						renderDark();				
						charUseScreen.render();
					}

					if(!sideBar.Opened())
						sideBar.render();
					
				}
				else
				{
					rootCharPlates.render();
					rootBarPanel.render();
					rootMenu.render();
					renderDark();
					sideBar.render();
				}
			}
			@Override
			public void handleClosing() {
				stateMachine.setState(getRootState());	
			}
			@Override
			public void onFling(float velocityX, float velocityY) {
				fling(velocityX, velocityY);
			}
			@Override
			public void backButtonPressed() {
				stateMachine.setState(getRootState());	
			}
			@Override
			public void touchActionUp(int x, int y) {
				if(skillsBackButton.touchActionUp(x, y) == LBStates.Selected)
					stateMachine.setState(getRootState());	
				else
				{
					if(skillsList.touchActionUp(x, y) == LBStates.Selected)
					{
						Ability ab = (Ability)skillsList.getSelectedEntry().obj;
						if(ab.isEnabled() && ab.isUsableOutOfBattle())
						{
							if(!skillsList.getSelectedEntry().Disabled())
							{
								abilityToUse = ab;
								stateMachine.setState(getSkillUseState());
							}							
						}
						else if(ab.getDescription().length() > 0)
							showMessage(ab.getDescription(), false);
					}
					
				}
					
			}
			@Override
			public void touchActionMove(int x, int y) {
				skillsBackButton.touchActionMove(x, y);
				skillsList.touchActionMove(x, y);
			}
			@Override
			public void touchActionDown(int x, int y) {
				skillsBackButton.touchActionDown(x, y);
				skillsList.touchActionDown(x, y);
			}
		};
	}
	private MainMenuState getSkillUseState()
	{
		{
			return new MainMenuState()
			{
				@Override
				public void changeStateTo(MainMenuState state){
					markers.clear();
				}
				@Override
				public void changeToSkillSelect() {
					charUseScreen.close();
					updateSkillScreen();
					abilityToUse = null;
				}
				@Override
				public void onSwitchedTo(MainMenuState prevState) {
					darken();
					buildCharUseScreen();
					updateCharUseScreen();
					charUseScreen.open();
				}
				@Override
				public void update() {
					charUseScreen.update();
					charUseInfo.update();
					charUseDesc.update();
				}
				@Override
				public void render() {
					skillsCharPanel.render();
					skillsList.render();
					skillsBackButton.render();
					renderDark();
					charUseScreen.render();
					if(charUseScreen.Opened())
					{
						charUseInfo.render();
						charUseDesc.render();
					}
						
				}
				@Override
				public void handleClosing() {
					stateMachine.setState(getSkillSelectState());	
				}
				@Override
				public void backButtonPressed() {
					stateMachine.setState(getSkillSelectState());	
				}
				@Override
				public void touchActionUp(int x, int y) {
					ListBox.LBStates state = charUseScreen.touchActionUp(x, y);
					
					switch(state)
					{
					case Close:
						stateMachine.setState(getSkillSelectState());	
						break;
					case Selected:
						if(abilityToUse.MPCost() <= selectedChar.getMP() && abilityToUse.isEnabled())
							useAbility((PlayerCharacter)charUseScreen.getSelectedEntry().obj);
						
						if(abilityToUse.MPCost() > selectedChar.getMP() || !abilityToUse.isEnabled())
							closeUseAfterClear = true;
						
						break;
					default:
						break;
					}
				}
				@Override
				public void touchActionMove(int x, int y) {
					charUseScreen.touchActionMove(x, y);
				}
				@Override
				public void touchActionDown(int x, int y) {
					charUseScreen.touchActionDown(x, y);
				}
			};
		}
	}
	private MainMenuState getSkillSelectCharState()
	{
		return new MainMenuState()
		{
			@Override
			public void onSwitchedTo(MainMenuState prevState) {
				darken();
			}
			@Override
			public void update() {
				rootInfoBar.update();
				updateRoot();
			}
			@Override
			public void render() {
				rootBarPanel.render();
				rootMenu.render();			
				renderDark();			
				rootCharPlates.render();
				rootInfoBar.render();
				
				if(!sideBar.Closed())
					sideBar.render();
			}
			@Override
			public void handleClosing() {
				stateMachine.setState(getRootState());	
			}
			@Override
			public void backButtonPressed() {
				stateMachine.setState(getRootState());	
			}
			@Override
			public void touchActionUp(int x, int y) {
				MenuPanel selectedPanel = rootCharPlates.touchActionUp(x, y);	
				//update party order based on charplates order
				for(int i = 0; i < 4; ++i) 
				{
					Global.party.partyMembers[i] = (PlayerCharacter)rootCharPlates.panels[i].obj;
					if(Global.party.partyMembers[i] != null) Global.party.partyMembers[i].setIndex(i);
					
				}
				
				//open equip screen with selected character
				if(selectedPanel != null)
				{
					selectedChar = (PlayerCharacter)selectedPanel.obj;
					stateMachine.setState(getSkillSelectState());	
				}
			}
			@Override
			public void touchActionMove(int x, int y) {
				rootCharPlates.touchActionMove(x, y);
			}
			@Override
			public void touchActionDown(int x, int y) {
				rootCharPlates.touchActionDown(x, y);
			}
		};		
	}
	private MainMenuState getCharStatusState()
	{
		return new MainMenuState()
		{
			@Override
			public void changeToRoot() {
				charStatusGrow.close();
				sideBar.setClosed();
			}
			@Override
			public void onSwitchedTo(MainMenuState prevState) {
				charStatusGrow.open();
			}
			@Override
			public void update() {
				charStatusGrow.update();
				charStatus.update();
				backButton.update();
				if(nextChar != null && sideBar.Opened())
				{
					selectedChar = nextChar;
					nextChar = null;
					updateCharStatus();
				}
			}
			@Override
			public void render() {
				if(charStatusGrow.Opened() || nextChar != null)
				{
					charStatus.render();
					
					backButton.render();
					
					if(!sideBar.Opened())
						sideBar.render();
				}				
				else
				{
					rootCharPlates.render();
					rootBarPanel.render();
					rootMenu.render();
					charStatusGrow.render();
				}
				
			}
			@Override
			public void handleClosing() {
				stateMachine.setState(getRootState());	
			}
			@Override
			public void onFling(float velocityX, float velocityY) {
				fling(velocityX, velocityY);
			}
			@Override
			public void backButtonPressed() {
				stateMachine.setState(getRootState());	
			}
			@Override
			public void touchActionUp(int x, int y) {
				ListBox.LBStates state = backButton.touchActionUp(x, y);
				if(state == LBStates.Selected)
					stateMachine.setState(getRootState());	

			}
			@Override
			public void touchActionMove(int x, int y) {
				backButton.touchActionMove(x, y);
			}
			@Override
			public void touchActionDown(int x, int y) {
				backButton.touchActionDown(x, y);
			}
		};
	}
	private MainMenuState getOptionsState()
	{
		return new MainMenuState()
		{
			@Override
			public void changeToRoot() {
				sideBar.close();
			}
			@Override
			public void onSwitchedTo(MainMenuState prevState) {
				if(sideBar.Closed())				
					sideBar.open();

				undarken();			
				updateOptionsScreen();
				
				if(prevState != null)	
					prevState.changeToOptions();
			}
			@Override
			public void update() {
				opts.update();
				if(optsColorWindow != null && !optsColorWindow.Closed())
					optsColorWindow.update();
			}
			@Override
			public void render() {
				if(sideBar.Opened())
				{
					opts.render();
					if(optsColorWindow != null && !optsColorWindow.Closed())
					{
						renderDark();
						optsColorWindow.render();
					}	
				}
				else
				{
					rootCharPlates.render();
					rootBarPanel.render();
					rootMenu.render();
					renderDark();
					sideBar.render();
				}
			}
			@Override
			public void handleClosing() {
				stateMachine.setState(getRootState());	
			}
			@Override
			public void backButtonPressed() {
				stateMachine.setState(getRootState());	
			}
			@Override
			public void touchActionUp(int x, int y) {
				if(opts.touchActionUp(x, y) == LBStates.Selected)
					handleOptOption((String)opts.getSelectedEntry().obj);
			}
			@Override
			public void touchActionMove(int x, int y) {
				opts.touchActionMove(x, y);
			}
			@Override
			public void touchActionDown(int x, int y) {
				opts.touchActionDown(x, y);
			}
		};
	}
	private MainMenuState getOptionFrameColorState()
	{
		return new MainMenuState()
		{
			@Override
			public void changeToOptions() {
				optsColorWindow.close();
				
				//stop from changing colors to too bright				
				int sum = Global.fc1r+Global.fc1g+Global.fc1b+
							Global.fc2r+Global.fc2g+Global.fc2b;
				sum /= 6;				
				if(sum >= 240)
				{
					Global.fc1b = Global.fc1g = Global.fc1r = 
						Global.fc2b = Global.fc2g = Global.fc2r = 0;
					updateOptionsScreen();
					showMessage("You made the menus too bright! That was silly of you...", false);
					showMessage("We at Dapper Hat kindly recommend that you choose a darker setting.", false);
				}
			}
			@Override
			public void onSwitchedTo(MainMenuState prevState) {
				darken();
				optsColorWindow.open();
				
				if(optsColorNumber == 1)
				{
					npRed.setValue(Global.fc1r);
					npGreen.setValue(Global.fc1g);
					npBlue.setValue(Global.fc1b);
				}
				else
				{
					npRed.setValue(Global.fc2r);
					npGreen.setValue(Global.fc2g);
					npBlue.setValue(Global.fc2b);
				}
			}
			@Override
			public void update() {
				optsColorWindow.update();
				npRed.update();
				npGreen.update();
				npBlue.update();
				optsBackButton.update();	
				
				if(optsColorNumber == 1)
				{
					Global.fc1r = npRed.getValue();
					Global.fc1g = npGreen.getValue();
					Global.fc1b = npBlue.getValue();
				}
				else
				{
					Global.fc2r = npRed.getValue();
					Global.fc2g = npGreen.getValue();
					Global.fc2b = npBlue.getValue();
				}
			}
			@Override
			public void render() {
				opts.render();
				renderDark();
				optsColorWindow.render();
				if(optsColorWindow.Opened())
				{
					npRed.render();
					npGreen.render();
					npBlue.render();
					optsBackButton.render();
				}
			}
			@Override
			public void handleClosing() {
				stateMachine.setState(getOptionsState());	
			}
			@Override
			public void backButtonPressed() {
				stateMachine.setState(getOptionsState());	
			}
			@Override
			public void touchActionUp(int x, int y) {
				npRed.touchActionUp(x, y);
				npGreen.touchActionUp(x, y);
				npBlue.touchActionUp(x, y);
				if(optsBackButton.touchActionUp(x, y) == LBStates.Selected)
					stateMachine.setState(getOptionsState());	
			}
			@Override
			public void touchActionMove(int x, int y) {
				npRed.touchActionMove(x, y);
				npGreen.touchActionMove(x, y);
				npBlue.touchActionMove(x, y);
				optsBackButton.touchActionMove(x, y);
			}
			@Override
			public void touchActionDown(int x, int y) {
				npRed.touchActionDown(x, y);
				npGreen.touchActionDown(x, y);
				npBlue.touchActionDown(x, y);
				optsBackButton.touchActionDown(x, y);
			}
		};
	}
	
	private void buildPaints()
	{		
		smallText = Global.textFactory.getTextPaint(9, Color.WHITE, Align.LEFT);				
		smallTextCenter = Global.textFactory.getTextPaint(9, Color.WHITE, Align.CENTER);		
		smallTextRight = Global.textFactory.getTextPaint(9, Color.WHITE, Align.RIGHT);	
		
		menuText = Global.textFactory.getTextPaint(13, Color.WHITE, Align.LEFT);	
		menuTextCenter = Global.textFactory.getTextPaint(13, Color.WHITE, Align.CENTER);	
		
		grayMenuText = Global.textFactory.getTextPaint(13, Color.GRAY, Align.LEFT);
		blueMenuText = Global.textFactory.getTextPaint(13, Color.CYAN, Align.LEFT);
		redMenuTextCenter = Global.textFactory.getTextPaint(13, Color.RED, Align.CENTER);
		blueMenuTextCenter = Global.textFactory.getTextPaint(13, Color.CYAN, Align.CENTER);
		
	}
	private static Paint buildPaint(int size, int c, Align align)
	{
		return Global.textFactory.getTextPaint(size, c, align);
	}
	private void buildRoot()
	{
		
		rootPanel = new MenuPanel(0, 0, Global.vpWidth, Global.vpHeight); rootPanel.update();
		rootBarPanel = new MenuPanel(0,Global.vpHeight - barHeight,Global.vpWidth - menuWidth, barHeight); rootBarPanel.update();
		
		//create root info bar
		rootBarPanel.addTextBox(Global.map.displayName(), 15, barHeight/2, menuText);
		rootBarPanel.addTextBox(Global.party.getGold()+"G", rootBarPanel.width - (int)smallText.measureText(Global.party.getGold()+"G") - 10, (int)(barHeight*0.33f), smallText);
		rootBarPanel.addTextBox("", 0, (int)(barHeight*0.66f), smallText);
		
		rootInfoBar = new MenuPanel(0,Global.vpHeight - barHeight,Global.vpWidth - menuWidth, barHeight); rootInfoBar.update();
		rootInfoBar.addTextBox("Select a Character", 15, barHeight/2, menuText);
		
		sideBar = new MenuPanel(Global.vpWidth, 0, 0, Global.vpHeight);
		sideBar.setOpenSize(Global.vpWidth, Global.vpHeight);
		sideBar.anchor = Anchors.TopRight;				
		sideBar.openSpeed = sideBarOpenSpeed;
		
		//create root menu
		rootMenu = new ListBox(new Rect(Global.vpWidth - menuWidth, 0, Global.vpWidth, Global.vpHeight), 7, 1, menuText);
		rootMenu.setDisabledPaint(grayMenuText);
		rootMenu.addItem("Items", "itm", false);
		rootMenu.addItem("Equip", "eqp", false);
		rootMenu.addItem("Skills", "skl", false);
		rootMenu.addItem("Party", "par", true);
		rootMenu.addItem("Options", "opt", false);
		rootMenu.addItem("Quit Game", "sav", false);
		rootMenu.addItem("Close", "clo", false);
		
		rootMenu.thickOptSelect = true;
		
		rootCharPlates = new DropBox(0, 0, Global.vpWidth - menuWidth, Global.vpHeight - barHeight, 4, 1);	
		buildCharPlates();
		buildCharStatus();
		buildCharUseScreen();
		buildEquipScreen();
		buildSkillScreen();
		buildOptionsScreen();
		
	}
	private void buildCharPlates()
	{		
		//build character plates
		PlayerCharacter[] chars = Global.party.getPartyMembers(false);
		
		for(int i = 0; i < 4; ++i)
		{
			rootCharPlates.panels[i].clear();
			rootCharPlates.panels[i].show();
			
			if(chars[i] == null  || chars[i].getEscaped())
			{
				rootCharPlates.panels[i].hide();
				rootCharPlates.panels[i].obj = null;
			}				
			else
			{
				Rect src = chars[i].getPortraitSrcRect();
				Rect dest = new Rect(0, 0, rootCharPlates.RowHeight(), rootCharPlates.RowHeight());
				dest.inset(3, 3);
				rootCharPlates.panels[i].addPicBox(Global.bitmaps.get("portraits"), src, dest);
				
				//charplate text boxes
				rootCharPlates.panels[i].addTextBox(chars[i].getDisplayName(), rootCharPlates.RowHeight() + 15, (int)(rootCharPlates.RowHeight()*0.25f), menuText);
				rootCharPlates.panels[i].addTextBox("lv"+chars[i].getLevel(), rootCharPlates.panels[i].width - (int)smallText.measureText("lv"+chars[i].getLevel()) - 10, (int)(rootCharPlates.RowHeight()*0.20f), smallText);
				rootCharPlates.panels[i].addTextBox("HP:"+chars[i].getHP()+"/"+chars[i].getStat(Stats.MaxHP), rootCharPlates.RowHeight()*2 + 15, (int)(rootCharPlates.RowHeight()*0.50f), smallText);
				rootCharPlates.panels[i].addTextBox("MP:"+chars[i].getMP()+"/"+chars[i].getStat(Stats.MaxMP), rootCharPlates.RowHeight()*3 + 15, (int)(rootCharPlates.RowHeight()*0.75f), smallText);
				
				//status effect icons
				float iconScale = 1.5f;
				int j = 0, d = (int)(Global.iconSize*iconScale + 2);
				for(StatusEffect se : chars[i].getStatusEffects())
					if(j < 9 && se.icon().length() > 0)
						rootCharPlates.panels[i].addPicBox(
							Global.createIcon(se.icon(), 
									rootCharPlates.RowHeight()+ d +  d*j++, 
									rootCharPlates.RowHeight() - d,
									iconScale));
				
				if(chars[i].isDead())
					rootCharPlates.panels[i].darken = true;				
				
				rootCharPlates.panels[i].obj = chars[i];
			}

		}
	}
	private void buildInventory()
	{		
		invInfoBar = new ListBox(new Rect(0, 0, Global.vpWidth, barHeight), 1, 3, menuTextCenter);
		invInfoBar.addItem("Sort", "srt", false);
		invInfoBar.addItem("Key Items", "key", false);
		invInfoBar.addItem("Back", "bak", false);		
		
		invList = new ListBox(new Rect(0, barHeight, Global.vpWidth, Global.vpHeight), 8, 2, menuText);
		invList.setDisabledPaint(grayMenuText);		
		
		invSort = new ListBox(Global.vpWidth/2, Global.vpHeight/2, 0, 0, 4, 1, menuTextCenter);
		invSort.setOpenSize(menuWidth, barHeight * 4);
		invSort.openSpeed = sideBarOpenSpeed;
		invSort.addItem("Auto", "aut", false);
		invSort.addItem("ABC", "abc", false);
		invSort.addItem("Type", "typ", false);
		invSort.addItem("Usable", "usf", false);		
		invSort.anchor = Anchors.TrueCenter;		
		
		fillInventory();		
		
	}
	private void fillInventory()
	{		
		ListBoxEntry entry;
		boolean disabled;
		
		invList.clearObjects();
		
		if(invShowKeys)
		{
			for(Item i : Global.party.getInventory())
				if(i.getType() == Type.Key)
					invList.addItem(i.getDisplayName(), i, false);
		}
		else
		{
			float iconScale = 2.0f;
			int d = (int)((float)Global.iconSize*iconScale/2.0f);
			for(Item i : Global.party.getInventory())
				if(i.getType() != Type.Key)
				{
					disabled = !i.isUsable(UseTypes.World);
					
					entry = invList.addItem(i.getDisplayName(), i, disabled);
					
					entry.getTextAt(0).x += d*2 + 4;					
					//add item count
					entry.addTextBox(""+i.getCount(), invList.getColumnWidth() - 32, invList.getRowHeight()/2, disabled ? grayMenuText : menuText);
					entry.addPicBox(Global.createIcon(i.getIcon(), d + 6, invList.getRowHeight()/2, iconScale));
				
				}
					
		}
		
		
		invList.update();
		
		
	}
	private void buildCharUseScreen()
	{
		List<PlayerCharacter> chars = Global.party.getPartyList(false);
		
		int width = (int)((Global.vpWidth*0.85f)/4.0f);
		
		charUseScreen = new ListBox(Global.vpWidth/2, Global.vpHeight/2, 0, 0, 1, chars.size(), menuTextCenter);
		charUseScreen.setOpenSize(width*chars.size(), (int)(menuWidth*1.25f));
		charUseScreen.openSpeed = sideBarOpenSpeed;
		charUseScreen.anchor = Anchors.TrueCenter;
		
		charUseInfo = new MenuPanel((Global.vpWidth - Math.max(width*chars.size(), (int)(width*1.25f)))/2, Global.vpHeight/2 - (int)(menuWidth*1.25f)/2, (int)(width*1.25f), barHeight);
		charUseInfo.setInset(6, 6);
		charUseInfo.anchor = Anchors.LeftCenter;
		charUseInfo.addTextBox("", 6, (barHeight-12)/2, menuText);	
		
		charUseDesc = new MenuPanel(0, Global.vpHeight, Global.vpWidth, barHeight);
		charUseDesc.setInset(6, 6);
		charUseDesc.anchor = Anchors.BottomLeft;
		charUseDesc.addTextBox("", 6, (barHeight-12)/2, menuText);	
		
		updateCharUseScreen();
	}
	private void updateCharUseScreen()
	{
		List<PlayerCharacter> chars = Global.party.getPartyList(false);
		charUseScreen.clear();
		//charUseScreen.updateFrame();
		int width = charUseScreen.getColumnWidth();
		
		//update infobox
		if(itemToUse != null)
		{
			charUseInfo.getTextAt(0).text = "Count:" + itemToUse.getCount();
			charUseDesc.getTextAt(0).text = itemToUse.getShortDescription();
		}
		if(abilityToUse != null)
		{
			charUseInfo.getTextAt(0).text = "Cost:" + abilityToUse.MPCost();
			charUseDesc.getTextAt(0).text = abilityToUse.getShortDescription();
		}
		
		//update frames		
		ListBoxEntry lbi;
		for(PlayerCharacter c : chars)
		{
			lbi = charUseScreen.addItem(c.getDisplayName(), c, false);
			charUseScreen.update();
			lbi.clear();
			
			Rect src = c.getPortraitSrcRect();
			Rect dest = new Rect(12, 12, width-12, width-12);
			lbi.addPicBox(Global.bitmaps.get("portraits"), src, dest);
			
			lbi.addTextBox(c.getDisplayName(), width/2, width-6, menuTextCenter);
			
			lbi.addTextBox("" + c.getHP() + "/" + c.getStat(Stats.MaxHP), width/2, (int)(charUseScreen.getRowHeight() - (charUseScreen.getRowHeight()-width - menuTextCenter.getTextSize()/2)*0.75f), smallTextCenter);
			lbi.addTextBox("" + c.getMP() + "/" + c.getStat(Stats.MaxMP), width/2, (int)(charUseScreen.getRowHeight() - (charUseScreen.getRowHeight()-width - menuTextCenter.getTextSize()/2)*0.50f), smallTextCenter);
				
			lbi.update();
			c.setPosition(lbi.getPos().x + dest.left, lbi.getPos().y + dest.top );
			
			List<StatusEffect> seList = c.getStatusEffects();
			if(seList.size() > 0)
			{
				float iconScale = 1.5f;
				int j = 0, d = (int)(Global.iconSize*iconScale + 2);				
				
				int seCount = 0;				
				for(StatusEffect se : seList)
					if(se.icon().length() > 0)
						seCount++;
				
				int seWidth = d*Math.min(6, seCount);
				
				for(StatusEffect se : seList)
					if(j < 6 && se.icon().length() > 0)
						lbi.addPicBox(
							Global.createIcon(se.icon(), 
									(int)((width - seWidth)/2.0f) + d/2 + d*j++, 
									(int)(charUseScreen.getRowHeight() - (charUseScreen.getRowHeight()-width - menuTextCenter.getTextSize()/2)*0.25f),
									iconScale));
			}
		
		}	
		
	}
	private void buildEquipScreen()
	{
		eqpSelect = new ListBox(new Rect(0, barHeight + (Global.vpHeight - barHeight)/2, (int)(Global.vpWidth*0.61f), Global.vpHeight),4, 1, menuText);
		eqpSelect.setDisabledPaint(grayMenuText);	
		
		eqpEquipSlots = new ListBox(new Rect((int)(Global.vpWidth*0.20f), barHeight, (int)(Global.vpWidth*0.61f), barHeight + (Global.vpHeight - barHeight)/2), 5, 1, menuText);
		eqpEquipSlots.setDisabledPaint(grayMenuText);		

					
		
		eqpInfoBar = new ListBox(new Rect(0, 0, Global.vpWidth, barHeight), 1, 3, menuTextCenter);
		eqpInfoBar.addItem("Remove All", "rmv", false);
		eqpInfoBar.addItem("Optimum", "opt", false);
		eqpInfoBar.addItem("Back", "bak", false);		
		
		eqpStats = new MenuPanel(
				(int)(Global.vpWidth * 0.61f), 
				barHeight, 
				Global.vpWidth - (int)(Global.vpWidth * 0.61f), 
				Global.vpHeight - barHeight);		
		
		eqpCharPanel = new MenuPanel(
				0, 
				barHeight, 
				(int)(Global.vpWidth * 0.20f), 
				(Global.vpHeight - barHeight)/2);
	}
	private void updateEquipScreen()
	{
		//charpanel, pic and name
		eqpCharPanel.clear();
		Rect src = selectedChar.getPortraitSrcRect();
		Rect dest = new Rect(0, 0, eqpCharPanel.width, eqpCharPanel.width);
		dest.inset(12, 12);
		eqpCharPanel.addPicBox(Global.bitmaps.get("portraits"), src, dest);
		eqpCharPanel.addTextBox(selectedChar.getDisplayName(), eqpCharPanel.width/2, eqpCharPanel.height - (eqpCharPanel.height - eqpCharPanel.width)/2 - 12, menuTextCenter);

		updateEquipStats();
		updateEquipmentSlots();
		fillEqpSelect();
	
	}	
	private void updateEquipStats()
	{
		float percentOfHeight = 1.0f / 13.0f;
		int i = 1;
		
		eqpStats.clear();
		
		float iconScale = 1.00f;	
		int column1x = 6;
		int column2x = (int)(eqpStats.width*0.54f);
		int column3x = (int)(eqpStats.width*0.85f);
		int arrowx = (int)(eqpStats.width*0.69f);
		
		selectedChar.updateSecondaryStats();
		
		//create labels and base stats
		eqpStats.addTextBox("POW", column1x, (int)(eqpStats.height*(percentOfHeight*i)), menuText);
		eqpStats.addTextBox(""+selectedChar.getStat(Stats.BattlePower), column2x, (int)(eqpStats.height*(percentOfHeight*i)), selectedChar.getStatMod(Stats.BattlePower) > 0 ? blueMenuTextCenter : selectedChar.getStatMod(Stats.BattlePower) < 0 ? redMenuTextCenter : menuTextCenter);
		++i;		
		eqpStats.addTextBox("DEF", column1x, (int)(eqpStats.height*(percentOfHeight*i)), menuText);
		eqpStats.addTextBox(""+selectedChar.getStat(Stats.Defense), column2x, (int)(eqpStats.height*(percentOfHeight*i)), selectedChar.getStatMod(Stats.Defense) > 0 ? blueMenuTextCenter : selectedChar.getStatMod(Stats.Defense) < 0 ? redMenuTextCenter : menuTextCenter);
		++i;		
		eqpStats.addTextBox("M.POW", column1x, (int)(eqpStats.height*(percentOfHeight*i)), menuText);
		eqpStats.addTextBox(""+selectedChar.getStat(Stats.MagicPower), column2x, (int)(eqpStats.height*(percentOfHeight*i)), selectedChar.getStatMod(Stats.MagicPower) > 0 ? blueMenuTextCenter : selectedChar.getStatMod(Stats.MagicPower) < 0 ? redMenuTextCenter : menuTextCenter);
		++i;		
		eqpStats.addTextBox("M.DEF", column1x, (int)(eqpStats.height*(percentOfHeight*i)), menuText);
		eqpStats.addTextBox(""+selectedChar.getStat(Stats.MagicDefense), column2x, (int)(eqpStats.height*(percentOfHeight*i)), selectedChar.getStatMod(Stats.MagicDefense) > 0 ? blueMenuTextCenter : selectedChar.getStatMod(Stats.MagicDefense) < 0 ? redMenuTextCenter : menuTextCenter);
		++i;		
		eqpStats.addTextBox("STR", column1x, (int)(eqpStats.height*(percentOfHeight*i)), menuText);
		eqpStats.addTextBox(""+selectedChar.getStat(Stats.Strength), column2x, (int)(eqpStats.height*(percentOfHeight*i)), selectedChar.getStatMod(Stats.Strength) > 0 ? blueMenuTextCenter : selectedChar.getStatMod(Stats.Strength) < 0 ? redMenuTextCenter : menuTextCenter);
		++i;		
		eqpStats.addTextBox("AGI", column1x, (int)(eqpStats.height*(percentOfHeight*i)), menuText);
		eqpStats.addTextBox(""+selectedChar.getStat(Stats.Agility), column2x, (int)(eqpStats.height*(percentOfHeight*i)), selectedChar.getStatMod(Stats.Agility) > 0 ? blueMenuTextCenter : selectedChar.getStatMod(Stats.Agility) < 0 ? redMenuTextCenter : menuTextCenter);
		++i;		
		eqpStats.addTextBox("VIT", column1x, (int)(eqpStats.height*(percentOfHeight*i)), menuText);
		eqpStats.addTextBox(""+selectedChar.getStat(Stats.Vitality), column2x, (int)(eqpStats.height*(percentOfHeight*i)), selectedChar.getStatMod(Stats.Vitality) > 0 ? blueMenuTextCenter : selectedChar.getStatMod(Stats.Vitality) < 0 ? redMenuTextCenter : menuTextCenter);
		++i;		
		eqpStats.addTextBox("INT", column1x, (int)(eqpStats.height*(percentOfHeight*i)), menuText);
		eqpStats.addTextBox(""+selectedChar.getStat(Stats.Intelligence), column2x, (int)(eqpStats.height*(percentOfHeight*i)), selectedChar.getStatMod(Stats.Intelligence) > 0 ? blueMenuTextCenter : selectedChar.getStatMod(Stats.Intelligence) < 0 ? redMenuTextCenter : menuTextCenter);
		++i;		
		eqpStats.addTextBox("SPD", column1x, (int)(eqpStats.height*(percentOfHeight*i)), menuText);
		eqpStats.addTextBox(""+selectedChar.getStat(Stats.Speed), column2x, (int)(eqpStats.height*(percentOfHeight*i)), selectedChar.getStatMod(Stats.Speed) > 0 ? blueMenuTextCenter : selectedChar.getStatMod(Stats.Speed) < 0 ? redMenuTextCenter : menuTextCenter);
		++i;		
		eqpStats.addTextBox("EV", column1x, (int)(eqpStats.height*(percentOfHeight*i)), menuText);
		eqpStats.addTextBox(""+selectedChar.getStat(Stats.Evade), column2x, (int)(eqpStats.height*(percentOfHeight*i)), selectedChar.getStatMod(Stats.Evade) > 0 ? blueMenuTextCenter : selectedChar.getStatMod(Stats.Evade) < 0 ? redMenuTextCenter : menuTextCenter);
		++i;		
		eqpStats.addTextBox("BLK", column1x, (int)(eqpStats.height*(percentOfHeight*i)), menuText);
		eqpStats.addTextBox(""+selectedChar.getStat(Stats.Block), column2x, (int)(eqpStats.height*(percentOfHeight*i)), selectedChar.getStatMod(Stats.Block) > 0 ? blueMenuTextCenter : selectedChar.getStatMod(Stats.Block) < 0 ? redMenuTextCenter : menuTextCenter);
		++i;		
		eqpStats.addTextBox("CRIT", column1x, (int)(eqpStats.height*(percentOfHeight*i)), menuText);
		eqpStats.addTextBox(""+selectedChar.getStat(Stats.Crit), column2x, (int)(eqpStats.height*(percentOfHeight*i)), selectedChar.getStatMod(Stats.Crit) > 0 ? blueMenuTextCenter : selectedChar.getStatMod(Stats.Crit) < 0 ? redMenuTextCenter : menuTextCenter);
		
		
		if(selectedEqpItem != null || eqpRemove)
		{
			//create arrows
			for(int j = 1; j < 13; ++j)
				eqpStats.addPicBox(Global.createIcon("arrow", arrowx, (int)(eqpStats.height*(percentOfHeight*j)), iconScale));
			
			//get new stats
			int[] newStats = selectedEqpItem != null ? 
					selectedChar.getModdedStats(selectedEqpItem) :
					selectedChar.getModdedStatsRmv(equipItemType);
			
			//create updated stats
			i = 1;
			eqpStats.addTextBox(""+newStats[Stats.BattlePower.ordinal()], 	column3x, (int)(eqpStats.height*(percentOfHeight*i++)), newStats[Stats.BattlePower.ordinal()] 	> selectedChar.getStat(Stats.BattlePower) 	? blueMenuTextCenter : newStats[Stats.BattlePower.ordinal()] 	< selectedChar.getStat(Stats.BattlePower) 	? redMenuTextCenter : menuTextCenter);
			eqpStats.addTextBox(""+newStats[Stats.Defense.ordinal()], 		column3x, (int)(eqpStats.height*(percentOfHeight*i++)), newStats[Stats.Defense.ordinal()] 		> selectedChar.getStat(Stats.Defense) 		? blueMenuTextCenter : newStats[Stats.Defense.ordinal()] 		< selectedChar.getStat(Stats.Defense) 		? redMenuTextCenter : menuTextCenter);
			eqpStats.addTextBox(""+newStats[Stats.MagicPower.ordinal()], 	column3x, (int)(eqpStats.height*(percentOfHeight*i++)), newStats[Stats.MagicPower.ordinal()] 	> selectedChar.getStat(Stats.MagicPower) 	? blueMenuTextCenter : newStats[Stats.MagicPower.ordinal()] 	< selectedChar.getStat(Stats.MagicPower) 	? redMenuTextCenter : menuTextCenter);
			eqpStats.addTextBox(""+newStats[Stats.MagicDefense.ordinal()], 	column3x, (int)(eqpStats.height*(percentOfHeight*i++)), newStats[Stats.MagicDefense.ordinal()] 	> selectedChar.getStat(Stats.MagicDefense) 	? blueMenuTextCenter : newStats[Stats.MagicDefense.ordinal()] 	< selectedChar.getStat(Stats.MagicDefense) 	? redMenuTextCenter : menuTextCenter);
			eqpStats.addTextBox(""+newStats[Stats.Strength.ordinal()], 		column3x, (int)(eqpStats.height*(percentOfHeight*i++)), newStats[Stats.Strength.ordinal()] 		> selectedChar.getStat(Stats.Strength) 		? blueMenuTextCenter : newStats[Stats.Strength.ordinal()] 		< selectedChar.getStat(Stats.Strength) 		? redMenuTextCenter : menuTextCenter);
			eqpStats.addTextBox(""+newStats[Stats.Agility.ordinal()], 		column3x, (int)(eqpStats.height*(percentOfHeight*i++)), newStats[Stats.Agility.ordinal()] 		> selectedChar.getStat(Stats.Agility) 		? blueMenuTextCenter : newStats[Stats.Agility.ordinal()] 		< selectedChar.getStat(Stats.Agility) 		? redMenuTextCenter : menuTextCenter);
			eqpStats.addTextBox(""+newStats[Stats.Vitality.ordinal()], 		column3x, (int)(eqpStats.height*(percentOfHeight*i++)), newStats[Stats.Vitality.ordinal()] 		> selectedChar.getStat(Stats.Vitality) 		? blueMenuTextCenter : newStats[Stats.Vitality.ordinal()] 		< selectedChar.getStat(Stats.Vitality) 		? redMenuTextCenter : menuTextCenter);
			eqpStats.addTextBox(""+newStats[Stats.Intelligence.ordinal()], 	column3x, (int)(eqpStats.height*(percentOfHeight*i++)), newStats[Stats.Intelligence.ordinal()] 	> selectedChar.getStat(Stats.Intelligence) 	? blueMenuTextCenter : newStats[Stats.Intelligence.ordinal()] 	< selectedChar.getStat(Stats.Intelligence) 	? redMenuTextCenter : menuTextCenter);
			eqpStats.addTextBox(""+newStats[Stats.Speed.ordinal()], 		column3x, (int)(eqpStats.height*(percentOfHeight*i++)), newStats[Stats.Speed.ordinal()] 		> selectedChar.getStat(Stats.Speed) 		? blueMenuTextCenter : newStats[Stats.Speed.ordinal()] 			< selectedChar.getStat(Stats.Speed) 		? redMenuTextCenter : menuTextCenter);
			eqpStats.addTextBox(""+newStats[Stats.Evade.ordinal()], 		column3x, (int)(eqpStats.height*(percentOfHeight*i++)), newStats[Stats.Evade.ordinal()] 		> selectedChar.getStat(Stats.Evade) 		? blueMenuTextCenter : newStats[Stats.Evade.ordinal()] 			< selectedChar.getStat(Stats.Evade) 		? redMenuTextCenter : menuTextCenter);
			eqpStats.addTextBox(""+newStats[Stats.Block.ordinal()], 		column3x, (int)(eqpStats.height*(percentOfHeight*i++)), newStats[Stats.Block.ordinal()] 		> selectedChar.getStat(Stats.Block) 		? blueMenuTextCenter : newStats[Stats.Block.ordinal()] 			< selectedChar.getStat(Stats.Block) 		? redMenuTextCenter : menuTextCenter);
			eqpStats.addTextBox(""+newStats[Stats.Crit.ordinal()], 			column3x, (int)(eqpStats.height*(percentOfHeight*i++)), newStats[Stats.Crit.ordinal()] 			> selectedChar.getStat(Stats.Crit) 			? blueMenuTextCenter : newStats[Stats.Crit.ordinal()] 			< selectedChar.getStat(Stats.Crit) 			? redMenuTextCenter : menuTextCenter);
			
			
		}
		
	}
	private void updateEquipmentSlots()
	{
		eqpEquipSlots.clearObjects();
		
		//create equip slots
		ListBoxEntry entry;	
		float iconScale = 1.75f;
		int d = (int)((float)Global.iconSize*iconScale/2.0f);
		
		//Weapon
		entry = eqpEquipSlots.addItem(selectedChar.hand1Equipped() ? selectedChar.hand1().getDisplayName() : "Weapon", Item.Type.Weapon, !selectedChar.hand1Equipped());
		entry.getTextAt(0).x += d*2 + 4;
		entry.addPicBox(Global.createIcon(selectedChar.hand1Equipped() ? selectedChar.hand1().getIcon() : "sword", d + 6, eqpEquipSlots.getRowHeight()/2, iconScale));
		
		//Shield
		entry = eqpEquipSlots.addItem(selectedChar.hand2Equipped() ? selectedChar.hand2().getDisplayName() : "Shield", Item.Type.Shield, !selectedChar.hand2Equipped());
		entry.getTextAt(0).x += d*2 + 4;
		entry.addPicBox(Global.createIcon(selectedChar.hand2Equipped() ? selectedChar.hand2().getIcon() : "hshield", d + 6, eqpEquipSlots.getRowHeight()/2, iconScale));
		
		//Helmet
		entry = eqpEquipSlots.addItem(selectedChar.helmEquipped() ? selectedChar.helmet().getDisplayName() : "Helmet", Item.Type.Helmet, !selectedChar.helmEquipped());
		entry.getTextAt(0).x += d*2 + 4;
		entry.addPicBox(Global.createIcon(selectedChar.helmEquipped() ? selectedChar.helmet().getIcon() : "hhelmet", d + 6, eqpEquipSlots.getRowHeight()/2, iconScale));
		
		//Torso
		entry = eqpEquipSlots.addItem(selectedChar.torsoEquipped() ? selectedChar.torso().getDisplayName() : "Torso", Item.Type.Torso, !selectedChar.torsoEquipped());
		entry.getTextAt(0).x += d*2 + 4;
		entry.addPicBox(Global.createIcon(selectedChar.torsoEquipped() ? selectedChar.torso().getIcon() : "htorso", d + 6, eqpEquipSlots.getRowHeight()/2, iconScale));
		
		//Accessory
		entry = eqpEquipSlots.addItem(selectedChar.accessEquipped() ? selectedChar.accessory().getDisplayName() : "Accessory", Item.Type.Accessory, !selectedChar.accessEquipped());
		entry.getTextAt(0).x += d*2 + 4;
		entry.addPicBox(Global.createIcon(selectedChar.accessEquipped() ? selectedChar.accessory().getIcon() : "ring", d + 6, eqpEquipSlots.getRowHeight()/2, iconScale));
				
		eqpEquipSlots.update();
	}	
	private void fillEqpSelect()
	{
		ListBoxEntry entry;
		boolean disabled = false;		
		eqpSelect.clearObjects();
		float iconScale = 2.0f;
		int d = (int)((float)Global.iconSize*iconScale/2.0f);
		
		if(equipItemType != null)
		{
			entry = eqpSelect.addItem("Remove", null, false);				
			entry.getTextAt(0).x += d*2 + 4;
		}			
		
		for(Item i : Global.party.getInventory())
			if(i.getType() != Type.Key && !i.isUsable(UseTypes.World) && i.getUsableChars().contains(selectedChar.getName()))
			{
				if(i.getType() == equipItemType)
				{	
					entry = eqpSelect.addItem(i.getDisplayName(), i, disabled);				
					entry.getTextAt(0).x += d*2 + 4;					
					//add item count
					entry.addTextBox(""+i.getCount(), eqpSelect.getColumnWidth() - 32, eqpSelect.getRowHeight()/2, disabled ? grayMenuText : menuText);
					entry.addPicBox(Global.createIcon(i.getIcon(), d + 6, eqpSelect.getRowHeight()/2, iconScale));
					
				}
				
			}	
		
		eqpSelect.update();	
	}
	private void buildCharStatus()
	{
		charStatusGrow = new MenuPanel(Global.vpWidth/2, Global.vpHeight/2, 0, 0);
		charStatusGrow.anchor = Anchors.TrueCenter;
		charStatusGrow.setOpenSize(Global.vpWidth, Global.vpHeight);
		charStatusGrow.openSpeed = sideBarOpenSpeed;

		charStatus = new MenuPanel(Global.vpWidth/2, Global.vpHeight/2, Global.vpWidth, Global.vpHeight);
		charStatus.anchor = Anchors.TrueCenter;
		
		
		
		backButton = new ListBox(new Rect(Global.vpWidth-menuWidth, 0, Global.vpWidth, barHeight), 1, 1, menuTextCenter);
		backButton.addItem("BACK", null, false);
		

	}
	private void updateCharStatus()
	{		
		populateCharStatusPanel(charStatus, selectedChar);	
	}
	private void buildSkillScreen()
	{
		skillsBackButton = new ListBox(Global.vpWidth*2/3, 0, Global.vpWidth/3, barHeight, 1, 1, menuTextCenter);
		skillsBackButton.addItem("Back", "bak", false);
		
		skillsCharPanel = new MenuPanel(0, 0, Global.vpWidth, Global.vpHeight/3);		
		
		skillsList = new ListBox(0, Global.vpHeight/3, Global.vpWidth, Global.vpHeight*2/3, 5, 2, menuText);
		skillsList.setDisabledPaint(grayMenuText);	
	}
	private void updateSkillScreen()
	{
		//charpanel
		skillsCharPanel.clear();
		Rect src = selectedChar.getPortraitSrcRect();
		Rect dest = new Rect(0, 0, Global.vpHeight/3, Global.vpHeight/3);
		dest.inset(12, 12);
		skillsCharPanel.addPicBox(Global.bitmaps.get("portraits"), src, dest);		
		skillsCharPanel.addTextBox(selectedChar.getDisplayName(), Global.vpHeight/3, (int)((Global.vpHeight/3)*0.25f), menuText);		
		skillsCharPanel.addTextBox("lv"+selectedChar.getLevel(), Global.vpWidth*2/3-6, (int)((Global.vpHeight/3)*0.25f), smallTextRight);
		skillsCharPanel.addTextBox("HP:", (Global.vpWidth*2/3-Global.vpHeight/3)/2+Global.vpHeight/3-6, (int)((Global.vpHeight/3)*0.50f), smallTextRight);
		skillsCharPanel.addTextBox(selectedChar.getHP()+"/"+selectedChar.getStat(Stats.MaxHP), Global.vpWidth*2/3-6, (int)((Global.vpHeight/3)*0.50f), smallTextRight);
		skillsCharPanel.addTextBox("MP:", (Global.vpWidth*2/3-Global.vpHeight/3)/2+Global.vpHeight/3-6, (int)((Global.vpHeight/3)*0.75f), smallTextRight);
		skillsCharPanel.addTextBox(selectedChar.getMP()+"/"+selectedChar.getStat(Stats.MaxMP), Global.vpWidth*2/3-6, (int)((Global.vpHeight/3)*0.75f), smallTextRight);
		
		
		//skills list
		ListBoxEntry entry;
		
		skillsList.clear();
		float iconScale = 2.0f;
		int d = (int)((float)Global.iconSize*iconScale/2.0f);
		for(Ability ab : selectedChar.getAbilities())
		{
			entry = skillsList.addItem(ab.getDisplayName(), ab, !ab.isUsableOutOfBattle());
			entry.getTextAt(0).x += d*2 + 4;					
			//add item count
			boolean disabled = !ab.isUsableOutOfBattle() || ab.MPCost() > selectedChar.getMP() || !ab.isEnabled();
			if(disabled) entry.disable(grayMenuText);
			entry.addTextBox(""+ab.MPCost(), skillsList.getColumnWidth() - 32, skillsList.getRowHeight()/2, disabled ? grayMenuText : menuText);
			entry.addPicBox(Global.createIcon("orb", d + 6, skillsList.getRowHeight()/2, iconScale));
		}

				
				
	}
	private void buildOptionsScreen()
	{
		opts = new ListBox(0, 0, Global.vpWidth, Global.vpHeight, 6, 1, blueMenuText);
		
		opts.addItem("Text Speed", "txt", false);				
		opts.addItem("Frame Color 1", "fc1", false);		
		opts.addItem("Frame Color 2", "fc2", false);		
		opts.addItem("Image Scaling", "is", false);		
		opts.addItem("Control Scheme", "cs", false);
		opts.addItem("Back", "bak", false);
		
		//opts.drawOutlines();
		
		for(ListBoxEntry entry : opts.getEntries())
		{
			if(!entry.obj.equals("bak"))
			{
				entry.getTextAt(0).x += 10;
				entry.addTextBox("#####", opts.getColumnWidth()/2, opts.getRowHeight()/2, menuText);
			
			}
			else
			{
				entry.getTextAt(0).textPaint = menuTextCenter;
				entry.getTextAt(0).x = opts.getColumnWidth() / 2;
			}
		}
		
		optsColorWindow = new MenuPanel(Global.vpWidth/2, Global.vpHeight/2, 0, 0);
		optsColorWindow.setOpenSize((int)(menuWidth * 2.5f), barHeight*4);
		optsColorWindow.openSpeed = sideBarOpenSpeed;
		optsColorWindow.anchor = Anchors.TrueCenter;
		
		optsColorWindow.addTextBox("R:", (int)(menuWidth*0.25f), (int)(barHeight*4*0.125f), menuTextCenter);
		optsColorWindow.addTextBox("G:", (int)(menuWidth*0.25f), (int)(barHeight*4*0.375f), menuTextCenter);
		optsColorWindow.addTextBox("B:", (int)(menuWidth*0.25f), (int)(barHeight*4*0.625f), menuTextCenter);
		
		
		npRed = new NumberPicker(
				Global.vpWidth/2 - (int)(menuWidth*0.75f), 
				Global.vpHeight / 2 - barHeight*2, 
				menuWidth*2, barHeight);
		npRed.setMinMax(0, 255);
		npRed.drawFrame = false;
		
		npGreen = new NumberPicker(
				Global.vpWidth/2 - (int)(menuWidth*0.75f), 
				Global.vpHeight / 2 - barHeight, 
				menuWidth*2, barHeight);
		npGreen.setMinMax(0, 255);
		npGreen.drawFrame = false;
		
		npBlue = new NumberPicker(
				Global.vpWidth/2 - (int)(menuWidth*0.75f), 
				Global.vpHeight / 2, 
				menuWidth*2, barHeight);
		npBlue.setMinMax(0, 255);
		npBlue.drawFrame = false;
		
		optsBackButton = new ListBox(
				Global.vpWidth/2 - (int)(menuWidth*1.25f), 
				Global.vpHeight / 2 + barHeight, 
				(int)(menuWidth*2.5f), barHeight, 1, 1, menuTextCenter);
		optsBackButton.addItem("DONE", null, false);
			
	}
	private void updateOptionsScreen()
	{
		int i = 0;
		
		//Test Speed
		opts.getEntryAt(i).getTextAt(1).text = ""+Global.textSpeed;
		if(Global.textSpeed == 1)
			opts.getEntryAt(i).getTextAt(1).text = "Slow";
		else if(Global.textSpeed == 3)
			opts.getEntryAt(i).getTextAt(1).text = "Normal";
		else if(Global.textSpeed == 5)
			opts.getEntryAt(i).getTextAt(1).text = "Fast";
		
		++i;
		
		//FC1
		opts.getEntryAt(i++).getTextAt(1).text = 
				"R:"+Global.fc1r+
				" G:"+Global.fc1g+
				" B:"+Global.fc1b;
		
		//FC2
		opts.getEntryAt(i++).getTextAt(1).text = 
				"R:"+Global.fc2r+
				" G:"+Global.fc2g+
				" B:"+Global.fc2b;
		
		//Image Scaling
		opts.getEntryAt(i++).getTextAt(1).text = Global.stretchScreen ? "ON" : "OFF";
		
		//Control Scheme
		opts.getEntryAt(i++).getTextAt(1).text = "Touch Screen";
	}
	
	private boolean hasItemToEquip(PlayerCharacter pc, Item.Type type)
	{
		List<Item> usableItems = new ArrayList<Item>();

		for(Item i : Global.party.getInventory())
			if(i.getType() == type && i.getUsableChars().contains(pc.getName()))
				usableItems.add(i);	
		
		return !usableItems.isEmpty();

	}
	
	private static void buildCharMainBox(MenuPanel panel, PlayerCharacter pc)
	{
		MenuPanel mainBox = new MenuPanel(0, barHeight, Global.vpWidth - menuWidth, Global.vpHeight - barHeight*3);
		Paint blue = buildPaint(12, Color.CYAN, Align.LEFT);
		Paint white = buildPaint(12, Color.WHITE, Align.RIGHT);
		
		//Portrait
		Rect src = pc.getPortraitSrcRect();
		
		
		Rect dest = new Rect(0, (mainBox.height - menuWidth) / 2 - 12, menuWidth,  (mainBox.height - menuWidth) / 2 + menuWidth - 12);
		dest.inset(16, 16);
		mainBox.addPicBox(Global.bitmaps.get("portraits"), src, dest);
		
		int numInfoRows = 8;
		int infoRows[] = new int[numInfoRows];		
		for(int i = 0; i < numInfoRows; ++i)
			infoRows[i] = mainBox.height / (numInfoRows+1) * (i+1);
		
		int infoCols[] = new int[2];
		infoCols[0] = menuWidth;
		infoCols[1] = mainBox.width - 10;
		
		//HP/MP
		mainBox.addTextBox("HP:", infoCols[0], infoRows[0],  blue);
		mainBox.addTextBox(pc.getHP()+"/"+pc.getStat(Stats.MaxHP), infoCols[1], infoRows[0],  white);
		mainBox.addTextBox("MP:", infoCols[0], infoRows[1],  blue);
		mainBox.addTextBox(pc.getMP()+"/"+pc.getStat(Stats.MaxMP), infoCols[1], infoRows[1],  white);
		
		mainBox.addTextBox("Level:", infoCols[0], infoRows[3],  blue);
		mainBox.addTextBox(""+pc.getLevel(), infoCols[1], infoRows[3],  white);
		mainBox.addTextBox("Experience:", infoCols[0], infoRows[4],  blue);
		mainBox.addTextBox(""+pc.getExp(), infoCols[1], infoRows[5],  white);
		mainBox.addTextBox("For Level Up", infoCols[0], infoRows[6],  blue);
		mainBox.addTextBox(""+pc.getRemainingExp(), infoCols[1], infoRows[7],  white);
		
		List<StatusEffect> seList = pc.getStatusEffects();
		if(seList.size() > 0)
		{
			float iconScale = 1.5f;
			int j = 0, d = (int)(Global.iconSize*iconScale + 2);				
			
			int seCount = 0;				
			for(StatusEffect se : seList)
				if(se.icon().length() > 0)
					seCount++;
			
			int seWidth = d*Math.min(6, seCount);
			
			for(StatusEffect se : seList)
				if(j < 6 && se.icon().length() > 0)
					mainBox.addPicBox(
						Global.createIcon(se.icon(), 
								(int)((menuWidth - seWidth)/2.0f) + d/2 + d*j++, 
								mainBox.height - barHeight / 2,
								iconScale));
		}
		
		panel.addChildPanel(mainBox);
	}
	private static void buildCharNameBox(MenuPanel panel, PlayerCharacter pc)
	{
		Paint largeWhiteCenter = buildPaint(18, Color.WHITE, Align.CENTER);
		MenuPanel nameBox = new MenuPanel(Global.vpWidth / 2, 0, Global.vpWidth - menuWidth*2, barHeight);
		nameBox.anchor = Anchors.TopCenter;
		
		nameBox.addTextBox(pc.getDisplayName(), nameBox.width/2, nameBox.height / 2, largeWhiteCenter);
		
		panel.addChildPanel(nameBox);
	}
	private static void buildCharStatsBox(MenuPanel panel, PlayerCharacter pc)
	{
		MenuPanel statsBox = new MenuPanel(Global.vpWidth, barHeight, menuWidth, Global.vpHeight - barHeight);
		statsBox.anchor = Anchors.TopRight;
		
		Paint blueRight = buildPaint(10, Color.CYAN, Align.RIGHT);
		Paint whiteRight = buildPaint(10, Color.WHITE, Align.RIGHT);
		Paint redRight = buildPaint(10, Color.RED, Align.RIGHT);				
		
		float widths[] = new float[1];
		blueRight.getTextWidths("5", widths);
		int charWidth = (int)widths[0];
		
		int statsBuffer = charWidth * 5;
		
		int statCols[] = new int[1];		
		statCols[0] = menuWidth - statsBuffer - charWidth;	
		
		int numStatRows = 12;
		int statRows[] = new int[numStatRows];		
		for(int i = 0; i < numStatRows; ++i)
			statRows[i] = statsBox.height / (numStatRows+1) * (i+1);		
			

		pc.updateSecondaryStats();			

		statsBox.addTextBox("Power:", statCols[0], statRows[0], blueRight);
		statsBox.addTextBox(""+pc.getStat(Stats.BattlePower), 
				statCols[0] + statsBuffer, statRows[0], genStatPaint(pc, Stats.BattlePower, blueRight, redRight, whiteRight));
		
		statsBox.addTextBox("Defend:", statCols[0], statRows[1], blueRight);
		statsBox.addTextBox(""+pc.getStat(Stats.Defense), 
				statCols[0] + statsBuffer, statRows[1], genStatPaint(pc, Stats.Defense, blueRight, redRight, whiteRight));
		
		statsBox.addTextBox("M.Pow:", statCols[0], statRows[2], blueRight);
		statsBox.addTextBox(""+pc.getStat(Stats.MagicPower), 
				statCols[0] + statsBuffer, statRows[2], genStatPaint(pc, Stats.MagicPower, blueRight, redRight, whiteRight));
		
		statsBox.addTextBox("M.Def:", statCols[0], statRows[3], blueRight);
		statsBox.addTextBox(""+pc.getStat(Stats.MagicDefense), 
				statCols[0] + statsBuffer, statRows[3], genStatPaint(pc, Stats.MagicDefense, blueRight, redRight, whiteRight));
		

		statsBox.addTextBox("Stren:", statCols[0], statRows[4], blueRight);
		statsBox.addTextBox(""+pc.getStat(Stats.Strength), 
				statCols[0] + statsBuffer, statRows[4], genStatPaint(pc, Stats.Strength, blueRight, redRight, whiteRight));
		
		statsBox.addTextBox("Agil:", statCols[0], statRows[5], blueRight);
		statsBox.addTextBox(""+pc.getStat(Stats.Agility), 
				statCols[0] + statsBuffer, statRows[5], genStatPaint(pc, Stats.Agility, blueRight, redRight, whiteRight));
		
		statsBox.addTextBox("Vital:", statCols[0], statRows[6], blueRight);
		statsBox.addTextBox(""+pc.getStat(Stats.Vitality), 
				statCols[0] + statsBuffer, statRows[6], genStatPaint(pc, Stats.Vitality, blueRight, redRight, whiteRight));
		
		statsBox.addTextBox("Intel:", statCols[0], statRows[7], blueRight);
		statsBox.addTextBox(""+pc.getStat(Stats.Intelligence), 
				statCols[0] + statsBuffer, statRows[7], genStatPaint(pc, Stats.Intelligence, blueRight, redRight, whiteRight));				

		statsBox.addTextBox("Speed:", statCols[0], statRows[8], blueRight);
		statsBox.addTextBox(""+pc.getStat(Stats.Speed), 
				statCols[0] + statsBuffer, statRows[8], genStatPaint(pc, Stats.Speed, blueRight, redRight, whiteRight));
		
		statsBox.addTextBox("Evade:", statCols[0], statRows[9], blueRight);
		statsBox.addTextBox(""+pc.getStat(Stats.Evade), 
				 statCols[0] + statsBuffer, statRows[9], genStatPaint(pc, Stats.Evade, blueRight, redRight, whiteRight));
		
		statsBox.addTextBox("Block:", statCols[0], statRows[10], blueRight);
		statsBox.addTextBox(""+pc.getStat(Stats.Block), 
				statCols[0] + statsBuffer, statRows[10], genStatPaint(pc, Stats.Block, blueRight, redRight, whiteRight));
		
		statsBox.addTextBox("Crit:", statCols[0], statRows[11], blueRight);
		statsBox.addTextBox(""+pc.getStat(Stats.Crit), 
				statCols[0] + statsBuffer, statRows[11], genStatPaint(pc, Stats.Crit, blueRight, redRight, whiteRight));
		
		panel.addChildPanel(statsBox);
	}
	private static Paint genStatPaint(PlayerCharacter c, Stats stat, Paint good, Paint bad, Paint neutral)
	{
		int mod = c.getStatMod(stat);
		return mod > 0 ? good : mod < 0 ? bad : neutral; 
	}
	private static void buildCharEquipBox(MenuPanel panel, PlayerCharacter pc)
	{
		MenuPanel equipBox = new MenuPanel(0, Global.vpHeight, Global.vpWidth - menuWidth, barHeight * 2);
		equipBox.anchor = Anchors.BottomLeft;
		
		Paint blue = buildPaint(10, Color.CYAN, Align.LEFT);
		Paint blueRight = buildPaint(10, Color.CYAN, Align.RIGHT);
		Paint whiteLeft = buildPaint(10, Color.WHITE, Align.LEFT);
		Paint grayLeft = buildPaint(10, Color.GRAY, Align.LEFT);
		
		Paint redRight = buildPaint(10, Color.RED, Align.RIGHT);
		Paint whiteRight = buildPaint(10, Color.WHITE, Align.RIGHT);
		
		Paint fire = buildPaint(10, Color.rgb(255, 128, 42), Align.RIGHT);
		Paint earth = buildPaint(10, Color.rgb(165, 138, 56), Align.RIGHT);
		Paint wind = buildPaint(10, Color.rgb(180, 210, 210), Align.RIGHT);
		Paint water = buildPaint(10, Color.rgb(80, 160, 215), Align.RIGHT);
		
		
		int numEqpStats = 5;
		int eqpRows[] = new int[numEqpStats];		
		for(int i = 0; i < numEqpStats; ++i)
			eqpRows[i] = equipBox.height / (numEqpStats+1) * (i+1);	
		
		float iconScale = 1.5f;
		int d = (int)((float)Global.iconSize*iconScale/2.0f);
		
		float widths[] = new float[1];
		blue.getTextWidths("5", widths);
		int charWidth = (int)widths[0];
		
		int eqpCols[] = new int[4];
		int iconBuffer = 10;
		eqpCols[0] = d*2 + iconBuffer*2;
		eqpCols[1] = equipBox.width/2 + charWidth * 2;
		eqpCols[2] = equipBox.width - charWidth * 6;
		eqpCols[3] = equipBox.width - charWidth;
		
		int iconYBuff = 2;
		
		//equipment
		equipBox.addPicBox(Global.createIcon(pc.hand1Equipped() ? pc.hand1().getIcon() : "sword", d + iconBuffer, eqpRows[0] + iconYBuff, iconScale));
		equipBox.addTextBox(pc.hand1Equipped() ? pc.hand1().getDisplayName() : "Weapon", eqpCols[0], eqpRows[0], pc.hand1Equipped() ? whiteLeft : grayLeft);
		
		equipBox.addPicBox(Global.createIcon(pc.hand2Equipped() ? pc.hand2().getIcon() : "hshield", d + iconBuffer, eqpRows[1] + iconYBuff, iconScale));
		equipBox.addTextBox(pc.hand2Equipped() ? pc.hand2().getDisplayName() : "Shield", eqpCols[0], eqpRows[1], pc.hand2Equipped() ? whiteLeft : grayLeft);
		
		equipBox.addPicBox(Global.createIcon(pc.helmEquipped() ? pc.helmet().getIcon() : "hhelmet", d + iconBuffer, eqpRows[2] + iconYBuff, iconScale));
		equipBox.addTextBox(pc.helmEquipped() ? pc.helmet().getDisplayName() : "Helmet", eqpCols[0], eqpRows[2], pc.helmEquipped() ? whiteLeft : grayLeft);
		
		equipBox.addPicBox(Global.createIcon(pc.torsoEquipped() ? pc.torso().getIcon() : "htorso", d + iconBuffer, eqpRows[3] + iconYBuff, iconScale));
		equipBox.addTextBox(pc.torsoEquipped() ? pc.torso().getDisplayName() : "Torso", eqpCols[0], eqpRows[3], pc.torsoEquipped() ? whiteLeft : grayLeft);
		
		equipBox.addPicBox(Global.createIcon(pc.accessEquipped() ? pc.accessory().getIcon() : "ring", d + iconBuffer, eqpRows[4] + iconYBuff, iconScale));
		equipBox.addTextBox(pc.accessEquipped() ? pc.accessory().getDisplayName() : "Accessory", eqpCols[0], eqpRows[4], pc.accessEquipped() ? whiteLeft : grayLeft);
		
		//Affinity
		equipBox.addTextBox("Affinity", eqpCols[1], eqpRows[0], blue);
		equipBox.addTextBox("Fire:", eqpCols[2], eqpRows[1], fire);
		equipBox.addTextBox("Earth:", eqpCols[2], eqpRows[2], earth);
		equipBox.addTextBox("Wind:", eqpCols[2], eqpRows[3], wind);
		equipBox.addTextBox("Water:", eqpCols[2], eqpRows[4], water);
		
		equipBox.addTextBox(""+pc.getStat(Stats.Fire), eqpCols[3], eqpRows[1], genStatPaint(pc, Stats.Fire, blueRight, redRight, whiteRight));
		equipBox.addTextBox(""+pc.getStat(Stats.Earth), eqpCols[3], eqpRows[2], genStatPaint(pc, Stats.Earth, blueRight, redRight, whiteRight));
		equipBox.addTextBox(""+pc.getStat(Stats.Wind), eqpCols[3], eqpRows[3], genStatPaint(pc, Stats.Wind, blueRight, redRight, whiteRight));
		equipBox.addTextBox(""+pc.getStat(Stats.Water), eqpCols[3], eqpRows[4], genStatPaint(pc, Stats.Water, blueRight, redRight, whiteRight));
		
		
		panel.addChildPanel(equipBox);	
	}

	public static void populateCharStatusPanel(MenuPanel panel, PlayerCharacter pc)
	{
		panel.clear();
		
		Paint p = buildPaint(13,  Color.WHITE, Align.CENTER);
		
		MenuPanel label = new MenuPanel(0, 0, menuWidth, barHeight);
		label.addTextBox("STATUS", menuWidth/2, barHeight/2, p);
		
		panel.addChildPanel(label);
		
		buildCharMainBox(panel, pc);
		buildCharStatsBox(panel, pc);
		buildCharNameBox(panel, pc);
		buildCharEquipBox(panel, pc);
		
		panel.update();
	}
	
 	public void close()
	{
		if(!close)
		{
			close = true;
			Global.screenFader.setFadeColor(255, 0, 0, 0);
			Global.screenFader.fadeOut(0.5f);
		}
		
	}	
	private void handleClosing()
	{
		if(messageBox.Opened())
		{
			messageBox.close();
			undarken();
		}
		else
		{
			stateMachine.getState().handleClosing();
		}		
		
	}
	public void open()
	{
		buildRoot();
		stateMachine = new MainMenuStateMachine();
		stateMachine.setState(getRootState());	
		close = false;
	}
		
	public void cancelToState(MainMenuState prevState)
	{
		stateMachine.resetToState(prevState);				
	}

	private void updateMarkers()
	{
		Vector<DamageMarker> toRemove = new Vector<DamageMarker>();
		
		for(DamageMarker d : markers)
		{
			d.update();			
			if(d.isDone())
				toRemove.add(d);
		}
		
		for(DamageMarker d : toRemove)
			markers.remove(d);
		
		if(markers.isEmpty() && closeUseAfterClear)
		{
			closeUseAfterClear = false;
			stateMachine.setState(itemToUse != null ? getItemSelectState() : getSkillSelectState());
		}
	}
	private void darken(){darkening = true;}	
	private void undarken(){darkening = false;}	
	private void renderDark(){Global.renderer.drawColor(Color.argb(darkenAlpha, 0, 0, 0));}
	public void showMessage(String msg, boolean yesNoOpt)
	{
		darken();
		messageBox.addMessage(msg, yesNoOpt);
		messageBox.open();
	}
	private void showMessageYesNo(String msg, MsgBoxEndAction yesAction, MsgBoxEndAction noAction)
	{
		darken();
		messageBox.showYesNo(msg, yesAction, noAction);
		messageBox.open();
	}
	
	private void handleOption(String opt)
	{
		if(opt.equals("itm"))
			stateMachine.setState(getItemSelectState());	
		else if(opt.equals("eqp"))
			stateMachine.setState(getEquipSelectCharState());	
		else if(opt.equals("skl"))
			stateMachine.setState(getSkillSelectCharState());	
		else if(opt.equals("opt"))
			stateMachine.setState(getOptionsState());	
		else if(opt.equals("sav"))
		{
			showMessageYesNo(
					"Quit the game and return to the title screen?", 
					new MsgBoxEndAction(){
						public void execute()
						{
							Global.restartGame();
						}
					}, null);

		}
			//
		else if(opt.equals("clo"))
			close();
	}	
	private void handleInvOption(String opt)
	{
		if(opt.equals("srt"))
		{
			if(invShowKeys)
			{
				Global.party.sortInventoryKeys();
				fillInventory();				
			}
			else
				stateMachine.setState(getItemSortState());	
			
		}
		else if(opt.equals("key"))
		{
			if(invShowKeys)
			{
				invShowKeys = false;
				invInfoBar.changeItemText(1, "Key Items");
				fillInventory();
			}
			else
			{
				invShowKeys = true;
				invInfoBar.changeItemText(1, "Inventory");
				fillInventory();
			}
		}
		else if(opt.equals("bak"))
		{
			stateMachine.setState(getRootState());	
		}

	}
	private void handleEqpOption(String opt)
	{
		if(opt.equals("rmv"))
		{
			selectedChar.unequipAll();
			updateEquipScreen();
			
		}
		else if(opt.equals("opt"))
		{
			selectedChar.equipBest(Item.Type.Weapon);
			selectedChar.equipBest(Item.Type.Shield);
			selectedChar.equipBest(Item.Type.Helmet);
			selectedChar.equipBest(Item.Type.Torso);
			selectedChar.equipBest(Item.Type.Accessory);
			updateEquipScreen();
		}
		else if(opt.equals("bak"))
		{
			stateMachine.setState(getRootState());	
		}

	}
	private void handleInvSortOption(String opt)
	{
		if(opt.equals("abc"))
		{
			
			Global.party.sortInventoryABC();
			
		}
		else if(opt.equals("typ"))
		{
			Global.party.sortInventoryType();
			

		}
		else if(opt.equals("usf"))
		{
			Global.party.sortInventoryUsable(UseTypes.World);
		}
		
		else if(opt.equals("aut"))
		{
			Global.party.sortInventoryABC();
			Global.party.sortInventoryType();
			Global.party.sortInventoryUsable(UseTypes.World);
		}
		
		fillInventory();
		stateMachine.setState(getItemSelectState());	
	}
	private void handleOptOption(String opt)
	{
		if(opt.equals("txt"))
		{
			if(Global.textSpeed == 1)
				Global.textSpeed = 3;
			else if(Global.textSpeed == 3)
				Global.textSpeed = 5;
			else if(Global.textSpeed == 5)
				Global.textSpeed = 1;
		}
		else if(opt.equals("fc1"))
		{
			optsColorNumber = 1;
			stateMachine.setState(getOptionFrameColorState());	
		}
		else if(opt.equals("fc2"))
		{
			optsColorNumber = 2;
			stateMachine.setState(getOptionFrameColorState());	
		}
		else if(opt.equals("is"))
		{
			Global.stretchScreen = !Global.stretchScreen;
			Global.panel.scaleImage();
		}
		else if(opt.equals("cs"))
		{
			showMessage("More control options coming soon!", false);
		}
		else if(opt.equals("bak"))
		{
			stateMachine.setState(getRootState());	
		}
		
		updateOptionsScreen();

	}
	
	private void useAbility(PlayerCharacter c)
	{
		List<PlayerCharacter> charList, affectedList = new ArrayList<PlayerCharacter>();
		updateCharUseScreen();
		
		if(abilityToUse.TargetType() == TargetTypes.AllAllies)
			charList = Global.party.getPartyList(false);
		else
		{
			charList = new ArrayList<PlayerCharacter>();
			charList.add(c);
		}	
			
		for(PlayerCharacter ch : charList)
			if(abilityToUse.willAffect(ch))
				affectedList.add(ch);		
		
		if(affectedList.size() > 0)
		{
			abilityToUse.executeOutOfBattle(c, affectedList, markers);
			selectedChar.modifyMP(-abilityToUse.MPCost());
			updateCharUseScreen();
		}
		
	}
	
	private void useItem(PlayerCharacter c)
	{
		List<PlayerCharacter> charList, affectedList = new ArrayList<PlayerCharacter>();
		updateCharUseScreen();
		
		if(itemToUse.getTargetType() == TargetTypes.AllAllies)
			charList = Global.party.getPartyList(false);
		else
		{
			charList = new ArrayList<PlayerCharacter>();
			charList.add(c);
		}	
			
		for(PlayerCharacter ch : charList)
			if(itemToUse.willAffect(ch))
				affectedList.add(ch);
		
		
		if(affectedList.size() > 0)
		{
			itemToUse.executeOutOfBattle(c, affectedList, markers);
			Global.party.removeItem(itemToUse.getId(), 1);
			updateCharUseScreen();
		}	
		
	}
	private void fling(float velocityX, float velocityY)
	{
		List<PlayerCharacter> charList = Global.party.getPartyList(false);
		if(charList.size() > 1 && Math.abs(velocityY) < 300 && Math.abs(velocityX) > 300)
		{
			undarken();
			equipItemType = null;
			
			//determine current index
			int i = 0;
			for(PlayerCharacter c : charList)
			{
				if(c.getName().equals(selectedChar.getName()))
					break;
				++i;
			}
			//swiped right
			if(velocityX > 0)
			{
				if(i+1 >= charList.size())
					nextChar = charList.get(0);
				else
					nextChar = charList.get(i+1);
				
				sideBar.setClosed();
				sideBar.pos = new Point(0, 0);
				sideBar.anchor = Anchors.TopLeft;
				sideBar.open();
			}
			else //swiped left
			{
				if(i == 0)
					nextChar = charList.get(charList.size()-1);
				else
					nextChar = charList.get(i-1);
				
				sideBar.setClosed();
				sideBar.pos = new Point(Global.vpWidth, 0);
				sideBar.anchor = Anchors.TopRight;
				sideBar.open();
			}
		}
	}	
	private void updateRoot()
	{
		rootMenu.update();
		rootCharPlates.update();
		
		
		//update timer
		rootBarPanel.getTextAt(2).x = rootBarPanel.width - (int)smallText.measureText("Time:"+Global.playTimer.playTime()) - 10;
		rootBarPanel.getTextAt(2).text = "Time:"+Global.playTimer.playTime();
		
		if(!charStatusGrow.Closed())
			charStatusGrow.update();
	}
	
	public void update()
	{	
		//update darkness
		if(darkening)
			darkenAlpha = Math.min(darkenAlpha + darkenInc, darkenAlphaMax);
		else
			darkenAlpha = Math.max(darkenAlpha - darkenInc, 0);
		
		if(close)
			handleClosing();
		
		messageBox.update();
		sideBar.update();
		updateMarkers();
		
		stateMachine.getState().update();

		
	}
	public void render()
	{		
		Global.renderer.drawColor(Color.argb(255, 0, 0, 200));
		
		rootPanel.render();
		
		stateMachine.getState().render();
		
		if(messageBox.Opened())
			renderDark();
		
		messageBox.render();
		
		//render dmgMarkers
		for(DamageMarker d : markers)
			if(d.isShown())
				d.render();
	}
			
	public void onFling(float velocityX, float velocityY)
	{			
		stateMachine.getState().onFling(velocityX, velocityY);
	}
	public void backButtonPressed()
	{
		if(!close)
		{
			if(messageBox.Opened())
			{
				messageBox.close();
				undarken();
			}
			else
				stateMachine.getState().backButtonPressed();

		
		}
		
		
	}
	public void touchActionMove(int x, int y)
	{
		if(!close)
			if(messageBox.Opened())
				messageBox.touchActionMove(x, y);
			else
				stateMachine.getState().touchActionMove(x, y);		
	}
	public void touchActionUp(int x, int y)
	{		
		if(!close)
		{
			if(messageBox.Opened())
			{
				messageBox.touchActionUp(x, y);
				
				if(!messageBox.Opened())
					undarken();
			}
			else
				stateMachine.getState().touchActionUp(x, y);

		}
	}
	public void touchActionDown(int x, int y)
	{
		if(!close)
			if(messageBox.Opened())
				messageBox.touchActionDown(x, y);				
			else
				stateMachine.getState().touchActionDown(x, y);
	}
		
}
