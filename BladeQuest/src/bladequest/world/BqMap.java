package bladequest.world;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.Rect;
import bladequest.actions.actFadeControl;
import bladequest.actions.actMerchant;
import bladequest.actions.actMessage;
import bladequest.actions.actModifyGold;
import bladequest.actions.actModifyInventory;
import bladequest.actions.actModifyParty;
import bladequest.actions.actNameSelect;
import bladequest.actions.actPanControl;
import bladequest.actions.actPath;
import bladequest.actions.actPauseMusic;
import bladequest.actions.actPlayMusic;
import bladequest.actions.actReactionBubble;
import bladequest.actions.actResetGame;
import bladequest.actions.actRestoreParty;
import bladequest.actions.actSaveMenu;
import bladequest.actions.actShake;
import bladequest.actions.actShowScene;
import bladequest.actions.actStartBattle;
import bladequest.actions.actSwitch;
import bladequest.actions.actTeleportParty;
import bladequest.actions.actWait;
import bladequest.graphics.Scene;
import bladequest.graphics.Tile;
import bladequest.graphics.TilePlate;
import bladequest.system.CommandLine;
import bladequest.system.DataLine;
import bladequest.system.FileReader;


public class BqMap 
{
	private String name, tilesetName, displayName, defaultBGM;
	public Bitmap tileset;
	
	private Point mapSize, plateCount;
	private TilePlate[] background, foreground;
	public List<GameObject> objects;
	public List<Tile> levelTiles;
	public List<EncounterZone> encounterZones;
	
	private int nameDisplayLength = 150, nameDisplayCounter = 0;
	private boolean showDisplayName = false;
	private Paint nameDisplayPaint;
	private Rect displayNameRect;
	
	private Scene backdrop;
	
	private Map<String, CommandLine> commands;
	
	//file-loading temporaries
	public GameObject gameOverObject;
	private ObjectPath loadedPath;
	private boolean loadedPathWait;
	private EncounterZone loadedEncounterZone;
	private GameObject loadedObject;
	private int loadedStateIndex;
	private actMessage forkMessage;
	private boolean yesFork, noFork;
	
	
	public BqMap(String mapname, InputStream file)
	{	
		buildCommandList();
		
		name = mapname;
		objects = new ArrayList<GameObject>();	
		encounterZones = new ArrayList<EncounterZone>();
		
		FileReader fr = new FileReader(file);
		defaultBGM = "";
		
		String s = "";
		List<DataLine> lines = new ArrayList<DataLine>();
		
		do
		{ 
			s = fr.ReadLine();
			if(s.length() > 0)
				lines.add(new DataLine(s)); 
		} while(s.length() > 0);
		
		for(DataLine dl : lines)
			LoadDataLine(dl);				
		
		loadObjects();
		for(GameObject go : objects)
			if(go.AutoStarts())
				go.execute();

		
		if(displayName != null)
		{
			nameDisplayCounter = 0;
			showDisplayName = true;
			
			nameDisplayPaint = Global.textFactory.getTextPaint(13, Color.WHITE, Align.CENTER);
			
			displayNameRect = new Rect();
			nameDisplayPaint.getTextBounds(displayName, 0, displayName.length()-1, displayNameRect);
			displayNameRect.inset(-12, -9);
			displayNameRect = new Rect(
					Global.vpToScreenX((Global.vpWidth - displayNameRect.width())/2), 
					Global.vpToScreenY(0), 
					Global.vpToScreenX((Global.vpWidth - displayNameRect.width())/2 + displayNameRect.width()), 
					Global.vpToScreenY(displayNameRect.height()));
			displayNameRect.offset(0, 4);
		}
		
		
	}
	
	public String Name() { return name;}	
	public String BGM() { return defaultBGM; }
	public String displayName() { return displayName;}
	public Point Size() { return mapSize; }	
	public List<GameObject> Objects() { return objects; }	
	public List<Tile> LevelTiles() { return levelTiles; }
	public void renderBackground(List<TilePlate> loadList){renderTiles(background, loadList);}	
	public void renderForeground(List<TilePlate> loadList){renderTiles(foreground, loadList);}
	
