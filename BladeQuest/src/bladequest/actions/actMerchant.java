package bladequest.actions;

import bladequest.actions.Action;
import bladequest.world.*;


public class actMerchant extends Action 
{
	String merchName;
	float discount;
	
	public actMerchant(String merchName, float discount)
	{
		super();
		this.merchName = merchName;
		this.discount = discount;
	}

	@Override
	public void run()
	{
		Global.openMerchantScreen(merchName, discount);
	}
	
	@Override
	public boolean isDone()
	{
		return Global.merchantScreen.closed();
	}

}
