package bladequest.world;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import bladequest.UI.DebugScreen;
import bladequest.UI.ListBox;
import bladequest.UI.MenuPanel.Anchors;
import bladequest.UI.MsgBox;
import bladequest.UI.NameSelect;
import bladequest.UI.SaveLoadMenu;
import bladequest.UI.MainMenu.MainMenu;
import bladequest.UI.MerchantScreen.MerchantScreen;
import bladequest.actions.Action;
import bladequest.actions.actExpectInput;
import bladequest.actions.actFadeControl;
import bladequest.actions.actPauseMusic;
import bladequest.actions.actPlayMusic;
import bladequest.actions.actResetGame;
import bladequest.actions.actShowScene;
import bladequest.actions.actUnloadScene;
import bladequest.actions.actWait;
import bladequest.battleactions.BattleAction;
import bladequest.bladescript.FileTokenizer;
import bladequest.bladescript.Parser;
import bladequest.bladescript.Script;
import bladequest.bladescript.ScriptVar;
import bladequest.combat.Battle;
import bladequest.combat.BattleEventBuilder;
import bladequest.enemy.Enemy;
import bladequest.graphics.AnimatedBitmap;
import bladequest.graphics.AnimationBuilder;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleAnimObjState;
import bladequest.graphics.BattleAnimObjState.PosTypes;
import bladequest.graphics.BattleAnimObject;
import bladequest.graphics.BattleAnimObject.Types;
import bladequest.graphics.BattleSprite;
import bladequest.graphics.BitmapFrame;
import bladequest.graphics.Icon;
import bladequest.graphics.ReactionBubble;
import bladequest.graphics.Renderer;
import bladequest.graphics.Scene;
import bladequest.graphics.ScreenFader;
import bladequest.graphics.Sprite;
import bladequest.graphics.TextFactory;
import bladequest.graphics.TilePlateBitmap;
import bladequest.graphics.WeaponSwing;
import bladequest.math.PointMath;
import bladequest.sound.BladeSong;
import bladequest.sound.Song;
import bladequest.system.BqActivity;
import bladequest.system.BqPanel;
import bladequest.system.GameSaveLoader;
import bladequest.system.Lock;
import bladequest.system.MapLoadThread;


//THIS IS A TEST COMMENT

public class Global 
{
	private static GameObject gameOverObject;
	private static final String TAG = Global.class.getSimpleName();
	public static LoadingScreen loadingScreen;
	public final static int MAX_FPS = 60;
	public final static int FRAME_PERIOD = 1000 / MAX_FPS;
	
	public final static int iconSize = 10;
	public final static int portraitSrcSize = 64;
	//public final static float iconScale = 2.0f;
	
	public static Random rand = new Random(System.currentTimeMillis());
	
	public static boolean loading = false;
	
	public static Lock lock = new Lock();
	public static States GameState;
	public static TitleScreen title;
	
	public static Map<String, AnimationBuilder> animationBuilders;
	public static Map<String, Bitmap> bitmaps;
	public static Map<String, Scene> scenes;
	public static Map<String, Sprite> sprites;
	public static Map<String, PlayerCharacter> characters;
	public static Map<String, Enemy> enemies;
	public static Map<String, BattleSprite> battleSprites;
	public static Map<String, Boolean> switches;
	public static Map<String, Encounter> encounters;
	public static Map<String, Item> items;
	public static Map<String, Ability> abilities;
	public static Map<String, String> maps;
	public static Map<String, Song> music;
	public static Map<String, Integer> sfx;
	public static Map<String, Icon> icons;
	public static Map<String, ReactionBubble> reactionBubbles;
	public static Map<String, Merchant> merchants;
	public static Map<String, BattleAnim> battleAnims;
	public static Map<String, WeaponSwing> weaponSwingModels;
	private static List<BattleAnim> playingAnims;
	
	public static SoundPool soundPool;
	public static PlayTimer playTimer;
	
	public static GameSaveLoader saveLoader;
	
	public static BqMap map;
	private static String encounter;
	
	public static int saveIndex = 0;
	
	public static int encounterRate = 8;	
	
	public static float imageScale = 1.0F;
	public static float baseScale = 0.0f;
	
	public static boolean appRunning = false;
	
	public static int moveSpeed = 3;
	public static int imageSpeed = 20;
	private static int imageTimer = 0;
	public static boolean updateAnimation = false;
	public static boolean animateTiles = false;
	
	public static int MAX_LEVEL = 99;
	
	private static int delayTimer = 0;
	private static int delayLength = 10;
	private static boolean inputDelay = false;
	
	public static MsgBox worldMsgBox = null;

	public static boolean originalTargetValid = false;
	
	public static Point vpGridSize = new Point( 14, 9);	
	
	public static int vpWidth = vpGridSize.x*32;
	public static int vpHeight = vpGridSize.y*32;	
	public static Point vpWorldPos = new Point(0, 0);
	public static Point vpGridPos = new Point(0, 0);
	private static Point vpPan = new Point(0, 0);
	private static Point vpPanTarget = new Point(0, 0);
	private static Point vpPanStart = new Point(0, 0);
	private static float vpPanLength;
	private static long panStartTime;
	
	public static int screenWidth = 0;
	public static int screenHeight = 0;
	
	public static boolean stretchScreen = true;
	
	public static Point mousePos = new Point(0, 0);
	public static Point mouseGridPos = new Point(0, 0);	
	
	public static boolean newTarget = false;
	
	public static boolean pathIsValid = false;
	public static Rect validPathArea;
	
	public static Renderer renderer;
	public static TextFactory textFactory;
	public static ScreenFader screenFader;
	public static ScreenShaker screenShaker;
	public static TargetReticle target;

	public static List<String> gameLog; 
	
	public static Party party;
	public static Battle battle;
	public static MainMenu menu;
	public static NameSelect nameSelect;
	public static SaveLoadMenu saveLoadMenu;
	public static MerchantScreen merchantScreen;
	public static DebugScreen debugScreen;
	public static boolean openMenuFlag, openDebugFlag;
	
	public static ListBox debugButton, menuButton;
	
	public static BqActivity activity;
	public static BqPanel panel;
	public static AudioManager audioMgr;
	
	public static int fc1r = 0, fc1g = 0, fc1b = 200;
	public static int fc2r = 0, fc2g = 0, fc2b = 100;

	public static int textSpeed = 1;
	
	private static int frameBorderColor = Color.rgb(255, 255, 255);	
	private static Paint framePaint = new Paint();
	private static Paint frameBorderPaint = new Paint();
	private static Paint frameBorderThick = new Paint();
	
	public static Scene showScene;
	
	public static TilePlateBitmap[] tilePlateBmps;	
	public static int tilePlateBitmapCount = 24;
	
	public static actExpectInput inputExpecter;
	
	public static void logMessage(String str)
	{
		if(gameLog == null)
			gameLog = new ArrayList<String>();
		
		gameLog.add(str);
	}
	
