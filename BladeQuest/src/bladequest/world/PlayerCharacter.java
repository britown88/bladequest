package bladequest.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.graphics.Point;
import android.graphics.Rect;
import bladequest.combat.BattleCalc;
import bladequest.combat.BattleEventBuilderObject;
import bladequest.combat.triggers.Event;
import bladequest.combatactions.CombatAction;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleSprite;
import bladequest.graphics.BattleSprite.faces;
import bladequest.graphics.Shadow;
import bladequest.graphics.Sprite;
import bladequest.graphics.WeaponSwingDrawable;
import bladequest.statuseffects.StatusEffect;
import bladequest.statuseffects.seKO;
import bladequest.world.Item.Type;

public class PlayerCharacter 
{
	protected String name;
	protected String displayName, startingName;
	protected boolean isEnemy = false;
	
	public boolean isInParty;
	
	//equipment
	private Item hand1, hand2, helmet, torso, accessory;
	
	public enum Slot
	{
		Hand1,
		Hand2,
		Helmet,
		Torso,
		Accessory
	}
	List<Item.Type> hand1Types, hand2Types;
	
	protected int[] stats;
	protected int[] statMods;
	protected int[] baseStats;
	
	protected Point position;
	
	protected int HP;
	protected int MP;
	
	protected boolean dead;
	protected boolean escaped;
	private String abilitiesName;
	
	protected int level;
	protected int exp;
	private int expToLevel;	
	
	protected BattleSprite battleSpr;
	private Sprite worldSpr;
	public Point portrait;
	
	protected List<PlayerCharacter> targets;
	
	protected int imageIndex;
	
	protected int index;	
	private Item itemToUse;
	protected Ability abilityToUse;
	protected Action action;
	
	private CombatAction combAction;
	
	private List<StatusEffect> statusEffects;
	protected List<Ability> abilities;
	private List<LearnableAbility> learnableAbilities;
	
	private BattleEventBuilderObject battleEventBuilderObj;
	private String combatActionText;
	
	private List<String> statusImmunities;
	
	private boolean visible;
	private boolean positionSpecial;
	private boolean mirrorSpecial;
	private boolean defaultMirroredState = false;
	
	private Event onPhysicalHitEvent, //you're about to be hit
			      onPhysicalHitDamageCalcEvent,  //damage on you is being calculated
			      onPhysicalHitLandsEvent,  //you've been hit
			      onPhysicalHitSuccessEvent,  //you've successfully hit someone
			      
				  onDamagedEvent,  //you're being attacked! 
				  onDamageReceivedEvent,  //you've been successfully attacked, ouch.  Damage is already dealt here. 
				  onAttackEvent;
	
	private Shadow shadow;
	
	
	public void setHandEquippable(Hand hand, List<Item.Type> equipList)
	{
		if (hand == Hand.MainHand) hand1Types = equipList;
		else hand2Types = equipList;
	}
	
	
	public PlayerCharacter(String name, String displayNam, String bSpr, String wSpr)
	{
		this.name = name;
		this.displayName = displayNam;
		battleSpr = new BattleSprite(Global.battleSprites.get(bSpr));
		worldSpr = new Sprite(Global.sprites.get(wSpr));
		level = 5;	
		exp = 0;
		setExpToLevel();
		imageIndex = 0;
		stats = new int[Stats.NUM_STATS.ordinal()];
		statMods = new int[Stats.NUM_STATS.ordinal()];
		baseStats = new int[4];
		abilitiesName = "";
		targets = new Vector<PlayerCharacter>();
		statusEffects = new ArrayList<StatusEffect>();
		abilities = new ArrayList<Ability>();
		learnableAbilities = new ArrayList<LearnableAbility>();
		portrait = new Point(0,0);
		position = new Point(0,0);
		visible = true;
		escaped = false;
		positionSpecial = false;
		statusImmunities = new ArrayList<String>();
		startingName = "";
		hand1Types = new ArrayList<Item.Type>();
		hand2Types = new ArrayList<Item.Type>();
	}
	
