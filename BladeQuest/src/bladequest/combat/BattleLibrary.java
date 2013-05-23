package bladequest.combat;

import java.util.List;

import bladequest.battleactions.BattleAction;
import bladequest.battleactions.bactAttackClose;
import bladequest.battleactions.bactAttackRandomTargets;
import bladequest.battleactions.bactBreakStance;
import bladequest.battleactions.bactDamage;
import bladequest.battleactions.bactInflictStatus;
import bladequest.battleactions.bactRunAnimation;
import bladequest.battleactions.bactWait;
import bladequest.scripting.LibraryWriter;
import bladequest.scripting.Script.BadSpecialization;
import bladequest.scripting.ScriptVar.BadTypeException;
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
	public static void publishLibrary(LibraryWriter library) 
	{
		try {
			library.add("getEnemies", BattleLibrary.class.getMethod("getEnemies", Battle.class));
			library.add("getPlayer", BattleLibrary.class.getMethod("getParty", Battle.class));
			library.add("getIsInBattle", BattleLibrary.class.getMethod("getIsInBattle", PlayerCharacter.class));
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (BadTypeException e) {
		} catch (BadSpecialization e) {
		}
		publishActions(library);
	}
	
	public static Ability addAbility(String name, String displayName, String targetType, int mpcost)
	{
		//default outside of battle usability to false.
		Ability newAbility = new Ability(name, displayName, TargetTypes.valueOf(targetType), mpcost, false);
		Global.abilities.put(name, newAbility);
		return newAbility;
	}
	
	public static Ability setDescription(Ability ability, String description)
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
	
	
	public static Ability addAction(Ability ability, BattleAction action)
	{
		ability.addAction(action);
		return ability;
	}
	
	
	public static BattleAction inflictStatusAction(StatusEffect effect)
	{
		return new bactInflictStatus(effect);
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
	
	public static Ability addDepAction(Ability ability, BattleAction action)
	{
		BattleAction prev = ability.lastAction();
		ability.addAction(action);
		action.addDependency(prev);
		return ability; 
	}
	
	public static void publishActions(LibraryWriter library)
	{
		try {
			//basic create
			library.add("addAbility", BattleLibrary.class.getMethod("addAbility", String.class, String.class, String.class, int.class));
			library.add("setDesc", BattleLibrary.class.getMethod("setDescription", Ability.class, String.class));
			library.add("setDisabled", BattleLibrary.class.getMethod("setDisabled", Ability.class));
			library.add("useOutsideBattle", BattleLibrary.class.getMethod("useOutsideBattle", Ability.class));
			
			//action add
			library.add("add", BattleLibrary.class.getMethod("addAction", Ability.class, BattleAction.class));
			library.add("addDep", BattleLibrary.class.getMethod("addDepAction", Ability.class, BattleAction.class));
			
			//statuses
			library.add("poisonStatus", BattleLibrary.class.getMethod("poisonStatus", float.class));
			
			//actions
			library.add("damageAction", BattleLibrary.class.getMethod("damageAction", float.class, String.class));
			library.add("attackCloseAction", BattleLibrary.class.getMethod("attackCloseAction", float.class, String.class));
			library.add("attackRandomlyAction", BattleLibrary.class.getMethod("attackRandomlyAction", float.class, String.class, int.class, float.class));
			library.add("waitAction", BattleLibrary.class.getMethod("waitAction", int.class));
			library.add("animAction", BattleLibrary.class.getMethod("animAction", String.class));
			library.add("inflictStatusAction",  BattleLibrary.class.getMethod("inflictStatusAction", StatusEffect.class));
			library.add("breakStanceAction",  BattleLibrary.class.getMethod("breakStanceAction", int.class));
			
			//dependency
			library.add("addDependency", BattleLibrary.class.getMethod("addDependency", BattleAction.class, BattleAction.class));
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (BadTypeException e) {
		} catch (BadSpecialization e) {
		}		
	}
}
