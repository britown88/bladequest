package bladequest.spudquest;

public abstract class SpudUIState 
{
	
	public abstract void run();
	public abstract boolean isDone();
	public abstract boolean blocks();
	
	public void render(){}
	public void touchActionDown(int x, int y){}
	public void touchActionUp(int x, int y){}
	public void touchActionMove(int x, int y){}

}
