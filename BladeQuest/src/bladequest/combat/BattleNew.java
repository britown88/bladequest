package bladequest.combat;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Point;
import bladequest.UI.ListBox;
import bladequest.UI.MenuPanel;
import bladequest.UI.MenuPanel.Anchors;
import bladequest.graphics.BattleSprite.faces;
import bladequest.statuseffects.StatusEffect;
import bladequest.world.Character;
import bladequest.world.Encounter;
import bladequest.world.Enemy;
import bladequest.world.Global;
import bladequest.world.TargetTypes;

public class BattleNew 
{	
	private final int frameMaxHeight = 96;
	private final int frameMinHeight = 32;
	private final int partyFrameBuffer = 32;
	private final int advanceDistance = 32;
	private final int charMoveSpeed = 7;
	private final Point partyPos = new Point(Global.vpWidth - 128, 0);
	private final int statFrameBuffer = 16;
	private final int charYSpacing = 64;
	private final int mpWindowHeight = 32;
	private final int mpWindowWidth = 128;
	
	private final String txtStart = "Tap screen to start!";
	private final String txtTargetSingle = "Select target...";
	private final String txtTargetSingleAlly = "Select ally...";
	private final String txtTargetSingleEnemy = "Select enemy...";
	private final String txtTargetEnemies = "Targeting all enemies...";
	private final String txtTargetAllies = "Targeting all Allies...";
	private final String txtTargetSelf = "Targeting self.";
	private final String txtDefeat = "Annihilated...";
	
	private int selCharX;
	private boolean selCharOpening;
	private boolean selCharOpened;
	private boolean selCharClosed;
	private boolean nextChar;
	private boolean prevChar;
	
	private Character currentChar;
	private int currentCharIndex;
	private TargetTypes targetType;
	
	private BattleStates state;
	private Encounter encounter;

	private List<Character> partyList;
	
	//gfx
	private Paint selectPaint;
	private Paint statsText, nameText, battleText, grayBattleText, enemyBattleText,nameDisplayPaint;
	
	//menu panels
	private MenuPanel startBar, infoPanel, displayNamePanel, mpWindow;
	private MenuPanel characterPanes[];
	private ListBox mainMenu;
	
	public BattleNew()
	{
		//constructor
	}
	
	public void startBattle(String encounter)
	{
		this.state = BattleStates.START;
		this.encounter = new Encounter(Global.encounters.get(encounter));
		
		partyList = Global.party.getPartyList(false);
		
		//determine first nondead character
		for(int i = 0; i < partyList.size(); ++i)
			if(!partyList.get(i).isDead())
			{
				currentCharIndex = i;
				currentChar = partyList.get(currentCharIndex);
				break;
			}
		
		//all characters are dead (this should never happen)
		if(currentChar == null)
			changeState(BattleStates.DEFEAT);
		else
		{
			//characters
			Character[] party = Global.party.getPartyMembers(false);
			for(int i = 0; i < 4; ++i)
				if(party[i] != null)
				{
					party[i].setIndex(i);
					party[i].genWeaponSwing();
					party[i].setFace(faces.Idle);
				}
			
			//init
			recedeChar();	
			
			//GFX
			Global.map.getBackdrop().load();
			buildPaints();
			
			//UI
			initUI();		
			update();
		}		
	}
	
