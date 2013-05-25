package bladequest.statuseffects;

import java.util.ArrayList;
import java.util.List;

import bladequest.battleactions.TargetedAction;
import bladequest.battleactions.bactDamage;
import bladequest.battleactions.bactRunAnimation;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.BattleAnim;
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
	
	@Override
	public void onTurn(BattleEventBuilder builder) 
	{
		PlayerCharacter healTarget =  builder.getSource();
		Global.battle.changeStartBarText(healTarget.getDisplayName() + " is regaining HP!");
		
		List<PlayerCharacter> healTargetList = new ArrayList<PlayerCharacter>();
		healTargetList.add(healTarget);
		
		BattleAnim anim = Global.battleAnims.get("poison");  //TODO: regeneration animation
		
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
			Global.playAnimation("poison", null, c.getPosition(true)); //TODO: regeneration animation
	}	
}
