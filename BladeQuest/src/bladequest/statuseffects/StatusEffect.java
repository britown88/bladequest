package bladequest.statuseffects;

import java.util.ArrayList;
import java.util.List;

import bladequest.combat.Battle;
import bladequest.combat.BattleEventBuilder;
import bladequest.world.PlayerCharacter;

public class StatusEffect 
{
	protected String name, icon;
	protected boolean 
		negative,//negative status effects like poison
		removeOnDeath,
		curable,
		battleOnly;  //should remove at the end of battle.
	
	public StatusEffect(String name, Boolean negative)
	{
		this.name = name;
		this.negative = negative;
	}
	
	public String icon() { return icon; }	
	public boolean isNegative() { return negative; }
	public boolean removesOnDeath() { return removeOnDeath; }
	public boolean isCurable() { return curable; }
	public boolean isBattleOnly() {return battleOnly;}
	public String Name() { return name; }
	
	public void onTurn(BattleEventBuilder eventBuilder) {}
	public void onInflict(PlayerCharacter c) {}
	public void onRemove(PlayerCharacter c) {}
	public void onStep(PlayerCharacter c) {}	
	
	public String saveLine() { return ""; }
	
	public interface Filter
	{
		boolean filter(StatusEffect effect);
	}
	public static List<StatusEffect> filterList(List<StatusEffect> statusEffects, Filter filter)
	{
		List<StatusEffect> out = new ArrayList<StatusEffect>();
		for (StatusEffect effect : statusEffects)
		{
			if (filter.filter(effect))
			{
				out.add(effect);
			}
		}
		return out;
	}
}
