package bladequest.context;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import bladequest.UI.MenuPanel;
import bladequest.UI.MenuPanel.Anchors;
import bladequest.context.ContextItem.Status;
import bladequest.world.Global;

public class ContextMenu 
{
	private static final int itemHeight = 32;
	private static final int itemWidthBuffer = 20;
	private Paint itemTextPaint;
	
	private List<MenuPanel> panels;
	private MenuPanel closingPanel;
	
	private ContextItem runningItem;
	
	public ContextMenu()
	{
		panels = new ArrayList<MenuPanel>();
		
		itemTextPaint = Global.textFactory.getTextPaint(12, Color.WHITE, Align.LEFT);
		
	}	
	
	public void addItem(String name, ContextItem item)
	{
		if(panels.size() == 4)
			return;
		
		Rect nameRect = new Rect();
		itemTextPaint.getTextBounds(name, 0, name.length()-1, nameRect);		
		int nameWidth = nameRect.width() + itemWidthBuffer;
		
		MenuPanel newPanel = new MenuPanel(Global.vpWidth, panels.size() * itemHeight, 0, itemHeight);
		newPanel.setOpenSize(nameWidth, itemHeight);
		newPanel.anchor = Anchors.TopRight;
		newPanel.openSpeed = 7;
		newPanel.addTextBox(name, itemWidthBuffer/3, itemHeight/2, itemTextPaint);
		newPanel.obj = item;
		newPanel.thickFrame = false;
		
		newPanel.open();
		
		panels.add(newPanel);
		
		item.onAdd();
	}	
	
	public void removeItem(ContextItem item)
	{
		for(MenuPanel panel : panels)
			if(panel.obj == item)
			{
				item.onRemove();
				closingPanel = panel;
				panel.close();
				return;
			}
	}
	
	public void update()
	{
		for(MenuPanel mp : panels)
			mp.update();
		
		if(closingPanel != null && closingPanel.Closed())
		{
			int index = panels.indexOf(closingPanel);
			
			for(int i = index + 1; i < panels.size(); ++i)
				panels.get(i).move(panels.get(i).pos.x, panels.get(i).pos.y - itemHeight, 5);
			
			panels.remove(index);
			closingPanel = null;
		}
		
		if(runningItem != null)
		{
			ContextItem.Status result = runningItem.update();
			
			if(result == Status.Finished)
				runningItem = null;			
			
		}
	}
	
	public void render()
	{
		for(MenuPanel mp : panels)
			mp.render();
	}
	
	public boolean contains(int x, int y)
	{
		for(MenuPanel panel : panels)
			if(panel.contains(x, y))
				return true;
		
		return false;
	}
	
	public void touchActionUp(int x, int y)
	{		
		if(closingPanel == null)
			for(MenuPanel panel : panels)
			{
				if(panel.Opened() && !panel.isMoving() && panel.contains(x, y))
				{
					if(runningItem == null)
					{
						runningItem = (ContextItem)panel.obj;
						runningItem.activate();
						removeItem((ContextItem)panel.obj);						
					}
					
					return;					
				}
			}
	}


}
