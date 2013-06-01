package bladequest.graphics;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import bladequest.world.Global;


public class BattleSprite {
	private Bitmap bitmap;
	private String bmpName;
	private int width;
	private int height;
	private faces face;
	private boolean mirrored;
	
	private float rotation = 0.0f;
	
	public String name;
	
	private List<List<Rect>> frameLists;
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getNumFrames() { return frameLists.get(face.ordinal()).size(); }
	public String getBmpName() { return bmpName; }
	public boolean getMirrored() {return mirrored; }
	
	public enum faces
	{
		Idle,		
		Ready,
		Use,
		Attack,
		Casting,
		Cast,		
		Victory,
		Damaged,
		Weak,
		Dead
	}
		
	public BattleSprite(String name, String bitmap, int width, int height)
	{
		this.name = name;
		bmpName = bitmap;
		this.bitmap = Global.bitmaps.get(bitmap);
		this.width = width;
		this.height = height;
		face = faces.Idle;
		
		createFrameList();	
	}
	
	public BattleSprite(BattleSprite spr)
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
			face = faces.Idle;		
			
			createFrameList();	
		}
		
	}
	
	private void createFrameList()
	{
		frameLists = new ArrayList<List<Rect>>();
		
		for(int j = 0; j < 10; j++)
			frameLists.add(new ArrayList<Rect>());
		
	}
	
	public BattleSprite.faces getFace()
	{
		return face;
	}
	
	public void changeFace(faces face)
	{
		this.face = face;
	}
	
	public void setMirrored(boolean isMirrored)
	{
		this.mirrored = isMirrored;
	}
	
	public void setRotation(float rotation)
	{
		this.rotation = rotation;
	}
	
	public void render(int x, int y, int index, boolean center)
	{	
		Point p;
		
		if(center)
			p = Global.vpToScreen(new Point(x-width/2, y-height/2));
		else	
			p = Global.vpToScreen(new Point(x, y));		
		if (frameLists.size() <= face.ordinal() || frameLists.get(face.ordinal()).size() <= index)
			Log.d("BATTLE RENDER", "Rendering index " + index + " of face " + face.name());
		else
		{
			if (mirrored)
			{
				Global.renderer.drawMirroredBitmap(bitmap, rotation, frameLists.get(face.ordinal()).get(index), 
						new Rect(p.x, p.y,p.x+width, p.y+height), null);				
			}
			else
			{
				Global.renderer.drawBitmap(bitmap, rotation, frameLists.get(face.ordinal()).get(index), 
						new Rect(p.x, p.y,p.x+width, p.y+height), null);				
			}
		}
		
	}	
	
	public Rect getFrameRect(faces face, int index)
	{
		return frameLists.get(face.ordinal()).get(index);
	}
	
	public void addFrame(faces face, int left, int top, int right, int bottom)
	{
		frameLists.get(face.ordinal()).add(new Rect(left, top,right, bottom));
	}
	
	public void addFrame(faces face, int size, int x, int y)
	{

		frameLists.get(face.ordinal()).add(new Rect(x*size, y*size,x*size+size, y*size+size));
	}
}