	private void initUI()
	{
		buildCharacterPanes();
		buildPanels();
		buildMainMenu();
		
	}
	private void buildCharacterPanes()
	{
		characterPanes = new MenuPanel[4];
		for(int i = 0; i < 4; ++i)
		{
			characterPanes[i] = new MenuPanel(
					partyPos.x + 64, 
					charYSpacing*i + statFrameBuffer,
					Global.vpWidth - (partyPos.x + 64),
					charYSpacing - statFrameBuffer);
			characterPanes[i].thickFrame = false;
		}
	}
	private void buildPanels()
	{		
		startBar = new MenuPanel(0, Global.vpHeight, partyPos.x - partyFrameBuffer, frameMinHeight);
		startBar.addTextBox(txtStart, 5, frameMinHeight/2, battleText);
		startBar.anchor = Anchors.BottomLeft;
		startBar.thickFrame = false;
		
		infoPanel = new MenuPanel(0, Global.vpHeight, Global.vpWidth, frameMinHeight);
		infoPanel.addTextBox("", 5, frameMinHeight/2, battleText);
		infoPanel.anchor = Anchors.BottomLeft;
		infoPanel.thickFrame = false;
		infoPanel.hide();
		
		displayNamePanel = new MenuPanel();
		displayNamePanel.thickFrame = false;
		displayNamePanel.setInset(-10, -7);
		displayNamePanel.addTextBox("", 0, 0, nameDisplayPaint);	
		displayNamePanel.hide(); 
		
		mpWindow = new MenuPanel(0, Global.vpHeight - frameMaxHeight-mpWindowHeight, mpWindowWidth, mpWindowHeight);
		mpWindow.thickFrame = false;
		mpWindow.addTextBox("", 5, mpWindowHeight/2, battleText);
		mpWindow.hide();
	}
	private void buildMainMenu()
	{
		mainMenu = new ListBox(0, Global.vpHeight, partyPos.x - partyFrameBuffer, frameMinHeight, 3, 2, battleText);
		mainMenu.setOpenSize(mainMenu.width, frameMaxHeight);
		mainMenu.setDisabledPaint(grayBattleText);
		mainMenu.anchor = Anchors.BottomLeft;
		mainMenu.thickFrame = false;
		mainMenu.hideOnClosed = true;
	}
	private void buildPaints()
	{		
		selectPaint = new Paint();
		selectPaint.setARGB(255, 255, 255, 255);
		selectPaint.setStyle(Paint.Style.STROKE);
		selectPaint.setStrokeWidth(4);
		selectPaint.setStrokeCap(Cap.ROUND);
		selectPaint.setAntiAlias(true);
		
		statsText = Global.textFactory.getTextPaint(8, Color.WHITE, Align.LEFT);
		nameText = Global.textFactory.getTextPaint(10, Color.WHITE, Align.LEFT);
		battleText = Global.textFactory.getTextPaint(13, Color.WHITE, Align.LEFT);
		grayBattleText = Global.textFactory.getTextPaint(13, Color.GRAY, Align.LEFT);
		enemyBattleText = Global.textFactory.getTextPaint(13, Color.WHITE, Align.CENTER);
		nameDisplayPaint = Global.textFactory.getTextPaint(13, Color.WHITE, Align.CENTER);
		
	}
	
	private void updateCharacterPanes()
	{		
		for(Character c : partyList)
		{
			int i = c.Index();
			
			characterPanes[i].clear();			
			
			characterPanes[i].addTextBox(c.getDisplayName(), 5, (int)((charYSpacing - statFrameBuffer)*0.25f), nameText);
			characterPanes[i].addTextBox("HP:" + c.getHP(), 7, (int)((charYSpacing - statFrameBuffer)*0.50f), statsText);
			characterPanes[i].addTextBox("MP:" + c.getMP(), 7, (int)((charYSpacing - statFrameBuffer)*0.75f), statsText);
			
			float iconScale = 1.5f;
			int j = 0, d = (int)(Global.iconSize*iconScale + 2);
			for(StatusEffect se : c.getStatusEffects())
				if(j < 4 && se.icon().length() > 0)
					characterPanes[i].addPicBox(Global.createIcon(se.icon(),(d/2) +  d*j++,-(d/2),iconScale));
			
			
			characterPanes[i].update();
			
		}
	}
	private void updatePanels()
	{
		startBar.update();
		infoPanel.update();
		displayNamePanel.update();
		mpWindow.update();
		mainMenu.update();
	}
	private void updateCharacterPositions()
	{
		for(Character c : partyList)
			c.setPosition(partyPos.x, partyPos.y + (charYSpacing * c.Index()));
		
		currentChar.setPosition(partyPos.x-selCharX, partyPos.y + (charYSpacing * currentChar.Index()));
	}
	private void updateMenuOptions(BattleStates state)
	{
		switch(state)
		{
		case SELECT:
			mainMenu.clearObjects();
			mainMenu.addItem("Attack", "atk", false);
			mainMenu.addItem("Item", "itm", false);
			mainMenu.addItem(currentChar.getActionName(), "act", false);
			mainMenu.addItem("Guard", "grd", false);
			mainMenu.addItem(currentChar.getAbilitiesName(), "ab", false);
			mainMenu.addItem("Run", "run", false);			
			break;
		}
		
		mainMenu.update();
		
	}
	private void changeStartBarText(String str){startBar.getTextAt(0).text = str;}
	
