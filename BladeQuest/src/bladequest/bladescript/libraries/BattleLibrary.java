package bladequest.bladescript.libraries;

import java.util.ArrayList;
import java.util.List;

import bladequest.battleactions.BattleAction;
import bladequest.battleactions.bactAttackClose;
import bladequest.battleactions.bactAttackRandomTargets;
import bladequest.battleactions.bactBasicAttack;
import bladequest.battleactions.bactBreakStance;
import bladequest.battleactions.bactDamage;
import bladequest.battleactions.bactFullRestore;
import bladequest.battleactions.bactInflictStatus;
import bladequest.battleactions.bactJumpHome;
import bladequest.battleactions.bactMessage;
import bladequest.battleactions.bactMirror;
import bladequest.battleactions.bactRemoveStatus;
import bladequest.battleactions.bactRunAnimation;
import bladequest.battleactions.bactRunAnimationBuilder;
import bladequest.battleactions.bactSetFace;
import bladequest.battleactions.bactShatter;
import bladequest.battleactions.bactSneakToTarget;
import bladequest.battleactions.bactSpecialMirrored;
import bladequest.battleactions.bactSpecialPosition;
import bladequest.battleactions.bactTargetingSelf;
import bladequest.battleactions.bactWait;
import bladequest.bladescript.LibraryWriter;
import bladequest.bladescript.ParserException;
import bladequest.bladescript.ScriptVar;
import bladequest.bladescript.ScriptVar.BadTypeException;
import bladequest.combat.Battle;
import bladequest.combat.BattleCalc;
import bladequest.combat.triggers.Condition;
import bladequest.combat.triggers.Trigger;
import bladequest.enemy.Enemy;
import bladequest.graphics.BattleSprite.faces;
import bladequest.statuseffects.StatusEffect;
import bladequest.statuseffects.seConfuse;
import bladequest.statuseffects.seFrozen;
import bladequest.statuseffects.sePoison;
import bladequest.world.Ability;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.Item;
import bladequest.world.PlayerCharacter;
import bladequest.world.Stats;
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
	public static Ability setShortDescription(Ability ability, String desc)
	{
		ability.setShortDescription(desc);
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
	
	public static StatusEffect confuseStatus(int duration)
	{
		return new seConfuse(duration);
	}
	
	public static StatusEffect frozenStatus(int duration)
	{
		return new seFrozen(duration);
	}
	
	
	
	public static Ability add(Ability ability, BattleAction action)
	{
		ability.addAction(action);
		return ability;
	}
	
	
	//just for Roland's "Shatter"
	public static BattleAction rolandSpecialShatterAction(ScriptVar ignored)
	{
		return new bactShatter();
	}
	
	public static BattleAction positionSpecialAction(boolean isSpecial)
	{
		return new bactSpecialPosition(isSpecial);
	}
	
	public static BattleAction sneakToAction(ScriptVar ignored)
	{
		return new bactSneakToTarget();
	}
	
	//anim time out off 60fps in 6 actions, so 10 == 1000ms.
	public static BattleAction jumpHomeAction(float invArc, int animTime)
	{
		return new bactJumpHome(invArc, animTime);
	}
	
	public static BattleAction inflictStatusAction(StatusEffect effect)
	{
		return new bactInflictStatus(effect);
	}	
	public static BattleAction removeStatusAction(String statusName)
	{
		return new bactRemoveStatus(statusName);
	}
	public static BattleAction fullRestore(int i)
	{
		return new bactFullRestore();
	}
	
	public static BattleAction messageAction(String message)
	{
		return new bactMessage(message);
	}
	
	public static BattleAction setFaceAction(String face, int index)
	{
		return new bactSetFace(faces.valueOf(face), index);
	}

	public static BattleAction targetSelfAction(BattleAction retarget)
	{
		return new bactTargetingSelf(retarget);
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
	
	//misschance is a percentage!
	public static BattleAction damageWithAccuracyAction(float power, String damageType, String accuracyType, float missChance)
	{
		return new bactDamage(power, DamageTypes.valueOf(damageType), BattleCalc.AccuracyType.valueOf(accuracyType), missChance);
	}	
	public static BattleAction basicAttackAction(float power, String damageType, float speedFactor)
	{
		return new bactBasicAttack(power, DamageTypes.valueOf(damageType), speedFactor);
	}
	
	//Function takes Target character and returns bool.
	public static BattleAction conditionalAttackAction(float power, String damageType, float speedFactor, ScriptVar function)
	{
		return new bactBasicAttack(power, DamageTypes.valueOf(damageType), speedFactor, function);
	}
		
	
	public static BattleAction mirrorAction(boolean mirrored)
	{
		return new bactMirror(mirrored);
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
	public static BattleAction animBuildAction(String animName)
	{
		return new bactRunAnimationBuilder(Global.animationBuilders.get(animName));
	}		
	
	public static BattleAction addDependency(BattleAction child, BattleAction parent)
	{
		return child.addDependency(parent);
	}
	
	public static BattleAction addDamageComponent(BattleAction child, String affinity, float factor)
	{
		((bactDamage)child).addDamageComponent(Stats.valueOf(affinity), factor);

		return child;
	}
	
	public static BattleAction specialMirroringAction(boolean mirrored)
	{
		return new bactSpecialMirrored(mirrored);
	}
	
	public static Ability addDep(Ability ability, BattleAction action)
	{
		BattleAction prev = ability.lastAction();
		ability.addAction(action);
		action.addDependency(prev);
		return ability; 
	}
	
	//null output
	public static int interruptCurrent(ScriptVar ignored)
	{
		Global.battle.interruptEvent();
		return 0;
	}
	//null output.
	//targets can be either a single target or a list.
	public static int addInterruptingAbility(PlayerCharacter source, ScriptVar targets, Ability ability)
	{
		List<PlayerCharacter> characters = new ArrayList<PlayerCharacter>();
		ScriptVar.listFromSingleOrList(characters, targets); 
		
		Global.battle.forceAddAbilityEvent(source, characters, ability);
		return 0;
	}
	
	public static PlayerCharacter getCurrentBattleActor(ScriptVar ignored)
	{
		return Global.battle.getCurrentActor();
	}
	
	//conditionList is a list of conditions/events OR a single condition/event.
	public static Trigger createTrigger(ScriptVar conditionList, ScriptVar onTrigger)
	{
		List<Condition> conditions = new ArrayList<Condition>();
		try {		
			if (conditionList.isOpaque()) //single condition...
			{
					conditions.add((Condition)conditionList.getOpaque());
			}
			else
			{
				while (!conditionList.isEmptyList())
				{
					conditions.add((Condition)conditionList.head().getOpaque());
					conditionList = conditionList.tail();
				}
			}
		} catch (BadTypeException e) {
			e.printStackTrace();
		}		
		
		return new Trigger(conditions)
		{
			ScriptVar onTrigger;
			Trigger initialize(ScriptVar onTrigger)
			{
				this.onTrigger = onTrigger;
				return this;
			}
			public void trigger() {
				try {
					onTrigger.apply(ScriptVar.toScriptVar(this));
				} catch (ParserException e) {
					e.printStackTrace();
				}
			}
		}.initialize(onTrigger);
	}
	
	
	public static void publishLibrary(LibraryWriter library) 
	{
		try {
			library.addAllIn(BattleLibrary.class);
		} catch (Exception e) {
		}
	}
}
