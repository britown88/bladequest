package bladequest.actions;

import android.util.Log;
import bladequest.bladescript.ParserException;
import bladequest.bladescript.ScriptVar;
import bladequest.combat.BattleMusicListener;
import bladequest.world.Global;

public class actSetBattleMusic extends Action {

	ScriptVar onInit, onVictory, onBattleEnd;
	
	public actSetBattleMusic(ScriptVar onInit, 
							 ScriptVar onVictory, 
							 ScriptVar onBattleEnd) {
		this.onInit = onInit;
		this.onVictory = onVictory;
		this.onBattleEnd = onBattleEnd;
	}

	@Override
	public boolean isDone() {
		return true;
	}

	@Override
	public void run() {
		 Global.battleMusicListener = new BattleMusicListener()
		 {
			@Override
			public void onStart() {
				try {
					onInit.apply(ScriptVar.toScriptVar(Global.battle));
				} catch (ParserException e) {
					e.printStackTrace();
					Log.d("Parser", e.what());
				}
			}

			@Override
			public void onVictory() {
				try {
					onVictory.apply(ScriptVar.toScriptVar(Global.battle));
				} catch (ParserException e) {
					e.printStackTrace();
					Log.d("Parser", e.what());
				}
			}

			@Override
			public void onBattleEnd() {
				try {
					onBattleEnd.apply(ScriptVar.toScriptVar(Global.battle));
				} catch (ParserException e) {
					e.printStackTrace();
					Log.d("Parser", e.what());
				}
			}

			@Override
			public void onRemove() {
				//serialization bullshit
			}
		};
	}

}
