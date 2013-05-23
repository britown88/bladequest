package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.BattleSprite.faces;
import bladequest.world.PlayerCharacter;

public class bactSetFace extends BattleAction {

	faces newFace;
	int frame;
	
	public bactSetFace(faces newFace, int frame) {
		this.newFace = newFace;
		this.frame = frame;
	}
	
	@Override
	public State run(BattleEventBuilder builder)
	{
		PlayerCharacter toUpdate = builder.getSource();
		if (toUpdate.isEnemy()) //hurr
		{
			return State.Finished;
		}
		toUpdate.setFace(newFace);
		toUpdate.setImageIndex(frame);
		return State.Finished;
	}
	
}
