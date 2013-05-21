package bladequest.scripting;

import bladequest.scripting.ScriptVar.BadTypeException;
import bladequest.scripting.ScriptVar.SpecializationLevel;

public interface FunctionSpecializer {
	
	boolean Equals(FunctionSpecializer rhs);
	SpecializationLevel getSpecializationLevelFor(ScriptVar var) throws BadTypeException; 
}
