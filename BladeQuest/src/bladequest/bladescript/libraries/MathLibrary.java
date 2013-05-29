package bladequest.bladescript.libraries;

import bladequest.bladescript.LibraryWriter;

public class MathLibrary 
{
	public static void publishLibrary(LibraryWriter library) 
	{
		try {
			library.addAllIn(MathLibrary.class);
		} catch (Exception e) {
		}
	}
	
	public static int add(int x, int y)
	{
		return x + y;
	}

}
