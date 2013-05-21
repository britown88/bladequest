package bladequest.scripting;

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
		BadTypeException()
		{
			super("Accessing invalid type!");
		}
		private static final long serialVersionUID = 4234017580208305725L;	
	}
	
	public boolean isInteger() { return false; }
	public boolean isFloat()  { return false; }
	public boolean isString()  { return false; }
	public boolean isBoolean()  { return false; }
	public boolean isList()  { return false; }
	public boolean isOpaque()  { return false; } //java functions
	public boolean isFunction()  { return false; }
	public boolean isEmptyList()  { return false; }
	
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
	
	public abstract ScriptVar clone();
	
	//int controls
	public int getInteger() throws BadTypeException {throw new BadTypeException();}
	
	//float controls
	public float getFloat()  throws BadTypeException {throw new BadTypeException();}
	
	//string controls
	public String getString()  throws BadTypeException {throw new BadTypeException();}
	
	//boolean controls
	public boolean getBoolean() throws BadTypeException {throw new BadTypeException();}
	
	//Opaque controls
	public Object getOpaque()  throws BadTypeException {throw new BadTypeException();}
	
	//List controls
	public ScriptVar head()  throws BadTypeException {throw new BadTypeException();}
	public ScriptVar tail()  throws BadTypeException {throw new BadTypeException();}
	
	//Function controls
	public enum SpecializationLevel 
	{
		NotSpecialized,
		Generic,
		TypeSpecialized,
		ValueSpecialized
	}
	public ScriptVar getSpecializedChild(FunctionSpecializer specialization) throws BadTypeException {throw new BadTypeException();}
	public void addChildFunction(ScriptVar child)  throws BadTypeException {throw new BadTypeException();}
	public ScriptVar curryValues(List<ScriptVar> var)   throws BadTypeException {throw new BadTypeException();}
	public FunctionSpecializer getSpecializer()  throws BadTypeException {throw new BadTypeException();}
	public ScriptVar apply(ScriptVar var)  throws BadTypeException {throw new BadTypeException();}
}