	public void renderBackgroundObjs(){renderObjects(background);}	
	public void renderForegroundObjs(){renderObjects(foreground);}
	
	public Scene getBackdrop(){return backdrop;}	
	public void setBGM(String bgm){defaultBGM = bgm;}	
	public void playBGM(boolean playIntro)
	{
		Global.musicBox.play(defaultBGM, playIntro, -1);
	}	
	
	private void initTilePlates()
	{
		plateCount = new Point((mapSize.x/10)+(mapSize.x%10>0?1:0), (mapSize.y/10)+(mapSize.y%10>0?1:0));
		
		tileset = Global.bitmaps.get(tilesetName);
		
		background = new TilePlate[plateCount.x*plateCount.y];
		for(int x = 0; x < plateCount.x; ++x)
			for(int y = 0; y < plateCount.y; ++y)
				background[(y*plateCount.x)+x] = new TilePlate(tileset, x, y, false);
		
		foreground = new TilePlate[plateCount.x*plateCount.y];
		for(int x = 0; x < plateCount.x; ++x)
			for(int y = 0; y < plateCount.y; ++y)
				foreground[(y*plateCount.x)+x] = new TilePlate(tileset, x, y, true);
	
		levelTiles = new ArrayList<Tile>();
	}
	
	public void renderObjects(TilePlate[] plates)
	{		
		
		for(TilePlate plate : plates)
		{
			plate.renderObjects();
		}
		
		/*
		int plateX = Global.vpGridPos.x / 10;
		int plateY = Global.vpGridPos.y / 10;
		
		boolean drawR2 = Global.vpGridPos.y - ((int)(Global.vpGridPos.y/10)*10) > 0;
		boolean drawC3 = Global.vpGridPos.x - ((int)(Global.vpGridPos.x/10)*10) > 5;
		
		//always
		plates[plateIndexFromVP(0,0)].renderObjects();
		plates[plateIndexFromVP(1,0)].renderObjects();
		
		//row2
		if(drawR2 && plateY + 1 < plateCount.y)
		{
			plates[plateIndexFromVP(0,1)].renderObjects();
			plates[plateIndexFromVP(1,1)].renderObjects();
			
		}		
		//column3
		if(drawC3 && plateX + 2 < plateCount.x)
			plates[plateIndexFromVP(2,0)].renderObjects();
		
		//row2 and column3
		if(drawR2 && drawC3 && plateY + 1 < plateCount.y && plateX + 2 < plateCount.x)
			plates[plateIndexFromVP(2,1)].renderObjects();		

*/
	}
	
	public void clearObjectAction()
	{
		for(GameObject go : objects)
			go.clearActions();
	}
	
	public void unloadTiles()
	{
		for(int x = 0; x < plateCount.x; ++x)
			for(int y = 0; y < plateCount.y; ++y)
			{
				foreground[plateIndex(x, y)].Unload();
				background[plateIndex(x, y)].Unload();
			}		
	}
	
	public void update()
	{
		if(showDisplayName)
			if(nameDisplayCounter++ >= nameDisplayLength)
				showDisplayName = false;
			
		for(GameObject b : objects) 
			b.update();   
	}
	
	public void renderDisplayName()
	{
		if(showDisplayName)
		{
			Global.drawFrame(displayNameRect, true);
			Global.renderer.drawText(displayName, displayNameRect.centerX(), displayNameRect.centerY(), nameDisplayPaint);
		}
	}
	
