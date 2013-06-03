package bladequest.world;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.graphics.Point;
import android.graphics.Rect;
import bladequest.graphics.ReactionBubble;
import bladequest.graphics.Shadow;
import bladequest.graphics.Sprite;
import bladequest.graphics.Tile;
import bladequest.pathfinding.AStarObstacle;
import bladequest.pathfinding.AStarPath;
import bladequest.statuseffects.StatusEffect;
import bladequest.world.Item.Type;
import bladequest.world.Item.UseTypes;

public class Party 
{
	private boolean allowMovement, allowSaving;
	private ObjectPath objPath;
	private long objPathWaitStart;
	private boolean objPathWaiting;

	private Point worldPos;
	private Point gridPos;

	private int imageIndex;
	
	private boolean gridaligned;
	
	private Point target;
	private Point originalTarget;
	private List<Point> movePath;
	private boolean objectAtDestination;
	private Point pathingStartPoint;
	private GameObject selectedObject;
	private List<AStarObstacle> obs;
	
	private int gold;
	
	public final static int partyCount = 10;
	
	public PlayerCharacter partyMembers[];
	private List<Item> inventory;
	
	private final float encounterGrowth = 0.9675f; 
	private List<EncounterZone> inZoneList;
	
	
	private boolean faceLocked = false;
	private boolean hide = false;
	
	private long stepcount;
	
	private Shadow shadow;
	private boolean moving, floating;
	private int startElevation, targetElevation, floatingElevation;
	private float elevationTime;
	private long elevationTimer;

	private int floatIndex, floatPeriod, floatIntensity;
	

	
	public Party(int x, int y) 
	{
		allowMovement = true;
		
		stepcount = 0;
		
		worldPos = new Point(x*32, y*32);
		gridPos = new Point(x, y);
		target = new Point(gridPos);
		
		//init party
		partyMembers = new PlayerCharacter[partyCount];
		inventory = new ArrayList<Item>();
		
		imageIndex = 0;

		gridaligned = true;
		obs = new ArrayList<AStarObstacle>();
		
		inZoneList = new ArrayList<EncounterZone>();
		
		shadow = new Shadow(20, 10, 16, 200, 2.0f);
	}	

	public int getX() {return worldPos.x;}
	public void setX(int x) {worldPos.x = x;}
	public int getY() {return worldPos.y;}
	public void setY(int y) {worldPos.y = y;}
	public boolean isGridAligned() { return gridaligned; }
	
	public boolean allowMovement(){return allowMovement;}
	public void setAllowMovement(boolean am)
	{
		if(am) 
		{
			if(!Global.menuButton.Opened())
				Global.menuButton.open();
		}
		else 
			Global.menuButton.close();
		
		allowMovement = am; 
	}
	
	public PlayerCharacter[] getPartyMembers(boolean includeAll)
	{
		if(!includeAll)
		{
			PlayerCharacter[] chars = new PlayerCharacter[4];
			for(int i = 0; i < 4; ++i)
				chars[i] = partyMembers[i];
			
			return chars;
		}
		else
			return partyMembers;
	}
	
