package bladequest.UI;

import android.graphics.*;
import android.graphics.Paint.Align;
import bladequest.world.Global;

import java.util.*;

//List Boxes can be given a list of items that can be displayed and scrolled through
//When an object is selected, the selected Object is saved and can be retrieved 
//by the parent object
public class ListBox extends MenuPanel
{
	private int numRows, numColumns, rowHeight, columnWidth, startY, startX, itemBuffer, scrollDelta, selectedIndex;
	//private List<String> itemNames;
	//private List<Object> items;
	//private List<Integer> disabledIndices;
	
	private List<ListBoxEntry> entries;
	private ListBoxEntry selectedItem, lastItemSelected;
	private boolean scrolling;	
	private Paint textPaint, disabledTextPaint;
	
	public boolean thickOptSelect, drawAllFrames;	
	
	
	public ListBox(Rect frameRect, int rows, int columns, Paint textPaint)
	{
		super(frameRect.left, frameRect.top, frameRect.width(), frameRect.height());
		this.textPaint = textPaint;
		
		this.numRows = rows;
		this.numColumns = columns;
		rowHeight = height / numRows;
		columnWidth = width / numColumns;
		
		entries = new ArrayList<ListBoxEntry>();
		clearObjects();
	}
	
	public ListBox(int x, int y, int width, int height, int rows, int columns, Paint textPaint)
	{
		super(x, y, width, height);
		this.textPaint = textPaint;
		
		this.numRows = rows;
		this.numColumns = columns;
		rowHeight = height / numRows;
		columnWidth = width / numColumns;
		
		entries = new ArrayList<ListBoxEntry>();
		clearObjects();
	}
	
	@Override
	public void setOpenSize(int width, int height)
	{
		super.setOpenSize(width, height);
		
		rowHeight = height / numRows;
		columnWidth = width / numColumns;	
		
		for(ListBoxEntry lbi : entries)
		{
			lbi.width = columnWidth;
			lbi.height = rowHeight;
			
			if(textPaint.getTextAlign() == Align.CENTER)
				lbi.getTextAt(0).x = columnWidth / 2;
		}
		
	}
	
	//returns the created item
	public ListBoxEntry addItem(String str, Object o, boolean disabled)
	{
		ListBoxEntry e = new ListBoxEntry(0, 0, columnWidth, rowHeight, str, o, textPaint);
		if(disabled) e.disable(disabledTextPaint);
		
		entries.add(e);
		
		return e;
		
	}
	
	public void clearObjects()
	{
		scrollDelta = 0;
		selectedItem = null;
		itemBuffer = 0;
		//showOptSelect = false;
		scrolling = false;
		entries.clear();
	}
	
	@Override
	public void clear()
	{
		super.clear();
		entries.clear();
		
	}
	
	public void drawOutlines()
	{
		for(ListBoxEntry lbi : entries)
			lbi.drawOutline = true;
	}
	
	public int getColumnWidth() { return columnWidth; }
	public int getRowHeight() { return rowHeight; }
	
	public boolean isScrolling() { return scrolling; }
	
	public ListBoxEntry getSelectedEntry() { return lastItemSelected; }
	public void clearSelectedEntry() { lastItemSelected = null; }
	public List<ListBoxEntry> getEntries() { return entries; }
	public ListBoxEntry getEntryAt(int index) { return entries.get(index);}
	public void setDisabledPaint(Paint p) { disabledTextPaint = p; }
	
	public void changeItemText(int index, String text)
	{
		if(index <= entries.size())
			entries.get(index).getTextAt(0).text = text;
	}
	
	@Override
	public void open()
	{
		super.open();
		selectedItem = null;
	}
	
	@Override	
	public void update()
	{
		super.update();
		
		int i = Math.max(0, itemBuffer);		
		ListBoxEntry entry;		
		
		for(int y = 0; y < numRows; ++y)
			for(int x = 0; x < numColumns; ++x)
			{
				if(i >= entries.size())
					break;
				entry = entries.get(i);		
				entry.pos.x = Global.screenToVPX(frameRect.left) + columnWidth * x;
				entry.pos.y = Global.screenToVPY(frameRect.top) + rowHeight * y + scrollDelta;
				entry.stopMoving();
				entry.update();
				
				i++;				
			}	
		
		for(ListBoxEntry lbi : entries)
			lbi.update();
		
		//autoscrolls to a neutral position			
		if(!scrolling && scrollDelta > 0)
			scrollDelta = Math.max(0, scrollDelta - 3);
		
		if(!scrolling && scrollDelta < 0)
			scrollDelta = Math.min(0, scrollDelta + 3);	
	}
	
