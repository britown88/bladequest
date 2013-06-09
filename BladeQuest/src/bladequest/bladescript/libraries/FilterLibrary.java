package bladequest.bladescript.libraries;

import bladequest.bladescript.LibraryWriter;
import bladequest.graphics.ScreenFilter;

public class FilterLibrary 
{
	public static void publishLibrary(LibraryWriter library) 
	{
		try {
			library.addAllIn(FilterLibrary.class);
		} catch (Exception e) {
		}
	}
	
	public static float[] darknessFilter(float t)
	{
		return ScreenFilter.darknessFilter(t);
	}

}
