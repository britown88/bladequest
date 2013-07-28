package bladequest.actions;

import bladequest.UI.MsgBox.MsgAction;
import bladequest.UI.MsgBox.MsgBox;
import bladequest.UI.MsgBox.MsgBox.Options;
import bladequest.UI.MsgBox.MsgBox.Position;
import bladequest.world.Global;


public class actMessage extends Action 
{	
	private String msg;
	private boolean timed;
	private Position pos;
	private float duration;
	
	MsgBox.Options option;
	
	public actMessage(String str, MsgBox.Options option, Position pos)
	{
		super();
		msg = str;
		this.option = option;
		this.pos = pos;
	}
	
	public void setTimed(float duration)
	{
		timed = true;
		this.duration = duration;
	}
	
	@Override
	public void run()
	{
		if(timed)
			Global.showTimedMessage(msg, pos, duration);
		else
			switch(option)
			{
			case None:
				Global.showBasicMessage(msg, pos);
				break;
			case YesNo:
				Global.showYesNoMessage(msg, pos, 
						new MsgAction() {
							@Override
							public void execute()
							{
								startBranch(0);
							}
						},
						new MsgAction() {
							@Override
							public void execute(){startBranch(1);
							}
						});
				break;
			case List:
				break;
			}
	}
	
	@Override
	public boolean isDone()
	{
		if(!runningBranch)
		{
			if(Global.worldMsgBox.Closed())
				return !runningBranch;
			else
				return false;
		}
		else
			return branchIsDone();
	}

}
