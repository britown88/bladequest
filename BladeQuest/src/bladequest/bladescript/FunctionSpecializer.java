package bladequest.bladescript;

import bladequest.bladescript.ScriptVar.BadTypeException;
import bladequest.bladescript.ScriptVar.SpecializationLevel;

public interface FunctionSpecializer {
	
	boolean Equals(FunctionSpecializer rhs);
	SpecializationLevel getSpecializationLevelFor(ScriptVar var) throws BadTypeException; 
}
