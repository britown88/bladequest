package bladequest.enemy;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.graphics.Rect;
import bladequest.battleactions.bactFlash;
import bladequest.battleactions.bactShake;
import bladequest.battleactions.bactWait;
import bladequest.bladescript.ScriptVar;
import bladequest.bladescript.ScriptVar.BadTypeException;
import bladequest.combat.BattleCalc;
import bladequest.combat.BattleEvent;
import bladequest.combat.BattleEventBuilder;
import bladequest.graphics.AnimatedBitmap;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleAnimObjState;
import bladequest.graphics.BattleAnimObjState.PosTypes;
import bladequest.graphics.BattleAnimObject;
import bladequest.graphics.BattleAnimObject.Types;
import bladequest.graphics.BattleSprite;
import bladequest.graphics.BattleSprite.faces;
import bladequest.graphics.ColorizedAnimation;
import bladequest.system.Recyclable;
import bladequest.world.Ability;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;
import bladequest.world.Stats;


public class Enemy extends PlayerCharacter
{	
	public interface BattleStartAction
	{
		void run(Enemy e);
	}
	public static class ScriptedBattleStartAction implements BattleStartAction
	{
		ScriptVar scriptFn;
		public ScriptedBattleStartAction(ScriptVar scriptFn)
		{
			this.scriptFn = scriptFn;
		}
		public void run(Enemy e)
		{
			try {
				scriptFn.apply(ScriptVar.toScriptVar(e));
			} catch (BadTypeException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private int gold;
	private String commonItem, rareItem, attackAnim;
	private int commonDropRate, rareDropRate, abilityChance;
	private boolean rareStealOnly;
	private boolean stolen;
	
	private int colorIndices[];
	
	private boolean bossFight;
	
	List<BattleStartAction> battleStartActions;
	
	AI ai;
	
	Recyclable AnimRecycleData;
	BattleAnim builtAnim;
	
	private List<Ability> abilities;
		
	public Enemy(String name, String spr)
	{
		super("", name, spr, "");
		isEnemy = true;
		
		position = new Point();
		
		stolen = false;
		
		abilities = new ArrayList<Ability>();
		battleStartActions = new ArrayList<BattleStartAction>(); 
	}
	
	public Enemy(Enemy e)
	{
		super(e);
		isEnemy = true;
		position = e.position;
		gold = e.gold;
		commonItem = e.commonItem;
		rareItem = e.rareItem;
		commonDropRate = e.commonDropRate;
		rareDropRate = e.rareDropRate;
		rareStealOnly = e.rareStealOnly;
		abilities = e.abilities;
		abilityChance = e.abilityChance;
		bossFight = e.bossFight;
		attackAnim = e.attackAnim;
		colorIndices = e.colorIndices;
		
		if (e.ai != null)
		{
			ai = new AI(e.ai);
		}
				
		battleStartActions = e.battleStartActions;
		
		stolen = false;
	}
	
	@Override
	public void startBattle()
	{
		super.startBattle();
	    AnimatedBitmap bmp= new ColorizedAnimation(Global.weaponSwingModels.get(attackAnim).toAnimatedBitmap(), colorIndices);
	    AnimRecycleData = (Recyclable)bmp;
		builtAnim = AnimatedBitmap.Extensions.genAnim(bmp, 180.0f, 15);
		
		for (BattleStartAction action : battleStartActions)
		{
			action.run(this);
		}
	}
	
	@Override
	public void endBattle()
	{
		super.endBattle();
		if (AnimRecycleData != null)
		{
			AnimRecycleData.recycle();
		}
	}
	
	
	public void addBattleStartAction(BattleStartAction action)
	{
		battleStartActions.add(action);
	}
	public void setAttackAnimation(String attackAnim) {this.attackAnim = attackAnim;}
	public void setColorIndicies(int[] colorIndices) {this.colorIndices = colorIndices;}
	
	@Override
	public BattleAnim getWeaponAnimation(){return builtAnim;}
	
	public void addAbility(String abilityName)
	{
		abilities.add(Global.abilities.get(abilityName));
	}
	public Ability getAbility(String abilityName)
	{
		for (Ability a : abilities)
		{
			if (a.name.equals(abilityName)) return a;
		}
		return null;
	}
	public AI getAI()
	{
		return ai;
	}
	public void Act()
	{
		abilityToUse = ai.getState().pickAbility(this);
		if (abilityToUse == null)  //default attack.
		{
			action = Action.Attack;
		}
		else
		{
			action = Action.Ability;
		}
	}
	
	public void setAI(AI ai)
	{
		this.ai = ai;
	}
	
	public BattleEvent genBattleEvent(List<PlayerCharacter> chars, List<Enemy> enemies)
	{
		Act();
		List<PlayerCharacter> targets = new ArrayList<PlayerCharacter>();
		List<PlayerCharacter> everybody = new ArrayList<PlayerCharacter>();
		
		for(PlayerCharacter c : chars)everybody.add(c);
		for(Enemy e : enemies)everybody.add(e);
		
		switch(action)
		{
		case Ability:
			switch(abilityToUse.TargetType())
			{
			case Single:
			case SingleEnemy:
				targets.add(chars.get(Global.rand.nextInt(chars.size())));
				break;				
			case SingleAlly:			
				targets.add(enemies.get(Global.rand.nextInt(enemies.size())));
				break;
			case AllAllies:
				for(Enemy e : enemies)targets.add(e);
				break;
			case AllEnemies:
				for(PlayerCharacter c : chars)targets.add(c);
				break;				
			case Everybody:
				for(PlayerCharacter c : everybody)targets.add(c);
				break;
			case Self:
				targets.add(this);
				break;
			}			
			break;
		default://attack
			targets.add(chars.get(Global.rand.nextInt(chars.size())));			
			break;
		}
		
		return new BattleEvent(this.action, abilityToUse, this, targets, Global.battle.getMarkers());
		
	}
	

	private void playBossDeathAnimation()
	{
		float fps = 60.0f;
		BattleAnim anim = new BattleAnim(fps);
		
		float msConvert = fps/1000.0f;
		
		BattleAnimObject baObj = new BattleAnimObject(Types.Bitmap, false, battleSpr.getBmpName());
		Rect srcRect = battleSpr.getFrameRect(faces.Idle, 0);
		
		BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Source);
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = new Point(0,0);
		state.size = new Point(battleSpr.getWidth(), battleSpr.getHeight());
		baObj.addState(state);
		
		state = new BattleAnimObjState((int)(2500*msConvert), PosTypes.Source);
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = new Point(0,0);
		state.size = new Point(battleSpr.getWidth(), battleSpr.getHeight());
		baObj.addState(state);
		
		state = new BattleAnimObjState((int)(4500*msConvert), PosTypes.Source);
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(0, 255, 0, 0);
		state.colorize = 1.0f;
		state.pos1 = new Point(0,0);
		state.size = new Point(battleSpr.getWidth(), battleSpr.getHeight());
		baObj.addState(state);
		
		anim.addObject(baObj);
		
		Global.playAnimation(anim, position, null, true);
	
		BattleEventBuilder builder = Global.battle.makeGraphicalBattleEventBuilder();
	
		builder.addEventObject(new bactWait(350));
		builder.addEventObject(new bactFlash(5).addDependency(builder.getLast()));
		
		builder.addEventObject(new bactWait(1800));
		builder.addEventObject(new bactFlash(25).addDependency(builder.getLast()));
		
		builder.addEventObject(new bactWait(2100));
		builder.addEventObject(new bactFlash(25).addDependency(builder.getLast()));
		
		builder.addEventObject(new bactWait(2500));
		builder.addEventObject(new bactShake(2, 2, true).addDependency(builder.getLast()));
	}
	
	private void playDeathAnimation()
	{
		BattleAnim anim = new BattleAnim(60.0f);
		
		BattleAnimObject baObj = new BattleAnimObject(Types.Bitmap, false, battleSpr.getBmpName());
		Rect srcRect = battleSpr.getFrameRect(faces.Idle, 0);
		
		BattleAnimObjState state = new BattleAnimObjState(0, PosTypes.Source);
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = new Point(0,0);
		state.size = new Point(battleSpr.getWidth(), battleSpr.getHeight());
		baObj.addState(state);
		
		state = new BattleAnimObjState(15, PosTypes.Source);
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = new Point(0,0);
		state.size = new Point(battleSpr.getWidth(), battleSpr.getHeight());
		baObj.addState(state);
		
		state = new BattleAnimObjState(75, PosTypes.Source);
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(0, 255, 0, 0);
		state.colorize = 1.0f;
		state.pos1 = new Point(0,0);
		state.size = new Point(battleSpr.getWidth(), battleSpr.getHeight());
		baObj.addState(state);
		
		anim.addObject(baObj);
		
		Global.playAnimation(anim, position, null, true);
	}
	
	@Override
	public void modifyHP(float HP, boolean percentage)
	{
		super.modifyHP(HP, percentage);
		
		if(dead)
		{
			if (bossFight)
			{
				playBossDeathAnimation();
			}
			else
			{
				playDeathAnimation();
			}
		}
	}
	
	public void setBoss()
	{
		bossFight = true;
		exp *=4;
	}
	
	public boolean isBoss() { return bossFight; }
	
	public void setGold(int i){gold = i;}	
	public int getGold(){return gold;}
	
	public void modifyLevel(int level)
	{
		this.level = level;
		updateStats();
		fullRestore();
		setExp();		
		
	}
	
	public void setItems(String commonItem, int commonDropRate, String rareItem, int rareDropRate, boolean rareStealOnly)
	{
		this.commonItem = commonItem;
		this.commonDropRate = commonDropRate;
		this.rareItem = rareItem.length() > 1 ? rareItem : "";
		this.rareDropRate = rareDropRate;
		this.rareStealOnly = rareStealOnly;
	}
	
	public boolean hasItems()
	{
		return (commonItem != null || rareItem != null) && !stolen;
	}
	
	public String getItem(boolean stealing)
	{
		int roll = Global.rand.nextInt(100);
		
		if(!stolen)
		{
			
			if((rareStealOnly && stealing) || !rareStealOnly)
			{
				if(roll < rareDropRate && rareItem != null)
				{
					stolen = stealing;
					return rareItem;
				}
			}
			
			if(roll < commonDropRate && commonItem != null)
			{
				stolen = stealing;
				return commonItem;
			}

		}
		
		return null;
	}
	
	public void setExp()
	{
		exp = (int)((float)Math.pow(level, 3.0f)/level);
		
	}	
	
	public int getExp(int partyLevel)
	{
		/*sum up base stats
		int baseSum = baseStats[Stats.Strength.ordinal()] + baseStats[Stats.Agility.ordinal()] + baseStats[Stats.Vitality.ordinal()] + baseStats[Stats.Intelligence.ordinal()];
		float baseMod = (((baseSum - 510.0f) / 510.0f) * 25.0f) / 100.0f;
		float levelDiff = level - partyLevel;
		float lvlMod = Math.max(0.1f * levelDiff, -1.0f);*/
		
		
		//return (int)Math.max(exp*(1.0f + baseMod)*(1.0f + lvlMod), 1.0f);
		return exp;
	}
	
	//override base class sprite-changing functions to do nothing
	@Override
	public void setFace(BattleSprite.faces newFace){}
	@Override
	public void setImageIndex(int index){}
	
	
	@Override
	public void battleRender()
	{
		if(!dead)
			battleSpr.render(position.x, position.y, 0, true);
	}
	
	@Override
	public Rect getRect() 
	{
		return Global.vpToScreen(new Rect(
				position.x-getWidth()/2, 
				position.y-getHeight()/2, 
				position.x+getWidth()/2, 
				position.y+getHeight()/2));}
	
	@Override
	public void updateSecondaryStats()
	{
		float str = getStat(Stats.Strength);
		float agi = getStat(Stats.Agility);
		float vit = getStat(Stats.Vitality);
		float intel = getStat(Stats.Intelligence);
		float lvl = level;
		
		//speed based on agi
		//( ( Agility x 3) + ( 255 / 99 ) x Level ) / 4
		stats[Stats.Speed.ordinal()] = (int)(((agi*3.0f)+(255.0f/99.0f)*lvl)/4.0f);
		
		//Evade: 255 = 90% evasion
		//Level 99: 10%
		//Agility 255: 10%
		float pointsPerPercent = 255.0f / BattleCalc.maxEvade;
		int levelBonus = (int)(((pointsPerPercent*10.0f)/99.0f)*lvl);
		int agiBonus = (int)(((pointsPerPercent*10.0f)/255.0f)*agi);
		int minBonus = (int)(pointsPerPercent*BattleCalc.minEvade);
		stats[Stats.Evade.ordinal()] = minBonus + levelBonus + agiBonus;
		
		pointsPerPercent = 255.0f / BattleCalc.maxCrit;
		minBonus = (int)(pointsPerPercent*BattleCalc.minCrit);
		stats[Stats.Crit.ordinal()] = minBonus;
		
		//hp/mp based on vit and int	
		stats[Stats.MaxHP.ordinal()] = (int)((((vit * 2.0f) + (255.0f/99.0f)*lvl) / 3.0f) * 20.0f * getCoefficient());
		stats[Stats.MaxMP.ordinal()] = (int)(((intel * 2.0f + (255.0f/99.0f)*lvl) / 3.0f) * 7.0f * getCoefficient());
		
		//AP	
		stats[Stats.BattlePower.ordinal()] = (int)(((str * 2.0f) + ((255.0f / 99.0f) * lvl)) / 3.0f);
		
		//Defense
		stats[Stats.Defense.ordinal()] = (int)(((vit * 4.0f) + ((255.0f / 99.0f) * lvl)) / 5.0f);
		
		stats[Stats.MagicPower.ordinal()] = (int)(((intel*3.0f)+(255.0f/99.0f)*lvl)/4.0f);
		stats[Stats.MagicDefense.ordinal()] = (int)(((intel*3.0f)+(255.0f/99.0f)*lvl)/4.0f);
		
		
	}

}
