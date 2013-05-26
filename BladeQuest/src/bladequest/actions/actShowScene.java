package bladequest.actions;

import bladequest.actions.Action;
import bladequest.world.*;


public class actShowScene extends Action 
{
	private String sceneName;
	InputTriggers inputTrigger;
	float timer;
	int r, g, b, fadeSpeed;
	boolean wait;
	
	public enum InputTriggers
	{
		Input,
		Timer
	}
	
	public actShowScene(String sceneName)
	{
		super();
		this.sceneName = sceneName;	
		
	}
	
	@Override
	public void run()
	{
		Global.ShowScene(sceneName);
		Global.showScene.done = false;
		Global.showScene.startTime = System.currentTimeMillis();
		Global.showScene.waitTime = timer;
	}
	
	@Override
	public boolean isDone()
	{
		return !wait || Global.showScene.done;
	}

}
