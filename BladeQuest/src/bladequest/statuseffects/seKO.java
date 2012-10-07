package bladequest.statuseffects;

import bladequest.combat.Battle;
import bladequest.world.Character;

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
	public void onTurn(Character c, Battle b) {}
	
	@Override
	public void onInflict(Character c) {}
	
	@Override
	public void onRemove(Character c) {}
	
	@Override
	public void onStep(Character c) {}	

}
