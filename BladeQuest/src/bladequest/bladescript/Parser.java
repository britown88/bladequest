package bladequest.bladescript;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
import bladequest.bladescript.Script.BadSpecialization;
import bladequest.bladescript.ScriptVar.BadTypeException;
import bladequest.bladescript.ScriptVar.EmptyList;
import bladequest.bladescript.ScriptVar.ListNode;
import bladequest.bladescript.ScriptVar.SpecializationLevel;

public class Parser {
		
	private abstract class ParserState
	{
		protected List<ParserState> stateStack;
		String stateName; 
		
		public ParserState(List<ParserState> stateStack, String stateName)
		{
			this.stateStack = stateStack;
			this.stateName = stateName;
		}
		protected void popState()
		{
			stateStack.remove(stateStack.size()-1);
		}
		protected void pushState(ParserState state)
		{
			stateStack.add(state);
		}
		public void publishStatement(Statement statement) throws ParserException, BadTypeException
		{
			stateStack.get(stateStack.size()-2).addChildStatement(statement);
		}
		private void throwBadState(String tokenType) throws ParserException 
		{
			throw new ParserException("Parser state: " + stateName + " hit unhandable token: " + tokenType); 
		}
		public void beginList() throws ParserException {throwBadState("beginList");}
		public void endList() throws ParserException {throwBadState("endList");}
		public void beginParen() throws ParserException {throwBadState("beginParenthesis");}
		public void endParen() throws ParserException  {throwBadState("endParenthesis");}
		public void endLine() throws ParserException, BadTypeException, BadSpecialization  {throwBadState("endLine");}
		public void localDef() throws ParserException  {throwBadState("local definition");}
		public void listSeparator() throws ParserException  {throwBadState("list separator");}
		public void readName(String name) throws ParserException  {throwBadState("readName");}
		public void readNumber(int number) throws ParserException  {throwBadState("readNumber");}
		public void readString(String string) throws ParserException  {throwBadState("readString");}
		public void readFloat(float number) throws ParserException  {throwBadState("readFloat");}
		public void readBool(boolean bool) throws ParserException  {throwBadState("readBool");}
		public void lambdaFunction() throws ParserException  {throwBadState("lambdaFunction");}
		public void endLambdaFunction() throws ParserException  {throwBadState("endLambdaFunction");}
		public void patternMatch() throws ParserException  {throwBadState("patternMatch");}
		public void caseMarker() throws ParserException  {throwBadState("patternMatchCase");}
		public void infixBinder() throws ParserException  {throwBadState("bindInfix");}
		public void addChildStatement(Statement statement) throws ParserException, BadTypeException  {throwBadState("addChildStatement");}
	}
	
	
	List<ParserState> stateStack;
	Script script;
	
	ParserState getFunctionLevelParserState()
	{
		return new ParserState(stateStack, "Top Level Parser")
		{
			@Override
			public void readName(String name)
			{
				pushState(getFunctionSpecializationState(name));
			}
			@Override			
			public void endLine() 
			{
				//zzz
			}
			@Override
			public void addChildStatement(Statement statement) throws ParserException //variable declaration statement!
			{
				ExecutionState state = new ExecutionState();
				state.functionArgs = new ArrayList<ScriptVar>();
				ScriptVar globalVar = null;
				globalVar = statement.invokeChecked(state, "global variable declaration");	
				String globalVarName = state.locals.keySet().iterator().next();  //holy shit java really? 
				script.addGlobal(globalVarName, globalVar);
			}
			@Override
			public void localDef() throws ParserException 
			{
				pushState(getStatementState(new ArrayList<String>(), new ArrayList<String>()));
				stateStack.get(stateStack.size()-1).localDef();
			}
		};
	}
	
	
	Map<String, FunctionSpecializer> typeSpecializers;
	FunctionSpecializer genericSpecializer;
	
