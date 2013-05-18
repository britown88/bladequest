package bladequest.statuseffects;

import bladequest.combat.Battle;
import bladequest.world.PlayerCharacter;
import bladequest.world.Global;
import bladequest.world.States;

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
		stepCount = 0;
	}
	@Override
	public String saveLine() 
	{ 
		return "status " + sePoison.class.getSimpleName() + " " + power; 
	}
	
	@Override
	public void onTurn(PlayerCharacter c, Battle b) 
	{
		//b.applyDamage(c, -Math.max(1, (int)((float)c.getStat(Stats.MaxHP)*(power/100.0f))), 50);
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
