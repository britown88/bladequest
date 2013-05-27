package bladequest.actions;

import bladequest.UI.MsgBox.YesNo;
import bladequest.actions.Action;
import bladequest.actions.Action.YesNoResult;
import bladequest.world.*;


public class actMessage extends Action 
{
	String msg;
	boolean yesNoOpt;

	
	public actMessage(String str)
	{
		super();
		msg = str;
		this.yesNoOpt = false;

	}
	
	public boolean yesNo() { return yesNoOpt; }
	
	public actMessage(String str, boolean yesNoOpt)
	{
		super();
		msg = str;
		this.yesNoOpt = yesNoOpt;

	}
	
	@Override
	public void run()
	{
		Global.showMessage(msg, yesNoOpt);
	}
	
	@Override
	public boolean isDone()
	{
		if(!runningBranch)
		{
			if(Global.worldMsgBox.Closed())
			{
				startBranch(Global.worldMsgBox.getSelectedOpt() == YesNo.Yes ? 0 : 1);
				return !runningBranch;
			}
			else
				return false;
		}
		else
			return branchIsDone();
	}

}
