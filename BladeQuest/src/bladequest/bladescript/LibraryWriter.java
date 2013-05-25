package bladequest.bladescript;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bladequest.bladescript.Script.BadSpecialization;
import bladequest.bladescript.ScriptVar.BadTypeException;

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
	
	public void addAllIn(Class<?> c) throws BadTypeException, BadSpecialization
	{
		for (Method m : c.getDeclaredMethods())
		{
			if (!m.getReturnType().equals(void.class) && !m.isSynthetic() && m.getParameterTypes().length > 0)
			{
				add(m.getName(), m);
			}
		}
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
