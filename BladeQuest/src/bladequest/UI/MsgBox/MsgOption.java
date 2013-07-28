package bladequest.UI.MsgBox;

public class MsgOption 
{
	private int index;
	private String text;
	private MsgAction action;
	private boolean disabled;
	
	public MsgOption(int index, String text, MsgAction action)
	{
		this.index = index;
		this.text = text;
		this.action = action;
	}
	
	public int getIndex() {	return index;	}
	public String getText() {	return text;	}
	public MsgAction getAction() {	return action;	}
	
	public void setDisabled(boolean disabled){this.disabled = disabled;}
	public boolean disabled(){return disabled;}
	
	

}
