package bladequest.UI;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.Rect;
import bladequest.UI.ListBox.LBStates;
import bladequest.UI.MenuPanel.Anchors;
import bladequest.UI.MsgBox.YesNo;
import bladequest.combat.DamageMarker;
import bladequest.statuseffects.StatusEffect;
import bladequest.world.Ability;
import bladequest.world.Character;
import bladequest.world.Global;
import bladequest.world.Item;
import bladequest.world.Item.Type;
import bladequest.world.States;
import bladequest.world.Stats;
import bladequest.world.TargetTypes;

public class MainMenu 
{
	private Paint menuText, menuTextRight, menuTextCenter, grayMenuText, 
	grayMenuTextCenter, redMenuText, blueMenuText, redMenuTextCenter, 
	blueMenuTextCenter, smallText, smallTextCenter, smallTextRight;
	private final float menuWidthVpPercent = 28.0f; 
	private final float menuHeightVpPercent = 16.0f;
	
	private final int menuWidth, barHeight;
	
	private menuStates currentState;
	private Vector<DamageMarker> markers;
	private Item itemToUse;
	private Character selectedChar, nextChar;
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
	private MenuPanel charStatusGrow, charStatus, charStatusLabel;
	private ListBox backButton, charStatusAbilities;
	
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
	
	
	
	public MainMenu()
	{
		currentState = menuStates.Root;
		
		markers= new Vector<DamageMarker>();
		
		menuWidth = (int)((float)Global.vpWidth * (menuWidthVpPercent/100.0f));
		barHeight = (int)((float)Global.vpHeight * (menuHeightVpPercent/100.0f));
		
		messageBox = new MsgBox();
		darkenAlpha = 0;
		darkening = false;
		
		equipItemType = null;
		
		buildPaints();
		
	}	
	
