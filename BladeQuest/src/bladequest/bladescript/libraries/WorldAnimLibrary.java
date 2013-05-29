package bladequest.bladescript.libraries;

import java.util.ArrayList;
import java.util.List;

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
	
	public static AnimationBuilder genWindowShatter(int value)
	{
		List<Point> pList = new ArrayList<Point>();
		
		for (int i = 0; i < 40; ++i)
		{
		
			pList.add(new Point(Global.rand.nextInt(600), Global.rand.nextInt(400)));
		}
		
//		pList.add(new Point(0, 0));
//		pList.add(new Point(200, 0));
//		pList.add(new Point(0, 200));
//		pList.add(new Point(200, 200));		
		
		return WorldAnimations.buildShatter(new Point(300, 100), pList, 4, 30.0f);
	}

}
