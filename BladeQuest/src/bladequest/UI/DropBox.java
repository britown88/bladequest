package bladequest.UI;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.graphics.Rect;
import bladequest.world.Global;

public class DropBox extends MenuPanel
{

	private List<DropBoxRect> panelRects;
	
	private DropBoxRect lastMovedRect;
	
	private int selectedIndex;
	private Point tapStartPos, selectedPanelStartPos;
	
	private boolean moved;
	
	public DropBox(int x, int y, int width, int height)
	{
		super(x, y, width, height);		
			
		panelRects = new ArrayList<DropBoxRect>();
		
		selectedIndex = -1;
	}
	
	//returns the index of the added rect
	public int addPanelRect(Rect r)
	{
		r.offset(pos.x, pos.y);
		
		DropBoxRect dbr = new DropBoxRect(Global.vpToScreen(r), panelRects.size());
	
		panelRects.add(dbr);
		return panelRects.size() - 1;
	}
	
	public DropBoxRect getLastMovedRect(){return lastMovedRect;}
	
	public void addPanel(MenuPanel panel, int index)
	{
		if(index < 0 || index >= panelRects.size())
			return;
		
		//displaces existing panels on add...shouldnt happen but better safe than sorry
		if(panelRects.get(index).getPanel() != null)
		{
			int newIndex = index + 1;
			if(newIndex < 0)
				newIndex = panelRects.size() - 1;
			else if(newIndex >= panelRects.size())
				newIndex= 0;
			
			movePanel(index, newIndex, true);
		}
		
		panelRects.get(index).setPanel(panel);
		panel.pos = new Point(
				Global.screenToVPX(panelRects.get(index).getRect().left), 
				Global.screenToVPY(panelRects.get(index).getRect().top));
		
	}
	
	public void setPanelLocked(int index, boolean locked)
	{
		if(index < 0 || index >= panelRects.size())
			return;
		
		panelRects.get(index).setLocked(locked);
		
	}
	
	public MenuPanel[] getPanels()
	{
		MenuPanel[] panels= new MenuPanel[panelRects.size()];
		
		for(int i = 0; i < panelRects.size(); ++i)
			panels[i] = panelRects.get(i).getPanel();
		
		return panels;
	}
	
	private void movePanel(int srcIndex, int destIndex, boolean autoMoveFirst)
	{
		if(srcIndex == destIndex)
			return;
		
		if(srcIndex < 0 || srcIndex >= panelRects.size() ||
				destIndex < 0 || destIndex >= panelRects.size())
			return;//invalidindices
		
		DropBoxRect src = panelRects.get(srcIndex);
		DropBoxRect dest = panelRects.get(destIndex);
		
		int direction = srcIndex < destIndex ? -1 : 1;
		if(autoMoveFirst) direction = -direction;
		
		if(src.isLocked() || src.getPanel() == null)
			return;//cant move or moving nothing
		
		//move onward to unlocked destination
		if(dest.isLocked())
		{
			int newIndex = destIndex+direction;
			if(newIndex < 0)
				newIndex = panelRects.size() - 1;
			else if(newIndex >= panelRects.size())
				newIndex= 0;
			
			movePanel(srcIndex, newIndex, true);
			return;
		}
		
		MenuPanel displacedPanel = dest.getPanel();
		MenuPanel movingPanel = src.getPanel();
		
		src.setPanel(null);//clear panel of source in case it needs to be replaced by a move
		
		//displace a panel recursively
		if(displacedPanel != null)
			movePanel(destIndex, destIndex + direction, true);
		
		//actually move the panel
		Point target = new Point(
				Global.screenToVPX(dest.getRect().left), 
				Global.screenToVPY(dest.getRect().top));
		
		dest.setPanel(movingPanel);		
		if(autoMoveFirst)
			movingPanel.move(target.x, target.y, 10);
		
		
	}
	


	
	@Override
	public void render()
	{
		renderFrame();
		
		int i = 0;
		for(DropBoxRect dbr : panelRects)
		{
			if(dbr.getPanel() != null && i != selectedIndex)
				dbr.getPanel().render();
			
			++i;
		}
		
		if(selectedIndex != -1 && panelRects.get(selectedIndex).getPanel() != null)
			panelRects.get(selectedIndex).getPanel().render();
		
		renderDarken();

	}
	
	@Override
	public void update()
	{
		updateFrame();
		for(DropBoxRect dbr : panelRects)
		{
			if(dbr.getPanel() != null)
				dbr.getPanel().update();
		}
	}
	
	public void touchActionDown(int x, int y) 
	{
		lastMovedRect = null;
		
		if(getRect().contains(x, y))
		{
			int i = 0;
			
			for(DropBoxRect dbr : panelRects)
			{
				MenuPanel p = dbr.getPanel();
				if(p != null && !p.isMoving() && p.contains(x, y))
				{
					selectedIndex = i;
					
					Rect r = p.getRect();
					tapStartPos = new Point(x - r.left , y - r.top);
					selectedPanelStartPos = new Point(p.pos);
					
					return;
					
				}
				
				++i;
			}
				
		}		
	}
	
	public MenuPanel touchActionUp(int x, int y) 
	{
		if(selectedIndex > -1)
		{
			MenuPanel mp = panelRects.get(selectedIndex).getPanel();
			
						
			if(mp != null)
			{
				
				Point target = new Point(
						Global.screenToVPX(panelRects.get(selectedIndex).getRect().left), 
						Global.screenToVPY(panelRects.get(selectedIndex).getRect().top));
					
				mp.move(target.x, target.y, 10);
			}
			
			if(moved)
				mp = null;
			
			moved = false;
			selectedIndex = -1;
			
			return mp;
			
		}
		
		
		
		return null;
		

	}
	
	
	public void touchActionMove(int x, int y) 
	{	
		
		if(selectedIndex > -1)
		{
			DropBoxRect selectedDBR = panelRects.get(selectedIndex);
			MenuPanel mp = selectedDBR.getPanel();			
			
			mp.pos.x = Global.screenToVPX(x - tapStartPos.x);
			mp.pos.y = Global.screenToVPY(y - tapStartPos.y);
			
			if(selectedDBR.isLocked())
			{
				int moveDistance = 5;
				
				mp.pos.x = Math.min(selectedPanelStartPos.x+ moveDistance, mp.pos.x);
				mp.pos.x = Math.max(selectedPanelStartPos.x- moveDistance, mp.pos.x);
				
				mp.pos.y = Math.min(selectedPanelStartPos.y+ moveDistance, mp.pos.y);
				mp.pos.y = Math.max(selectedPanelStartPos.y- moveDistance, mp.pos.y);
			}
			
			int i = 0;
			int currentIndex = -1;
			for(DropBoxRect dbr : panelRects)
			{
				if(dbr.getRect().contains(x, y))
				{
					currentIndex = i;
					break;
				}
								
				++i;
			}
			
			if(currentIndex != selectedIndex && currentIndex != -1)
			{
				moved = true;
				
				if(!panelRects.get(currentIndex).isLocked() && !selectedDBR.isLocked())
				{					
					movePanel(selectedIndex, currentIndex, false);
					selectedIndex = currentIndex;
					lastMovedRect = panelRects.get(selectedIndex);
				}
				
				
			}		

		}
	}
	
	
}
