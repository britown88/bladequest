package bladequest.graphics;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.Rect;
import bladequest.world.Global;



public class Sprite {
	private Bitmap bitmap;
	private String bmpName;
	private int width;
	private int height;
	private String face;
	
	public String name;
	
	private Map<String, Vector<Rect>> frameLists;
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getNumFrames() { return frameLists.get(face).size(); }
	
	public Sprite(String name, String bitmap, int width, int height)
	{
		this.name = name;
		bmpName = bitmap;
		this.bitmap = Global.bitmaps.get(bitmap);
		this.width = width;
		this.height = height;
		
		frameLists = new HashMap<String, Vector<Rect>>();	
	}
	
	public Sprite(Sprite spr)
	{
		if(spr != null)
		{
			name = spr.name;
			bmpName = spr.bmpName;
			bitmap = spr.bitmap;
			width = spr.width;
			height = spr.height;
			face = spr.face;		
			frameLists = spr.frameLists;
		}
		else
		{
			bmpName = "";
			bitmap = null;
			width = 0;
			height = 0;
			frameLists = new HashMap<String, Vector<Rect>>();	
		}		
	}
	
	public void changeFace(String face){this.face = face;}
	public String getFace() { return face; }
	
	
	public void render(int x, int y, int index)
	{			
		Global.renderer.drawBitmap(bitmap, frameLists.get(face).get(index), 
					new Rect(
							Global.worldToScreenX(x), 
							Global.worldToScreenY(y), 
							Global.worldToScreenX(x)+width, 
							Global.worldToScreenY(y)+height), 
					null);
	}
	
	public void renderFromVP(int x, int y, int index)
	{			
		Global.renderer.drawBitmap(bitmap, frameLists.get(face).get(index), 
					new Rect(
							Global.vpToScreenX(x), 
							Global.vpToScreenY(y), 
							Global.vpToScreenX(x)+width, 
							Global.vpToScreenY(y)+height), 
					null);
	}
	
	public void addFrame(String face, int left, int top, int right, int bottom)
	{
		if(!frameLists.containsKey(face))
			frameLists.put(face, new Vector<Rect>());
		
		frameLists.get(face).add(new Rect(left, top,right, bottom));
	}
	
	
	public void addFrame(String face, int size, int x, int y)
	{
		if(!frameLists.containsKey(face))
			frameLists.put(face, new Vector<Rect>());
		
		frameLists.get(face).add(new Rect(x*size, y*size, x*size+size, y*size+size));
	}

	
}
