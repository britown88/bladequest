package bladequest.world;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import bladequest.actions.Action;
import bladequest.graphics.ReactionBubble;
import bladequest.graphics.Shadow;
import bladequest.graphics.Tile;

public class GameObject {
	private String name;
	private Point worldPos;
	private Point gridPos;
	private Point target, origin;
	private boolean gridAligned;	

	private int currentState;	
	private List<ObjectState> states;
	private ObjectState runningState;
	
	public boolean faceLocked = false;
	public boolean hide = false;
	
	private Shadow shadow;
	private boolean moving, floating;
	private int startElevation, targetElevation, floatingElevation;
	private float elevationTime;
	private long elevationTimer;

	private int floatIndex, floatPeriod, floatIntensity;
	
	private ObjectPath defaultPath;
	
	public GameObject(String name, int x, int y)
	{
		this.name = name;
		worldPos = new Point(x*32, y*32);
		gridPos = new Point(x, y);	
		origin = new Point(gridPos);

		currentState = 0;
		gridAligned = true;
		target = gridPos;
		
		states = new ArrayList<ObjectState>();
		shadow = new Shadow(20, 10, 16, 200, 2.0f);
		
	}
	
	public void setDefaultPath(ObjectPath path)
	{
		this.defaultPath = path;
	}
	
	public String Name() { return name; }
	public void makeStateSolid(int index) {states.get(index).makeSolid();}
	public void setStateCollision(int index, boolean left, boolean top, boolean right, boolean bottom){states.get(index).setCollision(left, top, right, bottom);}
	public boolean[] getCollision() { return states.get(currentState).getCollision(); }
	public boolean hasCollision() { return states.get(currentState).hasCollision(); }	
	
	public boolean isGridAligned(){return gridAligned;}
	public Point getTarget(){return target;}
	public void setStateMovement(int index, int moveRange, int moveDelay){states.get(index).setMovement(moveRange, moveDelay);}
	public void setStateImageIndex(int index, int imageIndex){states.get(index).setImageIndex(imageIndex);}
	public void setStateAnimated(int index, boolean animated){states.get(index).setAnimated(animated);}
	public void setStateFace(int index, String face){states.get(index).setFace(face);}	
	public void setStateLayer(int index, Layer layer){states.get(index).setLayer(layer);}
	public void setStateAutoStart(int index, boolean autoStart){states.get(index).setAutoStart(autoStart);}	
	public Layer getLayer(){return states.get(currentState).getLayer();}
	public void addState(ObjectState state){states.add(state);}
	
	
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
	
	public void openReactionBubble(ReactionBubble bubble, float duration, boolean loop)
	{
		Global.openReactionBubble(bubble, name, new Point(worldPos.x, worldPos.y - 32 - shadow.getElevation()), duration, loop);
	}
	public void closeReactionBubble()
	{
		Global.closeReactionBubble(name);
	}
	
	public void postCreate()
	{
		for(ObjectState state : states)
			state.postCreate();	
		
		states.get(currentState).init();
	}
	
	public void setStateBubble(int index, String str){states.get(index).setBubble(str);}
	public void setStateSprite(int index, String str){states.get(index).setSprite(str);}
	public void addAction(int index, Action act){states.get(index).addAction(act);}	
	public void addSwitchCondition(int index, String str){states.get(index).addSwitchCondition(str);}
	public void addItemCondition(int index, String str){states.get(index).addItemCondition(str);}
	public void setStateOpts(int index, boolean waitOnActivate, boolean faceOnMove, boolean faceOnActivate) {states.get(index).setOpts(waitOnActivate, faceOnMove, faceOnActivate);}
	public Point getGridPos(){return gridPos;}	
	public Point getWorldPos(){return worldPos; }
	public ObjectState getCurrentState() { return runningState == null ? states.get(currentState) : runningState; }

	public Action getAction(int stateIndex, int actionIndex){return states.get(stateIndex).getAction(actionIndex); }
	public int numActions(int index) { return states.get(index).numActions(); }
	
	public void Face(String face) { states.get(currentState).face(face); }
	
	public boolean hasActions(){ return states.get(currentState).hasActions();}
	
	public void applyPath(ObjectPath path) 
	{ 
		if(runningState != null)
			runningState.applyPath(path);
		else
			states.get(currentState).applyPath(path); 
	}
	
	//returns true if it had something to excut, otherwise false
	public boolean execute()
	{
		runningState = states.get(currentState);
		return runningState.execute();
	}
	
	public boolean AutoStarts()
	{
		updateState();
		return states.get(currentState).AutoStarts();
	}
	
	public void clearActions()
	{
		if(runningState != null && runningState != Global.mapChangeCallingState)
			runningState.clearActions();
		else
			if(states.get(currentState) != Global.mapChangeCallingState)
				states.get(currentState).clearActions();
		
		updateState();
	}
	
