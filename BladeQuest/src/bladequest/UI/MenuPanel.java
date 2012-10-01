package bladequest.UI;

import bladequest.world.Global;
import bladequest.graphics.Icon;
import android.graphics.*;
import android.graphics.Paint.Style;

import java.util.*;


//Map Panel uses VP Coordinates
public class MenuPanel 
{
	public boolean drawFrame, drawOutline, thickFrame, drawContent, darken;	
	public int width, height;		
	public Point pos, closedSize, openSize;
	public Anchors anchor;
	
	private Paint darkenPaint;
	private int darkenAlpha = 150;
	private int darkenColor = Color.DKGRAY;
	
	private Point inset;	
	protected Rect frameRect;
	
	protected List<TextBox> textBoxes;
	protected List<PicBox> picBoxes;
	protected List<MenuPanel> children;
	private MenuPanel parent;
	
	//for moving around
	private Point dest, src;
	private int moveSpeed;
	private boolean moving;
	
	//for opening and closing	
	private boolean opens; //true if the frame can open
	private boolean opening;
	private boolean opened;
	protected boolean closed;
	public int openSpeed = 7;
	
	public Object obj;
	
	private boolean hasCustomColor;
	private boolean invertColors;
	private int customColor1, customColor2;
	
	public MenuPanel()
	{
		inset = new Point();
		frameRect = new Rect();
		
		thickFrame = true;
		drawFrame = true;
		drawOutline = false;
		drawContent = true;
		
		textBoxes = new ArrayList<TextBox>();
		picBoxes = new ArrayList<PicBox>();
		children = new ArrayList<MenuPanel>();
		
		pos = new Point(0,0);
		closedSize = new Point(0,0);
		openSize = new Point(0,0);
		anchor = Anchors.TopLeft;
		closed = true;
		opens = false;
		
		darkenPaint = new Paint(darkenColor);
		darkenPaint.setAlpha(darkenAlpha);
		darkenPaint.setStyle(Style.FILL);
		
	}
	
	public MenuPanel(int x, int y, int width, int height)
	{
		anchor = Anchors.TopLeft;
		closed = true;
		opens = false;
		
		pos = new Point(x,y);
		this.width = width;
		this.height = height;
		closedSize = new Point(width,height);
		openSize = new Point(width,height);
		
		inset = new Point();
		frameRect = new Rect();
		
		thickFrame = true;
		drawFrame = true;
		drawContent = true;
		drawOutline = false;
		
		textBoxes = new ArrayList<TextBox>();
		picBoxes = new ArrayList<PicBox>();
		children = new ArrayList<MenuPanel>();
		
		darkenPaint = new Paint(darkenColor);
		darkenPaint.setAlpha(darkenAlpha);
		darkenPaint.setStyle(Style.FILL);
	}
	
	public void move(int x, int y, int speed)
	{
		moving = true;
		src = new Point(this.pos);
		dest = new Point(x, y);
		moveSpeed = speed;
	}
	
	public void stopMoving()
	{
		moving = false;
	}
	
	public boolean Opened() { return opened; }
	public boolean Opening() { return opening; }
	public boolean Closed() { return closed; }
	
	public void clear() 
	{
		textBoxes.clear();
		picBoxes.clear();
		children.clear();
	}
	
	//set insets, pass percentages in float (0.0f-100.0f) or pixel counts in int
	public void setInset(float x, float y){inset.x = (int)((float)width*(x/100.0f)); inset.y = (int)((float)height*(y/100.0f));}
	public void setInset(int x, int y){inset.x = x; inset.y = y;}	
	public void setOpenSize(int width, int height) { opens = true; openSize.x = width; openSize.y = height;}
	public void setClosedSize(int width, int height) { closedSize.x = width; closedSize.y = height;}
	
	public Rect getRect() { updateFrame(); return frameRect; }
	public boolean contains(int x, int y) { return getRect().contains(x, y); }
	public void addTextBox(String text, int x, int y, Paint textPaint){ textBoxes.add(new TextBox(text, x, y, textPaint));}
	
	public void invertColors(boolean b) { invertColors = b;}
	
	public void addPicBox(Bitmap bmp, Rect src, Rect dest){ picBoxes.add(new PicBox(bmp, src, dest));}	
	public void addPicBox(Icon ico){ picBoxes.add(new PicBox(ico));}
	
	public void addChildPanel(MenuPanel child){ child.parent = this; children.add(child);}	
	
	
	public TextBox getTextAt(int index) { return textBoxes.get(index); } 	
	public void hide() { drawFrame = false; drawContent = false;}
	public void show() { drawFrame = true; drawContent = true;}
	
	public boolean isMoving() { return moving;}
	public boolean isShown() { return drawFrame && drawContent;}
	
	
	public int insetWidth() { return inset.x > 0 ? width + (2*inset.x) : width - (2*inset.x); }
	public int insetHeight() { return inset.y > 0 ? height + (2*inset.y) : height - (2*inset.y); }
	
	private void handleFrameOpenClose()
	{
		if(opening)
		{
			height += openSpeed;
			if(height >= openSize.y)
				height = openSize.y;

			width += openSpeed;
			if(width >= openSize.x)
				width = openSize.x;
			
			if(height >= openSize.y && width >= openSize.x)
			{
				//if(!opened)
				//mainMenu.showOptSelect = false;	
				opened = true;
				drawContent = true;
			}
		}
		else
		{			
			height -= openSpeed;
			if(height <= closedSize.y)
				height = closedSize.y;

			width -= openSpeed;
			if(width <= closedSize.x)
				width = closedSize.x;
			
			if(height <= closedSize.y && width <= closedSize.x)
			{
				drawContent = true;
				closed = true;
			}
				

		}
	}
	
