package bladequest.bladescript.libraries;

import bladequest.bladescript.LibraryWriter;
import bladequest.graphics.BattleSprite;
import bladequest.graphics.Sprite;
import bladequest.world.Global;

public class GraphicsLibrary 
{
	public static void publishLibrary(LibraryWriter library) 
	{
		try {
			library.addAllIn(GraphicsLibrary.class);
		} catch (Exception e) {
		}
	}
	
	public static Sprite createSprite(String name, String bitmap, int width, int height)
	{
		Sprite spr = new Sprite(name, bitmap, width, height);
		Global.sprites.put(name, spr);
		
		return spr;
	}
	
	public static Sprite addSpriteFrame(Sprite spr, String face, int left, int top, int right, int bottom)
	{
		spr.addFrame(face, left, top, right, bottom);
		return spr;		
	}
	
	public static Sprite addSpriteFrame2(Sprite spr, String face, int size, int x, int y)
	{
		spr.addFrame(face, size, x, y);
		return spr;		
	}
	
	public static Sprite setFace(Sprite spr, String face)
	{
		spr.changeFace(face);
		return spr;
	}
	
	public static Sprite createWorldSprite(String bitmap, String name, int x, int y)
	{
		Sprite spr = new Sprite(name, bitmap, 32, 32);		
		
		spr.addFrame("down",16, x*3+1, y*4+0);
		spr.addFrame("down", 16, x*3+2, y*4+0);	
		
		spr.addFrame("up", 16, x*3+1, y*4+1);
		spr.addFrame("up", 16, x*3+2, y*4+1);
		
		spr.addFrame("left", 16, x*3+1, y*4+2);
		spr.addFrame("left", 16, x*3+2, y*4+2);
		
		spr.addFrame("right", 16, x*3+1, y*4+3);
		spr.addFrame("right", 16, x*3+2, y*4+3);
		
		spr.changeFace("down");
		
		Global.sprites.put(name, spr);	
		
		return spr;
	}
	
	public static BattleSprite createEnemySprite(String name, String bitmap, int destSize, int srcSize, int srcX, int srcY)
	{
		BattleSprite bs = new BattleSprite(name, bitmap, destSize, destSize);
		bs.addFrame(BattleSprite.faces.Idle, srcSize, srcX, srcY);
		Global.battleSprites.put(name, bs);
		
		return bs;
	}
	
	public static BattleSprite createBattleSprite(String name, int x, int y)
	{
		BattleSprite bSpr = new BattleSprite(name, "herobattlers", 64, 64);		
		
		bSpr.addFrame(BattleSprite.faces.Idle, 32, x*3+2, y*5+0);
		
		bSpr.addFrame(BattleSprite.faces.Attack, 32, x*3+1, y*5+0);
		bSpr.addFrame(BattleSprite.faces.Attack, 32, x*3+0, y*5+0);
		bSpr.addFrame(BattleSprite.faces.Attack, 32, x*3+0, y*5+0);//3rd frame repeats frame 2
		
		bSpr.addFrame(BattleSprite.faces.Use, 32, x*3+1, y*5+1);
		
		bSpr.addFrame(BattleSprite.faces.Ready, 32, x*3+2, y*5+1);
		
		bSpr.addFrame(BattleSprite.faces.Cast, 32, x*3+0, y*5+2);
		
		bSpr.addFrame(BattleSprite.faces.Casting, 32, x*3+1, y*5+2);
		bSpr.addFrame(BattleSprite.faces.Casting, 32, x*3+2, y*5+2);
		
		bSpr.addFrame(BattleSprite.faces.Victory, 32, x*3+2, y*5+4);
		bSpr.addFrame(BattleSprite.faces.Victory, 32, x*3+1, y*5+4);
		
		bSpr.addFrame(BattleSprite.faces.Dead, 32, x*3+0, y*5+3);
		
		bSpr.addFrame(BattleSprite.faces.Weak, 32, x*3+1, y*5+3);
		
		bSpr.addFrame(BattleSprite.faces.Damaged, 32, x*3+2, y*5+3);
		
		Global.battleSprites.put(name, bSpr);
		
		return bSpr;
	}
	
	public static BattleSprite addBattleFrame(BattleSprite bs, String face, int srcSize, int srcX, int srcY)
	{
		bs.addFrame(BattleSprite.faces.valueOf(face), srcSize, srcX, srcY);
		return bs;
	}
	
	

}
