package bladequest.enemy;

import java.util.HashMap;
import java.util.Map;

public class AI {
	Map<String, AIState> stateMap;
	AIState state;
	
	public AI()
	{
		stateMap = new HashMap<String,AIState>();
		state = null;
	}
	public AI(AI rhs)
	{
		stateMap = new HashMap<String,AIState>();
		for (Map.Entry<String, AIState> entry : rhs.stateMap.entrySet()) {
		    String key = entry.getKey();
		    AIState value = entry.getValue();
		    AIState cloned = value.clone();
		    
		    
		    if (value == rhs.state)
		    {
		    	state = cloned;
		    }
		    stateMap.put(key, value);
		}
	}
	public void add(String string, AIState state)
	{
		stateMap.put(string, state);
	}
	public void switchToState(String string)
	{
		state = stateMap.get(string);
	}
	public AIState getState()
	{
		return state;
	}
}
