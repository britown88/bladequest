package bladequest.combatactions;

import bladequest.UI.ListBox;
import bladequest.UI.MenuPanel;
import bladequest.combat.Battle;
import bladequest.combat.BattleStateMachine;
import bladequest.world.PlayerCharacter;

public interface CombatActionBuilder {
	ListBox getMenu();
	MenuPanel getMPWindow();
	PlayerCharacter getUser();
	Battle getBattle();
	BattleStateMachine getStateMachine();
}