	public List<PlayerCharacter> getPartyList(boolean includeAll)
	{
		List<PlayerCharacter> newList = new ArrayList<PlayerCharacter>();
		for(int i = 0; i < (includeAll?partyCount:4); ++i)
			if(partyMembers[i] != null)
				newList.add(partyMembers[i]);
		
		return newList;
	}
	public boolean hasItem(String str)
	{
		Item i = Global.items.get(str);
		if(i != null)
		{
			for(Item j: inventory)
			{
				if(j.getId() == i.getId())
					return true;

			}
		}
		return false;
	}
	public int getItemCount(String str)
	{
		Item i = Global.items.get(str);
		if(i != null)
		{
			for(Item j: inventory)
			{
				if(j.getId() == i.getId())
					return j.getCount();

			}
		}
		return 0;
	}
	public void addItem(String str, int count)
	{
		Item i = Global.items.get(str);
		if(i != null)
		{
			for(Item j: inventory)
			{
				if(j.getId() == i.getId())
				{
					j.modifyCount(count);
					return;
				}
			}
			Item item = new Item(i);
			item.modifyCount(count-1);
			inventory.add(item);
		}
	}
	public void addItem(Item i)
	{
		inventory.add(i);

	}
	public void addItem(int id)
	{		
		for(Item item : Global.items.values())
			if(item.getId() == id)				
			{
				addItem(item.idName, 1);
				return;
			}
	}
	public void removeItem(String str)	
	{
		Item i = Global.items.get(str);
		if(i != null)
		{
			for(int j = 0; j < inventory.size(); j++)
			{
				if (inventory.get(j).getId() == i.getId())
				{
					inventory.remove(j);
					break;
				}
			}

		}
		
	}
	public void removeItem(int id)	
	{
		Item i = null;
		
		for(Item item : Global.items.values())
			if(item.getId() == id)
				i = item;
		
		if(i != null)
		{
			for(int j = 0; j < inventory.size(); j++)
			{
				if (inventory.get(j).getId() == i.getId())
				{
					inventory.remove(j);
					break;
				}
			}

		}
		
	}
	public void removeItem(int id, int count)	
	{
		Item i = null;
		
		for(Item item : Global.items.values())
			if(item.getId() == id)
				i = item;
		
		if(i != null)
		{
			for(int j = 0; j < inventory.size(); j++)
			{
				if (inventory.get(j).getId() == i.getId())
				{
					inventory.get(j).modifyCount(-count);
					if(inventory.get(j).getCount() == 0)
						inventory.remove(j);					
					break;
				}
			}

		}
		
	}
	
	public List<Item> getInventory()
	{
		List<Item> usables = new ArrayList<Item>();
		for(Item i : inventory)
			if(!i.isUsed())
				usables.add(i);
		
		return usables;

	}
	public int getElevation(){return shadow.getElevation();}
	public List<Item> getUsableInventory(Item.UseTypes useType)
	{
		List<Item> usables = new ArrayList<Item>();
		for(Item i : inventory)
			if(i.isUsable(useType) && !i.isUsed())
				usables.add(i);
		
		return usables;

	}
	
	public void sortInventoryABC()
	{
		List<String> nameList = new ArrayList<String>();
		
		for(Item i : inventory)
			nameList.add(i.getDisplayName());
		
		Collections.sort(nameList);
		
		List<Item> newList = new ArrayList<Item>();
		for(String s : nameList)
			for(Item i : inventory)
				if(i.getDisplayName() == s)
				{
					newList.add(i);
					break;
				}
		
		inventory = newList;
					
	}
	
	public void sortInventoryType()
	{
		List<Item.Type> typeList = new ArrayList<Item.Type>();			
		
		typeList.add(Item.Type.Usable);
		typeList.add(Item.Type.UsableSavePointOnly);
		typeList.add(Item.Type.UsableWorldOnly);		
		typeList.add(Item.Type.UsableBattleOnly);
		typeList.add(Item.Type.Weapon);
		typeList.add(Item.Type.Shield);		
		typeList.add(Item.Type.Helmet);
		typeList.add(Item.Type.Torso);
		typeList.add(Item.Type.Accessory);
		typeList.add(Item.Type.Key);
		
		List<Item> newList = new ArrayList<Item>();
		
		for(Item.Type type : typeList)
			for(Item i : inventory)
				if(i.getType() == type)
					newList.add(i);
		
		inventory = newList;
		
	}
	
	public void sortInventoryUsable(Item.UseTypes useType)
	{
		List<Item> newList = new ArrayList<Item>();
		
		//currently usable items first
		for(Item i : inventory)
			if(i.isUsable(useType))
				newList.add(i);
		
		
		//usable items not currnetly usable next
		for(Item i : inventory)
			if(!newList.contains(i) && 
					i.getType() == Type.Usable ||
					i.getType() == Type.UsableBattleOnly ||
					i.getType() == Type.UsableWorldOnly ||
					i.getType() == Type.UsableSavePointOnly)
				newList.add(i);
		
		//then the rest
		for(Item i : inventory)
			if(!newList.contains(i))
				newList.add(i);
		
		inventory = newList;
	}
	
