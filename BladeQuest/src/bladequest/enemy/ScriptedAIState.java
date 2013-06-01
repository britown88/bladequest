package bladequest.enemy;

import bladequest.bladescript.ParserException;
import bladequest.bladescript.ScriptVar;

public class ScriptedAIState extends AIState {

	ScriptVar scriptFn;
	public ScriptedAIState(ScriptVar scriptFn)
	{
		this.scriptFn = scriptFn;
	}
	@Override
	public void runAI(Enemy e, AIDecision decision) {
		try {
			scriptFn.apply(ScriptVar.toScriptVar(e)).apply(ScriptVar.toScriptVar(decision));
		} catch (ParserException ex) {
			ex.printStackTrace();
		}
	}

}
