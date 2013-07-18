package bladequest.bladescript.libraries;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import bladequest.battleactions.BattleAction;
import bladequest.battleactions.DelegatingAction;
import bladequest.battleactions.SourcedAction;
import bladequest.battleactions.bactAttackClose;
import bladequest.battleactions.bactAttackRandomTargets;
import bladequest.battleactions.bactBarrelRoll;
import bladequest.battleactions.bactBasicAttack;
import bladequest.battleactions.bactBreakStance;
import bladequest.battleactions.bactDamage;
import bladequest.battleactions.bactDamageGroup;
import bladequest.battleactions.bactFlash;
import bladequest.battleactions.bactFlashColorize;
import bladequest.battleactions.bactFrozenGrip;
import bladequest.battleactions.bactFullRestore;
import bladequest.battleactions.bactInflictStatus;
import bladequest.battleactions.bactJumpHome;
import bladequest.battleactions.bactJumpToAndFace;
import bladequest.battleactions.bactLureEnemies;
import bladequest.battleactions.bactMessage;
import bladequest.battleactions.bactMirror;
import bladequest.battleactions.bactRemoveStatus;
import bladequest.battleactions.bactRevive;
import bladequest.battleactions.bactRunAnimation;
import bladequest.battleactions.bactRunAnimationBuilder;
import bladequest.battleactions.bactScript;
import bladequest.battleactions.bactSetFace;
import bladequest.battleactions.bactSetRevive;
import bladequest.battleactions.bactShatter;
import bladequest.battleactions.bactShowDamagedFaces;
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
import bladequest.combat.BattleEventBuilder;
import bladequest.combat.DamageComponent;
import bladequest.combat.triggers.Condition;
import bladequest.combat.triggers.Trigger;
import bladequest.enemy.Enemy;
import bladequest.graphics.BattleSprite.faces;
import bladequest.observer.Observer;
import bladequest.observer.ObserverList;
import bladequest.statuseffects.StatusEffect;
import bladequest.statuseffects.seBerserk;
import bladequest.statuseffects.seConfuse;
import bladequest.statuseffects.seFrozen;
import bladequest.statuseffects.sePoison;
import bladequest.statuseffects.sePoisonWeapon;
import bladequest.statuseffects.seRegen;
import bladequest.system.Recyclable;
import bladequest.world.Ability;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
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
	public static Battle setBattleEndHandler(ScriptVar battleEndHandler)
	{
		Global.battle.setBattleEndHandler(new Battle.BattleEndHandler() {
			
			ScriptVar battleEndHandler;
			
			Battle.BattleEndHandler initialize(ScriptVar battleEndHandler)
			{
				this.battleEndHandler = battleEndHandler;
				return this;
			}

			@Override
			public void onBattleEnd() {
				try {
					battleEndHandler.apply(ScriptVar.toScriptVar(Global.battle));
				} catch (ParserException e) {
					e.printStackTrace();
					Log.d("Parser", e.what());
				}
			}
			
		}.initialize(battleEndHandler));
		
		return Global.battle; 
	}
	
	public static Ability getAbility(String name)
	{
		return Global.abilities.get(name);
	}
	
	//for creating abilities on-the-fly.
	public static Ability temporaryAbility(String name, String displayName, String targetType, int mpcost)
	{
		//default outside of battle usability to false.
		Ability newAbility = new Ability(name, displayName, TargetTypes.valueOf(targetType), mpcost, false);
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
	public static Ability setShortName(Ability ability, String desc)
	{
		ability.setShortName(desc);
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
	public static Ability setPriority(Ability ability, String priority)
	{
		ability.setPriority(BattleCalc.MovePriority.valueOf(priority));
		return ability;
	}
	
	public static StatusEffect poisonStatus(float power)
	{
		return new sePoison(power);
	}
	
	public static StatusEffect poisonWeaponStatus(int duration)
	{
		return new sePoisonWeapon(duration);
	}
	
	public static StatusEffect regenStatus(int minRegen, int maxRegen, int duration)
	{
		return new seRegen(minRegen, maxRegen, duration);
	}
	
	
	public static StatusEffect confuseStatus(int duration)
	{
		return new seConfuse(duration);
	}
	
	public static StatusEffect frozenStatus(int duration)
	{
		return new seFrozen(duration);
	}
	
	public static StatusEffect beserkStatus(int duration)
	{
		return new seBerserk(duration);
	}
	
	
	
	public static Ability add(Ability ability, BattleAction action)
	{
		ability.addAction(action);
		return ability;
	}
	
	//special mega kiiiiiiiiiiiick
	public static BattleAction barrelRollAction(int milliseconds)
	{
		return new bactBarrelRoll((long)milliseconds);
	}
	
	//just for Roland's "Shatter"
	public static BattleAction rolandSpecialShatterAction(ScriptVar ignored)
	{
		return new bactShatter();
	}
	
	//just for Roland's "Frozen Grip"
	public static BattleAction rolandSpecialFrozenGripAction(ScriptVar ignored)
	{
		return new bactFrozenGrip();
	}	
	
	//just for revive stuff
	public static BattleAction reviveAnimAction(ScriptVar ignored)
	{
		return new bactRevive();
	}
	
	public static BattleAction reviveAction(ScriptVar ignored)
	{
		return new bactSetRevive();
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
	public static BattleAction jumpToAndFaceAction(float invArc, int animTime)
	{
		return new bactJumpToAndFace(invArc, animTime);
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
	public static BattleAction flashAction(float seconds, int a, int r, int g, int b)
	{
		return new bactFlash(seconds,a,r,g,b);
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
	
	public static BattleAction sourcedAsTarget(BattleAction retarget)
	{
		return new DelegatingAction()
		{
			BattleAction retarget;
			
			DelegatingAction initialize(BattleAction retarget)
			{
				this.retarget = retarget;
				return this;
			}
			
			@Override
			protected void buildEvents(BattleEventBuilder builder) {
			
				builder.addEventObject(new SourcedAction(BattleAction.getTarget(builder)) {

					BattleAction retarget;
					
					DelegatingAction initialize(BattleAction retarget)
					{
						this.retarget = retarget;
						return this;
					}					
					@Override
					protected void buildEvents(BattleEventBuilder builder) {
						builder.addEventObject(retarget);
					}
					
					
				}.initialize(retarget));
			}
		}.initialize(retarget);
	}
	
	public static BattleAction attackCloseAction(float power, String damageType)
	{
		return new bactAttackClose(power, DamageTypes.valueOf(damageType));
	}
	
	public static BattleAction attackRandomlyAction(float power, String damageType, int attacks, float speedFactor)
	{
		return new bactAttackRandomTargets(power, DamageTypes.valueOf(damageType), attacks, speedFactor);
	}
	
	public static BattleAction showDamagedFacesAction(int time)
	{
		return new bactShowDamagedFaces(time);
	}
	
	public static BattleAction damageAction(float power, String damageType)
	{
		return new bactDamage(power, DamageTypes.valueOf(damageType));
	}
	
	public static BattleAction damageGroupAction(float power, String damageType, String accuracyType, float missChance)
	{
		return new bactDamageGroup(power, DamageTypes.valueOf(damageType), BattleCalc.AccuracyType.valueOf(accuracyType), missChance);
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
	public static BattleAction basicAttackActionWithAccuracy(float power, String damageType, float speedFactor, String accuracyType, float accuracyVal)
	{
		return new bactBasicAttack(power, DamageTypes.valueOf(damageType), speedFactor, BattleCalc.AccuracyType.valueOf(accuracyType), accuracyVal);
	}
		
	
	
	public static BattleEventBuilder add(BattleEventBuilder builder, BattleAction action)
	{
		builder.addEventObject(action);
		return builder;
	}
	public static BattleEventBuilder addDep(BattleEventBuilder builder, BattleAction action)
	{
		builder.addEventObject(action.addDependency(builder.getLast()));
		return builder;
	}
	public static BattleEventBuilder onHit(BattleAction damageEvent)
	{
		bactDamage dmg = null;
		try
		{
			dmg = (bactDamage)damageEvent;
		}
		catch(Exception e)
		{
			return null;
		}
		return dmg.onHitRunner();
	}
	
	
	public static PlayerCharacter getTarget(BattleEventBuilder builder)
	{
		return BattleAction.getTarget(builder);
	}
	public static PlayerCharacter getSource(BattleEventBuilder builder)
	{
		return builder.getSource();
	}
	public static float basicDamageCalc(PlayerCharacter attacker, PlayerCharacter defender, float power, String type)
	{
		return BattleCalc.calculatedDamage(attacker, defender, power, DamageTypes.valueOf(type), new ArrayList<DamageComponent>(), 0.0f, BattleCalc.AccuracyType.Regular, PlayerCharacter.Hand.MainHand);
	}
	
	
	public static BattleEventBuilder makeGraphicalBattleEventBuilder(Battle battle)
	{
		return battle.makeGraphicalBattleEventBuilder();
	}
	
	//Function takes Target character and returns bool.
	public static BattleAction conditionalAttackAction(float power, String damageType, float speedFactor, ScriptVar function)
	{
		return new bactBasicAttack(power, DamageTypes.valueOf(damageType), speedFactor, function);
	}
		
	
	public static BattleAction lureAction(int stepCount)
	{
		return new bactLureEnemies(stepCount);
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
		if (child instanceof bactDamage)
		{
			((bactDamage)child).addDamageComponent(Stats.valueOf(affinity), factor);
	    }
		else if (child instanceof bactDamageGroup)
		{
			((bactDamageGroup)child).addDamageComponent(Stats.valueOf(affinity), factor);
		}
	
		
		

		return child;
	}
	
	public static BattleAction scriptAction(ScriptVar script)
	{
		return new bactScript(script);
	}
	
	
	public static BattleAction specialMirroringAction(boolean mirrored)
	{
		return new bactSpecialMirrored(mirrored);
	}
	
	public static BattleAction flashColorizeAction(int a, int r, int g, int b, int timeMS, float factor)
	{
		return new bactFlashColorize(a, r, g, b, timeMS, factor);
	}
	
	
	public static Ability addDep(Ability ability, BattleAction action)
	{
		BattleAction prev = ability.lastAction();
		ability.addAction(action);
		action.addDependency(prev);
		return ability; 
	}
	
	public static Ability setAbilityAdvances(Ability ability, boolean advances)
	{
		ability.setAdvances(advances);
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
	
	public static int addNextAbility(PlayerCharacter source, ScriptVar targets, Ability ability)
	{
		List<PlayerCharacter> characters = new ArrayList<PlayerCharacter>();
		ScriptVar.listFromSingleOrList(characters, targets); 
		
		Global.battle.forceNextAbilityEvent(source, characters, ability);
		return 0;
	}
	
	public static PlayerCharacter getCurrentBattleActor(ScriptVar ignored)
	{
		return Global.battle.getCurrentActor();
	}
	public static PlayerCharacter getRandomTarget(PlayerCharacter character, String type)
	{
		return Battle.getRandomTargets(TargetTypes.valueOf(type), character).get(0);
	}
	
	
	public static Battle showBattleMessage(Battle battle, String message)
	{
		battle.showMessage(message);
		return battle;
	}
	
	private static class SwitchCondition implements Condition
	{
		boolean on;
		ObserverList<Condition> observers;
		SwitchCondition(boolean startState)
		{
			observers = new ObserverList<Condition>(Global.battle.updatePool);
			on = startState;
		}
		void set(boolean on)
		{
			this.on = on;
			if (on)
			{
				observers.push(this);
			}
		}
		@Override
		public Recyclable register(Observer<Condition> observer) {
			return observers.add(observer);
		}

		@Override
		public void remove(Observer<Condition> observer) {
			observers.remove(observer);
		}

		@Override
		public boolean triggered() {
			return on;
		}
		
	}
	public static SwitchCondition createSwitchCondition(boolean startState)
	{
		return new SwitchCondition(startState);
	}
	public static SwitchCondition setSwitchCondition(SwitchCondition switchCondition, boolean on)
	{
		switchCondition.set(on);
		return switchCondition;
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
					Log.d("Parser", e.what());
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
