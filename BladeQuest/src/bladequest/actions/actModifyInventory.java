package bladequest.actions;

import bladequest.world.*;

public class actModifyInventory extends Action
{
	int count;
	String item;
	boolean remove;

	public actModifyInventory(String item, int count, boolean remove )
	{
		super();
		this.count = count;
		this.item = item;
		this.remove = remove;
	}
	
	@Override
	public void run()
	{
		if(remove)
		{
			Global.party.removeItem(item);
		}
		else
		{
			int j = count;
			do
			{
				Global.party.addItem(item);
			}while(--j > 0);
		}
		
		done = true;
	}
	
	@Override
	public boolean isDone()
	{
		return done;
	}
}