	private void renderTiles(TilePlate[] plates, List<TilePlate> loadList)
	{
		int plateX = Global.vpGridPos.x / 10;
		int plateY = Global.vpGridPos.y / 10;
		
		boolean drawR2 = Global.vpGridPos.y - ((int)(Global.vpGridPos.y/10)*10) > 0;
		boolean drawC3 = Global.vpGridPos.x - ((int)(Global.vpGridPos.x/10)*10) > 5;
		
		//always
		plates[plateIndexFromVP(0,0)].render(loadList);
		plates[plateIndexFromVP(1,0)].render(loadList);
		
		if(plateX > 0) plates[plateIndexFromVP(-1,0)].Unload();
		if(plateY > 0)
		{
			plates[plateIndexFromVP(0,-1)].Unload(); 
			plates[plateIndexFromVP(1,-1)].Unload();
		}
		
		//row2
		if(drawR2)
		{
			if(plateY + 1 < plateCount.y)
			{
				plates[plateIndexFromVP(0,1)].render(loadList);
				plates[plateIndexFromVP(1,1)].render(loadList);
				
				if(plateX > 0) plates[plateIndexFromVP(-1,1)].Unload();
				if(plateY < plateCount.y - 2)
				{
					plates[plateIndexFromVP(0,2)].Unload(); 
					plates[plateIndexFromVP(1,2)].Unload();
				}
			}
			
		}
		
		//column3
		if(drawC3)
		{
			if(plateX + 2 < plateCount.x)
			{
				plates[plateIndexFromVP(2,0)].render(loadList);
				
				if(plateY > 0) plates[plateIndexFromVP(2,-1)].Unload();
				if(plateX < plateCount.x - 3) plates[plateIndexFromVP(3,0)].Unload();
			}
			
		}
		
		//row2 and column3
		if(drawR2 && drawC3)
		{
			
			if(plateY + 1 < plateCount.y && plateX + 2 < plateCount.x)
			{
				plates[plateIndexFromVP(2,1)].render(loadList);			
				
				if(plateX < plateCount.x - 3) plates[plateIndexFromVP(3,1)].Unload();
				if(plateY < plateCount.y - 2) plates[plateIndexFromVP(2,2)].Unload();
				
			}			

		}

	}
	
	private int plateIndexFromVP(int x, int y)
	{
		return (((Global.vpGridPos.y / 10) + y)*plateCount.x)+(Global.vpGridPos.x/10) + x;
	}
	
	private int plateIndex(int x, int y)
	{
		return (y*plateCount.x)+x;
	}
	
	public void addTile(Tile t)
	{
		int index = ((t.WorldPos().y/10)*plateCount.x)+(t.WorldPos().x/10);
		
		switch(t.Layer())
		{
		case Above:			
			foreground[index].addTile(t);
			break;
		default:			
			background[index].addTile(t);
			break;
		}
		
		if(t.hasCollision())
			LevelTiles().add(t);
	}
	
	public void addObject(GameObject go)
	{
		int index = ((go.getGridPos().y/10)*plateCount.x)+(go.getGridPos().x/10);
		
		switch(go.getLayer())
		{
		case Above:			
			foreground[index].addObject(go);
			break;
		default:			
			background[index].addObject(go);
			break;
		}
		
		objects.add(go);
		
	}
	
	private void loadObjects()
	{
		gameOverObject = new GameObject("gameover", 0, 0);
		gameOverObject.addState();
		gameOverObject.setStateCollision(0, false, false, false, false);
		gameOverObject.setStateMovement(0, 0, 0);
		gameOverObject.setStateOpts(0, true, false, false);
		gameOverObject.setStateFace(0, "down");
		gameOverObject.addAction(0, new actMessage("Guard: \nHe's over here!"));
		gameOverObject.addAction(0, new actMessage("Throw him back in his cell!"));
		gameOverObject.addAction(0, new actTeleportParty(gameOverObject, 15, 5, "prisonb2"));
		gameOverObject.addAction(0, new actRestoreParty());
		gameOverObject.addAction(0, new actSwitch("guardasleep", false));
		gameOverObject.addAction(0, new actSwitch("pdoor4", false));
		gameOverObject.addAction(0, new actSwitch("pdoor3", false));
		gameOverObject.addAction(0, new actModifyInventory("prisonkey", 1, true));
		gameOverObject.addAction(0, new actFadeControl(1, 255, 0, 0, 0, false, true));
		
		objects.add(gameOverObject);
		

	}
	