	ParserState getFunctionSpecializationState(String functionName)
	{
		return new ParserState(stateStack, "Function Specializer")
		{
			String fnName;
			List<FunctionSpecializer> specializers;
			List<String> argNames;
			FunctionSpecializer prevSpecializer;
			ParserState initialize(String fnName)
			{
				this.argNames = new ArrayList<String>();
				this.specializers = new ArrayList<FunctionSpecializer>();
				this.fnName = fnName;
				return this;
			}
			@Override
			public void readName(String name)
			{
				//a name... see if it's a specializer
				FunctionSpecializer specializer = typeSpecializers.get(name);
				if (specializer == null)
				{
					if (prevSpecializer == null)
					{
						specializers.add(genericSpecializer);	
					}
					else
					{
						specializers.add(prevSpecializer);
						prevSpecializer = null;
					}
					argNames.add(name);
				}
				else
				{
					prevSpecializer = specializer;
				}
			}
			public void readNumber(int number) throws ParserException 
			{
				argNames.add("");
				specializers.add(new FunctionSpecializer()
				{
					public int val;
					FunctionSpecializer initialize(int val)
					{
						this.val = val;
						return this;
					}
					@Override					
					public boolean Equals(FunctionSpecializer rhs) {
						return this.getClass() == rhs.getClass() && val == this.getClass().cast(rhs).val;
					}

					@Override
					public SpecializationLevel getSpecializationLevelFor(
							ScriptVar var) {
						try {
							if (var.isInteger() && var.getInteger() == val) return SpecializationLevel.ValueSpecialized;
						} catch (BadTypeException e) {
						}
						return SpecializationLevel.NotSpecialized;
					}
					@Override
					public String getSpecializationName() {
						return "Value: " + val;
					}
				}.initialize(number));
			}
			@Override
			public void endLine()

			{
				popState();
				pushState(getFunctionDefinitionState(fnName, specializers, argNames));
			}
		}.initialize(functionName);		
	}
	
	private class ExecutionState
	{
		ExecutionState()
		{
			locals = new HashMap<String, ScriptVar>();
		}
		public List<ScriptVar> functionArgs;
		public Map<String, ScriptVar> locals;
	}
	
	private abstract class Statement
	{
		int line;
		Statement()
		{
			line = getLineNumber();
		}
		public abstract ScriptVar invoke(ExecutionState state) throws ParserException;
		public ScriptVar invokeChecked(ExecutionState state, String currentState)  throws ParserException
		{
			ScriptVar out = null;
			try
			{
				out = invoke(state);
			}
			catch (ParserException e)
			{
				String errorMsg = currentState + " line: " + getLine();
				Log.d("Parser", "Runtime failure: " + errorMsg);
				throw new ParserException(errorMsg + " -> " + e.what());
			}
			return out;
		}
		public int       getLine() {return line;}
	}
	
	private Statement getStatementFromLocal(String name)
	{
		
		return new Statement()
		{
			private String localName;
			public Statement initialize(String localName)
			{
				this.localName = localName;
				return this;
			}					
			public ScriptVar invoke(ExecutionState state)
			{
				return state.locals.get(localName);
			}
		}.initialize(name);		
	}
	
	private Statement getStatementFromArgument(int argNumber)
	{
		return new Statement()
		{
			private int argNumber;
			public Statement initialize(int argNumber)
			{
				this.argNumber = argNumber;
				return this;
			}					
			public ScriptVar invoke(ExecutionState state)
			{
				return state.functionArgs.get(argNumber);
			}
		}.initialize(argNumber);		
	}
	
	private Statement getStatementFromGlobal(ScriptVar globalVar)
	{
		return new Statement()
		{
			private ScriptVar global;
			
			public Statement initialize(ScriptVar global)
			{
				this.global = global;
				return this;
			}
			public ScriptVar invoke(ExecutionState state)
			{
				return global;
			}
		}.initialize(globalVar);	
	}
	
	private Statement getStatementFromName(String name, List<String> argNames, List<String> locals) throws ParserException
	{
		//local variable?
		for (String arg : locals)
		{
			if (arg.equals(name))
			{
				return getStatementFromLocal(name);
			}
		}
		//argument name?
		int argNumber = 0;
		for (String arg : argNames)
		{
			if (arg.equals(name))
			{
				return getStatementFromArgument(argNumber);
			}
			++argNumber;
		}
		//global variable name?
		ScriptVar global = script.getVariable(name);
		if (global != null)
		{
			return getStatementFromGlobal(global);
		}
		//don't know what this is, guess it's an error.
		throw new ParserException("Undefined variable " + name + " encountered!");
	}
	
