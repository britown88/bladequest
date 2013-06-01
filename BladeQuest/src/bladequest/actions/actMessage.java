package bladequest.actions;

import bladequest.UI.MsgBox.YesNo;
import bladequest.world.Global;


public class actMessage extends Action 
{
	public enum Position
	{
		Top,
		Bottom
	}
	
	String msg;
	boolean yesNoOpt;
	Position pos;

	
	public actMessage(String str)
	{
		super();
		msg = str;
		this.yesNoOpt = false;
		pos = Position.Bottom;

	}
	
	public actMessage(String str, Position pos)
	{
		super();
		msg = str;
		this.yesNoOpt = false;
		this.pos = pos;

	}
	
	public boolean yesNo() { return yesNoOpt; }
	
	public actMessage(String str, boolean yesNoOpt)
	{
		super();
		msg = str;
		this.yesNoOpt = yesNoOpt;
		pos = Position.Bottom;

	}
	
	@Override
	public void run()
	{
		switch(pos)
		{
		case Top:
			Global.showMessageTop(msg, yesNoOpt);
			break;
		case Bottom:
			Global.showMessage(msg, yesNoOpt);
			break;
		default:
			break;
		}
		
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
