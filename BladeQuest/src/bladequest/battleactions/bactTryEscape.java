package bladequest.battleactions;

import java.util.List;

import android.graphics.Point;
import android.graphics.Rect;
import bladequest.combat.Battle;
import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.BattleEventObject;
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

public class bactTryEscape  extends BattleAction {
	BattleEventBuilder eventBuilder;
	public bactTryEscape(int frame, BattleEventBuilder eventBuilder) {
		super(frame);
		this.eventBuilder = eventBuilder;
	}
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
		
		anim.addObject(baObj);
		
		return anim;
	}
	public void run(PlayerCharacter character, List<PlayerCharacter> target, List<DamageMarker> markers)
	{
		boolean disableRunning = Global.battle.getEncounter().disableRunning;
		
		int startAnimationTime = BattleEvent.frameFromActIndex(getFrame());
		
		if (!disableRunning && Global.rand.nextInt(100) < 90)
		{
			Global.battle.changeStartBarText("Got away safely!");
			BattleAnim runAnim = makeRunAnimation(character);
			int finishRun = startAnimationTime + runAnim.syncToAnimation(1.0f);
			eventBuilder.setAnimation(runAnim, getFrame());
			eventBuilder.addEventObject(new BattleEventObject(startAnimationTime, new bactChangeVisibility(getFrame(), false), character, target));
			eventBuilder.addEventObject(new BattleEventObject(finishRun, new bactChangeVisibility(getFrame(), true), character, target));			
			eventBuilder.addEventObject(new BattleEventObject(finishRun, new BattleAction(getFrame())
			{
				@Override
				public void run(PlayerCharacter character, List<PlayerCharacter> target, List<DamageMarker> markers)
				{
					//note that all events are ignored after a character has escaped!  set visibility before running! (order added is tie breaker)
					character.setEscaped(true);
				}
			}, character, target));
			eventBuilder.addEventObject(new BattleEventObject(finishRun));
		}
		else
		{
			if (disableRunning)
			{
				Global.battle.changeStartBarText("There is no escape!");
				eventBuilder.addEventObject(new BattleEventObject(startAnimationTime + BattleEvent.frameFromActIndex(3)));
			}
			else
			{
				Global.battle.changeStartBarText("Couldn't run!");
				eventBuilder.addEventObject(new BattleEventObject(startAnimationTime + BattleEvent.frameFromActIndex(3)));
			}
		}
	}
}