	private Statement getIntegerStatement(int arg)
	{
		return new Statement()
		{
			private int arg;
			public Statement initialize(int arg)
			{
				this.arg = arg;
				return this;
			}					
			public ScriptVar invoke(ExecutionState state)
			{
				return ScriptVar.toScriptVar(arg);
			}
		}.initialize(arg);		
	}
	private Statement getFloatStatement(float arg)
	{
		return new Statement()
		{
			private float arg;
			public Statement initialize(float arg)
			{
				this.arg = arg;
				return this;
			}				
			@Override
			public ScriptVar invoke(ExecutionState state)
			{
				return new ScriptVar()
				{
					@Override
					public ScriptVar clone() {
						return this;
					}
					@Override
					public boolean isFloat() { return true; }
					public float getFloat() {return arg;}
				};
			}
		}.initialize(arg);		
	}	
	private Statement getBoolStatement(boolean arg)
	{
		return new Statement()
		{
			private boolean arg;
			public Statement initialize(boolean arg)
			{
				this.arg = arg;
				return this;
			}					
			public ScriptVar invoke(ExecutionState state)
			{
				return new ScriptVar()
				{
					@Override
					public ScriptVar clone() {
						return this;
					}
					@Override
					public boolean isBoolean() { return true; }
					@Override
					public boolean getBoolean() {return arg;}
				};
			}
		}.initialize(arg);		
	}	
	private Statement getStringStatement(String arg)
	{
		return new Statement()
		{
			private String arg;
			public Statement initialize(String arg)
			{
				this.arg = arg;
				return this;
			}					
			public ScriptVar invoke(ExecutionState state)
			{
				return ScriptVar.toScriptVar(arg);
			}
		}.initialize(arg);		
	}		

	Statement compileStatementList(List<Statement> statements)
	{
		//GG
		return new Statement()
		{
			List<Statement> statements;
			Statement initialize(List<Statement> statements)
			{
				this.statements = statements;
				return this;
			}
			public ScriptVar invoke(ExecutionState state) throws ParserException {
				ScriptVar out = null;
				for (Statement statement : statements)
				{
					ScriptVar currentVar = statement.invokeChecked(state, "compiled statement");
					if (out == null)
					{
						out = currentVar;
					}
					else
					{
						out = out.apply(currentVar);
					}
				}
				return out;
			}
		}.initialize(statements);
	}
	Statement compileStatementList(List<Statement> statements, Statement binderStatement) throws ParserException
	{
		if (statements.size() == 0)
		{
			throw new ParserException("Trying to compile a null statement list.  Did you put two infix ops in a row?");
		}
		if (binderStatement == null) return compileStatementList(statements);

		//
		return new Statement()
		{
			List<Statement> statements;
			Statement binderStatement;
			
			Statement initialize(List<Statement> statements, Statement binderStatement)
			{
				this.statements = statements;
				this.binderStatement = binderStatement;
				return this;
			}
			public ScriptVar invoke(ExecutionState state) throws ParserException {
				ScriptVar out = null;
				//apply the binder before the bindee!!!
				ScriptVar bound = binderStatement.invokeChecked(state, "Infix Binder");
				for (Statement statement : statements)
				{
					ScriptVar currentVar = statement.invokeChecked(state, "Compiled Infix List");
					if (out == null)
					{
						out = currentVar;
						//apply binder after first statement.
						out = out.apply(bound);
					}
					else
					{
						out = out.apply(currentVar);
					}
				}
				return out;
			}
		}.initialize(statements, binderStatement);
	}	
	
	
	
	Statement compileStatementListToList(List<Statement> statements)
	{
		//GGGGGGGGG
		return new Statement()
		{
			List<Statement> statements;
			Statement initialize(List<Statement> statements)
			{
				this.statements = statements;
				return this;
			}
			public ScriptVar invoke(ExecutionState state) throws ParserException {
				ScriptVar out = new EmptyList();
				for (Statement statement : statements)
				{
					out = new ListNode(statement.invokeChecked(state, "List Content"), out);
				}
				return out;
			}
		}.initialize(statements);
	}
		
	InvokeFunction createLambdaFunction(List<String> argNames, ExecutionState state, Statement body)
	{
		return new InvokeFunction(genericSpecializer)
		{
			List<String> argNames;
			ExecutionState state;
			Statement body;
			InvokeFunction initialize(List<String> argNames, ExecutionState state, Statement body)
			{
				this.argNames = argNames;
				this.state = state;
				this.body = body;
				return this;
			}
			@Override
			public ScriptVar invoke(List<ScriptVar> values)
					throws ParserException {
				ExecutionState lambdaState= new ExecutionState();
				lambdaState.functionArgs = values;  //WHOA DUDE
				lambdaState.locals = new HashMap<String, ScriptVar>(state.locals); //lambda capture!
				int argNum = 0;
				for (ScriptVar var : state.functionArgs)
				{
					//even more lambda capture.  move arguments -> locals.
					lambdaState.locals.put(argNames.get(argNum++), var);
				}
				return body.invokeChecked(lambdaState, "Lambda function: " + values.size() + " arg(s)");
			}

			@Override
			public ScriptVar clone() {
				return this;
			}
			
		}.initialize(argNames, state, body);
	}
	Statement createLambdaFunctionStatement(List<String> argNames, List<String> lambdaArgs, Statement body)
	{
		return new Statement()
		{
			List<String> argNames;
			List<FunctionSpecializer> specializers;
			Statement body;
			
			Statement initialize(List<String> argNames, List<String> lambdaArgs, Statement body)
			{
				this.argNames = argNames;
				this.body = body;
				specializers = new ArrayList<FunctionSpecializer>();
				for (int i = 0; i < lambdaArgs.size(); ++i)
				{
					specializers.add(genericSpecializer);
				}
				specializers.remove(specializers.size()-1);
				return this;
			}
			public ScriptVar invoke(ExecutionState state)
					throws BadTypeException {

				return Script.createFunction(createLambdaFunction(argNames, state, body), specializers);
			}
			
		}.initialize(argNames, lambdaArgs, body);
	}
	
