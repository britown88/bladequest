package bladequest.statuseffects;

import bladequest.combat.Battle;
import bladequest.world.Character;

public class StatusEffect 
{
	protected String name, icon;
	protected boolean 
		negative,//negative status effects like poison
		removeOnDeath,
		curable;
	
	public StatusEffect(String name, Boolean negative)
	{
		this.name = name;
		this.negative = negative;
	}
	
	public String icon() { return icon; }	
	public boolean isNegative() { return negative; }
	public boolean removesOnDeath() { return removeOnDeath; }
	public boolean isCurable() { return curable; }
	public String Name() { return name; }
	
	public void onTurn(Character c, Battle b) {}
	public void onInflict(Character c) {}
	public void onRemove(Character c) {}
	public void onStep(Character c) {}	
	
	public String saveLine() { return ""; }

}
