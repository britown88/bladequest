package bladequest.world;

import java.util.*;

import android.util.Log;
import bladequest.bladescript.ParserException;
import bladequest.bladescript.ScriptVar;
import bladequest.enemy.Enemy;

public class Encounter 
{
	private List<Enemy> enemies;
	private List<ScriptVar> scripts;
	private String backdrop;
	private String name;
	private String music;
	
	public boolean disableRunning, isBossFight;
	
	public Encounter(String name, String backdrop)
	{
		this.name = name;
		this.backdrop = backdrop;
		enemies = new ArrayList<Enemy>();
		scripts = new ArrayList<ScriptVar>();
	}
	
	public void setMusic(String music)
	{
		this.music = music;
	}
	
	public String getMusic()
	{
		return music;
	}
	
	public Encounter(Encounter e)
	{
		this.name = e.name;
		this.enemies = new ArrayList<Enemy>();
		this.disableRunning = e.disableRunning;
		this.backdrop = e.backdrop;
		
		for(Enemy en : e.enemies)
			this.enemies.add(new Enemy(en));

		this.scripts = e.scripts;
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
	
	public void addScript(ScriptVar script) 
	{
		scripts.add(script);
	}
	public void onStart()
	{
		for (ScriptVar script : scripts)
		{
			try {
				script.apply(ScriptVar.toScriptVar(Global.battle));
			} catch (ParserException e) {
				e.printStackTrace();
				Log.d("Parser", e.what());
			}			
		}
	}
	
	public List<Enemy> Enemies()
	{
		return enemies;
	}
	
}