	public PlayerCharacter(PlayerCharacter c)
	{
		shadow = c.shadow;
		targets = new Vector<PlayerCharacter>();
		statusEffects = new ArrayList<StatusEffect>();
		displayName = c.displayName;
		name = c.name;
		HP = c.HP;
		MP = c.MP;
		dead = c.dead;
		startingName = c.startingName; 
		
		isInParty = c.isInParty;
		index = c.index;
		
		//copy over stats
		stats = new int[Stats.NUM_STATS.ordinal()];
		statMods = new int[Stats.NUM_STATS.ordinal()];
		baseStats = new int[4];		
		for(int i = 0; i < Stats.NUM_STATS.ordinal(); ++i)
		{	stats[i] = c.stats[i];
			statMods[i] = c.statMods[i];}
		for(int i = 0; i < 4; ++i)
			baseStats[i] = c.baseStats[i];	
		
		combAction = c.combAction;
		abilitiesName = c.abilitiesName;
		
		level = c.level;
		exp = c.exp;
		expToLevel = c.expToLevel;
				
		battleSpr = new BattleSprite(c.battleSpr);
		worldSpr = new Sprite(c.worldSpr);
		portrait = new Point(c.portrait);
		
		imageIndex = c.imageIndex;
		hand1 = c.hand1;
		hand2 = c.hand2;
		torso = c.torso;
		accessory = c.accessory;
		helmet = c.helmet;
		
		abilities = new ArrayList<Ability>(c.abilities);
		learnableAbilities = new ArrayList<LearnableAbility>(c.learnableAbilities);
		statusEffects = new ArrayList<StatusEffect>(c.statusEffects);
		
		position = new Point(0,0);
		visible = c.visible;
		escaped = c.escaped;
		positionSpecial = c.positionSpecial;
		
		if (Global.battle != null) //triple-ghetto state check
		{
			onPhysicalHitEvent = new Event();
			onPhysicalHitLandsEvent = new Event();
			onPhysicalHitDamageCalcEvent = new Event();
			onPhysicalHitSuccessEvent = new Event();
			
			onDamagedEvent = new Event();
			onDamageReceivedEvent = new Event();
			onAttackEvent = new Event();
		}
		
		statusImmunities = c.statusImmunities;
		
		hand1Types = new ArrayList<Item.Type>(c.hand1Types);
		hand2Types = new ArrayList<Item.Type>(c.hand2Types);
	}
	
	public void setStartingName(String name){startingName = name;}
	public String getStartingName(){return startingName;}

	public Event getOnAttackEvent()
	{
		return onAttackEvent;
	}
	public Event getOnPhysicalHitEvent()
	{
		return onPhysicalHitEvent;
	}
	public void addStatusImmunity(String statusname)
	{
		statusImmunities.add(statusname);
	}
	public boolean isImmuneToStatus(String statusname)
	{
		for (String s : statusImmunities)
		{
			if (s.equals(statusname)) return true;
		}
		return false;
	}
	public Event getOnPhysicalHitDamageCalcEvent()
	{
		return onPhysicalHitDamageCalcEvent;
	}
	public Event getOnPhysicalHitLandsEvent()
	{
		return onPhysicalHitLandsEvent;
	}
	public Event getOnPhysicalHitSuccessEvent()
	{
		return onPhysicalHitSuccessEvent;	
	}
	
	public Event getOnDamagedEvent()
	{
		return onDamagedEvent;
	}	
	
	public Event getOnDamageReceivedEvent()
	{
		return onDamageReceivedEvent;
	}		
	
	public List<Item.Type> getUsableTypes(Slot slot)
	{
		List<Item.Type> out = new ArrayList<Item.Type>();
		switch (slot)
		{
		case Hand1:
			out = new ArrayList<Item.Type>(hand1Types);
			break;
		case Hand2:
			out = new ArrayList<Item.Type>(hand2Types);
			break;
		case Accessory:
			out.add(Item.Type.Accessory);
			break;
		case Helmet:
			out.add(Item.Type.Helmet);
			break;
		case Torso:
			out.add(Item.Type.Torso);
			break;
		default:
			break; 
		}
		return out;
	}
	
	public void setExp(int exp){this.exp = exp;}
	public void setDisplayName(String str){displayName = str;}
	public void setSprites(String world, String battle)
	{
		battleSpr = new BattleSprite(Global.battleSprites.get(battle));
		worldSpr = new Sprite(Global.sprites.get(world));
	}
	
	public void clearAbilities()
	{
		abilities.clear();
	}
	
	public void startBattle()
	{
		onPhysicalHitEvent = new Event();
		onPhysicalHitLandsEvent = new Event();
		onPhysicalHitDamageCalcEvent = new Event();
		onPhysicalHitSuccessEvent = new Event();
		onDamagedEvent = new Event();
		onDamageReceivedEvent = new Event();
		onAttackEvent = new Event();
		
		for (Item i : getEquippedItems())
		{
			i.runStartBattleScripts(this);
		}
		
	}
	public void endBattle()
	{
		onPhysicalHitEvent = null;
		onPhysicalHitDamageCalcEvent = null;
		onPhysicalHitLandsEvent = null;
		onPhysicalHitSuccessEvent = null;
		onDamagedEvent = null;
		onDamageReceivedEvent = null;
		onAttackEvent = null;
		
		
		//force 1HP at the end of battle.
		if (hasStatus("KO"))
		{
			this.revive();
		}
	}
	
	public int getExp(){return exp;}
	public int getRemainingExp(){return expToLevel - exp;}
	public Sprite getWorldSprite() { return worldSpr; }	
	public BattleSprite getBattleSprite() { return battleSpr; }
	public void setIndex(int i){index = i;}	
	public int Index(){return index;}	
	public List<PlayerCharacter> getTargets(){return targets;}	
	public void setTargets(List<PlayerCharacter> targets){this.targets = targets;}	
	public void addTarget(PlayerCharacter e){targets.add(e);}	
	public void clearTargets(){targets.clear();}
	public String getActionName() { return combAction.getName(); }
	public String getAbilitiesName() { return abilitiesName; }	
	public List<Ability> getAbilities() { return abilities; }
	public Item getItemToUse(){return itemToUse;}	
	public Ability getAbilityToUse(){return abilityToUse;}	
	public Action getAction(){return action;}	
	public String getDisplayName(){return displayName;}	
	public void setStatusEffects(List<StatusEffect> statusEffects) { this.statusEffects = statusEffects; }
	public List<StatusEffect> getStatusEffects() { return statusEffects; }
	public String getName(){return name;}	
	public boolean isEnemy() { return isEnemy; }	
	public void setHpMp(int hp, int mp){ this.HP = hp; this.MP = mp; }
	