	public void sortInventoryKeys()
	{
		List<String> nameList = new ArrayList<String>();
		
		for(Item i : inventory)
			if(i.getType() == Item.Type.Key)				
				nameList.add(i.getDisplayName());
		
		Collections.sort(nameList);
		
		List<Item> newList = new ArrayList<Item>();
		for(String s : nameList)
			for(Item i : inventory)
				if(i.getDisplayName() == s)
				{
					newList.add(i);
					break;
				}
		
		for(Item i : inventory)
			if(!newList.contains(i))
				newList.add(i);
		
		inventory = newList;
	}
	
	public void addCharacter(String str)
	{
		for(int i = 0; i < partyCount; ++i)
			if(partyMembers[i] == null)
			{
				partyMembers[i] = new PlayerCharacter(Global.characters.get(str));
				return;
			}
				
	}	
	
	public void insertCharacter(String str, int index)
	{
		if(index < partyCount)
			partyMembers[index] = new PlayerCharacter(Global.characters.get(str));

				
	}
	public void insertCharacter(PlayerCharacter c, int index)
	{
		if(index < partyCount)
			partyMembers[index] = c;

				
	}
	public Point getGridPos(){return gridPos;}	
	public Point getTarget(){return target;}	
	public void addGold(int gold){this.gold =Math.min(999999, this.gold+gold);}	
	public int getGold(){return gold;}	
	public List<Point> getPath(){return movePath;}
	public boolean SavingAllowed(){return allowSaving;}
	public void allowSaving(){ allowSaving = true; }
	
	public void openReactionBubble(ReactionBubble bubble, float duration, boolean loop)
	{
		Global.openReactionBubble(bubble, "party", new Point(worldPos.x, worldPos.y - 32 - shadow.getElevation()), duration, loop);
	}
	public void closeReactionBubble()
	{
		Global.closeReactionBubble("party");
	}
	
	//gets character from party, null if character is not in party
	public PlayerCharacter getCharacter(String name)
	{
		for(PlayerCharacter c : partyMembers)
			if(c != null && c.getName().equals(name))
				return c;
		
		return null;
	}
	
	public void teleport(int x, int y)
	{
		Global.clearMessages();
		for(EncounterZone ez : inZoneList)
			ez.reset();		
		inZoneList.clear();
		
		if(Global.map != null && Global.map.isLoaded())
			Global.map.unloadTiles();
		Global.setPanned(0, 0);
		worldPos = new Point(x*32, y*32);
		gridPos = new Point(x, y);
		clearMovementPath();
	}
	
	public void clearMovementPath()
	{
		if(movePath != null)
			movePath.clear();
		
		target = gridPos;
		originalTarget = target;
		Global.newTarget = false;
	}
	
	private boolean initBattle()
	{
		boolean annihilated = true;
		for(PlayerCharacter c : partyMembers)
			if(c != null && !c.isDead())
				annihilated = false;
		
		//update enzonelist
		for(EncounterZone zone : Global.map.encounterZones)
		{	
			//add new encounters
			if(zone.getZone().contains(gridPos.x,  gridPos.y) && !inZoneList.contains(zone))
				inZoneList.add(zone);
			
			//remove exited
			Iterator<EncounterZone> iter = inZoneList.iterator();
			while (iter.hasNext()) 
			{
				EncounterZone ez = iter.next();

			    if(!ez.getZone().contains(gridPos.x,  gridPos.y))
			    {
			    	ez.reset();
			    	iter.remove();
			    }
			}
		}
		
		//check for encounters, start battle
		if(!annihilated)
			for(EncounterZone zone : inZoneList)
				if(zone.checkForEncounters(encounterGrowth))
				{
					String battle = zone.getEncounter();
					if(battle != null)
					{		
						Global.beginBattle(battle, true);
						return true;
					}					
				}	
		
		return false;
	}
	
