package bladequest.UI;


import android.graphics.Paint;
import android.graphics.Paint.Align;

public class ListBoxEntry extends MenuPanel
{
	private boolean disabled;
	
	
	public ListBoxEntry(
			int x, int y, int width, int height, 
			String itemName, Object item,
			Paint textPaint)
	{
		super(x, y, width, height);
		this.obj = item;
		
		//create primary textbox
		addTextBox(
				itemName, 
				textPaint.getTextAlign() == Align.CENTER ? width/2 : 6, 
				height / 2, 
				textPaint);
		
		drawFrame = false;
		
	}
	
	public boolean Disabled(){return disabled;}	
	public void disable(Paint disablePaint)
	{
		disabled = true;
		getTextAt(0).textPaint = disablePaint;
	}	
	

}
