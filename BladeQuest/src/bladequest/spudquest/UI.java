package bladequest.spudquest;

import android.graphics.Rect;
import bladequest.UI.DropBox;
import bladequest.UI.MenuPanel;
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
		
		buildDropBox();
	}
	
	private void buildDropBox()
	{
		board = new DropBox(0, 0, Global.vpWidth, Global.vpHeight);
		board.drawFrame = false;
		
		for(int y = 0; y < 4; ++y)
			for(int x = 0; x< 4; ++x)
				board.addPanelRect(new Rect(10 + 60*x, 50 + 60*y, 10 + 60*x + 48, 50 + 60*y + 48));
		
		
		for(int y = 0; y < 2; ++y)
			for(int x = 0; x< 4; ++x)
				board.addPanelRect(new Rect(246 + 50*x, 186 + 50*y, 246 + 50*x + 48, 186 + 50*y + 48));
		
		for(int i = 0; i < 24; ++i)
		{
			MenuPanel mp = new MenuPanel(0, 0, 48, 48);
			board.addPanel(mp, i);
		}
		
	}
	
	public void update()
	{
		board.update();
	}
	
	public void render()
	{
		backdrop.render();
		board.render();
	}
	
	public void touchActionMove(int x, int y)
	{
		board.touchActionMove(x, y);
	}
	
	public void touchActionUp(int x, int y)
	{
		board.touchActionUp(x, y);
	}
	
	public void touchActionDown(int x, int y)
	{
		board.touchActionDown(x, y);
	}
	

}
