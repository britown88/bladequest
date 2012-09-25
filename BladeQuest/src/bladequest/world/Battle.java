package bladequest.world;

import android.graphics.*;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import bladequest.UI.*;
import bladequest.UI.MenuPanel.Anchors;
import bladequest.graphics.BattleSprite;
import bladequest.graphics.BattleSprite.faces;
import bladequest.statuseffects.StatusEffect;
import bladequest.world.Character.Action;

import java.util.*;


public class Battle {
	private final int frameMaxHeight = 96;
	private final int frameMinHeight = 32;
	private final int partyFrameBuffer = 32;
	private final int advanceDistance = 32;
	private final int charMoveSpeed = 7;
	private final Point partyPos = new Point(Global.vpWidth - 128, 0);	
	
	private Rect enemyFrame;
	
	private String defaultFrameText;
	
	private ListBox mainMenu;
	private MenuPanel infoWindow, mpWindow, displayNamePanel;	
	private MenuPanel charStats[];	
		
	private static int statFrameBuffer = 16;
	private static int charYSpacing = 64;
	
	private Paint selectPaint;
	private Paint statsText, nameText, battleText, grayBattleText, enemyBattleText;
	
	boolean endBattleFlag;
	
	private Vector<DamageMarker> markers;
	private String frameText;
	
	
	private int currentChar;
	private Enemy selectedEnemy;
	private boolean showEnemySelect;
	private battleStates currentState;
	private boolean showCharSelect;
	private Character charSelect;
	
	private Encounter encounter;
	public Vector<String> messageQueue;
	
	private int selCharX;
	private boolean selCharOpening;
	private boolean selCharOpened;
	private boolean selCharClosed;
	private boolean nextChar;
	private boolean prevChar;	
	
	private Vector<Character> moveOrder;
	private int currentActor;
	private boolean acting;
	
	private int actTimer;
	private int actionLength;
	
	private TargetTypes targetType;
	private boolean selectAllEnemies;
	private boolean selectAllAllies;
	
	private boolean attackMissed, attackCritical;


	//private boolean showDisplayName = false;
	private Paint nameDisplayPaint;
	//private Rect displayNameRect;
	//private String displayName;
	
	
	private Ability runningAbility;
	private int messagequeuetimer;
	
	
	//constructor
	public Battle()
	{
		messageQueue = new Vector<String>();
		markers= new Vector<DamageMarker>();
		moveOrder = new Vector<Character>();
		
		actTimer = 0;
		actionLength = 30;
		
		currentActor = 0;
		acting = false;
		
		
		
		enemyFrame = new Rect(Global.vpToScreen(new Rect(0,0,256, 256)));
		
		showEnemySelect = false;
		showCharSelect = false;
		endBattleFlag = false;	
		
		currentState = battleStates.START;
		currentChar = 0;
		selCharX = 0;
		selCharOpened = false;
		selCharClosed = true;
		nextChar = false;
		prevChar = false;
		
		//set party's
		int i = 0;
		for(Character c : Global.party.getPartyMembers(false))
		{
			if(c != null)
			{
				c.clearTargets();
				c.setIdle(false);			
				c.setIndex(i);
				c.genWeaponSwing();
			}
			i++;
			
		}
		
		buildPaints();
		initUI();
		//buildOptionRects();
		
		//contains the area covered by the main menu frame		
		
		
		
	}
	
	private void initUI()
	{
		mainMenu = new ListBox(new Rect(0, Global.vpHeight - frameMaxHeight, partyPos.x - partyFrameBuffer, Global.vpHeight), 3, 2, battleText);
		//listMenu = new ListBox(frameArea, 3, 2, battleText);
		mainMenu.setDisabledPaint(grayBattleText);
		mainMenu.thickFrame = false;
		
		infoWindow = new MenuPanel(0, Global.vpHeight, partyPos.x - partyFrameBuffer, frameMinHeight);
		infoWindow.thickFrame = false;
		infoWindow.addTextBox(frameText, 5, frameMinHeight/2, battleText);
		infoWindow.anchor = Anchors.BottomLeft;
		infoWindow.setOpenSize(infoWindow.width, frameMaxHeight);
		
		
		final int mpWindowHeight = 32;
		final int mpWindowWidth = 128;		
		
		mpWindow = new MenuPanel(0, Global.vpHeight - frameMaxHeight-mpWindowHeight, mpWindowWidth, mpWindowHeight);
		mpWindow.thickFrame = false;
		mpWindow.addTextBox("", 5, mpWindowHeight/2, battleText);
		
		displayNamePanel = new MenuPanel();
		displayNamePanel.thickFrame = false;
		displayNamePanel.setInset(-10, -7);
		displayNamePanel.addTextBox("", 0, 0, nameDisplayPaint);	
		displayNamePanel.hide();
		
		updateFrames();	
		
		charStats = new MenuPanel[4];
		for(int i = 0; i < 4; ++i)
		{
			charStats[i] = new MenuPanel(
					partyPos.x + 64, 
					charYSpacing*i + statFrameBuffer,
					Global.vpWidth - (partyPos.x + 64),
					charYSpacing - statFrameBuffer);
			charStats[i].thickFrame = false;
		}
		
		updateCharStats();
		
	}
	