	public void setBattleAction(Action action){this.action = action;}
	
	public int getStat(Stats stat){return getStat(stat.ordinal());}	
	public int getStat(int stat)
	{
		if(stat == Stats.MaxHP.ordinal())
			return Math.min(9999, stats[stat] + statMods[stat]);
		else if(stat == Stats.MaxMP.ordinal())
			return Math.min(999, stats[stat] + statMods[stat]);
		else
			return Math.min(255, stats[stat] + statMods[stat]);
	}	
	public int getUnModdedStat(Stats stat){return getUnModdedStat(stat.ordinal());}	
	public int getUnModdedStat(int stat)
	{
		if(stat == Stats.MaxHP.ordinal())
			return Math.min(9999, stats[stat]);
		else if(stat == Stats.MaxMP.ordinal())
			return Math.min(999, stats[stat]);
		else
			return Math.min(255, stats[stat]);
	}		
	public boolean muted()
	{
		for (StatusEffect se : statusEffects)
		{
			if (se.mutes()) return true;
		}
		return false;
	}
	public void modStat(int stat, int amt)
	{
		statMods[stat] += amt;
		
		if(this.HP > getStat(Stats.MaxHP))
			this.HP = getStat(Stats.MaxHP);
		
		if(this.MP > getStat(Stats.MaxMP))
			this.MP = getStat(Stats.MaxMP);
	}	
	public void setStatMod(int stat, int amt)
	{
		statMods[stat] = amt;
		
		if(this.HP > getStat(Stats.MaxHP))
			this.HP = getStat(Stats.MaxHP);
		
		if(this.MP > getStat(Stats.MaxMP))
			this.MP = getStat(Stats.MaxMP);
	}
	public int getStatMod(Stats stat) { return statMods[stat.ordinal()]; }
	public int getStatMod(int i) { return statMods[i]; }
	
	public int getHP(){return HP;}
	public int getMP(){return MP;}
	public boolean isDead(){return dead;}
	public boolean isInBattle(){return !dead && !escaped;}
	public int getLevel(){return level;}
	
	public void setAbilitiesName(String abilities){abilitiesName = abilities;}
	public void setCombatAction(CombatAction action) { combAction = action; }
	
	public void genWeaponSwing()
	{
		if(hasSlotEquipped(Slot.Hand1))
			hand1.generateSwing();
	}
	
	public WeaponSwingDrawable getWeaponSwing()
	{
		return hasSlotEquipped(Slot.Hand1) ? hand1.getSwing() : null;
	}
	
	public boolean hasSlotEquipped(Slot slot)
	{
		switch(slot)
		{
		case Hand1: return hand1Equipped();
		case Hand2: return hand2Equipped();
		case Helmet: return helmEquipped();
		case Accessory: return accessEquipped();
		case Torso: return torsoEquipped();
		default:
			break;
		}
		return false;			
	}
//	
//	public boolean hasTypeEquipped(Item.Type type)
//	{
//		switch(type)
//		{
//		case Weapon: return hand1Equipped();
//		case Shield: return hand2Equipped();
//		case Helmet: return helmEquipped();
//		case Accessory: return accessEquipped();
//		case Torso: return torsoEquipped();
//		default:
//			break;
//		}
//		return false;		
//	}
	public boolean helmEquipped() { return helmet != null; }
	public boolean hand1Equipped() { return hand1 != null; }
	public boolean torsoEquipped() { return torso != null; }
	public boolean accessEquipped() { return accessory != null; }
	public boolean hand2Equipped() { return hand2 != null; }
	public boolean hand2WeaponEquipped() { return hand2Equipped() && hand2.isWeapon();}
	
	public Item getEquippedItem(Slot slot)
	{
		switch(slot)
		{
		case Hand1: return hand1;
		case Hand2: return hand2;
		case Helmet: return helmet;
		case Accessory: return accessory;
		case Torso: return torso;
		default:
			break;
		}
		return null;
	}
	
	public Item helmet() { return helmet; }
	public Item hand1() { return hand1; }
	public Item torso() { return torso; }
	public Item accessory() { return accessory; }
	public Item hand2() { return hand2; }
	public List<Item> getEquippedItems()
	{
		List<Item> list = new ArrayList<Item>();
		if(hand1Equipped()) list.add(hand1);
		if(hand2Equipped()) list.add(hand2);
		if(helmEquipped()) list.add(helmet);
		if(torsoEquipped()) list.add(torso);
		if(accessEquipped()) list.add(accessory);
		
		return list;
	}
	
	public static class EquippedItem
	{
	   EquippedItem(Item item, Slot slot)
	   {
		   this.item = item;
		   this.slot = slot;
	   }
	   public Item item;
	   public Slot slot;
	}
	