	public static Script compileScript(String file, Map<String, ScriptVar> standardLibrary)
	{
		Log.d(TAG, "Loading " + file);
		Script script = new Script(standardLibrary);
		
		Parser p = null;
		try {
			p = new Parser(new FileTokenizer(file, activity.getAssets().open(file)), script);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		p.run(); //populates script!
		return script;
	}
	
	
	public static void genValidPathArea(List<Point> nodes)
	{
		if(nodes.size() > 0)
		{
			validPathArea = new Rect(nodes.get(0).x, nodes.get(0).y,nodes.get(0).x, nodes.get(0).y);
			for(Point p : nodes)
			{
				if(p.x < validPathArea.left) validPathArea.left = p.x;
				if(p.y < validPathArea.top) validPathArea.top = p.y;
				if(p.x > validPathArea.right) validPathArea.right = p.x;
				if(p.y > validPathArea.bottom) validPathArea.bottom = p.y;
			}
			
			//inflate			
			validPathArea.left -= 1;
			validPathArea.top -= 1;
			validPathArea.right += 2;
			validPathArea.bottom += 2;
		}
		
	}
	
	public static void updatePaints()
	{
		framePaint.setStyle(Style.FILL);
		
		
		frameBorderPaint.setColor(frameBorderColor);
		frameBorderPaint.setStyle(Style.STROKE);
		frameBorderPaint.setStrokeWidth(2);
		frameBorderPaint.setStrokeCap(Cap.ROUND);
		frameBorderPaint.setAntiAlias(true);
		
		frameBorderThick.setColor(frameBorderColor);
		frameBorderThick.setStyle(Style.STROKE);
		frameBorderThick.setStrokeWidth(4);
		frameBorderThick.setStrokeCap(Cap.ROUND);
		frameBorderThick.setAntiAlias(true);
	}
	
	public static void drawFrame(Rect r, boolean thick)
	{
		drawFrame(r, Color.rgb(fc1r, fc1g, fc1b), Color.rgb(fc2r, fc2g, fc2b), thick);

	}
	
	public static void drawFrame(Rect r, int frameColor1, int frameColor2, boolean thick)
	{
		int inset = thick ? 2 : 1;
		Rect insetBorder = new Rect(r);
		
		framePaint.setShader(new LinearGradient(0, r.top, 0, r.bottom, frameColor1, frameColor2, Shader.TileMode.MIRROR));
		//framePaint.setAlpha(128);
		renderer.drawRect(r, new Paint(framePaint), true);
		
		insetBorder.inset(inset, inset);
		if(thick)
			renderer.drawRect(insetBorder, frameBorderThick, true);
		else			
			renderer.drawRect(insetBorder, frameBorderPaint, true);
	}
	
	public static void drawFrameBorder(Rect r, boolean thick)
	{
		int inset = thick ? 2 : 1;
		Rect insetBorder = new Rect(r);
		
		insetBorder.inset(inset, inset);
		if(thick)
			renderer.drawRect(insetBorder, frameBorderThick, true);
		else			
			renderer.drawRect(insetBorder, frameBorderPaint, true);
	}

	public static void updateMousePosition(int x, int y, boolean autoNewTarget)
	{
		if(party.allowMovement() && menuButton.contains(x, y))
			return;
		
		if(debugButton != null && debugButton.contains(x, y))
			return;
		
		if(worldMsgBox == null || worldMsgBox.Closed())
		{
			if(inputDelay)
				return;
			
			if(party.allowMovement())
				target.show();
			
			//newTarget = false;

			mousePos.x = screenToWorldX(x);
			if(mousePos.x < vpWorldPos.x)
				mousePos.x = vpWorldPos.x;
			
			if(mousePos.x >= vpWorldPos.x + vpWidth)
				mousePos.x = vpWorldPos.x + vpWidth - 1;
			
			mousePos.y = screenToWorldY(y);
			if(mousePos.y < vpWorldPos.y)
				mousePos.y = vpWorldPos.y;
			
			if(mousePos.y >= vpWorldPos.y + vpHeight)
				mousePos.y = vpWorldPos.y + vpHeight - 1;
			
			Point newGridPos = new Point(mousePos.x/32, mousePos.y/32);
		
			if((!mouseGridPos.equals(newGridPos) || autoNewTarget) && party.allowMovement())
			{
				mouseGridPos = newGridPos;
				newTarget = true;
				
				playSound("dlg");
			}
		}

	}
	
	
	public static void showMessage(String str, boolean yesNoOpt)
	{
		if(worldMsgBox == null)
			worldMsgBox = new MsgBox();
		
		worldMsgBox.setBottom();
		
		menuButton.close();
		
		worldMsgBox.addMessage(str, yesNoOpt);
		if(worldMsgBox.Closed())
			worldMsgBox.open();
	}
	
	public static void showMessageTop(String str, boolean yesNoOpt)
	{
		if(worldMsgBox == null)
			worldMsgBox = new MsgBox();
		
		worldMsgBox.setTop();
		
		menuButton.close();
		
		worldMsgBox.addMessage(str, yesNoOpt);
		if(worldMsgBox.Closed())
			worldMsgBox.open();
	}
	public static void resetImageTimer()
	{
		updateAnimation = false;
		imageTimer = 0;
	}
	
	public static void openMainMenu()
	{
		if(party.allowMovement())
		{
			if(party.isGridAligned())
				openMainMenuSafe();
			else			
				openMenuFlag = true;
		}
		
	}
	public static void openMainMenuSafe()
	{
		openMenuFlag = false;
		menu = new MainMenu();
		menu.open();
		party.clearMovementPath();
		transition(States.GS_MAINMENU);

	}
	
	public static void openDebugMenu()
	{
		if(party.isGridAligned())
			openDebugMenuSafe();
		else				
			openDebugFlag = true;
		
	}
	public static void openDebugMenuSafe()
	{
		openDebugFlag = false;
		debugScreen = new DebugScreen();
		debugScreen.open();
		party.clearMovementPath();
		GameState = States.GS_DEBUG;

	}
	
	public static void openNameSelect(PlayerCharacter c)
	{
		if(nameSelect == null)
			nameSelect = new NameSelect();
		
		nameSelect.open(c);
		transition(States.GS_NAMESELECT);
	}
	
	public static void openSaveLoadMenu(int mode)
	{
		saveLoadMenu.open(mode);
		transition(States.GS_SAVELOADMENU);
	}
	
	public static void openMerchantScreen(String merchName, float discount)
	{
		if(merchantScreen == null)
			merchantScreen = new MerchantScreen();
		
		if(merchants.containsKey(merchName))
		{
			merchantScreen.open(merchants.get(merchName), discount);
			GameState = States.GS_MERCHANT;
		}
			
	}
	
	private static States transitionInto;	
	public static void transition(States into)
	{
		transitionInto = into;
		GameState = States.GS_MENUTRANSITION;
		screenFader.clear();
		screenFader.setFadeColor(255, 0, 0, 0);
		screenFader.fadeOut(0.5f);
	}
	
	public static void closeMainMenu()
	{
		menu.close();
	}
	
	public static void pan(int x, int y, float length)
	{
		vpPanTarget = new Point(x*32, y*32);
		vpPanStart = new Point(vpPan);
		vpPanLength = length;
		panStartTime = System.currentTimeMillis();
	}
	
	public static void setPanned(int x, int y)
	{
		vpPan = new Point(x*32, y*32);
		vpPanTarget = new Point(x*32, y*32);
		vpPanLength = 0.0f;
	}
	
	public static boolean isPanned()
	{
		return vpPan.equals(vpPanTarget);
	}
	
	private static Point partyBuffer = new Point(6, 4);
	public static void updateVpPos()
	{		
		if(map != null && map.isLoaded())
		{
			vpWorldPos.x = party.getX() - partyBuffer.x*32;
			vpWorldPos.y = party.getY() - partyBuffer.y*32;		
			
			if(party.getX() < partyBuffer.x*32)
				vpWorldPos.x = 0;
			
			if(party.getY() < partyBuffer.y*32)
				vpWorldPos.y = 0;
			
			if(party.getX() > map.Size().x*32 - (vpWidth - partyBuffer.x*32))
				vpWorldPos.x = map.Size().x*32 - vpWidth;
			
			if(party.getY() > map.Size().y*32 - (vpHeight - partyBuffer.y*32))
				vpWorldPos.y = map.Size().y*32 - vpHeight;		
			
			//update pan
			if(!vpPan.equals(vpPanTarget))
			{
				float delta = (System.currentTimeMillis() - panStartTime) / (vpPanLength * 1000.0f);
				
				if(delta < 1.0)
				{					
					vpPan.x = (int)(vpPanStart.x + (vpPanTarget.x - vpPanStart.x) * delta);
					vpPan.y = (int)(vpPanStart.y + (vpPanTarget.y - vpPanStart.y) * delta);
				}
				else
				{
					vpPan.x = vpPanTarget.x;
					vpPan.y = vpPanTarget.y;
				}
			}
			
			//apply pan
			vpWorldPos.x += vpPan.x;
			vpWorldPos.y += vpPan.y;
			
			vpGridPos.x = vpWorldPos.x/32;
			vpGridPos.y = vpWorldPos.y/32;	
		}
		
	}
	public static void delay()
	{
		inputDelay = true;
	}
	
	public static void ShowScene(String sceneName)
	{
		showScene = scenes.get(sceneName);
		showScene.load();
	}
	
	public static Icon createIcon(String name, int x, int y, float scale)
	{
		Icon i = new Icon(icons.get(name));
		i.scale = scale;
		i.move(x, y);
		
		return i;
	}
	
	public static BattleAnim playAnimation(String name, Point source, Point target){return playAnimation(name, source, target, false);}
	public static BattleAnim playAnimation(String name, Point source, Point target, boolean sendToBack)
	{
		BattleAnim anim = battleAnims.get(name);
		if(anim != null)
		{
			anim = new BattleAnim(anim);
			if(sendToBack) playingAnims.add(0, anim);
			else playingAnims.add(anim);
			anim.play(source, target);
			
			return anim;
		}
		
		return null;
	}
	public static BattleAnim playAnimation(BattleAnim anim, Point source, Point target){return playAnimation(anim, source, target, false);}
	public static BattleAnim playAnimation(BattleAnim anim, Point source, Point target, boolean sendToBack)
	{
		BattleAnim animCopy = new BattleAnim(anim);
		if(sendToBack) playingAnims.add(0, animCopy);
		else playingAnims.add(animCopy);
		
		animCopy.play(source, target);	
		
		return animCopy;
	}
	
	public static void clearAnimations()
	{
		playingAnims.clear();
	}
	
	public static void updateAnimations()
	{
		for(BattleAnim anim : playingAnims)
		{
			anim.update();
			if(anim.Done())
			{
				playingAnims.remove(anim);
				break;
			}
				
		}


	}
	
	public static void renderAnimations()
	{
		for(BattleAnim anim : playingAnims)
			anim.render();
	}

	public static void update()
	{
		//if(GameState != States.GS_TITLE)
		screenFader.update();
		BladeSong.instance().update();
		
		target.update();
		if(Global.playTimer != null)
			playTimer.update();
		
		updateAnimations();
		
		//update animation timer
		updateAnimation = false;
		imageTimer++;
		if(imageTimer >= imageSpeed)
		{
			updateAnimation = true;
			imageTimer = 0;
			
			animateTiles = !animateTiles;
		}
		
		//update
		switch(GameState)
		{
		case GS_MENUTRANSITION:
			if(screenFader.isDone())
			{
				GameState = transitionInto;
				screenFader.fadeIn(0.5f);
			}
				
			break;
		case GS_MAINMENU:
			menu.update();
			break;
		case GS_SAVELOADMENU:
			saveLoadMenu.update();
			if(saveLoadMenu.isClosed())
			{
				GameState = States.GS_WORLDMOVEMENT;
				delay();
				screenFader.fadeIn(0.5f);
			}				
			break;
		case GS_TITLE:
			title.update();
			break;
		//case GS_SHOWSCENE:
    	case GS_WORLDMOVEMENT:
    		updateReactionBubbles();
    		
    		if(worldMsgBox != null)
    			worldMsgBox.update();
    		
    		//runs any lingering state from before the mapchange
    		if(mapChangeCallingState != null)
    			if(mapChangeCallingState.isRunning())
    				mapChangeCallingState.update();
    			else
    				mapChangeCallingState = null;
    		
    		menuButton.update();
    		
    		if(debugButton != null)
    			debugButton.update();

    		if(gameOverObject.isRunning())
    			gameOverObject.update();
    		else if(map != null)
    			map.update();
    		
        	party.update();
        	updateVpPos();
        	screenShaker.update();
    		break;
    	case GS_PAUSE:
    		break;
    	case GS_BATTLE:
    		battle.update();
    		screenShaker.update();
    		break;  
    	case GS_BATTLETRANSITION:
    		imageScale += 0.10F*imageScale;
    		if(imageScale >= 15.0F)
    		{
    			imageScale = 1.0F;
    			GameState = States.GS_BATTLE;
    			update();
    			
    		}
    		
    		break;
    	case GS_MERCHANT:
    		merchantScreen.update();
    		break;
    	case GS_DEBUG:
    		debugScreen.update();
    		break;
    	case GS_LOADING:
    		if(mapLoadThread != null && mapLoadThread.isDone() && map != null)
    		{
    			map.playBGM(true);
    			GameState = States.GS_WORLDMOVEMENT; 
    			if(fadeInAfterLoad)
    				screenFader.fadeIn(2.0f);
    				
    			delay();
    		}
    		break;
    	case GS_NAMESELECT:
    		nameSelect.update();
    		break;
		default:
			break;
    	}
    			
		if(inputDelay)
		{
			delayTimer++;
			if(delayTimer >= delayLength)
			{
				delayTimer = 0;
				inputDelay = false;
			}
		}
	}

	public static Point inputToScreen(float x, float y)
	{
		
		//scale x and y coords to screen scale:		
		if(x > Global.screenWidth / 2)
			x = (int)((float)(x-Global.screenWidth/2.0F)/(Global.imageScale+Global.baseScale))+(int)(Global.screenWidth/2.0F);
		if(x < Global.screenWidth / 2)
			x = (int)(Global.screenWidth / 2.0F) - (int)(((Global.screenWidth/2.0F) - (float)x)/(Global.imageScale+Global.baseScale));
		if(y > Global.screenHeight / 2)
			y = (int)((float)(y-Global.screenHeight/2.0F)/(Global.imageScale+Global.baseScale))+(int)(Global.screenHeight/2.0F);
		if(y < Global.screenHeight / 2)
			y = (int)(Global.screenHeight / 2.0F) - (int)(((Global.screenHeight/2.0F) - (float)y)/(Global.imageScale+Global.baseScale));
		
		
		return new Point((int)x, (int)y);
	}
	
	public static Point vpToScreen(Point p)
	{
		Point p2 = new Point(p);
		setvpToScreen(p2);
		return p2;
	}
	public static void setvpToScreen(Point p)
	{
		p.set(p.x + ((screenWidth-vpWidth) / 2), p.y + ((screenHeight-vpHeight) / 2));
	}
	public static Rect vpToScreen(Rect r)
	{
		Rect r2 = new Rect(r);
		setvpToScreen(r2);
		return r2;
	}
	
	public static void setvpToScreen(Rect r)
	{
		r.set( (int)(r.left + ((float)(screenWidth-vpWidth) / 2.0f)), (int)(r.top + ((float)(screenHeight-vpHeight) / 2.0f)),
		       (int)(r.right + ((float)(screenWidth-vpWidth) / 2.0f)),(int)(r.bottom + ((float)(screenHeight-vpHeight) / 2.0f)));
	}
	public static int vpToScreenX(int x)
	{
		return x + ((screenWidth-vpWidth) / 2);
	}
	public static int vpToScreenY(int y)
	{
		return y + ((screenHeight-vpHeight) / 2);
	}
	public static int worldToScreenX(int x)
	{
		return x - vpWorldPos.x + ((screenWidth-vpWidth) / 2);
	}
	public static int worldToScreenY(int y)
	{
		return y - vpWorldPos.y + ((screenHeight-vpHeight) / 2);
	}
	public static Point worldToVP(Point p)
	{
		return new Point(p.x - vpWorldPos.x , p.y - vpWorldPos.y );
	}
	
	public static Point getVPCoordsFromObject(String name)
	{
		Point targetP = null;
		if(target.equals("party"))
			targetP = Global.party.getGridPos();
		else
			for(GameObject go : Global.map.Objects())
				if(go.Name().equals(target))
				{
					targetP = go.getGridPos();
					break;
				}						

		if(targetP != null)
		{
			targetP = new Point(targetP.x * 32 + 16, targetP.y * 32 + 16);
			targetP = Global.worldToVP(targetP);				
		}
		
		return targetP;
	}
	
	public static Rect screenToVP(Rect r)
	{
		return new Rect(
				screenToVPX(r.left),
				screenToVPY(r.top),
				screenToVPX(r.right),
				screenToVPY(r.bottom));
	}
	
	public static int screenToVPX(int x)
	{
		return x - ((screenWidth-vpWidth) / 2);
	}
	public static int screenToVPY(int y)
	{
		return y - ((screenHeight-vpHeight) / 2);
	}
	public static int screenToWorldX(int x)
	{
		return x - ((screenWidth-vpWidth) / 2) + vpWorldPos.x;
	}
	public static int screenToWorldY(int y)
	{
		return y - ((screenHeight-vpHeight) / 2) + vpWorldPos.y;
	}
	
	public static void beginBattle(String en, boolean allowGameOver)
	{
		encounter = en;
		GameState = States.GS_BATTLETRANSITION;
		
		battle = new Battle();
		
		if(encounters.get(en).isBossFight)
			BladeSong.instance().play("boss", true, true, 0);
		else
			BladeSong.instance().play("battle", true, true, 0);		
		
		battle.startBattle(encounter, allowGameOver);
	}
	
	public static void startGame()
	{
		if(!appRunning)
        {
        	appRunning = true;
        	
        	
        	loadResources(); 
        	//createWorld();
        	
        	title= new TitleScreen();
        	saveLoader = new GameSaveLoader();
        	saveLoadMenu = new SaveLoadMenu();
        	playingAnims = new ArrayList<BattleAnim>();
        	GameState = States.GS_TITLE;
        	title.titleStart();
        	BladeSong.instance().play("", false, true, 0);
        	
        	
        	Paint paint = textFactory.getTextPaint(13, Color.WHITE, Align.CENTER);
        	menuButton = new ListBox(vpWidth, vpHeight, 0, 40, 1, 1, paint);
        	menuButton.setOpenSize(80,  40);
        	menuButton.anchor = Anchors.BottomRight;
        	menuButton.addItem("Menu", null, false);
        	menuButton.update();
        	
        	
        	//create debug button
        	
        	//TODO: REMOVE THIS PART FOR RELEASE
        	debugButton = new ListBox(0, vpHeight, 40, 40, 1, 1, paint);
        	debugButton.anchor = Anchors.BottomLeft;
        	debugButton.addItem("!", null, false);
        	debugButton.update();
        	//TODO: REMOVE THIS PART FOR RELEASE
        }
	}
	
	public static void restartGame()
	{
		screenFader.setFadeColor(255, 255, 255, 255);
		screenFader.setFaded();
		
		if(map != null)
			map.unloadTiles();
		title= new TitleScreen();
    	GameState = States.GS_TITLE;
    	
    	screenFader.fadeIn(1.0f);
    	
    	title.titleStart();
    	BladeSong.instance().stop();
    	menuButton.setClosed();
    	audioMgr = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        audioMgr.requestAudioFocus(activity, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
       
	}
	
	public static void closeGame()
	{
		BladeSong.instance().stop();
		appRunning = false;
        activity.panel.destroyContext();
        activity.finish();
	}
	
	private static void CreateReactionBubble(String name, int frameLength, int x, int y, int frameCount)
	{
		
		Sprite spr = new Sprite(name, "reactionbubbles", 32, 64);	
		
		for(int i = 0; i < frameCount; ++i)
			spr.addFrame("down", new Rect((x+i)*16, y*32, (x+i)*16+16, y*32+32));
		
		spr.changeFace("down");
		
		Global.reactionBubbles.put(name, new ReactionBubble(spr, frameLength));

	}
	
	private static void genReactionBubbles()
	{
		reactionBubbles = new HashMap<String, ReactionBubble>();
		CreateReactionBubble("blank", 0, 0, 0, 1);
		CreateReactionBubble("sleep", 20, 1, 0, 4);
		CreateReactionBubble("qmark", 5, 5, 0, 2);
		CreateReactionBubble("angry", 5, 7, 0, 4);
		CreateReactionBubble("dots", 15, 0, 1, 3);
		CreateReactionBubble("exclam", 5, 3, 1, 3);
		CreateReactionBubble("sweat", 8, 6, 1, 3);
		
	}
	
	//created all the sprites and faces for a standard character in the spritesheet
	//x and y are the coordinates for the 3x4 group of sprites in the tileset
	public static void createWorldSprite(String bitmap, String name, int x, int y)
	{
		Sprite spr = new Sprite(name, bitmap, 32, 32);		
		
		spr.addFrame("down",16, x*3+1, y*4+0);
		spr.addFrame("down", 16, x*3+2, y*4+0);	
		
		spr.addFrame("up", 16, x*3+1, y*4+1);
		spr.addFrame("up", 16, x*3+2, y*4+1);
		
		spr.addFrame("left", 16, x*3+1, y*4+2);
		spr.addFrame("left", 16, x*3+2, y*4+2);
		
		spr.addFrame("right", 16, x*3+1, y*4+3);
		spr.addFrame("right", 16, x*3+2, y*4+3);
		
		spr.changeFace("down");
		
		sprites.put(name, spr);		
	}
	
	public static void createBattleSprite(String name, int x, int y)
	{
		BattleSprite bSpr = new BattleSprite(name, "herobattlers", 64, 64);		
		
		bSpr.addFrame(BattleSprite.faces.Idle, 32, x*3+2, y*5+0);
		
		bSpr.addFrame(BattleSprite.faces.Attack, 32, x*3+1, y*5+0);
		bSpr.addFrame(BattleSprite.faces.Attack, 32, x*3+0, y*5+0);
		bSpr.addFrame(BattleSprite.faces.Attack, 32, x*3+0, y*5+0);//3rd frame repeats frame 2
		
		bSpr.addFrame(BattleSprite.faces.Use, 32, x*3+1, y*5+1);
		
		bSpr.addFrame(BattleSprite.faces.Ready, 32, x*3+2, y*5+1);
		
		bSpr.addFrame(BattleSprite.faces.Cast, 32, x*3+0, y*5+2);
		
		bSpr.addFrame(BattleSprite.faces.Casting, 32, x*3+1, y*5+2);
		bSpr.addFrame(BattleSprite.faces.Casting, 32, x*3+2, y*5+2);
		
		bSpr.addFrame(BattleSprite.faces.Victory, 32, x*3+2, y*5+4);
		bSpr.addFrame(BattleSprite.faces.Victory, 32, x*3+1, y*5+4);
		
		bSpr.addFrame(BattleSprite.faces.Dead, 32, x*3+0, y*5+3);
		
		bSpr.addFrame(BattleSprite.faces.Weak, 32, x*3+1, y*5+3);
		
		bSpr.addFrame(BattleSprite.faces.Damaged, 32, x*3+2, y*5+3);
		
		battleSprites.put(name, bSpr);
	}

	public static void createEnemySprite(String name, String bitmap, int destSize, int srcSize, int srcX, int srcY)
	{
		BattleSprite bs = new BattleSprite(name, bitmap, destSize, destSize);
		bs.addFrame(BattleSprite.faces.Idle, srcSize, srcX, srcY);
		battleSprites.put(name, bs);
	}
	
	private static void loadBitmaps(String path)
	{
		String[] files = null;
		String bmpName;		

		try{ files = activity.getAssets().list(path); } catch (Exception e) {
			Log.d(TAG, "Unable to list files in Dir " + path);
			closeGame();}
		
		InputStream is = null;
		for(String filename : files)
		{
			if(filename.indexOf('.') != -1)
			{
				try {is = activity.getAssets().open(path+"/"+filename);} catch (Exception e) {
					Log.d(TAG, "Unable to open file "+path+"/"+filename);
					closeGame();}
				
				bmpName = filename.substring(0, filename.lastIndexOf('.'));
				
				if(is != null)
					bitmaps.put(bmpName, BitmapFactory.decodeStream(is));
			}			
			
		}
		
	}
	
	
	
	//TODO: Script this.
	
	public static AnimatedBitmap getBeserkerBase()
	{
		return new AnimatedBitmap()	
		{
			
			BitmapFrame[] frames;
			{
				final int height = 51;
				final int width = 49;
				
				frames = new BitmapFrame[4];
				for (int i = 0; i < 4; ++i)
				{
					frames[i] = new BitmapFrame(bitmaps.get("beserkstance"),new Rect(i*width, 0, (i+1)*width, height));
				}
			}
			@Override
			public BitmapFrame[] getFrames() {
				return frames;
			}
			
		};
	}
	
	public static AnimationBuilder getIceBarrage()
	{
		return new AnimationBuilder()
		{
			public BattleAnim buildAnimation(BattleEventBuilder builder) {

				//everything is at a -45 degree angle, give or take a couple degrees.
				//generate more and more and more icicles, and make them poof into small snow clouds on impact that fade gracefully up at an angle.
				
				BattleAnim out = new BattleAnim(1000.0f); //working in milliseconds
				
				final int icicleCount = 50;
				final float iceStormLength = 2500;
				final float icicleLife = 500;  
				
				Bitmap icicleBitmap = Global.bitmaps.get("icicle"); 
				Rect icicleRect = new Rect(0,0,30,80);	
				
				Bitmap icePoof = Global.bitmaps.get("particles");
				Rect poofRect = new Rect(1,13,13,24);
				
				for (int i = 0; i < icicleCount; ++i) 
				{
					BattleAnimObject icicle = new BattleAnimObject(Types.Bitmap, false, icicleBitmap);
					BattleAnimObject poofObj = new BattleAnimObject(Types.Bitmap, false, icePoof);
					
					
					float angle = Global.rand.nextFloat() * 10.0f + 65.0f;
					double angRad = (angle) * (Math.PI/180.0f);
					
					float xDir = (float)Math.cos(angRad);
					float yDir = (float)Math.sin(angRad);
					
					float reflY = yDir * -1;
					
					final int perspectiveSize = 80;
					final int hitBuffer = 300;
					//a bit of buffer on the end so as to not mess with the UI
					float targetX = (float)(hitBuffer + Global.rand.nextInt(Global.vpWidth-hitBuffer-64));
					float targetY = perspectiveSize + Global.rand.nextInt(Global.vpHeight - perspectiveSize-32);
					
					float startX = targetX - (xDir * 400.0f);
					float startY = targetY - (yDir * 400.0f);
					
					
					
					
					float t = ((float)i)/(icicleCount-1);

					//start state
					BattleAnimObjState state = new BattleAnimObjState((int)(t * iceStormLength), PosTypes.Screen);
					state.size = new Point(icicleRect.width()/3, icicleRect.height()/3);
					state.pos1 = new Point((int)startX, (int)startY);
					state.argb(255, 255, 255, 255);
					state.rotation = 270+angle;  //angle += 270
					state.setBmpSrcRect(icicleRect.left, icicleRect.top, icicleRect.right, icicleRect.bottom);
					icicle.addState(state);
					//end state
					int hitTime = (int)(t * iceStormLength+ icicleLife);
					state = new BattleAnimObjState((int)(hitTime), PosTypes.Screen);
					state.size = new Point(icicleRect.width()/3, icicleRect.height()/3);
					state.pos1 = new Point((int)targetX, (int)targetY);
					state.argb(255, 255, 255, 255);
					state.rotation = 270+angle;
					state.setBmpSrcRect(icicleRect.left, icicleRect.top, icicleRect.right, icicleRect.bottom);
					icicle.addState(state);					

					icicle.interpolateLinearly();
					
					int poofCount = Global.rand.nextInt(2) + 1;
					
					for (int j = 0; j < poofCount; ++j)
					{
						
						float endX = targetX + xDir * 8.0f + Global.rand.nextFloat() * 3.0f - 1.5f;
						float endY = targetY + reflY * 8.0f + Global.rand.nextFloat() * 3.0f - 1.5f;

						int rot = Global.rand.nextInt(360);
						state = new BattleAnimObjState(hitTime, PosTypes.Screen);
						state.size = new Point(poofRect.width(), poofRect.height());
						state.pos1 = new Point((int)targetX, (int)targetY);
						state.argb(196, 255, 255, 255);
						state.rotation = rot;
						state.setBmpSrcRect(poofRect.left, poofRect.top, poofRect.right, poofRect.bottom);
						
						poofObj.addState(state);
						
						int left = 200 + Global.rand.nextInt(100);
						
						state = new BattleAnimObjState(hitTime + left, PosTypes.Screen);
						state.size = new Point(poofRect.width(), poofRect.height());
						state.pos1 = new Point((int)endX, (int)endY);
						state.argb(0, 255, 255, 255);
						state.rotation = rot + Global.rand.nextInt(10) - 5;
						state.setBmpSrcRect(poofRect.left, poofRect.top, poofRect.right, poofRect.bottom);						
						
						poofObj.addState(state);
					}
					
					out.addObject(icicle);
					out.addObject(poofObj);
				}				
				
				return out;
			}
		};
	}
	public static AnimationBuilder getEntombAnim()
	{
		return new AnimationBuilder()
		{
			
			@Override
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f); //working in milliseconds
				
				final int icicleCount = 20;
				final int icicleStrikeWait = 1250;
				final int icicleStrikeTime = 205;
				final int icicleSpawnGap = 60;
				final int strikeGap = 40;
				final int iciclePoofLife = 750;
			//	final int icicleAdvanceTime = 100;
				final float icicleRadius = 80.0f;
				
				Bitmap icicleBitmap = Global.bitmaps.get("icicle"); 
				Rect icicleRect = new Rect(0,0,30,80);	
				
				Bitmap icePoof = Global.bitmaps.get("particles");
				Rect poofRect = new Rect(1,13,13,24);
				
				//inflict frozen status partially through this!
				
				for (int i = 0; i < icicleCount; ++i)
				{
					BattleAnimObject icicle = new BattleAnimObject(Types.Bitmap, false, icicleBitmap);
					
					int startTime = Global.rand.nextInt(strikeGap * icicleCount);
					
					float radius = icicleRadius + Global.rand.nextFloat() * 16.0f;
					
					float angle =  (float)(Math.PI - (((float)i)/icicleCount-1) * Math.PI);
					float x = (float)(Math.cos(angle) * radius);
					float y = (float)(Math.sin(angle) * radius) + 16.0f;  //offset down slightly to feel a bit more entomb-y
										
					float drawAngle =(float)(90  + (angle * 180/Math.PI));
					
					BattleAnimObjState state = new BattleAnimObjState(i * icicleSpawnGap, PosTypes.Target);
					state.pos1 = new Point((int)x, (int)y);
					state.size = new Point(icicleRect.width()/3, icicleRect.height()/3);
					state.argb(255, 255, 255, 255);
					state.rotation = drawAngle;  //angle += 270
					state.setBmpSrcRect(icicleRect.left, icicleRect.top, icicleRect.right, icicleRect.bottom);
					icicle.addState(state);		

					//hang in the air a bit.....
					state = new BattleAnimObjState(startTime + icicleStrikeWait, PosTypes.Target);
					state.pos1 = new Point((int)x, (int)y);
					state.size = new Point(icicleRect.width()/3, icicleRect.height()/3);
					state.argb(255, 255, 255, 255);
					state.rotation = drawAngle;  //angle += 270
					state.setBmpSrcRect(icicleRect.left, icicleRect.top, icicleRect.right, icicleRect.bottom);
					icicle.addState(state);		
					
					//Strike!  This should be pretty fast
					state = new BattleAnimObjState(startTime + icicleStrikeWait + icicleStrikeTime, PosTypes.Target);
					state.pos1 = new Point(0,0);
					state.size = new Point(icicleRect.width()/3, icicleRect.height()/3);
					state.argb(255, 255, 255, 255);
					state.rotation = drawAngle;
					state.setBmpSrcRect(icicleRect.left, icicleRect.top, icicleRect.right, icicleRect.bottom);
					icicle.addState(state);		
					
					out.addObject(icicle);
					
					//add a poof at this point that's fairly long-lived.
					BattleAnimObject poofAnim = new BattleAnimObject(Types.Bitmap, false, icePoof);
					
					float rnd = Global.rand.nextFloat() * 360.0f;
					state = new BattleAnimObjState(startTime + icicleStrikeWait +  icicleStrikeTime, PosTypes.Target);
					state.size = new Point((int)(poofRect.width()*4.5f), (int)(poofRect.height()*4.5f));
					state.pos1 = new Point(0,0);
					state.argb(196, 255, 255, 255);
					state.rotation = rnd;
					state.setBmpSrcRect(poofRect.left, poofRect.top, poofRect.right, poofRect.bottom);
					poofAnim.addState(state);
					
					
					int driftX = Global.rand.nextInt(96) - 48;
					int driftY = Global.rand.nextInt(96) - 48;
					
					state = new BattleAnimObjState(startTime + icicleStrikeTime + icicleStrikeWait + iciclePoofLife, PosTypes.Target);
					state.size = new Point(poofRect.width()*3, poofRect.height()*3);
					state.pos1 = new Point(driftX,driftY);
					state.argb(0, 255, 255, 255);
					state.rotation = rnd;
					state.setBmpSrcRect(poofRect.left, poofRect.top, poofRect.right, poofRect.bottom);
					poofAnim.addState(state);
					
					out.addObject(poofAnim);
				}
				
				return out;
			}			
		};
	}
	
	

