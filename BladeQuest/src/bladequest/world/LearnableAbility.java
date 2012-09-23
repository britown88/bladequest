package bladequest.world;

public class LearnableAbility 
{
	private Ability ability;
	private int levelReq;
	
	public LearnableAbility(String name, int levelReq)
	{
		this.ability =Global.abilities.get(name);
		this.levelReq = levelReq;
	}
	
	public Ability GetAbility(){return ability;}
	public int LevelReq() { return levelReq; }

}