	private void updateCharStats()
	{
		
		for(int i = 0; i < 4; ++i)
		{
			Character c = Global.party.getPartyMembers(false)[i];
			
			if(c != null)
			{
				charStats[i].clear();			
				
				charStats[i].addTextBox(c.getDisplayName(), 5, (int)((charYSpacing - statFrameBuffer)*0.25f), nameText);
				charStats[i].addTextBox("HP:" + c.getHP(), 7, (int)((charYSpacing - statFrameBuffer)*0.50f), statsText);
				charStats[i].addTextBox("MP:" + c.getMP(), 7, (int)((charYSpacing - statFrameBuffer)*0.75f), statsText);
				
				float iconScale = 1.5f;
				int j = 0, d = (int)(Global.iconSize*iconScale + 2);
				for(StatusEffect se : c.getStatusEffects())
					if(j < 4 && se.icon().length() > 0)
						charStats[i].addPicBox(Global.createIcon(se.icon(),(d/2) +  d*j++,-(d/2),iconScale));
				
				
				charStats[i].update();	
			}
					
		}
	}
	
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
	
	public void setFrameText(String text)
	{
		frameText = text;
	}
	
	
	//public methods
	public void setEncounter(String str)
	{
		encounter = new Encounter(Global.encounters.get(str));
	}
	public void backButtonPressed()
	{
		switch(currentState)
		{
		case SELECT:
			prevChar = true;
			infoWindow.close();
			recedeChar();	
			Global.playSound("denied1");
			break;
			
		case TARGET:
			
			recedeChar();
			getChar(currentChar).setIdle(false);
			selectAllAllies = false;
			selectAllEnemies = false;
			showCharSelect = false;
			showEnemySelect = false;
			
			if(getChar(currentChar).action == Action.Item)
				getChar(currentChar).unuseItem();
				
			changeState(battleStates.SELECT);	
			Global.playSound("denied1");
			break;
			
		case SELECTABILITY:			
		case SELECTITEM:			
			changeState(battleStates.SELECT);
			Global.playSound("denied1");
			break;
		}
		
	}
	public void initBattle()
	{
		Global.GameState = States.GS_BATTLE;
		defaultFrameText = "Tap screen to start!";	
		frameText = defaultFrameText;
		Global.map.getBackdrop().load();
		
		
	}
	
	private void triggerEndBattle()
	{
		
		endBattleFlag = true;
		fadeStarted = false;
		defaultFrameText = "";
		infoWindow.drawContent = true;

	}
	
	private boolean fadeStarted;
	
	public battleStates getState() { return currentState; }
	
	
	private void endBattle()
	{
		if(!fadeStarted && !Global.screenFader.fadingOut())
		{
			fadeStarted = true;
			Global.screenFader.setFadeColor(255, 0, 0, 0);
			Global.screenFader.fadeOut(3);
		}
		if(Global.screenFader.isDone() && fadeStarted)	
		{
			Global.map.getBackdrop().unload();
			Global.musicBox.resumeLastSong();
			Global.GameState = States.GS_WORLDMOVEMENT;
			
			if(getAliveCharacters().size() <= 0)
			{				
				if(Global.BattleStartObject != null)
					Global.BattleStartObject.clearActions();
				//Global.restartGame();
				Global.party.allowMovement = true;
				Global.setPanned(0, 0);				
				Global.map.gameOverObject.execute();
			}
				
			else
			{
				Global.screenFader.fadeIn(8);
			}
		}

		
		
			
		//global.party.clearMovementPath();
	}
	
	public void addMessage(String msg)
	{
		messagequeuetimer = 0;
		messageQueue.add(msg);
	}
	
