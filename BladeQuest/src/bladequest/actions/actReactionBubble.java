package bladequest.actions;

import bladequest.graphics.ReactionBubble;
import bladequest.world.GameObject;
import bladequest.world.Global;


public class actReactionBubble extends Action 
{
	private String target;
	private ReactionBubble bubble;
	private float duration;
	private boolean wait, loop, close;
	
	public actReactionBubble(String name, String target, float duration, boolean loop, boolean wait)
	{
		super();
		this.bubble = new ReactionBubble(Global.reactionBubbles.get(name));
		this.target = target;
		this.duration = duration;
		this.loop = loop;
		this.wait = wait;
		this.close = false;
	}
	
	public actReactionBubble(String target)
	{
		this.target = target;
		this.close = true;
	}
	
	@Override
	public void run()
	{
		
		if(target.equals("party"))
		{
			if(close)
				Global.party.closeReactionBubble();
			else
				Global.party.openReactionBubble(bubble, duration, loop);
		}
		else
		{
			for(GameObject go : Global.map.Objects())
			{
				if(go.Name().equals(target))
				{
					if(close)
						go.closeReactionBubble();
					else
						go.openReactionBubble(bubble, duration, loop);
					break;
				}
					
			}
		}
	}
	
	@Override
	public boolean isDone()
	{
		if(wait)
			return bubble.isDone();
		else
			return true;
	}

}