	public void applyPath(ObjectPath path)
	{
		objPath = path;
		allowMovement = false;
		clearMovementPath();
		HandleObjectPath();
		
	}
	
	private void HandleObjectPath()
	{		
		clearMovementPath();
		if(objPath.isDone())
		{
			//allowMovement = true;
			if(objPath.loops())
				objPath.reset();
			else
				objPath = null;
		}
		else
		{
			switch(objPath.getCurrentAction())
			{
			case MoveLeft:
				originalTarget = new Point(gridPos.x - 1, gridPos.y);
				pathingStartPoint = new Point(gridPos);
				mapPath();
				break;
			case MoveUp:
				originalTarget = new Point(gridPos.x, gridPos.y-1);
				pathingStartPoint = new Point(gridPos);
				mapPath();
				break;
			case MoveRight:
				originalTarget = new Point(gridPos.x + 1, gridPos.y);
				pathingStartPoint = new Point(gridPos);
				mapPath();
				break;
			case MoveDown:
				originalTarget = new Point(gridPos.x, gridPos.y+1);
				pathingStartPoint = new Point(gridPos);
				mapPath();
				break;
			case FaceLeft:
				vectorFace(new Point(-1, 0));
				objPath.advanceActions();
				HandleObjectPath();
				break;
			case FaceUp:
				vectorFace(new Point(0, -1));
				objPath.advanceActions();
				HandleObjectPath();
				break;
			case FaceRight:
				vectorFace(new Point(1, 0));
				objPath.advanceActions();
				HandleObjectPath();
				break;
			case FaceDown:
				vectorFace(new Point(0, 1));
				objPath.advanceActions();
				HandleObjectPath();
				break;
			case IncreaseMoveSpeed:
				if(Global.moveSpeed < 6)
					Global.moveSpeed++;
				objPath.advanceActions();
				HandleObjectPath();
				break;
			case DecreaseMoveSpeed:
				if(Global.moveSpeed > 1)
					Global.moveSpeed--;
				objPath.advanceActions();
				HandleObjectPath();
				break;
			case Hide:
				hide = true;
				objPath.advanceActions();
				HandleObjectPath();
				break;
			case Show:
				hide = false;
				objPath.advanceActions();
				HandleObjectPath();
				break;
			case LockFacing:
				faceLocked = true;
				objPath.advanceActions();
				HandleObjectPath();
				break;
			case UnlockFacing:
				faceLocked = false;
				objPath.advanceActions();
				HandleObjectPath();
				break;	
			case Wait:
				objPathWaitStart = System.currentTimeMillis();
				objPathWaiting = true;
				break;
			}
		}
	}
	
	private boolean stepActivate()
	{
		boolean executes = false;
		for(GameObject b : Global.map.Objects())
		{
			if(gridPos.equals(b.getGridPos()))
			{				
				objectAtDestination = false;
				if(!b.AutoStarts())
					if(b.execute())
						executes = true;
			}
				
		}
		return executes;
	}
	
	public long getStepCount() { return stepcount; }
		
	private void step()
	{
		stepcount++;
		allowSaving = false;
		gridaligned = true;
		gridPos = movePath.get(0);
		
		List<String> SERan = new ArrayList<String>();;		
		
		for(PlayerCharacter c : partyMembers)
			if(c != null)
			for(StatusEffect se : c.getStatusEffects())
			{
				if(SERan.indexOf(se.Name()) == -1)
				{
					SERan.add(se.Name());
					se.worldEffect();
				}
				se.onStep(c);
			}

		if(objPath != null)
		{
			objPath.advanceActions();
			HandleObjectPath();
		}			
		else
		{
			if(stepActivate() || initBattle())
				clearMovementPath();
			else if(Global.openMenuFlag)		
				Global.openMainMenuSafe();
			else if(Global.openDebugFlag)		
				Global.openDebugMenuSafe();
			else
				mapPath();
		}
		
		
	
	}
	
