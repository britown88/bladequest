package bladequest.combatactions;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import bladequest.battleactions.BattleAction;
import bladequest.battleactions.TargetedAction;
import bladequest.battleactions.bactChangeVisibility;
import bladequest.battleactions.bactFlashColorize;
import bladequest.battleactions.bactRunAnimation;
import bladequest.battleactions.bactSetFace;
import bladequest.battleactions.bactWait;
import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleAnimObjState;
import bladequest.graphics.BattleAnimObjState.PosTypes;
import bladequest.graphics.BattleAnimObject;
import bladequest.graphics.BattleAnimObject.Types;
import bladequest.graphics.BattleSprite.faces;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;
import bladequest.world.TargetTypes;

public class combEmpowerFire extends CombatAction {
	public combEmpowerFire()
	{
		name = "Empower Fire";
		type = DamageTypes.Magic;
		targetType = TargetTypes.AllAllies;
		
		actionText = " empowers the party!";
	}
	
	@Override
	public String getDescription() { return "Cause your party to gain fire affinity.";}
	
	BattleAnim buildLaserAnim()
	{
		BattleAnim out = new BattleAnim(1000.0f);
		//LASER PARTY
		Bitmap map = Global.bitmaps.get("animsprites");
		BattleAnimObject laser = new BattleAnimObject(Types.Bitmap, false, map);
		Rect laserRect = new Rect(99, 49, 99 + 24, 49 + 8);		
//		BattleAnimObjState laserInit = new BattleAnimObjState(0, PosTypes.Target);
//		laserInit.size = new Point((int)(128 * 2), (int)(16));
//		laserInit.pos1 = new Point(0,-464);
//		laserInit.argb(255,255,0,0);
//		laserInit.colorize = 1.0f;
//		laserInit.rotation = 90.0f;

//		laserInit.setBmpSrcRect(laserRect.left, laserRect.top, laserRect.right, laserRect.bottom);
//		laser.addState(laserInit);
		
		BattleAnimObjState laserFinish = new BattleAnimObjState(0, PosTypes.Target);
		laserFinish.size = new Point((int)(128), (int)(16));
		laserFinish.pos1 = new Point(0,-300);
		laserFinish.argb(255,255,0,0);
		laserFinish.colorize = 1.0f;
		laserFinish.rotation = 90.0f;
		laserFinish.setBmpSrcRect(laserRect.left, laserRect.top, laserRect.right, laserRect.bottom);
		laser.addState(laserFinish);
		
		
		BattleAnimObjState laserShorten = new BattleAnimObjState(500, PosTypes.Target);
		laserShorten.size = new Point(8, (int)(16));
		laserShorten.pos1 = new Point(0,0);
		laserShorten.argb(196,255,64,0);
		laserShorten.colorize = 1.0f;
		laserShorten.rotation = 90.0f;
		laserShorten.setBmpSrcRect(laserRect.left, laserRect.top, laserRect.right, laserRect.bottom);
		laser.addState(laserShorten);			
		
		out.addObject(laser);
		
		return out;
	}
	
	@Override
	public void buildEvents(BattleEventBuilder builder)
	{
		builder.addEventObject(new bactSetFace(faces.Cast, 0));
		
		BattleAction prevWait = null;
		BattleAnim anim = buildLaserAnim();
		
		for (PlayerCharacter target : builder.getTargets())
		{
			builder.addEventObject(new TargetedAction(target)
			{
				BattleAnim anim;
				TargetedAction initialize(BattleAnim anim)
				{
					this.anim = anim;
					return this;
				}
				@Override
				protected void buildEvents(BattleEventBuilder builder) {
					builder.addEventObject(new bactRunAnimation(anim));
					builder.addEventObject(new bactChangeVisibility(false).addDependency(builder.getLast()));
					builder.addEventObject(new bactFlashColorize(255,255,0,0,450,1.0f).addDependency(builder.getLast()));
					builder.addEventObject(new bactChangeVisibility(true).addDependency(builder.getLast()));
					builder.addEventObject(new bactRunAnimation(Global.battleAnims.get("fire")).addDependency(builder.getLast()));
					//TODO: fire affinity status buff.
				}
				protected BattleEventBuilder getAdaptedBuilder(BattleEventBuilder builder)
				{
					if (targets.isEmpty()) return super.getAdaptedBuilder(builder);
					return changeSource(super.getAdaptedBuilder(builder), targets.get(0));
				}

			}.initialize(anim).addDependency(builder.getLast()));
			
			builder.addEventObject(new bactWait(BattleEvent.frameFromActIndex(4)).addDependency(prevWait));
			prevWait = builder.getLast();			
		}
		builder.addEventObject(new bactSetFace(faces.Ready, 0).addDependency(builder.getLast()));
		builder.addEventObject(new bactWait(BattleEvent.frameFromActIndex(2)).addDependency(prevWait));
	}
}