	public List<EquippedItem> getSlottedEquippedItems()
	{
		List<EquippedItem> list = new ArrayList<EquippedItem>();
		if(hand1Equipped()) list.add(new EquippedItem(hand1, Slot.Hand1));
		if(hand2Equipped()) list.add(new EquippedItem(hand2, Slot.Hand2));
		if(helmEquipped()) list.add(new EquippedItem(helmet, Slot.Helmet));
		if(torsoEquipped()) list.add(new EquippedItem(torso, Slot.Torso));
		if(accessEquipped()) list.add(new EquippedItem(accessory, Slot.Accessory));
		
		return list;
	}
	
	public void setPortrait(int bmpX, int bmpY)
	{
		portrait = new Point(bmpX, bmpY);
	}
	
	public Rect getPortraitSrcRect()
	{ 
		return new Rect(
				portrait.x * Global.portraitSrcSize, 
				portrait.y * Global.portraitSrcSize, 
				portrait.x * Global.portraitSrcSize + Global.portraitSrcSize,
				portrait.y * Global.portraitSrcSize + Global.portraitSrcSize);
	}
	
	public boolean canEquip(Slot slot, Item item)
	{
		if (!getUsableTypes(slot).contains(item.getType())) return false;
		
		
		if (slot == Slot.Hand2)
		{
			if (item.isTwoHanded()) return false; //two handed items are only allowed on the main hand.
			if (hand1Equipped() && hand1.isTwoHanded()) return false;  //don't allow any equips when a two handed item is equipped.
		}
		
		return true;
	}
	
	public boolean canEquip(Item item)
	{
		for (Slot slot : Slot.values())
		{
			if (canEquip(slot, item)) return true;
		}
		return false;
	}
	
	public void firstEquip(Slot slot, String itemName)
	{
		Item item = new Item(Global.items.get(itemName));
		if(canEquip(slot, item))
		{
			item.equip(this);
			
			switch(slot)
			{
			case Hand1:hand1 = item;break;			
			case Hand2:hand2 = item;break;			
			case Torso:	torso = item;break;			
			case Helmet:helmet = item;break;			
			case Accessory:	accessory = item;break;
			default:
				break;
			}
		}
		
	}
	
	public boolean hasItemEquipped(int id)
	{
		if(hand1 != null && hand1.getId() == id)
			return true;
		if(hand2 != null && hand2.getId() == id)
			return true;
		if(helmet != null && helmet.getId() == id)
			return true;
		if(torso != null && torso.getId() == id)
			return true;
		if(accessory != null && accessory.getId() == id)
			return true;
		
		return false;
	}
	
	public void equip(Slot slot, int id)
	{
		Item item = null;
		
		for(Item i : Global.party.getInventory())
			if(i.getId() == id)
			{
				item = i;
				break;
			}

		//item wasn't found or item is not usable by this character
		if(item == null || !canEquip(slot, item))
			return;
		
		switch(slot)
		{
		case Hand1:
			if(hand1 != null)
			{
				hand1.unequip(this);
				Global.party.addItem(hand1.getId());
			}			
			hand1 = item;
			item.equip(this);
			Global.party.removeItem(id, 1);
			if (hand1.isTwoHanded())
			{
				this.unequip(Slot.Hand2);
			}			
			break;
			
		case Hand2:
			if(hand2 != null)
			{
				hand2.unequip(this);
				Global.party.addItem(hand2.getId());
			}			
			hand2 = item;
			item.equip(this);
			Global.party.removeItem(id, 1);
			break;
			
		case Torso:
			if(torso != null)
			{
				torso.unequip(this);
				Global.party.addItem(torso.getId());
			}			
			torso = item;
			item.equip(this);
			Global.party.removeItem(id, 1);
			break;
			
		case Helmet:
			if(helmet != null)
			{
				helmet.unequip(this);
				Global.party.addItem(helmet.getId());
			}			
			helmet = item;
			item.equip(this);
			Global.party.removeItem(id, 1);
			break;
			
		case Accessory:
			if(accessory != null)
			{
				accessory.unequip(this);
				Global.party.addItem(accessory.getId());
			}			
			accessory = item;
			item.equip(this);
			Global.party.removeItem(id, 1);
			break;
		default:
			break;
		}
	}
	
	public void unequipAll()
	{
		unequip(Slot.Hand1);
		unequip(Slot.Hand2);
		unequip(Slot.Helmet);
		unequip(Slot.Torso);
		unequip(Slot.Accessory);
	}
	
	public void clearEquipment()
	{
		hand1 = null;
		hand2 = null;
		helmet = null;
		torso = null;
		accessory = null;
	}
	
	public void unequip(Slot slot)
	{		
		switch(slot)
		{
		case Hand1:
			if(hand1 != null)
			{
				hand1.unequip(this);
				Global.party.addItem(hand1.getId());
				hand1 = null;
			}			
			break;
			
		case Hand2:
			if(hand2 != null)
			{
				hand2.unequip(this);
				Global.party.addItem(hand2.getId());
				hand2 = null;
			}			
			break;
			
		case Torso:
			if(torso != null)
			{
				torso.unequip(this);
				Global.party.addItem(torso.getId());
				torso = null;
			}			
			break;
			
		case Helmet:
			if(helmet != null)
			{
				helmet.unequip(this);
				Global.party.addItem(helmet.getId());
				helmet = null;
			}			
			break;
			
		case Accessory:
			if(accessory != null)
			{
				accessory.unequip(this);
				Global.party.addItem(accessory.getId());
				accessory = null;
			}			
			break;
		default:
			break;
		}
	}
	