	ParserState getLambdaFunctionBodyState(List<String> argNames, List<String> locals, List<String> lambdaArgs)
	{
		locals = new ArrayList<String>(locals); //create a new buffer for internal lambda locals
		//argNames are added to locals, for bonus effect...
		for (String argName : argNames)
		{
			locals.add(argName); //arguments are inside the closure!!
		}
		//lambda args is the argument names now.
		return new StatementParser(stateStack, lambdaArgs, locals, "Lambda function body")
		{
			List<Statement> statements;
			{
				statements = new ArrayList<Statement>();
			}
			@Override
			public void endLine() throws ParserException
			{
				if (!substatements.isEmpty())
				{
					statements.add(compileSubstatements());
					this.substatements = new ArrayList<Statement>();
				}
			}
			@Override
			public void endParen() throws ParserException
			{
				if (!substatements.isEmpty())
				{
					statements.add(compileSubstatements());
				}
				publishStatement(new Statement()
				{
					List<Statement> statements;
					Statement initialize(List<Statement> statements)
					{
						this.statements = statements;
						return this;
					}
					public ScriptVar invoke(ExecutionState state)
							throws ParserException {

						ScriptVar var = null;
						for (Statement statement : statements)
						{
							var = statement.invoke(state);
						}
						return var;
					}
					
				}.initialize(statements));
				popState(); //pop lambda function base state.
			}
		};		
	}
	ParserState getLambdaFunctionState(List<String> argNames, List<String> locals)
	{
		return new ParserState(stateStack, "Lambda Function Builder")
		{
			List<String> argNames;
			List<String> locals;
			List<String> lambdaArgs;
			{
				lambdaArgs = new ArrayList<String>();
			}
			ParserState initialize(List<String> argNames, List<String> locals)
			{
				this.argNames = argNames;
				this.locals = locals;
				return this;
			}
			//child statement here is the lambda function!
			public void addChildStatement(Statement statement) throws ParserException
			{
				popState(); //pop to send to parent!
				publishStatement(createLambdaFunctionStatement(argNames, lambdaArgs, statement));
			}
			@Override
			public void endLambdaFunction() throws ParserException
			{
				pushState(getLambdaFunctionBodyState(argNames, locals, lambdaArgs));
			}
			@Override
			public void readName(String name)
			{
				lambdaArgs.add(name);
			}
		}.initialize(argNames, locals);
	}
	ParserState getListParserState(List<String> argNames, List<String> locals)
	{
		return new StatementParser(stateStack, argNames, locals, "List Parser")
		{
			List<Statement> listStatements;
			{
				listStatements = new ArrayList<Statement>(); 
			}
			@Override
			public void endLine()
			{
				//ignored in lists.
			}
			@Override
			public void listSeparator() throws ParserException
			{
				listStatements.add(compileSubstatements());		
				this.substatements = new ArrayList<Statement>();
			}
			@Override
			public void endList() throws ParserException 
			{
				listStatements.add(compileSubstatements());				
				publishStatement(compileStatementListToList(listStatements));
				popState();
			}			
		};
	}

	Statement getPatternMatchStatement(Statement matchingStatement, List<Statement> conditionalStatements, List<Statement> outputStatements)
	{
		return new Statement()
		{
			Statement matchingStatement; List<Statement> conditionalStatements; List<Statement> outputStatements;
			
			Statement initialize(Statement matchingStatement, List<Statement> conditionalStatements, List<Statement> outputStatements)
			{
				this.matchingStatement = matchingStatement; 
				this.conditionalStatements = conditionalStatements; 
				this.outputStatements = outputStatements;
				return this;
			}
			@Override
			public ScriptVar invoke(ExecutionState state) throws ParserException {

				ScriptVar var = matchingStatement.invoke(state);
				
				int num = 0;
				for (Statement s : conditionalStatements)
				{
					//function that takes var and returns bool
					if (s.invoke(state).apply(var).getBoolean() == true)
					{
						return outputStatements.get(num).invokeChecked(state, "conditional statment");
					}
					++num;
				}
				throw new ParserException("none of the functions pattern-matched!");
			}
		}.initialize(matchingStatement, conditionalStatements, outputStatements);
	}
	