	public static AnimationBuilder getIgniteAnim()
	{
		return new AnimationBuilder()
		{
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f);
				
				Point playerPos = builder.getSource().getPosition(true);
				Point targetPos = BattleAction.getTarget(builder).getPosition(true);
				
				//shoot a fireball at an enemy which explodes rotated to their direction.
				
				//degrees
				float rotation = PointMath.angleBetween(playerPos, targetPos) * (180/(float)Math.PI);

				
				BitmapFrame[] frames = Global.getFireAnim().getFrames();
				
				//this fireball should be small and go fast.
				BattleAnimObject fireball = new BattleAnimObject(Types.Bitmap, false, frames[0].bitmap);
				
				
				BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Screen);
				state.size = new Point((int)(frames[0].srcRect.width() * 0.5f), (int)(frames[0].srcRect.height() * 0.5f));
				state.pos1 = playerPos;
				state.argb(255, 255, 255, 255);
				state.rotation = 0.0f;

				Rect rect = frames[0].srcRect;
				state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
				fireball.addState(state);
				
				state = new BattleAnimObjState(150, PosTypes.Screen);
				state.size = new Point((int)(frames[0].srcRect.width() * 0.5f), (int)(frames[0].srcRect.height() * 0.5f));
				state.pos1 = targetPos;
				state.argb(255, 255, 255, 255);
				state.rotation = 0.0f;

