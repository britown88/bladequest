package bladequest.scripting;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bladequest.scripting.Script.BadSpecialization;
import bladequest.scripting.ScriptVar.BadTypeException;

public class LibraryWriter {
	
	Map<String, ScriptVar> library;
	
	public Map<String, ScriptVar> getLibrary()
	{
		return library;
	}
	public LibraryWriter()
	{
		library = new HashMap<String, ScriptVar>();
	}
	public void add(String name, Method m) throws BadTypeException, BadSpecialization
	{
		List<FunctionSpecializer> specializations= new ArrayList<FunctionSpecializer>();;
		InvokeFunction func = Script.reflectedMethodToFunction(m, specializations);
		populateFunction(name, specializations);
		addInvokeFunction(name, func, specializations);
	}
	
	private void addInvokeFunction(String name, InvokeFunction function, List<FunctionSpecializer> specializations) throws BadTypeException, BadSpecialization
	{
		ScriptVar parentFunc = library.get(name);
		Script.specializeFunction(parentFunc, function, specializations);	
	}
	private void populateFunction(String name, List<FunctionSpecializer> specializations) throws BadTypeException, BadSpecialization
	{
		ScriptVar parentFunc = library.get(name);
		if (parentFunc == null)
		{
			library.put(name, Script.createFunction(null, specializations));
		}
	}	
	
}
