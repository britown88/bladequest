package bladequest.statuseffects;

import bladequest.combat.Battle;
import bladequest.world.PlayerCharacter;

public class seKO extends StatusEffect
{	
	public seKO()
	{
		super("KO", true);
		icon = "KO";
		curable = false;
		removeOnDeath = false;
	}
	@Override
	public String saveLine() 
	{ 
		return "status " + seKO.class.getSimpleName(); 
	}
	
	@Override
	public void onTurn(PlayerCharacter c, Battle b) {}
	
	@Override
	public void onInflict(PlayerCharacter c) {}
	
	@Override
	public void onRemove(PlayerCharacter c) {}
	
	@Override
	public void onStep(PlayerCharacter c) {}	

}
