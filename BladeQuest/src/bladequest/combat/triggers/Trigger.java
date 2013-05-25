package bladequest.combat.triggers;

import java.util.ArrayList;
import java.util.List;

import bladequest.observer.Observer;


public abstract class Trigger implements Observer<Condition>
{
	List<Condition> conditions;
	
	public Trigger(Condition condition)
	{
		conditions = new ArrayList<Condition>();
		conditions.add(condition);
		for (Condition c : conditions)
		{
			c.register(this);
		}
	}
	
	public Trigger(List<Condition> conditions)
	{
		this.conditions = conditions;
		for (Condition c : conditions)
		{
			c.register(this);
		}
	}
	
	@Override
	public void next(Condition cond) {}

	@Override
	public void finishStream() 
	{
		for (Condition c : conditions)
		{
			if (!c.triggered()) return;
		}
		trigger();
	}	
	
	public abstract void trigger();
}


