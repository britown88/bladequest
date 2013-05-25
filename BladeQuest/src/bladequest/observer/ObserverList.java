package bladequest.observer;

import java.util.LinkedList;
import java.util.List;

import bladequest.system.Recyclable;

public class ObserverList<T> {
  List<Observer<T> > observers;
  ObserverUpdatePool<T> pool;
  
  public ObserverList(ObserverUpdatePool<T> pool)
  {
	  observers = new LinkedList<Observer<T> >();
	  this.pool = pool;
  }
  public Recyclable add(Observer<T> observer)
  {
	  observers.add(observer);
	  
	  return new Recyclable()
	  {
		  ObserverList<T> list;
		  Observer<T> observer;
		  Recyclable initialize(ObserverList<T> list, Observer<T> observer)
		  {
			  this.list = list;
			  this.observer = observer;
			  return this;
		  }
		  public void recycle() {
			  list.remove(observer);
		  }

	  }.initialize(this, observer);
  }
  public void remove(Observer<T> observer)
  {
	  observers.remove(observer);
  }
  public void push(T obj)
  {
	  pool.incref();
	  for (Observer<T> observer : observers)
	  {
		  pool.pushObserver(observer, obj);
	  }
	  pool.decref();
  }
}