	private void buildPaints()
	{		
		smallText = Global.textFactory.getTextPaint(9, Color.WHITE, Align.LEFT);				
		smallTextCenter = Global.textFactory.getTextPaint(9, Color.WHITE, Align.CENTER);		
		smallTextRight = Global.textFactory.getTextPaint(9, Color.WHITE, Align.RIGHT);	
		
		menuText = Global.textFactory.getTextPaint(13, Color.WHITE, Align.LEFT);	
		menuTextCenter = Global.textFactory.getTextPaint(13, Color.WHITE, Align.CENTER);	
		menuTextRight = Global.textFactory.getTextPaint(13, Color.WHITE, Align.RIGHT);
		
		grayMenuText = Global.textFactory.getTextPaint(13, Color.GRAY, Align.LEFT);
		grayMenuTextCenter = Global.textFactory.getTextPaint(13, Color.GRAY, Align.CENTER);		
		redMenuText = Global.textFactory.getTextPaint(13, Color.RED, Align.LEFT);
		blueMenuText = Global.textFactory.getTextPaint(13, Color.CYAN, Align.LEFT);
		redMenuTextCenter = Global.textFactory.getTextPaint(13, Color.RED, Align.CENTER);
		blueMenuTextCenter = Global.textFactory.getTextPaint(13, Color.CYAN, Align.CENTER);
		
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
		Character[] chars = Global.party.getPartyMembers(false);
		
		for(int i = 0; i < 4; ++i)
		{
			rootCharPlates.panels[i].clear();
			rootCharPlates.panels[i].show();
			
			if(chars[i] == null)
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
			for(Item i : Global.party.getInventory(false))
				if(i.getType() == Type.Key)
					invList.addItem(i.getName(), i, false);
		}
		else
		{
			float iconScale = 2.0f;
			int d = (int)((float)Global.iconSize*iconScale/2.0f);
			for(Item i : Global.party.getInventory(false))
				if(i.getType() != Type.Key)
				{
					disabled = (i.getType() != Type.Usable) || 
							(i.getTargetType() == TargetTypes.SingleEnemy) ||
							(i.getTargetType() == TargetTypes.AllEnemies) ||
							(i.getTargetType() == TargetTypes.Self);
					
					entry = invList.addItem(i.getName(), i, disabled);
					
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
		List<Character> chars = Global.party.getPartyList(false);
		
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
	public void updateCharUseScreen()
	{
		List<Character> chars = Global.party.getPartyList(false);
		charUseScreen.clear();
		//charUseScreen.updateFrame();
		int width = charUseScreen.getColumnWidth();
		
		//update infobox
		if(itemToUse != null)
		{
			charUseInfo.getTextAt(0).text = "Count:" + itemToUse.getCount();
			charUseDesc.getTextAt(0).text = itemToUse.getDescription();

		}
		
		//update frames		
		ListBoxEntry lbi;
		for(Character c : chars)
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
		entry = eqpEquipSlots.addItem(selectedChar.weapEquipped() ? selectedChar.weapon().getName() : "Weapon", Item.Type.Weapon, !selectedChar.weapEquipped());
		entry.getTextAt(0).x += d*2 + 4;
		entry.addPicBox(Global.createIcon(selectedChar.weapEquipped() ? selectedChar.weapon().getIcon() : "sword", d + 6, eqpEquipSlots.getRowHeight()/2, iconScale));
		
		//Shield
		entry = eqpEquipSlots.addItem(selectedChar.shieldEquipped() ? selectedChar.shield().getName() : "Shield", Item.Type.Shield, !selectedChar.shieldEquipped());
		entry.getTextAt(0).x += d*2 + 4;
		entry.addPicBox(Global.createIcon(selectedChar.shieldEquipped() ? selectedChar.shield().getIcon() : "hshield", d + 6, eqpEquipSlots.getRowHeight()/2, iconScale));
		
		//Helmet
		entry = eqpEquipSlots.addItem(selectedChar.helmEquipped() ? selectedChar.helmet().getName() : "Helmet", Item.Type.Helmet, !selectedChar.helmEquipped());
		entry.getTextAt(0).x += d*2 + 4;
		entry.addPicBox(Global.createIcon(selectedChar.helmEquipped() ? selectedChar.helmet().getIcon() : "hhelmet", d + 6, eqpEquipSlots.getRowHeight()/2, iconScale));
		
		//Torso
		entry = eqpEquipSlots.addItem(selectedChar.torsoEquipped() ? selectedChar.torso().getName() : "Torso", Item.Type.Torso, !selectedChar.torsoEquipped());
		entry.getTextAt(0).x += d*2 + 4;
		entry.addPicBox(Global.createIcon(selectedChar.torsoEquipped() ? selectedChar.torso().getIcon() : "htorso", d + 6, eqpEquipSlots.getRowHeight()/2, iconScale));
		
		//Accessory
		entry = eqpEquipSlots.addItem(selectedChar.accessEquipped() ? selectedChar.accessory().getName() : "Accessory", Item.Type.Accessory, !selectedChar.accessEquipped());
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
		
		for(Item i : Global.party.getInventory(false))
			if(i.getType() != Type.Key && i.getType() != Type.Usable && i.getUsableChars().contains(selectedChar.getName()))
			{
				if(i.getType() == equipItemType)
				{	
					entry = eqpSelect.addItem(i.getName(), i, disabled);				
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
		
		charStatusLabel = new MenuPanel(0, 0, menuWidth, barHeight);
		charStatusLabel.addTextBox("STATUS", menuWidth/2, barHeight/2, menuTextCenter);
		
		backButton = new ListBox(new Rect(Global.vpWidth-menuWidth, 0, Global.vpWidth, barHeight), 1, 1, menuTextCenter);
		backButton.addItem("BACK", null, false);
		
		charStatusAbilities = new ListBox(new Rect(Global.vpWidth-(int)(menuWidth*1.5), (int)(barHeight*2.5), Global.vpWidth-(int)(menuWidth*0.25), menuWidth+(int)(barHeight*1.75)), 2, 1, menuTextCenter);
		charStatusAbilities.addItem("Ability", "ab", false);
		charStatusAbilities.addItem("Moveset", "ms", false);
	}
	private void updateCharStatus()
	{
		charStatus.clear();
		//Name
		charStatus.addTextBox(selectedChar.getDisplayName(), (int)(menuWidth*0.75), (int)(barHeight*1.5), menuTextCenter);
		
		//Portrait
		Rect src = selectedChar.getPortraitSrcRect();
		Rect dest = new Rect(0, (int)(barHeight*1.75), (int)(menuWidth*0.75), (int)(barHeight*1.75)+(int)(menuWidth*0.75));
		dest.inset(12, 12);
		charStatus.addPicBox(Global.bitmaps.get("portraits"), src, dest);
			
		//HP/MP
		charStatus.addTextBox("HP:", (int)(menuWidth*1.35), (int)(barHeight*1.5), blueMenuText);
		charStatus.addTextBox("MP:", (int)(menuWidth*1.55), (int)(barHeight*2.0), blueMenuText);
		charStatus.addTextBox(selectedChar.getHP()+"/"+selectedChar.getStat(Stats.MaxHP), (int)(menuWidth*1.65), (int)(barHeight*1.5), menuText);
		charStatus.addTextBox(selectedChar.getMP()+"/"+selectedChar.getStat(Stats.MaxMP), (int)(menuWidth*1.85), (int)(barHeight*2.0), menuText);
			
		//level and experience
		int abY = charStatusAbilities.pos.y-12;
		int abHei = charStatusAbilities.height+12;
		int i = 1;		
		charStatus.addTextBox("Level:", (int)(menuWidth*0.75), abY + (int)(abHei*0.17*i), blueMenuText);
		charStatus.addTextBox(""+selectedChar.getLevel(), charStatusAbilities.pos.x - 12, abY + (int)(abHei*0.17*i++), menuTextRight);
		charStatus.addTextBox("Experience:", (int)(menuWidth*0.75), abY + (int)(abHei*0.17*i++), blueMenuText);
		charStatus.addTextBox(""+selectedChar.getExp(), charStatusAbilities.pos.x - 12, abY + (int)(abHei*0.17*i++), menuTextRight);
		charStatus.addTextBox("For Level Up:", (int)(menuWidth*0.75), abY + (int)(abHei*0.17*i++), blueMenuText);
		charStatus.addTextBox(""+selectedChar.getRemainingExp(), charStatusAbilities.pos.x - 12, abY + (int)(abHei*0.17*i++), menuTextRight);
		
		selectedChar.updateSecondaryStats();
		
		//stats
		int statY = charStatusAbilities.pos.y + charStatusAbilities.height;
		int statHei = Global.vpHeight - statY;
		int buff = 8;
		i = 1;
		charStatus.addTextBox("POW:", buff, statY + (int)(statHei*0.2*i), blueMenuText);
		charStatus.addTextBox(""+selectedChar.getStat(Stats.BattlePower), (int)(Global.vpWidth*0.18)+buff, statY + (int)(statHei*0.2*i++), menuText);
		
		charStatus.addTextBox("DEF:", buff, statY + (int)(statHei*0.2*i), blueMenuText);
		charStatus.addTextBox(""+selectedChar.getStat(Stats.Defense), (int)(Global.vpWidth*0.18)+buff, statY + (int)(statHei*0.2*i++), menuText);
		
		charStatus.addTextBox("M.POW:", buff, statY + (int)(statHei*0.2*i), blueMenuText);
		charStatus.addTextBox(""+selectedChar.getStat(Stats.MagicPower), (int)(Global.vpWidth*0.18)+buff, statY + (int)(statHei*0.2*i++), menuText);
		
		charStatus.addTextBox("M.DEF:", buff, statY + (int)(statHei*0.2*i), blueMenuText);
		charStatus.addTextBox(""+selectedChar.getStat(Stats.MagicDefense), (int)(Global.vpWidth*0.18)+buff, statY + (int)(statHei*0.2*i++), menuText);
		
		i = 1;
		charStatus.addTextBox("STR:", (int)(Global.vpWidth*0.33)+buff, statY + (int)(statHei*0.2*i), blueMenuText);
		charStatus.addTextBox(""+selectedChar.getStat(Stats.Strength), (int)(Global.vpWidth*0.51)+buff, statY + (int)(statHei*0.2*i++), menuText);
		
		charStatus.addTextBox("AGI:", (int)(Global.vpWidth*0.33)+buff, statY + (int)(statHei*0.2*i), blueMenuText);
		charStatus.addTextBox(""+selectedChar.getStat(Stats.Agility), (int)(Global.vpWidth*0.51)+buff, statY + (int)(statHei*0.2*i++), menuText);
		
		charStatus.addTextBox("VIT:", (int)(Global.vpWidth*0.33)+buff, statY + (int)(statHei*0.2*i), blueMenuText);
		charStatus.addTextBox(""+selectedChar.getStat(Stats.Vitality), (int)(Global.vpWidth*0.51)+buff, statY + (int)(statHei*0.2*i++), menuText);
		
		charStatus.addTextBox("INT:", (int)(Global.vpWidth*0.33)+buff, statY + (int)(statHei*0.2*i), blueMenuText);
		charStatus.addTextBox(""+selectedChar.getStat(Stats.Intelligence), (int)(Global.vpWidth*0.51)+buff, statY + (int)(statHei*0.2*i++), menuText);
				
		i = 1;
		charStatus.addTextBox("SPD:", (int)(Global.vpWidth*0.66)+buff, statY + (int)(statHei*0.2*i), blueMenuText);
		charStatus.addTextBox(""+selectedChar.getStat(Stats.Speed), (int)(Global.vpWidth*0.84)+buff, statY + (int)(statHei*0.2*i++), menuText);
		
		charStatus.addTextBox("EV:", (int)(Global.vpWidth*0.66)+buff, statY + (int)(statHei*0.2*i), blueMenuText);
		charStatus.addTextBox(""+selectedChar.getStat(Stats.Evade), (int)(Global.vpWidth*0.84)+buff, statY + (int)(statHei*0.2*i++), menuText);
		
		charStatus.addTextBox("BLK:", (int)(Global.vpWidth*0.66)+buff, statY + (int)(statHei*0.2*i), blueMenuText);
		charStatus.addTextBox(""+selectedChar.getStat(Stats.Block), (int)(Global.vpWidth*0.84)+buff, statY + (int)(statHei*0.2*i++), menuText);
		
		charStatus.addTextBox("CRIT:", (int)(Global.vpWidth*0.66)+buff, statY + (int)(statHei*0.2*i), blueMenuText);
		charStatus.addTextBox(""+selectedChar.getStat(Stats.Crit), (int)(Global.vpWidth*0.84)+buff, statY + (int)(statHei*0.2*i++), menuText);
		
		//Abilities Labels
		charStatusAbilities.changeItemText(0, selectedChar.getActionName());
		charStatusAbilities.changeItemText(1, selectedChar.getAbilitiesName());
		
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
			entry.addTextBox(""+ab.MPCost(), skillsList.getColumnWidth() - 32, skillsList.getRowHeight()/2, !ab.isUsableOutOfBattle() ? grayMenuText : menuText);
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
	
	public void close()
	{
		if(!close)
		{
			close = true;
			Global.screenFader.setFadeColor(255, 0, 0, 0);
			Global.screenFader.fadeOut(4);
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
			switch(currentState)
			{
			case Root:
				if(Global.screenFader.isDone())
				{
					Global.GameState = States.GS_WORLDMOVEMENT;
					Global.delay();
					Global.screenFader.fadeIn(4);
					
					sideBar.setClosed();
					
				}
				break;
			case Options:
			case CharStatus:
			case EquipSelectChar:
			case SkillSelectChar:
			case SkillSelect:
			case Equip:
				changeState(menuStates.Root);
				break;
			case ItemSelect:			
				if(invSort.Closed() && charUseScreen.Closed())
					changeState(menuStates.Root);
				break;
			case ItemSort:			
			case ItemUse:
				changeState(menuStates.ItemSelect);
				break;
				
			case OptionFrameColor:
				changeState(menuStates.Options);
				break;
			}
		}		
		
	}
	public void open()
	{
		buildRoot();
		currentState = menuStates.Root;
		close = false;
	}
	

	public void applyDamage(Character c, int dmg, int delay)
	{
		switch(currentState)
		{
		case ItemUse:
			
			for(ListBoxEntry lbi : charUseScreen.getEntries())
			{
				//if(((Character)lbi.obj).getName().equals(c.getName()))
					//markers.add(new DamageMarker(dmg, c, delay, lbi.frameRect.left + lbi.width/2, lbi.frameRect.top + lbi.width/2));
				
			}
//			ListBoxEntry lbi = charUseScreen.getSelectedEntry();
//			markers.add(new DamageMarker(dmg, c, delay, lbi.frameRect.left + lbi.width/2, lbi.frameRect.top + lbi.width/2));
			break;
		}
		
	}
	public void dmgText(Character c, String str, int delay)
	{
		switch(currentState)
		{
		case ItemUse:
			ListBoxEntry lbi = charUseScreen.getSelectedEntry();
			//markers.add(new DamageMarker(str, c, delay, lbi.frameRect.left + lbi.width/2, lbi.frameRect.top + lbi.width/2));
			break;
		}
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
	}
	private void darken(){darkening = true;}	
	private void undarken(){darkening = false;}	
	private void renderDark(){Global.renderer.drawColor(Color.argb(darkenAlpha, 0, 0, 0));}
	private void showMessage(String msg, boolean yesNoOpt)
	{
		darken();
		messageBox.addMessage(msg, yesNoOpt);
		messageBox.open();
	}

	
	private void handleOption(String opt)
	{
		if(opt.equals("itm"))
			changeState(menuStates.ItemSelect);
		else if(opt.equals("eqp"))
			changeState(menuStates.EquipSelectChar);
		else if(opt.equals("skl"))
			changeState(menuStates.SkillSelectChar);
		else if(opt.equals("opt"))
			changeState(menuStates.Options);
		else if(opt.equals("sav"))
		{
			showMessage("Quit the game and return to the title screen?", true);
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
				changeState(menuStates.ItemSort);
			
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
			changeState(menuStates.Root);
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
			changeState(menuStates.Root);
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
			Global.party.sortInventoryUsable();
		}
		
		else if(opt.equals("aut"))
		{
			Global.party.sortInventoryABC();
			Global.party.sortInventoryType();
			Global.party.sortInventoryUsable();
		}
		
		fillInventory();
		changeState(menuStates.ItemSelect);
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
			changeState(menuStates.OptionFrameColor);
		}
		else if(opt.equals("fc2"))
		{
			optsColorNumber = 2;
			changeState(menuStates.OptionFrameColor);
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
			changeState(menuStates.Root);
		}
		
		updateOptionsScreen();

	}
	private void changeState(menuStates state)
	{			
		switch(state)
		{
		case SkillSelect:
			if(sideBar.Closed())
			{
				updateSkillScreen();
				sideBar.open();
			}
			undarken();
			break;
		case Equip:
			if(sideBar.Closed())
			{
				updateEquipScreen();
				sideBar.open();
			}
			undarken();
			break;
		case ItemSelect:
			if(sideBar.Closed())
			{
				buildInventory();
				sideBar.open();
			}
			undarken();
			
			switch(currentState)
			{
			case ItemSort:
				invSort.close();
				break;
			case ItemUse:
				charUseScreen.close();
				fillInventory();
				itemToUse = null;
				break;
			}
			break;
		case Root:
			undarken();
			buildCharPlates();
			
			sideBar.pos = new Point(Global.vpWidth, 0);
			sideBar.anchor = Anchors.TopRight;
			
			switch(currentState)
			{
			case Equip:
				sideBar.close();
				equipItemType = null;
				selectedEqpItem = null;
				eqpRemove = false;
				break;
			case Options:
			case SkillSelect:
				sideBar.close();
				break;
			case ItemSelect:
				sideBar.close();
				invShowKeys = false;
				break;
			case CharStatus:
				charStatusGrow.close();
				sideBar.setClosed();
				break;
			}
			break;	
		case Options:
			if(sideBar.Closed())				
				sideBar.open();

			undarken();			
			updateOptionsScreen();
			
			switch(currentState)
			{
			case OptionFrameColor:
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
				
				break;			
			}
			
			break;
		case OptionFrameColor:
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
			
			break;			
		case CharStatus:
			charStatusGrow.open();
			break;
		case ItemSort:
			darken();
			invSort.open();
			break;
		case ItemUse:
			darken();
			buildCharUseScreen();
			charUseScreen.open();
			break;
		case SkillSelectChar:
		case EquipSelectChar:
			darken();
			break;
		
		}
		
		currentState = state;
	}
	
	private void useItem(Character c)
	{
		List<Character> charList;
		
		if(itemToUse.getTargetType() == TargetTypes.AllAllies)
			charList = Global.party.getPartyList(false);
		else
		{
			charList = new ArrayList<Character>();
			charList.add(c);
		}	
		
		boolean willAffect = false;		
		for(Character ch : charList)
			if(itemToUse.willAffect(ch))
			{
				willAffect = true;
				break;
			}		
		
		if(willAffect)
		{
			itemToUse.execute(c, charList, markers);
			Global.party.removeItem(itemToUse.getId(), 1);
			updateCharUseScreen();
		}	
		
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
		
		switch(currentState)
		{
		case SkillSelectChar:
		case EquipSelectChar:
			rootInfoBar.update();
		case Root:
			rootMenu.update();
			rootCharPlates.update();
			
			
			//update timer
			rootBarPanel.getTextAt(2).x = rootBarPanel.width - (int)smallText.measureText("Time:"+Global.playTimer.playTime()) - 10;
			rootBarPanel.getTextAt(2).text = "Time:"+Global.playTimer.playTime();
			
			if(!charStatusGrow.Closed())
				charStatusGrow.update();
			
			break;
			
		case Equip:
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
			break;
			
		case SkillSelect:
			if(nextChar != null && sideBar.Opened())
			{
				selectedChar = nextChar;
				nextChar = null;
				updateSkillScreen();
				
			}
			
			skillsBackButton.update();
			skillsCharPanel.update();
			skillsList.update();
			break;
			
		case CharStatus:
			charStatusGrow.update();
			charStatus.update();
			backButton.update();
			charStatusAbilities.update();
			charStatusLabel.update();
			if(nextChar != null && sideBar.Opened())
			{
				selectedChar = nextChar;
				nextChar = null;
				updateCharStatus();
			}
			break;
			
		case ItemSelect:
			invInfoBar.update();
			invList.update();
			if(charUseScreen != null && !charUseScreen.Closed())
				charUseScreen.update();
			if(invSort != null && !invSort.Closed())
				invSort.update();

			break;
			
		case Options:
			opts.update();
			if(optsColorWindow != null && !optsColorWindow.Closed())
				optsColorWindow.update();
			break;
			
		case OptionFrameColor:
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
			break;
		case ItemSort:
			invSort.update();
			break;
			
		case ItemUse:
			charUseScreen.update();
			charUseInfo.update();
			charUseDesc.update();
			
			//if(itemToUse.getCount() == 0 && markers.size() == 0)
				//changeState(menuStates.ItemSelect);
			break;
			
			
			
		}

		
	}
	public void render()
	{		
		Global.renderer.drawColor(Color.argb(255, 0, 0, 200));
		
		rootPanel.render();
		
		switch(currentState)
		{
		case Root:			
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
			break;
			
		case CharStatus:
			if(charStatusGrow.Opened() || nextChar != null)
			{
				charStatus.render();
				
				backButton.render();
				charStatusAbilities.render();
				charStatusLabel.render();
				
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
				
			break;
			
		case SkillSelectChar:	
		case EquipSelectChar:			
			rootBarPanel.render();
			rootMenu.render();			
			renderDark();			
			rootCharPlates.render();
			rootInfoBar.render();
			
			if(!sideBar.Closed())
				sideBar.render();
		
			break;
			
		case Equip:
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
			break;
			
		case SkillSelect:
			if(sideBar.Opened() || nextChar != null)
			{
				
				skillsCharPanel.render();
				skillsList.render();
				skillsBackButton.render();

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
			break;
			
		case ItemSelect:			
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
			break;
			
		case Options:
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
			break;
			
		case OptionFrameColor:
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
				
			break;
			
		case ItemSort:
			invInfoBar.render();
			invList.render();
			renderDark();
			invSort.render();
			break;
			
		case ItemUse:
			invInfoBar.render();
			invList.render();
			renderDark();
			charUseScreen.render();
			if(charUseScreen.Opened())
			{
				charUseInfo.render();
				charUseDesc.render();
			}
				
			break;			
			
		}
		
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
		switch(currentState)
		{
		case CharStatus:
		case SkillSelect:
		case Equip:
			List<Character> charList = Global.party.getPartyList(false);
			if(charList.size() > 1 && Math.abs(velocityY) < 300 && Math.abs(velocityX) > 300)
			{
				undarken();
				equipItemType = null;
				
				//determine current index
				int i = 0;
				for(Character c : charList)
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
			break;
		}
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
			{
				switch(currentState)
				{
				case Root:
					sideBar.setClosed();				
					close();
					break;				
				
				case Equip:	
				case SkillSelect:
				case CharStatus:
				case SkillSelectChar:
				case EquipSelectChar:
				case Options:
				case ItemSelect:
					changeState(menuStates.Root);
					break;
					
				case OptionFrameColor:
					changeState(menuStates.Options);
					break;
					
				case ItemUse:				
				case ItemSort:
					changeState(menuStates.ItemSelect);
					break;

				}
			}			
		}
		
		
	}
	public void touchActionMove(int x, int y)
	{
		if(!close)
		{
			if(messageBox.Opened())
			{
				messageBox.touchActionMove(x, y);
			}
			else
			{
				switch(currentState)
				{
				case Root:
					rootMenu.touchActionMove(x, y);
				case SkillSelectChar:
				case EquipSelectChar:
					rootCharPlates.touchActionMove(x, y);
					break;
				case ItemSelect:
					invInfoBar.touchActionMove(x, y);
					invList.touchActionMove(x, y);
					break;
				case CharStatus:
					backButton.touchActionMove(x, y);
					charStatusAbilities.touchActionMove(x, y);
					break;
				case SkillSelect:
					skillsBackButton.touchActionMove(x, y);
					skillsList.touchActionMove(x, y);
					break;
				case Options:
					opts.touchActionMove(x, y);
					break;
					
				case OptionFrameColor:
					npRed.touchActionMove(x, y);
					npGreen.touchActionMove(x, y);
					npBlue.touchActionMove(x, y);
					optsBackButton.touchActionMove(x, y);
					break;				
					
				case Equip:
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
					
					break;
				case ItemSort:
					invSort.touchActionMove(x, y);
					break;
				case ItemUse:
					charUseScreen.touchActionMove(x, y);
					break;
				}
			}
			
		}
		
	}
	public void touchActionUp(int x, int y)
	{
		ListBox.LBStates state;
		MenuPanel selectedPanel;
		
		if(!close)
		{
			if(messageBox.Opened())
			{
				messageBox.touchActionUp(x, y);
				
				if(messageBox.isYesNo() && messageBox.getSelectedOpt() == YesNo.Yes)
				{
					//switch states and make a decision
					switch(currentState)
					{
					case Root:
						Global.restartGame();
						break;
					}
				}
				
				if(!messageBox.Opened())
					undarken();
			}
			else
			{
				switch(currentState)
				{
				case Root:
					state = rootMenu.touchActionUp(x, y);
					if(state == LBStates.Selected)
						handleOption((String)rootMenu.getSelectedEntry().obj);
					
					selectedPanel = rootCharPlates.touchActionUp(x, y);				
					//update party order based on charplates order
					for(int i = 0; i < 4; ++i) Global.party.partyMembers[i] = (Character)rootCharPlates.panels[i].obj;
					if(selectedPanel != null)
					{
						selectedChar = (Character)selectedPanel.obj;
						updateCharStatus();					
						changeState(menuStates.CharStatus);
					}
					break;
					
				case SkillSelectChar:
				case EquipSelectChar:
					selectedPanel = rootCharPlates.touchActionUp(x, y);	
					//update party order based on charplates order
					for(int i = 0; i < 4; ++i) Global.party.partyMembers[i] = (Character)rootCharPlates.panels[i].obj;
					//open equip screen with selected character
					if(selectedPanel != null)
					{
						selectedChar = (Character)selectedPanel.obj;
						changeState(currentState == menuStates.EquipSelectChar ? menuStates.Equip : menuStates.SkillSelect);
					}
					break;	
					
				case SkillSelect:
					if(skillsBackButton.touchActionUp(x, y) == LBStates.Selected)
						changeState(menuStates.Root);
					else
					{
						skillsList.touchActionUp(x, y);
					}
					
					break;
				case Options:
					if(opts.touchActionUp(x, y) == LBStates.Selected)
						handleOptOption((String)opts.getSelectedEntry().obj);
					break;
					
				case OptionFrameColor:
					npRed.touchActionUp(x, y);
					npGreen.touchActionUp(x, y);
					npBlue.touchActionUp(x, y);
					if(optsBackButton.touchActionUp(x, y) == LBStates.Selected)
						changeState(menuStates.Options);
					break;				
					
				case CharStatus:
					state = backButton.touchActionUp(x, y);
					if(state == LBStates.Selected)
						changeState(menuStates.Root);
					else
					{
						charStatusAbilities.touchActionUp(x, y);
					}				
					break;
					
				case ItemSelect:
					
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
							if(i.getType() != Type.Usable)
							{
								List<String> charNames = new ArrayList<String>();
								Character c;
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
								changeState(menuStates.ItemUse);
							}
								
						}
					}
					break;
					
				case Equip:	
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
							}
						}
						else
						{
							state = eqpInfoBar.touchActionUp(x, y);				
							if(state == LBStates.Selected)
							{
								handleEqpOption((String)eqpInfoBar.getSelectedEntry().obj);
								break;
							}
							
							state = eqpEquipSlots.touchActionUp(x, y);
							if(state == LBStates.Selected)
							{
								equipItemType = (Item.Type)eqpEquipSlots.getSelectedEntry().obj;
								darken();
								fillEqpSelect();
								//handleEqpOption((String)eqpInfoBar.getSelectedEntry().obj);
								break;
							}
						}
						
					}
					
					break;
					
				case ItemSort:
					state = invSort.touchActionUp(x, y);
					
					switch(state)
					{
					case Close:
						changeState(menuStates.ItemSelect);
						break;
					case Selected:
						handleInvSortOption((String)invSort.getSelectedEntry().obj);
						break;
					}
					break;
					
				case ItemUse:				
					state = charUseScreen.touchActionUp(x, y);
					
					switch(state)
					{
					case Close:
						changeState(menuStates.ItemSelect);
						break;
					case Selected:
						useItem((Character)charUseScreen.getSelectedEntry().obj);
						
						
						break;
					}
					break;
				}
			}
		}
	}
	public void touchActionDown(int x, int y)
	{
		if(!close)
		{
			if(messageBox.Opened())
			{
				messageBox.touchActionDown(x, y);
			}				
			else
			{
				switch(currentState)
				{
				case Root:
					rootMenu.touchActionDown(x, y);
				case SkillSelectChar:
				case EquipSelectChar:
					rootCharPlates.touchActionDown(x, y);
					break;
				case SkillSelect:
					skillsBackButton.touchActionDown(x, y);
					skillsList.touchActionDown(x, y);
					break;
				case Options:
					opts.touchActionDown(x, y);
					break;
					
				case OptionFrameColor:
					npRed.touchActionDown(x, y);
					npGreen.touchActionDown(x, y);
					npBlue.touchActionDown(x, y);
					optsBackButton.touchActionDown(x, y);
					break;
					
				case ItemSelect:
					invInfoBar.touchActionDown(x, y);
					invList.touchActionDown(x, y);					
					break;
				case CharStatus:
					backButton.touchActionDown(x, y);
					charStatusAbilities.touchActionDown(x, y);
					break;
				case Equip:
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
					break;
				case ItemSort:
					invSort.touchActionDown(x, y);
					break;
				case ItemUse:
					charUseScreen.touchActionDown(x, y);
					break;
				}
			}

			
		}
	}
	
	
	private enum menuStates
	{
		Root,
		ItemSelect,
		ItemSort,
		ItemUse,
		Equip,
		EquipSelectChar,
		SkillSelect,
		SkillUse,
		SkillSelectChar,
		CharStatus,
		Options,
		OptionFrameColor,
		Save
	}

}