	public void render()
	{	
		Global.map.getBackdrop().render();
		
		//render fader under everything during battle
		if(!endBattleFlag)
			Global.screenFader.render();
		
		drawEnemies();
		drawFrame();
		drawCharacters();	

		Global.renderAnimations();
		
		for(DamageMarker d : markers)
			if(d.isShown())
				d.render();
		
		displayNamePanel.render();
		
		//render fader over everything at battle's end
		if(endBattleFlag)
			Global.screenFader.render();
		
		
		
	}
	public void update()
	{	
		
		//update UI
		mpWindow.update();
		mainMenu.update();
		infoWindow.update();
		displayNamePanel.update();
		
		updateCharStats();
		
		if(runningAbility != null && runningAbility.running)
		{
			runningAbility.update();
			if(!runningAbility.running)
				runningAbility = null;
		}
		
		if(messageQueue.size() > 0)
		{
			messagequeuetimer++;
			frameText = messageQueue.get(0);
			if((currentState == battleStates.USEITEM) 
					&& messagequeuetimer > 50)
				nextMessage();
				

			//handleFrameOpenClose();
			handleCharAdvancing();
			updateMarkers();
		}			
		else
		{			
			if(endBattleFlag)
			{
				//markers.clear();
				endBattle();
				return;
			}
			
			//handleFrameOpenClose();
			handleCharAdvancing();
			updateMarkers();
			
				
			
			switch(currentState)
			{
			case SELECT:
				if(selCharClosed)
				{
					if(nextChar)
						nextCharacter();
					
					if(prevChar)
						prevCharacter();
				}				
				break;
			case RUNFAIL:
				frameText = "Enemies attack!";
				
				
				genMoveOrder(moveOrderGroups.EnemiesOnly);
				setEnemyTargets();
				
				acting = false;
				currentActor = 0;
				
				currentState = battleStates.ACT;
				break;
				
			case USEITEM:
				if(markers.size() == 0)
				{
					if(runningAbility != null && runningAbility.running)
						break;
					else
					if(currentActor < moveOrder.size() && !moveOrder.get(currentActor).isEnemy())
					{
						moveOrder.get(currentActor).setIdle(true);
						recedeChar();
					}						
					changeState(battleStates.ACTWAIT);
				}
					
				break;
				
			case ACTWAIT:
				if(true)
				{
					recedeChar();
					displayNamePanel.hide();
					showEnemySelect = false;
					
					acting = false;
					actTimer = 0;
					
					if(getAliveCharacters().size() == 0)
						Annihilated();
					else
						if(getAliveEnemies().size() == 0)
							changeState(battleStates.VICTORY);
						else
						{
							if(!moveOrder.get(currentActor).isEnemy())
								moveOrder.get(currentActor).action = Character.Action.Guard;
							
							currentActor++;
							currentState = battleStates.ACT;
							if(currentActor >= moveOrder.size())
								endAct();
						}
				}
				break;
				
			case ACT:
				if(acting)
				{
					actTimer++;
					if(actTimer >= actionLength)
					{
						if(moveOrder.get(currentActor).isEnemy())
						{
							Character c = moveOrder.get(currentActor).getTargets().get(0);						
							Enemy e = (Enemy)moveOrder.get(currentActor);
							
							if(c.isDead())
							{
								e.clearTargets();
								setEnemyTarget(e);
							}
							else
							{
								switch(e.getAction())
								{
								case Attack:
									physicalDamage(e, c);
									changeState(battleStates.ACTWAIT);
									Point cPos = new Point(c.position);
									cPos.offset(c.battleSpr.getWidth()/2, c.battleSpr.getHeight()/2);
									Global.playAnimation("slice1", e.position, cPos);
									
									break;
								case Ability:
									runningAbility = e.useAbility();
									changeState(battleStates.USEITEM);
									break;								
								}
								
							}						
						}
						else
						{
							if(selCharOpened)
							{
								Character e = moveOrder.get(currentActor).getTargets().get(0);						
								Character c = moveOrder.get(currentActor);
								
								switch(c.getAction())
								{
								case Attack:
									if(c.isDone())
									{
										if(e.isDead())
										{
											c.clearTargets();
											resetTarget(c);
										}
										else
										{
											physicalDamage(c, e);
											Point cPos = new Point(c.position);
											cPos.offset(c.battleSpr.getWidth()/2, c.battleSpr.getHeight()/2);
											//Global.playAnimation("movetest", cPos, e.position);
											c.setIdle(true);
											recedeChar();
											changeState(battleStates.ACTWAIT);											
										}
									}
									else
										c.updateSwing();
										
									break;
									
								case Item:
									c.useItem(e);								
									changeState(battleStates.USEITEM);	
									break;
									
								case Ability:
									runningAbility = c.useAbility();								
									changeState(battleStates.USEITEM);	
									break;
									
								case CombatAction:
									c.useCombatAction();
									changeState(battleStates.USEITEM);
									break;
								}						
							}
						}
					}
				}
				else
				{
					actTimer++;
					if(actTimer >= actionLength)
					{
						//if current actor is an enemy
						if(selCharClosed)
						{
							
							if(moveOrder.get(currentActor).isEnemy())
							{
								if(moveOrder.get(currentActor).isDead())
								{
									currentActor++;
									if(currentActor >= moveOrder.size())
										endAct();
								}
								else
								{
									//show enemy select
									selectedEnemy = (Enemy)moveOrder.get(currentActor);
									
									switch(selectedEnemy.getAction())
									{
									case Attack:
										attackMissed = (Global.rand.nextInt(100) <= 5);
										attackCritical = (Global.rand.nextInt(100) <= 5);
										frameText = selectedEnemy.getDisplayName() +" attacks!";
										break;
									case Ability:
										frameText = "";
										showDisplayName(selectedEnemy.getAbilityToUse().getDisplayName());
										break;
									}
									
									showEnemySelect = true;
									acting = true;
									actTimer = 0;
								}
								
							}
							else
							{									
								currentChar = moveOrder.get(currentActor).Index();
								if(getChar(currentChar).isDead())
								{
									currentActor++;
								}
								else
								{
									advanceChar();
									acting = true;
									getChar(currentChar).acted = true;
									switch (getChar(currentChar).getAction())
									{
									case CombatAction:
										showDisplayName(moveOrder.get(currentActor).getActionName());
										frameText = moveOrder.get(currentActor).getDisplayName() + moveOrder.get(currentActor).getCombatActionText();
										getChar(currentChar).setBattleFrame(faces.Cast);
										break;
									case Item:
										showDisplayName(moveOrder.get(currentActor).getItemToUse().getName());
										frameText = moveOrder.get(currentActor).getDisplayName() +" uses "+ moveOrder.get(currentActor).getItemToUse().getName();
										getChar(currentChar).setBattleFrame(faces.Use);
										break;
									case Ability:
										showDisplayName(moveOrder.get(currentActor).getAbilityToUse().getDisplayName());
										frameText = moveOrder.get(currentActor).getDisplayName() +" casts "+ moveOrder.get(currentActor).getAbilityToUse().getDisplayName();
										getChar(currentChar).setBattleFrame(faces.Cast);
										break;																	
									default:
										frameText = moveOrder.get(currentActor).getDisplayName() +" attacks!";
										getChar(currentChar).setSwing();
										attackMissed = (Global.rand.nextInt(100) <= 5);
										attackCritical = (Global.rand.nextInt(100) <= 5);
										Global.playSound("swordslash3");
										break;
										
										
									
									}
										
								}								
								actTimer = 0;
							}
						}		
					}
				}
			}
		}
	}
	
	
	//character mods
	private Character getChar(int index)
	{
		return Global.party.getPartyMembers(false)[index];
	}

	
	
	//initialization
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
	
	private void updateMenuItems()
	{
		if(getChar(currentChar) != null)
		{
			mainMenu.changeItemText(2, getChar(currentChar).getActionName());
			mainMenu.changeItemText(4, getChar(currentChar).getAbilitiesName());
		}
		
	}


