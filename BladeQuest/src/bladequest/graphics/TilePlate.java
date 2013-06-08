package bladequest.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import bladequest.world.GameObject;
import bladequest.world.Global;

public class TilePlate 
{
	final int layerCount = 4;
	
	private Lock lock;
	private boolean loaded,loading, empty, unloadAfterLoadFlag/*, foreground*/;
	private List<List<Tile>> tiles;
	private List<GameObject> objects;
	private Bitmap tileset;
	private Point platePos;
	private Paint p;
	
	private TilePlateBitmap bmp, animBmp;
	
	private boolean animated;

	
	public TilePlate(Bitmap tileset, int x, int y, boolean foreground)
	{
	    lock = new ReentrantLock();
		tiles = new ArrayList<List<Tile>>();
		for (int i = 0; i < layerCount; ++i)
		{
			tiles.add(new ArrayList<Tile>());
		}
		objects = new ArrayList<GameObject>();
		this.tileset = tileset;
		loaded = false;
		loading = false;
		platePos = new Point(x, y);
		unloadAfterLoadFlag = false;
		//this.foreground = foreground;
	}
	//in vp grid tiles
	public Rect getRect()
	{
		return new Rect(platePos.x*Global.tilePlateSize.x, 
						platePos.y*Global.tilePlateSize.y, 
						platePos.x*Global.tilePlateSize.x+ Global.tilePlateSize.x, 
						platePos.y*Global.tilePlateSize.y* Global.tilePlateSize.y);
	}
	
	public boolean tryLoad(Paint p )
	{
		if (loading) return false;
		if (loaded) return false;
		if(tiles.size() == 0) return false;
		loading = true;
		this.p = p;
		return true;
	}
	
	public void render()
	{
		if(!loading && loaded && !empty)
		{
			Global.renderer.drawBitmap((Global.animateTiles && animated) ? (animBmp != null ? animBmp.bmp : bmp.bmp): bmp.bmp,
				Global.worldToScreenX(platePos.x*32*Global.tilePlateSize.x),
				Global.worldToScreenY(platePos.y*32*Global.tilePlateSize.y), null	);
		}
	}
	
	public void renderObjects()
	{
		for(GameObject go : objects)
			go.render();
	}
	
	public void addTile(Tile t)
	{
		int layer = t.layerNumber;
		if (layer >= layerCount) layer -= layerCount;
		tiles.get(layer).add(t);	
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
		
		for (int i = 0; i < layerCount; ++i)
		{
			for(Tile t : tiles.get(i))
			{
				if(unloadAfterLoadFlag) break;
				t.render(canvas, tileset, false, p);
			}	
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
			
			for (int i = 0; i < layerCount; ++i)
			{
				for(Tile t : tiles.get(i))
				{
					if(unloadAfterLoadFlag) return;
					t.render(canvas, tileset, true, p);
				}	
			}			
		}		
	}
	
	public void Unload()
	{
		lock.lock();
		if(loaded)
		{
			Global.freeTileBitmap(bmp);
			if(animated && animBmp != null)
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