	ParserState getPatternOutputState(List<String> argNames, List<String> locals, Statement matchingStatement, List<Statement> conditionalStatements, List<Statement> outputStatements)
	{
		return new StatementParser(stateStack, argNames, locals, "Pattern Matcher")
		{
			Statement matchingStatement;
			List<Statement> conditionalStatements;
			List<Statement> outputStatements;
			
			StatementParser initialize(Statement matchingStatement, List<Statement> conditionalStatements, List<Statement> outputStatements)
			{
				this.matchingStatement = matchingStatement;
				this.conditionalStatements = conditionalStatements;
				this.outputStatements = outputStatements;
				return this;
			}
			
			@Override
			public void patternMatch() throws ParserException 
			{
				outputStatements.add(compileSubstatements());
				popState();
				pushState(getPatternConditionalState(argNames, locals, matchingStatement, conditionalStatements, outputStatements));
			}
			
			@Override
			public void endParen() throws ParserException 
			{
				outputStatements.add(compileSubstatements());
				publishStatement(getPatternMatchStatement(matchingStatement, conditionalStatements, outputStatements));
				popState();
			}			
			
			@Override
			public void endLine() throws ParserException
			{
				//ignore end lines.  It's common form for the pattern match to be on the other side.
			}			
		}.initialize(matchingStatement, conditionalStatements, outputStatements);		
	}
	
	ParserState getPatternConditionalState(List<String> argNames, List<String> locals, Statement matchingStatement, List<Statement> conditionalStatements, List<Statement> outputStatements)
	{
		return new StatementParser(stateStack, argNames, locals, "Pattern Conditional Parser")
		{
			Statement matchingStatement;
			List<Statement> conditionalStatements;
			List<Statement> outputStatements;
			
			StatementParser initialize(Statement matchingStatement, List<Statement> conditionalStatements, List<Statement> outputStatements)
			{
				this.matchingStatement = matchingStatement;
				this.conditionalStatements = conditionalStatements;
				this.outputStatements = outputStatements;
				return this;
			}
			
			@Override
			public void caseMarker() throws ParserException 
			{
				conditionalStatements.add(compileSubstatements());
				popState();
				pushState(getPatternOutputState(argNames, locals, matchingStatement, conditionalStatements, outputStatements));
			}
		}.initialize(matchingStatement, conditionalStatements, outputStatements);		
	}
	ParserState getPatternMatchInitState(List<String> argNames, List<String> locals)
	{
		return new StatementParser(stateStack, argNames, locals, "Pattern Match Comparator Builder")
		{
			@Override
			public void patternMatch() throws ParserException 
			{
				Statement matchingStatement = compileSubstatements();
				List<Statement> conditionalStatements =  new ArrayList<Statement>();
				List<Statement> outputStatements =  new ArrayList<Statement>();
				
				popState();
				pushState(getPatternConditionalState(argNames, locals, matchingStatement, conditionalStatements, outputStatements));
			}
			@Override
			public void endLine() throws ParserException
			{
				//ignore end lines.  It's common form for the pattern match to be on the other side.
			}
		};
	}
	ParserState getParenthesisParserState(List<String> argNames, List<String> locals)
	{
		return new StatementParser(stateStack, argNames, locals, "Statement in Parenthesis Parser")
		{
			@Override
			public void endParen() throws ParserException
			{
				publishStatement(compileSubstatements());
				popState();
			}
			@Override
			public void endLine() throws ParserException
			{
//				if (substatements.isEmpty()) return;  //doesn't die on infix line end (e.g. a > b syntax)
//				super.endLine();
				return;
			}			
			@Override
			public void lambdaFunction() throws ParserException 
			{
				if (!substatements.isEmpty())
				{
					throw new ParserException("bad lambda function placement.");
				}
				//secret lambda function block!
				//woot woot hoot hoot
				popState();
				pushState(getLambdaFunctionState(argNames, locals));
			}					
			@Override
			public void patternMatch() throws ParserException 
			{
				if (!substatements.isEmpty())
				{
					throw new ParserException("bad pattern matching placement, requires no previous statements.");
				}
				popState();
				pushState(getPatternMatchInitState(argNames, locals));
			}								
		};
	}
		
	
	private ScriptVar getAddLocalFunction(String localName, ExecutionState state)
	{
		return new ScriptVar()
		{
			private String localName;
			private ExecutionState state;
			
			public ScriptVar initialize(String localName, ExecutionState state)
			{
				this.localName = localName;
				this.state = state;
				return this;
			}
			
			@Override
			public ScriptVar apply(ScriptVar var) throws BadTypeException {
				state.locals.put(localName, var);
				return var;
			}
			@Override
			public boolean isFunction()
			{
				return true;
			}	
			@Override
			public ScriptVar clone() {
				return this;
			}
		
		}.initialize(localName, state);		
	}
	Statement getAddLocalStatement(String localName)
	{
		return new Statement()
		{
			String localName;
			Statement initialize(String localName)
			{
				this.localName = localName;
				return this;
			}
			@Override
			public ScriptVar invoke(ExecutionState state)
			{
				return getAddLocalFunction(localName, state);
			}
		}.initialize(localName);
	}
	ParserState getLocalDefClass(List<String> locals, StatementParser parser)
	{
		return new ParserState(stateStack, "Local Definition Builder")
		{
			List<String> locals;
			StatementParser parser;
			String name;
			ParserState initialize(List<String> locals, StatementParser parser)
			{
				this.locals = locals;
				this.parser = parser;
				return this;
			}
			@Override
			public void readName(String name) throws ParserException 
			{
				if (this.name == null)
				{
					this.name = name;
				}
				else
				{
					if (!name.equals("<-"))
					{
						throw new ParserException("Missing arrow after local variable definition.  Got " + name + " instead of <-");
					}
					
					locals.add(this.name);
					this.parser.setLocalCreateStatement(getAddLocalStatement(this.name));
					popState(); //return back to whatever parser to fill in the function.
				}
			}			
		}.initialize(locals, parser);
	}
	
