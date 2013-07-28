package bladequest.enemy;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.graphics.Rect;
import bladequest.battleactions.BattleAction;
import bladequest.battleactions.bactFlash;
import bladequest.battleactions.bactShake;
import bladequest.battleactions.bactWait;
import bladequest.bladescript.ParserException;
import bladequest.bladescript.ScriptVar;
import bladequest.combat.Battle;
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
import bladequest.statuseffects.StatusEffect;
import bladequest.system.Recyclable;
import bladequest.world.Ability;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;
import bladequest.world.TargetTypes;


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
			} catch (ParserException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	private class Drop
	{
		Drop(String item, float dropRate)
		{
			this.item = item;
			this.dropRate = dropRate;
		}
		String item;
		float dropRate; //0-1.
	}
	
	List<Drop> drops;
	String heldItem;
	
	
	private int gold;
	private String attackAnim;
	
	private int colorIndices[];
	
	private boolean bossFight;
	
	List<BattleStartAction> battleStartActions;
	
	AI ai;
	
	Recyclable AnimRecycleData;
	BattleAnim builtAnim;
	
		
	public Enemy(String name, String spr)
	{
		super("", name, spr, "");
		isEnemy = true;
		
		position = new Point();
		drops = new ArrayList<Drop>();
		battleStartActions = new ArrayList<BattleStartAction>(); 
	}
	
	public Enemy(Enemy e)
	{
		super(e);
		isEnemy = true;
		position = e.position;
		gold = e.gold;
		drops = e.drops;
		heldItem = e.heldItem;	
		bossFight = e.bossFight;
		attackAnim = e.attackAnim;
		colorIndices = e.colorIndices;
		
		fullRestore();
		
		if (e.ai != null)
		{
			ai = new AI(e.ai);
		}
				
		battleStartActions = e.battleStartActions;
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
	public PlayerCharacter Act()
	{
		class Picker implements AIDecision
		{
			PlayerCharacter target;
			Ability ability;
			@Override
			public void pickAbility(Ability ability) {
				this.ability = ability;
			}

			@Override
			public void pickTarget(PlayerCharacter target) {
				this.target = target;
			}	
		};
		Picker p = new Picker();
		ai.getState().runAI(this, p);
		if (p.ability == null)  //default attack.
		{
			action = Action.Attack;
		}
		else
		{
			action = Action.Ability;
			abilityToUse = p.ability;
		}
		return p.target;
	}
	
	public void setAI(AI ai)
	{
		this.ai = ai;
	}
	
	public void genBattleEvent()
	{
		PlayerCharacter target = Act();

		if (target == null)
		{
			switch(action)
			{
			case Ability:
				TargetTypes targetType = abilityToUse.TargetType();
				if (targetType == TargetTypes.Single) targetType = TargetTypes.SingleEnemy;
				targets = Battle.getRandomTargets(targetType, this);
				break;
			default://attack
				targets = Battle.getRandomTargets(TargetTypes.SingleEnemy, this);			
				break;
			}			
		}
		else
		{
			targets = new ArrayList<PlayerCharacter>();
			targets.add(target);
		}
		//return new BattleEvent(this.action, abilityToUse, this, targets, Global.battle.getMarkers());
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
		
		state = new BattleAnimObjState((int)(300*msConvert), PosTypes.Source);
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = new Point(0,0);
		state.size = new Point(battleSpr.getWidth(), battleSpr.getHeight());
		baObj.addState(state);
		
		state = new BattleAnimObjState((int)(700*msConvert), PosTypes.Source);
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.colorize = 1.0f;
		state.pos1 = new Point(0,0);
		state.size = new Point(battleSpr.getWidth(), battleSpr.getHeight());
		baObj.addState(state);
		
		state = new BattleAnimObjState((int)(1100*msConvert), PosTypes.Source);
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.colorize = 0.0f;
		state.pos1 = new Point(0,0);
		state.size = new Point(battleSpr.getWidth(), battleSpr.getHeight());
		baObj.addState(state);				
		
		state = new BattleAnimObjState((int)(2500*msConvert), PosTypes.Source);
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(255, 255, 255, 255);
		state.pos1 = new Point(0,0);
		state.size = new Point(battleSpr.getWidth(), battleSpr.getHeight());
		baObj.addState(state);
		
		state = new BattleAnimObjState((int)(6500*msConvert), PosTypes.Source);
		state.setBmpSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
		state.argb(0, 255, 0, 0);
		state.colorize = 1.0f;
		state.pos1 = new Point(0,0);
		state.size = new Point(battleSpr.getWidth(), battleSpr.getHeight());
		baObj.addState(state);
		
		anim.addObject(baObj);
		
		Global.playAnimation(anim, position, null, true);
	
		BattleEventBuilder builder = Global.battle.makeGraphicalBattleEventBuilder();
	
		BattleAction waitAction = new bactWait(350); 
		builder.addEventObject(waitAction);
		builder.addEventObject(new bactFlash(1.0f, 255, 255, 255, 255).addDependency(builder.getLast()));
		waitAction.setReferences();
		
		waitAction = new bactWait(1800);
		builder.addEventObject(waitAction);
		builder.addEventObject(new bactFlash(0.25f, 255, 255, 255, 255).addDependency(builder.getLast()));
		waitAction.setReferences();
		
		waitAction = new bactWait(2100);
		builder.addEventObject(waitAction);
		builder.addEventObject(new bactFlash(0.25f, 255, 255, 255, 255).addDependency(builder.getLast()));
		waitAction.setReferences();
		
		waitAction = new bactWait(2500);
		builder.addEventObject(waitAction);
		builder.addEventObject(new bactShake(2, 4, true).addDependency(builder.getLast()));
		waitAction.setReferences();
		
		waitAction = new bactWait(8000); //prevent battle from ending too quickly!
		builder.addEventObject(waitAction);
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
		exp *= 4;
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
	
	public void setHeldItem(String heldItem)
	{
		this.heldItem = heldItem;
	}
	
	public void addDrop(String dropItem, float rate)
	{
		drops.add(new Drop(dropItem, rate));
	}
	
	public boolean holdingItem()
	{
		return heldItem != null;
	}
	
	public String stealItem()
	{
		if (!holdingItem()) return null;
		int roll = Global.rand.nextInt(100);
		if (roll < 20) return null; //
		
		String out = heldItem;
		heldItem = null;
		return out;
	}
	
	public String getDrop()
	{
		for (Drop d : drops)
		{
			if (Global.rand.nextFloat() < d.dropRate)
			{
				return d.item;
			}
		}
		return null;  //no soup
	}
	public void setExp()
	{
		exp = level*level;
		
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
		{
			battleSpr.render(position.x, position.y, 0, true);
			for (StatusEffect se : getStatusEffects())
			{
				se.onRender(this);
			}			
		}
	}
	
	@Override
	public Rect getRect() 
	{
		return Global.vpToScreen(new Rect(
				position.x-getWidth()/2, 
				position.y-getHeight()/2, 
				position.x+getWidth()/2, 
				position.y+getHeight()/2));}

}
