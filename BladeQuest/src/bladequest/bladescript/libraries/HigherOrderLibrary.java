package bladequest.bladescript.libraries;

import bladequest.bladescript.LibraryWriter;
import bladequest.bladescript.ParserException;
import bladequest.bladescript.ScriptVar;

public class HigherOrderLibrary {

	
	public static ScriptVar iterateBound(ScriptVar bound, ScriptVar fn, int count)
	{
		for (int i = 0; i < count; ++i)
		{
			try {
				fn.apply(ScriptVar.toScriptVar(i));
			} catch (ParserException e) {
				e.printStackTrace();
			}
		}
		return bound;
	}
	
	public static ScriptVar mapBound(ScriptVar bound, ScriptVar fn, ScriptVar list)
	{
		if (!list.isEmptyList())
		{
			try {
				mapBound(bound, fn, list.tail());				
				fn.apply(bound).apply(list.head());
			} catch (ParserException e) {
				e.printStackTrace();
			}			
		}
		return bound;
	}	
	
	public static ScriptVar map(ScriptVar fn, ScriptVar list)
	{
		if (list.isEmptyList()) return new ScriptVar.EmptyList();
		try {
			ScriptVar tail = map(fn, list.tail());
			return new ScriptVar.ListNode(fn.apply(list.head()), tail);
		} catch (ParserException e) {
			e.printStackTrace();
		}			
		return null;
	}	
	
	public static void publishLibrary(LibraryWriter library) 
	{
		try {
			library.addAllIn(HigherOrderLibrary.class);	
		} catch (Exception e) {
		}
	}
}
