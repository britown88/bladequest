package bladequest.bladescript.libraries;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import bladequest.bladescript.LibraryWriter;
import bladequest.graphics.AnimationBuilder;
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
	
	public static AnimationBuilder genWindowShatter(int value)
	{
		List<Point> pList = new ArrayList<Point>();
		
		pList.add(new Point(0, 0));
		pList.add(new Point(200, 0));
		pList.add(new Point(0, 200));
		pList.add(new Point(200, 200));		
		
		return WorldAnimations.buildShatter(new Point(0, 0), pList, 5, 5.0f);
	}

}
