package bladequest.combat.triggers;

import bladequest.observer.Observable;

public interface Condition extends Observable<Condition> {
	boolean triggered(); //might also be getting unset!
}