package bladequest.actions;

import bladequest.UI.MsgBox;
import bladequest.UI.MsgBox.Options;
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
	boolean timed;
	Position pos;
	float duration;
	
	MsgBox.Options option;

	
	public actMessage(String str)
	{
		super();
		msg = str;
		this.option = Options.None;
		pos = Position.Bottom;

	}
	
	public actMessage(String str, float seconds)
	{
		super();
		msg = str;
		this.option = Options.None;
		pos = Position.Bottom;
		duration = seconds;
		timed = true;

	}
	
	public actMessage(String str, Position pos)
	{
		super();
		msg = str;
		this.option = Options.None;
		this.pos = pos;

	}
	
	public boolean yesNo() { return option == Options.YesNo; }
	
	public actMessage(String str, MsgBox.Options option)
	{
		super();
		msg = str;
		this.option = option;
		pos = Position.Bottom;

	}
	
	@Override
	public void run()
	{
		if(timed)
			Global.showMessage(msg, duration);
		else
		{
			switch(pos)
			{
			case Top:
				Global.showMessageTop(msg, option);
				break;
			case Bottom:
				Global.showMessage(msg, option);
				break;
			default:
				break;
			}			
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
