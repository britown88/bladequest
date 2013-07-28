package bladequest.UI.MsgBox;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import bladequest.UI.ListBox;
import bladequest.UI.MenuPanel;
import bladequest.UI.ListBox.LBStates;
import bladequest.UI.MenuPanel.Anchors;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class MsgBox extends MenuPanel
{
	public enum Position
	{
		Top,
		Center,
		Bottom
	}
	
	private String msg;

	private Paint txtPaint, txtPaintCenter;	
	private static final int buffer =10;
	public static final int msgBoxHeight = 96;
	public static final int optionRowHeight = 40;
	
	private List<String> rowList;
	private List<Message> msgQueue;
	
	
	private int textSpeed;
	private int textTimer;
	private int currentRow;
	private int currentChar;
	private String partialRow;
	private boolean done;
	
	private ListBox yesNoMenu, optionsMenu;
	private boolean hasCharName;
	
	private final char wildCard = '|';
	
	private int rowCount;
	
	public boolean alwaysSpeed0, timed;
	private long startTime;
	private float duration;
	
	public enum Options
	{
		None,
		YesNo,
		List
	}

	public MsgBox()
	{
		super(Global.vpWidth/2, Global.vpHeight - buffer, 0, msgBoxHeight - buffer*2);
		setOpenSize(320 - buffer*2, msgBoxHeight - buffer*2);
		openSpeed= 30;
		
		rowCount = 4;
		
		txtPaint = Global.textFactory.getTextPaint(13, Color.WHITE, Align.LEFT);
		txtPaintCenter = Global.textFactory.getTextPaint(13, Color.WHITE, Align.CENTER);

		anchor = Anchors.BottomCenter;
		for(int i = 0; i < rowCount; ++i)
			addTextBox("", 8, (int)(openSize.y * (0.2f*(i+1))), txtPaint);
		
		yesNoMenu = new ListBox(Global.vpWidth/2, Global.vpHeight - buffer, 0, optionRowHeight, 1, 2, txtPaintCenter);
		yesNoMenu.anchor = Anchors.BottomCenter;
		yesNoMenu.setOpenSize(openSize.x, optionRowHeight);
		yesNoMenu.openSpeed = 30;
		
		rowList = new ArrayList<String>();		
		msgQueue = new ArrayList<Message>();
		alwaysSpeed0 = false;
		//clear(msg);		
	}
	
	public void setPosition(Position newPos)
	{
		switch(newPos)
		{
		case Bottom:
			pos.y = Global.vpHeight - buffer;
			anchor = Anchors.BottomCenter;
			break;
		case Top:
			pos.y = buffer;
			anchor = Anchors.TopCenter;
			break;
		case Center:
			pos.y = Global.vpHeight / 2;
			anchor = Anchors.TrueCenter;
			break;
		}
		
		update();
		
	}
		
	public void addBasicMessage(String msg)
	{
		msgQueue.add(new Message(msg, Options.None));
	}
	
	public void addMessage(Message msg)
	{
		msgQueue.add(msg);
	}
	
	public void addYesNoMessage(String message, MsgAction yesAction, MsgAction noAction)
	{		
		Message msg = new Message(message, Options.YesNo);
		msg.addOption("Yes", yesAction);
		msg.addOption("No", noAction);
		
		msgQueue.add(msg);
	}
	
	@Override
	public void clear()
	{		
		for(int i = 0; i < rowCount; ++i)
			textBoxes.get(i).text = "";
		
		rowList.clear();
		
		textSpeed = alwaysSpeed0 ? 0 : 6 - Global.textSpeed;
		textTimer = 0;
		currentRow = 0;
		currentChar = 0;
		partialRow = "";
		
		done = false;				
	}
	
	//forces next message
	public void nextMessage()
	{
		Global.delay();
		msgQueue.remove(0);
		if(msgQueue.size() > 0)					
			showCurrentMessage();
		else					
			close();
	}
	
	private void showCurrentMessage()
	{
		Message currentMsg = msgQueue.get(0);
		msg = currentMsg.getMessage();
		hasCharName = msg.indexOf(wildCard) != -1;
		
		clear();
		rowSplit();	
		
				
	}
	
	private void openYesNo()
	{
		yesNoMenu.clearObjects();
		for(MsgOption opt : msgQueue.get(0).getOptions())
			yesNoMenu.addItem(opt.getText(), opt.getAction(), opt.disabled());
		
		yesNoMenu.open();
		//TODO make this work for top vs bottom
		move(Global.vpWidth/2, Global.vpHeight - buffer*2 - optionRowHeight, 5);		
	}
	
	private void setSpeed(int s){textSpeed = s;}	
	public boolean isDone(){return done;}
	
	@Override
	public void close()
	{
		super.close();	
		yesNoMenu.close();
		msgQueue.clear();
	}
	
	@Override
	public void setClosed()
	{
		super.setClosed();
		yesNoMenu.setClosed();
		msgQueue.clear();
	}
	
	@Override
	public void open()
	{
		super.open();
		
		if(msgQueue.size() > 0)
			showCurrentMessage();
		
		//yesNoMenu.open();
		
		Global.delay();
		
		timed = false;
	}
	
	public void open(float seconds)
	{
		super.open();
		
		if(msgQueue.size() > 0)
			showCurrentMessage();
		
		Global.delay();
		
		timed = true;
		duration = seconds;
		startTime = System.currentTimeMillis();
		
	}
	
	@Override
	public void render()
	{
		if(!Closed())
		{
			super.render();
			if(!yesNoMenu.Closed())
				yesNoMenu.render();
		}
			
	}
	
	@Override	
	public void update()
	{
		super.update();
		yesNoMenu.update();
		
		if(timed && System.currentTimeMillis() - startTime >= duration*1000.0f)
		{
			yesNoMenu.close();
			close();
			return;
		}
		
		if(!done  && Opened())
		{
			textTimer++;
			if(textTimer >= textSpeed)
			{
				textTimer = 0;
				if(currentRow < rowList.size())
				{
					if(currentChar >= rowList.get(currentRow).length())
					{
						currentRow++;
						currentChar = 0;
						if(currentRow >= rowList.size())
						{
							done = true;
							if(msgQueue.get(0).getOption() == Options.YesNo)
								openYesNo();
						}						
						else
						{
							partialRow = "" + rowList.get(currentRow).charAt(currentChar);		
							textBoxes.get(currentRow).text = partialRow;
						}
							
					}
					else
					{
						partialRow += rowList.get(currentRow).charAt(currentChar);
						textBoxes.get(currentRow).text = partialRow;
					}
				}
				else
				{
					done = true;
					if(msgQueue.get(0).getOption() == Options.YesNo)
						openYesNo();
				}
				
				currentChar++;
				
				//Global.playSound("dlg");
			}
			
		}			
	}
	
	private void rowSplit()
	{
		float rowWidth = 0.0F;
		String tempStr = "";
		int i = 0;
		
		float[] widths = new float[1];		
		txtPaint.getTextWidths(" ", widths);
		float spaceWidth = widths[0];
		
		widths = new float[msg.length()];		
		txtPaint.getTextWidths(msg, widths);	
		
		List<String> words = new ArrayList<String>();
		List<Float> wordWidths = new ArrayList<Float>();
		
		//split into words
		for (i = 0;i < msg.length(); ++i)
		{		
			if(msg.charAt(i) == ' ')
			{
				addWord(tempStr, rowWidth, words, wordWidths);
				tempStr = "";
				rowWidth = 0;
							
			}	
			else
			{
				tempStr += msg.charAt(i);
				rowWidth += widths[i];
			}
		}
		if(msg.charAt(i-1) != ' ')
		{
			addWord(tempStr, rowWidth, words, wordWidths);
		}
		
		//reset for rowsplit		
		tempStr = "";
		rowWidth = 0;
		i = 0;
		
		//split into rows		
		for(Float f : wordWidths)
		{	
			if(i < words.size())
			{
				if(rowWidth + f + spaceWidth <= openSize.x - 16 && (words.get(i).length() > 0 && words.get(i).charAt(0) != '\n'))
				{
					rowWidth += (f + spaceWidth);
					if(tempStr.length() > 0)
						tempStr += " ";
					
					tempStr += words.get(i);
				}
				else
				if(rowList.size()< rowCount)
				{				
					String s;
					boolean newline = false;
					if(tempStr.charAt(0) == '\n')
					{
						s = tempStr.substring(1);
						newline = true;
					}
					else
						s = tempStr;				
					
					rowList.add(s);
					tempStr = words.get(i);
					rowWidth = wordWidths.get(i);
					
					if(newline)
						rowWidth -= spaceWidth;
					
				}			
				i++;
			}
						
		}
		if(rowList.size()< rowCount && tempStr.length() > 0)
		{
			if(tempStr.charAt(0) == '\n')	
				rowList.add(tempStr.substring(1));
			else
				rowList.add(tempStr);
		}
					
		
	}
	
	private void addWord(String tempStr, float rowWidth, List<String> words, List<Float> wordWidths)
	{
		if(tempStr.length() > 0)
		{
			String newStr = hasCharName ? replaceCharNames(tempStr) : tempStr;
			words.add(newStr);
			
			//if string was changed, use different widths
			if(newStr.equals(tempStr))						
				wordWidths.add(rowWidth);
			else
			{
				float[] newWidths = new float[newStr.length()];
				txtPaint.getTextWidths(newStr, newWidths);
				float newWidth = 0;
				for(float f : newWidths)newWidth += f;
				wordWidths.add(newWidth);
					
			}
				
		}
	}
	
	private String replaceCharNames(String str)
	{
		if(str.indexOf(wildCard) != -1)
		{
			int first = str.indexOf(wildCard);
			int last  = str.lastIndexOf(wildCard);
			String charName = str.substring(first+1, last);
			
			PlayerCharacter c = null;
			//search immediate party first
			for(PlayerCharacter ch : Global.party.getPartyList(true))
			{
				if(ch.getName().equals(charName))
				{
					c = ch;
					break;
				}
			}
			//else get default name
			if(c == null)
				c = Global.characters.get(charName);
			
			if(str.contains("party"))
			{
				int index = Integer.parseInt(charName.substring(charName.length() - 1)) - 1;
				List<PlayerCharacter> party = Global.party.getPartyList(false);
				if(party.size() > index)
					c = party.get(index);
			}
			
			
			if(c == null)
				//didnt find a character, return original string
				return str;
			else
			{
				//put moneysigns back before replacing
				charName = ""+wildCard+charName+wildCard;
				//return modified word, maintaining any punctuation or characters following
				return str.replace(charName, c.getDisplayName());				
			}
			
		}
		else
			return str;
	}
	
	public void touchActionMove(int x, int y)
	{
		if(Opened())
		{
			if(yesNoMenu.Opened())
				yesNoMenu.touchActionMove(x, y);
		}
	}
	
	public void touchActionUp(int x, int y)
	{
		if(Opened() && !timed)
		{
			if(!yesNoMenu.Closed())
			{
				LBStates state = yesNoMenu.touchActionUp(x, y);
				if(state == LBStates.Selected)
				{
					yesNoMenu.close();
					close();
					
					MsgAction result = (MsgAction)yesNoMenu.getSelectedEntry().obj;	
					if(result != null)
						result.execute();
				}
			}				
			else
			{
				if(done)
				{
					nextMessage();
				}
				else
					setSpeed(0);
			}
			
		}
	}
	
	public void touchActionDown(int x, int y)
	{
		if(Opened())
		{
			setSpeed(0);
			if(yesNoMenu.Opened())
				yesNoMenu.touchActionDown(x, y);
		}
		
	}


}
