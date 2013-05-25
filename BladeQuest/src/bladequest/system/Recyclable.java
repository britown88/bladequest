package bladequest.system;

public interface Recyclable {
  public void recycle();
  
  class Extensions
  {
	  public static void tryRecycle(Object obj)
	  {
		  Recyclable toRecycle = (Recyclable)obj;
		  if (toRecycle != null)
		  {
			  toRecycle.recycle();
		  }
	  }
  }
}
