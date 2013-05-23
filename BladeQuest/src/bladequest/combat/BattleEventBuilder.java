package bladequest.combat;

import java.util.List;

import bladequest.battleactions.BattleAction;
import bladequest.world.PlayerCharacter;

public interface BattleEventBuilder {
 List<PlayerCharacter> getTargets();
 PlayerCharacter getSource();
 
 BattleAction getLast();
 
 void addEventObject(BattleAction eventObj);
 void addMarker(DamageMarker marker);
 int getCurrentBattleFrame();
}