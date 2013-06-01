package bladequest.world;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
import bladequest.UI.MenuPanel;
import bladequest.bladescript.LibraryWriter;
import bladequest.bladescript.Script.BadSpecialization;
import bladequest.bladescript.ScriptVar;
import bladequest.bladescript.ScriptVar.BadTypeException;
import bladequest.bladescript.libraries.GameObjectLibrary;
import bladequest.bladescript.libraries.HigherOrderLibrary;
import bladequest.bladescript.libraries.MathLibrary;
import bladequest.bladescript.libraries.WorldAnimLibrary;
import bladequest.graphics.Scene;
import bladequest.graphics.Tile;
import bladequest.graphics.TilePlate;
import bladequest.sound.BladeSong;
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
	private Paint nameDisplayPaint;
	
	private Scene backdrop;
	
	private Map<String, CommandLine> commands;	
	//file-loading temporaries
	private EncounterZone loadedEncounterZone;	
	private MenuPanel displayNamePanel;
	
	InputStream mapFile;
	
	private boolean loaded;
	public boolean isLoaded(){return loaded;}
	
	
	public BqMap(String mapname, InputStream file)
	{	
		buildCommandList();
		
		name = mapname;
		objects = new ArrayList<GameObject>();	
		encounterZones = new ArrayList<EncounterZone>();
		
		defaultBGM = "";
		
		mapFile = file;
		
		
	}
	
	public void  load()
	{
		loaded = false;
		FileReader fr = new FileReader(mapFile);		
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
		
		loadGameObjects();

		for(GameObject go : objects)
			if(go.AutoStarts())
				go.execute();
		
		loaded = true;
		buildDisplayName();
		nameDisplayCounter = 0;
	}
	
	public void clearAutoStarters()
	{
		for(GameObject go : objects)
			go.setStateAutoStart(0, false);
	}
	
	
	private void buildDisplayName()
	{
		nameDisplayPaint = Global.textFactory.getTextPaint(13, Color.WHITE, Align.CENTER);
				
		Rect displayNameRect = new Rect();
		nameDisplayPaint.getTextBounds(displayName, 0, displayName.length()-1, displayNameRect);		
		displayNameRect = Global.screenToVP(displayNameRect);
		displayNameRect.inset(-20, -10);	
		
		displayNamePanel = new MenuPanel((Global.vpWidth - displayNameRect.width())/2, 0, displayNameRect.width(), displayNameRect.height());
		displayNamePanel.thickFrame = false;
		displayNamePanel.update();		

		displayNamePanel.addTextBox(displayName, displayNamePanel.width / 2, displayNamePanel.height / 2, nameDisplayPaint);
	}
	
	
	private Map<String, ScriptVar> standardLibrary;
	private void loadGameObjects()
	{
		String objFilename = name+".omap";
		String objPath = "maps/omaps/"+objFilename;
		List<String> fileList = new ArrayList<String>();
		try {
			fileList = Arrays.asList(Global.activity.getAssets().list("maps/omaps"));
		} catch (IOException e) {
			Global.logMessage("LOADING FILES FAILED");
			e.printStackTrace();
		}
		
		if(fileList.contains(objFilename))
		{
			standardLibrary = getStandardLibrary();		
			Global.compileScript(objPath, standardLibrary);
			
			//add objs to necessary tileplates
			for(GameObject go : objects)
			{
				int index = ((go.getGridPos().y/10)*plateCount.x)+(go.getGridPos().x/10);			
				if(go.getLayer() == Layer.Above)
					foreground[index].addObject(go);
				else
					background[index].addObject(go);
				
				go.postCreate();

			}
			
			
		}
	}
	
	public static Map<String, ScriptVar> getStandardLibrary()
	{
		LibraryWriter library = new LibraryWriter();
	
		try {
			library.add("subtract", GameDataLoader.class.getMethod("subtractScriptFn", int.class, int.class));
			library.add("equals", GameDataLoader.class.getMethod("equalsScriptFn", int.class, int.class));
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (BadTypeException e) {
		} catch (BadSpecialization e) {
		}

		HigherOrderLibrary.publishLibrary(library);
		MathLibrary.publishLibrary(library);
		GameObjectLibrary.publishLibrary(library);
		WorldAnimLibrary.publishLibrary(library);
		
		return library.getLibrary();
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
		BladeSong.instance().play(defaultBGM, playIntro, true, 0);
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
		displayNamePanel.update();
		
		if(displayNamePanel.isShown())
			if(nameDisplayCounter++ >= nameDisplayLength)
				displayNamePanel.hide();				
			
		for(GameObject b : objects) 
			b.update();   
	}
	
	public void renderDisplayName()
	{
		displayNamePanel.render();
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
		
		
		objects.add(go);
		
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
		
		commands.put("t", new CommandLine(){public void execute(DataLine dl) {tile(dl);};});
				
		commands.put("size", new CommandLine(){public void execute(DataLine dl) {size(dl);};});
		commands.put("tileset", new CommandLine(){public void execute(DataLine dl) {tileset(dl);};});
		commands.put("displayname", new CommandLine(){public void execute(DataLine dl) {displayname(dl);};});
		commands.put("BGM", new CommandLine(){public void execute(DataLine dl) {BGM(dl);};});
		
		commands.put("zone", new CommandLine(){public void execute(DataLine dl) {zone(dl);};});
		commands.put("encounter", new CommandLine(){public void execute(DataLine dl) {encounter(dl);};});
		commands.put("endzone", new CommandLine(){public void execute(DataLine dl) {endzone(dl);};});
	}	
	
	private void size(DataLine dl){mapSize = new Point(Integer.parseInt(dl.values.get(0)), Integer.parseInt(dl.values.get(1)));}
	private void tileset(DataLine dl)
	{
		tilesetName = dl.values.get(0).toLowerCase(Locale.US);
		backdrop = Global.scenes.get(tilesetName + "backdrop");
		initTilePlates();
	}
	
	private void displayname(DataLine dl){displayName = dl.values.get(0);}
	private void BGM(DataLine dl){defaultBGM = dl.values.get(0);}
	
	private void zone(DataLine dl){loadedEncounterZone = new EncounterZone(Integer.parseInt(dl.values.get(0)),Integer.parseInt(dl.values.get(1)),Integer.parseInt(dl.values.get(2)),Integer.parseInt(dl.values.get(3)),Float.parseFloat(dl.values.get(4)));}
	private void encounter(DataLine dl){loadedEncounterZone.addEncounter(dl.values.get(0));}
	private void endzone(DataLine dl){encounterZones.add(loadedEncounterZone);}
	
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


	

}