	private class StatementParser extends ParserState
	{
		List<String> argNames;
		List<String> locals;
		List<Statement> substatements;
		Statement localCreateStatement;
		Statement binderStatement;
		public StatementParser(List<ParserState> stateStack, List<String> argNames, List<String> locals, String stateName)
		{
			super(stateStack, stateName);
			this.argNames = argNames;
			this.locals = locals;
			this.substatements = new ArrayList<Statement>();
			localCreateStatement = null;
		}
		public void setLocalCreateStatement(Statement localCreateStatement)
		{
			this.localCreateStatement = localCreateStatement;  
		}
		public Statement compileSubstatements() throws ParserException
		{
			Statement out = null;
			if (localCreateStatement == null)
			{
				out = compileStatementList(substatements, binderStatement);
			}
			else
			{
				out = compileStatementList(substatements, binderStatement);
				substatements = new ArrayList<Statement>();
				
				//add the local variable.
				substatements.add(localCreateStatement);
				substatements.add(out);
				out = compileStatementList(substatements);
				localCreateStatement = null;
			}
			binderStatement = null;
			return out;
		}
		@Override
		public void addChildStatement(Statement subStatement)
		{
			substatements.add(subStatement);
		}
		@Override
		public void beginList() throws ParserException 
		{
		   pushState(getListParserState(argNames, locals));
		}
		@Override
		public void beginParen() throws ParserException 
		{
			pushState(getParenthesisParserState(argNames, locals));
		}
		@Override
		public void readName(String name) throws ParserException 
		{
			substatements.add(getStatementFromName(name, argNames, locals));			
		}
		@Override
		public void readNumber(int number) throws ParserException
		{
			substatements.add(getIntegerStatement(number));	
		}
		@Override
		public void readString(String string) throws ParserException 
		{
			substatements.add(getStringStatement(string));	
		}
		@Override
		public void readFloat(float number) throws ParserException 
		{
			substatements.add(getFloatStatement(number));
		}
		@Override
		public void readBool(boolean bool) throws ParserException 
		{
			substatements.add(getBoolStatement(bool));
		}
		public void localDef() 
		{
			pushState(getLocalDefClass(locals, this));	
		}
	
		public void infixBinder() throws ParserException
		{
			//parser another statement, write it FIRST.
			//statement1 > statement2 statment3
			//statement2 statement1 statment3
			
			//we do this by compile the current statements into a binder statement.
			//from there, after the first statement, we then apply then run the statement and apply.
			binderStatement = compileSubstatements();
			this.substatements = new ArrayList<Statement>();
		}
	
	}
	
	ParserState getStatementState(List<String> argNames, List<String> locals)
	{
		return new StatementParser(stateStack, argNames, locals, "Top-level statement Parser")
		{
			@Override
			public void endLine() throws ParserException 
			{
				//if the substatements are empty and we're at the end of a line in this state, it means that there was just
				//an infix binder used, e.g.
				//makeAbility "foo" >
				//   setDesc "example" > #... 
				if (!substatements.isEmpty())
				{
					publishStatement(compileSubstatements());
					popState();
				}
			}			
		};
	}	
	
