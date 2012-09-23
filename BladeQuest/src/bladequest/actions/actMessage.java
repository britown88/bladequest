package bladequest.actions;

import bladequest.actions.Action;
import bladequest.world.*;


public class actMessage extends Action 
{
	String msg;
	boolean yesNoOpt;
	public int yesDelta, noDelta; 
	public Action skipTo;
	
	public actMessage(String str)
	{
		super();
		msg = str;
		this.yesNoOpt = false;
		yesDelta = 0;
		noDelta = 0;
	}
	
	public boolean yesNo() { return yesNoOpt; }
	
	public actMessage(String str, boolean yesNoOpt)
	{
		super();
		msg = str;
		this.yesNoOpt = yesNoOpt;
		yesDelta = 0;
		noDelta = 0;
	}
	
	@Override
	public void run()
	{
		Global.showMessage(msg, yesNoOpt);
	}
	
	@Override
	public boolean isDone()
	{
		return Global.worldMsgBox.Closed();
	}

}