	public void open()
	{
		closed = false;
		opening = true;
		drawContent = false;
	}
	
	public void close()
	{
		opened = false;
		opening = false;
		drawContent = false;
	}
	
	public void setOpened()
	{
		opening = false;
		opened = true;
		width = openSize.x;
		height = openSize.y;
		drawContent = true;
	}
	
	public void setClosed()
	{
		opening = false;
		opened = false;
		closed = true;
		width = closedSize.x;
		height = closedSize.y;
		//drawContent = true;
	}
	
	public void setCustomColor(int c1, int c2)
	{
		hasCustomColor = true;
		customColor1 = c1;
		customColor2 = c2;
	}
	
	protected Point getTopLeft()
	{
		switch(anchor)
		{
		case TopLeft:
			return new Point(pos.x, pos.y);
		case TopRight:
			return new Point(pos.x-width, pos.y);
		case BottomLeft:
			return new Point(pos.x, pos.y-height);
		case BottomRight:
			return new Point(pos.x-width, pos.y-height);
		case TrueCenter:
			return new Point(pos.x-width/2, pos.y-height/2);
		case TopCenter:
			return new Point(pos.x-width/2, pos.y);
		case RightCenter:
			return new Point(pos.x-width, pos.y-height/2);
		case LeftCenter:
			return new Point(pos.x, pos.y-height/2);
		case BottomCenter:
			return new Point(pos.x-width/2, pos.y-height);
		}
		
		return null;
		
	}
	
	protected void updateFrame() 
	{
		//adjust position if inset makes panel appear offscreen
		if(pos.y + inset.y < 0) pos.y = Math.abs(inset.y);
		
		switch(anchor)
		{
		case TopLeft:
			frameRect = Global.vpToScreen(new Rect(pos.x, pos.y, pos.x+width, pos.y+height));
			break;
		case TopRight:
			frameRect = Global.vpToScreen(new Rect(pos.x-width, pos.y, pos.x, pos.y+height));
			break;
		case BottomLeft:
			frameRect = Global.vpToScreen(new Rect(pos.x, pos.y-height, pos.x+width, pos.y));
			break;
		case BottomRight:
			frameRect = Global.vpToScreen(new Rect(pos.x-width, pos.y-height, pos.x, pos.y));
			break;
			
		case TrueCenter:
			frameRect = Global.vpToScreen(new Rect(pos.x-width/2, pos.y-height/2, pos.x+width/2, pos.y+height/2));
			break;
		case TopCenter:
			frameRect = Global.vpToScreen(new Rect(pos.x-width/2, pos.y, pos.x+width/2, pos.y + height));
			break;
		case RightCenter:
			frameRect = Global.vpToScreen(new Rect(pos.x-width, pos.y-height/2, pos.x, pos.y+height/2));
			break;
		case LeftCenter:
			frameRect = Global.vpToScreen(new Rect(pos.x, pos.y-height/2, pos.x+width, pos.y+height/2));
			break;
		case BottomCenter:
			frameRect = Global.vpToScreen(new Rect(pos.x-width/2, pos.y-height, pos.x+width/2, pos.y));
			break;
		}
		
		if(parent != null)
		{
			Point parentPos = parent.getTopLeft();
			frameRect.offset(parentPos.x, parentPos.y);
		}
			
		
		
		frameRect.inset(inset.x, inset.y);
	}
	
	public void update()
	{
		if(moving)
		{
			if(pos.x < dest.x) pos.x += moveSpeed;
			else if(pos.x > dest.x) pos.x -= moveSpeed;
			if(pos.y < dest.y) pos.y += moveSpeed;
			else if(pos.y > dest.y) pos.y -= moveSpeed;
			
			if( pos.x < dest.x && src.x > dest.x) pos.x = dest.x;
			if( pos.x > dest.x && src.x < dest.x) pos.x = dest.x;
			if( pos.y < dest.y && src.y > dest.y) pos.y = dest.y;
			if( pos.y > dest.y && src.y < dest.y) pos.y = dest.y;
			
			if(pos.x == dest.x && pos.y == dest.y)
				moving = false;
		}		
		
		if(opens)
			handleFrameOpenClose();
		
		updateFrame();
		for(MenuPanel child : children)
			child.update();

		
	}	
	
	protected void renderFrame() 
	{
		if(drawFrame)
		{
			if(hasCustomColor)
				if(invertColors)
					Global.drawFrame(frameRect,customColor2, customColor1, thickFrame);
				else
					Global.drawFrame(frameRect,customColor1, customColor2, thickFrame);
			else
				Global.drawFrame(frameRect, thickFrame);
		}
			
			
		//Global.renderer.drawRect(frameRect, darkenPaint, true);
	}
	
	protected void renderDarken() 
	{
		if(drawFrame && darken)
		{
			Global.renderer.drawRect(frameRect, darkenPaint, true);
			Global.drawFrameBorder(frameRect, thickFrame);
		}
	}	
	
	public void render()
	{
		renderFrame();
		
		if(drawOutline)
			Global.drawFrameBorder(frameRect, thickFrame);
		
		if(drawContent)
		{
			for(PicBox p : picBoxes)
				p.render(frameRect.left, frameRect.top);
			for(TextBox t : textBoxes)
				t.render(frameRect.left, frameRect.top);
			for(MenuPanel child : children)
				child.render();
		}
		
		renderDarken();			
	}

	
	public enum Anchors
	{
		TopLeft,
		TopRight,
		BottomLeft,
		BottomRight,
		TrueCenter,
		TopCenter,
		RightCenter,
		LeftCenter,
		BottomCenter
	}
	

}
