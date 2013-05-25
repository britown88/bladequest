package bladequest.observer;

public interface Observer<T> {
	void next(T obj);
	void finishStream();
}
