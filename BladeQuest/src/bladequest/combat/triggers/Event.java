package bladequest.combat.triggers;

import bladequest.observer.Observer;
import bladequest.observer.ObserverList;
import bladequest.system.Recyclable;
import bladequest.world.Global;

public class Event implements Condition{
	
	ObserverList<Condition> children;
	boolean beingTriggered;
	
	public Event()
	{
		children = new ObserverList<Condition>(Global.battle.updatePool);
		beingTriggered = false;
	}
	@Override
	public Recyclable register(Observer<Condition> observer) {
		return children.add(observer);
	}

	@Override
	public void remove(Observer<Condition> observer) {
		children.remove(observer);
	}

	@Override
	public boolean triggered() {
		return beingTriggered;
	}
	public void trigger()
	{
		if (!beingTriggered)
		{
			beingTriggered = true;
			//may become problematic if this doesn't fire instantly?
			children.push(this);
			beingTriggered = false;
		}
	}
	
}