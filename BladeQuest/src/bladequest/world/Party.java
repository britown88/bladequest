package bladequest.world;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.Point;
import android.graphics.Rect;
import bladequest.graphics.Sprite;
import bladequest.graphics.Tile;
import bladequest.pathfinding.AStarObstacle;
import bladequest.pathfinding.AStarPath;
import bladequest.statuseffects.StatusEffect;
import bladequest.world.Item.Type;

public class Party 
{
	public boolean allowMovement;
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
	
	private static int noEncounterBuffer = 15;
	private int noEncounterTimer;
	
	private boolean faceLocked = false;
	private boolean hide = false;
	
	
	public Party(int x, int y) 
	{
		allowMovement = true;
		
		worldPos = new Point(x*32, y*32);
		gridPos = new Point(x, y);
		target = new Point(gridPos);
		
		//init party
		partyMembers = new PlayerCharacter[partyCount];
		inventory = new ArrayList<Item>();
		
		imageIndex = 0;

		gridaligned = true;
		obs = new ArrayList<AStarObstacle>();

		noEncounterTimer = 0;
	}	

	public int getX() {return worldPos.x;}
	public void setX(int x) {worldPos.x = x;}
	public int getY() {return worldPos.y;}
	public void setY(int y) {worldPos.y = y;}
	public boolean isGridAligned() { return gridaligned; }

	
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
	public void addItem(String str)
	{
		Item i = Global.items.get(str);
		if(i != null)
		{
			for(Item j: inventory)
			{
				if(j.getId() == i.getId())
				{
					j.modifyCount(1);
					return;
				}
			}
			inventory.add(new Item(i));
		}
	}
	public void addItem(Item i)
	{
		inventory.add(i);

	}
	public void addItem(int id)
	{
		Item i = null;
		
		for(Item item : Global.items.values())
			if(item.getId() == id)
				i = item;
		
		if(i != null)
		{
			for(Item j: inventory)
			{
				if(j.getId() == i.getId())
				{
					j.modifyCount(1);
					return;
				}
			}
			inventory.add(new Item(i));
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
	
	public List<Item> getInventory(boolean onlyUsable)
	{
		List<Item> usables = new ArrayList<Item>();
		for(Item i : inventory)
			if((onlyUsable ? (i.getType() == Item.Type.Usable) : true) && !i.isUsed())
				usables.add(i);
		
		return usables;

	}
	
	public void sortInventoryABC()
	{
		List<String> nameList = new ArrayList<String>();
		
		for(Item i : inventory)
			nameList.add(i.getName());
		
		Collections.sort(nameList);
		
		List<Item> newList = new ArrayList<Item>();
		for(String s : nameList)
			for(Item i : inventory)
				if(i.getName() == s)
				{
					newList.add(i);
					break;
				}
		
		inventory = newList;
					
	}
	
	public void sortInventoryType()
	{
		List<Item.Type> typeList = new ArrayList<Item.Type>();		
		typeList.add(Item.Type.Accessory);
		typeList.add(Item.Type.Helmet);
		typeList.add(Item.Type.Key);
		typeList.add(Item.Type.Shield);		
		typeList.add(Item.Type.Torso);
		typeList.add(Item.Type.Usable);
		typeList.add(Item.Type.Weapon);
		
		List<Item> newList = new ArrayList<Item>();
		
		for(Item.Type type : typeList)
			for(Item i : inventory)
				if(i.getType() == type)
					newList.add(i);
		
		inventory = newList;
		
	}
	
	public void sortInventoryUsable()
	{
		List<Item> newList = new ArrayList<Item>();
		
		for(Item i : inventory)
			if(i.getType() == Type.Usable 
					&& i.getTargetType() != TargetTypes.AllEnemies
					&& i.getTargetType() != TargetTypes.SingleEnemy)
				newList.add(i);
		
		for(Item i : inventory)
			if(!newList.contains(i) && i.getType() == Type.Usable)
				newList.add(i);
		
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
				nameList.add(i.getName());
		
		Collections.sort(nameList);
		
		List<Item> newList = new ArrayList<Item>();
		for(String s : nameList)
			for(Item i : inventory)
				if(i.getName() == s)
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
		
		if(Global.map != null)
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
		
		//check for encounters, start battle
		if(!annihilated && noEncounterTimer++ > noEncounterBuffer)
		{
			List<EncounterZone> zones = new ArrayList<EncounterZone>();
			
			for(EncounterZone zone : Global.map.encounterZones)
			{
				if(zone.getZone().contains(gridPos.x, gridPos.y))
					zones.add(zone);
			}
			
			if(zones.size() > 0)
			{
				String battle = zones.get(Global.rand.nextInt(zones.size())).getEncounter();
				if(battle != null)
				{
					noEncounterTimer = 0;			
					Global.beginBattle(null, battle);
					return true;
				}
				
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
			objPath = null;
		}
		else
		{
			switch(objPath.nextAction())
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
				HandleObjectPath();
				break;
			case FaceUp:
				vectorFace(new Point(0, -1));
				HandleObjectPath();
				break;
			case FaceRight:
				vectorFace(new Point(1, 0));
				HandleObjectPath();
				break;
			case FaceDown:
				vectorFace(new Point(0, 1));
				HandleObjectPath();
				break;
			case IncreaseMoveSpeed:
				if(Global.moveSpeed < 6)
					Global.moveSpeed++;
				HandleObjectPath();
				break;
			case DecreaseMoveSpeed:
				if(Global.moveSpeed > 1)
					Global.moveSpeed--;
				HandleObjectPath();
				break;
			case Hide:
				hide = true;
				HandleObjectPath();
				break;
			case Show:
				hide = false;
				HandleObjectPath();
				break;
			case LockFacing:
				faceLocked = true;
				HandleObjectPath();
				break;
			case UnlockFacing:
				faceLocked = false;
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
		for(GameObject b : Global.map.Objects())
		{
			if(gridPos.equals(b.getGridPos()))
			{				
				objectAtDestination = false;
				if(!b.AutoStarts())
					return b.execute();
			}
				
		}
		return false;
	}
	
	private void step()
	{
		gridaligned = true;
		gridPos = movePath.get(0);
		
		for(PlayerCharacter c : partyMembers)
			if(c != null)
			for(StatusEffect se : c.getStatusEffects())
				se.onStep(c);
		
		else//dont go into battle if menu was opened
		{
			//check for encounters, if not, continue movement
			if(objPath != null)
				HandleObjectPath();
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
	}
	
	public void update()
	{
		//handle path waiting
		if(objPathWaiting)
		{
			if(System.currentTimeMillis() - objPathWaitStart >= 1000)
			{
				objPathWaiting = false;
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
		for(int i = 0; i < 4; ++i)
			if( partyMembers[i] != null  && partyMembers[i].isInBattle())
				return partyMembers[i];
		
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
			Sprite spr = firstCharSpr;
			spr.render(worldPos.x, worldPos.y, imageIndex);
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