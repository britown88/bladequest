package bladequest.context;


public abstract class ContextItem
{	
	 public abstract void activate();
	 public abstract Status update();
		
	 public void onStep(){}
	 public void onAdd(){}
	 public void onRemove(){}
	 public void onEnterMap(){}
	 public void onLeaveMap(){}
	 
	 public enum Status
	 {
		 Ready,
		 Running,
		 Finished
	 }
}
	

