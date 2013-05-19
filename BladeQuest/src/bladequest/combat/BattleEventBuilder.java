package bladequest.combat;

import java.util.List;

import bladequest.graphics.BattleAnim;
import bladequest.world.PlayerCharacter;

public interface BattleEventBuilder {
 List<PlayerCharacter> getTargets();
 PlayerCharacter getSource();
 
 void setAnimation(BattleAnim anim, int frameOffset);
 void addEventObject(BattleEventObject eventObj);
}
