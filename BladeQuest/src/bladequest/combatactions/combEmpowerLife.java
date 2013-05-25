package bladequest.combatactions;

import bladequest.battleactions.BattleAction;
import bladequest.battleactions.TargetedAction;
import bladequest.battleactions.bactInflictStatus;
import bladequest.battleactions.bactRunAnimation;
import bladequest.battleactions.bactSetFace;
import bladequest.battleactions.bactWait;
import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
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
	public String getDescription() { return "Cause your party to regenerate health over a few turns.";}
	
	@Override
	public void buildEvents(BattleEventBuilder builder)
	{
		builder.addEventObject(new bactSetFace(faces.Cast, 0));
		
		BattleAction prevWait = null;
		BattleAnim anim = Global.battleAnims.get("movetest");
		
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
					builder.addEventObject(new bactInflictStatus(new seRegen(10, 20, 3)).addDependency(builder.getLast()));
				}

			}.initialize(anim).addDependency(builder.getLast()));
			
			builder.addEventObject(new bactWait(BattleEvent.frameFromActIndex(4)).addDependency(prevWait));
			prevWait = builder.getLast();			
		}
		builder.addEventObject(new bactSetFace(faces.Ready, 0).addDependency(builder.getLast()));
		builder.addEventObject(new bactWait(BattleEvent.frameFromActIndex(2)).addDependency(prevWait));
	}
}
