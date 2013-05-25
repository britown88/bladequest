package bladequest.enemy;

import bladequest.world.Ability;

public abstract class AIState {
	
	//override this if you have state!
	public AIState clone()
	{
		return this;
	}
	public abstract Ability pickAbility(Enemy e);
}
