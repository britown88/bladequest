package bladequest.bladescript;

import java.util.ArrayList;
import java.util.List;

public abstract class ScriptVar {
	
	public static class ListNode extends ScriptVar 
	{
		public ListNode(ScriptVar headNode, ScriptVar tailNode)
		{
			this.headNode = headNode;
			this.tailNode = tailNode;
		}
		public boolean isList()
		{
			return true;
		}
		public boolean isEmptyList()
		{
			return false;
		}				
		public ScriptVar head()  throws BadTypeException 
		{
			return headNode;
		}
		public ScriptVar tail()  throws BadTypeException 
		{
			return tailNode;
		}
				
		public ScriptVar headNode;
		public ScriptVar tailNode;
		@Override
		public ScriptVar clone() {
			return this;
		}
	}
	
	public static class EmptyList extends ScriptVar
	{
		public boolean isList()
		{
			return true;
		}
		public boolean isEmptyList()
		{
			return true;
		}		
		public ScriptVar clone() {
			return this;
		}
		
	}	
	
	public class BadTypeException extends ParserException
	{
		BadTypeException(String expectedType, String myType)
		{
			super("Accessing invalid type! Got a " + myType + " but expected a " + expectedType);
		}
		private static final long serialVersionUID = 4234017580208305725L;	
	}
	
	public String typeName()
	{
		if (isInteger()) return "int";
		if (isFloat()) return "float";
		if (isString()) return "string";
		if (isBoolean()) return "bool";
		if (isList()) return "list";
		if (isFunction()) return "function";
		//otherwise... opaque!
		
		try {
			return getOpaque().getClass().getName();
		} catch (BadTypeException e) {
			e.printStackTrace();
		}
		return "Error";
	}
	public boolean isInteger() { return false; }
	public boolean isFloat()  { return false; }
	public boolean isString()  { return false; }
	public boolean isBoolean()  { return false; }
	public boolean isList()  { return false; }
	public boolean isOpaque()  { return false; } //java functions
	public boolean isFunction()  { return false; }
	public boolean isEmptyList()  { return false; }
	
	
	public static ScriptVar genericScriptToVar(List<?> list)
	{
		ScriptVar out = new EmptyList();
		for (Object obj : list)
		{
			out = new ListNode(toScriptVar(obj), out);
		}
		return out;
	}
	public static ScriptVar toScriptVar(Object obj)
	{
		if (obj.getClass() == Integer.class)
		{
			return toScriptVar(((Integer)obj).intValue());
		}
		else if (obj.getClass() == Float.class)
		{
			return toScriptVar(((Float)obj).floatValue());			
		}
		else if (obj.getClass() == Boolean.class)
		{
			return toScriptVar(((Boolean)obj).booleanValue());			
		}		
		else if (obj.getClass() == String.class)
		{
			return toScriptVar(((String)obj));			
		}		
		else if (ScriptVar.class.isAssignableFrom(obj.getClass()) )
		{
			//a type of scriptVar!  just return it.
			return (ScriptVar)obj;
		}
		else if (obj instanceof List<?>)
		{
			return genericScriptToVar((List<?>)obj);
		}

		return new ScriptVar()
		{
			Object obj;
			ScriptVar initialize(Object obj)
			{
				this.obj = obj;
				return this;
			}
			@Override
			public ScriptVar clone() {
				return this;
			}
			@Override
			public boolean isOpaque() 
			{
				return true; 
			}
			@Override
			public Object getOpaque() 
			{
				return obj;
			}
		}.initialize(obj);		
	}
	public static ScriptVar toScriptVar(String string)
	{
		return new ScriptVar()
		{
			String str;
			ScriptVar initialize(String string)
			{
				this.str = string;
				return this;
			}
			@Override
			public ScriptVar clone() {
				return this;
			}
			@Override
			public boolean isString() 
			{
				return true; 
			}
			@Override
			public String getString() 
			{
				return str;
			}
		}.initialize(string);
	}
	public static ScriptVar toScriptVar(boolean boolVar)
	{
		return new ScriptVar()
		{
			boolean boolVar;
			ScriptVar initialize(boolean boolVar)
			{
				this.boolVar = boolVar;
				return this;
			}
			@Override
			public ScriptVar clone() {
				return this;
			}
			@Override
			public boolean isBoolean() 
			{
				return true; 
			}
			@Override
			public boolean getBoolean() 
			{
				return boolVar;
			}
		}.initialize(boolVar);
	}		
	public static ScriptVar toScriptVar(int intVar)
	{
		return new ScriptVar()
		{
			int intVar;
			ScriptVar initialize(int intVar)
			{
				this.intVar = intVar;
				return this;
			}
			@Override
			public ScriptVar clone() {
				return this;
			}
			@Override
			public boolean isInteger() 
			{
				return true; 
			}
			@Override
			public int getInteger() 
			{
				return intVar;
			}
		}.initialize(intVar);
	}	
	public static ScriptVar convertStringList(List<String> strings)
	{
		List<ScriptVar> vars = new ArrayList<ScriptVar>();
		for (String str : strings)
		{
			vars.add(toScriptVar(str));
		}
		return compileList(vars);
	}
	public static ScriptVar compileList(List<ScriptVar> vars)
	{
		ScriptVar out = new EmptyList();
		for (ScriptVar var : vars)
		{
			out = new ListNode(var, out);
		}
		return out;
	}
	
