package bladequest.UI.MsgBox;

import java.util.ArrayList;
import java.util.List;

import bladequest.UI.MsgBox.MsgBox.Options;

public class Message 
{
	private Options option;
	private String message;
	private List<MsgOption> options;
	
	public Options getOption() {return option;	}
	public String getMessage() {return message;	}

	public Message(String message, Options option)
	{
		this.message = message;
		this.option = option;
		
		options = new ArrayList<MsgOption>();
	}
	
	public List<MsgOption> getOptions(){ return options; }
	
	public void addOption(String text, MsgAction action)
	{
		options.add(new MsgOption(options.size(), text, action));
	}

}