	private void LoadDataLine(DataLine dl)
	{
		if(dl.item.charAt(0) == '#')
			return;
		else
			if(commands.containsKey(dl.item))
				commands.get(dl.item).execute(dl);
	}
		
	private void buildCommandList()
	{
		commands = new HashMap<String, CommandLine>();
		
		commands.put("size", new CommandLine(){public void execute(DataLine dl) {size(dl);};});
		commands.put("tileset", new CommandLine(){public void execute(DataLine dl) {tileset(dl);};});
		commands.put("displayname", new CommandLine(){public void execute(DataLine dl) {displayname(dl);};});
		commands.put("BGM", new CommandLine(){public void execute(DataLine dl) {BGM(dl);};});
		
		commands.put("zone", new CommandLine(){public void execute(DataLine dl) {zone(dl);};});
		commands.put("encounter", new CommandLine(){public void execute(DataLine dl) {encounter(dl);};});
		commands.put("endzone", new CommandLine(){public void execute(DataLine dl) {endzone(dl);};});
		
		commands.put("t", new CommandLine(){public void execute(DataLine dl) {tile(dl);};});
		
		commands.put("object", new CommandLine(){public void execute(DataLine dl) {object(dl);};});
		commands.put("endobject", new CommandLine(){public void execute(DataLine dl) {endobject(dl);};});
		commands.put("collision", new CommandLine(){public void execute(DataLine dl) {collision(dl);};});
		commands.put("movement", new CommandLine(){public void execute(DataLine dl) {movement(dl);};});
		commands.put("opts", new CommandLine(){public void execute(DataLine dl) {opts(dl);};});
		commands.put("imageindex", new CommandLine(){public void execute(DataLine dl) {imageindex(dl);};});
		commands.put("animated", new CommandLine(){public void execute(DataLine dl) {animated(dl);};});
		commands.put("face", new CommandLine(){public void execute(DataLine dl) {face(dl);};});
		commands.put("layer", new CommandLine(){public void execute(DataLine dl) {layer(dl);};});
		commands.put("autostart", new CommandLine(){public void execute(DataLine dl) {autostart(dl);};});
		commands.put("sprite", new CommandLine(){public void execute(DataLine dl) {sprite(dl);};});
		commands.put("action", new CommandLine(){public void execute(DataLine dl) {action(dl);};});
		commands.put("switchcondition", new CommandLine(){public void execute(DataLine dl) {switchcondition(dl);};});
		commands.put("itemcondition", new CommandLine(){public void execute(DataLine dl) {itemcondition(dl);};});
		commands.put("addstate", new CommandLine(){public void execute(DataLine dl) {addstate(dl);};});
		
		commands.put("actfade", new CommandLine(){public void execute(DataLine dl) {actfade(dl);};});
		commands.put("actmessage", new CommandLine(){public void execute(DataLine dl) {actmessage(dl);};});
		commands.put("actyesfork", new CommandLine(){public void execute(DataLine dl) {actyesfork(dl);};});
		commands.put("actnofork", new CommandLine(){public void execute(DataLine dl) {actnofork(dl);};});
		commands.put("actendfork", new CommandLine(){public void execute(DataLine dl) {actendfork(dl);};});
		commands.put("actgold", new CommandLine(){public void execute(DataLine dl) {actgold(dl);};});
		commands.put("actinventory", new CommandLine(){public void execute(DataLine dl) {actinventory(dl);};});
		commands.put("actparty", new CommandLine(){public void execute(DataLine dl) {actparty(dl);};});
		commands.put("actshowscene", new CommandLine(){public void execute(DataLine dl) {actshowscene(dl);};});
		commands.put("actmerchant", new CommandLine(){public void execute(DataLine dl) {actmerchant(dl);};});
		commands.put("actnameselect", new CommandLine(){public void execute(DataLine dl) {actnameselect(dl);};});
		commands.put("actresetgame", new CommandLine(){public void execute(DataLine dl) {actresetgame(dl);};});
		commands.put("actplaymusic", new CommandLine(){public void execute(DataLine dl) {actplaymusic(dl);};});
		commands.put("actpausemusic", new CommandLine(){public void execute(DataLine dl) {actpausemusic(dl);};});
		commands.put("actpan", new CommandLine(){public void execute(DataLine dl) {actpan(dl);};});
		commands.put("actpath", new CommandLine(){public void execute(DataLine dl) {actpath(dl);};});
		commands.put("actrestore", new CommandLine(){public void execute(DataLine dl) {actrestore(dl);};});
		commands.put("actshake", new CommandLine(){public void execute(DataLine dl) {actshake(dl);};});
		commands.put("actbattle", new CommandLine(){public void execute(DataLine dl) {actbattle(dl);};});
		commands.put("actswitch", new CommandLine(){public void execute(DataLine dl) {actswitch(dl);};});
		commands.put("actsavemenu", new CommandLine(){public void execute(DataLine dl) {actsavemenu(dl);};});
		commands.put("actteleport", new CommandLine(){public void execute(DataLine dl) {actteleport(dl);};});
		commands.put("actwait", new CommandLine(){public void execute(DataLine dl) {actwait(dl);};});
		commands.put("actopenbubble", new CommandLine(){public void execute(DataLine dl) {actOpenBubble(dl);};});
		commands.put("actclosebubble", new CommandLine(){public void execute(DataLine dl) {actCloseBubble(dl);};});
		
		commands.put("moveleft", new CommandLine(){public void execute(DataLine dl) {patmoveleft(dl);};});
		commands.put("moveup", new CommandLine(){public void execute(DataLine dl) {patmoveup(dl);};});
		commands.put("moveright", new CommandLine(){public void execute(DataLine dl) {patmoveright(dl);};});
		commands.put("movedown", new CommandLine(){public void execute(DataLine dl) {patmovedown(dl);};});
		commands.put("faceleft", new CommandLine(){public void execute(DataLine dl) {patfaceleft(dl);};});
		commands.put("faceup", new CommandLine(){public void execute(DataLine dl) {patfaceup(dl);};});
		commands.put("faceright", new CommandLine(){public void execute(DataLine dl) {patfaceright(dl);};});
		commands.put("facedown", new CommandLine(){public void execute(DataLine dl) {patfacedown(dl);};});
		commands.put("lockfacing", new CommandLine(){public void execute(DataLine dl) {patlockfacing(dl);};});
		commands.put("unlockfacing", new CommandLine(){public void execute(DataLine dl) {patunlockfacing(dl);};});
		commands.put("increasemovespeed", new CommandLine(){public void execute(DataLine dl) {patincreasemovespeed(dl);};});
		commands.put("decreasemovespeed", new CommandLine(){public void execute(DataLine dl) {patdecreasemovespeed(dl);};});
		commands.put("hide", new CommandLine(){public void execute(DataLine dl) {pathide(dl);};});
		commands.put("show", new CommandLine(){public void execute(DataLine dl) {patshow(dl);};});
		commands.put("wait", new CommandLine(){public void execute(DataLine dl) {patwait(dl);};});
		commands.put("endpath", new CommandLine(){public void execute(DataLine dl) {endpath(dl);};});
		
	}	
	