	InvokeFunction makeScriptFunction(String functionName, List<Statement> statements, FunctionSpecializer finalSpecializer)
	{
		return new InvokeFunction(finalSpecializer)
		{
			String name;
			List<Statement> statements;
			InvokeFunction initialize(List<Statement> statements, String functionName)
			{
				this.statements = statements;
				this.name = functionName;
				return this;
			}
			@Override
			public ScriptVar invoke(List<ScriptVar> arguments) throws ParserException {
				//time to run the function!
				ScriptVar last = null;
				ExecutionState state = new ExecutionState();
				state.functionArgs = arguments;
				for (Statement statement : statements)
				{
					last = statement.invokeChecked(state, "function: " + name);	
				}
				return last;
			}

			@Override
			public ScriptVar clone() {
				return this;
			}
		}.initialize(statements, functionName);
	}
	
	ParserState getFunctionDefinitionState(String functionName, List<FunctionSpecializer> specializers, List<String> argNames)
	{
		return new ParserState(stateStack, "Function Starter")
		{
			String fnName;
			List<FunctionSpecializer> specializers;
			List<String> argNames;
			List<String> locals;
			List<Statement> statements;
			ParserState initialize(String fnName, List<FunctionSpecializer> specializers, List<String> argNames)
			{
				this.argNames = argNames;
				this.specializers = specializers;
				this.fnName = fnName;
				this.locals = new ArrayList<String>();
				this.statements = new ArrayList<Statement>();
				return this;
			}	
			void moveToStatementState()
			{
				pushState(getStatementState(argNames, locals));
			}
			@Override
			public void addChildStatement(Statement statement) 
			{
				statements.add(statement);
			} 
			public void beginList() throws ParserException
			{
				moveToStatementState();
				stateStack.get(stateStack.size()-1).beginList();				
			}
			public void beginParen() throws ParserException 
			{
				moveToStatementState();
				stateStack.get(stateStack.size()-1).beginParen();
			}
			public void readName(String name) throws ParserException
			{
				moveToStatementState();
				stateStack.get(stateStack.size()-1).readName(name);
			}
			public void readNumber(int number)  throws ParserException
			{
				moveToStatementState();
				stateStack.get(stateStack.size()-1).readNumber(number);
			}
			public void readString(String string)  throws ParserException
			{
				moveToStatementState();
				stateStack.get(stateStack.size()-1).readString(string);	
			}
			public void readFloat(float number)  throws ParserException
			{
				moveToStatementState();
				stateStack.get(stateStack.size()-1).readFloat(number);	
			}
			public void readBool(boolean bool)   throws ParserException
			{
				moveToStatementState();
				stateStack.get(stateStack.size()-1).readBool(bool);
			}
			
			
			public void endLine() throws BadTypeException, BadSpecialization 
			{
				FunctionSpecializer lastSpecializer = specializers.get(specializers.size()-1);
				specializers.remove(specializers.size()-1);				
				script.populateFunction(fnName, specializers);
				InvokeFunction function = makeScriptFunction(fnName, statements, lastSpecializer);
				script.addInvokeFunction(fnName, function, specializers);
				popState();
			}
			public void localDef() throws ParserException
			{
				moveToStatementState();
				stateStack.get(stateStack.size()-1).localDef();				
			}			
		}.initialize(functionName, specializers, argNames);
	}
	Tokenizer tokenizer;
	
	public static abstract class TypeSpecializer implements FunctionSpecializer
	{

		@Override
		public boolean Equals(FunctionSpecializer rhs) {
			return this.getClass() == rhs.getClass(); //reflection fun times!
		}

		public abstract boolean specializes(ScriptVar var);
		@Override
		public SpecializationLevel getSpecializationLevelFor(ScriptVar var) {
			if (specializes(var))
			{
				return SpecializationLevel.TypeSpecialized;
			}
			return SpecializationLevel.NotSpecialized;
		}
		
	}
	
	public static TypeSpecializer getIntSpecializer()
	{
		return new TypeSpecializer(){public boolean specializes(ScriptVar var) {return var.isInteger();} public String getSpecializationName() {return "int";}};
	}
	
	public static TypeSpecializer getFloatSpecializer()
	{
		return new TypeSpecializer(){public boolean specializes(ScriptVar var) {return var.isFloat();} public String getSpecializationName() {return "float";}};
	}	
	
	public static TypeSpecializer getStringSpecializer()
	{
		return new TypeSpecializer(){public boolean specializes(ScriptVar var) {return var.isString();} public String getSpecializationName() {return "string";}};
	}
	
