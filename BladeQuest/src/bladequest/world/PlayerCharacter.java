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
	protected String displayName;
	protected boolean isEnemy = false;
	
	//equipment
	private Item weapon, shield, helmet, torso, accessory;
	
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
	
	private boolean visible;
	private boolean positionSpecial;
	private boolean mirrorSpecial;
	private boolean defaultMirroredState = false;
	
	private Event onPhysicalHitEvent, onDamagedEvent, onAttackEvent;
	
	private Shadow shadow;
	
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
		weapon = c.weapon;
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
			onDamagedEvent = new Event();
			onAttackEvent = new Event();
		}		
	}

	public Event getOnAttackEvent()
	{
		return onAttackEvent;
	}
	public Event getOnPhysicalHitEvent()
	{
		return onPhysicalHitEvent;
	}
	public Event getOnDamagedEvent()
	{
		return onDamagedEvent;
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
		onDamagedEvent = new Event();
		onAttackEvent = new Event();
	}
	public void endBattle()
	{
		onPhysicalHitEvent = null;
		onDamagedEvent = null;
		onAttackEvent = null;
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
		if(hasTypeEquipped(Type.Weapon))
			weapon.generateSwing();
	}
	
	public WeaponSwingDrawable getWeaponSwing()
	{
		return hasTypeEquipped(Type.Weapon) ? weapon.getSwing() : null;
	}
	
	public boolean hasTypeEquipped(Item.Type type)
	{
		switch(type)
		{
		case Weapon: return weapEquipped();
		case Shield: return shieldEquipped();
		case Helmet: return helmEquipped();
		case Accessory: return accessEquipped();
		case Torso: return torsoEquipped();
		default:
			break;
		}
		return false;		
	}
	public boolean helmEquipped() { return helmet != null; }
	public boolean weapEquipped() { return weapon != null; }
	public boolean torsoEquipped() { return torso != null; }
	public boolean accessEquipped() { return accessory != null; }
	public boolean shieldEquipped() { return shield != null; }
	public Item getEquippedItem(Item.Type type)
	{
		switch(type)
		{
		case Weapon: return weapon;
		case Shield: return shield;
		case Helmet: return helmet;
		case Accessory: return accessory;
		case Torso: return torso;
		default:
			break;
		}
		return null;
	}
	public Item helmet() { return helmet; }
	public Item weapon() { return weapon; }
	public Item torso() { return torso; }
	public Item accessory() { return accessory; }
	public Item shield() { return shield; }
	public List<Item> getEquippedItems()
	{
		List<Item> list = new ArrayList<Item>();
		if(weapEquipped()) list.add(weapon);
		if(shieldEquipped()) list.add(shield);
		if(helmEquipped()) list.add(helmet);
		if(torsoEquipped()) list.add(torso);
		if(accessEquipped()) list.add(accessory);
		
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
	
	
	public void firstEquip(String itemName)
	{
		Item item = new Item(Global.items.get(itemName));
		if(item.getUsableChars().contains(name))
		{
			item.equip(this);
			
			switch(item.getType())
			{
			case Weapon:weapon = item;break;			
			case Shield:shield = item;break;			
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
		if(weapon != null && weapon.getId() == id)
			return true;
		if(shield != null && shield.getId() == id)
			return true;
		if(helmet != null && helmet.getId() == id)
			return true;
		if(torso != null && torso.getId() == id)
			return true;
		if(accessory != null && accessory.getId() == id)
			return true;
		
		return false;
	}
	
	public void equip(int id)
	{
		Item item = null;
		
		for(Item i : Global.party.getInventory())
			if(i.getId() == id)
			{
				item = i;
				break;
			}

		//item wasn't found or item is not usable by this character
		if(item == null || !item.getUsableChars().contains(name))
			return;
		
		switch(item.getType())
		{
		case Weapon:
			if(weapon != null)
			{
				weapon.unequip(this);
				Global.party.addItem(weapon.getId());
			}			
			weapon = item;
			item.equip(this);
			Global.party.removeItem(id, 1);
			break;
			
		case Shield:
			if(shield != null)
			{
				shield.unequip(this);
				Global.party.addItem(shield.getId());
			}			
			shield = item;
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
		unequip(Type.Weapon);
		unequip(Type.Shield);
		unequip(Type.Helmet);
		unequip(Type.Torso);
		unequip(Type.Accessory);
	}
	
	public void clearEquipment()
	{
		weapon = null;
		shield = null;
		helmet = null;
		torso = null;
		accessory = null;
	}
	
	public void unequip(Item.Type type)
	{		
		switch(type)
		{
		case Weapon:
			if(weapon != null)
			{
				weapon.unequip(this);
				Global.party.addItem(weapon.getId());
				weapon = null;
			}			
			break;
			
		case Shield:
			if(shield != null)
			{
				shield.unequip(this);
				Global.party.addItem(shield.getId());
				shield = null;
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
	
	protected void updateStats()
	{
		float str = baseStats[Stats.Strength.ordinal()];
		float agi = baseStats[Stats.Agility.ordinal()];
		float vit = baseStats[Stats.Vitality.ordinal()];
		float intel = baseStats[Stats.Intelligence.ordinal()];
		float lvl = level;
		
		//stat = (basestat/99)*level
		stats[Stats.Strength.ordinal()] = (int)(str * Math.pow(lvl/99.0f, 2.0f));
		stats[Stats.Agility.ordinal()] = (int)(agi * Math.pow(lvl/99.0f, 2.0f));
		stats[Stats.Vitality.ordinal()] = (int)(vit * Math.pow(lvl/99.0f, 2.0f));
		stats[Stats.Intelligence.ordinal()] = (int)(intel * Math.pow(lvl/99.0f, 2.0f));
		
		stats[Stats.Fire.ordinal()] = 100;
		stats[Stats.Wind.ordinal()] = 100;
		stats[Stats.Water.ordinal()] = 100;
		stats[Stats.Earth.ordinal()] = 100;
		
		updateSecondaryStats();
	}
	private int calcBP(float lvl, float str, float w){return (int)(((str * 2.0f) + (w * 2.0f) + ((255.0f / 99.0f) * lvl)) / 5.0f);}
	private int calcDef(float lvl, float vit, float arm, float sh) { return (int)(((vit * 2.0f) + (arm * 2.0f) + (sh * 2.0f) + ((255.0f / 99.0f) * lvl)) / 5.0f);}
	
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
		stats[Stats.MaxHP.ordinal()] = Math.min(9999, (int)((((vit * 2.0f) + (255.0f/99.0f)*lvl) / 3.0f) * 15.0f * getCoefficient()));
		stats[Stats.MaxMP.ordinal()] = Math.min(999, (int)(((intel * 2.0f + (255.0f/99.0f)*lvl) / 3.0f) * 5.0f * getCoefficient()));
	
		//AP
		float w = weapEquipped() ? weapon.Power() : 0.0f;		
		stats[Stats.BattlePower.ordinal()] = calcBP(lvl, str, w);
		
		//Defense
		float arm = (helmEquipped() ? helmet.Power() : 0.0f) + (torsoEquipped() ? torso.Power() : 0.0f);
		float sh = shieldEquipped() ? shield.Power() : 0.0f;
		stats[Stats.Defense.ordinal()] = calcDef(lvl, vit, arm, sh);
		
		//MagicPower/Defense
		stats[Stats.MagicPower.ordinal()] = (int)(((intel*3.0f)+(255.0f/99.0f)*lvl)/4.0f);
		stats[Stats.MagicDefense.ordinal()] = (int)(((intel*3.0f)+(255.0f/99.0f)*lvl)/4.0f);
		
		
	}
	
	//pass item equipment slot and this function will equip the best 
	//(most Def or BP) item available
	public void equipBest(Item.Type type)
	{
		List<Item> itemList = new ArrayList<Item>();
		
		//build list of items of the appropriate type usable by this character		
		for(Item i : Global.party.getInventory())
			if(i.getType() == type && i.getUsableChars().contains(name))
				itemList.add(i);
		
		
		if(itemList.size() > 0)
		{
			Item bestItem = null;
			
			switch(type)
			{
			//weapon finds best BP
			case Weapon:
				int bestBPboost = -256;
				
				for(Item i : itemList)
				{
					int[] newStats = getModdedStats(i);
					if(newStats[Stats.BattlePower.ordinal()] - getStat(Stats.BattlePower) > bestBPboost)
					{
						bestBPboost = newStats[Stats.BattlePower.ordinal()] - getStat(Stats.BattlePower);
						if(bestBPboost > 0)
							bestItem = i;
					}
				}				
				break;
			//armor finds best Def
			case Torso:
			case Helmet:
			case Shield:
				int bestDefboost = -256;
				
				for(Item i : itemList)
				{
					int[] newStats = getModdedStats(i);
					if(newStats[Stats.Defense.ordinal()] - getStat(Stats.Defense) > bestDefboost)
					{
						bestDefboost = newStats[Stats.Defense.ordinal()] - getStat(Stats.Defense);
						if(bestDefboost > 0)
							bestItem = i;
					}
				}				
				break;
			//accessory finds either BP or def, whichever provides larger boost
			case Accessory:
				int bestBoost = -256;
				
				for(Item i : itemList)
				{
					int[] newStats = getModdedStats(i);
					if(newStats[Stats.Defense.ordinal()] - getStat(Stats.Defense) > bestBoost)
					{
						bestBoost = newStats[Stats.Defense.ordinal()] - getStat(Stats.Defense);
						if(bestBoost > 0)
							bestItem = i;
					}
					else if(newStats[Stats.BattlePower.ordinal()] - getStat(Stats.BattlePower) > bestBoost)
					{
						bestBoost = newStats[Stats.BattlePower.ordinal()] - getStat(Stats.BattlePower);
						if(bestBoost > 0)
							bestItem = i;
					}
				}				
				break;
			default:
				break;
			}
			
			if(bestItem != null)
				equip(bestItem.getId());
		}
		
	}
	
	public int[] getModdedStats(Item i)
	{
		int[] moddedStats = new int[Stats.NUM_STATS.ordinal()];		
		Item currEquipped = null;
		
		if(hasTypeEquipped(i.getType()))
			currEquipped = getEquippedItem(i.getType());
		
		equip(i.getId());
		updateSecondaryStats();
		
		for(int stat = 0; stat < Stats.NUM_STATS.ordinal(); ++stat)
			moddedStats[stat] = getStat(stat);
		
		if(currEquipped != null)
			equip(currEquipped.getId());
		else
			unequip(i.getType());	
		
		updateSecondaryStats();
		
		//return modded stats array		
		return moddedStats;
		
	}
	
	//get stats from removing item at given slot	
	public int[] getModdedStatsRmv(Type type)
	{
		int[] moddedStats = new int[Stats.NUM_STATS.ordinal()];
	
		Item currEquipped = null;
		
		if(hasTypeEquipped(type))
			currEquipped = getEquippedItem(type);
		
		unequip(type);
		updateSecondaryStats();
		
		for(int stat = 0; stat < Stats.NUM_STATS.ordinal(); ++stat)
			moddedStats[stat] = getStat(stat);
		
		if(currEquipped != null)
			equip(currEquipped.getId());
		
		
		updateSecondaryStats();
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
			modifyLevel(1, true);
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
	
	public void playWeaponAnimation(Point src, Point tar){if(weapEquipped()) weapon.playAnimation(src, tar);}
	public BattleAnim getWeaponAnimation(){if(weapEquipped()) return weapon.getAnim(); else return null;}
	
	public void renderShadow()
	{
		if (visible && isInBattle() && shadow != null)
		{
			Point centeredPosition = getPosition(true);
			shadow.setPosition(centeredPosition.x, centeredPosition.y);
			shadow.render();
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