	private void size(DataLine dl){mapSize = new Point(Integer.parseInt(dl.values.get(0)), Integer.parseInt(dl.values.get(1)));}
	private void tileset(DataLine dl){tilesetName = dl.values.get(0).toLowerCase(Locale.US);backdrop = Global.scenes.get(tilesetName + "backdrop");initTilePlates();}
	private void displayname(DataLine dl){displayName = dl.values.get(0);}
	private void BGM(DataLine dl){defaultBGM = dl.values.get(0);}
	
	private void zone(DataLine dl){loadedEncounterZone = new EncounterZone(Integer.parseInt(dl.values.get(0)),Integer.parseInt(dl.values.get(1)),Integer.parseInt(dl.values.get(2)),Integer.parseInt(dl.values.get(3)),Integer.parseInt(dl.values.get(4)));}
	private void encounter(DataLine dl){loadedEncounterZone.addEncounter(dl.values.get(0));}
	private void endzone(DataLine dl){encounterZones.add(loadedEncounterZone);}
	
	private void object(DataLine dl){
		loadedObject = new GameObject(
				dl.values.get(0),
				Integer.parseInt(dl.values.get(1)),
				Integer.parseInt(dl.values.get(2)));
		loadedStateIndex = -1;}
	private void endobject(DataLine dl){addObject(loadedObject);}
	private void collision(DataLine dl){
		loadedObject.setStateCollision(loadedStateIndex,
				Boolean.parseBoolean(dl.values.get(0)),
				Boolean.parseBoolean(dl.values.get(1)),
				Boolean.parseBoolean(dl.values.get(2)),
				Boolean.parseBoolean(dl.values.get(3)));}
	private void movement(DataLine dl){
		loadedObject.setStateMovement(loadedStateIndex,
				Integer.parseInt(dl.values.get(0)),
				Integer.parseInt(dl.values.get(1)));}
	private void opts(DataLine dl){
		loadedObject.setStateOpts(loadedStateIndex, 
			Boolean.parseBoolean(dl.values.get(0)), 
			Boolean.parseBoolean(dl.values.get(1)), 
			Boolean.parseBoolean(dl.values.get(2)));}
	private void imageindex(DataLine dl){
		loadedObject.setStateImageIndex(loadedStateIndex, 
				Integer.parseInt(dl.values.get(0)));}
	private void animated(DataLine dl){
		loadedObject.setStateAnimated(loadedStateIndex, 
				Boolean.parseBoolean(dl.values.get(0)));}
	private void face(DataLine dl){
		int face = Integer.parseInt(dl.values.get(0));
		switch(face)
		{case 0:loadedObject.setStateFace(loadedStateIndex, "left");break;
		case 1:loadedObject.setStateFace(loadedStateIndex, "up");break;
		case 2:loadedObject.setStateFace(loadedStateIndex, "right");break;
		case 3:loadedObject.setStateFace(loadedStateIndex, "down");	break;}}
	private void layer(DataLine dl){
		String layer = dl.values.get(0);
		if(layer.equals("above")) loadedObject.setStateLayer(loadedStateIndex, Layer.Above);
		else loadedObject.setStateLayer(loadedStateIndex, Layer.Under);	}
	private void autostart(DataLine dl){
		loadedObject.setStateAutoStart(loadedStateIndex, 
			Boolean.parseBoolean(dl.values.get(0)));}
	private void sprite(DataLine dl){
		if(dl.values.get(0).length() > 0)
			loadedObject.setStateSprite(loadedStateIndex, 
				dl.values.get(0));}
	private void action(DataLine dl){loadActionLine(dl);}
	private void switchcondition(DataLine dl){
		loadedObject.addSwitchCondition(
				loadedStateIndex, dl.values.get(0));}
	private void itemcondition(DataLine dl){
		loadedObject.addItemCondition(
				loadedStateIndex, dl.values.get(0));}
	private void addstate(DataLine dl){
		loadedObject.addState();
		loadedStateIndex++;	}
		
