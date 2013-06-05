package bladequest.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import bladequest.world.GameObject;
import bladequest.world.Global;

public class TilePlate 
{
	private Lock lock;
	private boolean loaded,loading, empty, unloadAfterLoadFlag/*, foreground*/;
	private List<Tile> tiles;
	private List<GameObject> objects;
	private Bitmap tileset;
	private Point platePos;
	
	private TilePlateBitmap bmp, animBmp;
	
	private boolean animated;

	
	public TilePlate(Bitmap tileset, int x, int y, boolean foreground)
	{
	    lock = new ReentrantLock();
		tiles = new ArrayList<Tile>();
		objects = new ArrayList<GameObject>();
		this.tileset = tileset;
		loaded = false;
		loading = false;
		platePos = new Point(x, y);
		unloadAfterLoadFlag = false;
		//this.foreground = foreground;
	}
	
	
	public boolean tryLoad()
	{
		if (loading) return false;
		if (loaded) return false;
		if(tiles.size() == 0) return false;
		loading = true;
		return true;
	}
	
	public void render()
	{
		if(!loading && loaded && !empty)
		{
			Global.renderer.drawBitmap((Global.animateTiles && animated) ? (animBmp != null ? animBmp.bmp : bmp.bmp): bmp.bmp,
				Global.worldToScreenX(platePos.x*320),
				Global.worldToScreenY(platePos.y*320), null	);
		}
	}
	
	public void renderObjects()
	{
		for(GameObject go : objects)
			go.render();
	}
	
	public void addTile(Tile t)
	{
		tiles.add(t);	
		if(t.animated())
			animated = true;
	}
	public void addObject(GameObject go)
	{
		objects.add(go);
	}
	
	public boolean Loaded() { return loaded; }
	
	public void Load() 
	{
		bmp = Global.getFreeTileBitmap();
		
		bmp.bmp.eraseColor(Color.TRANSPARENT);
		empty = tiles.size() == 0;
		Canvas canvas = new Canvas(bmp.bmp);
		
		for(Tile t : tiles)
		{
			if(unloadAfterLoadFlag) break;
			t.render(canvas, tileset, false);
		}
		
		if(animated && !unloadAfterLoadFlag)
		{
			LoadAnimation();
		}
		
		lock.lock();
		loaded = true;	
		loading = false;
		if(unloadAfterLoadFlag)
		{
			unloadAfterLoadFlag = false;
			Unload();
		}
		lock.unlock();
	}
	
	public void LoadAnimation() 
	{
		animBmp = Global.getFreeTileBitmap();
		
		animBmp.bmp.eraseColor(Color.TRANSPARENT);
		empty = tiles.size() == 0;
		
		if(!empty)
		{
			Canvas canvas = new Canvas(animBmp.bmp);		
			for(Tile t : tiles)
			{
				if(unloadAfterLoadFlag) return;
				t.render(canvas, tileset, true);
			}
		}		
	}
	
	public void Unload()
	{
		lock.lock();
		if(loaded)
		{
			Global.freeTileBitmap(bmp);
			if(animated)
			{
				Global.freeTileBitmap(animBmp);
			}
			bmp = animBmp = null;
			loaded = false;
		}
		else if(loading)
			unloadAfterLoadFlag = true;
		lock.unlock();
	}

}