	public static TypeSpecializer getBoolSpecializer()
	{
		return new TypeSpecializer(){public boolean specializes(ScriptVar var) {return var.isBoolean();} public String getSpecializationName() {return "bool";}};
	}		
	
	public static TypeSpecializer getListSpecializer()
	{
		return new TypeSpecializer(){public boolean specializes(ScriptVar var) {return var.isList();} public String getSpecializationName() {return "list";}};
	}		
	
	public static TypeSpecializer getOpaqueSpecializer()
	{
		return new TypeSpecializer(){public boolean specializes(ScriptVar var) {return var.isOpaque();} public String getSpecializationName() {return "anyOpaque";}};
	}			
	
	public static TypeSpecializer getFunctionSpecializer()
	{
		return new TypeSpecializer(){public boolean specializes(ScriptVar var) {return var.isFunction();} public String getSpecializationName() {return "function";}};
	}				
		
	
	public static FunctionSpecializer getGenericSpecializer()
	{
		return new FunctionSpecializer()
		{

			@Override
			public boolean Equals(FunctionSpecializer rhs) {
				return rhs.getClass() == this.getClass();
			}
			

			@Override
			public SpecializationLevel getSpecializationLevelFor(ScriptVar var) {
				return SpecializationLevel.Generic;
			}


			@Override
			public String getSpecializationName() {
				return "any";
			}
		};
	}

	private void populateTypeSpecializers()
	{
		typeSpecializers.put("int", getIntSpecializer());
		typeSpecializers.put("float", getFloatSpecializer());
		typeSpecializers.put("string", getStringSpecializer());
		typeSpecializers.put("bool", getBoolSpecializer());
		typeSpecializers.put("list", getListSpecializer());
		typeSpecializers.put("opaque", getOpaqueSpecializer());
		typeSpecializers.put("func", getFunctionSpecializer());		
	}
	
	public int lineNumber = 1;
	public int getLineNumber()
	{
		return lineNumber;
	}
	public Parser(Tokenizer tokenizer, Script script)
	{
		this.script = script;
		typeSpecializers = new HashMap<String, FunctionSpecializer>();
		populateTypeSpecializers();
		genericSpecializer = getGenericSpecializer();
		stateStack = new ArrayList<ParserState>();
		this.tokenizer = tokenizer;
	}
	
	public void run()
	{
		List<Integer> parenStack = new ArrayList<Integer>();
		stateStack.add(getFunctionLevelParserState());
		try
		{
			
			for(ScriptToken token = tokenizer.getNextToken();;token = tokenizer.getNextToken())
			{	
				ParserState state = stateStack.get(stateStack.size()-1);
				switch (token.getType())
				{
				case BeginList:
					state.beginList();
					break;
				case BeginParen:
					parenStack.add(lineNumber);
					state.beginParen();
					break;
				case Boolean:
					state.readBool(token.getBoolean());
					break;
				case EndFile:
					state.endLine();
					state = stateStack.get(stateStack.size()-1);
					state.endLine();
					break;
				case EndLine:
					state.endLine();
					++lineNumber;
					break;
				case EndList:
					state.endList();
					break;
				case EndParen:
					state.endParen();
					parenStack.remove(parenStack.size()-1);
					break;
				case Float:
					state.readFloat(token.getFloat());
					break;
				case Name:
					state.readName(token.getName());
					break;
				case Number:
					state.readNumber(token.getNumber());
					break;
				case String:
					state.readString(token.getString());
					break;
				case LocalDef:
					state.localDef();
					break;
				case ListSeparator:
					state.listSeparator();
					break;
				case beginLambdaFunction:
					state.lambdaFunction();
					break;
				case endLambdaFunction:
					state.endLambdaFunction();
					break;	
				case patternMatch:
					state.patternMatch();
					break;
				case caseMarker:
					state.caseMarker();
					break;
				case infixBinder:
					state.infixBinder();
					break;					
				default:
					break; 
				}
				if (token.getType() == ScriptToken.Type.EndFile) break; 
			}		
			if (stateStack.size() != 1)
			{
				String openParens = "Open paren lines: ";
				for (int i : parenStack)
				{
					openParens = openParens + " " + i;  
				}
				throw new ParserException("Parser ended with pending states.  Did you end the file with a missed parenthesis or other grammer?  " + openParens) ;
			}
		}	
		catch(ParserException e)
		{
			//YOU DUN GOOFED			
			e.printStackTrace();
			Log.d("Parser", tokenizer.getExceptionInfo() + " Error on line: " + lineNumber + "  -> "+ e.what());
		}	
	}
	
}