	private void tile(DataLine dl)
	{
		int x = Integer.parseInt(dl.values.get(0));
		int y = Integer.parseInt(dl.values.get(1));
		int bmpX = Integer.parseInt(dl.values.get(2));
		int bmpY = Integer.parseInt(dl.values.get(3));
		Layer layer = dl.values.get(4).equals("a") ? Layer.Above : Layer.Under;
		boolean collLeft = dl.values.get(5).equals("1");
		boolean collTop = dl.values.get(6).equals("1");
		boolean collRight = dl.values.get(7).equals("1");
		boolean collBottom = dl.values.get(8).equals("1");
		
				
		Tile t = new Tile(x, y, bmpX, bmpY, layer);
		t.setCollision(collLeft, collTop, collRight, collBottom);
		
		if(dl.values.get(9) != null && dl.values.get(9).equals("t"))
			t.animate(Integer.parseInt(dl.values.get(10)), Integer.parseInt(dl.values.get(11)));

		addTile(t);	
	}
		
	private void actfade(DataLine dl){
		loadedObject.addAction(loadedStateIndex, 
				new actFadeControl(
						Integer.parseInt(dl.values.get(1)),
						Integer.parseInt(dl.values.get(2)),
						Integer.parseInt(dl.values.get(3)),
						Integer.parseInt(dl.values.get(4)),
						Integer.parseInt(dl.values.get(5)),
						Boolean.parseBoolean(dl.values.get(6)),
						Boolean.parseBoolean(dl.values.get(7))));}
	private void actmessage(DataLine dl){
		String text = dl.values.get(1);
		text = text.replace("\\n", " \n");
		
		if(dl.values.size() > 2 && dl.values.get(2).length() > 0)
		{
			forkMessage = new actMessage(text, Boolean.parseBoolean(dl.values.get(2)));
			loadedObject.addAction(loadedStateIndex, forkMessage);	
			yesFork = noFork = false;
		}				
		else
			loadedObject.addAction(loadedStateIndex, 
					new actMessage(text));}
	private void actyesfork(DataLine dl){
		yesFork = true;}
	private void actnofork(DataLine dl){
		forkMessage.yesDelta--;		
		forkMessage.skipTo = loadedObject.getAction(loadedStateIndex, loadedObject.numActions(loadedStateIndex)-1);
		yesFork = false;
		noFork = true;}
	private void actendfork(DataLine dl){
		forkMessage.noDelta--;
		noFork = false;}
	private void actgold(DataLine dl){
		loadedObject.addAction(loadedStateIndex, 
				new actModifyGold(Integer.parseInt(dl.values.get(1))));}
	private void actinventory(DataLine dl){
		loadedObject.addAction(loadedStateIndex, 
			new actModifyInventory(
					dl.values.get(1), 
					Integer.parseInt(dl.values.get(2)), 
					Boolean.parseBoolean(dl.values.get(3))));}
	private void actparty(DataLine dl){
		loadedObject.addAction(loadedStateIndex, 
			new actModifyParty(
					dl.values.get(1), 
					Boolean.parseBoolean(dl.values.get(2))));}
	private void actshowscene(DataLine dl){
		loadedObject.addAction(loadedStateIndex, 
			new actShowScene(dl.values.get(1)));}
	private void actmerchant(DataLine dl){
		loadedObject.addAction(loadedStateIndex, 
			new actMerchant(dl.values.get(1), Float.parseFloat(dl.values.get(2))));}
	private void actnameselect(DataLine dl){
		loadedObject.addAction(loadedStateIndex, 
			new actNameSelect(dl.values.get(1)));}
	private void actresetgame(DataLine dl){
		loadedObject.addAction(loadedStateIndex, 
			new actResetGame());}
	private void actplaymusic(DataLine dl){
		loadedObject.addAction(loadedStateIndex, 
			new actPlayMusic(
					dl.values.get(1), 
					Boolean.parseBoolean(dl.values.get(2)),
					Integer.parseInt(dl.values.get(3))));}
	private void actpausemusic(DataLine dl){
		loadedObject.addAction(loadedStateIndex, 
			new actPauseMusic());}
	