	public void useItem()
	{	
		unuseItem();
		Global.party.removeItem(itemToUse.getId(), 1);
		//itemToUse.execute(this, targets);
	}
	
	public String getCombatActionText() { return combatActionText; }
	public void setCombatActionText(String text) {combatActionText = text; }
	public CombatAction getCombatAction() { return combAction; }
	
	public String combatActionName;

	
	public void setUseCombatAction()
	{
		action = Action.CombatAction;

	}
	
	public void unuseItem()
	{
		itemToUse.unuse();
	}
	
	public void setItemToUse(Item item)
	{
		itemToUse = item;
		itemToUse.use();
		action = Action.Item;	

	}
	
	public void useAbility(Ability ability)
	{	
		MP = Math.max(0,  MP-ability.MPCost());			
	}
	
	public void modifyMP(int MP)
	{
		this.MP = Math.max(0,  Math.min(getStat(Stats.MaxMP), this.MP + MP));
	}
	
	public void setAbilityToUse(Ability ability)
	{
		
		abilityToUse = ability;
		action = Action.Ability;	
	}
	
	//set stats between 0 and 10
	public void setBaseStats(int strength, int agility, int vitality, int intelligence)
	{
		baseStats[Stats.Strength.ordinal()] = (int)(strength*25.5f);
		baseStats[Stats.Agility.ordinal()] = (int)(agility*25.5f);
		baseStats[Stats.Vitality.ordinal()] = (int)(vitality*25.5f);
		baseStats[Stats.Intelligence.ordinal()] = (int)(intelligence*25.5f);
		
		updateStats();
	}
	
	public float getCoefficient()
	{
		return (0.5f * ((float)level / 99.0f)) + 0.5f;
	}
	
	public void modifyLevel(int level, boolean relative)
	{
		if(relative)
			this.level += level;
		else
			this.level = level;
		
		setExpToLevel();		
		updateStats();
	}
	public int getBaseStat(Stats stat)
	{
		return baseStats[stat.ordinal()];
	}
	
	
	protected void updateStats()
	{
		float str = baseStats[Stats.Strength.ordinal()];
		float agi = baseStats[Stats.Agility.ordinal()];
		float vit = baseStats[Stats.Vitality.ordinal()];
		float intel = baseStats[Stats.Intelligence.ordinal()];
		float lvl = level;
		
		//stat = (basestat/99)*level
		stats[Stats.Strength.ordinal()] = (int)(str * (lvl/99.0f));
		stats[Stats.Agility.ordinal()] = (int)(agi * (lvl/99.0f));
		stats[Stats.Vitality.ordinal()] = (int)(vit * (lvl/99.0f));
		stats[Stats.Intelligence.ordinal()] = (int)(intel * (lvl/99.0f));
		
		stats[Stats.Fire.ordinal()] = 100;
		stats[Stats.Wind.ordinal()] = 100;
		stats[Stats.Water.ordinal()] = 100;
		stats[Stats.Earth.ordinal()] = 100;
		
		updateSecondaryStats(PlayerCharacter.Hand.MainHand);
	}
	
	public enum Hand
	{
		MainHand,
		OffHand
	}
	
	public void updateSecondaryStats(Hand handedness)
	{
		float str = getStat(Stats.Strength);
		float agi = getStat(Stats.Agility);
		float intel = getStat(Stats.Intelligence);
		float lvl = level;
		
		//speed based on agi
		//( ( Agility x 3) + ( 255 / 99 ) x Level ) / 4
		stats[Stats.Speed.ordinal()] = (int)(agi);
		
		//Evade: 255 = 90% evasion
		//Level 99: 10%
		//Agility 255: 10%
		//Base minimum: 5%
		float pointsPerPercent = 255.0f / BattleCalc.maxEvade;
		int levelBonus = (int)(((pointsPerPercent*10.0f)/99.0f)*lvl);
		int agiBonus = (int)(((pointsPerPercent*10.0f)/255.0f)*agi);
		int minBonus = (int)(pointsPerPercent*BattleCalc.minEvade);
		stats[Stats.Evade.ordinal()] = minBonus + levelBonus + agiBonus;
		
		pointsPerPercent = 255.0f / BattleCalc.maxCrit;
		minBonus = (int)(pointsPerPercent*BattleCalc.minCrit);
		stats[Stats.Crit.ordinal()] = minBonus;
		
		//hp/mp based on vit and int	
		stats[Stats.MaxHP.ordinal()] = BattleCalc.calculateCurvedHPMPValue(baseStats[Stats.Vitality.ordinal()], lvl, BattleCalc.maxHP);
		stats[Stats.MaxMP.ordinal()] = BattleCalc.calculateCurvedHPMPValue(baseStats[Stats.Intelligence.ordinal()], lvl, BattleCalc.maxMP);
	
		
		float w = 0.0f;
		//AP
		if (handedness == Hand.MainHand)
		{
			w = hand1Equipped() ? hand1.Power() : 0.0f;	
		}
		else	
		{
			w = hand2Equipped() && hand2.isWeapon() ? hand2.Power() : 0.0f;
		}
		stats[Stats.BattlePower.ordinal()] = (int) (str + w);
		
		//Defense

		stats[Stats.Defense.ordinal()] = (int) ((helmEquipped() ? helmet.Power() : 0.0f) + (torsoEquipped() ? torso.Power() : 0.0f));
		
		//MagicPower/Defense
		stats[Stats.MagicPower.ordinal()] = (int)(intel);
		stats[Stats.MagicDefense.ordinal()] = (int)(intel);
		
		
	}
	
	
	public List<Item> getUsableItemsForSlot(Slot slot)
	{
		List<Item> itemList = new ArrayList<Item>();
		
		//build list of items of the appropriate type usable by this character		
		for(Item i : Global.party.getInventory())
			if(canEquip(slot, i))
				itemList.add(i);	
		
		return itemList;
	}
	