	@Override	
	public void render()
	{	
		renderFrame();
		
		if(drawContent)
		{
			//disable drawing for the first row if scrolling
			//would display that above row 0
			boolean draw = scrollDelta >= 0;
			int i = Math.max(0, itemBuffer);
			
			//decide if an entry is selected
			for(ListBoxEntry lbe : entries)
			{
				lbe.drawFrame = drawAllFrames;	
				lbe.invertColors(false);
			}
						
			if(selectedItem != null && selectedIndex < entries.size())	
			{
				entries.get(selectedIndex).drawFrame = true;	
				if(drawAllFrames)
					entries.get(selectedIndex).invertColors(true);
			}
						
			
			for(int y = 0; y < numRows; ++y)
				for(int x = 0; x < numColumns; ++x)
				{
					if(i >= entries.size())
						break;
					
					if(draw)
						entries.get(i).render();

					
					//first row's been drawn, draw the rest
					if(i == itemBuffer + numColumns - 1)
						draw = true;
					
					i++;				
				}		
		}	
		
		renderDarken();
	}
	
	private boolean mouseDown;
	
	public void touchActionDown(int x, int y)
	{
		selectedIndex = getSelectedIndex(x,y);
		
		if(frameRect.contains(x, y))
		{
			if(selectedIndex < entries.size())
				selectedItem = entries.get(selectedIndex);
			
			mouseDown = true;
		}
		
		startY = y;
		startX = x;
		scrolling = false;
	}
	
	//returns whether the box stays open, closes, or if an item was selected
	public LBStates touchActionUp(int x, int y)
	{
		//if nothing happens, box will stay open
		LBStates returnState = LBStates.Open;
		
		//do nothing if we're scrolling		
		
		if(!scrolling)
			returnState = selectedItem != null ? LBStates.Selected : LBStates.Close;

		scrolling = false;
		mouseDown = false;
		
		lastItemSelected = selectedItem;
		selectedItem = null;
		
		return returnState;
	}
	
	public void touchActionMove(int x, int y)
	{
		if(mouseDown)
		{
			if(!scrolling)
			{
				//dragged out of starting row
				if( ((y > startY && itemBuffer >= numColumns) || 
						(y < startY && itemBuffer + (numColumns*numRows) < entries.size())) && 
						(int)(y / numRows) != (int)(startY / numRows))
				{
					//start scrolling
					scrolling = true;
					selectedItem = null;
				}
				else	
					//dragged to left or right
					if(frameRect.contains(x, y))
					{
						//reevaluate which item is selected
						selectedIndex = getSelectedIndex(x,y);
						if(selectedIndex < entries.size())
							selectedItem = entries.get(selectedIndex);							
						else
							selectedItem = null;
					}			
			}
			else
			{
				selectedItem = null;
				
				if( (y > startY && itemBuffer >= numColumns) || 
						(y < startY && itemBuffer + (numColumns*numRows) < entries.size()))
				{
					
					scrollDelta = y - startY;
					if(scrollDelta < -(frameRect.height() / numRows))
					{					
						itemBuffer += numColumns;
						update();
						startY -= (frameRect.height() / numRows);
						scrollDelta = 0;
					}
					
					if(scrollDelta > (frameRect.height() / numRows))
					{
						itemBuffer -= numColumns;
						update();
						startY += (frameRect.height() / numRows);
						scrollDelta = 0;
					}
					
				}
			}
		}
		
	}
	
	//pass mouse coordinates
	//return items index
	private int getSelectedIndex(int x, int y)
	{
		int row = Math.max(0, y - frameRect.top)/rowHeight;
		int column = Math.max(0, x - frameRect.left)/columnWidth;
		
		//selectPos = new Point(column, row);		
		return numColumns * row + column + itemBuffer;
	}
	
	public enum LBStates
	{
		Open,
		Close,
		Selected
	}


}
