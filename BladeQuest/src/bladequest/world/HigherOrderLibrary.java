package bladequest.world;

import bladequest.scripting.LibraryWriter;
import bladequest.scripting.ScriptVar;
import bladequest.scripting.ScriptVar.BadTypeException;

public class HigherOrderLibrary {

	
	public static ScriptVar iterateBound(ScriptVar bound, ScriptVar fn, int count)
	{
		for (int i = 0; i < count; ++i)
		{
			try {
				fn.apply(ScriptVar.toScriptVar(i));
			} catch (BadTypeException e) {
				e.printStackTrace();
			}
		}
		return bound;
	}
	
	public static ScriptVar mapBound(ScriptVar bound, ScriptVar fn, ScriptVar list)
	{
		while (!list.isEmptyList())
		{
			try {
				fn.apply(bound).apply(list.head());
				list = list.tail();
			} catch (BadTypeException e) {
				e.printStackTrace();
			}			
		}
		return bound;
	}	
	
	public static void publishLibrary(LibraryWriter library) 
	{
		try {
			library.addAllIn(HigherOrderLibrary.class);	
		} catch (Exception e) {
		}
	}
}
