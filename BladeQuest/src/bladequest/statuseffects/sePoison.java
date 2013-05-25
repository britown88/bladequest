package bladequest.statuseffects;

import bladequest.battleactions.TargetedAction;
import bladequest.battleactions.bactDamage;
import bladequest.battleactions.bactRunAnimation;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.BattleAnim;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;
import bladequest.world.States;
import bladequest.world.Stats;

public class sePoison extends StatusEffect
{
	float power;
	int stepCount;
	
	public sePoison(float power)
	{
		super("poison", true);
		this.power = power;
		icon = "poison";
		curable = true;
		removeOnDeath = true;
		battleOnly = false;
		hidden = false;
		stepCount = 0;
	}
	@Override
	public String saveLine() 
	{ 
		return "status " + sePoison.class.getSimpleName() + " " + power; 
	}
	
	@Override
	public void onTurn(BattleEventBuilder builder) 
	{
		//set text, play animation damage, return.
		PlayerCharacter damageTarget =  builder.getSource();
		Global.battle.changeStartBarText(damageTarget.getDisplayName() + " is damaged by poison!");
		BattleAnim anim = Global.battleAnims.get("poison");
		
		int damage = Math.max(1, (int)((float)damageTarget.getStat(Stats.MaxHP)*(power/100.0f)));
		
		builder.addEventObject(new TargetedAction(damageTarget)
		{
			int damage;
			BattleAnim anim;
			TargetedAction initialize(int damage, BattleAnim anim)
			{
				this.anim = anim;
				this.damage = damage;
				return this;
			}
			@Override
			protected void buildEvents(BattleEventBuilder builder) {
				builder.addEventObject(new bactRunAnimation(anim));
				builder.addEventObject(new bactDamage(damage, DamageTypes.Fixed));	
			}
		}.initialize(damage, anim));
	}
	
	@Override
	public void onInflict(PlayerCharacter c) 
	{
		if(Global.GameState == States.GS_BATTLE)
			Global.playAnimation("poison", null, c.getPosition(true));
	}
	
	@Override
	public void onRemove(PlayerCharacter c) {}
	
	@Override
	public void onStep(PlayerCharacter c) 
	{
		stepCount++;
		
		if(stepCount % 3 == 0)
		{
			if(!c.isDead())
			{
				c.modifyHP(-power/10.0f, true);
				Global.screenShaker.shake(3, 0.1f, true);
				Global.screenFader.setFadeColor(64, 34, 177, 76);
				Global.screenFader.flash(5);
				
				if(c.isDead())
				{
					Global.showMessage(c.getDisplayName() + " succumbed to poison and fell unconscious.", false);
					Global.party.clearMovementPath();
				}
			}
			
		}
			
	}	

}
