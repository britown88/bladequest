package bladequest.UI;

import bladequest.world.Global;
import android.graphics.*;
import android.graphics.Paint.Align;

public class DropBox extends MenuPanel
{
	public MenuPanel panels[];
	
	private int rowCount, columnCount, rowHeight, columnWidth;
	
	private int selectedIndex, destIndex;
	private Point tapStartPos;
	private Rect panelRects[];
	
	private boolean moved;
	
	public DropBox(int x, int y, int width, int height, int rows, int columns)
	{
		super(x, y, width, height);
		
		this.rowCount = rows;
		this.columnCount = columns;
		
		this.rowHeight = height / rowCount;
		this.columnWidth = width / columnCount;
		
		
		panels = new MenuPanel[rows*columns];		
		panelRects = new Rect[rows*columns];	
		
		for(int _y = 0; _y < rows; ++_y)
			for(int _x = 0; _x < columns; ++_x)
			{
				panels[index(_x, _y)] = new MenuPanel(
						x + columnWidth * _x, y + rowHeight * _y, 
						columnWidth, rowHeight);				
			}
		
		selectedIndex = -1;
	}
	
	public int RowHeight() { return rowHeight; }
	public int ColumnWidth() { return columnWidth; }
	
	private int index(int x, int y)
	{
		return y*columnCount + x;
	}
	
	@Override
	public void render()
	{
		renderFrame();
		
		
		for(int i = 0; i < panels.length; ++i)
		{
			if(selectedIndex > -1)
			{
				if(i != selectedIndex)
					panels[i].render();
			}
			else	
				panels[i].render();
		}
		
		if(selectedIndex > -1)
			panels[selectedIndex].render();
		
		renderDarken();

	}
	
	@Override
	public void update()
	{
		updateFrame();
		for(MenuPanel mp : panels)
			if(mp != null) mp.update();
	}
	
	public void touchActionDown(int x, int y) 
	{
		if(getRect().contains(x, y))
		{
			int i = 0;
			
			//-1 means nothing was selected
			selectedIndex = -1;
			
			for(MenuPanel mp : panels)
			{
				//don't wanna do this while plates are moving
				if(panels[i].isMoving())
					return;
				
				//build list of current rect positions and save which one's been selected
				panelRects[i] = new Rect(mp.getRect());			
				if(panelRects[i].contains(x, y))
					selectedIndex = i;
				++i;
			}
			destIndex = selectedIndex;	
			if(selectedIndex > -1)
			{
				Rect r = panels[selectedIndex].getRect();
				tapStartPos = new Point(x - r.left , y - r.top);
			}
				
		}		
	}
	
	public MenuPanel touchActionUp(int x, int y) 
	{
		//return the selected item, or null if shifting took place
		movePanel(selectedIndex, selectedIndex);	
		MenuPanel mp = (moved || selectedIndex == -1 || panels[selectedIndex].obj == null) ? null : panels[selectedIndex];
		moved = false;
		selectedIndex = -1;

		return mp;
	}
	
	public void touchActionMove(int x, int y) 
	{
		if(selectedIndex > -1)
		{
			panels[selectedIndex].pos.x = Global.screenToVPX(x - tapStartPos.x);
			panels[selectedIndex].pos.y = Global.screenToVPY(y - tapStartPos.y);
			
			int currentIndex = -1;			
			for(int i = 0; i < panels.length; ++i)
				if(panelRects[i].contains(x, y))
				{
					currentIndex = i;
					break;
				}
			
			if(currentIndex != destIndex && currentIndex != -1)
			{
				MenuPanel newPanelList[];
				newPanelList = new MenuPanel[columnCount*rowCount];	
				
				moved = true;
				
				destIndex = currentIndex;
				for(int i = 0; i < panels.length; ++i)
				{
					if(i != selectedIndex)
					{
						if(i > selectedIndex && i <= destIndex)
						{
							movePanel(i, i-1);
							newPanelList[i-1] = panels[i];
						}							
						else if(i < selectedIndex && i >= destIndex)
						{
							movePanel(i, i+1);
							newPanelList[i+1] = panels[i];
						}
						else
						{
							newPanelList[i] = panels[i];
							//newPanelRectList[i] = panelRects[i];
						}
							
					}
				}
				
				//add selected index to new queue and save
				newPanelList[destIndex] = panels[selectedIndex];
				//newPanelRectList[destIndex] = panelRects[selectedIndex];
				selectedIndex = destIndex;
				
				panels = newPanelList;
				//panelRects = newPanelRectList;
			}

		}
	}
	
	private void movePanel(int srcIndex, int destIndex)
	{
		if(srcIndex > -1 && srcIndex < panels.length && 
				destIndex > -1 && destIndex < panels.length	)
		{
			panels[srcIndex].move(
					Global.screenToVPX(panelRects[destIndex].left), 
					Global.screenToVPY(panelRects[destIndex].top), 10);
		}
		
	}
	
	
}
