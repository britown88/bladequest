package bladequest.bladescript.libraries;

import bladequest.actions.*;
import bladequest.bladescript.LibraryWriter;
import bladequest.system.DataLine;
import bladequest.world.GameObject;
import bladequest.world.Global;
import bladequest.world.Layer;
import bladequest.world.ObjectPath;
import bladequest.world.ObjectState;

public class GameObjectLibrary 
{
	public static void publishLibrary(LibraryWriter library) 
	{
		try {
			library.addAllIn(GameObjectLibrary.class);
		} catch (Exception e) {
		}
	}
	
	public static GameObject newObject(String name, int x, int y)
	{
		GameObject loadedObject = new GameObject(name, x, y);		
		Global.map.addObject(loadedObject);		
		return loadedObject;
	}	
	public static ObjectState setCollisionData(ObjectState state, boolean left, boolean top, boolean right, boolean bottom)
	{
		state.setCollision(left, top, right, bottom);
		return state;
	}	
	public static ObjectState setMovement(ObjectState state, int range, int delay)
	{
		state.setMovement(range, delay);
		return state;
	}	
	public static ObjectState setOptions(ObjectState state, boolean waitOnActivate, boolean faceOnMove, boolean faceOnActivate)
	{
		state.setOpts(waitOnActivate, faceOnMove, faceOnActivate);
		return state;
	}	
	public static ObjectState setImageIndex(ObjectState state, int index)
	{
		state.setImageIndex(index);
		return state;
	}		
	public static ObjectState setAnimated(ObjectState state, boolean animated)
	{
		state.setAnimated(animated);
		return state;
	}	
	public static ObjectState setFace(ObjectState state, String face)
	{
		state.setFace(face);
		return state;
	}	
	public static ObjectState setLayer(ObjectState state, String AboveBelow)
	{
		state.setLayer(Layer.valueOf(AboveBelow));
		return state;
	}	
	public static ObjectState setAutoStart(ObjectState state, boolean autostart)
	{
		state.setAutoStart(autostart);
		return state;
	}	
	public static ObjectState setSprite(ObjectState state, String sprite)
	{
		state.setSprite(sprite);
		return state;
	}	
	public static ObjectState setSpriteFromTile(ObjectState state, int x, int y)
	{
		state.setSprite("tile " + x + " " + y);
		return state;
	}	
	public static ObjectState setBubble(ObjectState state, String bubbleName)
	{
		state.setBubble(bubbleName);
		return state;
	}
	public static ObjectState addAction(ObjectState state, Action action)
	{
		state.addAction(action);
		return state;
	}	
	public static ObjectState addSwitchCondition(ObjectState state, String switchName)
	{
		state.addSwitchCondition(switchName);
		return state;
	}	
	public static ObjectState addItemCondition(ObjectState state, String itemName)
	{
		state.addItemCondition(itemName);
		return state;
	}
	public static ObjectState createState(GameObject obj)
	{
		ObjectState state = new ObjectState(obj);
		obj.addState(state);
		return state;
	}
	
	
	public static Action addToBranch(Action action, int index, Action actToAdd)
	{
		action.addToBranch(index, actToAdd);
		return action;
	}
	public static Action fadeControl(int fadeSpeed, int a, int r, int g, int b, boolean fadeOut, boolean wait)
	{		
		Action act = new actFadeControl(fadeSpeed, a, r, g, b, fadeOut, wait);
		return act;
	}
	public static Action messageWithYesNo(String message)
	{		
		Action act = new actMessage(message, true);
		return act;
	}
	public static Action message(String message)
	{		
		Action act = new actMessage(message);
		return act;
	}
	public static Action modifyGold(int gold)
	{		
		Action act = new actModifyGold(gold);
		return act;
	}
	public static Action modifyInventory(String name, int count, boolean remove)
	{		
		Action act = new actModifyInventory(name, count, remove);
		return act;
	}
	public static Action modifyParty(String name, boolean remove)
	{		
		Action act = new actModifyParty(name, remove);
		return act;
	}
	public static Action showScene(String name)
	{		
		Action act = new actShowScene(name);
		return act;
	}
	public static Action openMerchant(String name, float discount)
	{		
		Action act = new actMerchant(name, discount);
		return act;
	}
	public static Action openNameSelect(String charName)
	{		
		Action act = new actNameSelect(charName);
		return act;
	}
	public static Action resetgame(int i)
	{		
		Action act = new actResetGame();
		return act;
	}
	public static Action playMusic(String song, boolean playIntro, boolean loop, float fadeTime)
	{		
		Action act = new actPlayMusic(song, playIntro, loop, fadeTime);
		return act;
	}
	public static Action pauseMusic(float fadeTime)
	{		
		Action act = new actPauseMusic(fadeTime);
		return act;
	}
	public static Action openBubble(String name, String target, float duration, boolean loop, boolean wait)
	{		
		Action act = new actReactionBubble(name, target, duration, loop, wait);
		return act;
	}
	public static Action closeBubble(String target)
	{		
		Action act = new actReactionBubble(target);
		return act;
	}
	public static Action panControl(int x, int y, int speed, boolean wait)
	{		
		Action act = new actPanControl(x, y, speed, wait);
		return act;
	}
	public static Action createPath(String target, boolean wait)
	{		
		ObjectPath path = new ObjectPath(target);
		Action act = new actPath(path, wait);
		return act;
	}
	public static Action restoreParty(int i)
	{		
		Action act = new actRestoreParty();
		return act;
	}
	public static Action shakeControl(float duration, int intensity, boolean wait)
	{		
		Action act = new actShake(duration, intensity, wait);
		return act;
	}
	public static Action startBattle(String encounter)
	{		
		Action act = new actStartBattle(encounter);
		return act;
	}
	public static Action switchControl(String switchName, boolean state)
	{		
		Action act = new actSwitch(switchName, state);
		return act;
	}
	public static Action openSaveMenu(int i)
	{		
		Action act = new actSaveMenu();
		return act;
	}
	public static Action teleportParty(GameObject parent, int x, int y, String mapname)
	{		
		Action act = new actTeleportParty(parent, x, y, mapname);
		return act;
	}
	public static Action wait(float seconds)
	{		
		Action act = new actWait(seconds);
		return act;
	}
	
	public static Action addPathAction(Action path, String action)
	{
		((actPath)path).getPath().addAction(ObjectPath.Actions.valueOf(action));
		return path;
	}
	
	

}
