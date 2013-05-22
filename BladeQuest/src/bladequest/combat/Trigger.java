package bladequest.combat;

public abstract class Trigger {
	public static abstract class Condition
	{
		public abstract boolean triggered();
	}
	public abstract void trigger();
}