	public void setElevation(int pixels)
	{
		shadow.setElevation(pixels);
	}
	public void moveElevation(int pixels, float time)
	{
		moving = true;
		elevationTime = time;
		elevationTimer = System.currentTimeMillis();
		startElevation = shadow.getElevation();
		targetElevation = pixels;
	}
	public void setFloating(boolean floating, int periodLength, int intensity)
	{
		this.floating = floating;
		this.floatPeriod = periodLength;
		this.floatIntensity = intensity;
		this.floatingElevation = shadow.getElevation();
		floatIndex = 0;

	}
	
	private void updateElevation()
	{		
		Point vpCoords = Global.worldToVP(worldPos);
		shadow.setPosition(vpCoords.x + 16, vpCoords.y + 16 - shadow.getElevation());
		
		if(moving)
		{
			float delta = (System.currentTimeMillis() - elevationTimer) / (elevationTime * 1000.0f);
			
			if(delta < 1.0f)
				shadow.setElevation((int)(startElevation + (targetElevation - startElevation)*delta));
			else
			{
				shadow.setElevation(targetElevation);
				if(floating)
					floatingElevation = targetElevation;
				moving = false;				
			}				
		}	
		else if(floating)
		{			
			double offset = Math.sin(floatIndex++*(2*Math.PI)/floatPeriod);
			shadow.setElevation(floatingElevation + (int)(offset*floatIntensity));			
			
		}
			
	}
	
 	public void update()
	{
 		updateElevation();
 		
		//handle path waiting
		if(objPathWaiting)
		{
			if(System.currentTimeMillis() - objPathWaitStart >= 1000)
			{
				objPathWaiting = false;
				objPath.advanceActions();
				HandleObjectPath();
			}
			
		}
		
		//update path when target changes
		if(Global.newTarget && gridaligned && allowMovement)
		{
			Global.newTarget = false;			
			originalTarget = Global.mouseGridPos;
			pathingStartPoint = new Point(gridPos);
			mapPath();
		}	
		else if(!gridPos.equals(target) && (movePath != null && movePath.size() > 0))
		{
			Point p = movePath.get(0);
			Point vect = new Point(p.x - gridPos.x, p.y - gridPos.y);
			Point dest = new Point(p.x*32, p.y*32);
			
			//snap-in:  checks to see if you moved over your destination and snaps back
			if(vect.x > 0)worldPos.x = (worldPos.x > dest.x) ? dest.x : worldPos.x;
			if(vect.x < 0)worldPos.x = (worldPos.x < dest.x) ? dest.x : worldPos.x;
			if(vect.y > 0)worldPos.y = (worldPos.y > dest.y) ? dest.y : worldPos.y;
			if(vect.y < 0)worldPos.y = (worldPos.y < dest.y) ? dest.y : worldPos.y;
			
			if(!worldPos.equals(dest))
			{
				//move toward target
				gridaligned = false;
				worldPos.x += vect.x*Global.moveSpeed;
				worldPos.y += vect.y*Global.moveSpeed;
				
				if(!faceLocked)
					vectorFace(vect);
			}
			else
			{	
				//run next piece of the path
				step();
			}

		}
	}	
	
