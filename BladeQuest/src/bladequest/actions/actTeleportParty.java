package bladequest.actions;

import android.graphics.Point;
import bladequest.actions.Action;
import bladequest.world.*;


public class actTeleportParty extends Action 
{
	private Point dest;
	private String mapName;
	private GameObject parent;
	
	public actTeleportParty(GameObject parent, int x, int y, String mapname)
	{
		super();
		this.mapName = mapname;
		dest = new Point(x, y);
		this.parent = parent;		
	}
	
	@Override
	public void run()
	{
		if(!Global.map.Name().equals(mapName))
			Global.LoadMap(mapName, parent.getCurrentState());

		Global.party.teleport(dest.x, dest.y);
	}
	
	@Override
	public boolean isDone()
	{
		return true;
	}

}
