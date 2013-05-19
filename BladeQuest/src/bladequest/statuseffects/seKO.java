package bladequest.statuseffects;

import bladequest.combat.Battle;
import bladequest.combat.BattleEventBuilder;
import bladequest.world.PlayerCharacter;

public class seKO extends StatusEffect
{	
	public seKO()
	{
		super("KO", true);
		icon = "KO";
		hidden = false;
		curable = false;
		removeOnDeath = false;
		battleOnly = false;
	}
	@Override
	public String saveLine() 
	{ 
		return "status " + seKO.class.getSimpleName(); 
	}
	
	@Override
	public void onTurn(BattleEventBuilder eventBuilder) {}
	
	@Override
	public void onInflict(PlayerCharacter c) {}
	
	@Override
	public void onRemove(PlayerCharacter c) {}
	
	@Override
	public void onStep(PlayerCharacter c) {}	

}
