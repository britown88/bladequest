package bladequest.combat.triggers;

import java.util.ArrayList;
import java.util.List;

import bladequest.observer.Observer;
import bladequest.system.Recyclable;


public abstract class Trigger implements Observer<Condition>, Recyclable
{
	List<Condition> conditions;
	List<Recyclable> conditionRegisters; 
	
	public void recycle()
	{
		for (Recyclable r : conditionRegisters)
		{
			r.recycle();
		}
	}
	public Trigger(Condition condition)
	{
		conditionRegisters = new ArrayList<Recyclable>();
		conditions = new ArrayList<Condition>();
		conditions.add(condition);
		for (Condition c : conditions)
		{
			conditionRegisters.add(c.register(this));
		}
	}
	
	public Trigger(List<Condition> conditions)
	{
		conditionRegisters = new ArrayList<Recyclable>();
		this.conditions = new ArrayList<Condition>(conditions);
		for (Condition c : conditions)
		{
			conditionRegisters.add(c.register(this));
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