	private void actOpenBubble(DataLine dl){
		loadedObject.addAction(loadedStateIndex, 
				new actReactionBubble(
						dl.values.get(1), 
						dl.values.get(2), 
						Float.parseFloat(dl.values.get(3)), 
						Boolean.parseBoolean(dl.values.get(4)),
						Boolean.parseBoolean(dl.values.get(5))));
	}
	private void actCloseBubble(DataLine dl){
		loadedObject.addAction(loadedStateIndex, 
				new actReactionBubble(dl.values.get(1)));
	}
	
	private void actpan(DataLine dl){
		loadedObject.addAction(loadedStateIndex, 
			new actPanControl(
					Integer.parseInt(dl.values.get(1)), 
					Integer.parseInt(dl.values.get(2)), 
					Integer.parseInt(dl.values.get(3)), 
					Boolean.parseBoolean(dl.values.get(4))));}
	private void actpath(DataLine dl){
		loadedPath = new ObjectPath(dl.values.get(1));
		loadedPathWait = Boolean.parseBoolean(dl.values.get(2));}
	private void actrestore(DataLine dl){
		loadedObject.addAction(loadedStateIndex, 
			new actRestoreParty());}
	private void actshake(DataLine dl){loadedObject.addAction(
			loadedStateIndex, 
			new actShake(
					Float.parseFloat(dl.values.get(1)), 
					Integer.parseInt(dl.values.get(2)), 
					Boolean.parseBoolean(dl.values.get(3))));}
	private void actbattle(DataLine dl){
		loadedObject.addAction(loadedStateIndex, 
			new actStartBattle(loadedObject, dl.values.get(1)));}
	private void actswitch(DataLine dl){loadedObject.addAction(
			loadedStateIndex, 
			new actSwitch(
					dl.values.get(1), 
					Boolean.parseBoolean(dl.values.get(2))));}
	private void actsavemenu(DataLine dl){
		loadedObject.addAction(loadedStateIndex, new actSaveMenu());}
	private void actteleport(DataLine dl){loadedObject.addAction(
			loadedStateIndex, 
			new actTeleportParty(
					loadedObject, 
					Integer.parseInt(dl.values.get(1)), 
					Integer.parseInt(dl.values.get(2)), 
					dl.values.get(3)));}
	private void actwait(DataLine dl){
		loadedObject.addAction(loadedStateIndex, 
			new actWait(Float.parseFloat(dl.values.get(1))));}
	
