package bladequest.bladescript;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bladequest.bladescript.ScriptVar.BadTypeException;
import bladequest.bladescript.ScriptVar.SpecializationLevel;

public class Script {


	Map<String, ScriptVar> standardLibrary; 
	Map<String, ScriptVar> globals;
	
	
	public ScriptVar getVariable(String var)
	{
		ScriptVar out = globals.get(var);
		if (out != null) return out;
		out = standardLibrary.get(var);
		return out;
	}
	
	public Script(Map<String, ScriptVar> standardLibrary)
	{
		this.standardLibrary = standardLibrary;
		globals = new HashMap<String, ScriptVar>();
	}
	
	private static class OpaqueSpecializer implements FunctionSpecializer
	{
		public Class<?> classType;
		OpaqueSpecializer(Class<?> classType)
		{
			this.classType = classType;
		}
		@Override
		public boolean Equals(FunctionSpecializer rhs) {
			
			OpaqueSpecializer rhsSpecializer = (OpaqueSpecializer)rhs;
			if (rhsSpecializer == null) return false;
			return classType.equals(rhsSpecializer.classType);
		}
		@Override
		public SpecializationLevel getSpecializationLevelFor(ScriptVar var)
				throws BadTypeException {
			if (!var.isOpaque()) return SpecializationLevel.NotSpecialized;
			//otherwise....  we do actually know this type.  #note to self - doable in C++ *statically* with counting template for future use
			//though you can't do conversions in C++.  This should automatically handle bactDamage -> battleAction, but it breaks some overloading.
			if (classType.isAssignableFrom(var.getOpaque().getClass()))
			{
				return SpecializationLevel.TypeSpecialized;
			}
			return SpecializationLevel.NotSpecialized;
		}
		@Override
		public String getSpecializationName() {
			return classType.getName();
		}
	}
	
	
	public static FunctionSpecializer getSpecializerForReflectedType(Class<?> classType)
	{
		if (classType.equals(int.class))
		{
			return Parser.getIntSpecializer();
		}
		else if (classType.equals(float.class))
		{
			return Parser.getFloatSpecializer();
		}
		else if (classType.equals(boolean.class))
		{
			return Parser.getBoolSpecializer();
		}
		else if (classType.equals(String.class))
		{
			return Parser.getStringSpecializer();
		}
		else if (classType.equals(ScriptVar.class))
		{
			return Parser.getGenericSpecializer();
		}		
		
		return new OpaqueSpecializer(classType);
	}
	public static InvokeFunction reflectedMethodToFunction(Method method, List<FunctionSpecializer> specializations)
	{
		for (Class<?> classType : method.getParameterTypes())
		{
			specializations.add(getSpecializerForReflectedType(classType));
		}

		FunctionSpecializer specializer = specializations.get(specializations.size()-1);
		specializations.remove(specializations.size()-1);
		
		return new InvokeFunction(specializer)
		{
			Method method;
			InvokeFunction initialize(Method method)
			{
				this.method = method;
				return this;
			}
			@Override
			public ScriptVar invoke(List<ScriptVar> values)
					throws BadTypeException {

				Object[] objects = new Object[values.size()];
				int argNum = 0;
				Class<?> paramTypes[] = method.getParameterTypes();
				for (ScriptVar var : values)
				{
					objects[argNum] = paramTypes[argNum].equals(ScriptVar.class) ? var : var.getAsObject();
					++argNum;
				}
				
				try {
					return ScriptVar.toScriptVar(method.invoke(null, objects));
				} catch (IllegalArgumentException e) {
					
				} catch (IllegalAccessException e) {

				} catch (InvocationTargetException e) {

				}
				return null;
			}

			@Override
			public ScriptVar clone() {
				return this;
			}
			
		}.initialize(method);
	}
	public static ScriptVar createFunction(InvokeFunction function, List<FunctionSpecializer> specializations)  throws BadTypeException
	{
		ScriptVar baseFunc = new FunctionBase();
		ScriptVar prevFunc = baseFunc;
		for (FunctionSpecializer specializer : specializations)
		{
			Function newFunc = new Function(specializer);
			prevFunc.addChildFunction(newFunc);
			prevFunc = newFunc;
		}
		if (function != null)
		{
			prevFunc.addChildFunction(function);
		}
		return baseFunc;
	}
	public static class BadSpecialization extends ParserException
	{
		BadSpecialization()
		{
			super("Function could not be specialized!");
		}
		private static final long serialVersionUID = 8350151948108957058L;
		
	}
	public void addGlobal(String name, ScriptVar global)
	{
		globals.put(name, global);
	}
	
	public static void specializeFunction(ScriptVar function, InvokeFunction specialization, List<FunctionSpecializer> specializations) throws BadTypeException, BadSpecialization
	{
		ScriptVar currentFunc = function;
		for (FunctionSpecializer specializer : specializations)
		{
			ScriptVar childFunc = currentFunc.getSpecializedChild(specializer);
			if (childFunc == null)
			{
				childFunc = new Function(specializer);
				currentFunc.addChildFunction(childFunc);
			}
			currentFunc = childFunc;
		}
		
		if (currentFunc.getSpecializedChild(specialization.getSpecializer()) != null) 
		{
			throw new BadSpecialization();
		}
		
		currentFunc.addChildFunction(specialization);
	}
	public void addInvokeFunction(String name, InvokeFunction function, List<FunctionSpecializer> specializations) throws BadTypeException, BadSpecialization
	{
		ScriptVar parentFunc = globals.get(name);
		if (parentFunc == null)
		{
			globals.put(name, createFunction(function, specializations));
		}
		else
		{
		  specializeFunction(parentFunc, function, specializations);	
		}
	}
	public void populateFunction(String name, List<FunctionSpecializer> specializations) throws BadTypeException, BadSpecialization
	{
		ScriptVar parentFunc = globals.get(name);
		if (parentFunc == null)
		{
			globals.put(name, createFunction(null, specializations));
		}
	}
}
