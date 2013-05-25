package bladequest.observer;

import bladequest.system.Recyclable;
import bladequest.observer.Observer;

public interface Observable<T> {
   Recyclable register(Observer<T> observer);
   void remove(Observer<T> observer);
}
