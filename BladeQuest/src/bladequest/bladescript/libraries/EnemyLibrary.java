package bladequest.bladescript.libraries;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import bladequest.bladescript.LibraryWriter;
import bladequest.bladescript.Script.BadSpecialization;
import bladequest.bladescript.ScriptVar;
import bladequest.bladescript.ScriptVar.BadTypeException;
import bladequest.combat.triggers.Event;
import bladequest.enemy.AI;
import bladequest.enemy.AIState;
import bladequest.enemy.Enemy;
import bladequest.enemy.ScriptedAIState;
import bladequest.world.Ability;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;
import bladequest.world.Stats;

public class EnemyLibrary {


	public static int RGBA(int r, int g, int b, int a)
	{
		return Color.argb(a, r, g, b);
	}
	public static ScriptVar interpolateColorsToList(int c1, int c2, int iterations)
	{
		int r1 = Color.red(c1);
		int g1 = Color.green(c1);
		int b1 = Color.blue(c1);
		int a1 = Color.alpha(c1);
		
		int r2 = Color.red(c2);
		int g2 = Color.green(c2);
		int b2 = Color.blue(c2);
		int a2 = Color.alpha(c2);
		
		float t = 0.0f;
		float step = 1.0f / (iterations-1);
		
		ScriptVar out = new ScriptVar.EmptyList();
		
		for (int i = 0; i < iterations; ++i)
		{
			int r = (int)(r1 * t + r2 * (1.0f-t));
			int g = (int)(g1 * t + g2 * (1.0f-t));
			int b = (int)(b1 * t + b2 * (1.0f-t));
			int a = (int)(a1 * t + a2 * (1.0f-t));
			
			out = new ScriptVar.ListNode(ScriptVar.toScriptVar(Color.argb(a, r, g, b)), out);
			
			t += step;
		}
		
		return out;
	}
	public static AI createAI(String defaultStateName, AIState defaultState)
	{
		AI ai = new AI();
		ai.add(defaultStateName, defaultState);
		ai.switchToState(defaultStateName);
		return ai;
	}
	public static AI addAIState(AI ai, String stateName, AIState state)
	{
		ai.add(stateName, state);
		return ai;
	}
	
	//NOTE: FOR REGISTERING TRIGGERS, DO IT ON BATTLE START, NOT ON ENEMY PROTOTYPE/TEMPLATE/WHATEVER CREATE!
	//DONT DO IT
	//NO DONT
	public static Event getOnPhysicalHitEvent(PlayerCharacter character)
	{
		return character.getOnPhysicalHitEvent();
	}
	
	public static Enemy onBattleStartRun(Enemy enemy, ScriptVar fn)
	{
		enemy.addBattleStartAction(new Enemy.ScriptedBattleStartAction(fn));
		return enemy;
	}
	public static Enemy setEnemyAI(Enemy enemy, AI ai)
	{
		enemy.setAI(ai);
		return enemy;
	}
	public static Enemy switchEnemyState(Enemy enemy, String newState)
	{
		enemy.getAI().switchToState(newState);
		return enemy;
	}
	public static AIState createAIState(ScriptVar stateFn)
	{
		return new ScriptedAIState(stateFn);
	}
	
	public static Ability getEnemyAbility(Enemy enemy, String abilityName)
	{
		return enemy.getAbility(abilityName);
	}
	public static Enemy addEnemy(String name, String displayName, String displaySprite)
	{
		Enemy enemy = new Enemy(displayName, displaySprite);
		Global.enemies.put(name, enemy);
		return enemy;
	}
	public static Enemy setEBaseStats(Enemy enemy, int str, int agi, int vit, int intel)
	{
		enemy.setBaseStats(str, agi, vit, intel);
		return enemy;
	}
	public static Enemy modStat(Enemy enemy, String stat, int mod)
	{
		enemy.setStatMod(Stats.valueOf(stat).ordinal(), mod);
		return enemy;
	}
	public static Enemy setELevel(Enemy enemy, int level)
	{
		enemy.modifyLevel(level);
		return enemy;
	}
	//THIS IS SPECIAL BULLSTHI
	//it's the "swing anim" stuff, not BattleAnims!
	public static Enemy setAttackAnimation(Enemy enemy, String animName) 
	{
		enemy.setAttackAnimation(animName);
		return enemy;
	}
	public static Enemy setAttackColors(Enemy enemy, ScriptVar colorList)
	{
		List<Integer> colors = new ArrayList<Integer>();
		while (!colorList.isEmptyList())
		{
			try {
				colors.add(colorList.head().getInteger());
				colorList = colorList.tail();				
			} catch (BadTypeException e) {
				e.printStackTrace();
				return enemy;
			} 
		}
		
		int idx = 0;
		int[] ints = new int[colors.size()];
		for (Integer intVal : colors)
		{
			ints[idx++] = intVal;
		}
		
		
		enemy.setColorIndicies(ints);
		return enemy;
	}
	public static Enemy setGold(Enemy enemy, int gold)
	{
		enemy.setGold(gold);
		return enemy;
	}
	public static Enemy setItems(Enemy enemy, String commonItem, int commonDropRate, String rareItem, int rareDropRate, boolean rareStealOnly)
	{
		enemy.setItems(commonItem, commonDropRate, rareItem, rareDropRate, rareStealOnly);
		return enemy;
	}
	public static Enemy setBoss(Enemy enemy)
	{
		enemy.setBoss();
		return enemy;
	}
	public static Enemy addEAbility(Enemy enemy, String abilityName)
	{
		enemy.addAbility(abilityName);
		return enemy;
	}
	
	public static void publishLibrary(LibraryWriter writer)
	{
		try {
			writer.addAllIn(EnemyLibrary.class);
		} catch (BadTypeException e) {
			e.printStackTrace();
		} catch (BadSpecialization e) {
			e.printStackTrace();
		}
	}
}
