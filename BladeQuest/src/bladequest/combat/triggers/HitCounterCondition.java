package bladequest.combat.triggers;

import bladequest.observer.Observer;
import bladequest.observer.ObserverList;
import bladequest.system.Recyclable;
import bladequest.world.Global;

//normally based off of a root event.
public class HitCounterCondition implements ChildCondition {

	ObserverList<Condition> children;
	int hitCount = 0;
	int targetCount;
	boolean newlyTriggered;
	public HitCounterCondition(int targetCount)
	{
		this.targetCount = targetCount;
		children = new ObserverList<Condition>(Global.battle.updatePool);
		newlyTriggered = false;
	}
	
	@Override
	public boolean triggered() {
		return hitCount >= targetCount;
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
	public void next(Condition obj) {
		++hitCount;
		if (hitCount == targetCount) newlyTriggered = true;
	}

	@Override
	public void finishStream() {
		if (newlyTriggered)
		{
			newlyTriggered = false;
			children.push(this);
		}
	}

}
