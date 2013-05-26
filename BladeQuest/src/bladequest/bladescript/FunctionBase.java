package bladequest.bladescript;

import java.util.ArrayList;
import java.util.List;

public class FunctionBase extends ScriptVar {
	List<ScriptVar> children;
	FunctionBase()
	{
		children = new ArrayList<ScriptVar>();
	}
	public ScriptVar clone()
	{
		FunctionBase out = new FunctionBase();
		out.children = new ArrayList<ScriptVar>();
		for (ScriptVar child : children)
		{
			out.children.add(child.clone());
		}
		return out;
	}
	@Override
	public ScriptVar getSpecializedChild(FunctionSpecializer specialization) throws BadTypeException 
	{
		for (ScriptVar child : children)
		{
			if (child.getSpecializer().Equals(specialization)) return child;
		}
		return null;
	}
	@Override
	public void addChildFunction(ScriptVar childFunc)
	{
		children.add(childFunc);
	}
	@Override
	public ScriptVar apply(ScriptVar var) throws BadTypeException {
		// TODO Auto-generated method stub
		ScriptVar nextFunc = null;
		List<ScriptVar> curriedValues = new ArrayList<ScriptVar>();
		curriedValues.add(var);
		SpecializationLevel specialization = SpecializationLevel.NotSpecialized;
		for (ScriptVar child : children)
		{
			SpecializationLevel thisSpecialization = child.getSpecializer().getSpecializationLevelFor(var);
			if (thisSpecialization.ordinal() > specialization.ordinal())
			{
				nextFunc = child;
				specialization = thisSpecialization;
			}
		}
		if (nextFunc == null)
		{
			String types = "";
			for (int i = 0; i < children.size(); ++i)
			{
				types += children.get(i).getSpecializer().getSpecializationName();
				if (i != children.size() - 1)
				{
					types += " OR ";
				}
			}
			throw new BadTypeException(types, var.typeName());
		}
		return nextFunc.clone().curryValues(curriedValues);
	}
	@Override
	public boolean isFunction()
	{
		return true;
	}	
}