	public void appendToList(List<Object> objList)
	{
		if (isList())
		{
			if (isEmptyList()) return;
			try {			
				objList.add(head());
				tail().appendToList(objList);
			} catch (BadTypeException e) {

			}
		}
		else
		{
			objList.add(getAsObject());
		}
	}
	public Object getAsObject()
	{
		try {		
			if (isInteger()) return getInteger();
			if (isFloat())   return getFloat();
			if (isBoolean()) return getBoolean();
			if (isString())  return getString();
			if (isOpaque())  return getOpaque();
			
			if (isList())
			{
				List<Object> objList = new ArrayList<Object>();
				appendToList(objList);
				return objList;
			}
		} catch (BadTypeException e) {

		}		
		return null;
	}
	public abstract ScriptVar clone();
	
	//int controls
	public int getInteger() throws BadTypeException {throw new BadTypeException("int", typeName());}
	
	//float controls
	public float getFloat()  throws BadTypeException {throw new BadTypeException("float", typeName());}
	
	//string controls
	public String getString()  throws BadTypeException {throw new BadTypeException("string", typeName());}
	
	//boolean controls
	public boolean getBoolean() throws BadTypeException {throw new BadTypeException("bool", typeName());}
	
	//Opaque controls
	public Object getOpaque()  throws BadTypeException {throw new BadTypeException("javaType", typeName());}
	
	//List controls
	public ScriptVar head()  throws BadTypeException {throw new BadTypeException("list", typeName());}
	public ScriptVar tail()  throws BadTypeException {throw new BadTypeException("function", typeName());}
	
	//Function controls
	public enum SpecializationLevel 
	{
		NotSpecialized,
		Generic,
		TypeSpecialized,
		ValueSpecialized
	}
	public ScriptVar getSpecializedChild(FunctionSpecializer specialization) throws BadTypeException {throw new BadTypeException("function", typeName());}
	public void addChildFunction(ScriptVar child)  throws BadTypeException {throw new BadTypeException("function", typeName());}
	public ScriptVar curryValues(List<ScriptVar> var)   throws BadTypeException {throw new BadTypeException("function", typeName());}
	public FunctionSpecializer getSpecializer()  throws BadTypeException {throw new BadTypeException("function", typeName());}
	public ScriptVar apply(ScriptVar var)  throws BadTypeException {throw new BadTypeException("function", typeName());}
}
