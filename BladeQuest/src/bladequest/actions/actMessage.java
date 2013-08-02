package bladequest.actions;

import java.util.ArrayList;
import java.util.List;

import bladequest.UI.MsgBox.Message;
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
	
	List<String> optionList;
	
	
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
	
	public void useOptionList(List<String> optionList)
	{
		this.optionList= new ArrayList<String>(optionList); 
	}
	
	public boolean splits(){return option != Options.None;}
	
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
				Message messageObj = new Message(msg, option);
				if(optionList != null)
				{
					int i = 0;
					for(String str : optionList)
					{
						messageObj.addOption(str, 
							new MsgAction() 
							{
								private int index;
								public MsgAction initialize(int index) {this.index = index; return this;}
								@Override
								public void execute(){startBranch(index);}
							}.initialize(i++));						
					}
				}
				
				Global.showMessage(messageObj, pos);				
				
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
