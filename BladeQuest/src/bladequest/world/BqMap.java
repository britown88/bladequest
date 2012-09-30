package bladequest.world;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import bladequest.actions.*;
import bladequest.graphics.*;
import bladequest.system.*;


import android.graphics.*;
import android.graphics.Paint.Align;


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
	
	
	public BqMap(String mapname, InputStream file)
	{	
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
	
	public GameObject gameOverObject;
	
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
	
	private FileSections section = FileSections.Header;
	private ObjectPath loadedPath;
	private boolean loadedPathWait;
	
	private void LoadDataLine(DataLine dl)
	{
		if(dl.item.charAt(0) == '#')
			return;
		else
		if(dl.item.charAt(0) == '[')
		{
			if(dl.item.equals("[header]"))
				section = FileSections.Header;
			else if(dl.item.equals("[tiles]"))
				section = FileSections.Tiles;
			else if(dl.item.equals("[objects]"))
				section = FileSections.Objects;
			else if(dl.item.equals("[encounters]"))
				section = FileSections.Encounters;
			
				
		}
		else
		{
			switch(section)
			{
			case Header:
				loadHeaderLine(dl);
				break;
			case Tiles:
				loadTileLine(dl);
				break;
			case Objects:
				loadObjectLine(dl);
				break;
			case Encounters:
				loadEncounterLine(dl);
				break;
			case Path:
				loadPathSection(dl.item);
				break;
			}
		}
	}
	
	private void loadHeaderLine(DataLine dl)
	{
		if(dl.item.equals("size"))
		{
			mapSize = new Point(
					Integer.parseInt(dl.values.get(0)), 
					Integer.parseInt(dl.values.get(1)));			
			
		}
		else if(dl.item.equals("tileset"))
		{
			tilesetName = dl.values.get(0).toLowerCase();
			backdrop = Global.scenes.get(tilesetName + "backdrop");
			initTilePlates();
		}
		else if(dl.item.equals("displayname"))
		{
			displayName = dl.values.get(0);
		}
		else if(dl.item.equals("BGM"))
		{
			defaultBGM = dl.values.get(0);
		}
	}
	
	private EncounterZone loadedEncounterZone;
	private void loadEncounterLine(DataLine dl)
	{
		if(dl.item.equals("zone"))
		{
			loadedEncounterZone = new EncounterZone(
				Integer.parseInt(dl.values.get(0)),
				Integer.parseInt(dl.values.get(1)),
				Integer.parseInt(dl.values.get(2)),
				Integer.parseInt(dl.values.get(3)),
				Integer.parseInt(dl.values.get(4)));
			
		}
		else if(dl.item.equals("encounter"))
		{
			loadedEncounterZone.addEncounter(dl.values.get(0));
		}
		else if(dl.item.equals("endzone"))
		{
			encounterZones.add(loadedEncounterZone);
		}
	}
	private void loadTileLine(DataLine dl)
	{
		if(dl.item.equals("t"))
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
	
	private GameObject loadedObject;
	private int loadedStateIndex;
	
	private void loadObjectLine(DataLine dl)
	{
		if(dl.item.equals("object"))
		{
			loadedObject = new GameObject(
					dl.values.get(0), 
					Integer.parseInt(dl.values.get(1)),
					Integer.parseInt(dl.values.get(2)));
			loadedStateIndex = -1;
		}
		else if(dl.item.equals("endobject"))
		{
			addObject(loadedObject);
		}
		else if(dl.item.equals("collision"))
		{
			loadedObject.setStateCollision(loadedStateIndex, 
					Boolean.parseBoolean(dl.values.get(0)), 
					Boolean.parseBoolean(dl.values.get(1)), 
					Boolean.parseBoolean(dl.values.get(2)), 
					Boolean.parseBoolean(dl.values.get(3)));
			
		}
		else if(dl.item.equals("movement"))
		{
			loadedObject.setStateMovement(loadedStateIndex, 
					Integer.parseInt(dl.values.get(0)), 
					Integer.parseInt(dl.values.get(1)));
			
		}
		else if(dl.item.equals("opts"))
		{
			loadedObject.setStateOpts(loadedStateIndex, 
					Boolean.parseBoolean(dl.values.get(0)), 
					Boolean.parseBoolean(dl.values.get(1)), 
					Boolean.parseBoolean(dl.values.get(2)));
		}
		
		
		else if(dl.item.equals("imageindex"))
		{
			loadedObject.setStateImageIndex(loadedStateIndex, 
					Integer.parseInt(dl.values.get(0)));
		}
		else if(dl.item.equals("animated"))
		{
			loadedObject.setStateAnimated(loadedStateIndex, 
					Boolean.parseBoolean(dl.values.get(0)));
			
		}
		else if(dl.item.equals("face"))
		{
			int face = Integer.parseInt(dl.values.get(0));
			switch(face)
			{
			case 0:
				loadedObject.setStateFace(loadedStateIndex, "left");
				break;
			case 1:
				loadedObject.setStateFace(loadedStateIndex, "up");
				break;
			case 2:
				loadedObject.setStateFace(loadedStateIndex, "right");
				break;
			case 3:
				loadedObject.setStateFace(loadedStateIndex, "down");
				break;
			}
			
		}
		else if(dl.item.equals("layer"))
		{
			String layer = dl.values.get(0);
			if(layer.equals("above"))
				loadedObject.setStateLayer(loadedStateIndex, Layer.Above);
			else
				loadedObject.setStateLayer(loadedStateIndex, Layer.Under);
			
		}
		else if(dl.item.equals("autostart"))
		{
			loadedObject.setStateAutoStart(loadedStateIndex, 
					Boolean.parseBoolean(dl.values.get(0)));			
		}
		else if(dl.item.equals("sprite"))
		{
			if(dl.values.get(0).length() > 0)
				loadedObject.setStateSprite(loadedStateIndex, 
					dl.values.get(0));			
			
		}
		else if(dl.item.equals("action"))
		{
			loadActionLine(dl);			
		}
		else if(dl.item.equals("switchcondition"))
		{
			loadedObject.addSwitchCondition(loadedStateIndex, dl.values.get(0));
			
		}
		else if(dl.item.equals("itemcondition"))
		{
			loadedObject.addItemCondition(loadedStateIndex, dl.values.get(0));
		}
		else if(dl.item.equals("addstate"))
		{
			loadedObject.addState();
			loadedStateIndex++;			
		}
			
	}
	
	private void loadPathSection(String pathSection)
	{
		if(pathSection.equals("moveleft"))
			loadedPath.addAction(ObjectPath.Actions.MoveLeft);
		else if(pathSection.equals("moveup"))
			loadedPath.addAction(ObjectPath.Actions.MoveUp);
		else if(pathSection.equals("moveright"))
			loadedPath.addAction(ObjectPath.Actions.MoveRight);
		else if(pathSection.equals("movedown"))
			loadedPath.addAction(ObjectPath.Actions.MoveDown);
		else if(pathSection.equals("faceleft"))
			loadedPath.addAction(ObjectPath.Actions.FaceLeft);
		else if(pathSection.equals("faceup"))
			loadedPath.addAction(ObjectPath.Actions.FaceUp);
		else if(pathSection.equals("faceright"))
			loadedPath.addAction(ObjectPath.Actions.FaceRight);
		else if(pathSection.equals("facedown"))
			loadedPath.addAction(ObjectPath.Actions.FaceDown);
		else if(pathSection.equals("lockfacing"))
			loadedPath.addAction(ObjectPath.Actions.LockFacing);
		else if(pathSection.equals("unlockfacing"))
			loadedPath.addAction(ObjectPath.Actions.UnlockFacing);
		else if(pathSection.equals("increasemovespeed"))
			loadedPath.addAction(ObjectPath.Actions.IncreaseMoveSpeed);
		else if(pathSection.equals("decreasemovespeed"))
			loadedPath.addAction(ObjectPath.Actions.DecreaseMoveSpeed);
		else if(pathSection.equals("hide"))
			loadedPath.addAction(ObjectPath.Actions.Hide);
		else if(pathSection.equals("show"))
			loadedPath.addAction(ObjectPath.Actions.Show);
		else if(pathSection.equals("wait"))
			loadedPath.addAction(ObjectPath.Actions.Wait);
		else if(pathSection.equals("endpath"))
		{
			loadedObject.addAction(loadedStateIndex, new actPath(loadedPath, loadedPathWait));
			section = FileSections.Objects;			
		}
	}
	
	private actMessage forkMessage;
	private boolean yesFork, noFork;
	
	private void loadActionLine(DataLine dl)
	{
		String action = dl.values.get(0);
		
		if(yesFork)
			forkMessage.yesDelta++;
		else if(noFork)
			forkMessage.noDelta++;
		
		if(action.equals("fade"))
		{
			loadedObject.addAction(loadedStateIndex, 
					new actFadeControl(
							Integer.parseInt(dl.values.get(1)),
							Integer.parseInt(dl.values.get(2)),
							Integer.parseInt(dl.values.get(3)),
							Integer.parseInt(dl.values.get(4)),
							Integer.parseInt(dl.values.get(5)),
							Boolean.parseBoolean(dl.values.get(6)),
							Boolean.parseBoolean(dl.values.get(7))));
			
		}
		else if(action.equals("message"))
		{
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
						new actMessage(text));
		}
		else if(action.equals("yesfork"))
		{
			yesFork = true;
		}
		else if(action.equals("nofork"))
		{
			forkMessage.yesDelta--;
			
			forkMessage.skipTo = loadedObject.getAction(loadedStateIndex, loadedObject.numActions(loadedStateIndex)-1);
			yesFork = false;
			noFork = true;
		}
		else if(action.equals("endfork"))
		{
			forkMessage.noDelta--;
			noFork = false;
			
			
		}
		else if(action.equals("gold"))
		{
			loadedObject.addAction(loadedStateIndex, 
					new actModifyGold(Integer.parseInt(dl.values.get(1))));
		}
		else if(action.equals("inventory"))
		{
			loadedObject.addAction(loadedStateIndex, 
					new actModifyInventory(
							dl.values.get(1), 
							Integer.parseInt(dl.values.get(2)), 
							Boolean.parseBoolean(dl.values.get(3))));
		}
		else if(action.equals("party"))
		{
			loadedObject.addAction(loadedStateIndex, 
					new actModifyParty(
							dl.values.get(1), 
							Boolean.parseBoolean(dl.values.get(2))));
		}
		else if(action.equals("showscene"))
		{
			loadedObject.addAction(loadedStateIndex, 
					new actShowScene(dl.values.get(1)));
		}
		else if(action.equals("merchant"))
		{
			loadedObject.addAction(loadedStateIndex, 
					new actMerchant(dl.values.get(1), Float.parseFloat(dl.values.get(2))));
		}
		else if(action.equals("nameselect"))
		{
			loadedObject.addAction(loadedStateIndex, 
					new actNameSelect(dl.values.get(1)));
		}
		else if(action.equals("resetgame"))
		{
			loadedObject.addAction(loadedStateIndex, 
					new actResetGame());
		}
		else if(action.equals("playmusic"))
		{
			loadedObject.addAction(loadedStateIndex, 
					new actPlayMusic(
							dl.values.get(1), 
							Boolean.parseBoolean(dl.values.get(2)),
							Integer.parseInt(dl.values.get(3))));
		}
		else if(action.equals("pausemusic"))
		{
			loadedObject.addAction(loadedStateIndex, 
					new actPauseMusic());
		}
		else if(action.equals("pan"))
		{
			loadedObject.addAction(loadedStateIndex, 
					new actPanControl(
							Integer.parseInt(dl.values.get(1)), 
							Integer.parseInt(dl.values.get(2)), 
							Integer.parseInt(dl.values.get(3)), 
							Boolean.parseBoolean(dl.values.get(4))));
		}
		else if(action.equals("path"))
		{
			section = FileSections.Path;
			loadedPath = new ObjectPath(dl.values.get(1));
			loadedPathWait = Boolean.parseBoolean(dl.values.get(2));
		}
		else if(action.equals("restore"))
		{
			loadedObject.addAction(loadedStateIndex, 
					new actRestoreParty());
		}
		else if(action.equals("shake"))
		{
			loadedObject.addAction(loadedStateIndex, 
					new actShake(
							Float.parseFloat(dl.values.get(1)), 
							Integer.parseInt(dl.values.get(2)), 
							Boolean.parseBoolean(dl.values.get(3))));
		}
		else if(action.equals("battle"))
		{
			loadedObject.addAction(loadedStateIndex, 
					new actStartBattle(loadedObject, dl.values.get(1)));
		}
		else if(action.equals("switch"))
		{
			loadedObject.addAction(loadedStateIndex, 
					new actSwitch(
							dl.values.get(1), 
							Boolean.parseBoolean(dl.values.get(2))));
		}
		else if(action.equals("savemenu"))
		{
			loadedObject.addAction(loadedStateIndex, new actSaveMenu());
		}
		else if(action.equals("teleport"))
		{
			loadedObject.addAction(loadedStateIndex, 
					new actTeleportParty(
							loadedObject, 
							Integer.parseInt(dl.values.get(1)), 
							Integer.parseInt(dl.values.get(2)), 
							dl.values.get(3)));
		}
		else if(action.equals("wait"))
		{
			loadedObject.addAction(loadedStateIndex, 
					new actWait(Float.parseFloat(dl.values.get(1))));
		}
		
	}
	
	private enum FileSections
	{
		Header,
		Tiles,
		Objects,
		Encounters,
		Path
	}
	
	

}
