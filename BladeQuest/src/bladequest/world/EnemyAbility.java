package bladequest.world;

import java.util.*;

public class EnemyAbility 
{
	private Ability ability;
	private int chanceToCast, healthBelow, healthAbove;

	
	public EnemyAbility(String ability, int chance, int healthAbove, int healthBelow)
	{
		this.ability = Global.abilities.get(ability);
		this.chanceToCast = chance;
		this.healthAbove = healthAbove;
		this.healthBelow = healthBelow;
	}
	
	public Ability cast(int healthPercent)
	{
		Ability ab = null;
		if(healthPercent > healthAbove && healthPercent <= healthBelow)
		{
			if(Global.rand.nextInt(100) < chanceToCast)
				ab = ability;
		}
		return ab;
	}
	

}
