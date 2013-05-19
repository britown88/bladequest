package bladequest.combatactions;

import java.util.ArrayList;
import java.util.List;

import bladequest.battleactions.bactDamage;
import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.BattleEventObject;
import bladequest.graphics.BattleSprite.faces;
import bladequest.world.DamageTypes;
import bladequest.world.PlayerCharacter;
import bladequest.world.TargetTypes;


public class combWish extends CombatAction 
{
	public combWish()
	{
		name = "Wish";
		type = DamageTypes.Magic;
		targetType = TargetTypes.AllAllies;
		
		actionText = " makes a wish!";
	}
	
	@Override
	public void buildEvents(BattleEventBuilder builder)
	{
		int animStartIndex = 3;
		PlayerCharacter source = builder.getSource();
		builder.addEventObject(new BattleEventObject(BattleEvent.frameFromActIndex(0), faces.Ready, 0, source));
		builder.addEventObject(new BattleEventObject(BattleEvent.frameFromActIndex(animStartIndex), faces.Cast, 0, source));
		
		int frame = animStartIndex+1;
		for (PlayerCharacter target : builder.getTargets())
		{
			List<PlayerCharacter> currentTarget = new ArrayList<PlayerCharacter>();
			currentTarget.add(target);
			builder.addEventObject(new BattleEventObject(BattleEvent.frameFromActIndex(frame), new bactDamage(frame, -100, DamageTypes.Fixed), source, currentTarget));
			frame += 2;
		}
		builder.addEventObject(new BattleEventObject(BattleEvent.frameFromActIndex(frame), faces.Ready, 0, source));
		builder.addEventObject(new BattleEventObject(BattleEvent.frameFromActIndex(frame+2)));
	}
}
