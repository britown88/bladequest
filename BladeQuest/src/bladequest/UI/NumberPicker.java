package bladequest.UI;

import bladequest.world.Global;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;

public class NumberPicker extends MenuPanel 
{
	private int value, min, max, inc;
	private Paint txtPaint;
	private MenuPanel upArrow, downArrow, selectedPanel;
	
	private long startTime;
	private final float startingIncDelay = 0.5f;
	private float incDelay;
	
	public NumberPicker(int x, int y, int width, int height)
	{
		super(x, y, width, height);
		
		min = 0;
		max = -1;
		value = min;
		inc = 1;
		
		txtPaint = Global.textFactory.getTextPaint(13, Color.WHITE, Align.CENTER);
		addTextBox(""+value, width / 2, height / 2, txtPaint);
		
		upArrow = new MenuPanel(width * 2 / 3, 0, width / 3, height);
		upArrow.drawFrame = false;
		upArrow.addTextBox(">", upArrow.width / 2, upArrow.height / 2, txtPaint);
		upArrow.obj = 1;		
		
		downArrow = new MenuPanel(0, 0, width / 3, height);
		downArrow.drawFrame = false;
		downArrow.addTextBox("<", downArrow.width / 2, downArrow.height / 2, txtPaint);
		downArrow.obj = -1;
		
		addChildPanel(upArrow);
		addChildPanel(downArrow);
		
	}
	
	public void setMinMax(int min, int max){this.min = min;this.max = max;}	
	public void setValue(int value){this.value = value;}
	public void setIncrement(int inc){this.inc = inc;}
	
	public int getValue() { return value; }
	
	@Override
	public void update()
	{
		super.update();		
		getTextAt(0).text = ""+value;
		
		if(selectedPanel != null)
		{
			float elapsed = (System.currentTimeMillis() - startTime) / 1000.0f;
			if(elapsed >= incDelay)
			{
				startTime = System.currentTimeMillis();
				incDelay = 0;
				value += (Integer)(selectedPanel.obj) * inc;
				
				if(value > max && max != -1) value = max;
				if(value < min) value = min;
			}
		}
	}
	
	private void select(MenuPanel panel)
	{
		if(selectedPanel != null)
			selectedPanel.drawFrame = false;
		
		if(!panel.equals(selectedPanel))
		{
			resetTimer();
			value += (Integer)(panel.obj) * inc;
			if(value > max && max != -1) value = max;
			if(value < min) value = min;
		}			
		
		selectedPanel = panel;
		selectedPanel.drawFrame = true;	
		
	}
	
	private void clearSelect()
	{
		if(selectedPanel != null)
		{
			selectedPanel.drawFrame = false;
			selectedPanel = null;
		}
			
	}
	
	private void resetTimer()
	{
		incDelay = startingIncDelay;
		startTime = System.currentTimeMillis();
	}
	
	public void touchActionMove(int x, int y)
	{
		if(upArrow.contains(x, y))
			select(upArrow);
		else if(downArrow.contains(x, y))
			select(downArrow);
		else clearSelect();		
	}
	public void touchActionUp(int x, int y)
	{
		clearSelect();
	}
	public void touchActionDown(int x, int y)
	{
		if(upArrow.contains(x, y))
			select(upArrow);			
		else if(downArrow.contains(x, y))
			select(downArrow);
		else clearSelect();	
	}


}
