package bladequest.bladescript;

import bladequest.bladescript.ScriptVar.SpecializationLevel;

public interface FunctionSpecializer {
	
	boolean Equals(FunctionSpecializer rhs);
	String getSpecializationName();
	SpecializationLevel getSpecializationLevelFor(ScriptVar var) throws ParserException; 
}
