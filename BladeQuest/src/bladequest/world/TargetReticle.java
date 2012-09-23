package bladequest.world;

public class TargetReticle 
{
	private boolean show;
	
	private int duration, timer;
	
	public TargetReticle()
	{
		show = false;
		timer = 0;
		duration = 25;
	}
	
	public void show()
	{
		timer = 0;
		show = true;
	}
	
	public void update()
	{
		if(show)
		{
			if(timer++ >= duration)
			{
				show = false;
				timer = 0;
			}
		}
		
	}
	
	public void render()
	{
		if(show)
			Global.sprites.get("target").render(Global.mouseGridPos.x*32, Global.mouseGridPos.y*32, 0);
	}
	
	
	
	

}
