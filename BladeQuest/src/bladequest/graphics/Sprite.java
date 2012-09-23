package bladequest.graphics;

import android.graphics.Rect;
import android.graphics.Bitmap;
import bladequest.world.Global;

import java.util.Vector;



public class Sprite {
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
		//world states
		Left,
		Right,
		Up,
		Down
	}

	
	public Sprite(String name, String bitmap, int width, int height)
	{
		this.name = name;
		bmpName = bitmap;
		this.bitmap = Global.bitmaps.get(bitmap);
		this.width = width;
		this.height = height;
		face = faces.Down;
		
		createFrameList();	
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
			face = Sprite.faces.Down;		
			
			createFrameList();	
		}
		
	}
	
	private void createFrameList()
	{
		frameLists = new Vector<Vector<Rect>>();
		
		for(int j = 0; j < 4; j++)
			frameLists.add(new Vector<Rect>());
		
	}

	
	public void changeFace(faces face){this.face = face;}
	public faces getFace() { return face; }
	
	
	public void render(int x, int y, int index)
	{			
		Global.renderer.drawBitmap(bitmap, frameLists.get(face.ordinal()).get(index), 
					new Rect(
							Global.worldToScreenX(x), 
							Global.worldToScreenY(y), 
							Global.worldToScreenX(x)+width, 
							Global.worldToScreenY(y)+height), 
					null);
	}
	
	public void renderFromVP(int x, int y, int index)
	{			
		Global.renderer.drawBitmap(bitmap, frameLists.get(face.ordinal()).get(index), 
					new Rect(
							Global.vpToScreenX(x), 
							Global.vpToScreenY(y), 
							Global.vpToScreenX(x)+width, 
							Global.vpToScreenY(y)+height), 
					null);
	}
	
	public void addFrame(faces face, int left, int top, int right, int bottom)
	{
		frameLists.get(face.ordinal()).add(new Rect(left, top,right, bottom));
	}
	
	
	public void addFrame(faces face, int size, int x, int y)
	{

		frameLists.get(face.ordinal()).add(new Rect(x*size, y*size, x*size+size, y*size+size));
	}

	
}
