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
import android.os.Debug;
import bladequest.UI.MenuPanel;
import bladequest.bladescript.LibraryWriter;
import bladequest.bladescript.Script.BadSpecialization;
import bladequest.bladescript.ScriptVar;
import bladequest.bladescript.ScriptVar.BadTypeException;
import bladequest.bladescript.libraries.FilterLibrary;
import bladequest.bladescript.libraries.GameObjectLibrary;
import bladequest.bladescript.libraries.HigherOrderLibrary;
import bladequest.bladescript.libraries.MathLibrary;
import bladequest.bladescript.libraries.SoundLibrary;
import bladequest.bladescript.libraries.WorldAnimLibrary;
import bladequest.graphics.Scene;
import bladequest.graphics.ScreenFilter;
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
	public Tile[] levelTiles;
	public GameObject[] levelObjects;  //list of collidable objects by tile.
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
		
		buildDisplayName();
		nameDisplayCounter = 0;
		
		loadGameObjects();

		for(GameObject go : objects)
			if(go.AutoStarts())
				go.execute();
		
		loaded = true;
		
	}
	
	public void clearAutoStarters()
	{
		for(GameObject go : objects)
			go.setStateAutoStart(0, false);
	}
	
	
	private void buildDisplayName()
	{
		nameDisplayPaint = Global.textFactory.getTextPaint(13, Color.WHITE, Align.CENTER);
				
		if(displayName.length() > 0)
		{
			Rect displayNameRect = new Rect();
			nameDisplayPaint.getTextBounds(displayName, 0, displayName.length()-1, displayNameRect);		
			displayNameRect = Global.screenToVP(displayNameRect);
			displayNameRect.inset(-20, -10);	
			
			displayNamePanel = new MenuPanel((Global.vpWidth - displayNameRect.width())/2, 0, displayNameRect.width(), displayNameRect.height());
			displayNamePanel.thickFrame = false;
			displayNamePanel.update();		

			displayNamePanel.addTextBox(displayName, displayNamePanel.width / 2, displayNamePanel.height / 2, nameDisplayPaint);
		
		}
		

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
		FilterLibrary.publishLibrary(library);
		SoundLibrary.publishLibrary(library);
		
		return library.getLibrary();
	}
	
	public String Name() { return name;}	
	public String BGM() { return defaultBGM; }
	public String displayName() { return displayName;}
	public Point Size() { return mapSize; }	
	public List<GameObject> Objects() { return objects; }
	public List<GameObject> Objects(int l, int t, int r, int b) 
	{
		List<GameObject> out = new ArrayList<GameObject>();
		for (int j = t; j != b; ++j)
		{
			if (j >= mapSize.y || j < 0) continue;
			int idx= l + j * mapSize.x;
			for (int i = l; i != r; ++i)
			{
				
				if (i >= mapSize.x || i < 0)
				{
					++idx;
					continue;
				}

					
				GameObject iter = levelObjects[idx] ; 
				while (iter != null)
				{
					out.add(iter);
					iter = iter.nextCollidable;
				}
				++idx;				
			}
		}
		return out; 
	}
	//not inclusive.
	//left, right, top, bottom
	public List<Tile> LevelTiles(int l, int t, int r, int b) 
	{
		List<Tile> out = new ArrayList<Tile>();
		for (int j = t; j != b; ++j)
		{
			if (j >= mapSize.y || j < 0) continue;
			int idx= l + j * mapSize.x;
			for (int i = l; i != r; ++i)
			{

				if (i >= mapSize.x || i < 0)
				{
					++idx;
					continue;
				}
				
				if (levelTiles[idx] != null)
				{
					out.add(levelTiles[idx]);
				}
				++idx;
			}
		}
		return out; 
	}
	public Tile levelTile(int x, int y)
	{
		return levelTiles[x + y * mapSize.x];
	}
	public void renderBackground(List<TilePlate> loadList){renderTiles(background, loadList);}	
	public void renderForeground(List<TilePlate> loadList){renderTiles(foreground, loadList);}
	
	public void renderBackgroundObjs(){renderObjects(Layer.Under); renderObjects(Layer.Level);}	
	public void renderForegroundObjs(){renderObjects(Layer.Above);}
	
	public Scene getBackdrop(){return backdrop;}	
	public void setBGM(String bgm){defaultBGM = bgm;}	
	public void playBGM(boolean playIntro)
	{
		BladeSong.instance().play(defaultBGM, playIntro, true, 0);
	}	
	
	private void initTilePlates()
	{
		plateCount = new Point((mapSize.x/Global.tilePlateSize.x)+(mapSize.x%Global.tilePlateSize.x>0?1:0), 
							   (mapSize.y/Global.tilePlateSize.y)+(mapSize.y%Global.tilePlateSize.y>0?1:0));
		
		tileset = Global.bitmaps.get(tilesetName);
		
		background = new TilePlate[plateCount.x*plateCount.y];
		for(int x = 0; x < plateCount.x; ++x)
			for(int y = 0; y < plateCount.y; ++y)
				background[(y*plateCount.x)+x] = new TilePlate(tileset, x, y, false);
		
		foreground = new TilePlate[plateCount.x*plateCount.y];
		for(int x = 0; x < plateCount.x; ++x)
			for(int y = 0; y < plateCount.y; ++y)
				foreground[(y*plateCount.x)+x] = new TilePlate(tileset, x, y, true);
	
		levelTiles = new Tile[mapSize.x * mapSize.y];
		levelObjects = new GameObject[mapSize.x * mapSize.y];
	}
	
	public void renderObjects(Layer layer)
	{		
		
		for (GameObject obj : Objects(Global.vpGridPos.x-1, Global.vpGridPos.y-1, Global.vpGridPos.x+2+Global.vpGridSize.x, Global.vpGridPos.y+2+Global.vpGridSize.y))
		{
			if (obj.getLayer().equals(layer))
			{
				obj.render();	
			}
		}
//		for(TilePlate plate : plates)
//		{
//			plate.renderObjects();
//		}
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
		if(displayNamePanel != null)
		{
			displayNamePanel.update();
			
			if(displayNamePanel.isShown())
				if(nameDisplayCounter++ >= nameDisplayLength)
					displayNamePanel.hide();	
		}
					
			
		for(GameObject b : objects) 
			b.update();   
	}
	
	public void renderDisplayName()
	{
		if(displayNamePanel != null)
			displayNamePanel.render();
	}
	
	private void renderTiles(TilePlate[] plates, List<TilePlate> loadList)
	{
		int plateX = Global.vpGridPos.x / Global.tilePlateSize.x;
		int plateY = Global.vpGridPos.y / Global.tilePlateSize.y;
		
		//position relative to the current tile plate
		int xInTile = Global.vpGridPos.x - (plateX*Global.tilePlateSize.x);
		int yInTile = Global.vpGridPos.y - (plateY*Global.tilePlateSize.y);
		
		int xCols =  1 + (Global.vpGridSize.x+xInTile)/Global.tilePlateSize.x;
		int yCols =  1 + (Global.vpGridSize.y+yInTile)/Global.tilePlateSize.y;
		
		int xDefaultCols = 1 + Global.vpGridSize.x/Global.tilePlateSize.x;
		int yDefaultCols = 1 + Global.vpGridSize.y/Global.tilePlateSize.y;
		
		int bufferTilesX = (Global.tilePlateSize.x*xDefaultCols)%Global.vpGridSize.x;
		int bufferTilesY = (Global.tilePlateSize.y*yDefaultCols)%Global.vpGridSize.y;
		
		boolean prevXLoaded = xInTile < (bufferTilesX/2);
		boolean nextXLoaded = !prevXLoaded;
		
		boolean prevYLoaded = yInTile < (bufferTilesY/2);
		boolean nextYLoaded = !prevYLoaded;
		

		//valid tile plates - 
		
		//   loaded at 0-3
		//   |  loaded at 4-9
		//   v  v
		//   xxxx <- loaded at 0>1
		//   x*xx
		//   xxxx <-loaded at 2-9
		
		for (int y = plateY-2; y < plateY+yDefaultCols+2; ++y)
		{
			if (y < 0 || y >= plateCount.y) continue;
			for (int x = plateX-2; x < plateX+xDefaultCols+2; ++x)
			{
				if (x < 0 || x >= plateCount.x) continue;
				if (y == plateY-2 || y == plateY+yDefaultCols+1 ||
					x == plateX-2 || x == plateX+xDefaultCols+1 ||
					(x == plateX-1 && !prevXLoaded) || 
					(y == plateY-1 && !prevYLoaded) || 
					(x == plateX+xDefaultCols && !nextXLoaded) || 
					(y == plateY+yDefaultCols && !nextYLoaded))
				{
					plates[x + y * plateCount.x].Unload();
				}
				else
				{
					//plate should be loadedededed.
					if (plates[x + y * plateCount.x].tryLoad(ScreenFilter.instance().defaultPaint()))
					{
						loadList.add(plates[x + y * plateCount.x]);
					}
				}
			}				
		}
	
		ScreenFilter.instance().save();
		ScreenFilter.instance().clear();
		
		for (int y = 0; y < yCols; y++)
		{
			if (y + plateY >= plateCount.y) break;
			for (int x = 0; x < xCols; x++)
			{
				if (x + plateX >= plateCount.x) break;
				plates[plateIndexFromVP(x,y)].render();				
			}
		}
		ScreenFilter.instance().restore();
	}
	
	private int plateIndexFromVP(int x, int y)
	{
		return (((Global.vpGridPos.y / Global.tilePlateSize.y) + y)*plateCount.x)+(Global.vpGridPos.x/Global.tilePlateSize.x) + x;
	}
	
	private int plateIndex(int x, int y)
	{
		return (y*plateCount.x)+x;
	}
	public void invalidateTiles()
	{
		int idx = 0;
		for (int j = 0; j < plateCount.y; ++j)
		{
			for (int i = 0; i < plateCount.x; ++i)
			{
				foreground[idx].Unload();
				background[idx].Unload();
				++idx;
			}			
		}
	}
	public void addTile(Tile t)
	{
		int index = ((t.WorldPos().y/Global.tilePlateSize.y)*plateCount.x)+(t.WorldPos().x/Global.tilePlateSize.x);
		
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
		{
			Point p = t.WorldPos();
			levelTiles[p.x + p.y*mapSize.x] = t;  //overwrites this tile.  if this is bad behavior, please contact the management.
		}
	}
	
	public void addObject(GameObject go)
	{
		objects.add(go);
		moveObject(go, null);
	}
	public void moveObject(GameObject go, Point prev)
	{
		//unlink
		if (go.nextCollidable != null) go.nextCollidable.prevCollidable = go.prevCollidable;
		if (go.prevCollidable != null) go.prevCollidable.nextCollidable = go.nextCollidable;
		
		if (prev != null && go.nextCollidable == null && go.prevCollidable == null)
		{
			//we're removing the last object from this tile, nuke the list.
			levelObjects[prev.x + prev.y * mapSize.x] = null;
		}
		else
		{
			if (go.prevCollidable == null && go.nextCollidable != null)
			{
				levelObjects[prev.x + prev.y * mapSize.x] = go.nextCollidable;	
			}
			go.nextCollidable = null;
			go.prevCollidable = null;	
		}
		
		
		
		//kk, relink.
		Point p = go.getGridPos();
		int idx = p.x + p.y * mapSize.x;
		GameObject first = levelObjects[idx];
		if (first != null)
		{
			first.prevCollidable = go;
			go.nextCollidable = first;
		}
		levelObjects[idx] = go;		
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
		int layer = Integer.parseInt(dl.values.get(4));
		boolean collLeft = dl.values.get(5).equals("1");
		boolean collTop = dl.values.get(6).equals("1");
		boolean collRight = dl.values.get(7).equals("1");
		boolean collBottom = dl.values.get(8).equals("1");
		
				
		
		Tile t = new Tile(x, y, bmpX, bmpY, layer > 3 ? Layer.Above : Layer.Under, layer%4);
		t.setCollision(collLeft, collTop, collRight, collBottom);
		
		if(dl.values.get(9) != null && dl.values.get(9).equals("t"))
			t.animate(Integer.parseInt(dl.values.get(10)), Integer.parseInt(dl.values.get(11)));

		addTile(t);	
	}


	

}
