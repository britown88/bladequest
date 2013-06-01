package bladequest.bladescript.libraries;

import android.util.Log;
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
	
	public static ScriptVar iterateBetween(ScriptVar fn, int begin, int end)
	{
		int step = 1;
		if(end < begin)
			step = -1;
		for (int i = begin; i != end; i += step)
		{			
			try {
				fn.apply(ScriptVar.toScriptVar(i));
			} catch (ParserException e) {
				e.printStackTrace();
				Log.d("Parser", e.what());
			}
		}
		return ScriptVar.toScriptVar(0);
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
