package bladequest.enemy;


public abstract class AIState {
	
	//override this if you have state!
	public AIState clone()
	{
		return this;
	}
	public abstract void runAI(Enemy e, AIDecision decision);
}
