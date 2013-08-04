package bladequest.UI;

import android.graphics.Rect;

public class DropBoxRect 
{
	private Rect rect;
	private MenuPanel panel;
	private int index;
	
	private boolean locked;
	
	public DropBoxRect(Rect r, int index)
	{
		this.rect = r;
		this.index = index;
	}

	public Rect getRect() {return rect;}
	public MenuPanel getPanel() {return panel;}
	public void setPanel(MenuPanel panel) {this.panel = panel;}
	
	public boolean isLocked(){return locked;}
	public void setLocked(boolean locked){this.locked = locked;}
	
	public int index(){return index;}
	
	

}
