package bladequest.graphics;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import bladequest.world.GameObject;
import bladequest.world.Global;

public class TilePlate 
{
	private boolean loaded,loading, empty, unloadAfterLoadFlag/*, foreground*/;
	private List<Tile> tiles;
	private List<GameObject> objects;
	private Bitmap tileset;
	private Point platePos;
	
	private TilePlateBitmap bmp, animBmp;
	
	private boolean animated;

	
	public TilePlate(Bitmap tileset, int x, int y, boolean foreground)
	{
		tiles = new ArrayList<Tile>();
		objects = new ArrayList<GameObject>();
		this.tileset = tileset;
		loaded = false;
		loading = false;
		platePos = new Point(x, y);
		unloadAfterLoadFlag = false;
		//this.foreground = foreground;
	}
	
	public void render(List<TilePlate> loadList)
	{
		if(!loading)
		{
			if(loaded)
			{
				if(!empty)
					Global.renderer.drawBitmap((Global.animateTiles && animated) ? (animBmp != null ? animBmp.bmp : bmp.bmp): bmp.bmp,
						Global.worldToScreenX(platePos.x*320),
						Global.worldToScreenY(platePos.y*320), null	);
			}
			else if(tiles.size() > 0)
			{
				loading = true;
				loadList.add(this);
				
			}	
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
		for(TilePlateBitmap b : Global.tilePlateBmps)
		{
			if(!b.used)
			{
				bmp = b;
				bmp.used = true;
				break;
			}
		}
		
		empty = tiles.size() == 0;
		Canvas canvas = new Canvas(bmp.bmp);
		
		for(Tile t : tiles)
			t.render(canvas, tileset, false);
		
		if(animated)
			LoadAnimation();
		
		loaded = true;	
		loading = false;
		if(unloadAfterLoadFlag)
		{
			unloadAfterLoadFlag = false;
			Unload();
		}
	}
	
	public void LoadAnimation() 
	{
		for(TilePlateBitmap b : Global.tilePlateBmps)
		{
			if(!b.used)
			{
				animBmp = b;
				animBmp.used = true;
				break;
			}
		}
		
		empty = tiles.size() == 0;
		
		if(animBmp != null)
		{
			Canvas canvas = new Canvas(animBmp.bmp);		
			for(Tile t : tiles)
				t.render(canvas, tileset, true);
		}		
	}
	
	public void Unload()
	{
		if(loaded)
		{
			//bmp.recycle();
			bmp.used = false;
			bmp.bmp.eraseColor(Color.TRANSPARENT);
			
			if(animated)
			{
				animBmp.used = false;
				animBmp.bmp.eraseColor(Color.TRANSPARENT);
			}
			loaded = false;
		}
		else if(loading)
			unloadAfterLoadFlag = true;
	}

}
