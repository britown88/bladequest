package bladequest.battleactions;

import android.graphics.Point;
import android.graphics.Rect;
import bladequest.combat.Battle;
import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.DamageMarker;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleAnimObjState;
import bladequest.graphics.BattleAnimObjState.PosTypes;
import bladequest.graphics.BattleAnimObject;
import bladequest.graphics.BattleAnimObject.Types;
import bladequest.graphics.BattleSprite;
import bladequest.graphics.BattleSprite.faces;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class bactTryEscape  extends DelegatingAction {
	
	private BattleAnim makeRunAnimation(PlayerCharacter character)
	{
		BattleAnim anim = new BattleAnim(60.0f);
		
		BattleSprite playerSprite = character.getBattleSprite();
		
		BattleAnimObject baObj = new BattleAnimObject(Types.Bitmap, false, playerSprite.getBmpName());
		Rect srcRect = playerSprite.getFrameRect(faces.Ready, 0);
		
        BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Source);
		
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = new Point(0, 0);
		state.size = new Point(playerSprite.getWidth(), playerSprite.getHeight());
		baObj.addState(state);		
		
        state = new BattleAnimObjState(8, PosTypes.Source);
		
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = new Point((Global.vpWidth-Battle.partyPos.x-Battle.advanceDistance), 0);
		state.size = new Point(playerSprite.getWidth(), playerSprite.getHeight());
		baObj.addState(state);		
		
		baObj.interpolateLinearly();
		
		anim.addObject(baObj);
		
		return anim;
	}
	@Override
	protected void buildEvents(BattleEventBuilder builder) {
		boolean disableRunning = Global.battle.getEncounter().disableRunning;
		
		if (!disableRunning && Global.rand.nextInt(100) < 90)
		{
			Global.battle.changeStartBarText("Got away safely!");
			BattleAnim runAnim = makeRunAnimation(builder.getSource());
			
			builder.addEventObject(new bactChangeVisibility(false));
			builder.addEventObject(new bactRunAnimation(runAnim));			
			builder.addEventObject(new BattleAction()
			{
				@Override
				public State run(BattleEventBuilder builder)
				{
					PlayerCharacter character = builder.getSource();
					character.setEscaped(true);
					character.setVisible(true);
					return State.Finished;
				}
			}.addDependency(builder.getLast()));
		}
		else
		{
			if (disableRunning)
			{
				Global.battle.changeStartBarText("There is no escape!");
			}
			else
			{
				Global.battle.changeStartBarText("Couldn't run!");
			}
			builder.addEventObject(new bactWait(BattleEvent.frameFromActIndex(3)));
			builder.addMarker(new DamageMarker("FAILED!", builder.getSource()));
		}
	}
}
