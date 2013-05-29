package bladequest.enemy;

import bladequest.bladescript.ParserException;
import bladequest.bladescript.ScriptVar;
import bladequest.world.Ability;

public class ScriptedAIState extends AIState {

	ScriptVar scriptFn;
	public ScriptedAIState(ScriptVar scriptFn)
	{
		this.scriptFn = scriptFn;
	}
	@Override
	public Ability pickAbility(Enemy enemy) {
		try {
			return enemy.getAbility(scriptFn.apply(ScriptVar.toScriptVar(enemy)).getString());
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return null;
	}

}
