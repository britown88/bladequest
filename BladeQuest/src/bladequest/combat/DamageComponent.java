package bladequest.combat;

import bladequest.world.Stats;

public class DamageComponent 
{
	public DamageComponent (Stats affinity, float power)
	{
		this.affinity = affinity;
		this.power = power;
	}
	
	public DamageComponent(DamageComponent other)
	{
		this.affinity = other.affinity;
		this.power = other.power;
	}
	
	public Stats getAffinity() {return affinity;}
	public float getPower() { return power;}
	
	private Stats affinity;
	private float power;

}
