package bladequest.combat;

import java.util.List;

import bladequest.battleactions.BattleAction;
import bladequest.battleactions.bactAttackClose;
import bladequest.battleactions.bactAttackRandomTargets;
import bladequest.battleactions.bactBreakStance;
import bladequest.battleactions.bactDamage;
import bladequest.battleactions.bactInflictStatus;
import bladequest.battleactions.bactRemoveStatus;
import bladequest.battleactions.bactRunAnimation;
import bladequest.battleactions.bactWait;
import bladequest.scripting.LibraryWriter;
import bladequest.statuseffects.StatusEffect;
import bladequest.statuseffects.sePoison;
import bladequest.world.Ability;
import bladequest.world.DamageTypes;
import bladequest.world.Enemy;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;
import bladequest.world.TargetTypes;

public class BattleLibrary {

	public static List<Enemy> getEnemies(Battle battle)
	{
		return battle.getEncounter().Enemies();
	}
	public static List<PlayerCharacter> getParty(Battle battle)
	{
		return battle.getParty();
	}
	public static boolean getIsInBattle(PlayerCharacter p)
	{
		return p.isInBattle();
	}	

	
	public static Ability addAbility(String name, String displayName, String targetType, int mpcost)
	{
		//default outside of battle usability to false.
		Ability newAbility = new Ability(name, displayName, TargetTypes.valueOf(targetType), mpcost, false);
		Global.abilities.put(name, newAbility);
		return newAbility;
	}
	
	public static Ability setDesc(Ability ability, String description)
	{
		ability.setDescription(description);
		return ability;
	}
	
	public static Ability useOutsideBattle(Ability ability)
	{
		ability.makeUsableOutOfBattle();
		return ability;
	}	
	public static Ability setDisabled(Ability ability)
	{
		ability.setEnabled(false);
		return ability;
	}	
	
	
	public static StatusEffect poisonStatus(float power)
	{
		return new sePoison(power);
	}
	
	
	public static Ability add(Ability ability, BattleAction action)
	{
		ability.addAction(action);
		return ability;
	}
	
	
	public static BattleAction inflictStatusAction(StatusEffect effect)
	{
		return new bactInflictStatus(effect);
	}	
	public static BattleAction removeStatusAction(String statusName)
	{
		return new bactRemoveStatus(statusName);
	}	

	
	public static BattleAction attackCloseAction(float power, String damageType)
	{
		return new bactAttackClose(power, DamageTypes.valueOf(damageType));
	}
	
	public static BattleAction attackRandomlyAction(float power, String damageType, int attacks, float speedFactor)
	{
		return new bactAttackRandomTargets(power, DamageTypes.valueOf(damageType), attacks, speedFactor);
	}
	
	public static BattleAction damageAction(float power, String damageType)
	{
		return new bactDamage(power, DamageTypes.valueOf(damageType));
	}
	
	public static BattleAction waitAction(int milliseconds)
	{
		return new bactWait(milliseconds);
	}
	
	public static BattleAction breakStanceAction(int placeholder)
	{
		return new bactBreakStance();
	}
	
	public static BattleAction animAction(String animName)
	{
		return new bactRunAnimation(Global.battleAnims.get(animName));
	}	
	
	public static BattleAction addDependency(BattleAction child, BattleAction parent)
	{
		return child.addDependency(parent);
	}
	
	public static Ability addDep(Ability ability, BattleAction action)
	{
		BattleAction prev = ability.lastAction();
		ability.addAction(action);
		action.addDependency(prev);
		return ability; 
	}
	
	
	public static void publishLibrary(LibraryWriter library) 
	{
		try {
			library.addAllIn(BattleLibrary.class);
		} catch (Exception e) {
		}
	}
}
