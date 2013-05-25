package bladequest.world;

import java.util.*;

import bladequest.enemy.Enemy;

public class Encounter 
{
	private List<Enemy> enemies;
	private String backdrop;
	private String name;
	
	public boolean disableRunning, isBossFight;
	
	public Encounter(String name, String backdrop)
	{
		this.name = name;
		this.backdrop = backdrop;
		enemies = new ArrayList<Enemy>();
	}
	
	public Encounter(Encounter e)
	{
		this.name = e.name;
		this.enemies = new ArrayList<Enemy>();
		this.disableRunning = e.disableRunning;
		this.backdrop = e.backdrop;
		
		for(Enemy en : e.enemies)
			this.enemies.add(new Enemy(en));

	}
	
	public void addEnemy(String enemy, int x, int y)
	{
		Enemy e = new Enemy(Global.enemies.get(enemy));

		if(e != null)
		{
			if(e.isBoss())
				isBossFight = true;
			e.setPosition(x, y);
			enemies.add(e);
		}
		
	}
	
	public List<Enemy> Enemies()
	{
		return enemies;
	}
	
}