	private void buildObsMap()
	{
		obs = new ArrayList<AStarObstacle>();		
		objectAtDestination = false;
		
		//only add obs that can show up between gridpos and originaltarget		
		Rect pathArea = new Rect(
				Global.vpGridPos.x-5,
				Global.vpGridPos.y-5,
				Global.vpGridPos.x + Global.vpGridSize.x + 6,
				Global.vpGridPos.y + Global.vpGridSize.y + 6);
		
		for(GameObject b : Global.map.Objects())
		{
			if(b.hasCollision() && pathArea.contains(b.getGridPos().x, b.getGridPos().y))		
			{	
				boolean[] collSides = b.getCollision();
				obs.add(new AStarObstacle(b.getTarget(), collSides[0], collSides[1], collSides[2], collSides[3]));
				obs.add(new AStarObstacle(b.getGridPos(), collSides[0], collSides[1], collSides[2], collSides[3]));
				
				if(b.getGridPos().equals(originalTarget) && b.hasActions())
				{
					objectAtDestination = true;
					selectedObject = b;
				}
			}
		}
		for(Tile t : Global.map.LevelTiles())
		{
			if(pathArea.contains(t.WorldPos().x, t.WorldPos().y))
			{
				boolean[] collSides = t.getCollision();
				obs.add(new AStarObstacle(t.WorldPos(), collSides[0], collSides[1], collSides[2], collSides[3]));
			}			
		}
	}
	
	private void mapPath()
	{
		buildObsMap();		

		//create new path
		AStarPath path = new AStarPath(gridPos, originalTarget, obs);
		movePath = path.AStar();		
		

		//if path has steps, begin walking
		if(movePath.size() > 0)
		{
			target = movePath.get(movePath.size() - 1);
			if(path.destinationUnreachable())
				originalTarget = target;
			
			Global.genValidPathArea(movePath);
			Global.pathIsValid = true;
		}
		else 
		{				
			if(!gridPos.equals(originalTarget))
			{
				//if we stopped short because we were blocked
				if(!faceLocked)
					face(originalTarget);
				
				//if you clicked an object and were 2 or less blocks away
				if(objectAtDestination && path.calculateHeuristic(pathingStartPoint) <= 2 && !selectedObject.AutoStarts())
				{
					//execute the object's script
					if(selectedObject.execute())
					{
						//Global.saveLoader.saveGame(0);
						//Global.saveLoader.writeSaves(Global.activity);
						clearMovementPath();
					}
						

					objectAtDestination = false;	
					
					
				}				
			}
		}
				


	}
	
	private PlayerCharacter getFirstChar()
	{
		for(PlayerCharacter pc : getPartyMembers(false))
			if(pc != null && pc.isInBattle())
				return pc;
		
		return null;
	}
	
	//faces based on a vector	
	private void vectorFace(Point vect)
	{
		Sprite spr = firstCharSpr;
		if(vect.x > 0)
			spr.changeFace("right");
		else if(vect.x < 0)
			spr.changeFace("left");
		else if(vect.y > 0)
			spr.changeFace("down");
		else if(vect.y < 0)
			spr.changeFace("up");
	}
	
	//faces a point in the world	
	private void face(Point p)
	{
		Sprite spr = firstCharSpr;
		
		if(gridPos.x < p.x) spr.changeFace("right");
		else if(gridPos.y < p.y) spr.changeFace("down");
		else if(gridPos.x > p.x) spr.changeFace("left");
		else if(gridPos.y > p.y) spr.changeFace("up");	
		
	}
	
	public void render() {
		updateAnimation();
		if(!hide)
		{
			if(shadow.getElevation() > 0)
				shadow.render();
			Sprite spr = firstCharSpr;
			spr.render(worldPos.x, worldPos.y - shadow.getElevation(), imageIndex);

		}
		
	}
	
	private Sprite firstCharSpr;
	
	private void updateAnimation()
	{
		PlayerCharacter c = getFirstChar();
		Sprite spr;
		if(c == null)
			spr = firstCharSpr;
		else
		{
			spr= c.getWorldSprite();
			String face = firstCharSpr == null ? "down" : firstCharSpr.getFace();			
			firstCharSpr = c.getWorldSprite();
			
			spr.changeFace(face);
			firstCharSpr.changeFace(face);
		}
			
		
		
		if(spr != null)
		{
			if(Global.updateAnimation)
			{				
				imageIndex++;
				if(imageIndex >= spr.getNumFrames())
					imageIndex = 0;
			}
		}
	}

}