package bladequest.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObserverUpdatePool<T> {
	Map<Observer<T>, List<T> > triggeredObservers;
	List<Observer<T>> ordering;
	int obsRef;
	
	private void rebuild()
	{
		ordering = new ArrayList<Observer<T> >();
		triggeredObservers = new HashMap<Observer<T>, List<T> >();		
	}
	private void send()
	{
		Map<Observer<T>, List<T> > triggersToSend = triggeredObservers;
		List<Observer<T>> order = ordering;
		
		rebuild();
		
		incref();
		for (Observer<T>  observer : order)
		{
			for (T value : triggersToSend.get(observer))
			{
				observer.next(value);
			}
			observer.finishStream();
		}
		decref();
	}
	public ObserverUpdatePool()
	{
		obsRef = 0;
		rebuild();
	}
	
	public void pushObserver(Observer<T> child, T parent)
	{
		List<T> triggeredParents = triggeredObservers.get(child);
		if (triggeredParents == null)
		{
			triggeredParents = new ArrayList<T>();
			triggeredParents.add(parent);
			triggeredObservers.put(child, triggeredParents);
			ordering.add(child);
		}
		else
		{
			triggeredParents.add(parent);
		}
		//not mid-send, send immediately
		if (obsRef == 0)
		{
			send();
		}
	}
	public void incref()
	{
		++obsRef;
	}
	void decref()
	{
		if (--obsRef == 0 && !ordering.isEmpty())
		{
			send();
		}
	}
}