				state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
				fireball.addState(state);		
				
				fireball.interpolateLinearly();
				
				out.addObject(fireball);
				
				final int frameWaitTime = 100;
				
				//now, to add the explosion.
				

				BattleAnimObject explosion = new BattleAnimObject(Types.Bitmap, false, frames[0].bitmap);
				for (int i = 0; i < frames.length+1; ++i)
				{
					BitmapFrame frame = frames[Math.min(i, frames.length-1)];
					state = new BattleAnimObjState(150 + i * frameWaitTime, PosTypes.Target);
					state.size = new Point((int)(frame.srcRect.width() * 1.5f), (int)(frame.srcRect.height() * 1.5f));
					state.pos1 = new Point(0,0);
					state.argb(255, 255, 255, 255);
					state.rotation = rotation + 90.0f;

					rect = frame.srcRect;
					state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
					explosion.addState(state);
				}
				out.addObject(explosion);
				
				return out;
			}
		};
	}
	
	public static AnimationBuilder getIgniteSmokeAnim()
	{
		return new AnimationBuilder()
		{
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f);
				
				Point playerPos = builder.getSource().getPosition(true);
				Point targetPos = BattleAction.getTarget(builder).getPosition(true);
				
		
				final float riseVel = -3.0f;
				final float airResist = 0.96f;
				final int steps = 10;
				int smokeParticles = Global.rand.nextInt(8) + 6;
				final int stepTime = 100;
				
				Bitmap smoke = Global.bitmaps.get("particles");
				Rect poofRect = new Rect(1,13,13,24); 
				

				for (int i = 0; i < smokeParticles; ++i)
				{
					
					float rotation = PointMath.angleBetween(playerPos, targetPos);
					
					rotation += (float)(Math.PI / 180.0 *( Global.rand.nextFloat() * 16 - 8));
				
					float x = (float)Math.cos(rotation);
					float y = (float)Math.sin(rotation);
					
					float velocity = 3.0f + Global.rand.nextFloat()*16;
					Point position = new Point(targetPos.x + Global.rand.nextInt(16)-8,
											   targetPos.y + Global.rand.nextInt(16)-8);
					
					
					float pX = position.x;
					float pY = position.y;
					
					float sprRotation = Global.rand.nextFloat()*360.0f;
					
					int startTime = 150 + Global.rand.nextInt(200) + 25;
					
					BattleAnimObject smokeObj = new BattleAnimObject(Types.Bitmap, false, smoke);
					
					int RGB = Global.rand.nextInt(32);
					
					for (int j = 0; j < steps; ++j)
					{
						
						BattleAnimObjState state = new BattleAnimObjState(startTime + j * stepTime, PosTypes.Screen);
						state.size = new Point(poofRect.width()*2 - ((j*poofRect.width()*2)/(steps-1)), 
								               poofRect.height()*2 - ((j*poofRect.height()*2)/(steps-1)) );
						state.pos1 = position;
						state.argb(196, RGB, RGB, RGB);
						state.colorize = 1.0f;
						state.rotation = sprRotation;
						
						state.setBmpSrcRect(poofRect.left, poofRect.top, poofRect.right, poofRect.bottom);
						
						smokeObj.addState(state);
						
						velocity *= airResist;
						pY += riseVel + y * velocity;
						pX += x*velocity;
						
						position = new Point((int)pX, (int)pY);
					}	
					
					out.addObject(smokeObj);
				}
				
				return out;
			}
		};
	}	


	public static AnimationBuilder getHealAnim()
	{
		return new AnimationBuilder()
		{
			public BattleAnim buildAnimation(BattleEventBuilder builder) 
			{
				BitmapFrame[] frames = Global.getSparkleParticle().getFrames();
				BattleAnim out = new BattleAnim(60.0f);
				
				final int particleCount = 20;
				
				int particleDelay = 2;
				int sparkleDelay = 12;
				int riseRate = 16;
				
				for (int j = 0; j < particleCount; ++j) 
				{
					BattleAnimObject sparkle = new BattleAnimObject(Types.Bitmap, false, frames[0].bitmap);
					
					int xOffset = Global.rand.nextInt(36) - 18;
					int yOffset =  Global.rand.nextInt(8) + 32;
					
					int g = Global.rand.nextInt(64) + 256-64;

					for(int i = 0; i < frames.length; ++i)
					{
						int width = (int)(frames[i].srcRect.width() * 2.5f);
						int height = (int)(frames[i].srcRect.height() * 2.5f);						
						BattleAnimObjState state = new BattleAnimObjState(j*particleDelay + i * sparkleDelay, PosTypes.Target);
						state.size = new Point(width  - (width * i)/(frames.length-1), 
											   height - (height* i)/(frames.length-1));
						
						state.pos1 = new Point(xOffset,yOffset-(riseRate * i));
						state.argb(255, 0, g, 0);
						state.colorize = 1.0f;
						Rect r = frames[i].srcRect;
						state.setBmpSrcRect(r.left, r.top, r.right, r.bottom);
						sparkle.addState(state);
					}
					out.addObject(sparkle);
				}
				return out;				
			}
		};
	}
	public static AnimationBuilder getDrainAnim()
	{
		return new AnimationBuilder()
		{
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f);
				
				
				Point playerPos = builder.getSource().getPosition(true);
				Point targetPos = BattleAction.getTarget(builder).getPosition(true);
				
				final int steps = 8;
				final int stepTime = 80;
				final int stepDelay = 35;
				
				final int particleNumber = 30;
				final float radius = 40.0f;

				BitmapFrame[] frames = Global.getSparkleParticle().getFrames();
				
				for (int i = 0; i < particleNumber; ++i)
				{
				    float t = ((float)i)/(particleNumber-1);
					
					int red = Global.rand.nextInt(120) + 120;
					
					BattleAnimObject blood = new BattleAnimObject(Types.Bitmap, false, frames[0].bitmap);
					
					int frame = Global.rand.nextInt(frames.length);
					float angle = Global.rand.nextFloat() * 360.0f;
							
					double angRad = (angle) * (Math.PI/180.0f);
							
					float rad = radius * (0.5f + Global.rand.nextFloat() * 0.5f);
					
					float x = (float)(Math.cos(angRad) * rad);
					float y = (float)(Math.sin(angRad) * rad);
					
					BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Target);
					state.size = new Point((int)(frames[frame].srcRect.width() * 1.5f), (int)(frames[frame].srcRect.height() * 1.5f));
					state.pos1 = new Point(0,0);
					state.argb(255, red, 0, 0);
					state.colorize = 1.0f;
					state.rotation = angle;

					Rect rect = frames[frame].srcRect;
					state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
					blood.addState(state);
					
					state = new BattleAnimObjState(200, PosTypes.Target);
					state.size = new Point((int)(frames[frame].srcRect.width() * 1.5f), (int)(frames[frame].srcRect.height() * 1.5f));
					state.pos1 = new Point((int)x,(int)y);
					state.argb(255, red, 0, 0);
					state.colorize = 1.0f;
					state.rotation = angle;

					state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
					blood.addState(state);					
					
					
					//fun part
					List<Point> points = PointMath.getArc(PointMath.offset(targetPos,x,y), playerPos, steps, t-.5f);
					
					int arg = 0;
					for (Point p : points)
					{
						state = new BattleAnimObjState(400 + stepDelay * i + (arg++) *stepTime, PosTypes.Screen);
						state.size = new Point((int)(frames[frame].srcRect.width() * 1.0f), (int)(frames[frame].srcRect.height() * 1.0f));
						state.pos1 = new Point(p.x, p.y);
						state.argb(255, (int)(red/1.35f), 0, 0);
						state.colorize = 1.0f;
						state.rotation = angle;					
						
						state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
						blood.addState(state);			
					}
					
					out.addObject(blood);
				}
				
				return out;
			}
		};
	}	
	public static AnimationBuilder getPotionAnim()
	{
		return new AnimationBuilder()
		{
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f);
				
				
				//put a whole bunch of sparkles, then suck them in.
				//spiral
				
				final int particleNumber = 30;
				final float radius = 40.0f;

				BitmapFrame[] frames = Global.getSparkleParticle().getFrames();
				
				for (int i = 0; i < particleNumber; ++i)
				{
				
					int r = Global.rand.nextInt(225);
					int gb = Global.rand.nextInt(120) + 135;
					
					if (r > gb) r = gb;
					BattleAnimObject sparkle = new BattleAnimObject(Types.Bitmap, false, frames[0].bitmap);
					
					int frame = Global.rand.nextInt(frames.length);
					float angle = Global.rand.nextFloat() * 360.0f;
							
					double angRad = (angle) * (Math.PI/180.0f);
							
					float rad = radius * (0.5f + Global.rand.nextFloat() * 0.5f);
					
					float x = (float)(Math.cos(angRad) * rad);
					float y = (float)(Math.sin(angRad) * rad);
					
					BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Target);
					state.size = new Point((int)(frames[frame].srcRect.width() * 1.5f), (int)(frames[frame].srcRect.height() * 1.5f));
					state.pos1 = new Point((int)x,(int)y);
					state.argb(255, r, gb, gb);
					state.colorize = 1.0f;
					state.rotation = angle;

					Rect rect = frames[frame].srcRect;
					state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
					sparkle.addState(state);
					
					state = new BattleAnimObjState(100, PosTypes.Target);
					state.size = new Point((int)(frames[frame].srcRect.width() * 1.5f), (int)(frames[frame].srcRect.height() * 1.5f));
					state.pos1 = new Point((int)x,(int)y);
					state.argb(255, r, gb, gb);
					state.colorize = 1.0f;
					state.rotation = angle;

					state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
					sparkle.addState(state);					
					
					state = new BattleAnimObjState(400, PosTypes.Target);
					state.size = new Point((int)(frames[frame].srcRect.width() * 0.5f), (int)(frames[frame].srcRect.height() * 0.5f));
					state.pos1 = new Point(0, 0);
					state.argb(64, r, gb, gb);
					state.colorize = 1.0f;
					state.rotation = angle;					
					
					state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
					sparkle.addState(state);
			
					
					out.addObject(sparkle);
				}
				
				return out;
			}
		};
	}
		
	
	public static AnimationBuilder getAntidoteAnim()
	{
		return new AnimationBuilder()
		{
			public BattleAnim buildAnimation(BattleEventBuilder builder) {
				BattleAnim out = new BattleAnim(1000.0f);
				
				
				//spiral
				
				final int antidoteSpiralPoints = 6;
				final int trail = 3;
				final int subStep = 32;
				final int subSteps = 5;
				final float radius = 40.0f;
				final int stepLen = 150;
				
				int step = 360/(antidoteSpiralPoints);
				int baseAngle = step;


				BitmapFrame[] frames = Global.getSparkleParticle().getFrames();
				
				for (int i = 0; i < antidoteSpiralPoints; ++i)
				{
					
					BattleAnimObject[] sparkle = new BattleAnimObject[trail];
					
					for (int j = 0; j < trail; ++j)
					{
						sparkle[j] = new BattleAnimObject(Types.Bitmap, false, frames[0].bitmap);	
					}
					
					
					for (int j = 0; j < subSteps; ++j)
					{
						for (int k = 0; k < trail; ++k)
						{
						
							
							int idx = j + k;
							if (idx >= subSteps) continue;
							
							int frame = idx % (frames.length-1);
							float r = (radius / (subSteps-1)) * ((subSteps-1)-idx);
							int angle = baseAngle + subStep * idx;
							
							double angRad = (angle) * (Math.PI/180.0f);
							
							float x = (float)(Math.cos(angRad) * r);
							float y = (float)(Math.sin(angRad) * r);
							
							BattleAnimObjState state = new BattleAnimObjState(j*stepLen, PosTypes.Target);
							state.size = new Point((int)(frames[frame].srcRect.width() * 1.0f), (int)(frames[frame].srcRect.height() * 1.0f));
							state.pos1 = new Point((int)x,(int)y);
							state.argb(255, 0, 255 - (trail-k-1)*20, 0);
							state.colorize = 1.0f;
							state.rotation = Global.rand.nextFloat()*360.0f;
							
							Rect rect = frames[frame].srcRect;
							state.setBmpSrcRect(rect.left, rect.top, rect.right, rect.bottom);
							sparkle[k].addState(state);
						}
					}
					
					for (int j = 0; j < trail; ++j)
					{
						out.addObject(sparkle[j]);	
					}
					
					baseAngle += step;
				}
				
				return out;
			}
		};
	}
	
	
	public static AnimatedBitmap getBeserkerSwords()
	{
		return new AnimatedBitmap()	
		{
			
			BitmapFrame[] frames;
			{
				final int top = 50;
				final int height = 14;
				final int width = 15;
				
				frames = new BitmapFrame[5];
				for (int i = 0; i < 5; ++i)
				{
					frames[i] = new BitmapFrame(bitmaps.get("beserkstance"),new Rect(i*width, top, (i+1)*width, top + height));
				}
			}
			@Override
			public BitmapFrame[] getFrames() {
				return frames;
			}
			
		};
	}
	
	public static AnimatedBitmap getSparkleParticle()
	{
		return new AnimatedBitmap()	
		{

			BitmapFrame[] frames;
			{
				frames = new BitmapFrame[5];
				for (int i = 0; i < 5; ++i)
				{
					int idx = i;
					if (i > 2) idx = 4-i;
					frames[i] = new BitmapFrame(bitmaps.get("particles"),new Rect(idx*8, 0, (idx+1)*8, 8));
				}
			}
			@Override
			public BitmapFrame[] getFrames() {
				return frames;
			}
			
		};
	}

	public static AnimatedBitmap getFireAnim()
	{
		return new AnimatedBitmap()	
		{

			BitmapFrame[] frames;
			{
				frames = new BitmapFrame[6];
				for (int i = 0; i < 6; ++i)
				{
					frames[i] = new BitmapFrame(bitmaps.get("fire"),new Rect(i*53, 0, (i+1)*53, 53));
				}
			}
			@Override
			public BitmapFrame[] getFrames() {
				return frames;
			}
			
		};
	}
	
	private static void loadScenes(String path)
	{
		String[] files = null;
		String bmpName;		

		try{ files = activity.getAssets().list(path); } catch (Exception e) {
			Log.d(TAG, "Unable to list files in Dir " + path);
			closeGame();}
		
		InputStream is = null;
		for(String filename : files)
		{
			if(filename.indexOf('.') != -1)
			{
				try {is = activity.getAssets().open(path+"/"+filename);} catch (Exception e) {
					Log.d(TAG, "Unable to open file "+path+"/"+filename);
					closeGame();}
				
				bmpName = filename.substring(0, filename.lastIndexOf('.'));
				
				if(is != null)
					scenes.put(bmpName, new Scene(is));
			}
			
			
		}
		
	}
	public static void loadMaps(String path)
	{
		String[] files = null;
		String mapName;		

		try{ files = activity.getAssets().list(path); } catch (Exception e) {
			Log.d(TAG, "Unable to list files in Dir " + path);
			closeGame();}
		

		for(String filename : files)
		{
			if(filename.indexOf('.') != -1)
			{
				mapName = filename.substring(0, filename.lastIndexOf('.'));
				maps.put(mapName, path+"/"+filename);	
			}
					
		}
		
	}

	private static void loadSound(String path)
	{
		AssetFileDescriptor afd = null;
		String[] files = null;
		String sfxName;	
		
		path += "/effects";
		try{ files = activity.getAssets().list(path); } catch (Exception e) {
			Log.d(TAG, "Unable to list files in Dir " + path);
			closeGame();}
		
		for(String filename : files)
		{
			if(filename.indexOf('.') != -1)
			{
				try {afd = activity.getAssets().openFd(path+"/"+filename);} catch (Exception e) {
					Log.d(TAG, "Unable to open file "+path+"/"+filename);
					closeGame();}
				
				sfxName = filename.substring(0, filename.lastIndexOf('.'));
				
				if(afd != null)
					sfx.put(sfxName, soundPool.load(afd, 1));
			}						
		}
		
	}
	private static void genIcons()
	{
		icons = new HashMap<String, Icon>();	
		icons.put("dagger", new Icon(0,0));icons.put("sword", new Icon(1,0));
		icons.put("whip", new Icon(2,0));icons.put("bow", new Icon(3,0));
		icons.put("hammer", new Icon(4,0));icons.put("spear", new Icon(5,0));
		
		icons.put("gun", new Icon(0,1));icons.put("boomerang", new Icon(1,1));
		icons.put("claw", new Icon(2,1));icons.put("staff", new Icon(3,1));
		icons.put("axe", new Icon(4,1));icons.put("book", new Icon(5,1));
		
		icons.put("magisword", new Icon(0,2));
		
		icons.put("ltorso", new Icon(0,3));icons.put("htorso", new Icon(1,3));
		icons.put("lshield", new Icon(2,3));icons.put("hshield", new Icon(3,3));
		icons.put("lhelmet", new Icon(4,3));icons.put("hhelmet", new Icon(5,3));
		
		icons.put("ring", new Icon(0,4));icons.put("bracelet", new Icon(1,4));
		icons.put("crystal", new Icon(2,4));icons.put("boot", new Icon(3,4));
		icons.put("cape", new Icon(4,4));icons.put("orb", new Icon(5,4));
		
		icons.put("poison", new Icon(0,5));icons.put("KO", new Icon(1,5));
		icons.put("mute", new Icon(2,5));icons.put("frozen", new Icon(3,5));
		icons.put("blind", new Icon(4,5));icons.put("confuse", new Icon(5,5));
		
		icons.put("stun", new Icon(0,6));	
		
		icons.put("arrow", new Icon(1, 6));	
	}
	
	private static void genWeaponSwings()
	{
		weaponSwingModels.put("sword", new WeaponSwing("sword", 0, 0));
		weaponSwingModels.put("dagger", new WeaponSwing("dagger", 0, 1));
		weaponSwingModels.put("staff", new WeaponSwing("staff", 0, 2));
	}
	public static void loadResources()
	{
		updatePaints();
		renderer = new Renderer();
		screenFader = new ScreenFader();
		screenShaker = new ScreenShaker();
		target = new TargetReticle();
		textFactory = new TextFactory(Typeface.createFromAsset(activity.getAssets(),"fonts/pressstart.ttf"));
		playingReactions = new HashMap<String,ReactionBubble>();
		
		bitmaps = new HashMap<String, Bitmap>();
		loadBitmaps("drawable/characters");
		loadBitmaps("drawable/misc");
		loadBitmaps("drawable/misc/title");
		loadBitmaps("drawable/tilesets");	
		createAnimationBuilders();

		scenes = new HashMap<String, Scene>();
		loadScenes("drawable/scenes");		
		genIcons();
		genReactionBubbles();
		
		weaponSwingModels = new HashMap<String, WeaponSwing>();
		genWeaponSwings();
		
		//create shared tileplate bmp's
		tilePlateBmps = new TilePlateBitmap[tilePlateBitmapCount];
		for(int i = 0; i < tilePlateBitmapCount; ++i)
			tilePlateBmps[i] = new TilePlateBitmap();
		
		soundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 0);		
		streamVolume = audioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		sfx = new HashMap<String, Integer>();
		music = new HashMap<String, Song>();
		loadSound("sound");
		
		menu = new MainMenu();
		createGameOverObject();
		
	}	
	
	public static void createAnimationBuilders()
	{
		animationBuilders = new HashMap<String, AnimationBuilder>();
		animationBuilders.put("icebarrage", getIceBarrage());
		animationBuilders.put("antidote", getAntidoteAnim());
		animationBuilders.put("potion", getPotionAnim());
		animationBuilders.put("drain", getDrainAnim());
		animationBuilders.put("heal", getHealAnim());
		animationBuilders.put("ignite", getIgniteAnim());
		animationBuilders.put("igniteSmoke", getIgniteSmokeAnim());
		animationBuilders.put("entomb", getEntombAnim());
		
	}
	
	
	public static int streamVolume;
	
	public static void playSound(String sound)
	{
		
		soundPool.play(sfx.get(sound), streamVolume, streamVolume, 1, 0, 0f);
	}
			
	public static boolean createWorld()
	{	
		if(sprites== null)sprites = new HashMap<String, Sprite>(); else sprites.clear();
		if(switches== null)switches = new HashMap<String, Boolean>(); else switches.clear();
		if(characters== null)characters = new HashMap<String, PlayerCharacter>(); else characters.clear();
		if(enemies== null)enemies = new HashMap<String, Enemy>(); else enemies.clear();
		if(battleSprites== null)battleSprites = new HashMap<String, BattleSprite>(); else battleSprites.clear();
		if(encounters== null)encounters = new HashMap<String, Encounter>(); else encounters.clear();
		if(items== null)items = new HashMap<String, Item>(); else items.clear(); 
		if(abilities== null)abilities = new HashMap<String, Ability>(); else abilities.clear(); 
		if(maps== null)maps = new HashMap<String, String>(); else maps.clear(); 
		if(merchants== null)merchants = new HashMap<String, Merchant>(); else merchants.clear(); 
		if(battleAnims== null)battleAnims = new HashMap<String, BattleAnim>(); else battleAnims.clear(); 
		//if(reactionBubbles== null)reactionBubbles = new HashMap<String, ReactionBubble>(); else reactionBubbles.clear(); 
		
		//reset pan
		setPanned(0, 0);	
		
		//create party
		party = new Party(0, 0);	
		
		//load game data
		GameDataLoader.load();			
		
		return true;		
	}
	
	//called when new game is selected from the title screen	
	public static void NewGame()
	{		
		playTimer = new PlayTimer(0);
		party = new Party(0, 0);
		
		loading = false;	
		
		GameDataLoader.loadNewGame(activity);
		
//		for(PlayerCharacter pc : party.getPartyList(false))
//		{
//			pc.modifyLevel(99, false);
//			pc.fullRestore();
//		}
		
	}
	
	private static MapLoadThread mapLoadThread;
	private static boolean fadeInAfterLoad;
	
	public static void LoadMap(String name, boolean fadein)
	{
		fadeInAfterLoad = fadein;
		
		if(map != null)
		{
			map.clearObjectAction();
			map.unloadTiles();
		}
		
		playingReactions.clear();
		
		playTimer.stop();
		
		loadingScreen = new LoadingScreen();		
		InputStream is = null;
		
		try {is = activity.getAssets().open(maps.get(name));} catch (Exception e) {
			Log.d(TAG, "Unable to open file " + maps.get(name));
			closeGame();}
		
		map = new BqMap(name, is);		
		mapLoadThread = new MapLoadThread(map);
		
		GameState = States.GS_LOADING;
		mapLoadThread.start();
		
		
	}
	
	public static ObjectState mapChangeCallingState;
	public static void LoadMap(String name, ObjectState callingState)
	{
		mapChangeCallingState = callingState;
		LoadMap(name, false);	
		
	}

	public static boolean noRunningAnims() 
	{		
		return playingAnims.isEmpty();
	}

	private static Map<String, ReactionBubble> playingReactions;
	
	public static void openReactionBubble(ReactionBubble bubble, String target, Point drawPos, float duration, boolean loop)
	{		
		bubble.open(drawPos, duration, loop);		
		playingReactions.put(target, bubble);
	}
	
	public static void closeReactionBubble(String target)
	{
			if(playingReactions.get(target) != null)
				playingReactions.remove(target);
	}
	
	private static void updateReactionBubbles()
	{

			List<String> playingBubbleNames = new ArrayList<String>();
			for(String name : playingReactions.keySet())
				if(playingReactions.get(name).isDone())
					playingBubbleNames.add(name);			
			
			for(String name : playingBubbleNames)
				playingReactions.remove(name);
	
	}

	public static void renderReactionBubbles() 
	{
		for(ReactionBubble bubble : playingReactions.values())
			bubble.render();
		
	}

	public static void executeGameOver()
	{
		//screenFader.setFadeColor(255, 0, 0, 0);
		
		Global.map.clearObjectAction();
		GameState = States.GS_WORLDMOVEMENT;
		screenFader.setFaded();
		gameOverObject.execute();
	}
	
	private static void createGameOverObject()
	{
		gameOverObject = new GameObject("gameover", 0, 0);
		gameOverObject.addState(new ObjectState(gameOverObject));
		gameOverObject.setStateOpts(0, true, false, false);
		gameOverObject.addAction(0, new actPlayMusic("annihilation", true, true,0.0f));
		gameOverObject.addAction(0, new actShowScene("gameover"));
		gameOverObject.addAction(0, new actFadeControl(3.0f, 255, 0, 0, 0, false, true));
		
		Action ei = new actExpectInput(5.0f);
		ei.addToBranch(0, new actWait(0.25f));
		ei.addToBranch(1, new actPauseMusic(3.0f));
		ei.addToBranch(1, new actFadeControl(3.0f, 255, 255, 255, 255, true, true));
		ei.addToBranch(1, new actWait(2.0f));
		ei.addToBranch(1, new actUnloadScene());
		ei.addToBranch(1, new actResetGame());
		ei.setBranchLoop(0, true);
		gameOverObject.addAction(0, ei);

	}
}
