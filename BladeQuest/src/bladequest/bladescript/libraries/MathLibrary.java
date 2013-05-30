package bladequest.bladescript.libraries;

import android.graphics.Point;
import bladequest.bladescript.LibraryWriter;

public class MathLibrary 
{
	public static void publishLibrary(LibraryWriter library) 
	{
		try {
			library.addAllIn(MathLibrary.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int add(int x, int y)
	{
		return x + y;
	}
	
	public static Point point(int x, int y)
	{
		return new Point(x, y);
	}

}
