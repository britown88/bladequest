package bladequest.graphics;

import bladequest.world.Global;
import bladequest.world.Layer;
import android.graphics.*;

public class Tile 
{
	private Point worldPos;
	private Rect src;
	private Layer layer;
	private boolean[] collSides;
	
	private boolean animated;
	private Rect animSrc; 
	
	int layerNumber;
	
	public Tile(int x, int y, int bmpX, int bmpY, Layer layer, int layerNumber)
	{
		this.worldPos = new Point(x, y);
		this.src = new Rect(bmpX*16, bmpY*16, bmpX*16+16, bmpY*16+16);
		this.layer = layer;
		collSides = new boolean[4];
		animated = false;
		this.layerNumber = layerNumber; 
	}
	
	public boolean animated() { return animated; }
	public void animate(int bmpX, int bmpY)
	{
		animated = true;
		this.animSrc = new Rect(bmpX*16, bmpY*16, bmpX*16+16, bmpY*16+16);
	}
	
	public Point WorldPos() { return worldPos; }
	public Layer Layer() { return layer; }
	public void setCollision(boolean left, boolean top, boolean right, boolean bottom)
	{
		collSides[0] = left;
		collSides[1] = top;
		collSides[2] = right;
		collSides[3] = bottom;		
	}
	public boolean[] getCollision(){return collSides;}
	public boolean hasCollision() 
	{
		if (layerNumber != 0) return false;
		if (layer != Layer.Under) return false;
		return collSides[0] || collSides[1] || collSides[2] || collSides[3]; 
	}
	
	public void render(Canvas canvas, Bitmap bitmap, boolean animated, Paint p)
	{		
		Rect dest = new Rect(
				(worldPos.x%Global.tilePlateSize.x)*16,
				(worldPos.y%Global.tilePlateSize.y)*16,
				(worldPos.x%Global.tilePlateSize.x)*16 + 16,
				(worldPos.y%Global.tilePlateSize.y)*16 + 16);
		
		if(bitmap != null && !bitmap.isRecycled())		
			canvas.drawBitmap(bitmap, (animated && this.animated) ? animSrc : src, dest, p);	
		
	}
	
	public void renderToWorld(int x, int y, Bitmap bitmap)
	{
		Rect dest = new Rect(
				Global.worldToScreenX(x),
				Global.worldToScreenY(y),
				Global.worldToScreenX(x+32),
				Global.worldToScreenY(y+32));
		Global.renderer.drawBitmap(bitmap, src, dest, null);
	}
	
	public boolean isBlocked(Tile t)
	{
		if(t.worldPos.x < worldPos.x)
		{
			return collSides[0] || t.collSides[2];
		}		
		else if(t.worldPos.x > worldPos.x)
		{
			return collSides[2] || t.collSides[0];
		}		
		else if(t.worldPos.y < worldPos.y)
		{
			return collSides[1] || t.collSides[3];
		}		
		else if(t.worldPos.y > worldPos.y)
		{
			return collSides[3] || t.collSides[1];
		}
		else
			return false;

	}
	
	public boolean allowEntryFrom(Point origin)
	{
		if(origin.x < worldPos.x && collSides[0])
			return false;
		
		if(origin.x > worldPos.x && collSides[2])
			return false;
		
		if(origin.y < worldPos.y && collSides[1])
			return false;
		
		if(origin.y > worldPos.y && collSides[3])
			return false;
		
		
		return true;
	}
	
	

}