	private void changeState(BattleStates newState)
	{
		switch(newState)
		{
		case START:
			recedeChar();
			mainMenu.close();
			changeStartBarText(txtStart);
			currentChar.setFace(faces.Idle);
			break;
		case SELECT:
			updateMenuOptions(newState);
			mainMenu.open();
			advanceChar();
			currentChar.setFace(faces.Idle);
			break;
			
		case TARGET:
			currentChar.setFace(faces.Ready);
			switch(targetType)
			{
			case AllAllies:
				changeStartBarText(txtTargetAllies);
				break;
			case AllEnemies:
				changeStartBarText(txtTargetEnemies);
				break;
			case Self:
				changeStartBarText(txtTargetSelf);
				break;
			case Single:
				changeStartBarText(txtTargetSingle);
				break;
			case SingleAlly:
				changeStartBarText(txtTargetSingleAlly);
				break;
			case SingleEnemy:
				changeStartBarText(txtTargetSingleEnemy);
				break;
			}
			changeStartBarText(txtTargetSingle);
			mainMenu.close();
			
		}
		
		state = newState;
		
	}
	private void handleMenuOption(String opt)
	{
		if(opt.equals("atk"))
		{
			targetType = TargetTypes.Single;
			changeState(BattleStates.TARGET);
		}
		else if(opt.equals("itm"))
		{
			changeState(BattleStates.START);
		}
		else if(opt.equals("act"))
		{
			changeState(BattleStates.START);
		}
		else if(opt.equals("grd"))
		{
			changeState(BattleStates.START);
		}
		else if(opt.equals("ab"))
		{
			changeState(BattleStates.START);
		}
		else if(opt.equals("run"))
		{
			changeState(BattleStates.START);
		}
	}
	private void advanceChar()
	{
		selCharClosed = false;
		selCharOpening = true;
	}
	private void recedeChar()
	{
		selCharOpened = false;
		selCharOpening = false;
	}
	private void handleCharAdvancing()
	{
		if(!selCharOpened && !selCharClosed)
		{
			if(selCharOpening)
			{
				selCharX += charMoveSpeed;
				if(selCharX >= advanceDistance)
				{
					selCharX = advanceDistance;
					selCharOpened = true;
				}
					
			}
			else
			{
				selCharX -= charMoveSpeed;
				if(selCharX <= 0)
				{
					selCharX = 0;
					selCharClosed = true;
				}
					
			}
		}
		
		
	}
	
	private void nextCharacter()
	{
		nextChar = true;
		recedeChar();
	}
	
	private void drawActors()
	{
		for(Character c : partyList)
		{
			c.battleRender();
			characterPanes[c.Index()].render();
		}
			
		
		for(Enemy e : encounter.Enemies())
			e.battleRender();
		
			
	}
	private void drawPanels()
	{		
		mainMenu.render();
		if(mainMenu.Closed())
			startBar.render();
		
		infoPanel.render();
		mpWindow.render();
		displayNamePanel.render();
	}
	
	public void update()
	{
		updatePanels();
		updateCharacterPanes();
		
		handleCharAdvancing();
		updateCharacterPositions();
		
		if(nextChar && selCharClosed)
		{
			if(currentCharIndex + 1 < partyList.size())
			{
				currentChar = partyList.get(++currentCharIndex);
				changeState(BattleStates.SELECT);
			}
			else
				changeState(BattleStates.ACT);
			
			nextChar = false;
		}
		
	}	
	public void render()
	{
		Global.map.getBackdrop().render();
		drawActors();
		drawPanels();
		
	}
	
	public void backButtonPressed()
	{
		
	}
	public void touchActionUp(int x, int y)
	{
		switch(state)
		{
		case START:
			changeState(BattleStates.SELECT);
			break;
		case SELECT:
			switch(mainMenu.touchActionUp(x, y))
			{
			case Selected:
				handleMenuOption((String)(mainMenu.getSelectedEntry().obj));
				break;
			case Close:
				changeState(BattleStates.START);
				break;
			default:
				break;
			}
		case TARGET:
			if(mainMenu.Closed())
			{
				if(startBar.contains(x, y))
					changeState(BattleStates.SELECT);
				else
				{
					nextCharacter();
				}
			}
			
			break;
		}
		
	}
	public void touchActionDown(int x, int y)
	{
		switch(state)
		{
		case SELECT:
			mainMenu.touchActionDown(x, y);
			break;
		}
	}
	public void touchActionMove(int x, int y)
	{
		switch(state)
		{
		case SELECT:
			mainMenu.touchActionMove(x, y);
			break;
		}
	}
	
	public enum BattleStates
	{
		START,
		SELECT,
		SELECTITEM,
		SELECTABILITY,
		TARGET,
		ACT,
		RUN,
		VICTORY,
		DEFEAT		
	}
	
}
