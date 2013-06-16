package bladequest.bladescript.libraries;

import android.graphics.Point;
import bladequest.actions.Action;
import bladequest.actions.actAllowSaving;
import bladequest.actions.actAnimation;
import bladequest.actions.actClearAnimations;
import bladequest.actions.actDefaultBattleMusic;
import bladequest.actions.actDisableBattleMusic;
import bladequest.actions.actElevation;
import bladequest.actions.actExpectInput;
import bladequest.actions.actFadeControl;
import bladequest.actions.actFilter;
import bladequest.actions.actFlash;
import bladequest.actions.actFloat;
import bladequest.actions.actGameOver;
import bladequest.actions.actGoldTransaction;
import bladequest.actions.actMerchant;
import bladequest.actions.actMessage;
import bladequest.actions.actMessage.Position;
import bladequest.actions.actModifyGold;
import bladequest.actions.actModifyInventory;
import bladequest.actions.actModifyParty;
import bladequest.actions.actNameSelect;
import bladequest.actions.actPanControl;
import bladequest.actions.actPath;
import bladequest.actions.actPauseMusic;
import bladequest.actions.actPlayMusic;
import bladequest.actions.actPopSong;
import bladequest.actions.actPushSong;
import bladequest.actions.actReactionBubble;
import bladequest.actions.actRemoveFilter;
import bladequest.actions.actResetGame;
import bladequest.actions.actRestoreParty;
import bladequest.actions.actSaveMenu;
import bladequest.actions.actSetBattleMusic;
import bladequest.actions.actShake;
import bladequest.actions.actShowScene;
import bladequest.actions.actStartBattle;
import bladequest.actions.actSwitch;
import bladequest.actions.actTeleportParty;
import bladequest.actions.actUnloadScene;
import bladequest.actions.actWait;
import bladequest.bladescript.LibraryWriter;
import bladequest.bladescript.ScriptVar;
import bladequest.graphics.AnimationBuilder;
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
	public static ObjectState setIgnorePartyOnMove(ObjectState state, boolean ignore)
	{
		state.setIgnorePartyOnMove(ignore);
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
	public static ObjectState setDefaultPath(ObjectState state, String path)
	{
		state.setDefaultPath(path);
		return state;
	}
	public static ObjectState setMoveOptions(ObjectState state, int speed, int wait)
	{
		state.setSpeedOptions(speed, wait);
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
	public static Action setBranchLoop(Action action, int index, boolean loop)
	{
		action.setBranchLoop(index, loop);
		return action;
	}
	public static Action startGoldTransaction(int gold)
	{
		return new actGoldTransaction(gold);
	}
	public static Action fadeControl(float fadeTime, int a, int r, int g, int b, boolean fadeOut, boolean wait)
	{		
		Action act = new actFadeControl(fadeTime, a, r, g, b, fadeOut, wait);
		return act;
	}
	public static Action flash(float flashLength, int a, int r, int g, int b, boolean wait)
	{
		return new actFlash(a, r, g, b, flashLength, wait);
	}
	public static Action allowSaving(int i)
	{
		return new actAllowSaving();
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
	public static Action messageTimed(String message, float duration)
	{		
		Action act = new actMessage(message, duration);
		return act;
	}
	public static Action messageTop(String message)
	{		
		Action act = new actMessage(message, Position.Top);
		return act;
	}
	public static Action modifyGold(int gold)
	{		
		Action act = new actModifyGold(gold);
		return act;
	}
	public static Action playAnimation(AnimationBuilder builder, String source, String target, boolean wait)
	{		
		Action act = new actAnimation(builder, source, target, wait);
		return act;
	}
	public static Action playAnimation(AnimationBuilder builder, Point source, Point target, boolean wait)
	{		
		Action act = new actAnimation(builder, source, target, wait);
		return act;
	}
	public static Action playAnimationStoppedShort(AnimationBuilder builder, String source, String target, float secondsShort)
	{		
		Action act = new actAnimation(builder, source, target, secondsShort);
		return act;
	}
	public static Action filter(float[] filter)
	{		
		Action act = new actFilter(filter);
		return act;
	}
	public static Action removeFilter(int value)
	{		
		Action act = new actRemoveFilter();
		return act;
	}
	
	
	public static Action setBattleMusic(ScriptVar onInit, ScriptVar onVictory, ScriptVar onBattleEnd)
	{
		return new actSetBattleMusic(onInit, onVictory, onBattleEnd);
	}
	
	public static Action disableBattleMusic(int i)
	{
		return new actDisableBattleMusic();
	}
	public static Action defaultBattleMusic(int i)
	{
		return new actDefaultBattleMusic();
	}
 	public static Action clearAnimations(int i)
	{
		return new actClearAnimations();
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
	public static Action unloadScene(int i)
	{		
		Action act = new actUnloadScene();
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
	public static Action resetGame(int i)
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
	public static Action pushSong(int value)
	{
		return new actPushSong();
	}
	public static Action popSong(int value)
	{
		return new actPopSong();
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
	public static Action panControl(int x, int y, float time, boolean wait)
	{		
		Action act = new actPanControl(x, y, time, wait);
		return act;
	}
	public static Action expectInput(float delay)
	{		
		Action act = new actExpectInput(delay);
		return act;
	}
	public static Action createPath(String target, boolean wait, String cmds)
	{		
		ObjectPath path = new ObjectPath(target);
		path.deserialize(cmds);
		Action act = new actPath(path, wait);
		return act;
	}
	public static Action restoreParty(int i)
	{		
		Action act = new actRestoreParty();
		return act;
	}
	public static Action changeElevation(String target, int pixels, float time, boolean wait)
	{
		return new actElevation(target, pixels, time, wait);
	}
	public static Action setFloating(String target, boolean startFloating, int period, int intensity)
	{
		return new actFloat(target, startFloating, period, intensity);
	}
	public static Action shakeControl(float duration, int intensity, boolean wait)
	{		
		Action act = new actShake(duration, intensity, wait);
		return act;
	}
	public static Action startBattle(String encounter, boolean allowGameOver)
	{		
		Action act = new actStartBattle(encounter, allowGameOver);
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
	public static Action gameOver(int i)
	{
		Action act = new actGameOver();
		return act;
	}

}
