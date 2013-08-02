package bladequest.spudquest;

import bladequest.UI.DropBox;
import bladequest.graphics.Scene;
import bladequest.world.Global;

public class UI 
{
	private Scene backdrop;
	private DropBox board;
	
	public UI()
	{
		backdrop = Global.scenes.get("sqbd");
		backdrop.load();
	}
	
	public void update()
	{
		
	}
	
	public void render()
	{
		backdrop.render();
	}
	
	public void touchActionMove(int x, int y)
	{
		
	}
	
	public void touchActionUp(int x, int y)
	{
		
	}
	
	public void touchActionDown(int x, int y)
	{
		
	}
	

}
