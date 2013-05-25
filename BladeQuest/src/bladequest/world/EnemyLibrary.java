package bladequest.world;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import bladequest.scripting.LibraryWriter;
import bladequest.scripting.Script.BadSpecialization;
import bladequest.scripting.ScriptVar;
import bladequest.scripting.ScriptVar.BadTypeException;

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
	public static Enemy addEnemy(String name, String displayName, String displaySprite)
	{
		Enemy enemy = new Enemy(displayName, displaySprite);
		Global.enemies.put(name, enemy);
		return enemy;
	}
	public static Enemy setBaseStats(Enemy enemy, int str, int agi, int vit, int intel)
	{
		enemy.setBaseStats(str, agi, vit, intel);
		return enemy;
	}
	public static Enemy modStat(Enemy enemy, String stat, int mod)
	{
		enemy.setStatMod(Stats.valueOf(stat).ordinal(), mod);
		return enemy;
	}
	public static Enemy setLevel(Enemy enemy, int level)
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
	public static Enemy setAIAbilityRate(Enemy enemy, int rate)
	{
		enemy.setAI(rate);
		return enemy;
	}
	public static Enemy addAbility(Enemy enemy, String abilityName, int chanceToCast, int healthAbove, int healthBelow)
	{
		enemy.addAbility(abilityName, chanceToCast, healthAbove, healthBelow);
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
