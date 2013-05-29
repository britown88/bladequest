package bladequest.bladescript;

import java.util.List;

public abstract class InvokeFunction extends ScriptVar {
	
	public InvokeFunction(FunctionSpecializer specializer)
	{
		this.specializer = specializer;
	}
	FunctionSpecializer specializer;
	
	@Override
	public ScriptVar curryValues(List<ScriptVar> values) throws ParserException
	{
		return invoke(values);
	}
	public abstract ScriptVar invoke(List<ScriptVar> values) throws ParserException;
	@Override
	public FunctionSpecializer getSpecializer()  
	{
		return specializer;
	}
	@Override
	public boolean isFunction()
	{
		return true;
	}		
}