	private void updateFrames()
	{		
		
		if(messageQueue.size() > 0 || currentState == battleStates.VICTORY)
			infoWindow.setClosedSize(Global.vpWidth, frameMinHeight);
		else
			infoWindow.setClosedSize(partyPos.x - partyFrameBuffer, frameMinHeight);

		
		infoWindow.getTextAt(0).text = frameText;
		
		
	}

	
	//drawing
	private void drawCharacters()
	{
		int i = 0;	
		
		if(showCharSelect)
		{
			Global.renderer.drawRect(
					Global.vpToScreenX(charSelect.getPosition().x), 
					Global.vpToScreenY(charSelect.getPosition().y), 
					Global.vpToScreenX(charSelect.getPosition().x+64), 
					Global.vpToScreenY(charSelect.getPosition().y+64), selectPaint);
		}
		
		for(Character c : Global.party.getPartyMembers(false))
		{
			if(c != null)
			{
				//draw stat frames
				charStats[i].render();
				
				if(selectAllAllies && !c.isDead())
					Global.renderer.drawRect(
							Global.vpToScreenX(c.getPosition().x), 
							Global.vpToScreenY(c.getPosition().y), 
							Global.vpToScreenX(c.getPosition().x+64), 
							Global.vpToScreenY(c.getPosition().y+64), selectPaint);
				
				
				//draw character sprite		
				if(i == currentChar)
				{
					c.battleRender(partyPos.x - selCharX, partyPos.y + (charYSpacing*i));
					c.setPosition(partyPos.x - selCharX, partyPos.y + (charYSpacing*i) + 4);
				}
				else
				{
					c.battleRender(partyPos.x, partyPos.y + (charYSpacing*i));
					c.setPosition(partyPos.x, partyPos.y + (charYSpacing*i) + 4);
				}				
				
			}
			i++;
			
				
		}
	}
	private void drawEnemies()
	{
		for(Enemy e : encounter.Enemies())
			if(!e.isDead())
				e.battleRender();	
		
		if(selectAllEnemies)
		{
			for(Enemy e : encounter.Enemies())
			{
				Rect eRect = genEnemyRect(e);
				Global.renderer.drawRect(eRect, selectPaint, false);
				if(Global.vpToScreenY(eRect.top - (int)(enemyBattleText.getTextSize()/2)-4)> 0)
					Global.renderer.drawText(e.getDisplayName(), eRect.left + (eRect.right - eRect.left)/2, eRect.top - enemyBattleText.getTextSize()/2-4, enemyBattleText);
				else
					Global.renderer.drawText(e.getDisplayName(), eRect.left + (eRect.right - eRect.left)/2, eRect.bottom + enemyBattleText.getTextSize()/2-4, enemyBattleText);
				
			}
		}
		else
		{
			if(showEnemySelect)
			{
				
				if(selectedEnemy != null)
				{
					//HAHA AINT I FUNNY
					Rect eRect = genEnemyRect(selectedEnemy);
					Global.renderer.drawRect(eRect, selectPaint, false);
					if(Global.vpToScreenY(eRect.top - (int)(enemyBattleText.getTextSize()/2)-4)> 0)
						Global.renderer.drawText(selectedEnemy.getDisplayName(), eRect.left + (eRect.right - eRect.left)/2, eRect.top - enemyBattleText.getTextSize()/2-4, enemyBattleText);
					else
						Global.renderer.drawText(selectedEnemy.getDisplayName(), eRect.left + (eRect.right - eRect.left)/2, eRect.bottom + enemyBattleText.getTextSize()/2-4, enemyBattleText);
					
				}				
			}			
		}		
	}
	private void drawFrame()
	{
		updateFrames();		

		if(!infoWindow.Opened())			
			infoWindow.render();
		else
			mainMenu.render();
			

		if(infoWindow.Opened())
		{

			switch(currentState)
			{				
			case SELECTITEM:							
				if(mainMenu.getSelectedEntry() != null && mainMenu.getSelectedEntry() != null)
				{
					mpWindow.getTextAt(0).text = "Qty: "+((Item)mainMenu.getSelectedEntry().obj).getCount();
					mpWindow.render();
				}
				break;
				
			case SELECTABILITY:			
				if(mainMenu.getSelectedEntry() != null && mainMenu.getSelectedEntry() != null)
				{
					mpWindow.getTextAt(0).text = "Cost: "+((Ability)mainMenu.getSelectedEntry().obj).MPCost();
					mpWindow.render();
				}
				break;
			
			}
			
		}
	}
	
	int genColor(int val, int max)
	{
		return Color.argb(255, 255 - (int)(255*(val/max)), (int)(255*(val/max)), 0);
	}
	
