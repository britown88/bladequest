package bladequest.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.graphics.Point;
import android.graphics.Rect;
import bladequest.combatactions.CombatAction;
import bladequest.graphics.BattleAnim;
import bladequest.graphics.BattleSprite;
import bladequest.graphics.BattleSprite.faces;
import bladequest.graphics.Sprite;
import bladequest.graphics.WeaponSwingDrawable;
import bladequest.statuseffects.StatusEffect;
import bladequest.statuseffects.seKO;
import bladequest.world.Item.Type;

public class Character 
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
	
	private boolean dead;
	private String abilitiesName;
	
	protected int level;
	protected int exp;
	private int expToLevel;	
	
	protected BattleSprite battleSpr;
	private Sprite worldSpr;
	public Point portrait;
	
	protected List<Character> targets;
	
	protected int imageIndex;
	
	protected int index;	
	private Item itemToUse;
	protected Ability abilityToUse;
	protected Action action;
	
	private CombatAction combAction;
	
	private List<StatusEffect> statusEffects;
	private List<Ability> abilities;
	private List<LearnableAbility> learnableAbilities;
	
	public Character(String name, String displayNam, String bSpr, String wSpr)
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
		targets = new Vector<Character>();
		statusEffects = new ArrayList<StatusEffect>();
		abilities = new ArrayList<Ability>();
		learnableAbilities = new ArrayList<LearnableAbility>();
		portrait = new Point(0,0);
		position = new Point(0,0);
	}
	
	public Character(Character c)
	{
		targets = new Vector<Character>();
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
	
	
	public int getExp(){return exp;}
	public int getRemainingExp(){return expToLevel - exp;}
	public Sprite getWorldSprite() { return worldSpr; }	
	public BattleSprite getBattleSprite() { return battleSpr; }
	public void setIndex(int i){index = i;}	
	public int Index(){return index;}	
	public List<Character> getTargets(){return targets;}	
	public void setTargets(List<Character> targets){this.targets = targets;}	
	public void addTarget(Character e){targets.add(e);}	
	public void clearTargets(){targets.clear();}
	public String getActionName() { return combAction.getName(); }
	public String getAbilitiesName() { return abilitiesName; }	
	public List<Ability> getAbilities() { return abilities; }
	public Item getItemToUse(){return itemToUse;}	
	public Ability getAbilityToUse(){return abilityToUse;}	
	public Action getAction(){return action;}	
	public String getDisplayName(){return displayName;}	
	public List<StatusEffect> getStatusEffects() { return statusEffects; }
	public String getName(){return name;}	
	public boolean isEnemy() { return isEnemy; }	
	public void setHpMp(int hp, int mp){ this.HP = hp; this.MP = mp; }
	
	public void setBattleAction(Action action){this.action = action;}
	
	public int getStat(Stats stat)
	{
		if(stat == Stats.MaxHP)
			return Math.min(9999, stats[stat.ordinal()] + statMods[stat.ordinal()]);
		else if(stat == Stats.MaxMP)
			return Math.min(999, stats[stat.ordinal()] + statMods[stat.ordinal()]);
		else
			return Math.min(255, stats[stat.ordinal()] + statMods[stat.ordinal()]);
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
		
		for(Item i : Global.party.getInventory(false))
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
		}
	}
	
	public void useItem(Character target)
	{	
		unuseItem();
		Global.party.removeItem(itemToUse.getId(), 1);
		itemToUse.execute(this, targets);
	}
	
	public TargetTypes getCombatActionTargetType() { return combAction.getTargetType(); }
	public String getCombatActionText() { return combAction.getActionText(); }
	
	public void setUseCombatAction()
	{
		action = Action.CombatAction;

	}
	
	public void useCombatAction()
	{
		combAction.execute(targets);
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
	
	public Ability  useAbility()
	{	
		MP -= abilityToUse.MPCost();
		
		/*if(Global.rand.nextInt(100) < abilityToUse.Accuracy())
			abilityToUse.execute(this, targets);
		else
		{
			for(Character t : targets)
				Global.battle.dmgText(t, "MISS", 0);
		}*/
		
		return abilityToUse;
			
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
		
		str = stats[Stats.Strength.ordinal()];
		agi = stats[Stats.Agility.ordinal()];
		vit = stats[Stats.Vitality.ordinal()];
		intel = stats[Stats.Intelligence.ordinal()];
		
		//speed based on agi
		//( ( Agility x 3) + ( 255 / 99 ) x Level ) / 4
		stats[Stats.Speed.ordinal()] = (int)(((agi*3.0f)+(255.0f/99.0f)*lvl)/4.0f);
		
		//hp/mp based on vit and int	
		stats[Stats.MaxHP.ordinal()] = Math.min(9999, (int)((((vit * 2.0f) + (255.0f/99.0f)*lvl) / 3.0f) * 15.0f * getCoefficient()));
		stats[Stats.MaxMP.ordinal()] = Math.min(999, (int)(((intel * 2.0f + (255.0f/99.0f)*lvl) / 3.0f) * 5.0f * getCoefficient()));

	}
	private int calcBP(float lvl, float str, float w){return (int)(((str * 2.0f) + (w * 2.0f) + ((255.0f / 99.0f) * lvl)) / 5.0f);}
	private int calcDef(float lvl, float vit, float arm, float sh) { return (int)(((vit * 2.0f) + (arm * 2.0f) + (sh * 2.0f) + ((255.0f / 99.0f) * lvl)) / 5.0f);}
	public int getBattlePower()
	{
		float lvl = level;
		float str = Math.min(255, stats[Stats.Strength.ordinal()] + statMods[Stats.Strength.ordinal()]);
		float w = weapEquipped() ? weapon.Power() : 0.0f;
		
		int bp = calcBP(lvl, str, w);
		stats[Stats.BattlePower.ordinal()] = bp;
		
		return Math.min(255, bp + statMods[Stats.BattlePower.ordinal()]);
		
	}
	public int getDefense()
	{
		float lvl = level;		
		float vit = Math.min(255, stats[Stats.Vitality.ordinal()] + statMods[Stats.Vitality.ordinal()]);
		float arm = (helmEquipped() ? helmet.Power() : 0.0f) + (torsoEquipped() ? torso.Power() : 0.0f);
		float sh = shieldEquipped() ? shield.Power() : 0.0f;
		
		int def = calcDef(lvl, vit, arm, sh);
		stats[Stats.Defense.ordinal()] = def;		

		return Math.min(255, def + statMods[Stats.Defense.ordinal()]);
	}
	
	//pass item equipment slot and this function will equip the best 
	//(most Def or BP) item available
	public void equipBest(Item.Type type)
	{
		List<Item> itemList = new ArrayList<Item>();
		
		//build list of items of the appropriate type usable by this character		
		for(Item i : Global.party.getInventory(false))
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
			}
			
			if(bestItem != null)
				equip(bestItem.getId());
		}
		
	}
	
	public int[] getModdedStats(Item i)
	{
		int[] moddedStats = new int[Stats.NUM_STATS.ordinal()];
		
		//fill with current statmods
		for(int j = 0; j < Stats.NUM_STATS.ordinal(); ++j)
			moddedStats[j] = statMods[j];
		
		//remove currently equipped stat mods
		if(hasTypeEquipped(i.getType()))
			for(int j = 0; j < Stats.NUM_STATS.ordinal(); ++j)
				moddedStats[j] -= getEquippedItem(i.getType()).getStatMod(j);
		
		//add new item statmods (if new item is being equipped)
		for(int j = 0; j < Stats.NUM_STATS.ordinal(); ++j)
			moddedStats[j] += i.getStatMod(j);
		
		//update BP and Def and add existing stats
		getBattlePower();
		getDefense();
		for(int j = 0; j < Stats.NUM_STATS.ordinal(); ++j)
			moddedStats[j] += stats[j];
		
		//if weapon, remove current BP, calculate new BP and add to moddedStats
		if(i.getType() == Type.Weapon)
		{
			moddedStats[Stats.BattlePower.ordinal()] -= stats[Stats.BattlePower.ordinal()];
			float lvl = level;
			float str = Math.min(255, moddedStats[Stats.Strength.ordinal()]);
			float w = i.Power();
			moddedStats[Stats.BattlePower.ordinal()] += calcBP(lvl, str, w);
			
		}
		
		//if shield, torso, or helmet, remove current def, calculate new def and add to Modded Stats
		else if(i.getType() == Type.Torso || i.getType() == Type.Shield || i.getType() == Type.Helmet)
		{
			moddedStats[Stats.Defense.ordinal()] -= stats[Stats.Defense.ordinal()];
			float lvl = level;		
			float vit = Math.min(255, moddedStats[Stats.Vitality.ordinal()]);
			float arm = ((i.getType() == Type.Helmet) ? i.Power() : (helmEquipped() ? helmet.Power() : 0.0f) )
					+ ((i.getType() == Type.Torso) ? i.Power() : (torsoEquipped() ? torso.Power() : 0.0f));
			float sh = (i.getType() == Type.Shield) ? i.Power() : (shieldEquipped() ? shield.Power() : 0.0f);
			moddedStats[Stats.Defense.ordinal()] += calcDef(lvl, vit, arm, sh);
			
		}
		//return modded stats array		
		return moddedStats;
		
	}
	
	//get stats from removing item at given slot	
	public int[] getModdedStatsRmv(Type type)
	{
		int[] moddedStats = new int[Stats.NUM_STATS.ordinal()];
		
		//fill with current statmods
		for(int j = 0; j < Stats.NUM_STATS.ordinal(); ++j)
			moddedStats[j] = statMods[j];
		
		//remove currently equipped stat mods
		if(hasTypeEquipped(type))
			for(int j = 0; j < Stats.NUM_STATS.ordinal(); ++j)
				moddedStats[j] -= getEquippedItem(type).getStatMod(j);
		
		//update BP and Def and add existing stats
		getBattlePower();
		getDefense();
		for(int j = 0; j < Stats.NUM_STATS.ordinal(); ++j)
			moddedStats[j] += stats[j];
		
		//if weapon, remove current BP, calculate new BP and add to moddedStats
		if(type == Type.Weapon)
		{
			moddedStats[Stats.BattlePower.ordinal()] -= stats[Stats.BattlePower.ordinal()];
			float lvl = level;
			float str = Math.min(255, moddedStats[Stats.Strength.ordinal()]);
			float w = 0.0f;
			moddedStats[Stats.BattlePower.ordinal()] += calcBP(lvl, str, w);
			
		}
		
		//if shield, torso, or helmet, remove current def, calculate new def and add to Modded Stats
		else if(type == Type.Torso || type == Type.Shield || type == Type.Helmet)
		{
			moddedStats[Stats.Defense.ordinal()] -= stats[Stats.Defense.ordinal()];
			float lvl = level;		
			float vit = Math.min(255, moddedStats[Stats.Vitality.ordinal()]);
			float arm = ((type == Type.Helmet) ? 0.0f : (helmEquipped() ? helmet.Power() : 0.0f) )
					+ ((type == Type.Torso) ? 0.0f : (torsoEquipped() ? torso.Power() : 0.0f));
			float sh = (type == Type.Shield) ? 0.0f : (shieldEquipped() ? shield.Power() : 0.0f);
			moddedStats[Stats.Defense.ordinal()] += calcDef(lvl, vit, arm, sh);
			
		}
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
		for(StatusEffect s : statusEffects)
			if(s.Name().equals(se))
			{
				s.onRemove(this);
				statusEffects.remove(s);
				//setIdle(false);
				return;
			}		
	}
	
	public void applyStatusEffect(StatusEffect se)
	{
		if(!dead || se.Name().equals("KO"))
		{
			for(StatusEffect s : statusEffects)
				if(s.Name().equals(se.Name()))
					return;
			
			se.onInflict(this);
			setFace(battleSpr.getFace());
				
			statusEffects.add(se);
		}
		
	}
	
	public void statusOnTurn(Battle b)
	{
		for(StatusEffect se : statusEffects)
		{
			se.onTurn(this, b);
		}
	}
	

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
		for(LearnableAbility la : learnableAbilities)
		{
			if(la.LevelReq() <= level)
			{
				boolean alreadyHas = false;
				
				//check if ability already exists
				for(Ability ab : abilities)
					if(ab.name.equals(la.GetAbility().name))
					{
						learnableAbilities.remove(la);
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
		}
		return "";
	}
	
	public void addAbility(String name)
	{
		abilities.add(Global.abilities.get(name));
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

	
	private boolean hasNegativeStatus()
	{
		for(StatusEffect se : statusEffects)
			if(se.isNegative())
				return true;
		
		return false;
	}

	
	private BattleSprite.faces savedFace;
	public void showDamaged()
	{
		BattleSprite.faces face = battleSpr.getFace();
		
		if(face != faces.Attack)
		{
			savedFace = face;			
			setFace(faces.Damaged);
		}
	}
	public void clearDamaged()
	{
		BattleSprite.faces face = battleSpr.getFace();
		if(face == faces.Damaged)
			setFace(savedFace);
	}
	
	
	public void setFace(BattleSprite.faces newFace)
	{
		if(!isEnemy)
		{
			BattleSprite.faces oldFace = battleSpr.getFace();
			boolean weak = HP <= (float)stats[Stats.MaxHP.ordinal()]*0.25F || hasNegativeStatus();
			
			switch(newFace)
			{
			case Idle:
			case Ready:
				if(dead) setFace(faces.Dead);
				else if (weak) setFace(faces.Weak);
				else battleSpr.changeFace(newFace);
				break;
			case Damaged:
			case Attack:
			case Cast:
			case Casting:		
			case Dead:		
			case Use:
			case Victory:
			case Weak:
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
	
	public void battleRender()
	{
		if(battleSpr.getFace() != faces.Attack)
			updateAnimation();//dont update attack anim, it's managed by battle
		
		battleSpr.render(position.x, position.y, imageIndex, false);
		
		//draw weapon swing
		if(battleSpr.getFace() == faces.Attack)
		{
			WeaponSwingDrawable swing = getWeaponSwing();
			if(swing != null)
				swing.render(imageIndex, position.x-20, position.y-6);
		}
			
		
	}
	
	public Rect getRect() {return Global.vpToScreen(new Rect(position.x, position.y, position.x+getWidth(), position.y+getHeight()));}
	public Point getPosition() { return position; }	
	public Point getPosition(boolean center) 
	{ 
		Point p = new Point(position);
		if(center && !isEnemy)
			p.offset(battleSpr.getWidth()/2, battleSpr.getHeight()/2);
		
		return p; 
	}	
	public int getWidth() { return battleSpr.getWidth(); }
	public int getHeight() { return battleSpr.getHeight(); }
	public void setPosition(int x, int y) { position = new Point(x, y);}	
	public void setPosition(Point p) {position = p;}
	
	public enum Action
	{
		Attack,
		Item,
		Guard,
		CombatAction,
		Ability
	}

}