	//pass item equipment slot and this function will equip the best 
	//based purely on value, and greedy.  this won't unequip other party members.
	public void equipBest(Slot slot)
	{

		List<Item> itemList = getUsableItemsForSlot(slot);
		
		if(itemList.size() > 0)
		{
			Item bestItem = null;
			int bestCost = -1;
			for (Item i : itemList)
			{
				if (i.getValue() > bestCost)
				{
					bestCost = i.getValue();
					bestItem = i;
				}
			}
			
			if(bestItem != null)
				equip(slot, bestItem.getId());
		}
		
	}
	
	public int[] getModdedStats(Slot slot, Item i)
	{
		int[] moddedStats = new int[Stats.NUM_STATS.ordinal()];		
		Item currEquipped = null;
		
		if(hasSlotEquipped(slot))
			currEquipped = getEquippedItem(slot);
		
		equip(slot, i.getId());
		updateSecondaryStats(PlayerCharacter.Hand.MainHand);
		
		for(int stat = 0; stat < Stats.NUM_STATS.ordinal(); ++stat)
			moddedStats[stat] = getStat(stat);
		
		if(currEquipped != null)
			equip(slot, currEquipped.getId());
		else
			unequip(slot);	
		
		updateSecondaryStats(PlayerCharacter.Hand.MainHand);
		
		//return modded stats array		
		return moddedStats;
		
	}
	
	//get stats from removing item at given slot	
	public int[] getModdedStatsRmv(Slot slot)
	{
		int[] moddedStats = new int[Stats.NUM_STATS.ordinal()];
	
		Item currEquipped = null;
		
		if(hasSlotEquipped(slot))
			currEquipped = getEquippedItem(slot);
		
		unequip(slot);
		updateSecondaryStats(PlayerCharacter.Hand.MainHand);
		
		for(int stat = 0; stat < Stats.NUM_STATS.ordinal(); ++stat)
			moddedStats[stat] = getStat(stat);
		
		if(currEquipped != null)
			equip(slot, currEquipped.getId());
		
		
		updateSecondaryStats(PlayerCharacter.Hand.MainHand);
		//return modded stats array		
		return moddedStats;
		
	}
	
	public void kill()
	{
		HP = 0;
		dead = true;
		setFace(faces.Dead);
		
		//remove onDeath status effects
		List<StatusEffect> newList = new ArrayList<StatusEffect>();
		
		for (StatusEffect se : StatusEffect.filterList(statusEffects, new StatusEffect.Filter(){
			@Override
			public boolean filter(StatusEffect effect) {
				return effect.removesOnDeath();
			}
		})) 
		{
			se.onRemove(this);
		}
		
		for(StatusEffect se : statusEffects)
			if(!se.removesOnDeath())
				newList.add(se);		
		statusEffects = newList;
		
		applyStatusEffect(new seKO());
	}
	
	public void revive()
	{
		dead = false;
		removeStatusEffect("KO");
		setFace(faces.Idle);	
		HP = Math.max(1, HP);		
	}
	
	public boolean hasStatus(String effect)
	{
		for(StatusEffect s : statusEffects)
			if(s.Name().equals(effect))
				return true;
		
		return false;

	}
	
	public void cureAdverseStatuses()
	{
		if(!dead)
		{
			List<StatusEffect> newList = new ArrayList<StatusEffect>();
			
			for(StatusEffect se : statusEffects)
				if(!se.isNegative() || !se.isCurable())
					newList.add(se);			
				
			statusEffects = newList;
			setFace(faces.Idle);
			//setIdle(false);	
		}
		
	}
	
	public void removeStatusEffect(String se)
	{
		List<StatusEffect> newList = new ArrayList<StatusEffect>();
		
		for(StatusEffect s : statusEffects)
			if(s.Name().equals(se))
			{
				newList.add(s);
			}
		
		for (StatusEffect s : newList)
		{
			removeStatusEffect(s);

		}
	}
	
	
	public void removeStatusEffect(StatusEffect se)
	{
		se.onRemove(this);
		statusEffects.remove(se);
		setFace(battleSpr.getFace());		
	}
	
