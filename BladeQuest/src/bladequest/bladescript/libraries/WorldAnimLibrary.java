package bladequest.bladescript.libraries;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Point;
import bladequest.bladescript.LibraryWriter;
import bladequest.graphics.AnimationBuilder;
import bladequest.world.Global;
import bladequest.world.WorldAnimations;

public class WorldAnimLibrary 
{
	public static void publishLibrary(LibraryWriter library) 
	{
		try {
			library.addAllIn(WorldAnimLibrary.class);
		} catch (Exception e) {
		}
	}
	
	public static AnimationBuilder genGrowingCircle(int a, int r, int g, int b, float time, int radius)
	{
		return WorldAnimations.buildGrowingCircle(Color.argb(a,  r,  g,  b), time, radius);
		
	}
	
	public static AnimationBuilder genCircle(int a, int r, int g, int b, int radius)
	{
		return WorldAnimations.buildCircle(Color.argb(a,  r,  g,  b), radius);
		
	}
	
	public static AnimationBuilder genMoonKun(int i)
	{
		return WorldAnimations.buildSugoiMoon();
		
	}
	
	public static AnimationBuilder genTwinkleShrink(int i)
	{
		return WorldAnimations.buildTwinkleShrink();
		
	}
	
	public static AnimationBuilder genTitleSequence(int i)
	{
		return WorldAnimations.buildTitleSequence();
		
	}
	
	
	public static AnimationBuilder genWindowShatter(int value)
	{
		List<Point> pList = new ArrayList<Point>();
		
		pList.add(new Point(21, 28));
		pList.add(new Point(23, 13));
		pList.add(new Point(30, 13));
		pList.add(new Point(42, 13));
		pList.add(new Point(53, 17));
		pList.add(new Point(59, 28));
		pList.add(new Point(73, 37));
		pList.add(new Point(66, 51));
		pList.add(new Point(66, 64));
		pList.add(new Point(61, 66));
		pList.add(new Point(55, 70));
		pList.add(new Point(46, 72));
		pList.add(new Point(39, 73));
		pList.add(new Point(33, 72));
		pList.add(new Point(22, 66));
		pList.add(new Point(17, 62));
		pList.add(new Point(6, 46));
		pList.add(new Point(37, 25));
		pList.add(new Point(55, 52));
		pList.add(new Point(33, 58));
		
		for(Point p : pList)
		{
			p.x = p.x*2 + 32*4;
			p.y = p.y*2;
		}	
		
		return WorldAnimations.buildShatter(new Point(32*4+80, 80), pList, 4, 30.0f);
	}

}
