package bladequest.graphics;

import java.util.Vector;

import bladequest.world.Global;

import android.graphics.*;
import android.util.*;


public class BattleSprite {
	private Bitmap bitmap;
	private String bmpName;
	private int width;
	private int height;
	private faces face;
	
	public String name;
	
	private Vector<Vector<Rect>> frameLists;
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getNumFrames() { return frameLists.get(face.ordinal()).size(); }
	
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
		frameLists = new Vector<Vector<Rect>>();
		
		for(int j = 0; j < 10; j++)
			frameLists.add(new Vector<Rect>());
		
	}
	
	public BattleSprite.faces getFace()
	{
		return face;
	}
	
	public void changeFace(faces face)
	{
		this.face = face;
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
			Global.renderer.drawBitmap(bitmap, frameLists.get(face.ordinal()).get(index), 
						new Rect(p.x, p.y,p.x+width, p.y+height), null);
		}
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
