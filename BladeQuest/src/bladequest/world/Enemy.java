package bladequest.world;

import android.graphics.*;

import java.util.*;


public class Enemy extends Character
{	
	private int gold;
	private String commonItem, rareItem;
	private int commonDropRate, rareDropRate, abilityChance;
	private boolean rareStealOnly;
	private boolean stolen;
	
	private boolean bossFight;
	
	private List<EnemyAbility> abilities = new ArrayList<EnemyAbility>();
	
	public Enemy(String name, String spr)
	{
		super("", name, spr, "");
		isEnemy = true;
		
		position = new Point();
		
		stolen = false;
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
		
		stolen = false;
	}
	
	public void setAI(int abilityChance){this.abilityChance = abilityChance;}
	
	public void addAbility(String abilityName, int chanceToCast, int healthAbove, int healthBelow)
	{
		abilities.add(new EnemyAbility(abilityName, chanceToCast, healthAbove, healthBelow));
	}
	
	public void Act()
	{
		if(Global.rand.nextInt(100) < abilityChance)
		{
			List<Ability> possibleAbilities = new ArrayList<Ability>();
			Ability a;
			for(EnemyAbility ea : abilities)
			{
				a = ea.cast((int)(((float)getHP()/(float)stats[Stats.MaxHP.ordinal()])*100.0f));
				if(a != null) possibleAbilities.add(a);
			}
			
			if(possibleAbilities.size() > 0)
			{
				action = Action.Ability;
				abilityToUse = possibleAbilities.get(Global.rand.nextInt(possibleAbilities.size()));
				return;				
			}
		}
		
		action = Action.Attack;
		return;
	}
	
	public void setBossMods(float HP)
	{
		bossFight = true;
		stats[Stats.MaxHP.ordinal()] *= HP;
		this.HP = stats[Stats.MaxHP.ordinal()];
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
	
	@Override
	public void battleRender()
	{
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
		stats[Stats.MaxHP.ordinal()] = (int)((((vit * 2.0f) + (255.0f/99.0f)*lvl) / 3.0f) * 20.0f * getCoefficient());
		stats[Stats.MaxMP.ordinal()] = (int)(((intel * 2.0f + (255.0f/99.0f)*lvl) / 3.0f) * 7.0f * getCoefficient());

	}
	
	@Override	
	public int getBattlePower()
	{
		float lvl = level;
		float str = stats[Stats.Strength.ordinal()];
		
		int bp = (int)(((str * 2.0f) + ((255.0f / 99.0f) * lvl)) / 3.0f);		
		stats[Stats.BattlePower.ordinal()] = bp;
		
		return bp;
		
	}
	
	@Override	
	public int getDefense()
	{
		float lvl = level;
		float vit = stats[Stats.Vitality.ordinal()];
		
		int def = (int)(((vit * 4.0f) + ((255.0f / 99.0f) * lvl)) / 5.0f);
		stats[Stats.Defense.ordinal()] = def;		
		
		return def;
	}
	
	



}
