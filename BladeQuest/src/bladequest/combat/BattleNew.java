package bladequest.combat;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Point;
import android.graphics.Rect;
import bladequest.UI.ListBox;
import bladequest.UI.MenuPanel;
import bladequest.UI.MenuPanel.Anchors;
import bladequest.graphics.BattleSprite.faces;
import bladequest.statuseffects.StatusEffect;
import bladequest.world.Character;
import bladequest.world.Character.Action;
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
	private final String txtTargetAllies = "Targeting all allies...";
	private final String txtTargetSelf = "Targeting self...";
	private final String txtTargetEverybody = "Targeting everybody!";
	private final String txtDefeat = "Annihilated...";
	
	private int selCharX;
	private boolean selCharOpening;
	private boolean selCharOpened;
	private boolean selCharClosed;
	private boolean nextChar;
	private boolean prevChar;
	
	private long actTimerStart;
	private boolean nextActor;
	
	private Character currentChar;
	private int currentCharIndex;
	private TargetTypes targetType;
	private List<Character> targets;
	private List<BattleEvent> battleEvents;
	private List<DamageMarker> markers;
	
	private BattleStates state;
	private Encounter encounter;

	private List<Character> partyList;
	
	private Rect enemyArea;
	
	//gfx
	private Paint selectPaint;
	private Paint statsText, nameText, battleText, grayBattleText, enemyBattleText,nameDisplayPaint;
	
	//menu panels
	private MenuPanel startBar, infoPanel, displayNamePanel, mpWindow;
	private MenuPanel characterPanes[];
	private ListBox mainMenu;
	
	public BattleNew()
	{
		enemyArea = Global.vpToScreen(new Rect(0,0,partyPos.x-partyFrameBuffer, Global.vpHeight-frameMinHeight));
		targets = new ArrayList<Character>();
		battleEvents = new ArrayList<BattleEvent>();
		markers = new ArrayList<DamageMarker>();
	}
	
	public void startBattle(String encounter)
	{
		this.state = BattleStates.START;
		this.encounter = new Encounter(Global.encounters.get(encounter));
		
		partyList = Global.party.getPartyList(false);
		
		selectFirstChar();
		
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
	private void showDisplayName(String str)
	{
		displayNamePanel.show();		
		
		Rect displayNameRect = new Rect();
		nameDisplayPaint.getTextBounds(str, 0, str.length()-1, displayNameRect);
		displayNameRect = Global.screenToVP(displayNameRect);
		
		
		displayNamePanel.pos.x = (Global.vpWidth - displayNameRect.width())/2;
		displayNamePanel.pos.y = 0;
		displayNamePanel.width = displayNameRect.width();
		displayNamePanel.height = displayNameRect.height();
		displayNamePanel.update();
		
		displayNamePanel.getTextAt(0).text = str;
		displayNamePanel.getTextAt(0).x = displayNamePanel.insetWidth()/2;
		displayNamePanel.getTextAt(0).y = displayNamePanel.insetHeight()/2;

	}
	private void updateDamageMarkers()
	{
		List<DamageMarker> delete = new ArrayList<DamageMarker>();
		
		for(DamageMarker dm : markers)
			if(dm.isDone())
				delete.add(dm);
			else
				dm.update();
		
		for(DamageMarker dm : delete)
			markers.remove(dm);		
		
	}
	
	//act state functions
	private void initActState()
	{		
		//add enemy actions to event queue
		for(Enemy e : encounter.Enemies())
			if(!e.isDead())
				battleEvents.add(e.genBattleEvent(partyList, encounter.Enemies()));
		
		for(Character c : partyList)
			if(!c.isDead())
				addBattleEvent(c, c.getTargets());
		
		battleEvents = BattleCalc.genMoveOrder(battleEvents);
		
		for(BattleEvent be : battleEvents)
			be.init();

	}
	private void updateActStatus()
	{
		if(nextActor)
		{
			if(selCharClosed)
				nextActor();
		}
		else
		{
			BattleEvent currentEvent = battleEvents.get(0);
			Character actor = currentEvent.getSource();
			
			//set frame text
			switch(actor.getAction())
			{case Attack:changeStartBarText(actor.getDisplayName()+" attacks!");break;
			case Item:changeStartBarText(actor.getDisplayName()+" uses "+actor.getItemToUse().getName()+"!");break;
			case Ability:changeStartBarText(actor.getDisplayName()+" casts "+actor.getAbilityToUse().getDisplayName()+"!");break;
			case CombatAction:changeStartBarText(actor.getDisplayName()+actor.getCombatActionText());break;
			default: break;}
			
			if(actor.isEnemy() || selCharOpened)
			{
				currentEvent.update(this, markers);
				if(currentEvent.isDone())
					nextActorInit();
			}	
			else if(!actor.isEnemy())
			{
				currentChar = actor;
				advanceChar();
			}
				
		}			
	}
	
/*	if(actor.isEnemy())
	{
		//enemy action code
		if(!actor.acting)
		{
			actTimerStart = System.currentTimeMillis();							
			actor.acting = true;
			setTarget(actor);
			if(actor.getAction() == Action.Ability)
				showDisplayName(actor.getAbilityToUse().getDisplayName());
		}
		else
		{
			int index = (int)(System.currentTimeMillis() - actTimerStart)/actTimerLength;
			
			switch(actor.getAction())
			{
			case Attack:
				if(index == 3)
				{
					((Enemy)actor).playAttackAnimation(actor.getPosition(true), targets.get(0).getPosition(true));
					
				}							
				else if(index > 7)
				{
					this.targets.clear();
					nextActorInit();
				}
				break;
			case Ability:
				if(index > 7)
				{
					this.targets.clear();
					displayNamePanel.hide();
					nextActorInit();
				}
				break;
			default:
				nextActorInit();
				break;
			}					
		}				
	}
	else
	{
		//player action code
		currentChar = actor;
		
		if(selCharOpened)
		{					
			if(!actor.acting)
			{
				actTimerStart = System.currentTimeMillis();							
				actor.acting = true;
			}
			else
			{
				switch(actor.getAction())
				{
				case Attack:
					int index = (int)(System.currentTimeMillis() - actTimerStart)/actTimerLength;						
					index -= 2;
					
					//add 2 frames of no movement before attacking and 1 frame after							
					if(index < 0)
					{
						actor.setFace(faces.Ready);
						actor.setImageIndex(0);
					}							
					else if(index >= 0 && index < 3)//<3
					{
						actor.setFace(faces.Attack);
						actor.setImageIndex(index);
						if(index == 1)
							actor.playWeaponAnimation(actor.getPosition(true), targets.get(0).getPosition(true));
					}								
					else if(index == 3)
					{
						actor.setFace(faces.Ready);
						actor.setImageIndex(0);
					}
					else if(index > 5)
						nextActorInit();
					break;
				case Ability:
				case CombatAction:
				case Guard:
				case Item:
					break;
				}					
			}
			
		}
		else if(selCharClosed)
			advanceChar();
	}*/
	
	private void nextActorInit()
	{
			nextActor = true;
			//battleEvents.get(0).getSource().acting = false;
			Character actor = battleEvents.get(0).getSource();			
			if(!actor.isEnemy())
			{
				recedeChar();
				actor.setFace(faces.Idle);
				actor.setImageIndex(0);
			}
	}
	private void nextActor()
	{		
		nextActor = false;		
		battleEvents.remove(0);		
		targets.clear();
		if(battleEvents.size() == 0)
		{
			selectFirstChar();
			if(currentChar == null)
				changeState(BattleStates.DEFEAT);
			else
				changeState(BattleStates.START);
		}
		else if(!battleEvents.get(0).getSource().isEnemy())
			advanceChar();		
	}
	
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
		case ACT:
			mainMenu.close();
			initActState();
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
			case Everybody:
				changeStartBarText(txtTargetEverybody);
				break;
			}
			mainMenu.close();			
		}
		
		state = newState;
		
	}
	private void handleMenuOption(String opt)
	{
		if(opt.equals("atk"))
		{
			targetType = TargetTypes.Single;
			currentChar.setBattleAction(Action.Attack);
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
	private void selectFirstChar()
	{
		currentChar = null;
		
		//determine first nondead character
		for(int i = 0; i < partyList.size(); ++i)
			if(!partyList.get(i).isDead())
			{
				currentCharIndex = i;
				currentChar = partyList.get(currentCharIndex);
				break;
			}
	}
	private void postDamageMarker(int value, Character target){markers.add(new DamageMarker(value, target));}
	private void postDamageMarker(String value, Character target){markers.add(new DamageMarker(value, target));}
	private void addBattleEvent(Character source, List<Character> targets){battleEvents.add(new BattleEvent(source, targets));}
	
	private void nextCharacter()
	{
		//changeState(BattleStates.SELECT);
		//mainMenu.close();		
		recedeChar();
		nextChar = true;
	}
	private void previousCharacter()
	{
		if(selCharOpened)
		{
			if(currentCharIndex == 0)
				changeState(BattleStates.START);
			else
			{			
				recedeChar();
				prevChar = true;
				
			}
			
		}
		
		
	}
	private void handleNextPrev()
	{
		if((nextChar || prevChar) && selCharClosed)
		{
			if(prevChar)
			{
				currentChar = partyList.get(--currentCharIndex);
				changeState(BattleStates.SELECT);
				
				//TODO: unuse unused items
			}
			else if(currentCharIndex + 1 < partyList.size())
			{
				currentChar = partyList.get(++currentCharIndex);
				changeState(BattleStates.SELECT);
			}
			else
				changeState(BattleStates.ACT);
			
			nextChar = prevChar = false;
		}
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
	private void drawSelect()
	{
		for(Character t : targets)
		{
			Global.renderer.drawRect(t.getRect(), selectPaint, true);
			
			//draw enemy names
			if(t.isEnemy())
			{
				Rect eRect = t.getRect();//HAHA AINT I FUNNY				
				boolean drawTop = Global.vpToScreenY(eRect.top - (int)(enemyBattleText.getTextSize()/2)-4)> 0;
				float drawY = drawTop ? 
						eRect.top - enemyBattleText.getTextSize()/2 - 4 : 
						eRect.bottom + enemyBattleText.getTextSize()/2-4;					
				Global.renderer.drawText(t.getDisplayName(), eRect.centerX(), drawY, enemyBattleText);
				
			}
		}
			

	}
	private void drawDamageMarkers()
	{
		for(DamageMarker dm : markers)
			dm.render();
	}
	
	public void update()
	{
		updatePanels();
		updateCharacterPanes();
		
		handleCharAdvancing();
		updateCharacterPositions();
		updateDamageMarkers();
		
		handleNextPrev();
		
		switch(state)
		{
		case ACT:
			updateActStatus();
			break;
		}
		
	}	
	public void render()
	{
		Global.map.getBackdrop().render();
		drawActors();
		drawPanels();
		drawSelect();
		
		Global.renderAnimations();
		drawDamageMarkers();
		
	}
	
	public void backButtonPressed()
	{
		switch(state)
		{
		case TARGET:
			if(selCharOpened)
				changeState(BattleStates.SELECT);
			break;
		case SELECT:
			previousCharacter();
			break;
				
		}
		
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
					if(targets.size() > 0)
					{
						//targets were selected
						currentChar.setTargets(new ArrayList<Character>(targets));
						currentChar.setFace(faces.Ready);
						nextCharacter();
						targets.clear();
					}
					else
						changeState(BattleStates.SELECT);
					
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
		case TARGET:
			if(mainMenu.Closed())
				getTouchTargets(x, y);
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
		case TARGET:
			if(mainMenu.Closed())
				getTouchTargets(x, y);
			break;
			
		}
	}
	private void getTouchTargets(int x, int y)
	{
		targets.clear();		
		switch(targetType)
		{
		case Single:
			if(enemyArea.contains(x, y))
				targetEnemy(x, y);
			else
				targetAlly(x, y);
			break;
		case SingleEnemy:
			if(enemyArea.contains(x, y))
				targetEnemy(x, y);
			break;
		case SingleAlly:
			targetAlly(x, y);
			break;
		case Self:
			targets.add(currentChar);
			break;
		case AllAllies:
			for(Character c : partyList)
				targets.add(c);
			break;
		case AllEnemies:
			for(Enemy e : encounter.Enemies())
				if(!e.isDead())
					targets.add(e);
			break;
		case Everybody:
			for(Enemy e : encounter.Enemies())
				if(!e.isDead())
					targets.add(e);
			for(Character c : partyList)
				targets.add(c);
			break;
			
		}		

	}
	//add closest enemy; assumes tapped in enemy area
	private void targetEnemy(int x, int y)
	{
		int lowestDist = Math.abs(enemyArea.top - enemyArea.bottom) + Math.abs(enemyArea.left - enemyArea.right);
		Enemy closest = null;
		for(Enemy e : encounter.Enemies())
		{
			if(!e.isDead())
			{
				Point pos = Global.vpToScreen(e.getPosition());
				int dist = Math.abs(pos.x - x) + Math.abs(pos.y - y);
				if(dist < lowestDist) {lowestDist = dist;closest = e;}
			}			
		}
		
		targets.add(closest);
	}	
	//target closest ally, assumes tapping within char rect
	private void targetAlly(int x, int y)
	{
		for(Character c : partyList)
			if(c.getRect().contains(x, y))
			{
				targets.add(c);
				break;
			}
	}
	private void setTarget(Character c)
	{
		targets.clear();
		targets.add(c);
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
