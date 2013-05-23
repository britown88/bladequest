package bladequest.scripting;

public abstract class ScriptToken {
	public enum Type
	{
		BeginParen,
		EndParen,
		Name,
		String,
		Number,
		Float,
		Boolean,
		BeginList,
		EndList,
		EndLine,		
		EndFile,
		LocalDef,
		ListSeparator,
		beginLambdaFunction,
		endLambdaFunction,
		patternMatch,
		caseMarker,
		infixBinder
	}
	private Type type;
	public Type getType()
	{
		return type;
	}
	public class BadGet extends ParserException {
		public BadGet()
		{
			super("Returning illegal type from token.");
		}
		private static final long serialVersionUID = 350383328848066619L;
	};
	
	//overrides
	public String getName() throws BadGet { throw new BadGet(); }
	public String getString() throws BadGet { throw new BadGet(); }
	public int getNumber() throws BadGet { throw new BadGet(); }
	public float getFloat() throws BadGet { throw new BadGet(); }
	public boolean getBoolean() throws BadGet { throw new BadGet(); }
	
	public ScriptToken(Type type)
	{
		this.type = type;
	}	
	
}
