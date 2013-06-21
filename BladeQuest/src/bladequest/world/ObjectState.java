package bladequest.world;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import bladequest.actions.Action;
import bladequest.actions.actMessage;
import bladequest.graphics.ReactionBubble;
import bladequest.graphics.Sprite;
import bladequest.graphics.Tile;
import bladequest.math.PointMath;
import bladequest.system.DataLine;

public class ObjectState {
	private Sprite spr;
	private Tile tileSprite;
	private int currentAction;
	private boolean isRunning;
	private boolean autoStart, animated, faceOnMove, ignorePartyOnMove, faceOnActivate, waitOnActivate;	
	
	private Layer layer; 
	private boolean[] collSides;
	
	private int moveDelay;
	private int moveTimer;
	private int moveRange;
	
	private int imageIndex;
	
	private GameObject parent;
	
	private List<Action> actionList; 
	private List<String> switchList; 
	private List<String> itemReqList;	
	
	private ObjectPath objPath;
	private long objPathWaitStart;
	private boolean objPathWaiting;
	private int moveSpeed = 3;
	private int defaultMoveWait = 0;
	private int moveWait;
	
	private String bubbleName;
	private ObjectPath defaultPath;
	
	
	public ObjectState(GameObject parent)
	{	
		this.parent = parent;
		currentAction = 0;
		isRunning = false;
		waitOnActivate = true;
		faceOnActivate = true;
		faceOnMove = true;
		
		animated = true;
		moveRange = 0;
		moveDelay = 20;
		moveTimer = 0;
		
		imageIndex = 0;
		
		layer = Layer.Level;
		
		actionList = new ArrayList<Action>(); 
		switchList = new ArrayList<String>(); 
		itemReqList = new ArrayList<String>(); 
		
		collSides = new boolean[4];
		for(int i = 0; i < 4; ++i)
			collSides[i] = false;
	}
	
	public void postCreate()
	{
		if(spr == null)
			faceOnActivate = false;			
	}
	
	public void makeSolid() 
	{
		for(int i = 0; i < 4; ++i)
			collSides[i] = true;
	}
	public void setCollision(boolean left, boolean top, boolean right, boolean bottom)
	{
		collSides[0] = left;
		collSides[1] = top;
		collSides[2] = right;
		collSides[3] = bottom;	
	}
	public void setDefaultPath(String serializedPath)
	{
		defaultPath = new ObjectPath("");
		defaultPath.deserialize(serializedPath);
		defaultPath.setLooping(true);
	}
	
	public void setIgnorePartyOnMove(boolean ignore)
	{
		ignorePartyOnMove = ignore;
	}
	public boolean getIgnorePartyOnMove()
	{
		return ignorePartyOnMove;
	}
	public boolean[] getCollision() { return collSides; }
	public boolean hasCollision() { return collSides[0] || collSides[1] || collSides[2] || collSides[3]; }
	
	public int getMoveSpeed() { return moveSpeed; }
	public boolean isRunning() { return isRunning; }
	public void setAutoStart(boolean start) { autoStart = start; }
	public int getMovementRange() { return moveRange; }	
	public Layer getLayer(){return layer;}	
	public void setLayer(Layer layer){this.layer = layer;}		
	public void addSwitchCondition(String str){switchList.add(str);}	
	public void addItemCondition(String str){itemReqList.add(str);}
	public void addAction(Action act){actionList.add(act);}	
	
	public Action getAction(int index){ return actionList.get(index);}
	public int numActions() { return actionList.size(); }
	
	public boolean hasPath() { return objPath != null; }
	public void setSpeedOptions(int speed, int wait)
	{
		moveSpeed = speed;
		defaultMoveWait = wait;
	}
	
	public void setBubble(String name){bubbleName = name; }
	
	public void setFace(String face){if(spr != null) spr.changeFace(face);}
	public void setImageIndex(int index){imageIndex = index;}
	public void setAnimated(boolean animated) { this.animated = animated; }
	public void setOpts(boolean waitOnActivate, boolean faceOnMove, boolean faceOnActivate) 
	{ 
		this.waitOnActivate = waitOnActivate; 
		this.faceOnActivate = faceOnActivate; 
		this.faceOnMove = faceOnMove; 
	}
	public void setMovement(int moveRange, int moveDelay)
	{
		this.moveRange = moveRange;
		this.moveDelay = moveDelay;	
	}
	public boolean AutoStarts(){return autoStart;}
	
	public boolean hasActions() { return actionList.size() >0; }
	