	public void applyStatusEffect(StatusEffect se)
	{
		if(!dead || se.Name().equals("KO"))
		{	
			se.onInflict(this);
			statusEffects.add(se);
			setFace(battleSpr.getFace());			
		}
		
	}
	
/*	public void statusOnTurn(Battle b)
	{
		for(StatusEffect se : statusEffects)
		{
			se.onTurn(this, b);
		}
	}
	*/

	//returns overlap exp if user leveld up, 0 if not
	public int awardExperience(int exp)
	{
		int overlap = 0;
		
		this.exp += exp;
		
		//level up
		if(this.exp >= expToLevel)
		{
			overlap = this.exp - expToLevel;
			int oldMaxHP = getUnModdedStat(Stats.MaxHP);
			int oldMaxMP = getUnModdedStat(Stats.MaxMP);
			modifyLevel(1, true);
			int newMaxHP = getUnModdedStat(Stats.MaxHP);
			int newMaxMP = getUnModdedStat(Stats.MaxMP);
			
			//on level up, gain back the newly given HP/MP.
			HP += newMaxHP - oldMaxHP;
			MP += newMaxMP - oldMaxMP;
			this.exp = 0;
			
			return overlap;
		}
		else
			return 0;
		
	}
	
	public String checkForAbilities()
	{
		List<LearnableAbility> remainingAbilities = new ArrayList<LearnableAbility>();
		for(LearnableAbility la : learnableAbilities)
		{
			if(la.LevelReq() <= level)
			{
				boolean alreadyHas = false;
				
				//check if ability already exists
				for(Ability ab : abilities)
					if(ab.name.equals(la.GetAbility().name))
					{
						alreadyHas = true;
						break;
					}
				
				if(!alreadyHas)
				{
					abilities.add(la.GetAbility());
					learnableAbilities.remove(la);
					return la.GetAbility().getDisplayName();
				}				
				
			}
			else
			{
				remainingAbilities.add(la);
			}
		}
		learnableAbilities = remainingAbilities;
		return "";
	}
	
	public void addAbility(String name)
	{
		abilities.add(Global.abilities.get(name));
	}
	public void removeAbility(String name)
	{
		for (Ability ability : abilities)
		{
			if (ability.name.equals(name))
			{
				abilities.remove(ability);
				return;
			}
		}
	}
	public Ability getAbility(String abilityName)
	{
		for (Ability ability : abilities)
		{
			if (ability.name.equals(abilityName))
			{
				return ability;
			}
		}
		return null;
	}
	public void addLearnableAbility(String name, int level)
	{
		learnableAbilities.add(new LearnableAbility(name, level));
	}
	
	private void setExpToLevel()
	{
		expToLevel = (int)Math.pow(level, 3);
	}
	
	public void fullRestore()
	{
		if(dead)
			revive();
		
		cureAdverseStatuses();		
		
		HP = getStat(Stats.MaxHP);
		MP = getStat(Stats.MaxMP);	
		
	}
	
	public void modifyHP(float HP, boolean percentage)
	{
		if(!dead)
		{
			if(percentage)
				this.HP += (float)stats[Stats.MaxHP.ordinal()] * ((float)HP/100.0F);
			else
				this.HP += HP;
			
			if(this.HP <= 0)
			{
				this.HP = 0;
				kill();
			}
			
			if(this.HP > getStat(Stats.MaxHP))
				this.HP = getStat(Stats.MaxHP);
		}		
	}
	
	public void setBattleFrame(BattleSprite.faces face)
	{
		setFace(face);
		imageIndex = 0;
	}

	
	private boolean hasWeakeningStatus()
	{
		for(StatusEffect se : statusEffects)
			if(se.weakens())
				return true;
		
		return false;
	}
	
	public boolean hasAdverseStatus()
	{
		for(StatusEffect se : statusEffects)
			if(se.isNegative())
				return true;
		
		return false;
	}
	


	
	private faces getWeakenedFace()
	{
		faces out = faces.Weak;
		for(StatusEffect se : statusEffects)
		{
			if(se.weakens())
			{
				out = se.weakendFace();
			}
		}
		
		return out;
	}

	
	private BattleSprite.faces savedFace;
	public void showDamaged()
	{
		BattleSprite.faces face = battleSpr.getFace();
		
		if(face != faces.Attack)
		{
			if(face != faces.Damaged)
				savedFace = face;
			
			setFace(faces.Damaged);
		}
	}
	public void showHealed()
	{
		BattleSprite.faces face = battleSpr.getFace();
		if (!hasWeakeningStatus() && face == faces.Weak)
		{
			setFace(faces.Idle);
		}
	}
	public void clearDamaged()
	{
		BattleSprite.faces face = battleSpr.getFace();
		if(face == faces.Damaged && (getWeakenedFace() != faces.Damaged) && savedFace != null)
		{
			//reset to idle before setting to avoid oldface loop
			battleSpr.changeFace(faces.Idle);
			setFace(savedFace);
		}
			
	}
	
	
	public void setFace(BattleSprite.faces newFace)
	{
		if(!isEnemy)
		{
			BattleSprite.faces oldFace = battleSpr.getFace();
			boolean weak = HP <= (float)stats[Stats.MaxHP.ordinal()]*0.25F || hasWeakeningStatus();
			imageIndex = 0;			
			
			switch(newFace)
			{
			case Idle:
				if(oldFace == faces.Damaged)
				{
					//dont go to idle if being hurt, go to idle after
					savedFace = faces.Idle;
					break;
				}					
			case Ready:
				if(dead) setFace(faces.Dead);
				else if (weak) setFace(getWeakenedFace());
				else battleSpr.changeFace(newFace);
				break;
			case Dead:
				if(!dead) battleSpr.changeFace(faces.Idle);
				else battleSpr.changeFace(newFace);					
				break;
			case Weak:
				if(!weak) battleSpr.changeFace(faces.Idle);
				else battleSpr.changeFace(newFace);					
				break;
			default:
				battleSpr.changeFace(newFace);				
				break;
			}				
		}
	}
	
