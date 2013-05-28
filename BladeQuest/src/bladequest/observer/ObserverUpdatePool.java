package bladequest.observer;

import java.util.ArrayList;
import java.util.List;

public class ObserverUpdatePool<T> {
	
	private class Triggered
	{
		Triggered(Observer<T> observer, List<T> list) 
		{
			this.observer = observer;
			this.list = list;
		}
		public Observer<T> observer;
		public List<T> list;
	};
	
	List<Triggered> triggeredObservers;
	int obsRef;
	
	private void rebuild()
	{
		triggeredObservers = new ArrayList<Triggered>();		
	}
	private void send()
	{
		List<Triggered> triggersToSend = triggeredObservers;
		
		rebuild();
		
		incref();
		for (Triggered t : triggersToSend)
		{
			Observer<T> observer = t.observer;
			for (T value : t.list)
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
		List<T> triggeredParents = null;
		for (Triggered t : triggeredObservers)
		{
			if (t.observer == child)
			{
				triggeredParents = t.list;
				break;
			}
		}
		if (triggeredParents == null)
		{
			triggeredParents = new ArrayList<T>();
			triggeredParents.add(parent);
			triggeredObservers.add(new Triggered(child, triggeredParents));
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
		if (--obsRef == 0 && !triggeredObservers.isEmpty())
		{
			send();
		}
	}
}
