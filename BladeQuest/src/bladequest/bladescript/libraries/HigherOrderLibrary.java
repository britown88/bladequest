package bladequest.bladescript.libraries;

import bladequest.bladescript.LibraryWriter;
import bladequest.bladescript.ScriptVar;
import bladequest.bladescript.ScriptVar.BadTypeException;

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
	
	public static ScriptVar map(ScriptVar fn, ScriptVar list)
	{
		ScriptVar out = new ScriptVar.EmptyList();
		while (!list.isEmptyList())
		{
			try {
				out = new ScriptVar.ListNode(fn.apply(list.head()), out);
				list = list.tail();
			} catch (BadTypeException e) {
				e.printStackTrace();
			}			
		}
		return out;
	}	
	
	public static void publishLibrary(LibraryWriter library) 
	{
		try {
			library.addAllIn(HigherOrderLibrary.class);	
		} catch (Exception e) {
		}
	}
}
