package bladequest.combatactions;

import java.util.ArrayList;
import java.util.List;

import bladequest.battleactions.bactInflictStatus;
import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.BattleEventObject;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleSprite.faces;
import bladequest.statuseffects.seRegen;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;
import bladequest.world.TargetTypes;

public class combEmpowerLife extends CombatAction {
	public combEmpowerLife()
	{
		name = "Empower Life";
		type = DamageTypes.Magic;
		targetType = TargetTypes.AllAllies;
		
		actionText = " empowers the party!";
	}
	
	@Override
	public void buildEvents(BattleEventBuilder builder)
	{
		int animStartIndex = 3;
		PlayerCharacter source = builder.getSource();
		builder.addEventObject(new BattleEventObject(BattleEvent.frameFromActIndex(0), faces.Ready, 0, source));
		builder.addEventObject(new BattleEventObject(BattleEvent.frameFromActIndex(animStartIndex), faces.Cast, 0, source));
		
		int frame = animStartIndex;		
		int endFrameTime =  BattleEvent.frameFromActIndex(frame);
		for (PlayerCharacter target : builder.getTargets())
		{
			BattleAnim anim = Global.battleAnims.get("movetest");
			endFrameTime = anim.syncToAnimation(1.0f) + BattleEvent.frameFromActIndex(frame);
			List<PlayerCharacter> currentTarget = new ArrayList<PlayerCharacter>();
			currentTarget.add(target);
			builder.addEventObject(new BattleEventObject(BattleEvent.frameFromActIndex(frame), anim, source, currentTarget));
			builder.addEventObject(new BattleEventObject(endFrameTime, new bactInflictStatus(frame, true, new seRegen(10,20, 3)), source, currentTarget));
			frame += 4;
		}
		builder.addEventObject(new BattleEventObject(BattleEvent.frameFromActIndex(frame), faces.Ready, 0, source));
		builder.addEventObject(new BattleEventObject(endFrameTime+BattleEvent.frameFromActIndex(2)));
	}
}
