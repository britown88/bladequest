package bladequest.battleactions;

import android.util.Log;
import bladequest.bladescript.ParserException;
import bladequest.bladescript.ScriptVar;
import bladequest.combat.BattleEventBuilder;

public class bactScript extends DelegatingAction {

	ScriptVar script;
	
	public bactScript(ScriptVar script) {
		this.script = script;
	}

	@Override
	protected void buildEvents(BattleEventBuilder builder) {		
		try {
			script.apply(ScriptVar.toScriptVar(builder));
		} catch (ParserException e) {
			e.printStackTrace();
			Log.d("PARSER", e.what());
		}
	}
	
}
