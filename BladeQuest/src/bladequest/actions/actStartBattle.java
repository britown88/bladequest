package bladequest.actions;

import bladequest.actions.Action;
import bladequest.world.*;


public class actStartBattle extends Action 
{
	String encounter;
	GameObject go;
	
	public actStartBattle(GameObject go, String encounter)
	{
		super();
		this.encounter = encounter;
		this.go =go;
	}
	
	@Override
	public void run()
	{
		Global.beginBattle(go, encounter);
		done = true;
	}
	
	@Override
	public boolean isDone()
	{
		return done;
	}

}