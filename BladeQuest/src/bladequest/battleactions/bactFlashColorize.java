package bladequest.battleactions;

import android.graphics.Point;
import android.graphics.Rect;
import bladequest.battleactions.BattleAction.State;
import bladequest.combat.Battle;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleAnimObjState;
import bladequest.graphics.BattleAnimObject;
import bladequest.graphics.BattleSprite;
import bladequest.graphics.BattleAnimObjState.PosTypes;
import bladequest.graphics.BattleAnimObject.Types;
import bladequest.graphics.BattleSprite.faces;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class bactFlashColorize extends DelegatingAction {
	
	int time;
	int a,r,g,b;
	float factor;
	
	private BattleAnim makeColorizeAnim(PlayerCharacter character)
	{
		BattleAnim anim = new BattleAnim(1000.0f);  //lol milliseconds
		
		BattleSprite playerSprite = character.getBattleSprite();
		
		BattleAnimObject baObj = new BattleAnimObject(Types.Bitmap, false, playerSprite.getBmpName());
		Rect srcRect = playerSprite.getFrameRect(faces.Ready, 0);
		
        BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Source);
		
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = new Point(0, 0);
		state.size = new Point(playerSprite.getWidth(), playerSprite.getHeight());
		baObj.addState(state);		
		
        state = new BattleAnimObjState(time/2, PosTypes.Source);
		
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(a, r, g, b);
		state.colorize = factor;
		state.pos1 = new Point(0, 0);
		state.size = new Point(playerSprite.getWidth(), playerSprite.getHeight());
		baObj.addState(state);		
		
		state = new BattleAnimObjState(time, PosTypes.Source);

		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = new Point(0, 0);
		state.size = new Point(playerSprite.getWidth(), playerSprite.getHeight());
		baObj.addState(state);		
		
		anim.addObject(baObj);
		
		return anim;
	}
	
	public bactFlashColorize(int a, int r, int g, int b, int totalMS, float colorizeFactor)
	{
		this.time = totalMS;
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
		this.factor = colorizeFactor;
	}

	@Override
	protected void buildEvents(BattleEventBuilder builder) {
		builder.addEventObject(new bactRunAnimation(makeColorizeAnim(builder.getSource())));
	}
}
