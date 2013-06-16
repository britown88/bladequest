package bladequest.actions;

import android.util.Log;
import bladequest.bladescript.ParserException;
import bladequest.bladescript.ScriptVar;

public class actScript extends Action 
{
	private ScriptVar func;
	
	public actScript(ScriptVar func)
	{
		this.func = func;
	}
	
	@Override
	public void run()
	{
		try {
			func.apply(ScriptVar.toScriptVar(0));
		} catch (ParserException e) {
			e.printStackTrace();
			Log.d("Parser", e.what());
		}		
		
	}
	
	@Override
	public boolean isDone()
	{
		return true;
	}

}