	public void setImageIndex(int index){ imageIndex = index;}
	
	private void updateAnimation()
	{
		if(battleSpr != null)
		{
			if(Global.updateAnimation)
			{	
				imageIndex++;
				
				if(imageIndex >= battleSpr.getNumFrames()) 
					imageIndex = 0;
					
			}
		}
	}
	
	public void playWeaponAnimation(Point src, Point tar){if(hand1Equipped()) hand1.playAnimation(src, tar);}
	public BattleAnim getWeaponAnimation(){if(hand1Equipped()) return hand1.getAnim(); else return null;}
	
	public void renderShadow()
	{
		if (visible && isInBattle() && shadow != null)
		{
			Point centeredPosition = getPosition(true);
			shadow.setPosition(centeredPosition.x, centeredPosition.y);
			if (battleSpr.getMirrored())
			{
				shadow.setXNudge(-shadow.getXNudge());
			}
			shadow.render();
			if (battleSpr.getMirrored())
			{
				shadow.setXNudge(-shadow.getXNudge());
			}			
		}
	}
	
	public void battleRender()
	{
		if(battleSpr.getFace() != faces.Attack)
			updateAnimation();//dont update attack anim, it's managed by battle
		
		if (visible && !getEscaped())
		{
			battleSpr.render(position.x, position.y, imageIndex, false);
			
			//draw weapon swing
			if(battleSpr.getFace() == faces.Attack)
			{
				boolean mirrored = getMirrored();
				//int offset = -20;
				//if (mirrored) offset *= -1;
				WeaponSwingDrawable swing = getWeaponSwing();
				if(swing != null)
					swing.render(imageIndex, position.x -20, position.y-6, mirrored);
			}
		}	
		
		for (StatusEffect se : statusEffects)
		{
			se.onRender(this);
		}
	}
	
	public Rect getRect() {return Global.vpToScreen(new Rect(position.x, position.y, position.x+getWidth(), position.y+getHeight()));}
	public Point getPosition() { return position; }	
	public Point getPosition(boolean center) 
	{ 
		Point p = new Point(position);
		if(center && !isEnemy)
			p.offset(battleSpr.getWidth()/2, battleSpr.getHeight()/2);
		else if (!center && isEnemy)
			p.offset(-battleSpr.getWidth()/2, -battleSpr.getHeight()/2);
		
		return p; 
	}	
	public int getWidth() { return battleSpr.getWidth(); }
	public int getHeight() { return battleSpr.getHeight(); }
	public void setPosition(int x, int y) { position = new Point(x, y);}	
	public void setPosition(Point p) {position = p;}
	
	
	public void setEventBuilder(BattleEventBuilderObject eventBuilder)
	{
		battleEventBuilderObj = eventBuilder;
	}
	public BattleEventBuilderObject getEventBuilder()
	{
		return battleEventBuilderObj;
	}
	
	public boolean getPositionSpecial() { return positionSpecial;}
	public void setPositionSpecial(boolean positionSpecial) {this.positionSpecial = positionSpecial;}
	
	public void setVisible(boolean isVisible) {visible = isVisible;}
	public boolean getVisible() { return visible;}
	
	public void setMirrored(boolean isMirrored) {battleSpr.setMirrored(isMirrored);}
	public void setDefaultMirroredState(boolean isMirrored) {defaultMirroredState = isMirrored;}
	public void setMirroredSpecial(boolean mirroredSpecial) {this.mirrorSpecial = mirroredSpecial;}
	public boolean getMirrored() { return battleSpr.getMirrored();}
	public boolean getDefaultMirrored() { return defaultMirroredState;}
	public boolean getMirroredSpecial() {return this.mirrorSpecial;} 
	
	public void setShadow(Shadow shadow){this.shadow = shadow;}
	public Shadow getShadow() {return this.shadow;}
	
	public void setEscaped(boolean escape) {escaped = escape;}
	public boolean getEscaped() {return escaped;}
	
	public enum Action
	{
		Attack,
		Item,
		Guard,
		CombatAction,
		Ability,
		Run,
		Skipped
	}

}
