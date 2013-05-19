package bladequest.statuseffects;

import java.util.ArrayList;
import java.util.List;

import bladequest.battleactions.bactDamage;
import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.BattleEventObject;
import bladequest.graphics.BattleAnim;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;
import bladequest.world.States;

public class seRegen extends StatusEffect {
	int minHeal;
	int maxHeal;
	public seRegen(int minHeal, int maxHeal) {
		super("Regen", false);
		icon = "poison"; //deal with it nerds
		curable = false;
		removeOnDeath = true;
		battleOnly = true;
		this.minHeal = minHeal;
		this.maxHeal = maxHeal;
	}
	
	@Override
	public void onTurn(BattleEventBuilder eventBuilder) 
	{
		PlayerCharacter healTarget =  eventBuilder.getSource();
		Global.battle.changeStartBarText(healTarget.getDisplayName() + " is regaining HP!");
		
		List<PlayerCharacter> healTargetList = new ArrayList<PlayerCharacter>();
		healTargetList.add(healTarget);
		
		int animFrameIndex = 3;
		BattleAnim anim = Global.battleAnims.get("poison");  //TODO: regeneration animation
		
		int healAmnt = Global.rand.nextInt(maxHeal-minHeal) + minHeal;
		
		int startAnimTime = BattleEvent.frameFromActIndex(animFrameIndex);
		int endAnimTime = anim.syncToAnimation(1.0f) + startAnimTime; 		
		eventBuilder.addEventObject(new BattleEventObject(startAnimTime, anim, healTarget, healTargetList));
		eventBuilder.addEventObject(new BattleEventObject(endAnimTime, new bactDamage(endAnimTime, -healAmnt, DamageTypes.Fixed), healTarget, healTargetList));
		eventBuilder.addEventObject(new BattleEventObject(endAnimTime));
	}
	@Override
	public void onInflict(PlayerCharacter c) 
	{
		if(Global.GameState == States.GS_BATTLE)
			Global.playAnimation("poison", null, c.getPosition(true)); //TODO: regeneration animation
	}	
}