	public void setSprite(String spr)
	{
		if(spr.contains("tile"))
		{
			DataLine line = new DataLine(spr);
			tileSprite = new Tile(0,0,Integer.parseInt(line.values.get(0)), Integer.parseInt(line.values.get(1)), Layer.Level, 0);
			animated = false;
		}
		else
			this.spr  = new Sprite(Global.sprites.get(spr));
	}
	
	public void deinit()
	{
		if(bubbleName != null)
			Global.closeReactionBubble(parent.Name());
	}
	
	public void init()
	{
		if(bubbleName != null)
		{
			ReactionBubble bubble = new ReactionBubble(Global.reactionBubbles.get(bubbleName));
			Point drawPos = new Point(parent.getWorldPos().x, parent.getWorldPos().y - 32);
			Global.openReactionBubble(bubble, parent.Name(), drawPos, -1, true);
		}	
		
		if(defaultPath != null)
			applyPath(defaultPath);
		
	}
	
	public boolean execute()
	{
		if(Global.party.allowMovement() && actionList.size() > 0)
		{
			if(waitOnActivate)
				Global.party.clearMovementPath();
			FaceOnActivate();
			Global.party.setAllowMovement(!waitOnActivate);
			isRunning = true;
			parent.clearMovement();
			objPath = null;
			actionList.get(currentAction).run();

			return true;
		}
		else
			return false;
		
	}
	
	public void applyPath(ObjectPath path)
	{
		objPath = path;
		HandleObjectPath();
		
	}
	
	public void clearActions()
	{
		//actionList.clear();
		isRunning = false;
		currentAction = 0;
		
	}
	
	public void step()
	{
		if(objPath != null)
		{
			if(!isRunning && defaultMoveWait > 0)
			{
				objPathWaitStart = System.currentTimeMillis();
				moveWait = defaultMoveWait;
				objPathWaiting = true;
				
			}
			else
			{
				objPath.advanceActions();
				HandleObjectPath();
			}
			
		}
			
	}
	
	public static interface ActivationHandler
	{
		boolean canActivate(Point startPoint, Point partyPos);
	}
	
	private class DefaultActivationHandler implements ActivationHandler
	{
		public boolean canActivate(Point startPoint, Point partyPos)
		{
			if (autoStart) return false;
			if (PointMath.length(PointMath.subtract(startPoint, parent.getGridPos())) > 1.0f)
			{
				return false;
			}
			
			return true;			
		}
	}
	
	private ActivationHandler activationHandler = new DefaultActivationHandler();
	
	public void setActivationHandler(ActivationHandler handler)
	{
		activationHandler = handler;
	}
	
	public boolean canActivate(Point startPoint, Point partyPos)
	{
		return activationHandler.canActivate(startPoint, partyPos);
	}
	
	private void HandleObjectPath()
	{		
		if(objPath.isDone())
		{
			if(objPath.loops())
			{				
				objPath.reset();
				HandleObjectPath();
			}				
			else
				objPath = null;
		}
		else
		{
			switch(objPath.getCurrentAction())
			{
			case MoveLeft:
				move("left", ignorePartyOnMove);
				break;
			case MoveUp:
				move("up", ignorePartyOnMove);
				break;
			case MoveRight:
				move("right", ignorePartyOnMove);
				break;
			case MoveDown:
				move("down", ignorePartyOnMove);
				break;
			case FaceLeft:
				parent.Face("left");
				objPathWaitStart = System.currentTimeMillis();
				moveWait = defaultMoveWait;
				objPathWaiting = true;
				break;
			case FaceUp:
				parent.Face("up");
				objPathWaitStart = System.currentTimeMillis();
				moveWait = defaultMoveWait;
				objPathWaiting = true;
				break;
			case FaceRight:
				parent.Face("right");
				objPathWaitStart = System.currentTimeMillis();
				moveWait = defaultMoveWait;
				objPathWaiting = true;
				break;
			case FaceDown:
				parent.Face("down");
				objPathWaitStart = System.currentTimeMillis();
				moveWait = defaultMoveWait;
				objPathWaiting = true;
				break;
			case IncreaseMoveSpeed:
				if(moveSpeed < 6)
					moveSpeed++;
				objPathWaitStart = System.currentTimeMillis();
				moveWait = defaultMoveWait;
				objPathWaiting = true;
				break;
			case DecreaseMoveSpeed:
				if(moveSpeed > 1)
					moveSpeed--;
				objPathWaitStart = System.currentTimeMillis();
				moveWait = defaultMoveWait;
				objPathWaiting = true;
				break;
			case Hide:
				parent.hide = true;
				objPathWaitStart = System.currentTimeMillis();
				moveWait = defaultMoveWait;
				objPathWaiting = true;
				break;
			case Show:
				parent.hide = false;
				objPathWaitStart = System.currentTimeMillis();
				moveWait = defaultMoveWait;
				objPathWaiting = true;
				break;
			case LockFacing:
				parent.faceLocked = true;
				objPathWaitStart = System.currentTimeMillis();
				moveWait = defaultMoveWait;
				objPathWaiting = true;
				break;
			case UnlockFacing:
				parent.faceLocked = false;
				objPathWaitStart = System.currentTimeMillis();
				moveWait = defaultMoveWait;
				objPathWaiting = true;
				break;	
			case Wait:
				objPathWaitStart = System.currentTimeMillis();
				moveWait = 100;
				objPathWaiting = true;
				break;
			}
		}
	}
	