	private void patmoveleft(DataLine dl){loadedPath.addAction(ObjectPath.Actions.MoveLeft);}
	private void patmoveup(DataLine dl){loadedPath.addAction(ObjectPath.Actions.MoveUp);}
	private void patmoveright(DataLine dl){loadedPath.addAction(ObjectPath.Actions.MoveRight);}
	private void patmovedown(DataLine dl){loadedPath.addAction(ObjectPath.Actions.MoveDown);}
	private void patfaceleft(DataLine dl){loadedPath.addAction(ObjectPath.Actions.FaceLeft);}
	private void patfaceup(DataLine dl){loadedPath.addAction(ObjectPath.Actions.FaceUp);}
	private void patfaceright(DataLine dl){loadedPath.addAction(ObjectPath.Actions.FaceRight);}
	private void patfacedown(DataLine dl){loadedPath.addAction(ObjectPath.Actions.FaceDown);}
	private void patlockfacing(DataLine dl){loadedPath.addAction(ObjectPath.Actions.LockFacing);}
	private void patunlockfacing(DataLine dl){loadedPath.addAction(ObjectPath.Actions.UnlockFacing);}
	private void patincreasemovespeed(DataLine dl){loadedPath.addAction(ObjectPath.Actions.IncreaseMoveSpeed);}
	private void patdecreasemovespeed(DataLine dl){loadedPath.addAction(ObjectPath.Actions.DecreaseMoveSpeed);}
	private void pathide(DataLine dl){loadedPath.addAction(ObjectPath.Actions.Hide);}
	private void patshow(DataLine dl){loadedPath.addAction(ObjectPath.Actions.Show);}
	private void patwait(DataLine dl){loadedPath.addAction(ObjectPath.Actions.Wait);}
	private void endpath(DataLine dl){loadedObject.addAction(loadedStateIndex, new actPath(loadedPath, loadedPathWait));}
	
	private void loadActionLine(DataLine dl)
	{
		String action = "act" + dl.values.get(0);		
		if(yesFork)forkMessage.yesDelta++;else if(noFork)forkMessage.noDelta++;
		
		if(commands.containsKey(action))
			commands.get(action).execute(dl);		
	}

	

}
