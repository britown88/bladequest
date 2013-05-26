package bladequest.statuseffects;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.graphics.Rect;
import bladequest.battleactions.TargetedAction;
import bladequest.battleactions.bactDamage;
import bladequest.battleactions.bactRunAnimation;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleAnimObjState;
import bladequest.graphics.BattleAnimObjState.PosTypes;
import bladequest.graphics.BattleAnimObject;
import bladequest.graphics.BattleAnimObject.Types;
import bladequest.graphics.BitmapFrame;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;
import bladequest.world.States;

public class seRegen extends StatusEffect {
	int minHeal;
	int maxHeal;
	int duration;
	public seRegen(int minHeal, int maxHeal, int duration) {
		super("Regen", false);
		icon = "orb"; 
		curable = false;
		removeOnDeath = true;
		hidden = false;
		battleOnly = true;
		this.minHeal = minHeal;
		this.maxHeal = maxHeal;
		this.duration = duration;
	}
	static BattleAnim makeSparkleFairyBullshit(int particleCount)
	{
		BitmapFrame[] frames = Global.getSparkleParticle().getFrames();
		BattleAnim out = new BattleAnim(60.0f);
		
		int particleDelay = 2;
		int sparkleDelay = 12;
		int fallRate = 16;
		for (int j = 0; j < particleCount; ++j) 
		{
			BattleAnimObject sparkle = new BattleAnimObject(Types.Bitmap, false, frames[0].bitmap);
			
			int xOffset = Global.rand.nextInt(36) - 18;
			int yOffset =  Global.rand.nextInt(8) - 32;
			
			for(int i = 0; i < frames.length; ++i)
			{
				BattleAnimObjState state = new BattleAnimObjState(j*particleDelay + i * sparkleDelay, PosTypes.Target);
				state.size = new Point((int)(frames[i].srcRect.width() * 1.5f), (int)(frames[i].srcRect.height() * 1.5f));
				state.pos1 = new Point(xOffset,yOffset+(fallRate * i));
				state.argb(255 - ((i * 255) / (frames.length)), 0, 255, 0);
				state.colorize = 1.0f;
				Rect r = frames[i].srcRect;
				state.setBmpSrcRect(r.left, r.top, r.right, r.bottom);
				sparkle.addState(state);
			}
			out.addObject(sparkle);
		}
		return out;
	}
	@Override
	public void onTurn(BattleEventBuilder builder) 
	{
		PlayerCharacter healTarget =  builder.getSource();
		Global.battle.setInfoBarText(healTarget.getDisplayName() + " is regaining HP!");
		
		List<PlayerCharacter> healTargetList = new ArrayList<PlayerCharacter>();
		healTargetList.add(healTarget);
		
		BattleAnim anim = makeSparkleFairyBullshit(25);
		
		int healAmnt = Global.rand.nextInt(maxHeal-minHeal) + minHeal;
		
		builder.addEventObject(new TargetedAction(healTarget)
		{
			BattleAnim anim;
			int healAmnt;
			TargetedAction initialize(int healAmnt, BattleAnim anim)
			{
				this.healAmnt = healAmnt;
				this.anim = anim;
				return this;
			}
			@Override
			protected void buildEvents(BattleEventBuilder builder) {
				builder.addEventObject(new bactRunAnimation(anim));
				builder.addEventObject(new bactDamage(-healAmnt, DamageTypes.Fixed));	
			}
		}.initialize(healAmnt, anim));
				
		
		
		if (duration > 0 && --duration == 0)
		{
			healTarget.removeStatusEffect(name);
		}
	}
	@Override
	public void onInflict(PlayerCharacter c) 
	{
		if(Global.GameState == States.GS_BATTLE)
			Global.playAnimation(makeSparkleFairyBullshit(25), null, c.getPosition(true)); //TODO: regeneration animation
	}	
}