	private Rect genEnemyRect(Enemy e)
	{
		if(e != null)
		{
			return new Rect(
					Global.vpToScreenX(e.getPosition().x - e.getWidth()/2),
					Global.vpToScreenY(e.getPosition().y - e.getHeight()/2),
					Global.vpToScreenX(e.getPosition().x + e.getWidth()/2),
					Global.vpToScreenY(e.getPosition().y + e.getHeight()/2));
		}
		else
			return new Rect();
		
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
	
	
	//battle logic
	private void nextCharacter()
	{
		do
		{
			currentChar++;
		}while(currentChar < 4 && getChar(currentChar) == null);
		
		
		if(currentChar >= 4)
		{
			currentChar = 0;
			changeState(battleStates.ACT);
			nextChar = false;
		}
		else
		{
			if(!getChar(currentChar).isDead())
			{
				advanceChar();
				infoWindow.open();
				nextChar = false;
				updateMenuItems();
			}
				
			
		}
		
			
	}
	private void prevCharacter()
	{	
		int lastActive = currentChar;
		
		do
			currentChar--;
		while(currentChar >= 0 && (getChar(currentChar) == null || getChar(currentChar).isDead()));
		
		if(currentChar < 0)
		{
			changeState(battleStates.START);
			currentChar = lastActive;
		}
				
		else
		{
			getChar(currentChar).setIdle(false);
			
			if(getChar(currentChar).action == Action.Item)
				getChar(currentChar).unuseItem();
			
			getChar(currentChar).clearTargets();
			advanceChar();
			infoWindow.open();
			updateMenuItems();
		}
		
		prevChar = false;
				
	}
	private void changeState(battleStates state)
	{		
		currentState = state;
		
		//scrollDelta = 0;
		switch(currentState)
		{
		case SELECT:
			frameText = defaultFrameText;
			for(Character c : getAliveCharacters())
				c.setDone(true);
			
			mainMenu.clearObjects();
			mainMenu.addItem("Attack", "atk", false);
			mainMenu.addItem("Item", "itm", false);
			mainMenu.addItem("Action", "act", false);
			mainMenu.addItem("Guard", "grd", false);
			mainMenu.addItem("Ability", "ab", false);
			mainMenu.addItem("Run", "run", false);
			
			updateMenuItems();
			mainMenu.update();
			
			break;
			
		case SELECTITEM:			
			mainMenu.clearObjects();
			for(Item i : Global.party.getInventory(true))
				mainMenu.addItem(i.getName(), i, false);
			mainMenu.update();
			break;
			
		case SELECTABILITY:
			mainMenu.clearObjects();
			for(Ability a : getChar(currentChar).getAbilities())
				mainMenu.addItem(a.getDisplayName(), a, a.MPCost() > getChar(currentChar).getMP());
			mainMenu.update();
			break;
			
		case RUN:
			boolean success = !encounter.disableRunning && Global.rand.nextInt(100) < 90;
			if(success)
			{
				messageQueue.add("Your party runs away!");
				
				do{					
					if(getChar(currentChar) != null && getChar(currentChar).action == Action.Item)
						getChar(currentChar).unuseItem();
				} while(currentChar-- > 0);
				
				
				triggerEndBattle();
			}
			else
			{
				if(encounter.disableRunning)
					messageQueue.add("Can't run!");
				else
					messageQueue.add("Failed to run away!");
				
				currentChar = 0;
				changeState(battleStates.RUNFAIL);
				//nextChar = false;
			}
			break;
		
		case ACT:
			frameText = "";
			
			
			genMoveOrder(moveOrderGroups.All);
			setEnemyTargets();
			
			acting = false;
			currentActor = 0;
			
			for(Character c : Global.party.getPartyMembers(false))
				if(c != null) c.acted = false;


			break;
			
		case VICTORY:
			//Global.musicBox.stop();
			Global.musicBox.play("victory", true, -1);
			Vector<Character> chars = getAliveCharacters();
			
			int avgLevel, levelTotal = 0;
			
			for(Character c : Global.party.getPartyMembers(false))
				if(c != null && !c.acted && c.action == Action.Item)
					c.unuseItem();
			
			do{					
				if(getChar(currentChar) != null && getChar(currentChar).action == Action.Item)
					getChar(currentChar).unuseItem();
			} while(currentChar-- > 0);
			
			for(Character c : chars)
			{
				c.setBattleFrame(BattleSprite.faces.Victory);
				levelTotal += c.getLevel();
			}
			avgLevel = levelTotal / chars.size();
			
			messageQueue.add("You are victorious!");
			int gold = 0;
			int exp = 0;
			List<String> itemDrops = new ArrayList<String>();
			for(Enemy e : encounter.Enemies())
			{
				gold += e.getGold();
				exp += e.getExp(avgLevel); 
				if(e.hasItems())
				{
					String item = e.getItem(false);
					if(item != null)
						itemDrops.add(item);
				}
				
			}
			
			//pick random drop
			String wonItem = null;
			if(itemDrops.size() > 0)
				wonItem = itemDrops.get(Global.rand.nextInt(itemDrops.size()));
			
			
			//exp /= getAliveCharacters().size();
			
			Global.party.addGold(gold);
			
			messageQueue.add("Obtained " + gold + "G!");
			messageQueue.add("Earned " + exp + " experience!");
			
			String newAbilities = "";
			
			for(Character c : chars)
			{
				int leftover = c.awardExperience(exp);
				while(leftover > 0)
				{
					messageQueue.add(c.getDisplayName() + " grew to level " + c.getLevel() + "!");
					
					do
					{
						newAbilities = c.checkForAbilities();
						if(newAbilities != "")
							messageQueue.add(c.getDisplayName() + " learned " + newAbilities + "!");
						
					}while(newAbilities != "");
					
					leftover = c.awardExperience(leftover);
				}				
			}
			
			if(wonItem != null)
			{
				Global.party.addItem(wonItem);	
				messageQueue.add("Found a " + Global.items.get(wonItem).getName() + "!");
			}
			//infoWindow.closeFrame();
			
			triggerEndBattle();
			break;
			
		case TARGET:			
			switch(targetType)
			{
			case Self:
				frameText = "Targeting Self...";
				charSelect = getChar(currentChar);
				showCharSelect = true;
				break;
			case AllAllies:
				frameText = "Selecting All Allies...";
				selectAllAllies = true;
				break;
			case AllEnemies:
				frameText = "Selecting All Enemies...";
				selectAllEnemies = true;
				break;
			case Single:
				frameText = "Select a Target.";
				break;
			case SingleEnemy:
				frameText = "Select an Enemy.";
				break;
			case SingleAlly:
				frameText = "Select an Ally.";
				break;
				
			}

		
		}
	}


	private void handleOption(String opt)
	{
		if(opt.equals("itm"))
		{
			changeState(battleStates.SELECTITEM);	
			infoWindow.open();
		}
		else if(opt.equals("atk"))
		{
			getChar(currentChar).setAttack();
			getChar(currentChar).setReady();				
			targetType = TargetTypes.Single;
			changeState(battleStates.TARGET);
		}
		else if(opt.equals("grd"))
		{
			getChar(currentChar).guard();
			getChar(currentChar).setReady();				
			recedeChar();
			nextChar = true;
			changeState(battleStates.SELECT);
		}
		else if(opt.equals("ab"))
		{
			changeState(battleStates.SELECTABILITY);	
			infoWindow.open();
		}
		else if(opt.equals("act"))
		{
			getChar(currentChar).setUseCombatAction();
			getChar(currentChar).setReady();				
			targetType = getChar(currentChar).getCombatActionTargetType();
			changeState(battleStates.TARGET);
		}
		else if(opt.equals("run"))
		{
			changeState(battleStates.RUN);
			recedeChar();
		}

	}
	private void resetTarget(Character c)
	{
		Vector<Enemy> temp = getAliveEnemies();

		if(temp.size() > 0)
		{
			for(Enemy e : temp)
			{
				if(!e.isDead())
				{
					c.addTarget(e);
					break;
				}
			}
		}
		else
		{
			//changeState(battleStates.VICTORY);
		}
	}
	
	private void setEnemyTargets()
	{
		for(Enemy e : encounter.Enemies())
		{
			setEnemyTarget(e);			
		}
			
	}
	private void setEnemyTarget(Enemy e)
	{
		List<Character> alive = getAliveCharacters();
		if(alive.size() == 0)
		{
			Annihilated();
			return;
		}
		
		switch(e.getAction())
		{
		case Ability:
			switch(e.getAbilityToUse().TargetType())
			{
			case SingleEnemy:			
			case SingleAlly:
			case Single:
				e.addTarget(alive.get(Global.rand.nextInt(alive.size())));
				break;
			case AllAllies:
			case AllEnemies:
				for(Character c : alive)
					e.addTarget(c);
				break;
			case Self:
				e.addTarget(e);
				break;
			}			
			break;
		default:
			e.addTarget(alive.get(Global.rand.nextInt(alive.size())));
			break;
		}
	}
	
	private void genMoveOrder(moveOrderGroups group)
	{
		moveOrder = new Vector<Character>();
		
		if(group == moveOrderGroups.All || group == moveOrderGroups.EnemiesOnly)
		for(Enemy e : encounter.Enemies())
		{
			e.Act();
			
			if(moveOrder.size() == 0)
			{
				moveOrder.add(e);
			}
			else
			{
				int i = 0;
				for(Character c : moveOrder)
				{
					
					if(e.getStat(Stats.Speed) > c.getStat(Stats.Speed))
					{
						moveOrder.insertElementAt(e, i);
						break;
					}
					i++;
				}
				if(!moveOrder.contains(e))
					moveOrder.add(e);
			}
			
		}
		
		if(group == moveOrderGroups.All || group == moveOrderGroups.CharactersOnly)		
		for(int c = 0; c < 4; ++c)
		{
			if(getChar(c) != null && !getChar(c).isDead())
			{
				if(getChar(c).getAction() != Character.Action.Guard)
				{
					if(getChar(c).getAction()== Action.Item)
						moveOrder.insertElementAt(getChar(c), 0);
					else
					{
						int i = 0;
						for(Character ch : moveOrder)
						{				
							if(getChar(c).getStat(Stats.Speed) > ch.getStat(Stats.Speed))
							{
								moveOrder.insertElementAt(getChar(c), i);
								break;
							}
							i++;
						}
						if(!moveOrder.contains(getChar(c)))
							moveOrder.add(getChar(c));
					}
					
				}
			}
			
			
		}
		
	}
	private void endAct()
	{
		changeState(battleStates.START);
		
		frameText = defaultFrameText;
		showEnemySelect = false;
		for(Character c : Global.party.getPartyMembers(false))
		{
			if(c != null)
				c.clearTargets();
			
		}
		for(Enemy e: encounter.Enemies())
		{
			e.clearTargets();
		}
		
		for(Character c : getAliveCharacters())
		{
			c.setIdle(true);
			c.statusOnTurn(this);
		}
		
		for(Enemy e: getAliveEnemies())
		{
			e.statusOnTurn(this);
		}
			
		
	}
	
	public static int genDamage(Character attacker, Character defender)
	{
		return genDamage(attacker, defender, attacker.getBattlePower());
	}
	
	public static int genDamage(float BP, Character defender)
	{
		return genDamage(null, defender, BP);
	}	
	
	public static int genDamage(Character attacker, Character defender, float BP)
	{
		float DP = defender.getDefense();
		
		int baseDmg;
		float coefficient = attacker == null ? 1.0f : attacker.getCoefficient();
		
		if(!defender.isEnemy())
			baseDmg = (int)((Math.max(1.0f, (BP * 2.0f) - DP) * 4.0f * coefficient));
		else
			baseDmg = (int)((Math.max(1.0f, (BP * 2.0f) - DP) * 10.0f * coefficient));
		
		int variance = Global.rand.nextInt(20);
		
		int dmgMod = (int)((float)baseDmg*(float)((variance-10)/100.0F));
		
		return baseDmg + dmgMod;
	}
	
	
	private Vector<Character> getAliveCharacters()
	{
		Vector<Character> temp = new Vector<Character>();
		for(Character c : Global.party.getPartyMembers(false))
		{
			if(c != null && !c.isDead())
				temp.add(c);
		}
		
		return temp;
	}
	private Vector<Enemy> getAliveEnemies()
	{
		Vector<Enemy> temp = new Vector<Enemy>();
		for(Enemy e : encounter.Enemies())
		{
			if(!e.isDead())
				temp.add(e);
		}
		
		return temp;
	}
	
	public void addDamageMarker(DamageMarker marker)
	{
		markers.add(marker);
	}
	
	public void physicalDamage(Character attacker, Character defender)
	{
		int dmg = genDamage(attacker, defender);
		
		
		
		
		if(attackCritical)
		{
			dmg *= 2;
			if(!attackMissed)
			{
				Global.screenFader.setFadeColor(255, 255, 255, 255);
				Global.screenFader.flash(15);
			}
			
			attackCritical = false;
		}
			
		
		//50% damage while guarding
		if(defender.getAction() == Character.Action.Guard)
			dmg = Math.max((int)(dmg * 0.5f), 1);
		
		if(!attackMissed)	
		{
			applyDamage(defender, -dmg, 0);				
		}				
		else
			dmgText(defender, "MISS", 0);
	
	}
	
	public void postDamage(Character c)
	{
		if(c.isDead())
			moveOrder.remove(c);
		
		Global.playSound("gettinghit2");
		
		if(!c.isEnemy())
		{
			if(c.isDead())
			{
				c.setBattleFrame(BattleSprite.faces.Dead);
				if(getAliveCharacters().size() <= 0)
				{
					recedeChar();
					infoWindow.close();
					Annihilated();
				}
			}
								
			else
			{
				if(moveOrder.indexOf(c) >= currentActor || c.getAction() == Character.Action.Guard)
					c.setReady();				
				else
					c.setIdle(true);
			}	
		}	
		else
		{
			if(getAliveEnemies().size() == 0 && currentState != battleStates.USEITEM)
			{
				recedeChar();
				infoWindow.close();
				changeState(battleStates.VICTORY);
				
			}
				
		}
	}
	
	public void applyDamage(Character c, int dmg, int delay)	
	{
		markers.add(new DamageMarker(dmg, c, delay));
	}
	
	public void dmgText(Character c, String str, int delay)
	{
		markers.add(new DamageMarker(str, c, delay));
	}
	
	private void Annihilated()
	{
		
		messageQueue.add("Annihilated...");
		changeState(battleStates.SELECT);
		triggerEndBattle();
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
	
	
	
	//input
	public void touchActionDown(int x, int y)
	{	
		//only handle events if messaqueue is empty
		if(messageQueue.size() == 0)
		{
			if(endBattleFlag)
				return;
			
			switch(currentState)
			{
			case START:
				break;
			case TARGET:
				switch(targetType)
				{
				case Single:
				case SingleAlly:
				case SingleEnemy:
					setMouseSelect(x, y);
					break;
				default:
					break;
				}
				
				break;
			case SELECT:
			case SELECTITEM:
			case SELECTABILITY:
				mainMenu.touchActionDown(x, y);
				break;
			case ACT:
				break;
			case VICTORY:
				break;
			}
		}
	}
	public void touchActionUp(int x, int y)
	{
		
		showCharSelect = false;
		
		//only handle events if messaqueue is empty
		if(messageQueue.size() == 0)
		{			
			if(endBattleFlag)
				return;
			
			switch(currentState)
			{
			case START:
				if(getAliveCharacters().size() > 0)
				{
					currentChar = 0;
					while(getChar(currentChar) == null || getChar(currentChar).isDead())
						currentChar++;							
					
					infoWindow.open();				
					advanceChar();
					infoWindow.open();
					changeState(battleStates.SELECT);
					Global.playSound("menusound1");
				}
				else
				{
					Annihilated();
				}
				break;
				
			
				
			case TARGET:
				if(infoWindow.Closed())
				{
					if(mainMenu.contains(x, y))
					{
						//recedeChar();
						infoWindow.open();
						changeState(battleStates.SELECT);
						getChar(currentChar).setIdle(false);
						selectAllAllies = false;
						selectAllEnemies = false;
						if(getChar(currentChar).action == Action.Item)
							getChar(currentChar).unuseItem();
						Global.playSound("denied1");
						showEnemySelect = false;
					}
					else 
					{
						Global.playSound("menusound1");
						switch(targetType)
						{
						case SingleAlly:
						case SingleEnemy:
						case Single:
							if(selectedEnemy != null && enemyFrame.contains(x, y) && targetType != TargetTypes.SingleAlly)
							{
								getChar(currentChar).addTarget(selectedEnemy);
								getChar(currentChar).setReady();
								recedeChar();
								nextChar = true;
								changeState(battleStates.SELECT);
								showEnemySelect = false;	
								
							}	
							else if(targetType != TargetTypes.SingleEnemy)
							{
								for(Character c : Global.party.getPartyMembers(false))
								{
									if(c != null)
									{
										Rect cRect = new Rect(
												Global.vpToScreenX(c.getPosition().x), 
												Global.vpToScreenY(c.getPosition().y), 
												Global.vpToScreenX(c.getPosition().x+64), 
												Global.vpToScreenY(c.getPosition().y+64));
										if(cRect.contains(x, y))
										{
											getChar(currentChar).addTarget(c);
											getChar(currentChar).setReady();
											recedeChar();
											nextChar = true;
											changeState(battleStates.SELECT);
											showEnemySelect = false;
										}
									}
									
								}
									
							}
							break;
						case AllAllies:
							for(Character c : Global.party.getPartyMembers(false))
								if(c != null && !c.isDead())
									getChar(currentChar).addTarget(c);
							getChar(currentChar).setReady();
							recedeChar();
							nextChar = true;
							changeState(battleStates.SELECT);
							showEnemySelect = false;
							selectAllAllies = false;
							break;
						case AllEnemies:
							for(Character e : encounter.Enemies())
								if(!e.isDead())
									getChar(currentChar).addTarget(e);
							getChar(currentChar).setReady();
							recedeChar();
							nextChar = true;
							changeState(battleStates.SELECT);
							showEnemySelect = false;
							selectAllEnemies = false;
							break;
						case Self:
							getChar(currentChar).addTarget(getChar(currentChar));
							getChar(currentChar).setReady();
							recedeChar();
							nextChar = true;
							changeState(battleStates.SELECT);
							showEnemySelect = false;
							break;
						}
					}
					
				}				
				
				break;
			case SELECT:
				if(infoWindow.Opened())
				{				
					switch(mainMenu.touchActionUp(x, y))
					{
					case Close:
						recedeChar();
						infoWindow.close();
						Global.playSound("denied1");
						break;
					case Selected:
						infoWindow.close();
						String str = (String)(mainMenu.getSelectedEntry().obj);
						handleOption(str);
						Global.playSound("menusound1");
						break;
					}	
				}
				else
				{
					infoWindow.open();
					advanceChar();
				
				}
				break;
			case SELECTITEM:				
				switch(mainMenu.touchActionUp(x, y))
				{
				case Close:
					changeState(battleStates.SELECT);
					Global.playSound("denied1");
					break;
				case Selected:
					Item i = (Item)(mainMenu.getSelectedEntry().obj);
					//Global.party.removeItem(i.getId(), 1);											
					getChar(currentChar).setItemToUse(i);
					getChar(currentChar).setReady();
					targetType = i.getTargetType();
					infoWindow.close();
					changeState(battleStates.TARGET);
					Global.playSound("menusound1");
					break;
				}
				break;
				
			case SELECTABILITY:				
				switch(mainMenu.touchActionUp(x, y))
				{
				case Close:
					changeState(battleStates.SELECT);
					Global.playSound("denied1");
					break;
				case Selected:
					Ability a = (Ability)(mainMenu.getSelectedEntry().obj);
					if(getChar(currentChar).getMP() >= a.MPCost())
					{
						infoWindow.close();					
						getChar(currentChar).setAbilityToUse(a);
						getChar(currentChar).setReady();
						targetType = a.TargetType();
						changeState(battleStates.TARGET);
					}
					Global.playSound("menusound1");
					break;
				}
				
				break;
			case ACT:
				break;
			case VICTORY:
				break;
			}
		}
		else
		{
			//messagequeue has items, display next
			Global.playSound("menusound1");
			nextMessage();
			
		}
	}
	
	private void nextMessage()
	{
		messageQueue.remove(0);
		messagequeuetimer = 0;
		
		if(messageQueue.size() == 0 && currentState != battleStates.RUNFAIL && !endBattleFlag)
		{
			frameText = defaultFrameText;
			if(currentState == battleStates.SELECT)
			{
				infoWindow.open();
				advanceChar();
			}
		}
	}
	
	public void touchActionMove(int x, int y)
	{		
		//only handle events if messaqueue is empty
		if(messageQueue.size() == 0)
		{
			if(endBattleFlag)
				return;
			
			switch(currentState)
			{
			case START:
				break;
			case TARGET:
				switch(targetType)
				{
				case Single:
				case SingleAlly:
				case SingleEnemy:
					setMouseSelect(x, y);
					break;
				default:
					break;
				}
				break;
			case SELECT:						
			case SELECTITEM:				
			case SELECTABILITY:	
				mainMenu.touchActionMove(x, y);				
				break;
			case ACT:
				break;
			case VICTORY:
				break;
			}
		}
	}
	
	private void setMouseSelect(int x, int y)
	{
		showEnemySelect = false;
		showCharSelect = false;
		if(enemyFrame.contains(x, y) && targetType != TargetTypes.SingleAlly)
		{
			showEnemySelect = true;
			int lowest = 256;
			for(Enemy e : getAliveEnemies())
			{
				int dist = Math.abs(x - Global.vpToScreenX(e.getPosition().x)) + 
							Math.abs(y - Global.vpToScreenY(e.getPosition().y));
				if(dist < lowest)
				{
					lowest = dist;
					selectedEnemy = e;
				}		
			}
		}
		else if(targetType != TargetTypes.SingleEnemy)
		{	
			for(Character c : getAliveCharacters())
			{
				if(c != null && c.getPosition() != null)
				{
					Rect cRect = new Rect(
							Global.vpToScreenX(c.getPosition().x), 
							Global.vpToScreenY(c.getPosition().y), 
							Global.vpToScreenX(c.getPosition().x+64), 
							Global.vpToScreenY(c.getPosition().y+64));
					if(cRect.contains(x, y))
					{
						showCharSelect = true;
						charSelect = c;					
					}					
				}
			}
					
		}
				
	}

	//states enum
	public enum battleStates
	{
		START,
		SELECT,
		TARGET,
		ACT,
		ACTWAIT,
		RUN,
		RUNFAIL,
		USEITEM,
		VICTORY,
		SELECTITEM,
		SELECTABILITY
	}
	
	private enum moveOrderGroups
	{
		All,
		EnemiesOnly,
		CharactersOnly
	}
}
