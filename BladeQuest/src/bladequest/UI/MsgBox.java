package bladequest.UI;

import android.graphics.*;
import android.graphics.Paint.Align;
import bladequest.UI.ListBox.LBStates;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

import java.util.*;

public class MsgBox extends MenuPanel
{
	private String msg;

	private Paint txtPaint, txtPaintCenter;	
	private static int buffer = 6;
	public static int msgBoxHeight = 96;
	
	private List<String> rowList, msgQueue;
	
	private int textSpeed;
	private int textTimer;
	private int currentRow;
	private int currentChar;
	private String partialRow;
	private boolean done;
	
	private ListBox yesNoMenu;
	private boolean YesNoOpt, hasCharName;
	private YesNo selectedOption;
	
	private int rowCount;
	
	public boolean alwaysSpeed0;

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
		
		yesNoMenu = new ListBox(Global.vpWidth/2, Global.vpHeight - height - buffer*2, 0, 40, 1, 2, txtPaintCenter);
		yesNoMenu.anchor = Anchors.BottomCenter;
		yesNoMenu.setOpenSize(openSize.x, 40);
		yesNoMenu.openSpeed = 30;
		yesNoMenu.addItem("Yes", YesNo.Yes, false);
		yesNoMenu.addItem("No", YesNo.No, false);
		
		rowList = new ArrayList<String>();		
		msgQueue = new ArrayList<String>();	
		alwaysSpeed0 = false;
		//clear(msg);		
	}
	
	
	
	public void addMessage(String msg)
	{
		addMessage(msg, false);
	}
	public void addMessage(String msg, boolean YesNo)
	{
		msgQueue.add(msg);
		this.YesNoOpt = YesNo;
	}
	
	private void clear(String str)
	{
		this.msg = str;
		hasCharName = str.indexOf('$') != -1;
		for(int i = 0; i < rowCount; ++i)
			textBoxes.get(i).text = "";
		
		rowList.clear();
		
		textSpeed = alwaysSpeed0 ? 0 : 6 - Global.textSpeed;
		textTimer = 0;
		currentRow = 0;
		currentChar = 0;
		partialRow = "";
		
		done = false;
		
		//close();		
		rowSplit();
		
	}
	
	//forces next message
	public void nextMessage()
	{
		Global.delay();
		msgQueue.remove(0);
		if(msgQueue.size() > 0)					
			clear(msgQueue.get(0));
		else					
			close();
	}
	
	private void setSpeed(int s){textSpeed = s;}	
	public boolean isDone(){return done;}
	public YesNo getSelectedOpt() { return selectedOption; }
	public boolean isYesNo() { return YesNoOpt; }
	
	@Override
	public void close()
	{
		super.close();	
		yesNoMenu.close();
		msgQueue.clear();
	}
	
	@Override
	public void open()
	{
		super.open();
		
		if(msgQueue.size() > 0)
			clear(msgQueue.get(0));	
		
		selectedOption = null;
		
		//yesNoMenu.open();
		
		Global.delay();
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
		
		if(!done  && Opened())
		{
			textTimer++;
			if(textTimer >= textSpeed)
			{
				textTimer = 0;
				
				if(currentChar >= rowList.get(currentRow).length())
				{
					currentRow++;
					currentChar = 0;
					if(currentRow >= rowList.size())
					{
						done = true;
						if(YesNoOpt && msgQueue.size() <= 1)
							yesNoMenu.open();
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
				currentChar++;
				
				Global.playSound("dlg");
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
		if(str.indexOf('$') != -1)
		{
			int first = str.indexOf('$');
			int last  = str.lastIndexOf('$');
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
			
			
			if(c == null)
				//didnt find a character, return original string
				return str;
			else
			{
				//put moneysigns back before replacing
				charName = "$"+charName+"$";
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
		if(Opened())
		{
			if(!yesNoMenu.Closed())
			{
				LBStates state = yesNoMenu.touchActionUp(x, y);
				if(state == LBStates.Selected)
				{
					selectedOption = (YesNo)yesNoMenu.getSelectedEntry().obj;
					yesNoMenu.close();
					close();
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
			if(yesNoMenu.Opened())
				yesNoMenu.touchActionDown(x, y);
		}
		
	}
	
	public enum YesNo
	{
		Yes,
		No
	}

}
