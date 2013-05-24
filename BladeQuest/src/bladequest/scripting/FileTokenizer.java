package bladequest.scripting;

import java.io.InputStream;

public class FileTokenizer implements Tokenizer {

	InputStream input;
	boolean savedChar;
	char saved;
	String buffer;
	boolean stringBuffer, commented, lastCharEnd, atEnd;
	public FileTokenizer(InputStream input)
	{
		atEnd = false;
		this.input = input;
		savedChar = false;
		buffer = "";
	}
	ScriptToken getEOFToken()
	{
		return new ScriptToken(ScriptToken.Type.EndFile) 
		{
			
		};
	}
	
	public boolean isFloat(String str)
	{
		boolean foundDot = false;
		int num = 0;
		for (char c : str.toCharArray())
		{
			++num; 
			if (c == '-' && num == 1) 
			{
				continue;
			}
			if (c == 'f' && num == str.length())
			{
				foundDot = true;
				continue;
			}
			if (c > '9' || c < '0')
			{
				if (c == '.')
				{
					if (foundDot) return false;
					foundDot = true;		
					continue;
				}
				return false;
			}
		}
		return foundDot;
	}
	
	public boolean isInt(String str)
	{
		int num = 0;
		for (char c : str.toCharArray())
		{
			if (c == '-' && num == 0) {++num; continue;}
			if (c > '9' || c < '0')
			{
				return false;
			}
			++num;
		}		
		return true;
	}
	public ScriptToken getBoolToken(boolean value)
	{
		return new ScriptToken(ScriptToken.Type.Boolean)
		{
			boolean value;
			ScriptToken initialize(boolean value) {this.value = value; return this;}
			public boolean getBoolean()  { return value;}
		}.initialize(value);
	}
	
	public ScriptToken sendBuffer()
	{
		if (buffer.equals("")) return null;
		
		String temp = buffer;
		buffer = "";
		if (stringBuffer)
		{
			return new ScriptToken(ScriptToken.Type.String)
			{
				String str;
				ScriptToken initialize(String str) {this.str = str; return this;}
				public String getString()  { return  str;}
			}.initialize(temp);
		}
		
		if (isInt(temp))
		{
			return new ScriptToken(ScriptToken.Type.Number)
			{
				int number;
				ScriptToken initialize(int number) {this.number = number; return this;}
				public int getNumber()  { return number;}
			}.initialize(Integer.parseInt(temp));
		}
		
		if (isFloat(temp))
		{
			return new ScriptToken(ScriptToken.Type.Float)
			{
				float number;
				ScriptToken initialize(float number) {this.number = number; return this;}
				public float getFloat()  { return number;}
			}.initialize(Float.parseFloat(temp));
		}
		
		if (temp.equals("true"))
		{
			return getBoolToken(true);
		}		
		if (temp.equals("false"))
		{
			return getBoolToken(false);
		}				
		
		if (temp.equals("("))
		{
			return new ScriptToken(ScriptToken.Type.BeginParen){};
		}
		if (temp.equals(")"))
		{
			return new ScriptToken(ScriptToken.Type.EndParen){};
		}
		if (temp.equals("{"))
		{
			return new ScriptToken(ScriptToken.Type.BeginList){};
		}
		if (temp.equals("}"))
		{
			return new ScriptToken(ScriptToken.Type.EndList){};
		}		
		if (temp.equals("\\"))
		{
			return new ScriptToken(ScriptToken.Type.LocalDef){};
		}				
		if (temp.equals(","))
		{
			return new ScriptToken(ScriptToken.Type.ListSeparator){};
		}
		if (temp.equals("["))
		{
			return new ScriptToken(ScriptToken.Type.beginLambdaFunction){};
		}
		if (temp.equals("]"))
		{
			return new ScriptToken(ScriptToken.Type.endLambdaFunction){};
		}
		if (temp.equals("|"))
		{
			return new ScriptToken(ScriptToken.Type.patternMatch){};
		}
		if (temp.equals(":"))
		{
			return new ScriptToken(ScriptToken.Type.caseMarker){};
		}
		if (temp.equals(">"))
		{
			return new ScriptToken(ScriptToken.Type.infixBinder){};
		}		
		//if everything else fails, it's a name of something.
		return new ScriptToken(ScriptToken.Type.Name)
		{
			String str;
			ScriptToken initialize(String str) {this.str = str; return this;}
			public String getName()  { return  str;}
		}.initialize(temp);
	}
	public void saveChar(char character)
	{
		savedChar = true;
		saved = character;
	}

	public ScriptToken nextCharacter(char character)
	{
		boolean lastEnd = lastCharEnd;
		lastCharEnd = false;
		boolean bufferEmpty = buffer.equals(""); 
		switch (character)
		{
		case ' ': case '\t':	if (commented || stringBuffer) break; return sendBuffer(); 
		case '\r': case '\n':
		{
			commented = false;
			if (bufferEmpty)
			{
				if (!lastEnd)
				{
					lastCharEnd = true;
					return new ScriptToken(ScriptToken.Type.EndLine){};
				}
				return null;
			}
			saveChar(character); return sendBuffer();			
		}
		case '>': case '(': case ')': case '{': case '}': case '\\': case ',': case '[': case ']':  case '|': case ':' : if (commented) break;
			if (!stringBuffer)
			{
				if (bufferEmpty) {buffer += character; return sendBuffer();}
				saveChar(character); return sendBuffer();				
			}
			break;
		case '\"': if (commented) break;
			if (stringBuffer)
			{
				ScriptToken out = sendBuffer();
				stringBuffer = false;
				return out;
			}
			else
			{
				if (!bufferEmpty) 
				{
					saveChar(character); return sendBuffer();
				}
				else
				{
					stringBuffer = true;
					return null;
				}
			}
		case '#':
			if (!commented)	
			{
				commented = true;
				return sendBuffer();				
			}		
		default:
		}
		if (!commented)
		{
			buffer += character;
		}		
		return null;
	}
	@Override
	public ScriptToken getNextToken() {

		ScriptToken token = null;
		if (savedChar)
		{
			savedChar = false;
			token = nextCharacter(saved);
			if (token != null) return token;
		}
		while (token == null)
		{
			if (atEnd)
			{
				return getEOFToken();
			}
		char c;
		int i = 0;
			try {
				i = input.read();
			} catch (Exception e) {
				atEnd = true;
				token = sendBuffer();
				if (token != null) return token;
				
				return getEOFToken();
			}
			
			if(i == -1)
			{
				atEnd = true;
				token = sendBuffer();
				if (token != null) return token;
				
				return getEOFToken();
			}
			
			c = (char)i;				

			token = nextCharacter(c);
		}			
		return token;
	}

}