	public void FaceOnActivate()
	{
		if(faceOnActivate && tileSprite == null)
		{
			Point vect = new Point (Global.party.getGridPos().x-parent.getGridPos().x, Global.party.getGridPos().y-parent.getGridPos().y);
			if(vect.x > 0)
				spr.changeFace("right");
			if(vect.x < 0)
				spr.changeFace("left");
			if(vect.y < 0)
				spr.changeFace("up");
			if(vect.y > 0)
				spr.changeFace("down");			
		}		
	}
	
	public boolean meetConditions()
	{
		boolean returnval = true;
		if(switchList.size() > 0)
			for(String str : switchList)
			{
				if(Global.switches.containsKey(str))
					returnval = returnval && Global.switches.get(str);
				else				
				{
					Global.switches.put(str, false);
					returnval = false;
				}
					
				
			}
				
		if(itemReqList.size() > 0)
			for(String str : itemReqList)
				returnval = returnval && Global.party.hasItem(str);
		
		return returnval;
	}
	
	public void update()
	{			
		if(isRunning && waitOnActivate)
			Global.party.setAllowMovement(false);
		
		if(!isRunning && Global.party.allowMovement() && autoStart)
			execute();
			
		//handle path waiting
		if(objPathWaiting && objPath != null)
		{
			if(System.currentTimeMillis() - objPathWaitStart >= moveWait)
			{
				objPathWaiting = false;
				objPath.advanceActions();
				HandleObjectPath();
			}			
		}

		if(objPath == null && !isRunning && moveRange > 0 && parent.isGridAligned() && Global.GameState == States.GS_WORLDMOVEMENT)
		{
			moveTimer++;
			if(moveTimer >= moveDelay)
			{
				moveTimer = 0;
				int ri = Global.rand.nextInt(4);
				switch(ri)
				{
				case 0:
					move("up", ignorePartyOnMove);
					break;
				case 1:
					move("down", ignorePartyOnMove);
					break;
				case 2:
					move("left", ignorePartyOnMove);
					break;
				case 3:
					move("right", ignorePartyOnMove);
					break;					
				}
			}
		}
		
		if(isRunning)
		{				
			if(actionList.get(currentAction).isDone())
			{
				actionList.get(currentAction).reset();
					
				currentAction++;
				
				if (currentAction >= actionList.size())
				{
					currentAction = 0;
					isRunning = false;
					if(waitOnActivate)
						Global.party.setAllowMovement(true);
					if(defaultPath != null)
					{
						applyPath(defaultPath);
					}				
					else if (!parent.isGridAligned())
					{
						parent.restorePath();
					}
				}
				else
				{
					actionList.get(currentAction).run();
					currentAction += actionList.get(currentAction).skip;
					actionList.get(currentAction).skip = 0;
				}
			}
			else if(currentAction+1 < actionList.size() && 
					actionList.get(currentAction).name.equals("actMessage") &&
					actionList.get(currentAction+1).name.equals("actMessage") &&
					!((actMessage)actionList.get(currentAction)).yesNo() )
			{				
				actionList.get(currentAction).reset();
				currentAction++;
				actionList.get(currentAction).run();
				
			}
		}

	}
	
	private void move(String face, boolean ignorePartyCollision)
	{
		parent.setTarget(face, ignorePartyCollision);		
	}
	
	public void face(String face)
	{
		if(spr != null && faceOnMove && tileSprite == null)
			spr.changeFace(face);
	}
	
	private void updateAnimation()
	{
		if(Global.updateAnimation)
		{				
			imageIndex++;
			
			if(imageIndex >= spr.getNumFrames())
				imageIndex = 0;
		}	
		
	}
	
	public void render(int x, int y) 
	{
		if(spr != null)
		{
			if(animated)
					updateAnimation();
			spr.render(x - spr.getWidth()/2 + 16, y - spr.getHeight() + 32, imageIndex);				
		}
		else
			if(tileSprite != null)
				tileSprite.renderToWorld(x, y, Global.map.tileset);
		
	}
}