	public void clearMovement()
	{
		target = gridPos;
	}
	
	
	public boolean setTarget(String face, boolean ignoreParty)
	{		
		if(!faceLocked)
			states.get(currentState).face(face);
		
		if(face.equals("up"))
			target = new Point(gridPos.x, gridPos.y - 1);
		else if(face.equals("down"))
			target = new Point(gridPos.x, gridPos.y + 1);
		else if(face.equals("left"))
			target = new Point(gridPos.x - 1, gridPos.y);
		else if(face.equals("right"))
			target = new Point(gridPos.x + 1, gridPos.y);			

		boolean collision = false;
		
		//check for object collision
		for(GameObject gb : Global.map.Objects())
		{
			if(gb.getGridPos().equals(target) && gb.getTarget().equals(target))
				collision = true;
		}
		
		//check for tile collision
		if(Global.map.isLoaded())
		{
			Tile orig= null, dest = null;
			for(Tile t : Global.map.LevelTiles())
			{
				if(t.WorldPos().equals(target))
					dest = t;
				if(t.WorldPos().equals(gridPos))
					orig = t;
			}
			
			if(dest != null)
				if(orig != null)collision = dest.isBlocked(orig);
				else collision = !dest.allowEntryFrom(gridPos);		
			else
				if(orig != null)collision = !orig.allowEntryFrom(target);	
				else collision = false;		
			
		}
		
		if(!ignoreParty)
		{
			//check for party collision
			if(Global.party.getGridPos().equals(target) || Global.party.getTarget().equals(target))
				collision = true;
			
			//ignore points on party's path (don't intercept)
			List<Point> partyPath = Global.party.getPath();
			if(partyPath != null)
				if(Global.party.getPath().contains(target))
					collision = true;
		}
		
		//finally ignore the point if it's outside the state's moverange (unless it's in a path)
		if(runningState != null)
		{
			if(!runningState.hasPath() && !states.get(currentState).hasPath())
				if(Math.abs(origin.x - target.x) > states.get(currentState).getMovementRange() ||
					Math.abs(origin.y - target.y) > states.get(currentState).getMovementRange())
						collision = true;
		}
		else
		{
			if(!states.get(currentState).hasPath())
				if(Math.abs(origin.x - target.x) > states.get(currentState).getMovementRange() ||
					Math.abs(origin.y - target.y) > states.get(currentState).getMovementRange())
						collision = true;
		}
		
		
		
		if(collision)
		{
			blockedTarget = face;
			target = gridPos;			
		}
		else
			blockedTarget = null;
		
		return !collision;
			
	}
	private String blockedTarget = null;

	
	public boolean isRunning()
	{
		return runningState != null;
	}
	
	public void update()
	{
		updateElevation();
		updateState();
		
		if(blockedTarget != null)
			setTarget(blockedTarget, false);

		
		if(states.get(currentState).isRunning())
			runningState = states.get(currentState);
		
		if(runningState != null)
		{
			runningState.update();
			if(!runningState.isRunning())
			{
				//running state is done, remove it and do some end-script items
				runningState = null;
			}
		}
		else
		{
			states.get(currentState).update();
			
			
		}		
		
		if(!target.equals(gridPos))
		{
			Point vect = new Point (target.x-gridPos.x, target.y-gridPos.y);
			Point dest = new Point (target.x*32, target.y*32);
			int speed = states.get(currentState).getMoveSpeed();
			
					
			if(vect.x > 0)
				worldPos.x = (worldPos.x > dest.x) ? dest.x : worldPos.x;
			if(vect.x < 0)
				worldPos.x = (worldPos.x < dest.x) ? dest.x : worldPos.x;
			if(vect.y > 0)
				worldPos.y = (worldPos.y > dest.y) ? dest.y : worldPos.y;
			if(vect.y < 0)
				worldPos.y = (worldPos.y < dest.y) ? dest.y : worldPos.y;
			
			if(!worldPos.equals(dest))
			{
				gridAligned = false;
				worldPos.x += vect.x*speed;
				worldPos.y += vect.y*speed;
			}
			else
			{
				gridPos = target;
				gridAligned = true;
				if(blockedTarget == null)
				{
					if(runningState != null)
						runningState.step();
					else
						states.get(currentState).step();
				}
				
			}
		}
		
		
	}
	
	private void updateState()
	{
		
		
		int newState = 0;
		for(int i = 0; i < states.size(); ++i)
			if(states.get(i).meetConditions())
				newState = i;
			else
				break;
		
		if(currentState != newState)
		{
			states.get(currentState).deinit();
			currentState = newState;
			states.get(currentState).init();
			if(states.get(currentState).AutoStarts() || (Global.party.isGridAligned() && gridPos.equals(Global.party.getGridPos().x, Global.party.getGridPos().y)))
				states.get(currentState).execute();
		}

	}
	public void render() 
	{
		if(!hide)
		{
			if(shadow.getElevation() > 0)
				shadow.render();
			states.get(currentState).render(worldPos.x, worldPos.y - shadow.getElevation());

		}
	}
}
